package com.xerago.rsa.service;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import com.xerago.rsa.util.*;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord.NULL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.gson.Gson;
import com.valuemomentum.plm.parser.Coverage;
import com.valuemomentum.plm.parser.Deductibles;
import com.valuemomentum.plm.parser.Limits;
import com.valuemomentum.plm.parser.Product;
import com.valuemomentum.plm.parser.TwoWheelerQuoteParser;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.dao.UserServicesDAO;
import com.xerago.rsa.domain.AgentPlanMapping;
import com.xerago.rsa.domain.DElectricalAccessoriesLimit;
import com.xerago.rsa.domain.DNonElecAccessoriesLimit;
import com.xerago.rsa.domain.DPolicyAttributes;
import com.xerago.rsa.domain.DPolicyCoverages;
import com.xerago.rsa.domain.DPolicyDetails;
import com.xerago.rsa.domain.DPolicyPricingElements;
import com.xerago.rsa.domain.DPolicyVehicleDetails;
import com.xerago.rsa.domain.DPoscodeDetails;
import com.xerago.rsa.domain.DPoscodeMaster;
import com.xerago.rsa.domain.DQuoteDetails;
import com.xerago.rsa.domain.DTPExistingPolicyDetails;
import com.xerago.rsa.domain.DTppPricingElements;
import com.xerago.rsa.domain.DUpdatedClientDetails;
import com.xerago.rsa.domain.LongtermRateMaster;
import com.xerago.rsa.domain.cassandra.DMicroServiceDetails;
import com.xerago.rsa.domain.cassandra.DMicroServiceDetailsKey;
import com.xerago.rsa.dto.request.AadhaarRequest;
import com.xerago.rsa.dto.response.AadhaarResponse;
import com.xerago.rsa.dto.response.ApiResponse;
import com.xerago.rsa.dto.response.Liability;
import com.xerago.rsa.dto.response.OdPremium;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.PremiumDetailsData;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.dto.response.VahanResponse;
import com.xerago.rsa.model.ElectronicValues;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.model.NonElectronicValues;
import com.xerago.rsa.model.TppPricingValuesModel;
import com.xerago.rsa.util.jms.EmailMessageSender;

import de.codecentric.boot.admin.model.Application;

@Service
public abstract class Process implements Constants {

	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Autowired
	CassandraOperations cassandraOperations;
	
	@Autowired
	TwoWheelerQuoteParser quoteParser;
	
	@Autowired
	private TpSurveyorService TpSurveyorService;
	
	@Autowired
	EmailMessageSender emailMessageSender;
	
	@Value("${com.xerago.rsa.user}")
	private String d2cUserServiceUrl;
	
	@Autowired
	@Qualifier("ObjectMapperWithoutRoot")
	ObjectMapper objectMapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserServicesDAO userServicesDAO;
	
	@Autowired
	private SMSSender smsSender;
	
	private static final Logger LOGGER = LogManager.getRootLogger();

	private static final String SUB_LINE = "motorCycle";

	private static final String LINE = "PublicMotor";

	private static final String BUSINESS_STATUS_DRAFT = "draft";

	private static final String BUSINESS_STATUS_QUOTE = "quote";
	private static final String INCEPTION_TIME = "00:00";
	private static final String BRANCH_CODE = "X0";
	private static final String AGENTS = "Agents";
	private static final String INTERNET = "Internet";

	Map<String, String> hashCodemap = new HashMap<String, String>();
	
	public PremiumDetails calculatePremium(MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		
		MotorValidation motorValidation = new MotorValidation();

		Product product;

		/**
		 * For first year bulk discount and nil intermediation discount not applicable
		 */
		if(motorInsuranceModel.getPolicyTerm()!= 1) twoWheelerDAO.getBulkRateNilIntermediatonRate(motorInsuranceModel);
		
		product = twoWheelerDAO.loadMandatoryCoverages(motorInsuranceModel.getTypeOfCover(), motorInsuranceModel);

		LOGGER.info("product.getCoverages() :::: " + product.getCoverages().size());
		LOGGER.info("calculatePremium-VehicleModelCode:::: " + motorInsuranceModel.getVehicleModelCode());
		
		Ifoundry.setSelectedLimitValues(product, motorInsuranceModel);
		Ifoundry.setlstCoverages(product, motorInsuranceModel);

		motorInsuranceModel.setProduct(product);

		// Technical Discount calculation

		String productName = product.getProductName();
		double technicalDiscount = 0;
		try {
			
			technicalDiscount = twoWheelerDAO.getTechnicalDiscount(motorInsuranceModel, productName);
			
			motorInsuranceModel.setTechnicalDiscount(technicalDiscount);
			LOGGER.info("calculatePremium-quoteParser-VehicleAge::" + motorInsuranceModel.getVehicleAge());
			product = quoteParser.invokeRating(product, motorInsuranceModel);
			
			LOGGER.info("invokeRating Done");

			TppPricingValuesModel tppPricingValuesModel = setPremiumDetails(motorInsuranceModel,product);

			DQuoteDetails quoteDetails = setDQuoteDetails(motorInsuranceModel);
			
			LOGGER.info("AgentId: " + motorInsuranceModel.getAgentId());

			Date dtInception = null;
			Date dtExpiry = null;

			motorInsuranceModel.getQuoteId();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
			dtInception = simpleDateFormat.parse(motorInsuranceModel.getPolicyStartDate());
			dtExpiry = simpleDateFormat.parse(motorValidation.setExpiryDate(motorInsuranceModel));
			new java.sql.Date(dtInception.getTime());
			new java.sql.Date(dtExpiry.getTime());

			DPolicyDetails dPolicyDetails = null;
			DPolicyDetails policyDetails  = setDPolicyDetails(motorInsuranceModel, quoteDetails, dtInception, dtExpiry, dPolicyDetails);

			List<DPolicyAttributes> policyAttributesList = setDPolicyAttributes(product, policyDetails);

			List<DPolicyCoverages> policyCoveragesList = setDPolicyCoverages(motorInsuranceModel, product,
					policyDetails);
			
			List<DPolicyPricingElements> policyPricingelementsList = setDPolicyPricingElements(product, policyDetails);
			
			List<DElectricalAccessoriesLimit> dElectricalAccessoriesLimitList = setDElectricalAccessoriesLimit(motorInsuranceModel, policyDetails);
		
			List<DNonElecAccessoriesLimit> dNonElecAccessoriesLimitList = setDNonElecAccessoriesLimit(motorInsuranceModel, policyDetails);
			DPolicyVehicleDetails dPolicyVehicleDetail = null;
			DPolicyVehicleDetails dPolicyVehicleDetails = setDPolicyVehicelDetails(motorInsuranceModel, policyDetails, dPolicyVehicleDetail);
			
			DUpdatedClientDetails updatedClientDetails = setDUpdatedClientDetails(motorInsuranceModel, quoteDetails);
			LOGGER.info("updatedClientDetails - "+updatedClientDetails.getQuoteId()+", "+updatedClientDetails.getId());

			List<DTppPricingElements> tppPricingElementsList = setTppPricingElements(motorInsuranceModel, tppPricingValuesModel, quoteDetails);
			
			DPoscodeDetails dPoscodeDetails = setDPoscodeDetails(motorInsuranceModel,quoteDetails);
			// DTPExistingPolicyDetails
			setDTPExistingPolicyDetails(motorInsuranceModel);
			
			dbInsertion(motorInsuranceModel, premiumDetails, status, quoteDetails, policyDetails, policyAttributesList,
					policyCoveragesList, policyPricingelementsList, dElectricalAccessoriesLimitList,
					dNonElecAccessoriesLimitList, dPolicyVehicleDetails, updatedClientDetails, tppPricingElementsList,dPoscodeDetails);
		} catch (RemoteException ex) {
		    if(ex instanceof AxisFault){
		    	LOGGER.info("Axis Fault error: " + ((AxisFault)ex).getFaultAction());
		    	status.setMessage("Premium Calculated Failure");
				status.setStatusCode("E-0003");
				premiumDetails.setData(null);
				premiumDetails.setStatus(status);;
		    }
		}  catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			status.setMessage("Premium Calculated Failure");
			status.setStatusCode("E-0003");
			premiumDetails.setData(null);
			premiumDetails.setStatus(status);
			LOGGER.info("*********Failure*********" + e);
			LOGGER.info("Exception-invokeRating::" + e);

		}
		/*
		 * Proposal Mail creation
		 */
		return premiumDetails;
	}

	/**
	 * <p> This Entity is only applicable for ROlloverCar - typeOfCover - Standalone.</p>
	 * @param motorInsuranceModel
	 * @throws ParseException
	 * @author roshini
	 * @since 2019-06-17
	 */
	protected void setDTPExistingPolicyDetails(MotorInsuranceModel motorInsuranceModel) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		if("StandAlone".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()) && "updateVehicleDetails".equalsIgnoreCase(motorInsuranceModel.getProcessType()) ) {
			DTPExistingPolicyDetails dTPExistingPolicyDetails = twoWheelerDAO.get(DTPExistingPolicyDetails.class, motorInsuranceModel.getQuoteId());
			if(dTPExistingPolicyDetails != null) {
				dTPExistingPolicyDetails.setTpPolicyNumber(motorInsuranceModel.getTpPolicyNumber());
				dTPExistingPolicyDetails.setTpPolicyTerm(motorInsuranceModel.getTpPolicyTerm());
				dTPExistingPolicyDetails.setTpInsurerName(motorInsuranceModel.getTpInsurer());
				dTPExistingPolicyDetails.setTpInceptionDate(simpleDateFormat.parse(motorInsuranceModel.getTpInceptionDate()));
				dTPExistingPolicyDetails.setTpExpiryDate(simpleDateFormat.parse(motorInsuranceModel.getTpExpiryDate()));
				dTPExistingPolicyDetails.setTpAddress1(motorInsuranceModel.getTpAddress1());
				dTPExistingPolicyDetails.setTpAddress2(motorInsuranceModel.getTpAddress2());
				dTPExistingPolicyDetails.setTpCity(motorInsuranceModel.getTpCity());
				dTPExistingPolicyDetails.setTpState(motorInsuranceModel.getTpState());
				dTPExistingPolicyDetails.setTpPincode(motorInsuranceModel.getTpPincode());
				twoWheelerDAO.saveOrUpdate(dTPExistingPolicyDetails);
			} else {
				dTPExistingPolicyDetails = new DTPExistingPolicyDetails();
				dTPExistingPolicyDetails.setQuoteId(motorInsuranceModel.getQuoteId());
				dTPExistingPolicyDetails.setTpPolicyNumber(motorInsuranceModel.getTpPolicyNumber());
				dTPExistingPolicyDetails.setTpPolicyTerm(motorInsuranceModel.getTpPolicyTerm());
				dTPExistingPolicyDetails.setTpInsurerName(motorInsuranceModel.getTpInsurer());
				dTPExistingPolicyDetails.setTpInceptionDate(simpleDateFormat.parse(motorInsuranceModel.getTpInceptionDate()));
				dTPExistingPolicyDetails.setTpExpiryDate(simpleDateFormat.parse(motorInsuranceModel.getTpExpiryDate()));
				dTPExistingPolicyDetails.setTpAddress1(motorInsuranceModel.getTpAddress1());
				dTPExistingPolicyDetails.setTpAddress2(motorInsuranceModel.getTpAddress2());
				dTPExistingPolicyDetails.setTpCity(motorInsuranceModel.getTpCity());
				dTPExistingPolicyDetails.setTpState(motorInsuranceModel.getTpState());
				dTPExistingPolicyDetails.setTpPincode(motorInsuranceModel.getTpPincode());
				twoWheelerDAO.save(dTPExistingPolicyDetails);
			}
		} else {
			DTPExistingPolicyDetails dTPExistingPolicyDetails = twoWheelerDAO.get(DTPExistingPolicyDetails.class, motorInsuranceModel.getQuoteId());
			if(dTPExistingPolicyDetails != null) {
				twoWheelerDAO.delete(dTPExistingPolicyDetails);
			}
		}
		
	}
	/**
	 * 
	 * @param motorInsuranceModel
	 * @param quoteDetails
	 * @return
	 */

	protected DPoscodeDetails setDPoscodeDetails(MotorInsuranceModel motorInsuranceModel, DQuoteDetails quoteDetails) {
		DPoscodeDetails dPoscodeDetails = null ; 
		if("Yes".equalsIgnoreCase(motorInsuranceModel.getIsPosOpted())){
			
			 dPoscodeDetails = new DPoscodeDetails();
			if(StringUtils.isBlank(motorInsuranceModel.getPosCode())){		
				dPoscodeDetails.setQuoteId(motorInsuranceModel.getQuoteId());
				dPoscodeDetails.setPosName(motorInsuranceModel.getPosName());
				dPoscodeDetails.setPosPan(motorInsuranceModel.getPosPAN());
				dPoscodeDetails.setPosAadhaar(motorInsuranceModel.getPosAadhaar());
				dPoscodeDetails.setPosMobile(motorInsuranceModel.getPosMobile());
				dPoscodeDetails.setPosLicenceExpDate(motorInsuranceModel.getPosLicenceExpDate());
			}else{
				quoteDetails.setPosCode(motorInsuranceModel.getPosCode());
				DPoscodeMaster dPoscodeMaster = twoWheelerDAO.get(DPoscodeMaster.class, motorInsuranceModel.getPosCode());
				if(dPoscodeMaster != null){
					dPoscodeDetails.setQuoteId(motorInsuranceModel.getQuoteId());
					dPoscodeDetails.setPosName(dPoscodeMaster.getPosName());
					dPoscodeDetails.setPosPan(dPoscodeMaster.getPosPan());
					dPoscodeDetails.setPosAadhaar(dPoscodeMaster.getPosAadhaar());
					dPoscodeDetails.setPosMobile(dPoscodeMaster.getPosMobile());
					dPoscodeDetails.setPosLicenceExpDate(dPoscodeMaster.getPosLicenceExpDate());
				}
			}
		}
		
		return dPoscodeDetails;
	}




	/**
	 * @param motorInsuranceModel
	 * @param premiumDetails
	 * @param status
	 * @param quoteDetails
	 * @param policyDetails
	 * @param policyAttributesList
	 * @param policyCoveragesList
	 * @param policyPricingelementsList
	 * @param dElectricalAccessoriesLimitList
	 * @param dNonElecAccessoriesLimitList
	 * @param dPolicyVehicleDetails
	 * @param updatedClientDetails
	 * @param tppPricingElementsList
	 * @throws Exception
	 * @throws NumberFormatException
	 * @throws JMSException
	 * @throws IOException
	 */
	public void dbInsertion(MotorInsuranceModel motorInsuranceModel, PremiumDetails premiumDetails, Status status,
			DQuoteDetails quoteDetails, DPolicyDetails policyDetails, List<DPolicyAttributes> policyAttributesList,
			List<DPolicyCoverages> policyCoveragesList, List<DPolicyPricingElements> policyPricingelementsList,
			List<DElectricalAccessoriesLimit> dElectricalAccessoriesLimitList,
			List<DNonElecAccessoriesLimit> dNonElecAccessoriesLimitList, DPolicyVehicleDetails dPolicyVehicleDetails,
			DUpdatedClientDetails updatedClientDetails, List<DTppPricingElements> tppPricingElementsList, DPoscodeDetails dPoscodeDetails)
			throws Exception, NumberFormatException, JMSException, IOException {

		LOGGER.info("motorInsuranceModel.getSource() ::: " + motorInsuranceModel.getSource());
		Integer returnCode = 0;
		if(StringUtils.isBlank(motorInsuranceModel.getSource()) || "Microsite".equalsIgnoreCase(motorInsuranceModel.getSource())
				|| "STP".equalsIgnoreCase(motorInsuranceModel.getSource()) ) {
			returnCode = twoWheelerDAO.setInsetData(motorInsuranceModel.isQuoteExistingDB(), quoteDetails,
					policyDetails, policyAttributesList, policyCoveragesList, policyPricingelementsList,
					dPolicyVehicleDetails, dElectricalAccessoriesLimitList, dNonElecAccessoriesLimitList,
					updatedClientDetails, tppPricingElementsList,motorInsuranceModel.getProcessType(),dPoscodeDetails, motorInsuranceModel.getSource());
		} else {
			DMicroServiceDetails dMicroServiceDetails = new DMicroServiceDetails(new DMicroServiceDetailsKey(motorInsuranceModel.getQuoteId(), new Date()));
			dMicroServiceDetails.setdQuoteDetails(quoteDetails);
			dMicroServiceDetails.setdPolicyDetails(policyDetails);
			dMicroServiceDetails.setdPolicyAttributes(policyAttributesList);
			dMicroServiceDetails.setdPolicyCoverages(policyCoveragesList);
			dMicroServiceDetails.setdPolicyPricingElements(policyPricingelementsList);
			dMicroServiceDetails.setdElectricalAccessoriesLimit(dElectricalAccessoriesLimitList);
			dMicroServiceDetails.setdNonElecAccessoriesLimit(dNonElecAccessoriesLimitList);
			dMicroServiceDetails.setdPolicyVehicleDetails(dPolicyVehicleDetails);
			dMicroServiceDetails.setdUpdatedClientDetails(updatedClientDetails);
			dMicroServiceDetails.setdTPPPricingElements(tppPricingElementsList);
			cassandraOperations.insert(dMicroServiceDetails);	
			returnCode = 1;
		} 
		
		LOGGER.info("**********returnCode ***" + returnCode);
		
		if (returnCode == 1) {
			
			setResponseDetails(motorInsuranceModel, premiumDetails, status, updatedClientDetails,policyDetails);
			
		} else {
			status.setStatusCode("E-0002");
			status.setMessage("Premium Calculated Failed");
			premiumDetails.setData(null);
		}
	}


	/**
	 * @param motorInsuranceModel
	 * @param premiumDetails
	 * @param status
	 * @throws NumberFormatException
	 * @throws JMSException
	 * @throws IOException
	 */
	public void setResponseDetails(MotorInsuranceModel motorInsuranceModel, PremiumDetails premiumDetails,
			Status status, DUpdatedClientDetails dUpdatedClientDetails,DPolicyDetails policyDetails) throws Exception {
		PremiumDetailsData premiumDetailsData = new PremiumDetailsData();
		
		OdPremium odPremium = setODPremiumDetails(motorInsuranceModel, premiumDetailsData);
		
		Liability liability = setLiabilityPremiumDetails(motorInsuranceModel, premiumDetailsData);
		
		VahanResponse vahanResponse= setVahanServiveResponse(motorInsuranceModel, premiumDetailsData);	
		LOGGER.info("policyDetails.getIsBreakinInsurance() = "+policyDetails.getIsBreakinInsurance());
		setPremiumDetailsData(motorInsuranceModel, premiumDetails, premiumDetailsData, odPremium, liability,policyDetails,status);
		if(!"1".equalsIgnoreCase(policyDetails.getIsBreakinInsurance())){
			setPremiumDetailsStatus(premiumDetails, status, motorInsuranceModel, dUpdatedClientDetails);
		}
	}

	

	/**
	 * @param premiumDetails
	 * @param status
	 * @throws Exception 
	 */
	public void setPremiumDetailsStatus(PremiumDetails premiumDetails, Status status, MotorInsuranceModel motorInsuranceModel, DUpdatedClientDetails dUpdatedClientDetails) throws Exception {
		LOGGER.info("premiumDetails");
	
		
	}


	/**
	 * @param motorInsuranceModel
	 * @param premiumDetails
	 * @param premiumDetailsData
	 * @param odPremium
	 * @param liability
	 * @throws NumberFormatException
	 */
	public void setPremiumDetailsData(MotorInsuranceModel motorInsuranceModel, PremiumDetails premiumDetails,
			PremiumDetailsData premiumDetailsData, OdPremium odPremium, Liability liability,DPolicyDetails policyDetails,Status status)
			throws NumberFormatException {
		LOGGER.info("liability.getTotalLiabilityPremium() ::: " + liability.getTotalLiabilityPremium());
		LOGGER.info("odPremium.getTotalOdPremium() ::: " + odPremium.getTotalOdPremium());
		
		double totalpremium = 0;
		if (odPremium.getTotalOdPremium() == null) {
			totalpremium = Double.parseDouble(liability.getTotalLiabilityPremium());
		} else {
			totalpremium = Double.parseDouble(liability.getTotalLiabilityPremium())
					+ Double.parseDouble(odPremium.getTotalOdPremium());
		}
		premiumDetailsData.setPackagePremium(Double.toString(totalpremium));
		premiumDetailsData.setQuoteId(motorInsuranceModel.getPremiumBreakUpMap().get("quoteId"));
		premiumDetailsData.setPolicyStartDate(motorInsuranceModel.getPolicyStartDate());
		premiumDetailsData.setPolicyExpiryDate(motorInsuranceModel.getPolicyExpiryDate());
		premiumDetailsData.setOdStartDate(motorInsuranceModel.getPolicyStartDate());
		premiumDetailsData.setOdExpiryDate(motorInsuranceModel.getOwnDamageExpiryDate());
		premiumDetailsData.setGrossPremium(String.valueOf(motorInsuranceModel.getPremium()));
		premiumDetailsData.setPremiumWithoutCovers(String.valueOf(motorInsuranceModel.getPremiumWithOutAddOnCov()));
		premiumDetailsData.setPolicyTerm(motorInsuranceModel.getPolicyTerm() );
		premiumDetailsData.setLiabilityPolicyTerm(String.valueOf(motorInsuranceModel.getLiabilityPolicyTerm()));
		premiumDetailsData.setVersionNo("1.0");
		premiumDetailsData.setPremium(String.valueOf(motorInsuranceModel.getPremium()));
		premiumDetailsData.setTechnicalDiscount(String.valueOf(motorInsuranceModel.getTechnicalDiscount()));
		premiumDetailsData.setCampaignDiscount(String.valueOf(motorInsuranceModel.getCampaignDiscount()));
		/*if (motorInsuranceModel.getPolicyTerm() != 0) {
			if (motorInsuranceModel.getPolicyTerm() > 1) {
				premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor1Year());
				premiumDetailsData.setIdvFor1stYear(String.valueOf(motorInsuranceModel.getIdvFor1Year()));
				premiumDetailsData.setIdvFor2ndYear(String.valueOf(motorInsuranceModel.getIdvFor2Year()));
				if (motorInsuranceModel.getPolicyTerm() > 2) {
					premiumDetailsData.setIdvFor3rdYear(String.valueOf(motorInsuranceModel.getIdvFor3Year()));
					if (motorInsuranceModel.getPolicyTerm() > 3) {
						premiumDetailsData.setIdvFor4thYear(String.valueOf(motorInsuranceModel.getIdvFor4Year()));
						if (motorInsuranceModel.getPolicyTerm() > 4) {
							premiumDetailsData.setIdvFor5thYear(String.valueOf(motorInsuranceModel.getIdvFor5Year()));
						}
					}
				}

			}
		}*/
		//Task #184555 Two Wheeler WS tags			
		premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
		if(!"BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
			LOGGER.info("getIDV1 ::: "+motorInsuranceModel.getIdv());
			if(motorInsuranceModel.getPolicyTerm() >= 1) {
				premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor1Year());
				premiumDetailsData.setIdvFor1stYear(String.valueOf(motorInsuranceModel.getModifiedIdvfor1Year()));
				if(motorInsuranceModel.getPolicyTerm() >= 2) {
					premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor2Year());
					premiumDetailsData.setIdvFor2ndYear(String.valueOf(motorInsuranceModel.getModifiedIdvfor2Year()));
					if(motorInsuranceModel.getPolicyTerm() >= 3) {
						premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor3Year());
						premiumDetailsData.setIdvFor3rdYear(String.valueOf(motorInsuranceModel.getModifiedIdvfor3Year()));
						if(motorInsuranceModel.getPolicyTerm() >= 4) {
							premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor4Year());
							premiumDetailsData.setIdvFor4thYear(String.valueOf(motorInsuranceModel.getModifiedIdvfor4Year()));
							if(motorInsuranceModel.getPolicyTerm() >= 5) {
								premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor5Year());
								premiumDetailsData.setIdvFor5thYear(String.valueOf(motorInsuranceModel.getModifiedIdvfor5Year()));
							}
						}
					}
				}
			}
		} else {
			LOGGER.info("getIDV2 ::: "+motorInsuranceModel.getIdv());
			if(motorInsuranceModel.getPolicyTerm() >= 1) {
				premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor1Year());
				premiumDetailsData.setIdvFor1stYear(String.valueOf(motorInsuranceModel.getIdvFor1Year()));
				if(motorInsuranceModel.getPolicyTerm() >= 2) {
					premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor2Year());
					premiumDetailsData.setIdvFor2ndYear(String.valueOf(motorInsuranceModel.getIdvFor2Year()));
					if(motorInsuranceModel.getPolicyTerm() >= 3) {
						premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor3Year());
						premiumDetailsData.setIdvFor3rdYear(String.valueOf(motorInsuranceModel.getIdvFor3Year()));
						if(motorInsuranceModel.getPolicyTerm() >= 4) {
							premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor4Year());
							premiumDetailsData.setIdvFor4thYear(String.valueOf(motorInsuranceModel.getIdvFor4Year()));
							if(motorInsuranceModel.getPolicyTerm() >= 5) {
								premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor5Year());
								premiumDetailsData.setIdvFor5thYear(String.valueOf(motorInsuranceModel.getIdvFor5Year()));
							}
						}
					}
				}
			}
			LOGGER.info("getProductName::: "+premiumDetailsData.getIdv());
		}
		
		premiumDetailsData.setTaxType(motorInsuranceModel.getTaxType());
		if ("GST".equalsIgnoreCase(motorInsuranceModel.getTaxType())) {
			
			premiumDetailsData.setServiceTax("0");
			premiumDetailsData.setEcess("");
			premiumDetailsData.setKrishiCess("");
			
			premiumDetailsData.setIgst(motorInsuranceModel.getIgst() + "");
			premiumDetailsData.setCgst(motorInsuranceModel.getCgst() + "");
			premiumDetailsData.setUtgst(motorInsuranceModel.getUtgst() + "");
			premiumDetailsData.setSgst(motorInsuranceModel.getSgst() + "");
			premiumDetailsData.setKeralaFloodCess(motorInsuranceModel.getKeralaFloodCess()+"");
			
		} else {
			premiumDetailsData.setServiceTax(motorInsuranceModel.getPremiumBreakUpMap().get("ServiceeTax"));
			premiumDetailsData.setEcess(motorInsuranceModel.getPremiumBreakUpMap().get("EducationCess"));
			premiumDetailsData.setKrishiCess(motorInsuranceModel.getPremiumBreakUpMap().get("KrishiKalyan"));
			
			premiumDetailsData.setIgst("0");
			premiumDetailsData.setCgst("0");
			premiumDetailsData.setUtgst("0");
			premiumDetailsData.setSgst("0");
		}
		/*[Task #228791] Break-In Insurance for Two Wheeler - TECH*/
		if (("calculatePremium".equalsIgnoreCase(motorInsuranceModel.getProcessType()) 
				|| ("updateVehicleDetails".equalsIgnoreCase(motorInsuranceModel.getProcessType())))
				&& !"RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId())
						&& !"BR292000".equalsIgnoreCase(motorInsuranceModel.getAgentId())
						&& "1".equalsIgnoreCase(policyDetails.getIsBreakinInsurance())) {
			status.setStatusCode("S-0002");
			status.setMessage("Premium calculated successfully. This quote is subject to Break in case hence request to have VIR approval code in next service call.");
			
		}
		/*end*/
		premiumDetails.setData(premiumDetailsData);
		premiumDetails.setStatus(status);
	}


	/**
	 * @param motorInsuranceModel
	 * @param premiumDetailsData
	 * @return
	 */
	public Liability setLiabilityPremiumDetails(MotorInsuranceModel motorInsuranceModel,
			PremiumDetailsData premiumDetailsData) {
		Liability liability = new Liability();
		liability.setBasicPremiumIncludingPremiumForTppd(
				motorInsuranceModel.getPremiumBreakUpMap().get("VMC_LiabilityCover"));
		liability.setBiFuelKitCng("0.0");
		liability.setPersonalAccidentBenefits("-");
		liability.setUnderSectionIIIOwnerDriver(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_PAOwnerDriverCover"));
		liability.setUnnamedPassengrs(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_PAUnnamed"));
		liability.setPaCoverToPaidDriver(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_PAPaidDriver"));
		liability.setToPaidDrivers(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_LLPaidDriverCover"));
		liability.setToEmployeses(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_LLEMPLOYEES"));
		liability.setTotalLiabilityPremium(motorInsuranceModel.getPremiumBreakUpMap().get("liability_premium"));
		premiumDetailsData.setLiability(liability);
		return liability;
	}


	/**
	 * @param motorInsuranceModel
	 * @param premiumDetailsData
	 * @return
	 */
	public OdPremium setODPremiumDetails(MotorInsuranceModel motorInsuranceModel,
			PremiumDetailsData premiumDetailsData) {
		//Setting OD_Premium
		OdPremium odPremium = new OdPremium();
		odPremium.setBasicPremiumAndNonElectricalAccessories(
				motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODBasicCover"));
		odPremium.setElectricalAccessories(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ElecAccessoriesCover"));
		odPremium.setBiFuelKit("0.0");
		odPremium.setFiberGlassTank("0.0");
		odPremium.setAutomobileAssociationDiscount(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_OwnDamageCover_pricingelement_AutoAssociationMembership"));
		odPremium.setVoluntaryDeductables(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODBasicCover_deductible_VoluntaryDeductible"));
		odPremium.setVoluntaryDeductable(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODCommercialCover_pricingelement_VoluntaryDed"));
		odPremium.setNoClaimBonus(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODCommercialCover_pricingelement_NoCliamDiscount"));
		odPremium.setDepreciationWaiver("0.0");
		odPremium.setEngineProtector("0.0");
		odPremium.setNcbProtector("0.0");
		odPremium.setWindShieldGlass("0.0");
		odPremium.setLifeTimeRoadTax("0.0");
		odPremium.setSpareCar("0.0");
		odPremium.setInvoicePriceInsurance("0.0");
		odPremium.setLossOfBaggage("0.0");
		odPremium.setTotalOdPremium(motorInsuranceModel.getPremiumBreakUpMap().get("od_premium"));
		odPremium.setKeyReplacement("0.0");
		odPremium.setBulkDealDiscountCoverPremium(String.valueOf(motorInsuranceModel.getBulkDealDiscountCoverPremium()));
		odPremium.setNilIntermediationCoverPremium(String.valueOf(motorInsuranceModel.getNilIntermediationCoverPremium()));
		odPremium.setTowingChargesCoverPremium(String.valueOf(motorInsuranceModel.getAdditionalTowingChargesCoverPremium()));
		premiumDetailsData.setOdPremium(odPremium);
		
		return odPremium;
	}


	/**
	 * @param motorInsuranceModel
	 * @param tppPricingValuesModel
	 * @param quoteDetails
	 * @return
	 */
	private List<DTppPricingElements> setTppPricingElements(MotorInsuranceModel motorInsuranceModel,
			TppPricingValuesModel tppPricingValuesModel, DQuoteDetails quoteDetails) {
		// DTppPricingElements

		List<DTppPricingElements> tppPricingElementsList = new ArrayList<DTppPricingElements>();
		DTppPricingElements tppPricingElements = new DTppPricingElements();
		tppPricingElements.setDQuoteDetails(quoteDetails);
		//tppPricingElements.setId(new BigDecimal("14.133333"));
		//tppPricingElements.setQuoteId(quoteDetails.getQuoteId());
		tppPricingElements.setVersionNo(new BigDecimal("14.133333"));
		tppPricingElements.setClientCode(motorInsuranceModel.getClientCode());
		tppPricingElements.setCapdiscount(tppPricingValuesModel.getCapDiscount());
		tppPricingElements.setCaploading(tppPricingValuesModel.getCapLoading());
		tppPricingElements.setOdtotalexpensemultiplier(tppPricingValuesModel.getODTotalExpenseMultiplier());
		tppPricingElements.setPremiumadjustmentpercentage(tppPricingValuesModel.getPremiumAdjustmentPercentage());
		tppPricingElements.setPremiumaftercapdiscount(tppPricingValuesModel.getPremiumAfterCapDiscount());
		tppPricingElements.setPremiumaftercaploading(tppPricingValuesModel.getPremiumAfterCapLoading());
		tppPricingElements.setPremiumaftertechdiscount(tppPricingValuesModel.getPremiumAfterTechDiscount());
		tppPricingElements.setThefttotalexpensemultiplier(tppPricingValuesModel.getTheftTotalExpenseMultiplier());
		tppPricingElements.setThirdpartyadjustedpremium(tppPricingValuesModel.getThirdPartyAdjustedPremium());
		tppPricingElements.setTptotalexpensemultiplier(tppPricingValuesModel.getTPTotalExpenseMultiplier());
		tppPricingElements.setVpcOdidvadjustor(tppPricingValuesModel.getVPC_ODIDVAdjustor());
		tppPricingElements.setVpcOdidvadjustorRate(tppPricingValuesModel.getVPC_ODIDVAdjustor_rate());
		tppPricingElements.setVpcOdleapoffaith(tppPricingValuesModel.getVPC_ODLeapofFaith());
		tppPricingElements.setVpcOdleapoffaithRate(tppPricingValuesModel.getVPC_ODLeapofFaith_rate());
		tppPricingElements.setVpcOdpremiuma(tppPricingValuesModel.getVPC_ODPremiumA());
		tppPricingElements.setVpcOdpremiumaRate(tppPricingValuesModel.getVPC_ODPremiumA_rate());
		tppPricingElements.setVpcOdpremiumb(tppPricingValuesModel.getVPC_ODPremiumB());
		tppPricingElements.setVpcOdpremiumbRate(tppPricingValuesModel.getVPC_ODPremiumB_rate());
		tppPricingElements.setVpcOdpremiumc(tppPricingValuesModel.getVPC_ODPremiumC());
		tppPricingElements.setVpcOdpremiumcRate(tppPricingValuesModel.getVPC_ODPremiumC_rate());
		tppPricingElements.setVpcOdpremiumd(tppPricingValuesModel.getVPC_ODPremiumD());
		tppPricingElements.setVpcOdstreetprice(tppPricingValuesModel.getVPC_ODStreetPrice());
		tppPricingElements.setVpcOdstreetpriceRate(tppPricingValuesModel.getVPC_ODStreetPrice_rate());
		tppPricingElements.setVpcTariffodpremium(tppPricingValuesModel.getVPC_TariffODPremium());
		tppPricingElements.setVpcTheftidvadjustor(tppPricingValuesModel.getVPC_TheftIDVAdjustor());
		tppPricingElements.setVpcTheftidvadjustorRate(tppPricingValuesModel.getVPC_TheftIDVAdjustor_rate());
		tppPricingElements.setVpcTheftleapoffaith(tppPricingValuesModel.getVPC_TheftLeapofFaith());
		tppPricingElements.setVpcTheftleapoffaithRate(tppPricingValuesModel.getVPC_TheftLeapofFaith_rate());
		tppPricingElements.setVpcTheftpremiuma(tppPricingValuesModel.getVPC_TheftPremiumA());
		tppPricingElements.setVpcTheftpremiumaRate(tppPricingValuesModel.getVPC_TheftPremiumA_rate());
		tppPricingElements.setVpcTheftpremiumb(tppPricingValuesModel.getVPC_TheftPremiumB());
		tppPricingElements.setVpcTheftpremiumbRate(tppPricingValuesModel.getVPC_TheftPremiumB_rate());
		tppPricingElements.setVpcTheftpremiumc(tppPricingValuesModel.getVPC_TheftPremiumC());
		tppPricingElements.setVpcTheftpremiumcRate(tppPricingValuesModel.getVPC_TheftPremiumC_rate());
		tppPricingElements.setVpcTheftpremiumd(tppPricingValuesModel.getVPC_TheftPremiumD());
		tppPricingElements.setVpcTheftstreetprice(tppPricingValuesModel.getVPC_TheftStreetPrice());
		tppPricingElements.setVpcTheftstreetpriceRate(tppPricingValuesModel.getVPC_TheftStreetPrice_rate());
		tppPricingElements.setVpcTpidvadjustor(tppPricingValuesModel.getVPC_TPIDVAdjustor());
		tppPricingElements.setVpcTpidvadjustorRate(tppPricingValuesModel.getVPC_TPIDVAdjustor_rate());
		tppPricingElements.setVpcTpleapoffaith(tppPricingValuesModel.getVPC_TPLeapofFaith());
		tppPricingElements.setVpcTpleapoffaithRate(tppPricingValuesModel.getVPC_TPLeapofFaith_rate());
		tppPricingElements.setVpcTppodpremium(tppPricingValuesModel.getVPC_TPPODPremium());
		tppPricingElements.setVpcTppremiuma(tppPricingValuesModel.getVPC_TPPremiumA());
		tppPricingElements.setVpcTppremiumadjustor(tppPricingValuesModel.getVPC_TPPremiumAdjustor());
		tppPricingElements.setVpcTppremiumadjustorRate(tppPricingValuesModel.getVPC_TPPremiumAdjustor_rate());
		tppPricingElements.setVpcTppremiumaRate(tppPricingValuesModel.getVPC_TPPremiumA_rate());
		tppPricingElements.setVpcTppremiumb(tppPricingValuesModel.getVPC_TPPremiumB());
		tppPricingElements.setVpcTppremiumbRate(tppPricingValuesModel.getVPC_TPPremiumB_rate());
		tppPricingElements.setVpcTppremiumc(tppPricingValuesModel.getVPC_TPPremiumC());
		tppPricingElements.setVpcTppremiumcRate(tppPricingValuesModel.getVPC_TPPremiumC_rate());
		tppPricingElements.setVpcTppremiumd(tppPricingValuesModel.getVPC_TPPremiumD());
		tppPricingElementsList.add(tppPricingElements);
		return tppPricingElementsList;
	}


	/**
	 * @param motorInsuranceModel
	 * @param quoteDetails
	 * @return
	 */
	protected DUpdatedClientDetails setDUpdatedClientDetails(MotorInsuranceModel motorInsuranceModel,
			DQuoteDetails quoteDetails) throws Exception {
		/*
		 * DUpdatedClientDetails Start
		 */

		DUpdatedClientDetails updatedClientDetails = new DUpdatedClientDetails();
		updatedClientDetails.setDQuoteDetails(quoteDetails);
		updatedClientDetails.setQuoteId(quoteDetails.getQuoteId());
		updatedClientDetails.setClientCode(motorInsuranceModel.getClientCode());
		
		/** Task #112360 TWO WHEELER FOR MY INSURANCE CLUB - 29/06/2016 */
		/*updatedClientDetails.setAddressLine1(motorInsuranceModel.getAddressOne());
		updatedClientDetails.setAddressLine2(motorInsuranceModel.getAddressTwo());
		updatedClientDetails.setAddressLine3(motorInsuranceModel.getAddressThree());
		updatedClientDetails.setAddressLine4(motorInsuranceModel.getAddressFour());
		updatedClientDetails.setPincode(motorInsuranceModel.getRegPinCode());
		updatedClientDetails.setCity(motorInsuranceModel.getRegCity());*/
		updatedClientDetails.setAddressLine1(motorInsuranceModel.getContactAddress1());
		updatedClientDetails.setAddressLine2(motorInsuranceModel.getContactAddress2());
		updatedClientDetails.setAddressLine3(motorInsuranceModel.getContactAddress3());
		updatedClientDetails.setAddressLine4(motorInsuranceModel.getContactAddress4());
		/**Task #138615 Two Wheeler – Mailing address*/
		updatedClientDetails.setCity(motorInsuranceModel.getContactCity());
		/**Task #138615 Two Wheeler – Mailing address*/
		updatedClientDetails.setPincode(motorInsuranceModel.getContactPincode());
		updatedClientDetails.setState(motorInsuranceModel.getContactState());
		/** Task #112360 TWO WHEELER FOR MY INSURANCE CLUB - 29/06/2016 */
		updatedClientDetails.setResidensePhone(StringUtils.isNotBlank(motorInsuranceModel.getStrPhoneNo()) ? motorInsuranceModel.getStrPhoneNo() : "");
		updatedClientDetails.setStdCode(StringUtils.isNotBlank(motorInsuranceModel.getStdCode()) ? motorInsuranceModel.getStdCode() : "");
		updatedClientDetails.setMobile(motorInsuranceModel.getStrMobileNo());
		updatedClientDetails.setEmail(motorInsuranceModel.getStrEmail());
		updatedClientDetails.setOccupation(motorInsuranceModel.getOccupation());
		LOGGER.info("motorInsuranceModel.getDateOfBirth() ::"+motorInsuranceModel.getDateOfBirth());
		updatedClientDetails.setDob(motorInsuranceModel.getDateOfBirth());
		updatedClientDetails.setDrivingExp(String.valueOf(motorInsuranceModel.getDrivingExperience()));
		updatedClientDetails.setProductName(motorInsuranceModel.getProductName());
		updatedClientDetails.setNomineeName(motorInsuranceModel.getNominee_Name());
		updatedClientDetails.setNomineeAge(motorInsuranceModel.getNominee_Age());
		updatedClientDetails.setNomineeRelationship(motorInsuranceModel.getRelationship_with_nominee());
		updatedClientDetails.setGuardianName(motorInsuranceModel.getGuardian_Name());
		updatedClientDetails.setGuardianAge(motorInsuranceModel.getGuardian_Age());
		updatedClientDetails.setGuardianRelationship(motorInsuranceModel.getRelationship_with_Guardian());
		updatedClientDetails.setFirstName(motorInsuranceModel.getStrFirstName());
		updatedClientDetails.setLastName(motorInsuranceModel.getStrLastName());
		updatedClientDetails.setTitle(motorInsuranceModel.getStrTitle());
		updatedClientDetails.setPanNumber(motorInsuranceModel.getPanNumber());
		//updatedClientDetails.setAadharNumber(motorInsuranceModel.getAadharNumber());
		updatedClientDetails.setEiaNumber(motorInsuranceModel.getEiaNumber());
		updatedClientDetails.setIrName(motorInsuranceModel.getIrName());
		// Task #252290 Aadhar Web Service
		setMaskedAadhaarNumber(updatedClientDetails,motorInsuranceModel);
		
		LOGGER.info("motorInsuranceModel.getStrTitle() " + motorInsuranceModel.getStrTitle());
		LOGGER.info("updatedClientDetails.getTitle() " + updatedClientDetails.getTitle());
		/*
		 * DUpdatedClientDetails End
		 */		
		if ("updateVehicleDetails".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
			DUpdatedClientDetails getDUpdatedClientDetails = twoWheelerDAO
					.getDUpdatedClientDetails(motorInsuranceModel.getQuoteId());
			if (getDUpdatedClientDetails != null && getDUpdatedClientDetails.getClientCodeXgen() != null) {
				updatedClientDetails.setClientCodeXgen(getDUpdatedClientDetails.getClientCodeXgen());
			} else {
				updatedClientDetails
						.setClientCodeXgen(userServicesDAO.generateClientCode(motorInsuranceModel.getAgentId()));
			}
			if ("Yes".equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())) {
				if (getDUpdatedClientDetails != null && getDUpdatedClientDetails.getNomineeClientCode() != null) {
					updatedClientDetails.setNomineeClientCode(getDUpdatedClientDetails.getNomineeClientCode());
				} else {
					updatedClientDetails.setNomineeClientCode(
							twoWheelerDAO.getNomineeCodeSequence(motorInsuranceModel.getAgentId()));
				}
			}
		}
		
		return updatedClientDetails;
	}
	/**
	 * @author roshini
	 * @exception Not able to connect UserService
	 * @return Masked Aadhaar number
	 * @param updatedClientDetails
	 * @param motorInsuranceModel
	 * @since 10-10-2018
	 * Task #252290 Aadhar Web Service
	 * This Method is used to call the API to get Masked aadhaar number.
	 */
	private void setMaskedAadhaarNumber(DUpdatedClientDetails updatedClientDetails,
			MotorInsuranceModel motorInsuranceModel) {
		if(motorInsuranceModel.getAadharNumber() != null && StringUtils.isNotBlank(motorInsuranceModel.getAadharNumber())){
			AadhaarRequest aadhaarRequest = new AadhaarRequest();
			aadhaarRequest.setAadhaarNumber(motorInsuranceModel.getAadharNumber());	
			aadhaarRequest.setQuoteId(updatedClientDetails.getQuoteId());
			aadhaarRequest.setAgentId(motorInsuranceModel.getAgentId());
			aadhaarRequest.setApiKey(motorInsuranceModel.getApiKey());
			aadhaarRequest.setFirstName(updatedClientDetails.getFirstName());
			aadhaarRequest.setLastName(updatedClientDetails.getLastName());
			aadhaarRequest.setDateOfBirth(updatedClientDetails.getDob());
			aadhaarRequest.setGender(updatedClientDetails.getTitle()!= null ? updatedClientDetails.getTitle().equalsIgnoreCase("Mr") ? "Male" : "Female":"");
			aadhaarRequest.setAddressLine1(motorInsuranceModel.getContactAddress1());
			aadhaarRequest.setAddressLine2(motorInsuranceModel.getContactAddress2());
			aadhaarRequest.setAddressLine3(motorInsuranceModel.getContactAddress3());
			aadhaarRequest.setAddressLine4(motorInsuranceModel.getContactAddress4());
			aadhaarRequest.setCity(motorInsuranceModel.getContactCity());
			aadhaarRequest.setState(motorInsuranceModel.getState());
			aadhaarRequest.setPincode(motorInsuranceModel.getContactPincode());
			aadhaarRequest.setPartner(false);
			ResponseEntity<AadhaarResponse> response = null ;
			try{
				HttpHeaders headers = new HttpHeaders();
				headers.add("Accept",MediaType.APPLICATION_JSON_VALUE);
				headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(aadhaarRequest), headers); 
				
				response = restTemplate.exchange(
						d2cUserServiceUrl+"/Services/UserServices/UpdateAadhaar", HttpMethod.POST, request, AadhaarResponse.class);
				//d2cServiceUrl+"/Services/UserServices/UpdateAadhaar"
				
				if(response.getStatusCode() == HttpStatus.OK){
					AadhaarResponse aadhaarResponse = response.getBody(); 
					if(aadhaarResponse != null && "S-0001".equalsIgnoreCase(aadhaarResponse.getCode()) && 
							aadhaarResponse.isSuccess() && StringUtils.isNotBlank(aadhaarResponse.getMessage())){
						updatedClientDetails.setAadharNumber(aadhaarResponse.getMessage());
		
					}
				}
			}catch(Exception i){
				LOGGER.info("Unable get Aadhaar masked Number **** ");
			}
		}
		
	}


	/**
	 * @param motorInsuranceModel
	 * @param policyDetails
	 * @param dPolicyVehicleDetailsObj 
	 * @param dtSqlCehicleReg
	 * @param dtSqlPrevPolicyExpir
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	protected DPolicyVehicleDetails setDPolicyVehicelDetails(MotorInsuranceModel motorInsuranceModel,
			DPolicyDetails policyDetails, DPolicyVehicleDetails dPolicyVehicleDetailsObj)
			throws NumberFormatException, Exception {
		
		/*
		 * DPolicyVehicleDetails Start
		 */
		DPolicyVehicleDetails dPolicyVehicleDetails = null;
		if(dPolicyVehicleDetailsObj == null) {
			dPolicyVehicleDetails = new DPolicyVehicleDetails();
		}else {
			dPolicyVehicleDetails = dPolicyVehicleDetailsObj;
		}
		
		Date dtPrevPolicyExpiry = null;
		java.sql.Date dtSqlPrevPolicyExpiry = null;

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date dtVehicleReg = sdf.parse(motorInsuranceModel.getVehicleRegistrationDate());
		java.sql.Date dtSqlCehicleReg = new java.sql.Date(dtVehicleReg.getTime());
		if (!DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
				&& StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())) {
			dtPrevPolicyExpiry = sdf.parse(motorInsuranceModel.getPreviousPolicyExpiryDate());
			dtSqlPrevPolicyExpiry = new java.sql.Date(dtPrevPolicyExpiry.getTime());
		}
		java.sql.Date dtSqlPrevPolicyExpir = dtSqlPrevPolicyExpiry;
		
		// policyVehicleDetails.setId(new BigDecimal("14.133333"));
		dPolicyVehicleDetails.setDPolicyDetails(policyDetails);

		dPolicyVehicleDetails.setRegistrationDate(dtSqlCehicleReg);
		dPolicyVehicleDetails.setYearOfManufacture(new Short((short) motorInsuranceModel.getYearOfManufacture()));

		if (YES.equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())) {
			dPolicyVehicleDetails.setRegistrationAddress1(motorInsuranceModel.getContactAddress1());
			dPolicyVehicleDetails.setRegistrationAddress2(motorInsuranceModel.getContactAddress2());
			dPolicyVehicleDetails.setRegistrationAddress3(motorInsuranceModel.getContactAddress3());
			dPolicyVehicleDetails.setRegistrationAddress4(motorInsuranceModel.getContactAddress4());
			dPolicyVehicleDetails.setRegistrationPincode(motorInsuranceModel.getContactPincode());
			/**Task #138615 Two Wheeler – Mailing address*/
			dPolicyVehicleDetails.setRegistrationCity(motorInsuranceModel.getVehicleRegisteredCity());
			/**Task #138615 Two Wheeler – Mailing address*/
			dPolicyVehicleDetails.setIsRegistrationSame('Y');

		} else {
			dPolicyVehicleDetails.setRegistrationAddress1(motorInsuranceModel.getAddressOne());
			dPolicyVehicleDetails.setRegistrationAddress2(motorInsuranceModel.getAddressTwo());
			dPolicyVehicleDetails.setRegistrationAddress3(motorInsuranceModel.getAddressThree());
			dPolicyVehicleDetails.setRegistrationAddress4(motorInsuranceModel.getAddressFour());
			dPolicyVehicleDetails.setRegistrationPincode(motorInsuranceModel.getRegPinCode());
			/**Task #138615 Two Wheeler – Mailing address*/
			dPolicyVehicleDetails.setRegistrationCity(motorInsuranceModel.getVehicleRegisteredCity());
			/**Task #138615 Two Wheeler – Mailing address*/
			dPolicyVehicleDetails.setIsRegistrationSame('N');
		}

		
		/* NCB Percentage populated to DB */
		int ncbPercentage = 0;
		LOGGER.info("motorInsuranceModel.getNoClaimBonusPercent()::" + motorInsuranceModel.getNoClaimBonusPercent());
		if ("1".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
			ncbPercentage = 0;
		} else if ("2".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
			ncbPercentage = 20;
		} else if ("3".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
			ncbPercentage = 25;
		} else if ("4".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
			ncbPercentage = 35;
		} else if ("5".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
			ncbPercentage = 45;
		} else if ("6".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
			ncbPercentage = 50;
		}

		dPolicyVehicleDetails.setNoClaimPercentage((byte)ncbPercentage);

		long differenceInDays = 0;
		if (org.apache.commons.lang.StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())) {
			String currentDate = new SimpleDateFormat(DATE_FORMAT).format(new Date());
			differenceInDays = IfoundryUtils.diffDates(motorInsuranceModel.getPreviousPolicyExpiryDate(),
					currentDate);
		}
		int ncbPercentageCurrent = 0;
		if (TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())
				&& "No".equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip())
				&& "No".equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())
				&& "Comprehensive".equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyType())
				&& !DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
				&& org.apache.commons.lang.StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())
				&& org.apache.commons.lang.StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
				&& !"0".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
				&& differenceInDays <= 90) {
			if (ncbPercentage == 0) {
				ncbPercentageCurrent = 20;
			} else if (ncbPercentage == 20) {
				ncbPercentageCurrent = 25;
			} else if (ncbPercentage == 25) {
				ncbPercentageCurrent = 35;
			} else if (ncbPercentage == 35) {
				ncbPercentageCurrent = 45;
			} else if (ncbPercentage == 45) {
				ncbPercentageCurrent = 50;
			} else if (ncbPercentage == 50) {
				ncbPercentageCurrent = 50;
			}
		}
		
		dPolicyVehicleDetails.setNoClaimPercentageCurrent(new BigDecimal(ncbPercentageCurrent));
		motorInsuranceModel.setNoClaimBonusPercentinCurrent(ncbPercentageCurrent+"");
		
		/* NCB Percentage populated to DB */

		LOGGER.info("motorInsuranceModel.getClaimAmountReceived() " + motorInsuranceModel.getClaimAmountReceived());
		LOGGER.info("motorInsuranceModel.getTotalDiscount() " + motorInsuranceModel.getTotalDiscount());
		LOGGER.info("motorInsuranceModel.getTotalIdv() " + motorInsuranceModel.getTotalIdv());
		LOGGER.info("motorInsuranceModel.getIsCarFinanced() " + motorInsuranceModel.getIsCarFinanced());
		LOGGER.info("motorInsuranceModel.getIsBiFuelKit() " + motorInsuranceModel.getIsBiFuelKit());
		LOGGER.info("motorInsuranceModel.getIsBiFuelKitYes() " + motorInsuranceModel.getIsBiFuelKitYes());
		LOGGER.info("motorInsuranceModel.getPreviousPolicyExpiryDateDeclaration() "	+ motorInsuranceModel.getPreviousPolicyExpiryDateDeclaration());
		LOGGER.info("motorInsuranceModel.getAddonValue() " + motorInsuranceModel.getAddonValue());
		LOGGER.info("motorInsuranceModel.getIsCarFinancedValue() " + motorInsuranceModel.getIsCarFinancedValue());
		LOGGER.info("motorInsuranceModel.getDiscountidvPercent() " + motorInsuranceModel.getDiscountidvPercent());

		dPolicyVehicleDetails.setRegistrationState(motorInsuranceModel.getState());
		dPolicyVehicleDetails.setRegistrationRegion(motorInsuranceModel.getRegion());
		dPolicyVehicleDetails.setRegistrationZone(motorInsuranceModel.getZone());
		dPolicyVehicleDetails.setRegisteringAuthority(motorInsuranceModel.getVehicleRegisteredCity());
		dPolicyVehicleDetails.setMakeCode(motorInsuranceModel.getVehicleManufacturerName());
		dPolicyVehicleDetails.setMakeName(motorInsuranceModel.getVehicleManufacturerName());
		dPolicyVehicleDetails.setModelCode(motorInsuranceModel.getVehicleModelCode());
		dPolicyVehicleDetails.setServiceModelCode(motorInsuranceModel.getServiceModelCode());
		dPolicyVehicleDetails.setCalculatedModelCode(motorInsuranceModel.getCalculatedModelCode());
		dPolicyVehicleDetails.setModelName(motorInsuranceModel.getVehicleModelName());
		
		dPolicyVehicleDetails.setSeatingCapacity(new BigDecimal(motorInsuranceModel.getSeatingCapacity()));
		dPolicyVehicleDetails.setEngineCapacity((long) motorInsuranceModel.getEngineCapacity());
		dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getIdv()));
		dPolicyVehicleDetails.setEngineNumber(motorInsuranceModel.getEngineNumber()!= null ? motorInsuranceModel.getEngineNumber().toUpperCase(): motorInsuranceModel.getEngineNumber());
		dPolicyVehicleDetails.setChassisNumber(motorInsuranceModel.getChassisNumber()!=null ? motorInsuranceModel.getChassisNumber().toUpperCase(): motorInsuranceModel.getChassisNumber());
		dPolicyVehicleDetails.setVehicleUsage(motorInsuranceModel.getVehicleAge() + "");
		dPolicyVehicleDetails.setPreviousPolicyNumber(motorInsuranceModel.getPreviuosPolicyNumber());
		
		dPolicyVehicleDetails.setPreviousInsurerName(motorInsuranceModel.getPreviousInsurerName());
		dPolicyVehicleDetails.setPreviousInsurerAddress(motorInsuranceModel.getPreviousinsurersCorrectAddress());
		dPolicyVehicleDetails.setPreviousPolicyExpiryDate(dtSqlPrevPolicyExpir);
		dPolicyVehicleDetails.setVehicleAge((byte) motorInsuranceModel.getVehicleAge());
		LOGGER.info("motorInsuranceModel.getTechnicalDiscount() ::: " + motorInsuranceModel.getTechnicalDiscount());
		dPolicyVehicleDetails.setTechnicalDiscount(new BigDecimal(motorInsuranceModel.getTechnicalDiscount()));
		dPolicyVehicleDetails.setFuelType(motorInsuranceModel.getFuelType());
		dPolicyVehicleDetails.setCarOwnershipChanged(motorInsuranceModel.getCarOwnerShip());
		
		// doubt
		// policyVehicleDetails.setOptedAdditionalCover(Boolean.parseBoolean(motorInsuranceModel.getOptedAdditonalCover()));
		dPolicyVehicleDetails
				.setOptedAdditionalCover(Boolean.parseBoolean(motorInsuranceModel.getOptedAdditonalCover()) ? 'Y' : 'N');
		dPolicyVehicleDetails
				.setOptedDiscount(new BigDecimal("ON".equalsIgnoreCase(motorInsuranceModel.getOptedDisount()) ? "0" : "1"));
		dPolicyVehicleDetails.setVehicleRegisteredInthenameof(motorInsuranceModel.getVehicleRegisteredInTheNameOf());
		dPolicyVehicleDetails.setRegistrationNumber(motorInsuranceModel.getRegistrationNumber()!=null ? motorInsuranceModel.getRegistrationNumber().toUpperCase(): motorInsuranceModel.getRegistrationNumber());
		
		LOGGER.info("motorInsuranceModel.getAverageMonthlyMileageRun()" + motorInsuranceModel.getAverageMonthlyMileageRun());
		//policyVehicleDetails.setAverageMonthlyMileageRun(
				//BigDecimal.valueOf(Double.parseDouble(motorInsuranceModel.getAverageMonthlyMileageRun() + "")));
		
		dPolicyVehicleDetails.setFinancierName(motorInsuranceModel.getFinancierName());
		dPolicyVehicleDetails.setPurposeOfCarUsed(motorInsuranceModel.getPurposeOfCarUsed());
		dPolicyVehicleDetails.setVehicleClass(motorInsuranceModel.getVehicleClass());
		dPolicyVehicleDetails.setPreviousPolicyType(motorInsuranceModel.getPreviousPolicyType());
		dPolicyVehicleDetails.setClaimsInPreviousPolicy(motorInsuranceModel.getClaimsMadeInPreviousPolicy());
		dPolicyVehicleDetails.setMostlyDrivenOn(motorInsuranceModel.getVehicleMostlyDrivenOn());
		dPolicyVehicleDetails.setBodyStyle(motorInsuranceModel.getBodyStyle());
		dPolicyVehicleDetails.setClaimsAmountReceived(
				MotorValidation.checkNullForBigdecimal(motorInsuranceModel.getClaimAmountReceived()));
		dPolicyVehicleDetails.setNoOfClaimsReported(MotorValidation.checkNullForByte(motorInsuranceModel.getClaimsReported()));
		// policyVehicleDetails.setNoClaimPercentage(noClaimPercentage);

		dPolicyVehicleDetails.setPreviousInsurerCity(motorInsuranceModel.getPreviousinsurersCity());
		dPolicyVehicleDetails.setPreviousInsurerPincode(motorInsuranceModel.getPreviousinsurersPincode());
		dPolicyVehicleDetails.setTotalDiscount(new BigDecimal(motorInsuranceModel.getTotalDiscount()));
		if(motorInsuranceModel.isEnableCampaignDiscount()){
			dPolicyVehicleDetails.setCampaignDiscount(new BigDecimal(motorInsuranceModel.getCampaignDiscount()));
			dPolicyVehicleDetails.setCampaignDiscountRequest(new BigDecimal(motorInsuranceModel.getCampaignDiscountRequest()));
		}
		dPolicyVehicleDetails.setAdditionalDiscount(new BigDecimal(motorInsuranceModel.getAdditionalDiscount()));
		dPolicyVehicleDetails.setCompanyName(motorInsuranceModel.getCompanyNameForCar());
		dPolicyVehicleDetails.setRegistrationOtherCity(motorInsuranceModel.getRegOtherCity());
		dPolicyVehicleDetails.setTotalIdv(MotorValidation.checkNullForBigdecimal(motorInsuranceModel.getTotalIdv()));
		dPolicyVehicleDetails
				.setDiscountIdvPercent(MotorValidation.checkNullForByte(motorInsuranceModel.getDiscountidvPercent()));
		dPolicyVehicleDetails.setOptedAdditionalcoverDiscount(new BigDecimal("0"));

		dPolicyVehicleDetails.setIsCarFinanced(YES.equalsIgnoreCase(motorInsuranceModel.getIsCarFinanced()) ? 'Y' : 'N');
		dPolicyVehicleDetails.setFinancedType(motorInsuranceModel.getIsCarFinanced());
		dPolicyVehicleDetails.setFinancedValue(motorInsuranceModel.getIsCarFinancedValue());
		dPolicyVehicleDetails.setFinancierName(
				YES.equalsIgnoreCase(motorInsuranceModel.getIsCarFinanced()) ? motorInsuranceModel.getFinancierName() : "");
		dPolicyVehicleDetails.setIsAntiTheftFitted(
				TRUE.equalsIgnoreCase(motorInsuranceModel.getCarFittedWithAntiTheftDevice()) ? true : false);
		dPolicyVehicleDetails.setIsBifuelKit(YES.equalsIgnoreCase(motorInsuranceModel.getIsBiFuelKit()) ? true : false);
		dPolicyVehicleDetails.setIsInbuilt("INBUILT".equalsIgnoreCase(motorInsuranceModel.getIsBiFuelKitYes()) ? 'Y' : 'N');

		dPolicyVehicleDetails.setPrevExpiryDateDeclaration(
				ON.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDateDeclaration()) ? true : false);
		dPolicyVehicleDetails.setNoClaimDeclaration(
				ON.equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusDeclaration()) ? "true" : "false");
		dPolicyVehicleDetails.setAddonFuelKitValue(new BigDecimal(MotorUtils.tryParseDouble(motorInsuranceModel.getAddonValue())));
		dPolicyVehicleDetails.setVehicleAge((byte) motorInsuranceModel.getVehicleAge());
		if("Yes".equalsIgnoreCase(motorInsuranceModel.getIsPosOpted()) && motorInsuranceModel.getTechnicalDiscountFromRequest() != null)
			dPolicyVehicleDetails.setTechnicalDiscountFromRequest(new BigDecimal(motorInsuranceModel.getTechnicalDiscountFromRequest()));
		
		if("BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {
			
			if(1 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor1Year(new BigDecimal(motorInsuranceModel.getIdv()));
				dPolicyVehicleDetails.setModifiedIdvFor1Year(new BigDecimal(motorInsuranceModel.getIdv()));	
			}
			
			if(2 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor2Year(new BigDecimal(motorInsuranceModel.getIdvFor2Year()));
				dPolicyVehicleDetails.setModifiedIdvFor2Year(new BigDecimal(motorInsuranceModel.getIdvFor2Year()));
				/**
				 * Overriding IDV value to set 2nd Year IDV
				 */
				dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getIdvFor2Year()));
				
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor2Year(motorInsuranceModel.getVehicleAgeforyear2()!=null ? motorInsuranceModel.getVehicleAgeforyear2().byteValue() : null);
			}
			
			if(3 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor3Year(new BigDecimal(motorInsuranceModel.getIdvFor3Year()));
				dPolicyVehicleDetails.setModifiedIdvFor3Year(new BigDecimal(motorInsuranceModel.getIdvFor3Year()));
				/**
				 * Overriding IDV value to set 3nd Year IDV
				 */
				dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getIdvFor3Year()));
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor3Year(motorInsuranceModel.getVehicleAgeforyear3() != null ? motorInsuranceModel.getVehicleAgeforyear3().byteValue() : null);
				
			}
			if(4 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor4Year(new BigDecimal(motorInsuranceModel.getIdvFor4Year()));
				dPolicyVehicleDetails.setModifiedIdvFor4Year(new BigDecimal(motorInsuranceModel.getIdvFor4Year()));
				/**
				 * Overriding IDV value to set 4th Year IDV
				 */
				dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getIdvFor4Year()));
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor4Year(motorInsuranceModel.getVehicleAgeforyear4() != null ? motorInsuranceModel.getVehicleAgeforyear4().byteValue() : null);
				
			}
			if(5 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor5Year(new BigDecimal(motorInsuranceModel.getIdvFor5Year()));
				dPolicyVehicleDetails.setModifiedIdvFor5Year(new BigDecimal(motorInsuranceModel.getIdvFor5Year()));
				/**
				 * Overriding IDV value to set 5th Year IDV
				 */
				dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getIdvFor5Year()));
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor5Year(motorInsuranceModel.getVehicleAgeforyear5() != null ? motorInsuranceModel.getVehicleAgeforyear5().byteValue() : null);
				
			}
			
			
		}
		
		if("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {
			
			if(1 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor1Year(new BigDecimal(motorInsuranceModel.getIdvFor1Year()));
				dPolicyVehicleDetails.setDiscountIdvPercent1Year(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent1Year()) ? Integer.valueOf(motorInsuranceModel.getDiscountIDVPercent1Year()) : null);
				dPolicyVehicleDetails.setModifiedIdvFor1Year(new BigDecimal(motorInsuranceModel.getModifiedIdvfor1Year()));	
			}
			
			if(2 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor2Year(new BigDecimal(motorInsuranceModel.getIdvFor2Year()));
				dPolicyVehicleDetails.setDiscountIdvPercent2Year(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent2Year()) ? Integer.valueOf(motorInsuranceModel.getDiscountIDVPercent2Year()) : null);
				dPolicyVehicleDetails.setModifiedIdvFor2Year(new BigDecimal(motorInsuranceModel.getModifiedIdvfor2Year()));	
				
				/**
				 * Overriding IDV & Total IDV & Discount IDV Percent value to set 2nd Year IDV
				 */
				if(motorInsuranceModel.getPolicyTerm() == 2) {
					dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getModifiedIdvfor2Year()));
					dPolicyVehicleDetails.setDiscountIdvPercent(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent2Year()) ? Byte.valueOf(motorInsuranceModel.getDiscountIDVPercent2Year()) : null);
					dPolicyVehicleDetails.setTotalIdv(new BigDecimal(motorInsuranceModel.getIdvFor2Year()));	
				}
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor2Year(motorInsuranceModel.getVehicleAgeforyear2()!=null ? motorInsuranceModel.getVehicleAgeforyear2().byteValue() : null);
				
			}
			
			if(3 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor3Year(new BigDecimal(motorInsuranceModel.getIdvFor3Year()));
				dPolicyVehicleDetails.setDiscountIdvPercent3Year(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent3Year()) ? Integer.valueOf(motorInsuranceModel.getDiscountIDVPercent3Year()) : null);
				dPolicyVehicleDetails.setModifiedIdvFor3Year(new BigDecimal(motorInsuranceModel.getModifiedIdvfor3Year()));
				
				/**
				 * Overriding IDV & Total IDV & Discount IDV Percent value to set 3nd Year IDV
				 */
				if(motorInsuranceModel.getPolicyTerm() == 3) {
					dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getModifiedIdvfor3Year()));
					dPolicyVehicleDetails.setDiscountIdvPercent(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent3Year()) ? Byte.valueOf(motorInsuranceModel.getDiscountIDVPercent3Year()) : null);
					dPolicyVehicleDetails.setTotalIdv(new BigDecimal(motorInsuranceModel.getIdvFor3Year()));	
				}
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor3Year(motorInsuranceModel.getVehicleAgeforyear3()!=null ? motorInsuranceModel.getVehicleAgeforyear3().byteValue() : null);
			}
			
			if(4 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor4Year(new BigDecimal(motorInsuranceModel.getIdvFor4Year()));
				dPolicyVehicleDetails.setDiscountIdvPercent4Year(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent4Year()) ? Integer.valueOf(motorInsuranceModel.getDiscountIDVPercent4Year()) : null);
				dPolicyVehicleDetails.setModifiedIdvFor4Year(new BigDecimal(motorInsuranceModel.getModifiedIdvfor4Year()));
				
				/**
				 * Overriding IDV & Total IDV & Discount IDV Percent value to set 4th Year IDV
				 */
				if(motorInsuranceModel.getPolicyTerm() == 4) {
					dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getModifiedIdvfor4Year()));
					dPolicyVehicleDetails.setDiscountIdvPercent(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent4Year()) ? Byte.valueOf(motorInsuranceModel.getDiscountIDVPercent4Year()) : null);
					dPolicyVehicleDetails.setTotalIdv(new BigDecimal(motorInsuranceModel.getIdvFor4Year()));	
				}
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor4Year(motorInsuranceModel.getVehicleAgeforyear4()!=null ? motorInsuranceModel.getVehicleAgeforyear4().byteValue() : null);
			}
			
			if(5 <= motorInsuranceModel.getPolicyTerm()) {
				dPolicyVehicleDetails.setIdvFor5Year(new BigDecimal(motorInsuranceModel.getIdvFor5Year()));
				dPolicyVehicleDetails.setDiscountIdvPercent5Year(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent5Year()) ? Integer.valueOf(motorInsuranceModel.getDiscountIDVPercent5Year()) : null);
				dPolicyVehicleDetails.setModifiedIdvFor5Year(new BigDecimal(motorInsuranceModel.getModifiedIdvfor5Year()));
				
				/**
				 * Overriding IDV & Total IDV & Discount IDV Percent value to set 5th Year IDV
				 */
				if(motorInsuranceModel.getPolicyTerm() == 5) {
					dPolicyVehicleDetails.setIdv(new BigDecimal(motorInsuranceModel.getModifiedIdvfor5Year()));
					dPolicyVehicleDetails.setDiscountIdvPercent(StringUtils.isNotBlank(motorInsuranceModel.getDiscountIDVPercent5Year()) ? Byte.valueOf(motorInsuranceModel.getDiscountIDVPercent5Year()) : null);
					dPolicyVehicleDetails.setTotalIdv(new BigDecimal(motorInsuranceModel.getIdvFor5Year()));	
				}
				/**
				 * Vehicle Age
				 * 
				 */
				dPolicyVehicleDetails.setVehicleAgeFor5Year(motorInsuranceModel.getVehicleAgeforyear5()!=null ? motorInsuranceModel.getVehicleAgeforyear5().byteValue() : null);
			}
		}
		
		setRateDiscountDifference(dPolicyVehicleDetails, motorInsuranceModel);
		return dPolicyVehicleDetails;
		/*
		 * DPolicyVehicleDetails end
		 */
	}


	/**
	 * @param motorInsuranceModel
	 * @param policyDetails
	 * @return
	 */
	private List<DNonElecAccessoriesLimit> setDNonElecAccessoriesLimit(MotorInsuranceModel motorInsuranceModel,
			DPolicyDetails policyDetails) throws Exception {
		List<DNonElecAccessoriesLimit> dNonElecAccessoriesLimitList = new ArrayList<DNonElecAccessoriesLimit>();
		if( motorInsuranceModel.getNonElectricalAccessoriesList() != null ){
			/* DNonElecAccessoriesLimit */
			for( NonElectronicValues nonElectronicValues: motorInsuranceModel.getNonElectricalAccessoriesList()){
				DNonElecAccessoriesLimit dNonElecAccessoriesLimit = new DNonElecAccessoriesLimit();
				dNonElecAccessoriesLimit.setNonElecAccessoriesName(nonElectronicValues.getNonElectronicNameOfElectronicAccessories());
				dNonElecAccessoriesLimit.setAccessoriesValue(nonElectronicValues.getNonElectronicValue());
				dNonElecAccessoriesLimit.setMakeOrModel(nonElectronicValues.getNonElectronicMakeModel());
				dNonElecAccessoriesLimit.setQuoteId(motorInsuranceModel.getQuoteId());
				dNonElecAccessoriesLimit.setDPolicyDetails(policyDetails);
				
				dNonElecAccessoriesLimitList.add(dNonElecAccessoriesLimit);
			}
		}
		return dNonElecAccessoriesLimitList;
	}


	/**
	 * @param motorInsuranceModel
	 * @param policyDetails
	 * @return
	 * @throws NumberFormatException
	 */
	private List<DElectricalAccessoriesLimit> setDElectricalAccessoriesLimit(MotorInsuranceModel motorInsuranceModel,
			DPolicyDetails policyDetails) throws NumberFormatException {
		List<DElectricalAccessoriesLimit> dElectricalAccessoriesLimitList = new ArrayList<DElectricalAccessoriesLimit>();
		if( motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ElecAccessoriesCover") != null &&  motorInsuranceModel.getElectricalAccessoriesList() != null && Double.parseDouble(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ElecAccessoriesCover")) > 0 ){
			/* DElectricalAccessoriesLimit */
			for( ElectronicValues electronicValues : motorInsuranceModel.getElectricalAccessoriesList() ){
				DElectricalAccessoriesLimit dElectricalAccessoriesLimit = new DElectricalAccessoriesLimit();
				dElectricalAccessoriesLimit.setAccessoriesName(electronicValues.getElectronicNameOfElectronicAccessories());
				dElectricalAccessoriesLimit.setAccessoriesValue(electronicValues.getElectronicValue());
				dElectricalAccessoriesLimit.setMakeOrModel(electronicValues.getElectronicMakeModel());
				dElectricalAccessoriesLimit.setQuoteId(motorInsuranceModel.getQuoteId());
				dElectricalAccessoriesLimit.setDPolicyDetails(policyDetails);
				
				dElectricalAccessoriesLimitList.add(dElectricalAccessoriesLimit);
			}
		}
		return dElectricalAccessoriesLimitList;
	}


	/**
	 * @param product
	 * @param policyDetails
	 * @return
	 */
	private List<DPolicyPricingElements> setDPolicyPricingElements(Product product, DPolicyDetails policyDetails) throws Exception {
		List<DPolicyPricingElements> policyPricingelementsList = new ArrayList<DPolicyPricingElements>();
		

		Map<String, String> PricingehmCoverages = MotorUtils.getCoveragesMap(product);
		LOGGER.info("PricingehmCoverages::: " + PricingehmCoverages);
		List<String> getCoverage_Details = MotorUtils.getCoverageVMC_Details();
		LOGGER.info("getCoverage_Details::: " + getCoverage_Details.size());
		for (int index = 0; index < getCoverage_Details.size(); index++) {
			DPolicyPricingElements policyPricingelements = new DPolicyPricingElements();

			// Restricted the below parameters insertion into this table for
			// Policy Bazar

			policyPricingelements.setDPolicyDetails(policyDetails);
			/*LOGGER.info("PricingElementName::: " + getCoverage_Details.get(index));
			LOGGER.info("PricingElementValue::: " + new BigDecimal(
					MotorUtils.getValue1(PricingehmCoverages, getCoverage_Details.get(index)), MathContext.DECIMAL64));*/
			policyPricingelements.setPricingElementName(getCoverage_Details.get(index));
			policyPricingelements.setPricingElementValue(new BigDecimal(
					MotorUtils.getValue1(PricingehmCoverages, getCoverage_Details.get(index)),
					MathContext.DECIMAL64).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			// }
			policyPricingelementsList.add(policyPricingelements);
			hashCodemap.put(getCoverage_Details.get(index), String.valueOf(MotorUtils.getValue1(PricingehmCoverages, getCoverage_Details.get(index))));
		}
		return policyPricingelementsList;
	}


	/**
	 * @param motorInsuranceModel
	 * @param product
	 * @param policyDetails
	 * @return
	 */
	private List<DPolicyCoverages> setDPolicyCoverages(MotorInsuranceModel motorInsuranceModel, Product product,
			DPolicyDetails policyDetails) throws Exception {
		// DPolicyCoverages
		
		List<DPolicyCoverages> policyCoveragesList = new ArrayList<DPolicyCoverages>();
		
		for (int indexC = 0; indexC < product.getCoverages().size() ; indexC++) {
			LOGGER.info("product.getCoverages() == "+product.getCoverages().get(indexC).getCoverageName());
			List<Coverage> lstSubCoverages = product.getCoverages().get(indexC).getSubCoverages();
			if (lstSubCoverages != null && !lstSubCoverages.isEmpty()) {
				for (Coverage objSubCoverage : lstSubCoverages) {
					boolean proceed = true;
					LOGGER.info("getSubCoverages CoverageName *     ::      " + objSubCoverage.getCoverageName());

					DPolicyCoverages policyCoverages = new DPolicyCoverages();

					// policyCoverages.setId(count++);
					policyCoverages.setDPolicyDetails(policyDetails);

					//
					policyCoverages.setCoverageName(objSubCoverage.getCoverageName());
					policyCoverages.setPremium(new BigDecimal(objSubCoverage.getPremium(), MathContext.DECIMAL64).setScale(2,
							BigDecimal.ROUND_HALF_EVEN));
					Deductibles d = new Deductibles(); 
					for (Deductibles deductibles : objSubCoverage.getDeductibles()) {
						d = deductibles;
						policyCoverages.setDeductibleName(deductibles.getDeductibleName());
						policyCoverages.setDeductibleType(deductibles.getDeductibleType());
						policyCoverages.setDeductibleValue(
								new BigDecimal(deductibles.getSelectedDeductibleValue(), MathContext.DECIMAL64));
					}
					
					policyCoverages.setIsComposite('T');
					policyCoverages.setParentCoverageId(Long.valueOf(objSubCoverage.getParentcoverageid()));
					
					LOGGER.info("objSubCoverage.getLimits().size() ::: " + objSubCoverage.getLimits().size());
					
					for (Limits limits : objSubCoverage.getLimits()) {
						LOGGER.info("objSubCoverage.limits.getLimitName() ::: " + limits.getLimitName());
						DPolicyCoverages policyCoverages1 = new DPolicyCoverages();
						policyCoverages1.setDPolicyDetails(policyDetails);
						policyCoverages1.setCoverageName(objSubCoverage.getCoverageName());
						policyCoverages1.setPremium(new BigDecimal(objSubCoverage.getPremium(), MathContext.DECIMAL64).setScale(2,
								BigDecimal.ROUND_HALF_EVEN));
						if("ODLimit".equalsIgnoreCase(limits.getLimitName())) {
							policyCoverages1.setDeductibleName(d.getDeductibleName());
							policyCoverages1.setDeductibleType(d.getDeductibleType());
							policyCoverages1.setDeductibleValue(
									new BigDecimal(d.getSelectedDeductibleValue(), MathContext.DECIMAL64));
						}
						policyCoverages1.setLimitName(limits.getLimitName());
						policyCoverages1.setLimitType(limits.getLimitName());
						limits.getDefaultValue();
						policyCoverages1.setLimitValue(new BigDecimal(limits.getSelectedLimitValue(), MathContext.DECIMAL64)
								.setScale(2, BigDecimal.ROUND_HALF_EVEN));
						policyCoverages1.setIsComposite('T');
						policyCoverages1.setParentCoverageId(Long.valueOf(objSubCoverage.getParentcoverageid()));
						policyCoveragesList.add(policyCoverages1);
						proceed = false;
					}
					
					if(proceed) policyCoveragesList.add(policyCoverages);

				}
			}

		}
		
		LOGGER.info("policyCoveragesList 1 ::: " + policyCoveragesList.size());

		for (int index = 0; index < product.getCoverages().size(); index++) {
			
			boolean proceed =  true;
			
			DPolicyCoverages policyCoverages = new DPolicyCoverages();

			// policyCoverages.setId(count++);
			policyCoverages.setDPolicyDetails(policyDetails);

			LOGGER.info("Coverages CoverageName *     ::      " + product.getCoverages().get(index).getCoverageName());
			//
			policyCoverages.setCoverageName(product.getCoverages().get(index).getCoverageName());
			policyCoverages.setPremium(new BigDecimal(product.getCoverages().get(index).getPremium(), MathContext.DECIMAL64)
					.setScale(2, BigDecimal.ROUND_HALF_EVEN));
			
			for (Deductibles deductibles : product.getCoverages().get(index).getDeductibles()) {
				policyCoverages.setDeductibleName(deductibles.getDeductibleName());
				policyCoverages.setDeductibleType(deductibles.getDeductibleType());
				policyCoverages
						.setDeductibleValue(new BigDecimal(deductibles.getSelectedDeductibleValue(), MathContext.DECIMAL64));
				
			}
			// policyCoverages.setIsComposite(product.getCoverages().get(index).getIsMandatory());
			policyCoverages.setIsComposite('T');

			policyCoverages.setParentCoverageId(Long.valueOf(product.getCoverages().get(index).getParentcoverageid()));
			
			for (Limits limits : product.getCoverages().get(index).getLimits()) {
				
				policyCoverages = new DPolicyCoverages();
				policyCoverages.setDPolicyDetails(policyDetails);
				policyCoverages.setCoverageName(product.getCoverages().get(index).getCoverageName());
				policyCoverages.setPremium(new BigDecimal(product.getCoverages().get(index).getPremium(), MathContext.DECIMAL64)
						.setScale(2, BigDecimal.ROUND_HALF_EVEN));
				policyCoverages.setIsComposite('T');
				policyCoverages.setParentCoverageId(Long.valueOf(product.getCoverages().get(index).getParentcoverageid()));
				policyCoverages.setLimitName(limits.getLimitName());
				policyCoverages.setLimitType(limits.getLimitName());
				policyCoverages.setLimitValue(new BigDecimal(limits.getSelectedLimitValue(), MathContext.DECIMAL64));
				policyCoveragesList.add(policyCoverages);
				proceed = false;
				
			}

			if(proceed) policyCoveragesList.add(policyCoverages);

		}

		LOGGER.info("policyCoveragesList 2 ::: " + policyCoveragesList.size());
		
		// VPC_OwnDamageCover_pricingelement_VoluntaryDed
		DPolicyCoverages policyCoverages_d = new DPolicyCoverages();
		policyCoverages_d.setDPolicyDetails(policyDetails);
		policyCoverages_d.setDeductibleName("VMC_VolDeductible");
		policyCoverages_d.setDeductibleType("VoluntaryDed");
		policyCoverages_d.setDeductibleValue(new BigDecimal(
				motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODCommercialCover_pricingelement_VoluntaryDed"),
				MathContext.DECIMAL64));
		policyCoverages_d.setIsComposite('T');
		policyCoveragesList.add(policyCoverages_d);
		
		LOGGER.info("policyCoveragesList 3 ::: " + policyCoveragesList.size());
		for(DPolicyCoverages coverages : policyCoveragesList) {
			LOGGER.info("coverages ::: " + coverages.getCoverageName() + " ::: coverages.getLimitName() ::: " + coverages.getLimitName());
		}
		return policyCoveragesList;
	}


	/**
	 * @param product
	 * @param policyDetails
	 * @return {@link List<DPolicyAttributes>}
	 */
	private List<DPolicyAttributes> setDPolicyAttributes(Product product, DPolicyDetails policyDetails) throws Exception {
		// DPolicyAttributes mutiple value

		List<DPolicyAttributes> policyAttributesList = new ArrayList<DPolicyAttributes>();
		for (int index = 0; index < product.getAttributes().size(); index++) {
			DPolicyAttributes policyAttributes = new DPolicyAttributes();
			
			policyAttributes.setDPolicyDetails(policyDetails);
			policyAttributes.setCoverageId(Long.valueOf(product.getAttributes().get(index).getCoverageId()));
			policyAttributes.setAttributeLevel(product.getAttributes().get(index).getLevel());
			policyAttributes.setAttributeName(product.getAttributes().get(index).getAttributeName());
			policyAttributes.setAttributeType(product.getAttributes().get(index).getAttributeType());
			policyAttributes.setAttributeDataType(product.getAttributes().get(index).getAttributeDataType());
			policyAttributes.setAttributeValue(product.getAttributes().get(index).getAttributeListValues() == null ? null
					: product.getAttributes().get(index).getAttributeListValues().toString());
			policyAttributesList.add(policyAttributes);

		}
		return policyAttributesList;
	}


	/**
	 * @param motorInsuranceModel
	 * @param quoteDetails
	 * @param dtInception
	 * @param dtExpiry
	 * @param dPolicyDetails 
	 * @param policyDetails
	 */
	protected DPolicyDetails setDPolicyDetails(MotorInsuranceModel motorInsuranceModel, DQuoteDetails quoteDetails,
			Date dtInception, Date dtExpiry, DPolicyDetails dPolicyDetailsObj) throws Exception {
		
		DPolicyDetails dPolicyDetails = null;
		if(dPolicyDetailsObj == null ) {
			dPolicyDetails = new DPolicyDetails();
		} else {
			dPolicyDetails = dPolicyDetailsObj;
		}
		/*
		 * DPolicyDetails Started
		 */
		DPolicyVehicleDetails policyVehicleDetails = new DPolicyVehicleDetails();
		dPolicyDetails.setDQuoteDetails(quoteDetails);
		
		dPolicyDetails.setProductName(motorInsuranceModel.getProductName());
		dPolicyDetails.setSubLine(SUB_LINE);
		dPolicyDetails.setLine(LINE);
		dPolicyDetails.setBusinessstatus(BUSINESS_STATUS_QUOTE);

		if (TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
			//Task #317705 GI Products on POS – Inception date and Time
			if ("DBS".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
				dPolicyDetails.setInceptionTime(motorInsuranceModel.getInceptionTime());
				LOGGER.info("dPolicyDetails.getInceptionTime() ::: " + dPolicyDetails.getInceptionTime());
			} else {
				dPolicyDetails.setInceptionTime(INCEPTION_TIME);
			}
		} else {
			dPolicyDetails.setInceptionTime(new SimpleDateFormat("HH:mm").format(new Date()));
		}

		dPolicyDetails.setInceptiondate(dtInception);
		dPolicyDetails.setExpirydate(dtExpiry);
		dPolicyDetails.setAgentId(motorInsuranceModel.getAgentCode());
		dPolicyDetails.setChannel(AGENTS);
		dPolicyDetails.setModeOfDelivery(INTERNET);
		dPolicyDetails.setPremium(BigDecimal.valueOf(motorInsuranceModel.getPremium()));
		dPolicyDetails.setCommissionAmount(BigDecimal.valueOf(0d));
		dPolicyDetails.setCommissionRate(BigDecimal.valueOf(0d));
		dPolicyDetails.setBranchCode(BRANCH_CODE);
		dPolicyDetails.setIsAutoAssociationMember(motorInsuranceModel.isAutomobileAssociationMembership() ? 'Y' : 'N');
		dPolicyDetails.setCustomerType(CommonUtils.INDIVIDUAL);

		String strFirstName = GenericUtils.checkString(motorInsuranceModel.getStrFirstName());
		String strLastName = GenericUtils.checkString(motorInsuranceModel.getStrLastName());
		String proposerName = strFirstName + " " + strLastName;

		dPolicyDetails.setCustomerName(proposerName);
		dPolicyDetails.setCustEmail(motorInsuranceModel.getStrEmail());

		
		dPolicyDetails.setCustMobile(motorInsuranceModel.getStrMobileNo());
		dPolicyDetails.setCustAddress1(motorInsuranceModel.getContactAddress1());
		dPolicyDetails.setCustAddress2(motorInsuranceModel.getContactAddress2());
		dPolicyDetails.setCustAddress3(motorInsuranceModel.getContactAddress3());
		dPolicyDetails.setCustAddress4(motorInsuranceModel.getContactAddress4());
		dPolicyDetails.setCustCityCode(motorInsuranceModel.getContactCity());
		dPolicyDetails.setCustCityName(motorInsuranceModel.getContactCity());
		dPolicyDetails.setCustState(motorInsuranceModel.getContactState());
		dPolicyDetails.setCustPincode(motorInsuranceModel.getContactPincode());
		dPolicyDetails.setLiabilityPolicyTerm((short)motorInsuranceModel.getLiabilityPolicyTerm());
		dPolicyDetails.setCpaPolicyTerm((short)motorInsuranceModel.getCpaPolicyTerm());
		// cheacking breaking insurance
		if ("true".equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
			long lnDifferenceInDays = MotorValidation.diffDates(motorInsuranceModel.getPreviousPolicyExpiryDate(),
					MotorValidation.GetCurrentDate());
			LOGGER.info("lnDifferenceInDays ::: " + lnDifferenceInDays);
		
		LOGGER.info("Vehicle RegistrationDate:: "+motorInsuranceModel.getVehicleRegistrationDate());
		//int vehicleage = (int) (PortalDateConversion.diffDates(MotorValidation.GetCurrentDate(), motorInsuranceModel.getVehicleRegistrationDate()) / 365);
		int VehicleAgeindays = (int) (PortalDateConversion.diffDates( motorInsuranceModel.getVehicleRegistrationDate(),MotorValidation.GetCurrentDate()));
		
		int compareBreakinCase = 0;
		if(VehicleAgeindays < 2557 && lnDifferenceInDays <= 365) {
			compareBreakinCase = 365;
		}
		LOGGER.info("CompareBreakinCase:: "+compareBreakinCase+ "lnDifferenceInDays:: "+lnDifferenceInDays);
			
			//LOGGER.info("motorInsuranceModel.getIsVehicleInspected() ::: " + motorInsuranceModel.getIsVehicleInspected());
			if(YES.equalsIgnoreCase(motorInsuranceModel.getIsVehicleInspected())){
				dPolicyDetails.setIsvehicleInspected("1");
				dPolicyDetails.setIsBreakinInsurance("0");
				Date inspectionDate = new SimpleDateFormat(Constants.DATE_FORMAT1).parse(motorInsuranceModel.getVehicleInspectionDate());
				policyVehicleDetails.setVehicleInspectionDate(inspectionDate);
				dPolicyDetails.setInceptionTime(new SimpleDateFormat(Constants.TIME_FORMAT).format(inspectionDate));
			} //else if (lnDifferenceInDays > 0)
			else if(!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()) && 
					(DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate()) ||
					StringUtils.isBlank(motorInsuranceModel.getPreviousPolicyExpiryDate()) ||
					lnDifferenceInDays > compareBreakinCase) ) {
				
				dPolicyDetails.setIsvehicleInspected("0");
				dPolicyDetails.setIsBreakinInsurance("1");
			} else {
				dPolicyDetails.setIsvehicleInspected("NA");
				dPolicyDetails.setIsBreakinInsurance("NA");
			}
			
			LOGGER.info("lnDifferenceInDays:" + lnDifferenceInDays);

		} else {
			dPolicyDetails.setIsvehicleInspected("NA");
			dPolicyDetails.setIsBreakinInsurance("NA");
		}
		
		dPolicyDetails.setPolicyTerm(new BigDecimal(motorInsuranceModel.getPolicyTerm()));
		dPolicyDetails.setIsDaysbreakin(String.valueOf(motorInsuranceModel.isSevenDaysBreakIn()));
		dPolicyDetails.setVirNumber(motorInsuranceModel.getVirNumber());
		if(motorInsuranceModel.getPremiumBreakUpMap() != null) {
			dPolicyDetails.setOdPremium(new BigDecimal(motorInsuranceModel.getPremiumBreakUpMap()
					.get("od_premium"), MathContext.DECIMAL64)
					.setScale(2,BigDecimal.ROUND_HALF_EVEN));
			dPolicyDetails.setLiabilityPremium(new BigDecimal(motorInsuranceModel.getPremiumBreakUpMap()
					.get("liability_premium"), MathContext.DECIMAL64)
					.setScale(2,BigDecimal.ROUND_HALF_EVEN));
		}
		
		
		//Task #296434 CPA cover changes for all motor products-163348 
		if(!"standalone".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())){
			if("Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
					&& "No".equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())){
				dPolicyDetails.setCpaCoverIsSelected(motorInsuranceModel.getCpaCoverIsRequired());
				dPolicyDetails.setNoEffectiveLicense(String.valueOf(motorInsuranceModel.isNoEffectiveDrivingLicense()));
				dPolicyDetails.setCoverWithSameAgent(String.valueOf(motorInsuranceModel.isCpaCoverWithInternalAgent()));
				dPolicyDetails.setStandalonePAPolicy(String.valueOf(motorInsuranceModel.isStandalonePAPolicy()));
				if(!motorInsuranceModel.isNoEffectiveDrivingLicense() 
						&& (motorInsuranceModel.isCpaCoverWithInternalAgent() || motorInsuranceModel.isStandalonePAPolicy()) ){					
					dPolicyDetails.setCpaCompanyName(motorInsuranceModel.getCpaCoverCompanyName());
					dPolicyDetails.setCpaPolicyNumber(motorInsuranceModel.getCpaCoverPolicyNumber());
					dPolicyDetails.setCpaExpiryDate(motorInsuranceModel.getCpaCoverExpiryDate());
				}
			}else if("Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
					&& "YES".equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())){
				dPolicyDetails.setCpaCoverIsSelected(motorInsuranceModel.getCpaCoverIsRequired());
				dPolicyDetails.setCpaSumInsured(motorInsuranceModel.getCpaSumInsured() != null
						? BigDecimal.valueOf(motorInsuranceModel.getCpaSumInsured()) : null);
			}
		}
		
		LOGGER.info("InceptionTime:: "+dPolicyDetails.getInceptionTime());
		return dPolicyDetails;
		/*
		 * DPolicyDetails End
		 */
	}


	/**
	 * 
	 * @param motorInsuranceModel
	 * @return {@link DQuoteDetails}
	 * @throws Exception
	 */
	private DQuoteDetails setDQuoteDetails(MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		DQuoteDetails quoteDetails = new DQuoteDetails();
		/*** Crating the object for db insert **/
		LOGGER.info("*** Crating the object for db insert **");

		SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat objSDF2 = new SimpleDateFormat(SIXTH_DATE_FORMAT);
		GenericUtils.checkString(motorInsuranceModel.getStrName());
		Calendar objCalExpDt = Calendar.getInstance();
		objCalExpDt.add(Calendar.DATE, Constants.QUOTE_EXPIRY);
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DATE, Constants.QUOTE_VALIDITY);

		String strProposalDate = objSDF.format(new Date());
		motorInsuranceModel.setProposalDate(strProposalDate);
		String strValidity = objSDF.format(calendar.getTime());
		String strQuoteExpDt = objSDF.format(objCalExpDt.getTime());

		String strPolInceptionDt = objSDF2.format(objSDF.parse(motorInsuranceModel.getPolicyStartDate()));
		objCalExpDt.add(Calendar.DATE, Constants.QUOTE_EXPIRY);
		Calendar objCalPolExp = Calendar.getInstance();
		Date dtPolIncepDt = objSDF2.parse(strPolInceptionDt);
		objCalPolExp.setTime(dtPolIncepDt);
		if(motorInsuranceModel.getPolicyTerm() > 0){
			objCalPolExp.add(Calendar.YEAR, motorInsuranceModel.getPolicyTerm());
		}else{
		   objCalPolExp.add(Calendar.YEAR, 1);
		}
		objCalPolExp.add(Calendar.DAY_OF_MONTH, -1);
		Date dtPolExpiry = objCalPolExp.getTime();
		objSDF2.format(dtPolExpiry);
		
		quoteDetails.setQuoteId(motorInsuranceModel.getQuoteId());
		quoteDetails.setQuoteDate(new Date());
		quoteDetails.setProduct("TWOWHEELER");
		quoteDetails.setPremium(BigDecimal.valueOf(motorInsuranceModel.getPremium()));
		quoteDetails.setValidity(MotorValidation.GetConvertionDate(strValidity));
		LOGGER.info("motorInsuranceModel.getBusinessStatus() ::: " + motorInsuranceModel.getBusinessStatus());
		char status = Constants.QUOTE_STATUS_DRAFT.charAt(0);
		if(StringUtils.isNotBlank(motorInsuranceModel.getBusinessStatus())) {
			status = motorInsuranceModel.getBusinessStatus().charAt(0);
		}
		quoteDetails.setStatus(Character.toUpperCase(status));
		quoteDetails.setAgentId(motorInsuranceModel.getAgentId());
		quoteDetails.setClientCode(motorInsuranceModel.getClientCode());
		LOGGER.info("PolicyTerm"+motorInsuranceModel.getPolicyTerm() +"LiabilityPolicyTerm::"+motorInsuranceModel.getLiabilityPolicyTerm());
		if("BrandNewTwowheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
				if(motorInsuranceModel.getPolicyTerm() == 1 && motorInsuranceModel.getLiabilityPolicyTerm()== 5
					&& "Comprehensive".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()))
					quoteDetails.setPlanName("MotorCycleBundled");
				else if( motorInsuranceModel.getPolicyTerm() == 0 && motorInsuranceModel.getLiabilityPolicyTerm()== 5
						&& "LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()))
					quoteDetails.setPlanName("MotorCycleLiabilityOnly");	
				else
					quoteDetails.setPlanName("MotorCyclePackage_LT");	
		}else{
			if(motorInsuranceModel.getPolicyTerm() == 0 && motorInsuranceModel.getLiabilityPolicyTerm() <= 3
					&& "LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())){
				quoteDetails.setPlanName("MotorCycleLiabilityOnlyRollOver");	
			} else if ("StandAlone".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())) {
				quoteDetails.setPlanName("MotorCycleStandAlone");
			} else
				quoteDetails.setPlanName("MotorCyclePackage");
		}
		quoteDetails.setServiceTax(new BigDecimal(motorInsuranceModel.getServiceTax()));
		quoteDetails.setQuoteExpiryDate(MotorValidation.GetConvertionDate(strQuoteExpDt));
		quoteDetails.setGrossPremium(BigDecimal.valueOf(motorInsuranceModel.getPremium()));
		quoteDetails.setNetPremium(BigDecimal.valueOf(motorInsuranceModel.getNetPremium()));
		quoteDetails.setCess(BigDecimal.valueOf(motorInsuranceModel.getEducationCess()));
		quoteDetails.setKrishiCess(BigDecimal.valueOf(motorInsuranceModel.getKrishiKalyanCess()));
		quoteDetails.setCgst(new BigDecimal(motorInsuranceModel.getCgst()));
		quoteDetails.setSgst(new BigDecimal(motorInsuranceModel.getSgst()));
		quoteDetails.setIgst(new BigDecimal(motorInsuranceModel.getIgst()));
		quoteDetails.setUgst(new BigDecimal(motorInsuranceModel.getUtgst()));
		quoteDetails.setGstin(motorInsuranceModel.getGstin());
		quoteDetails.setCgstRate(new BigDecimal(motorInsuranceModel.getCgstRate()));
		quoteDetails.setSgstRate(new BigDecimal(motorInsuranceModel.getSgstRate()));
		quoteDetails.setIgstRate(new BigDecimal(motorInsuranceModel.getIgstRate()));
		quoteDetails.setUgstRate(new BigDecimal(motorInsuranceModel.getUtgstRate()));	
		quoteDetails.setTaxType(motorInsuranceModel.getTaxType());
		quoteDetails.setExternalReferenceNumber(motorInsuranceModel.getExternalReferenceNumber());
		quoteDetails.setBranchCode(motorInsuranceModel.getBranchCode());
		quoteDetails.setChannelCode(motorInsuranceModel.getChannelCode());
		quoteDetails.setKeralaCess(new BigDecimal(motorInsuranceModel.getKeralaFloodCess()));
		quoteDetails.setKeralaCessRate(new BigDecimal(motorInsuranceModel.getKeralaFloodCessRate()));
		
		return quoteDetails;
	}

	/**
	 * @param motorInsuranceModel
	 * @param tppPricingValuesModel
	 * @param product
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private TppPricingValuesModel setPremiumDetails(MotorInsuranceModel motorInsuranceModel, Product product) throws NumberFormatException, Exception {
		/*
		 * quoteId creation and client code verification pending task
		 */
		/*
		 * Insert Tpp
		 */
		
		TppPricingValuesModel tppPricingValuesModel = new TppPricingValuesModel();
		motorInsuranceModel.setPremium(Double.valueOf("" +new BigDecimal(product.getPremium(), MathContext.DECIMAL64)
				.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
		Map<String, String> hmCoverages = MotorUtils.getCoveragesMap(product);
		LOGGER.info("hmCoverages ::: " + hmCoverages);
		motorInsuranceModel.setServiceTax(MotorUtils.getRoundedValue(hmCoverages, "ServiceeTax"));
		motorInsuranceModel.setEducationCess(MotorUtils.getRoundedValue(hmCoverages, "EducationCess"));
		motorInsuranceModel.setKrishiKalyanCess(MotorUtils.getRoundedValue(hmCoverages, "KrishiKalyan"));
		motorInsuranceModel.setServiceTaxPlusCess(motorInsuranceModel.getServiceTax() + motorInsuranceModel.getEducationCess() + motorInsuranceModel.getKrishiKalyanCess() );
		motorInsuranceModel.setNetPremium(product.getPremium() - motorInsuranceModel.getServiceTaxPlusCess());
		motorInsuranceModel.setPremiumWithOutAddOnCov(product.getPremium());
		motorInsuranceModel.getAgentId();
		motorInsuranceModel.setPlanName(Constants.PLAN_MOTOR_SHIELD_TWOWHEELER);
		//Since these values are not subracted to odpremium line commented
		//motorInsuranceModel.setBulkDealDiscountCoverPremium(Math.abs(MotorUtils.getValue(hmCoverages, "BulkDealDiscountCover")));
		//motorInsuranceModel.setNilIntermediationCoverPremium(Math.abs(MotorUtils.getValue(hmCoverages, "NilIntermediationRateCover")));
		motorInsuranceModel.setAdditionalTowingChargesCoverPremium(Math.abs(MotorUtils.getValue(hmCoverages, "AdditionalTowingChargesCover")));
		motorInsuranceModel.setCgst(MotorUtils.getRoundedValue(hmCoverages, "ServiceeTax_pricingelement_CGST"));
		motorInsuranceModel.setSgst(MotorUtils.getRoundedValue(hmCoverages, "ServiceeTax_pricingelement_SGST"));
		motorInsuranceModel.setIgst(MotorUtils.getRoundedValue(hmCoverages, "ServiceeTax_pricingelement_IGST"));
		motorInsuranceModel.setUtgst(MotorUtils.getRoundedValue(hmCoverages, "ServiceeTax_pricingelement_UTGST"));
		motorInsuranceModel.setKeralaFloodCess(MotorUtils.getRoundedValue(hmCoverages, "ServiceeTax_pricingelement_Kcess"));
		motorInsuranceModel.setCgstRate(MotorUtils.getValue2(hmCoverages, "ServiceeTax_pricingelement_CGST_rate"));
		motorInsuranceModel.setSgstRate(MotorUtils.getValue2(hmCoverages, "ServiceeTax_pricingelement_SGST_rate"));
		motorInsuranceModel.setIgstRate(MotorUtils.getValue2(hmCoverages, "ServiceeTax_pricingelement_IGST_rate"));
		motorInsuranceModel.setUtgstRate(MotorUtils.getValue2(hmCoverages, "ServiceeTax_pricingelement_UTGST_rate"));
		motorInsuranceModel.setKeralaFloodCessRate(MotorUtils.getValue2(hmCoverages, "ServiceeTax_pricingelement_Kcess_rate"));
		
		
		if (StringUtils.isBlank(motorInsuranceModel.getQuoteId()) || NULL.equalsIgnoreCase(motorInsuranceModel.getQuoteId()) ) {
			motorInsuranceModel.setPlanName("TWOWHEELER");
			motorInsuranceModel.setQuoteId(twoWheelerDAO.getQuoteSequence(motorInsuranceModel.getAgentId(), motorInsuranceModel.getPlanName()));
			motorInsuranceModel.setPlanName(Constants.PLAN_MOTOR_SHIELD_TWOWHEELER);
			motorInsuranceModel.setBusinessStatus(BUSINESS_STATUS_DRAFT);
		} else {
			LOGGER.info("QuoteId::" + motorInsuranceModel.getQuoteId());
			twoWheelerDAO.setQuoteExistingDB(motorInsuranceModel);
		}
		
		if (StringUtils.isBlank(motorInsuranceModel.getProcessType())
				|| "calculatepremium".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
			motorInsuranceModel.setBusinessStatus(BUSINESS_STATUS_DRAFT);
		} else {
			motorInsuranceModel.setBusinessStatus(BUSINESS_STATUS_QUOTE);	
		}

		/*
		 * Premium Breakup details start
		 */
		 PremiumBreakupDetails(product, motorInsuranceModel, tppPricingValuesModel);

		/*
		 * Premium Breakup details End
		 */
		 
		if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getLegalliabilitytoemployees())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getLegalliabilitytopaiddriver())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getAccidentcoverforpaiddriver())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getValueofelectricalaccessories())) {
			LOGGER.info("electrical accessories ::: " + motorInsuranceModel.getValueofelectricalaccessories());
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getValueofnonelectricalaccessories())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getFibreglass())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getAdditionalTowingChargesCover())) {
			motorInsuranceModel.setOptedAdditonalCover(ON);
		}

		if (TRUE.equalsIgnoreCase(motorInsuranceModel.getCarFittedWithAntiTheftDevice())) {
			motorInsuranceModel.setOptedDisount(ON);
		} else if (!ZERO.equalsIgnoreCase(motorInsuranceModel.getVoluntarydeductible())) {
			motorInsuranceModel.setOptedDisount(ON);
		} else if (motorInsuranceModel.isAutomobileAssociationMembership()) {
			motorInsuranceModel.setOptedDisount(ON);
		}
			
		return tppPricingValuesModel;
	}
	
	
	
	public boolean breakInInsurance(String apikey, String agentId, String quoteId, String title, String firstName,
			String lastName, String email) {
		boolean isBreakInInsurance = false;
		DPolicyDetails dPolicyDetails = new DPolicyDetails();
		try {
			dPolicyDetails = twoWheelerDAO.getBreakInInsuranceStatusForMotor(quoteId);
			LOGGER.info("dPolicyDetails.getIsvehicleInspected() ::::: " + dPolicyDetails.getIsvehicleInspected());
			if ("0".equals(dPolicyDetails.getIsvehicleInspected())) {
				isBreakInInsurance = true;
					MapMessage mapMessage = new ActiveMQMapMessage();
					mapMessage.setString("title", title);
					mapMessage.setString("firstName", firstName);
					mapMessage.setString("lastName", lastName);
					mapMessage.setString("agentId", agentId);
					mapMessage.setString("quoteId", quoteId);
					mapMessage.setString("emailId", email);
					mapMessage.setString("product", "TwoWheeler");
					mapMessage.setString("process", "BreakInInsurance");
					emailMessageSender.sendMessage(mapMessage, false);
					
				if (dPolicyDetails.getDQuoteDetails().getPosCode() != null) {
					DPoscodeMaster posNumber = twoWheelerDAO
							.getDPoscodeMaster(dPolicyDetails.getDQuoteDetails().getPosCode());
					if (StringUtils.isNotBlank(posNumber.getPosMobile())) {
						ActiveMQMapMessage cardSMS = new ActiveMQMapMessage();
						cardSMS.setString("MobileNo", posNumber.getPosMobile());
						cardSMS.setString("QuoteId", quoteId);
						cardSMS.setString("SMSType", "VirInitiation");
						smsSender.send(cardSMS);
					}
				}
					
				TpSurveyorService.makeSurveyorCall(quoteId);
				dPolicyDetails.setIsBreakinInsurance("1");
				twoWheelerDAO.updateBreakInInsuranceForMotor(dPolicyDetails);
			}
		} catch (Exception e) {
			LOGGER.info("BreakInInsurance Exception ::" + e);
		}
		return isBreakInInsurance;
	}

	private TppPricingValuesModel PremiumBreakupDetails(Product product, MotorInsuranceModel motorInsuranceModel,
			TppPricingValuesModel tppPricingValuesModel) throws Exception {

		Map<String, String> hmCoverages1 = MotorUtils.getCoveragesMap(product);
		double od_premium = 0;
		od_premium += MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover");
		od_premium += MotorUtils.getValue1(hmCoverages1, "VMC_ElecAccessoriesCover");
		od_premium += MotorUtils.getValue1(hmCoverages1, "VPC_FiberGlass");
		od_premium += MotorUtils.getValue1(hmCoverages1, "GeoExtension");
		od_premium += MotorUtils.getValue1(hmCoverages1, "ImportedVehicleLoading");
		od_premium += MotorUtils.getValue1(hmCoverages1, "DriverTutionExtension");
		od_premium += MotorUtils.getValue1(hmCoverages1, "AdditionalTowingChargesCover");

		od_premium -= MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_AntiTheftDiscount");
		od_premium -= MotorUtils.getValue1(hmCoverages1,
				"VMC_ODCommercialCover_pricingelement_AutoAssociationMembership");
		od_premium -= MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_VoluntaryDed");
		od_premium -= MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_NoCliamDiscount");
		od_premium -= MotorUtils.getValue1(hmCoverages1, "DiscountforHandicapped");
		od_premium = new BigDecimal(od_premium).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		double liability_premium = 0;
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_LiabilityCover");
		liability_premium -= MotorUtils.getValue1(hmCoverages1, "TPPDStatutoryDiscount");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_PAOwnerDriverCover");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_PAUnnamed");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_PAPaidDriver");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_LLPaidDriverCover");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_LLEMPLOYEES");
		liability_premium = new BigDecimal(liability_premium).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		motorInsuranceModel.getVehicleRegistrationDate();

		motorInsuranceModel.getVehicleRegistrationDate();

		String strQuoteDate = motorInsuranceModel.getPolicyStartDate();
		String wsStatus = motorInsuranceModel.getWsStatus();
		String strExpiryDt = motorInsuranceModel.getPolicyExpiryDate();
		double totalpremium = liability_premium + od_premium;
		String VMC_ODPremiumA_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumA_rate"));
		String VMC_ODPremiumB_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumB_rate"));
		String VMC_ODPremiumC_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumC_rate"));
		String VMC_ODIDVAdjustor_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODIDVAdjustor_rate"));
		String VMC_ODLeapofFaith_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODLeapofFaith_rate"));
		String VMC_ODStreetPrice_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODStreetPrice_rate"));

		String VMC_ODPremiumA = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumA"));
		String VMC_ODPremiumB = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumB"));
		String VMC_ODPremiumC = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumC"));
		String VMC_ODIDVAdjustor = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODIDVAdjustor"));
		String VMC_ODLeapofFaith = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODLeapofFaith"));
		String VMC_ODStreetPrice = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODStreetPrice"));

		String VMC_TheftPremiumA_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumA_rate"));
		String VMC_TheftPremiumB_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumB_rate"));
		String VMC_TheftPremiumC_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumC_rate"));
		String VMC_TheftIDVAdjustor_rate = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftIDVAdjustor_rate"));
		String VMC_TheftLeapofFaith_rate = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftLeapofFaith_rate"));
		String VMC_TheftStreetPrice_rate = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftStreetPrice_rate"));

		String VMC_TheftPremiumA = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumA"));
		String VMC_TheftPremiumB = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumB"));
		String VMC_TheftPremiumC = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumC"));
		String VMC_TheftIDVAdjustor = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftIDVAdjustor"));
		String VMC_TheftLeapofFaith = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftLeapofFaith"));
		String VMC_TheftStreetPrice = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftStreetPrice"));

		String VMC_TPPremiumA_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumA_rate"));
		String VMC_TPPremiumB_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumB_rate"));
		String VMC_TPPremiumC_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumC_rate"));
		String VMC_TPIDVAdjustor_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPIDVAdjustor_rate"));
		String VMC_TPLeapofFaith_rate = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPLeapofFaith_rate"));
		String VMC_TPPremiumAdjustor_rate = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumAdjustor_rate"));

		String VMC_TPPremiumA = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumA"));
		String VMC_TPPremiumB = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumB"));
		String VMC_TPPremiumC = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumC"));
		String VMC_TPIDVAdjustor = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPIDVAdjustor"));
		String VMC_TPLeapofFaith = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPLeapofFaith"));
		String VMC_TPPremiumAdjustor = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPremiumAdjustor"));

		String ODTotalExpenseMultiplier = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_ODTotalExpenseMultiplier"));
		String TPTotalExpenseMultiplier = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_TPBasicCover_pricingelement_TPTotalExpenseMultiplier"));
		String TheftTotalExpenseMultiplier = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_TheftTotalExpenseMultiplier"));
		String ThirdPartyAdjustedPremium = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_ThirdPartyAdjustedPremium"));
		String VMC_ODPremiumD = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_ODPremiumD"));
		String VMC_TheftPremiumD = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TheftPremiumD"));
		String VMC_TPPremiumD = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_TPBasicCover_pricingelement_VMC_TPPremiumD"));
		String VMC_TariffODPremium = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TariffODPremium"));
		String VMC_TPPODPremium = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_VMC_TPPODPremium"));

		String PremiumAfterTechDiscount = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_PremiumAfterTechDiscount"));
		String PremiumAfterCapDiscount = Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_PremiumAfterCapDiscount"));
		String CapDiscount = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_CapDiscount"));

		String PremiumAfterCapLoading = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_PremiumAfterCapLoading"));
		String CapLoading = Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_CapLoading"));

		LOGGER.info("PremiumAfterTechDiscount : " + PremiumAfterTechDiscount + " PremiumAfterCapDiscount : "
				+ PremiumAfterCapDiscount + "  CapDiscount : " + CapDiscount);
		String PremiumAdjustmentPercentage = "";
		try {
			double techDiscount = twoWheelerDAO.getTechnicalDiscount(motorInsuranceModel, "MotorCyclePackage");
			motorInsuranceModel.setTechnicalDiscount(techDiscount);
			PremiumAdjustmentPercentage = Double.toString(techDiscount);
		} catch (Exception e) {
			LOGGER.info("Exception in getTechnicalDiscount " + e);
		}

		LOGGER.info("---------------Before TPP insertion---------------");
		LOGGER.info("PremiumAfterCapLoading = " + PremiumAfterCapLoading + " and CapLoading =" + CapLoading);

		tppPricingValuesModel = new TppPricingValuesModel(VMC_ODPremiumA_rate, VMC_ODPremiumB_rate, VMC_ODPremiumC_rate,
				VMC_ODIDVAdjustor_rate, VMC_ODLeapofFaith_rate, VMC_ODStreetPrice_rate, VMC_ODPremiumA, VMC_ODPremiumB,
				VMC_ODPremiumC, VMC_ODIDVAdjustor, VMC_ODLeapofFaith, VMC_ODStreetPrice, VMC_TheftPremiumA_rate,
				VMC_TheftPremiumB_rate, VMC_TheftPremiumC_rate, VMC_TheftIDVAdjustor_rate, VMC_TheftLeapofFaith_rate,
				VMC_TheftStreetPrice_rate, VMC_TheftPremiumA, VMC_TheftPremiumB, VMC_TheftPremiumC,
				VMC_TheftIDVAdjustor, VMC_TheftLeapofFaith, VMC_TheftStreetPrice, VMC_TPPremiumA_rate,
				VMC_TPPremiumB_rate, VMC_TPPremiumC_rate, VMC_TPIDVAdjustor_rate, VMC_TPLeapofFaith_rate,
				VMC_TPPremiumAdjustor_rate, VMC_TPPremiumA, VMC_TPPremiumB, VMC_TPPremiumC, VMC_TPIDVAdjustor,
				VMC_TPLeapofFaith, VMC_TPPremiumAdjustor, ODTotalExpenseMultiplier, TPTotalExpenseMultiplier,
				TheftTotalExpenseMultiplier, ThirdPartyAdjustedPremium, VMC_ODPremiumD, VMC_TheftPremiumD,
				VMC_TPPremiumD, VMC_TariffODPremium, VMC_TPPODPremium, PremiumAfterTechDiscount,
				PremiumAfterCapDiscount, CapDiscount, PremiumAfterCapLoading, CapLoading, PremiumAdjustmentPercentage);

		Map<String, String> premiumMap = new HashMap();

		premiumMap.put("Premium", String.valueOf(motorInsuranceModel.getPremium()));
		premiumMap.put("VMC_ODBasicCover", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover")));
		premiumMap.put("VMC_ElecAccessoriesCover",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ElecAccessoriesCover")));

		premiumMap.put("VMC_ODCommercialCover_pricingelement_AntiTheftDiscount", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_AntiTheftDiscount")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_AutoAssociationMembership", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_AutoAssociationMembership")));
		premiumMap.put("VMC_ODBasicCover_VMC_VolDeductible",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_deductible_VoluntaryDeductible")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_VoluntaryDed", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_VoluntaryDed")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_NoCliamDiscount", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_NoCliamDiscount")));

		premiumMap.put("od_premium", String.valueOf(od_premium));
		premiumMap.put("VMC_LiabilityCover", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_LiabilityCover")));
		premiumMap.put("VMC_PAOwnerDriverCover",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_PAOwnerDriverCover")));
		premiumMap.put("VMC_PAUnnamed", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_PAUnnamed")));
		premiumMap.put("VMC_PAPaidDriver", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_PAPaidDriver")));
		premiumMap.put("VMC_LLPaidDriverCover",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_LLPaidDriverCover")));
		LOGGER.info(MotorUtils.getValue1(hmCoverages1, "VMC_LLEMPLOYEES") + "***************************");
		premiumMap.put("VMC_LLEMPLOYEES", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_LLEMPLOYEES")));
		premiumMap.put("liability_premium", String.valueOf(liability_premium));
		premiumMap.put("liability_premium_od_premium", String.valueOf(new BigDecimal(totalpremium)
				.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
		premiumMap.put("ServiceeTax", Double.toString(MotorUtils.getValue1(hmCoverages1, "ServiceeTax")));
		premiumMap.put("EducationCess", Double.toString(MotorUtils.getValue1(hmCoverages1, "EducationCess")));
		premiumMap.put("KrishiKalyan", Double.toString(MotorUtils.getValue1(hmCoverages1, "KrishiKalyan")));
		premiumMap.put("quoteId", motorInsuranceModel.getQuoteId());
		premiumMap.put("startDate", strQuoteDate);
		premiumMap.put("endDate", strExpiryDt);
		premiumMap.put("wsStatus", wsStatus);
		premiumMap.put("VPC_TPPremiumC",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_TPBasicCover_pricingelement_VPC_TPPremiumC")));
		premiumMap.put("VPC_TPPremiumA",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_TPBasicCover_pricingelement_VPC_TPPremiumA")));
		premiumMap.put("VPC_TPPremiumB",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_TPBasicCover_pricingelement_VPC_TPPremiumB")));
		premiumMap.put("VPC_TPPremiumD",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_TPBasicCover_pricingelement_VPC_TPPremiumD")));
		premiumMap.put("VPC_ODPremiumA_rate", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODPremiumA_rate")));
		premiumMap.put("VPC_ODPremiumB_rate", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODPremiumB_rate")));
		premiumMap.put("VPC_ODPremiumC_rate", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODPremiumC_rate")));
		premiumMap.put("VPC_ODIDVAdjustor_rate", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODIDVAdjustor_rate")));
		premiumMap.put("VPC_ODLeapofFaith_rate", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODLeapofFaith_rate")));
		premiumMap.put("VPC_ODStreetPrice_rate", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODStreetPrice_rate")));
		premiumMap.put("VPC_ODPremiumA",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODPremiumA")));
		premiumMap.put("VPC_ODPremiumB",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODPremiumB")));
		premiumMap.put("VPC_ODPremiumC",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODPremiumC")));
		premiumMap.put("VPC_ODIDVAdjustor", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODIDVAdjustor")));
		premiumMap.put("VPC_ODLeapofFaith", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODLeapofFaith")));
		premiumMap.put("VPC_ODStreetPrice", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VPC_ODBasicCover_pricingelement_VPC_ODStreetPrice")));
		premiumMap.put("VMC_ODBasicCover_pricingelement_BasePremium",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_pricingelement_BasePremium")));

		motorInsuranceModel.setPremiumBreakUpMap(premiumMap);

		return tppPricingValuesModel;
	}

	

	private void setRateDiscountDifference(DPolicyVehicleDetails dPolicyVehicleDetails, MotorInsuranceModel motorInsuranceModel) throws Exception {
		try {
			LOGGER.info("motorInsuranceModel.getTechnicalDiscount() ::: " + motorInsuranceModel.getTechnicalDiscount());
			LOGGER.info("Constants.Default_Rate_Discount ::: " + Constants.Default_Rate_Discount);
			BigDecimal defaultValue = new BigDecimal(Constants.Default_Rate_Discount);
			BigDecimal fromDB = new BigDecimal(motorInsuranceModel.getTechnicalDiscount());
			
			if(motorInsuranceModel.getPolicyTerm() > 1 && defaultValue.compareTo(fromDB) == 1) {
				BigDecimal difference = defaultValue.subtract(fromDB).setScale(2, BigDecimal.ROUND_HALF_EVEN).negate();
				LOGGER.info("difference ::: " + difference);
				dPolicyVehicleDetails.setRateDiscount(difference);
				
				BigDecimal VMC_ODBasicCover = new BigDecimal(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODBasicCover"));
				LOGGER.info("VMC_ODBasicCover ::: " + VMC_ODBasicCover);
				
				BigDecimal VMC_ODBasicCover_BasePremium = new BigDecimal(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODBasicCover_pricingelement_BasePremium"));
				LOGGER.info("VMC_ODBasicCover_BasePremium ::: " + VMC_ODBasicCover_BasePremium);
				
				int vehicleAge = 0;
				
				if(motorInsuranceModel.getPolicyTerm() == 1) {
					vehicleAge = motorInsuranceModel.getVehicleAge();
				}				
				if(motorInsuranceModel.getPolicyTerm() == 2) {
					vehicleAge = motorInsuranceModel.getVehicleAgeforyear2();
				}				
				if(motorInsuranceModel.getPolicyTerm() == 3) {
					vehicleAge = motorInsuranceModel.getVehicleAgeforyear3();
				}
				
				LongtermRateMaster longtermRateMaster = twoWheelerDAO.getLongtermRateMaster(motorInsuranceModel.getAgentId(),vehicleAge, motorInsuranceModel.getPolicyStartDate(),Constants.MotorCyclePackage); 
				LOGGER.info("longtermRateMaster.getDiscountValue() ::: " + longtermRateMaster.getDiscountValue());
				
				BigDecimal rateMasterDiscount = longtermRateMaster.getDiscountValue()!= null ? new BigDecimal(longtermRateMaster.getDiscountValue()) : new BigDecimal(0);
				LOGGER.info("rateMasterDiscount ::: " + rateMasterDiscount);
				
				BigDecimal basicODAfterRateMasterDiscount = VMC_ODBasicCover_BasePremium.add((VMC_ODBasicCover_BasePremium.multiply(rateMasterDiscount)));
				LOGGER.info("basicODAfterRateMasterDiscount ::: " + basicODAfterRateMasterDiscount);
				
				BigDecimal subText13 =  VMC_ODBasicCover.subtract(basicODAfterRateMasterDiscount);
				LOGGER.info("subText13 ::: " + subText13);
				
				dPolicyVehicleDetails.setRateDiscountPremium(subText13);
				
			} else {
				dPolicyVehicleDetails.setRateDiscount(new BigDecimal(0));
				dPolicyVehicleDetails.setRateDiscountPremium(new BigDecimal(0));
			}
			
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;
		}
	}
	
	private byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}
	
	private VahanResponse  setVahanServiveResponse(MotorInsuranceModel motorInsuranceModel,
			PremiumDetailsData premiumDetailsData) {
		LOGGER.info("vahan add");
		VahanResponse vahanResponse = new VahanResponse();	
		vahanResponse.setMakerName(motorInsuranceModel.getVahanMakerName());
		vahanResponse.setMakerModel(motorInsuranceModel.getVahanModelName());
		vahanResponse.setResponse(motorInsuranceModel.getVahanResponse());
		premiumDetailsData.setVahanResponse(vahanResponse);
		return vahanResponse;
	}
	
	
}
