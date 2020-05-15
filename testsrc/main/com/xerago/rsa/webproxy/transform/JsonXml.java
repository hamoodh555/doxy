package com.xerago.rsa.webproxy.transform;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xerago.rsa.util.Constants;
import com.xerago.rsa.communication.IPCServices;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.domain.AgentPlanMapping;
import com.xerago.rsa.domain.BrokerCodeMaster;
import com.xerago.rsa.domain.GstMaster;
import com.xerago.rsa.dto.request.CPACoverDetails;
import com.xerago.rsa.dto.request.CalculatePremiumRequest;
import com.xerago.rsa.dto.request.ElectricalAccessoriesDetails;
import com.xerago.rsa.dto.request.ExistingTPPolicyDetails;
import com.xerago.rsa.dto.request.NonElectricalAccessoriesDetails;
import com.xerago.rsa.dto.request.PosDetails;
import com.xerago.rsa.dto.request.ProposerDetails;
import com.xerago.rsa.dto.request.VehicleDetails;
import com.xerago.rsa.dto.response.MakerModelResponse;
import com.xerago.rsa.model.CityStateLookupModel;
import com.xerago.rsa.model.ElectronicValues;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.model.NonElectronicValues;
import com.xerago.rsa.tax.impl.GST;
import com.xerago.rsa.util.MotorValidation;

@Component
public class JsonXml implements Request, Constants {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	IPCServices ipcServices;
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Autowired
	GST gst;
	
	@Autowired
	FormPost formPost; 
	
	@Override
	public <T> MotorInsuranceModel parseValue(T t ) throws Exception {
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();

		CalculatePremiumRequest calculatePremiumRequest = (CalculatePremiumRequest) t;
		Map<String, String> occupationList = new HashMap<String, String>();
		occupationList.put("Armed Forces", "1");
		occupationList.put("Business / Sales Profession", "2");
		occupationList.put("Central / State Government Employee", "3");
		occupationList.put("Corporate Executive", "4");
		occupationList.put("Engineering Profession", "5");
		occupationList.put("Financial Services Profession", "6");
		occupationList.put("Home Maker / Housewife", "7");
		occupationList.put("IT Profession", "8");
		occupationList.put("Medical Profession", "9");
		occupationList.put("Musician / Artist", "10");
		occupationList.put("Sports Person", "11");
		occupationList.put("Student", "12");
		occupationList.put("Teaching Profession", "13");
		occupationList.put("Others", "14");
		occupationList.put("Heads of State", "15");
		occupationList.put("Heads of Government", "15");
		occupationList.put("Politician", "16");
		occupationList.put("Senior Government Official", "17");
		occupationList.put("Senior Judicial Official", "17");
		occupationList.put("Senior Military Official", "17");
		occupationList.put("State-owned Corporation Official", "18");
		occupationList.put("Political Party Official", "18");
		
		VehicleDetails vehicleDetails = calculatePremiumRequest.getVehicleDetails();
		
		/**
		 * Generic Informations
		 */
		motorInsuranceModel.setProcessType(calculatePremiumRequest.getProcess());
		motorInsuranceModel.setApiKey(calculatePremiumRequest.getAuthenticationDetails().getApikey());
		motorInsuranceModel.setPlanName(PLAN_MOTOR_SHIELD_TWOWHEELER);
		motorInsuranceModel.setVehicleSubLine(vehicleDetails.getVehicleSubLine());
		if("BrandnewTwowheeler".equalsIgnoreCase(vehicleDetails.getProductName())){
			motorInsuranceModel.setProductName("BrandnewTwowheeler");
		}else{
			motorInsuranceModel.setProductName("RolloverTwowheeler");
			motorInsuranceModel.setIsPreviousPolicyHolder(TRUE);
		}

		motorInsuranceModel.setChannelCode(calculatePremiumRequest.getChannelcode());
		
		LOGGER.info("Vehcile Subline :: " + motorInsuranceModel.getVehicleSubLine() );
		motorInsuranceModel.setQuoteId(calculatePremiumRequest.getQuoteId());
		motorInsuranceModel.setExternalReferenceNumber(calculatePremiumRequest.getExternalReferenceNumber());
		motorInsuranceModel.setAgentId(calculatePremiumRequest.getAuthenticationDetails().getAgentId());
		if (StringUtils.isBlank(motorInsuranceModel.getAgentId())) {
			motorInsuranceModel.setAgentId(AGENT_RSAI);
		}
		
		/**
		 * Vehicle Details 
		 */
		String yearOfManufacture = vehicleDetails.getYearOfManufacture();
		if (yearOfManufacture != null && !"null".equalsIgnoreCase(yearOfManufacture)) {
			try {
				motorInsuranceModel.setYearOfManufacture(Integer.parseInt(yearOfManufacture));
			} catch (Exception e) {
				motorInsuranceModel.setYearOfManufacture(0);
			}
		}
		motorInsuranceModel.setVehicleRegisteredCity(vehicleDetails.getCarRegisteredCity());
		
		motorInsuranceModel.setVehicleManufacturerName(vehicleDetails.getVehicleManufacturerName());
		motorInsuranceModel.setVehicleMakeCode(vehicleDetails.getVehicleManufacturerName());
		motorInsuranceModel.setVehicleModelCode(vehicleDetails.getVehicleModelCode());
		LOGGER.info("VehicleModelCode : " + motorInsuranceModel.getVehicleModelCode());
		/**
		 * Task #146177 Two wheeler long term product - TECH
		 */
		int policyTerm = vehicleDetails.getPolicyTerm() != null ? vehicleDetails.getPolicyTerm() : 1;
		motorInsuranceModel
				.setCpaPolicyTerm(vehicleDetails.getCpaPolicyTerm() != 0 ? vehicleDetails.getCpaPolicyTerm() : 1);
		if ("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType()))
			policyTerm = 5;
		
		String typeOfCover = vehicleDetails.getTypeOfCover();
		motorInsuranceModel.setPolicyTerm(policyTerm);
		motorInsuranceModel.setLiabilityPolicyTerm(policyTerm);
		
		if(!"getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType()) ){
			
			if("BrandnewTwowheeler".equalsIgnoreCase(vehicleDetails.getProductName())) {
				if( StringUtils.isBlank(typeOfCover) ){
					typeOfCover = "";
				}else if ("Bundled".equalsIgnoreCase(typeOfCover)){
					typeOfCover = "Comprehensive";
					motorInsuranceModel.setPolicyTerm(1);
					motorInsuranceModel.setLiabilityPolicyTerm(5);
					if (vehicleDetails.getCpaPolicyTerm() > 0) {
						motorInsuranceModel.setCpaPolicyTerm(vehicleDetails.getCpaPolicyTerm());
					} else {
						motorInsuranceModel.setCpaPolicyTerm(5);
					}
				}else if ("LiabilityOnly".equalsIgnoreCase(typeOfCover)){
					typeOfCover = "LiabilityOnly";
					motorInsuranceModel.setLiabilityPolicyTerm(5);
					motorInsuranceModel.setPolicyTerm(0);
					motorInsuranceModel.setCpaPolicyTerm(5);
				}else if("Comprehensive".equalsIgnoreCase(typeOfCover)){
					typeOfCover = "Comprehensive";
					motorInsuranceModel.setPolicyTerm(5);
					motorInsuranceModel.setLiabilityPolicyTerm(5);
					if (vehicleDetails.getCpaPolicyTerm() > 0) {
						motorInsuranceModel.setCpaPolicyTerm(vehicleDetails.getCpaPolicyTerm());
					} else {
						motorInsuranceModel.setCpaPolicyTerm(5);
					}
				}
				
			} else { // rollover
				if(StringUtils.isNotBlank(typeOfCover )){
					if("LiabilityOnly".equalsIgnoreCase(typeOfCover)){
						typeOfCover = "LiabilityOnly";
						motorInsuranceModel.setLiabilityPolicyTerm(policyTerm);
						motorInsuranceModel.setPolicyTerm(0);				
					} else if ("StandAlone".equalsIgnoreCase(typeOfCover)){
						typeOfCover = "StandAlone";
						motorInsuranceModel.setLiabilityPolicyTerm(0);
						motorInsuranceModel.setPolicyTerm(policyTerm);
						vehicleDetails.setCpaPolicyTerm(0);
					} else{
						typeOfCover = "Comprehensive";
					}
				}else 
					typeOfCover = "Comprehensive";
				// default to liability policy term if cpa policy term is not set
				motorInsuranceModel.setCpaPolicyTerm(
						vehicleDetails.getCpaPolicyTerm() != 0 ? vehicleDetails.getCpaPolicyTerm()
								: motorInsuranceModel.getLiabilityPolicyTerm());
			}
		}
		LOGGER.info("motorInsuranceModel = cp = "+motorInsuranceModel.getCpaPolicyTerm());

		motorInsuranceModel.setTypeOfCover(typeOfCover);
		motorInsuranceModel.setSource(calculatePremiumRequest.getSource());
		LOGGER.info("Vehicle idv:: "+vehicleDetails.getIdv());
		
		
		
		/**
		 * Task #342448 Manufacture list fetching not
		 * required for services Getting State, Zone, Region, CityCode,
		 * StateCode from getCityStateDetails()
		 */
		//ipcServices.getManufacturerNameList(motorInsuranceModel);
		getCityStateDetails(motorInsuranceModel);
		setIDV_PreviousPolicyDetails(vehicleDetails, motorInsuranceModel);
		
		/**
		 * Vehicle Details
		 */
		motorInsuranceModel.setVehicleRegisteredInTheNameOf(vehicleDetails.getVehicleRegisteredInTheNameOf());
		motorInsuranceModel.setCompanyNameForCar(vehicleDetails.getCompanyNameForCar());
		motorInsuranceModel.setCarOwnerShip(vehicleDetails.getVechileOwnerShipChanged());
		motorInsuranceModel.setDrivingExperience(StringUtils.isNumeric(vehicleDetails.getDrivingExperience()) ? Double.parseDouble(vehicleDetails.getDrivingExperience()) : 0);
		
		/**
		 * Personal Information
		 */
		ProposerDetails proposerDetails = calculatePremiumRequest.getProposerDetails();
		motorInsuranceModel.setStrTitle(proposerDetails.getTitle());
		motorInsuranceModel.setStrFirstName(proposerDetails.getFirstName());
		motorInsuranceModel.setStrLastName(proposerDetails.getLastName());
		motorInsuranceModel.setStrEmail(proposerDetails.getEmailId());
		motorInsuranceModel.setStrName(proposerDetails.getFirstName());
		motorInsuranceModel.setEiaNumber(proposerDetails.getEiaNumber());
		motorInsuranceModel.setIrName(proposerDetails.getIrName());
		//motorInsuranceModel.setIrName("irName");
		motorInsuranceModel.setGender("Mr".equalsIgnoreCase(proposerDetails.getTitle()) ? "Male" : "Female" );
		
		motorInsuranceModel.setStrMobileNo(proposerDetails.getMobileNo());
		motorInsuranceModel.setDateOfBirth(proposerDetails.getDateOfBirth());
		motorInsuranceModel.setStrPhoneNo(StringUtils.isNotBlank(proposerDetails.getStrStdCode())?proposerDetails.getStrStdCode():"" + "-" + (StringUtils.isNotBlank(proposerDetails.getStrPhoneNo())?proposerDetails.getStrPhoneNo():""));
		LOGGER.info("Occupation 1 : " + proposerDetails.getOccupation() + "Condition ::"+ occupationList.containsKey(proposerDetails.getOccupation()));
		if (occupationList.containsKey(proposerDetails.getOccupation())) {
			motorInsuranceModel.setOccupation(proposerDetails.getOccupation());
			motorInsuranceModel.setOccupationCode(occupationList.get(motorInsuranceModel.getOccupation()));
		} else {
			motorInsuranceModel.setOccupation(proposerDetails.getOccupation());
			motorInsuranceModel.setOccupationCode(occupationList.get("Others"));
		}
		
		/**
		 * Setting Client Code
		 */
		if(!"RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId()) && "updateVehicleDetails".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
			ipcServices.customerRegistration(motorInsuranceModel);	
		}
		if("RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId()) && !"getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
			ipcServices.customerRegistration(motorInsuranceModel);
		}
		
		
		motorInsuranceModel.setPolicyStartDate(MotorValidation.setPolicyStartDateForTwoWheeler(
				motorInsuranceModel.getPreviousPolicyExpiryDate(), motorInsuranceModel.getIsPreviousPolicyHolder(),
				motorInsuranceModel.getVehicleRegistrationDate(), motorInsuranceModel.getTypeOfCover(),motorInsuranceModel).toString());
		String strPolicyType = motorInsuranceModel.getPreviousPolicyType();
		if (StringUtils.isNotBlank(motorInsuranceModel.getAgentId())
				&& DBPARTNER_CITI.equalsIgnoreCase(motorInsuranceModel.getAgentId())
				&& StringUtils.isNotBlank(strPolicyType) && THIRDPARTY_ONLY.equalsIgnoreCase(strPolicyType)) {
			motorInsuranceModel.setNoClaimBonusPercent("0");
		}

		motorInsuranceModel.setVoluntarydeductible(String.valueOf(vehicleDetails.getVoluntaryDeductible()));

		
		String mobileno = StringUtils.isNotBlank(motorInsuranceModel.getStrMobileNo())
				? motorInsuranceModel.getStrMobileNo().length() >= 5
						? motorInsuranceModel.getStrMobileNo().substring(0, 5) : motorInsuranceModel.getStrMobileNo()
				: "";
		String emailId = StringUtils.isNotBlank(motorInsuranceModel.getStrEmail())
				? motorInsuranceModel.getStrEmail().length() >= 5 ? motorInsuranceModel.getStrEmail().substring(0, 5)
						: motorInsuranceModel.getStrEmail()
				: "";
		motorInsuranceModel.setUserId(mobileno + emailId);
		
		setCovers(vehicleDetails, motorInsuranceModel);
		
		setAdditionalDetails(calculatePremiumRequest, motorInsuranceModel);
		
		setLongTermProductValues(motorInsuranceModel);
		
		//Task #199795 - Aggregators Webservices
		motorInsuranceModel.setGstin(proposerDetails.getGstin());
		try {
			GstMaster gstMaster = gst.getGstMaster(motorInsuranceModel.getAgentId(), motorInsuranceModel.getPolicyStartDate());
			motorInsuranceModel.setTaxType(gstMaster.getTaxType());
			motorInsuranceModel.setRSBranchState(gstMaster.getState());
			
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;
		}
		
		motorInsuranceModel.setIsVehicleInspected(vehicleDetails.getIsVehicleInspected());
		motorInsuranceModel.setVirNumber(vehicleDetails.getVirNumber());
		motorInsuranceModel.setVehicleInspectionDate(vehicleDetails.getVehicleInspectionDate());
		
		
		if(proposerDetails.getTitle() != null 
				&& !"Mr".equalsIgnoreCase(proposerDetails.getTitle()) 
				&& "RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())
				&& "Ms".equalsIgnoreCase(proposerDetails.getTitle()) || "Mrs".equalsIgnoreCase(proposerDetails.getTitle())){
			BigDecimal campaignDiscountValue = twoWheelerDAO.getCampaignDiscountValue(motorInsuranceModel.getAgentId());
			boolean isEnableCampaignDis = vehicleDetails.getCampaignDiscount() != null ? true : false ;
			if(campaignDiscountValue != null && isEnableCampaignDis) {
				if(vehicleDetails.getCampaignDiscount() <= campaignDiscountValue.doubleValue()){
				motorInsuranceModel.setEnableCampaignDiscount(true);
				motorInsuranceModel.setCampaignDiscountRequest(getDiscountModifiedValue(vehicleDetails.getCampaignDiscount()));
				}else{
					motorInsuranceModel.setEnableCampaignDiscount(true);
					motorInsuranceModel.setCampaignDiscountRequest(getDiscountModifiedValue(campaignDiscountValue.doubleValue()));
				}
		}else{
			motorInsuranceModel.setEnableCampaignDiscount(false);
		}
		
		}	
		
		// Task #278458 POS DETAILS RS 154761 - TECH
		motorInsuranceModel.setIsPosOpted(calculatePremiumRequest.isPosOpted() ? "Yes" : "No");
		motorInsuranceModel.setPosCode(StringUtils.isNotBlank(calculatePremiumRequest.getPosCode()) ? calculatePremiumRequest.getPosCode() : "");	
		PosDetails posDetails = calculatePremiumRequest.getPosDetails();
		if(posDetails != null){
			motorInsuranceModel.setPosName(StringUtils.isNotBlank(posDetails.getName()) ? posDetails.getName() : "");
			motorInsuranceModel.setPosPAN(StringUtils.isNotBlank(posDetails.getPan()) ? posDetails.getPan().toUpperCase() : "");
			motorInsuranceModel.setPosAadhaar(StringUtils.isNotBlank(posDetails.getAadhaar()) ? posDetails.getAadhaar() : "");
			motorInsuranceModel.setPosMobile(StringUtils.isNotBlank(posDetails.getMobile()) ? posDetails.getMobile() : "");
			motorInsuranceModel.setPosLicenceExpDate(StringUtils.isNotBlank(posDetails.getLicenceExpiryDate()) ? posDetails.getLicenceExpiryDate() : "");
		}
		if(vehicleDetails.getTechnicalDiscount() != null)
			motorInsuranceModel.setTechnicalDiscountFromRequest(getDiscountModifiedValue(vehicleDetails.getTechnicalDiscount()));

		
		//CPA cover changes for all motor products-163348 
		if("Individual".equalsIgnoreCase(vehicleDetails.getVehicleRegisteredInTheNameOf())
				&& "No".equalsIgnoreCase(vehicleDetails.getCpaCoverIsRequired())){
			motorInsuranceModel.setCpaCoverIsRequired("No");
			CPACoverDetails cpaCoverDetails = vehicleDetails.getCpaCoverDetails();
			if(cpaCoverDetails != null){
				motorInsuranceModel.setNoEffectiveDrivingLicense(cpaCoverDetails.isNoEffectiveDrivingLicense());
				motorInsuranceModel.setCpaCoverWithInternalAgent(cpaCoverDetails.isCpaCoverWithInternalAgent());
				motorInsuranceModel.setStandalonePAPolicy(cpaCoverDetails.isStandalonePAPolicy());
				motorInsuranceModel.setCpaCoverCompanyName(StringUtils.isNotBlank(cpaCoverDetails.getCompanyName()) ? cpaCoverDetails.getCompanyName() : "" );
				motorInsuranceModel.setCpaCoverPolicyNumber(StringUtils.isNotBlank(cpaCoverDetails.getPolicyNumber()) ? cpaCoverDetails.getPolicyNumber() : "" );
				motorInsuranceModel.setCpaCoverExpiryDate(StringUtils.isNotBlank(cpaCoverDetails.getExpiryDate()) ? cpaCoverDetails.getExpiryDate() : "" );
			}
			motorInsuranceModel.setPaToOwnerDriver("No");
		} else {
			motorInsuranceModel.setPaToOwnerDriver("Yes");
			if("Company".equalsIgnoreCase(vehicleDetails.getVehicleRegisteredInTheNameOf())) {
				motorInsuranceModel.setCpaCoverIsRequired("No");
			} else {
				motorInsuranceModel.setCpaCoverIsRequired("Yes");
			}

			// cpa SI defaulted to 15 lac
			if ( vehicleDetails.getCpaSumInsured() == null) {
				motorInsuranceModel.setCpaSumInsured(15_00_000d);
			} else {
				motorInsuranceModel.setCpaSumInsured(vehicleDetails.getCpaSumInsured());
			}
		}
		setNominationforPA(calculatePremiumRequest.getProposerDetails(), motorInsuranceModel);
		setExistingTPPolicyDetails(motorInsuranceModel, calculatePremiumRequest);
		
		//Task #333350 Vahan Service Integration-192056 starts
		LOGGER.info("vahan check =============================== start "+motorInsuranceModel.getProcessType());
		if(motorInsuranceModel.getProcessType().equalsIgnoreCase("updateVehicleDetails")) {
			List<Map<String, String>> servicePlanList = twoWheelerDAO.getVahanServicePlan(motorInsuranceModel.getProductName(),motorInsuranceModel.getAgentId());
			LOGGER.info("vahan check =============================== start  "+servicePlanList.size());
			if(servicePlanList.size()>0) {
				String serviceCallAvail = servicePlanList.get(0).get("SERVICE_CALL_AVAIL");
				String showSuccess = servicePlanList.get(0).get("SHOW_SUCCESS");
				String showFailure = servicePlanList.get(0).get("SHOW_FAILURE");
				String blockIfFail = servicePlanList.get(0).get("FAIL_FLOW_BLOCK");
				LOGGER.info(" ----"+serviceCallAvail);
				if("Yes".equalsIgnoreCase(serviceCallAvail)) {
					
					String  productName = motorInsuranceModel.getProductName();
					List<Map<String, String>> vahanMasterList = new ArrayList<>();
					LOGGER.info(" vahan check start" + productName);
					LOGGER.info(" vahan check start" +  "RolloverTwoWheeler".equalsIgnoreCase(productName));
					if (StringUtils.isNotBlank(productName) && "RolloverTwoWheeler".equalsIgnoreCase(productName)) {
						LOGGER.info(" vahan check start if");
						vahanMasterList =	twoWheelerDAO.getVahanResponseMasterRN(motorInsuranceModel.getEngineNumber(),motorInsuranceModel.getChassisNumber(),motorInsuranceModel.getRegistrationNumber());
					}
					else
						vahanMasterList =	twoWheelerDAO.getVahanResponseMaster(motorInsuranceModel.getEngineNumber(),motorInsuranceModel.getChassisNumber());
					LOGGER.info(" ----"+vahanMasterList.size());
					
					if(vahanMasterList.size()==0) {
						LOGGER.info(" vahan check start");
						MakerModelResponse makerModelResponse =	ipcServices.getVahanServicePlan(motorInsuranceModel ,"motorCycle");
						
				        if(showSuccess.equalsIgnoreCase("Yes")) {
				        	if(makerModelResponse.getResponse().equalsIgnoreCase("Found")) {
				        		motorInsuranceModel.setVahanMakerName(makerModelResponse.getMakerName());
				        		motorInsuranceModel.setVahanModelName(makerModelResponse.getMakerModel());
				        		motorInsuranceModel.setVahanResponse(makerModelResponse.getResponse());
				        		motorInsuranceModel.setVahanMessage("Vahan service verified successfully");
				        	}
				        }
				        if(showFailure.equalsIgnoreCase("Yes")) {
				        	if(!makerModelResponse.getResponse().equalsIgnoreCase("Found")) {
				        		motorInsuranceModel.setVahanMakerName(makerModelResponse.getMakerName());
				        		motorInsuranceModel.setVahanModelName(makerModelResponse.getMakerModel());
				        		motorInsuranceModel.setVahanResponse(makerModelResponse.getResponse());
				        		motorInsuranceModel.setVahanMessage("Vahan service verification failed");
				        	}
				        }
				        
				        
				       if(blockIfFail.equalsIgnoreCase("Yes")) {
				        	if(!makerModelResponse.getResponse().equalsIgnoreCase("Found"))
				        		motorInsuranceModel.setVahanResponse("BlockFlow");
				        }
				        
					}else{
						if(showSuccess.equalsIgnoreCase("Yes")) {
							motorInsuranceModel.setVahanMakerName(vahanMasterList.get(0).get("MAKER_NAME"));
							motorInsuranceModel.setVahanModelName(vahanMasterList.get(0).get("MODEL_NAME"));
							motorInsuranceModel.setVahanResponse("Found");
							motorInsuranceModel.setVahanMessage("Vahan service verified successfully");
						}
					}
				}
				
			}
			LOGGER.info("vahan check ==============================end ");
		}
		//Task #333350 Vahan Service Integration-192056 ends
	
		
		
		return motorInsuranceModel;
	}
	
	/**
	 * <p>
	 * Applicable for :
	 * <ol>
	 * <li><b>Product Name : </b>RollOverTwowheeler</li>
	 * <li><b>Type Of Cover : </b>StandAlone</li>
	 * </ol>
	 * <p>
	 * Setting Existing third party policy details, If the selected policy type
	 * of cover is StandAlone.
	 * </p>
	 * 
	 * @category Liability Policy or Third Party Policy.
	 * @param objMotorInsuranceModel
	 * @param objCalculatePremiumRequest
	 * @author roshini
	 * @since 2019-06-14
	 */
	private void setExistingTPPolicyDetails(MotorInsuranceModel objMotorInsuranceModel,
			CalculatePremiumRequest objCalculatePremiumRequest) {
		if (StringUtils.isNotBlank(objMotorInsuranceModel.getTypeOfCover())
				&& StringUtils.equalsIgnoreCase("StandAlone", objMotorInsuranceModel.getTypeOfCover())) {
			ExistingTPPolicyDetails tpPolicyDetails = objCalculatePremiumRequest.getExistingTPPolicyDetails();
			if (tpPolicyDetails != null) {
				objMotorInsuranceModel.setTpInsurer(tpPolicyDetails.getTpInsurer());
				objMotorInsuranceModel.setTpPolicyNumber(tpPolicyDetails.getTpPolicyNumber());
				objMotorInsuranceModel.setTpInceptionDate(tpPolicyDetails.getTpInceptionDate());
				objMotorInsuranceModel.setTpExpiryDate(tpPolicyDetails.getTpExpiryDate());
				objMotorInsuranceModel.setTpPolicyTerm(tpPolicyDetails.getTpPolicyTerm());
				objMotorInsuranceModel.setTpAddress1(tpPolicyDetails.getTpAddress1());
				objMotorInsuranceModel.setTpAddress2(tpPolicyDetails.getTpAddress2());
				if(tpPolicyDetails.getTpCity() != null && StringUtils.isNotBlank(tpPolicyDetails.getTpCity())) {
					objMotorInsuranceModel.setTpCity(StringUtils.upperCase(tpPolicyDetails.getTpCity()));
					CityStateLookupModel cityStateLookupModel = twoWheelerDAO
							.getCityState(tpPolicyDetails.getTpCity());
					if(cityStateLookupModel != null && cityStateLookupModel.getState() != null){
						objMotorInsuranceModel.setTpState(StringUtils.upperCase(cityStateLookupModel.getState()));
					}
				}
				objMotorInsuranceModel.setTpPincode(tpPolicyDetails.getTpPincode());
			}
		}
	}
	
	private double getDiscountModifiedValue(double campaignDiscount) {
		double value = Math.abs(campaignDiscount);
		if (value >= 1) {
			return value / 100 * -1;
		} else
			return value * -1;
	}
	
	@Override
	public <T> void googleCampaignCodeCapture(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public <T> void setIDV_PreviousPolicyDetails(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {

		VehicleDetails vehicleDetails = (VehicleDetails) t;

		if ("BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {

			motorInsuranceModel.setIsPreviousPolicyHolder("false");
			motorInsuranceModel.setVehicleRegistrationDate(vehicleDetails.getVehicleRegDate());
//			motorInsuranceModel.setPreviousPolicyExpiryDate(vehicleDetails.getVehicleRegDate());

			/**
			 * Getting Idv, SeatingCapacity, EngineCapacity, VehicleClass,
			 * VehicleModelName, BodyStyle, VehicleMakeId, AddonModel, FuelType,
			 * ModifiedIDVfor2Year , ModifiedIDVfor3Year, VehicleAgeforyear2,
			 * VehicleAgeforyear3 from common services through IPC and these
			 * values are setting on called method
			 */
			ipcServices.getModelIdvResult(motorInsuranceModel);
			
		} else {
			motorInsuranceModel.setIsPreviousPolicyHolder("true");

			motorInsuranceModel.setVehicleRegistrationDate(vehicleDetails.getVehicleRegDate());
			motorInsuranceModel.setPreviousPolicyExpiryDate(vehicleDetails.getPreviousPolicyExpiryDate());

			motorInsuranceModel.setPreviousPolicyType(vehicleDetails.getPreviousPolicyType());
			motorInsuranceModel.setPreviuosPolicyNumber(vehicleDetails.getPreviousPolicyNo());
			motorInsuranceModel.setPreviousInsurerName(vehicleDetails.getPreviousInsurerName());
			motorInsuranceModel.setClaimsMadeInPreviousPolicy(vehicleDetails.getClaimsMadeInPreviousPolicy()); // Expected values are 'Yes' or 'No'
			motorInsuranceModel.setNoClaimBonusPercent(vehicleDetails.getNoClaimBonusPercent()); // Expected values are 1 or 2 or 3 or 4 or 5 or 6
			motorInsuranceModel.setNoClaimBonusPercentinCurrent(vehicleDetails.getNcbcurrent()); // Expected values are 20 or 25 or 35 or 45 or 50

			motorInsuranceModel.setClaimAmountReceived(vehicleDetails.getClaimAmountReceived());
			motorInsuranceModel.setClaimsReported(vehicleDetails.getClaimsReported());
		
			/**
			 * Getting Idv, SeatingCapacity, EngineCapacity, VehicleClass,
			 * VehicleModelName, BodyStyle, VehicleMakeId, AddonModel, FuelType,
			 * ModifiedIDVfor2Year , ModifiedIDVfor3Year, VehicleAgeforyear2,
			 * VehicleAgeforyear3 from common services through IPC and these
			 * values are setting on called method
			 */
			ipcServices.getModelIdvResult(motorInsuranceModel);
			
			motorInsuranceModel.setDiscountidvPercent(String.valueOf(vehicleDetails.getDiscountIdvPercent()));
			motorInsuranceModel.setDiscountIDVPercent1Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent2Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent3Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent4Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent5Year(motorInsuranceModel.getDiscountidvPercent());
		
			
			if( "RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName()) &&
					"getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType()) ) {
		
				LOGGER.info("GET QUOTE SERVICE....");
				
				motorInsuranceModel.setDiscountidvPercent(String.valueOf(vehicleDetails.getDiscountIdvPercent()));
				motorInsuranceModel.setDiscountIDVPercent1Year(motorInsuranceModel.getDiscountidvPercent());
				motorInsuranceModel.setDiscountIDVPercent2Year(String.valueOf(vehicleDetails.getDiscountIdvPercent()));
				motorInsuranceModel.setDiscountIDVPercent3Year(String.valueOf(vehicleDetails.getDiscountIdvPercent()));
				motorInsuranceModel.setDiscountIDVPercent4Year(String.valueOf(vehicleDetails.getDiscountIdvPercent()));
				motorInsuranceModel.setDiscountIDVPercent5Year(String.valueOf(vehicleDetails.getDiscountIdvPercent()));
				
				
				double orginalIdv = motorInsuranceModel.getIdv();
				long modifyYourIdv =  (long) Double.parseDouble(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIdv = Math.round( motorInsuranceModel.getIdv() + ( motorInsuranceModel.getIdv() * modifyYourIdv / 100 ) );
				motorInsuranceModel.setIdv(modifiedIdv);		
				LOGGER.info("orginalIdv :: "+orginalIdv+" :: "+motorInsuranceModel.getDiscountidvPercent()+" ::: "+modifiedIdv);
				
				motorInsuranceModel.setTotalIdv("" + orginalIdv);
				motorInsuranceModel.setIdvFor1Year(Double.parseDouble(motorInsuranceModel.getTotalIdv()));
				motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
				LOGGER.info("MODIFIED :: "+motorInsuranceModel.getModifiedIdvfor1Year());
				double orginalIdv2 = motorInsuranceModel.getIdvFor2Year();
				long modifyYourIdv2 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIDVfor2Year = Math.round( orginalIdv2 + ( orginalIdv2 * modifyYourIdv2 / 100 ));
				motorInsuranceModel.setModifiedIdvfor2Year(modifiedIDVfor2Year);
				
				double orginalIdv3 = motorInsuranceModel.getIdvFor3Year();
				long modifyYourIdv3 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIDVfor3Year = Math.round( orginalIdv3 + ( orginalIdv3 * modifyYourIdv3 / 100 ));
				motorInsuranceModel.setModifiedIdvfor3Year(modifiedIDVfor3Year);
						
				double orginalIdv4 = motorInsuranceModel.getIdvFor4Year();
				long modifyYourIdv4 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIDVfor4Year = Math.round( orginalIdv4 + ( orginalIdv4 * modifyYourIdv4 / 100 ));
				motorInsuranceModel.setModifiedIdvfor4Year(modifiedIDVfor4Year);
				
				double orginalIdv5 = motorInsuranceModel.getIdvFor5Year();
				long modifyYourIdv5 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIDVfor5Year = Math.round( orginalIdv5 + ( orginalIdv5 * modifyYourIdv5 / 100 ));
				motorInsuranceModel.setModifiedIdvfor5Year(modifiedIDVfor5Year);
				LOGGER.info("MODIFIED :: "+motorInsuranceModel.getModifiedIdvfor1Year());
			
			}else {
				
			if ( "RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId()) 
					|| "Microsite".equalsIgnoreCase(motorInsuranceModel.getSource()) 
					||  "NewSite".equalsIgnoreCase(motorInsuranceModel.getSource())) {	
				
				LOGGER.info("Idv Value:: ");
				double orginalIdv = motorInsuranceModel.getIdv();
				long modifyYourIdv =  (long) Double.parseDouble(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIdv = Math.round( motorInsuranceModel.getIdv() + ( motorInsuranceModel.getIdv() * modifyYourIdv / 100 ) );
				motorInsuranceModel.setIdv(modifiedIdv);
				
				motorInsuranceModel.setTotalIdv("" + orginalIdv);
				motorInsuranceModel.setIdvFor1Year(Double.parseDouble(motorInsuranceModel.getTotalIdv()));
				motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
				
				double orginalIdv2 = motorInsuranceModel.getIdvFor2Year();
				long modifyYourIdv2 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountIDVPercent2Year());
				double modifiedIDVfor2Year = Math.round( orginalIdv2 + ( orginalIdv2 * modifyYourIdv2 / 100 ));
				motorInsuranceModel.setModifiedIdvfor2Year(modifiedIDVfor2Year);
				
				double orginalIdv3 = motorInsuranceModel.getIdvFor3Year();
				long modifyYourIdv3 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountIDVPercent3Year());
				double modifiedIDVfor3Year = Math.round( orginalIdv3 + ( orginalIdv3 * modifyYourIdv3 / 100 ));
				motorInsuranceModel.setModifiedIdvfor3Year(modifiedIDVfor3Year);
				
				double orginalIdv4 = motorInsuranceModel.getIdvFor4Year();
				long modifyYourIdv4 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountIDVPercent4Year());
				double modifiedIDVfor4Year = Math.round( orginalIdv4 + ( orginalIdv4 * modifyYourIdv4 / 100 ));
				motorInsuranceModel.setModifiedIdvfor4Year(modifiedIDVfor4Year);
				
				double orginalIdv5 = motorInsuranceModel.getIdvFor5Year();
				long modifyYourIdv5 =  (long) Double.parseDouble(motorInsuranceModel.getDiscountIDVPercent5Year());
				double modifiedIDVfor5Year = Math.round( orginalIdv5 + ( orginalIdv5 * modifyYourIdv5 / 100 ));
				motorInsuranceModel.setModifiedIdvfor5Year(modifiedIDVfor5Year);
				
				
			}else {
				
				LOGGER.info("Idv Value 2:: ");
				if( vehicleDetails.getIdv() == 0){
					motorInsuranceModel.setTotalIdv(""+motorInsuranceModel.getIdv());
					motorInsuranceModel.setDiscountidvPercent("0");
				}else {
					double orginalIdv = Math.round( vehicleDetails.getIdv() );
					double modifiedIdv = Math.round(vehicleDetails.getModifiedIdv());
					
					motorInsuranceModel.setTotalIdv("" + orginalIdv);
					motorInsuranceModel.setIdv(modifiedIdv == 0 ? orginalIdv : modifiedIdv);
				}

				motorInsuranceModel.setIdvFor1Year(Double.parseDouble(motorInsuranceModel.getTotalIdv()));
				motorInsuranceModel.setDiscountIDVPercent1Year(motorInsuranceModel.getDiscountidvPercent());
				motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
				
				if( vehicleDetails.getIdvFor2Year() == 0){
					motorInsuranceModel.setDiscountIDVPercent2Year("0");
					motorInsuranceModel.setModifiedIdvfor2Year(motorInsuranceModel.getIdvFor2Year());
				}else {
					double orginalIdv2 = Math.round(vehicleDetails.getIdvFor2Year());
					double modifiedIdvValue2 = Math.round( vehicleDetails.getModifiedIDVfor2Year() );
					
					motorInsuranceModel.setIdvFor2Year(orginalIdv2);
					motorInsuranceModel.setModifiedIdvfor2Year(modifiedIdvValue2 == 0 ? orginalIdv2 : modifiedIdvValue2);
				}
				
				if( vehicleDetails.getIdvFor3Year() == 0 ){
					motorInsuranceModel.setDiscountIDVPercent3Year("0");
					motorInsuranceModel.setModifiedIdvfor3Year(motorInsuranceModel.getIdvFor3Year());
				}else {
					double orginalIdv3 = Math.round( vehicleDetails.getIdvFor3Year() );
					double modifiedIdvValue3 = Math.round( vehicleDetails.getModifiedIDVfor3Year() ); 
					
					motorInsuranceModel.setIdvFor3Year(orginalIdv3);
					motorInsuranceModel.setModifiedIdvfor3Year(modifiedIdvValue3 == 0 ? orginalIdv3 : modifiedIdvValue3);
				}
				
				if( vehicleDetails.getIdvFor4Year() == 0 ){
					motorInsuranceModel.setDiscountIDVPercent4Year("0");
					motorInsuranceModel.setModifiedIdvfor4Year(motorInsuranceModel.getIdvFor4Year());
				}else {
					double orginalIdv4 = Math.round( vehicleDetails.getIdvFor4Year() );
					double modifiedIdvValue4 = Math.round( vehicleDetails.getModifiedIDVfor4Year() ); 
					
					motorInsuranceModel.setIdvFor4Year(orginalIdv4);
					motorInsuranceModel.setModifiedIdvfor4Year(modifiedIdvValue4 == 0 ? orginalIdv4 : modifiedIdvValue4);
				}
				
				if( vehicleDetails.getIdvFor5Year() == 0 ){
					motorInsuranceModel.setDiscountIDVPercent5Year("0");
					motorInsuranceModel.setModifiedIdvfor5Year(motorInsuranceModel.getIdvFor5Year());
				}else {
					double orginalIdv5 = Math.round( vehicleDetails.getIdvFor5Year() );
					double modifiedIdvValue5 = Math.round( vehicleDetails.getModifiedIDVfor5Year() ); 
					
					motorInsuranceModel.setIdvFor5Year(orginalIdv5);
					motorInsuranceModel.setModifiedIdvfor5Year(modifiedIdvValue5 == 0 ? orginalIdv5 : modifiedIdvValue5);
				}
				
			}
		}
			LOGGER.info("motorInsuranceModel.setIdvFor1Year = "+motorInsuranceModel.getIdvFor1Year());
			LOGGER.info("motorInsuranceModel.modified  = "+motorInsuranceModel.getModifiedIdvfor1Year());
		/* Task #249208 Policy bazaar IDV deviation */
			if("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
				
			  AgentPlanMapping agentPlanMapping = twoWheelerDAO.getAgentPlanMapping(motorInsuranceModel.getAgentId(), "TWOWHEELER", "MotorCyclePackage");
				if(agentPlanMapping != null 
							&& agentPlanMapping.getIdvDeviationPercent() != null
							&& StringUtils.isNotBlank(agentPlanMapping.getIdvDeviationPercent()) 
							&& NumberUtils.isNumber(agentPlanMapping.getIdvDeviationPercent())
							&& Integer.parseInt(agentPlanMapping.getIdvDeviationPercent()) != 0){
						
				int idvDeviationPercentage = Math.abs(Integer.parseInt(agentPlanMapping.getIdvDeviationPercent()));
				
				Double d = new Double(vehicleDetails.getIdv());
				String Idv = d.toString();
					if(vehicleDetails.getIdv() == 0 || StringUtils.isBlank(Idv)) {
						double idv = Double.valueOf(motorInsuranceModel.getTotalIdv());
						LOGGER.info("idv -Db value =" + motorInsuranceModel.getTotalIdv());
						double idvdec = (idv * idvDeviationPercentage) / 100;
						LOGGER.info("idvded =" + idvdec+" idvdeviation ="+(idv - idvdec));
						int idvdeviation = (int) Math.floor(idv - idvdec);
						motorInsuranceModel.setTotalIdv("" + idvdeviation);
						motorInsuranceModel.setIdv(idvdeviation);
						motorInsuranceModel.setIdvFor1Year(idvdeviation);
						motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
						motorInsuranceModel.setOriginalIdvFor1Year(String.valueOf(idvdeviation));
						LOGGER.info("idv =" + motorInsuranceModel.getIdv());
						LOGGER.info("TotalIdv =" + motorInsuranceModel.getTotalIdv());
						
						if (motorInsuranceModel.getPolicyTerm() >= 2 && vehicleDetails.getIdvFor2Year() == 0) {
							idv = motorInsuranceModel.getIdvFor2Year();
							idvdec = (idv * idvDeviationPercentage) / 100;
							LOGGER.info("idv dev 2 : "+idvdec);
							idvdeviation = (int) Math.floor(idv - idvdec);
							LOGGER.info("idvdeviation 2nd year = "+idvdeviation);
							motorInsuranceModel.setIdvFor2Year(idvdeviation);
							motorInsuranceModel.setModifiedIdvfor2Year(idvdeviation);
							motorInsuranceModel.setOriginalIdvFor2Year(String.valueOf(idvdeviation));
	
							LOGGER.info("idv 2nd year = "+motorInsuranceModel.getIdvFor2Year());
							if (motorInsuranceModel.getPolicyTerm() >= 3 && vehicleDetails.getIdvFor3Year() == 0) {
								
								idv = motorInsuranceModel.getIdvFor3Year();
								idvdec = (idv * idvDeviationPercentage) / 100;
								LOGGER.info("idv dev 3 : "+idvdec);
								idvdeviation = (int) Math.floor(idv - idvdec);
								LOGGER.info("idvdeviation 3nd year = "+idvdeviation);
								motorInsuranceModel.setIdvFor3Year(idvdeviation);
								motorInsuranceModel.setModifiedIdvfor3Year(idvdeviation);
								motorInsuranceModel.setOriginalIdvFor3Year(String.valueOf(idvdeviation));
								LOGGER.info("idv 3nd year = "+motorInsuranceModel.getIdvFor3Year());
								
								if (motorInsuranceModel.getPolicyTerm() >= 4 && vehicleDetails.getIdvFor4Year() == 0 ) {
									
									idv = motorInsuranceModel.getIdvFor4Year();
									idvdec = (idv * idvDeviationPercentage) / 100;
									LOGGER.info("idv dev 4 : "+idvdec);
									idvdeviation = (int) Math.floor(idv - idvdec);
									LOGGER.info("idvdeviation 4th year = "+idvdeviation);
									motorInsuranceModel.setIdvFor4Year(idvdeviation);
									motorInsuranceModel.setModifiedIdvfor4Year(idvdeviation);
									motorInsuranceModel.setOriginalIdvFor4Year(String.valueOf(idvdeviation));
									LOGGER.info("idv 4th year = "+motorInsuranceModel.getIdvFor4Year());
									
									if (motorInsuranceModel.getPolicyTerm() >= 5 && vehicleDetails.getIdvFor5Year() == 0  ) {
										
										idv = motorInsuranceModel.getIdvFor4Year();
										idvdec = (idv * idvDeviationPercentage) / 100;
										LOGGER.info("idv dev 5 : "+idvdec);
										idvdeviation = (int) Math.floor(idv - idvdec);
										LOGGER.info("idvdeviation 5th year = "+idvdeviation);
										motorInsuranceModel.setIdvFor5Year(idvdeviation);
										motorInsuranceModel.setModifiedIdvfor5Year(idvdeviation);
										motorInsuranceModel.setOriginalIdvFor5Year(String.valueOf(idvdeviation));
										LOGGER.info("idv 5th year = "+motorInsuranceModel.getIdvFor5Year());
									}
								}
							}
						}
					}
				}
			}/* End - Task #249208 Policy bazaar IDV deviation */
		}
		
	}
			

/* Task #274702 15% IDV negative deviation for cover fox-151133 */
			/*if ("BA502116".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
				LOGGER.info("idv =" + motorInsuranceModel.getIdv());
				Double d=new Double(vehicleDetails.getIdv());
				String Idv=d.toString();
				if ((StringUtils.isBlank(Idv)) || (vehicleDetails.getIdv()) == 0) {
					double idv = motorInsuranceModel.getIdv();
					LOGGER.info("idv -Db value =" + motorInsuranceModel.getIdv());
					double idvdec = (idv * 15) / 100;
					LOGGER.info("idvded =" + idvdec+" idvdeviation ="+(idv - idvdec));
					int idvdeviation =(int) Math.floor(idv - idvdec);
					motorInsuranceModel.setTotalIdv("" + idvdeviation);
					motorInsuranceModel.setIdv(idvdeviation);
					motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
					LOGGER.info("idv =" + motorInsuranceModel.getIdv());
					LOGGER.info("TotalIdv =" + motorInsuranceModel.getTotalIdv());
				}
			}
			LOGGER.info("MODIFIED :: "+motorInsuranceModel.getModifiedIdvfor1Year());*/
		
	
	
	@Override
	public <T> void setCovers(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		VehicleDetails vehicleDetails = (VehicleDetails) t;

		// Do you Need Cover for Driver / Other Passengers / Two Wheeler / Accessories?
		
		motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengers(vehicleDetails.getPersonalAccidentCoverForUnnamedPassengers()); // Personal Accident Cover for Unnamed Passengers
		if(vehicleDetails.getAccidentCoverForPaidDriver()!= null && StringUtils.isNotBlank(vehicleDetails.getAccidentCoverForPaidDriver())){	
			if(StringUtils.equalsIgnoreCase("50000",vehicleDetails.getAccidentCoverForPaidDriver()) 
						|| StringUtils.equalsIgnoreCase("100000", vehicleDetails.getAccidentCoverForPaidDriver()))
					motorInsuranceModel.setAccidentcoverforpaiddriver(vehicleDetails.getAccidentCoverForPaidDriver()); // Accident Cover for Paid Driver
				else
					motorInsuranceModel.setAccidentcoverforpaiddriver("0");
		}else
			motorInsuranceModel.setAccidentcoverforpaiddriver("0");

		motorInsuranceModel.setLegalliabilitytopaiddriver(vehicleDetails.getLegalliabilityToPaidDriver()); // Legal Liability to be Paid to Driver

		motorInsuranceModel.setLegalliabilitytoemployees(vehicleDetails.getLegalliabilityToEmployees()); // Legal Liability to be Paid to Employee - If vehicle registered in the name of Company

		LOGGER.info("vehicleDetails ::: " + objectMapper.writeValueAsString(vehicleDetails));
		// Do you Need Cover for Non-Electrical Accessories?
		if ("yes".equalsIgnoreCase(vehicleDetails.getCover_non_elec_acc())
				&& vehicleDetails.getNonElectricalAccesories() != null 
				&& CollectionUtils .isNotEmpty(vehicleDetails.getNonElectricalAccesories().getNonelectronicAccessoriesDetails())) {
			List<NonElectronicValues> nonElectricalAccessoriesList = new ArrayList<NonElectronicValues>();
			List<NonElectricalAccessoriesDetails> list = vehicleDetails.getNonElectricalAccesories().getNonelectronicAccessoriesDetails();
			long totneval = 0;
			LOGGER.info("list.size() ::: " + list.size());
			for (NonElectricalAccessoriesDetails nonElectricalAccessoriesDetails : list) {
				NonElectronicValues nonElectronicValues = new NonElectronicValues();
				nonElectronicValues.setNonElectronicNameOfElectronicAccessories(nonElectricalAccessoriesDetails.getNameOfElectronicAccessories());
				nonElectronicValues.setNonElectronicMakeModel(nonElectricalAccessoriesDetails.getMakeModel());
				nonElectronicValues.setNonElectronicValue(nonElectricalAccessoriesDetails.getValue() + "");
				totneval = totneval + nonElectricalAccessoriesDetails.getValue();
				nonElectricalAccessoriesList.add(nonElectronicValues);
					
			}
			motorInsuranceModel.setNonElectricalAccessoriesList(nonElectricalAccessoriesList);
			motorInsuranceModel.setValueofnonelectricalaccessories(totneval + "");
			motorInsuranceModel.setNonElectronicValue(Long.toString(totneval));
			
		} else {
			motorInsuranceModel.setValueofnonelectricalaccessories(null);
			motorInsuranceModel.setNonElectricalAccessoriesList(null);
			motorInsuranceModel.setNonElectronicValue(null);
		}

		// Do you Need Cover for Electrical Accessories?
		if ("yes".equalsIgnoreCase(vehicleDetails.getCover_elec_acc()) 
				&& vehicleDetails.getElectricalAccessories() != null 
				&& CollectionUtils .isNotEmpty(vehicleDetails.getElectricalAccessories().getElectronicAccessoriesDetails())) {
			List<ElectronicValues> electricalAccessoriesList = new ArrayList<ElectronicValues>();
			List<ElectricalAccessoriesDetails> listElectricals = vehicleDetails.getElectricalAccessories().getElectronicAccessoriesDetails();
			long totneval=0;
			LOGGER.info("electricalAccessoriesList.size() ::: " + listElectricals.size());
			for (ElectricalAccessoriesDetails electricalAccessoriesDetails : listElectricals) {
				ElectronicValues electronicValues = new ElectronicValues();
				electronicValues.setElectronicNameOfElectronicAccessories(electricalAccessoriesDetails.getNameOfElectronicAccessories());
				electronicValues.setElectronicMakeModel(electricalAccessoriesDetails.getMakeModel());
				electronicValues.setElectronicValue(electricalAccessoriesDetails.getValue()+"");
				totneval = totneval + electricalAccessoriesDetails.getValue();
				electricalAccessoriesList.add(electronicValues);
			}
			motorInsuranceModel.setValueofelectricalaccessories(totneval + "");
			motorInsuranceModel.setElectricalAccessoriesList(electricalAccessoriesList);
			motorInsuranceModel.setElectronicValue(Long.toString(totneval));
			

		} else {
			motorInsuranceModel.setValueofelectricalaccessories(null);
			motorInsuranceModel.setElectricalAccessoriesList(null);
			motorInsuranceModel.setElectronicValue(null);
		}
		/**
		 * Task #141300 Add-on cover additional Towing charges
		 */
		motorInsuranceModel.setTowingChargesCover(vehicleDetails.getTowingChargesCover());
		if(Constants.YES.equalsIgnoreCase(motorInsuranceModel.getTowingChargesCover())) {
			
			if (StringUtils.isNotBlank(vehicleDetails.getTowingChargesCover_SI())) {
				try {
					motorInsuranceModel.setAdditionalTowingChargesCover(vehicleDetails.getTowingChargesCover_SI());
					motorInsuranceModel.setTowingChargesCoverSI(Double.parseDouble(vehicleDetails.getTowingChargesCover_SI()));
					
				} catch (NumberFormatException e) {
					LOGGER.info(e.getMessage(), e);
				}
			}
		}
		
		
	}
	
	@Override
	public <T> void setAdditionalDetails(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		CalculatePremiumRequest calculatePremiumRequest = (CalculatePremiumRequest) t;
		VehicleDetails vehicleDetails = calculatePremiumRequest.getVehicleDetails();
		motorInsuranceModel.setRegistrationNumber(vehicleDetails.getRegistrationNumber());
		motorInsuranceModel.setEngineNumber(vehicleDetails.getEngineNumber());
		motorInsuranceModel.setChassisNumber(vehicleDetails.getChassisNumber());
		motorInsuranceModel.setIsCarFinanced(vehicleDetails.getIsTwoWheelerFinanced());
		motorInsuranceModel.setIsCarFinancedValue(vehicleDetails.getIsTwoWheelerFinancedValue());
		motorInsuranceModel.setFinancierName(vehicleDetails.getFinancierName());
		
		setNominationforPA(calculatePremiumRequest.getProposerDetails(), motorInsuranceModel);
		
		setContactAddress(calculatePremiumRequest.getProposerDetails(), motorInsuranceModel);
		
		setVehicleRegistrationAddress(calculatePremiumRequest.getProposerDetails(), motorInsuranceModel);
	}
	
	
	@Override
	public <T> void setNominationforPA(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		ProposerDetails proposerDetails = (ProposerDetails) t;
		
		if(INDIVIDUAL.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
			motorInsuranceModel.setNominee_Name(proposerDetails.getNomineeName());
			motorInsuranceModel.setNominee_Age(proposerDetails.getNomineeAge());
			motorInsuranceModel.setRelationship_with_nominee(proposerDetails.getRelationshipWithNominee());
			motorInsuranceModel.setGuardian_Name(proposerDetails.getGuardianName());
			motorInsuranceModel.setGuardian_Age(proposerDetails.getGuardianAge());
			motorInsuranceModel.setRelationship_with_Guardian(proposerDetails.getRelationshipwithGuardian());
			motorInsuranceModel.setLegalliabilitytoemployees("No");
		}
		
		if (COMPANY.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())  ||
				("Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf()) 
						&& NO.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired()))) {
			
			motorInsuranceModel.setNominee_Name("");
			motorInsuranceModel.setNominee_Age("");
			motorInsuranceModel.setRelationship_with_nominee("");
			motorInsuranceModel.setGuardian_Name("");
			motorInsuranceModel.setGuardian_Age("");
			motorInsuranceModel.setRelationship_with_Guardian("");
		}
		
		if (StringUtils.isNumeric(motorInsuranceModel.getNominee_Age())
				&& (Integer.parseInt(motorInsuranceModel.getNominee_Age()) >= 18)) {
			motorInsuranceModel.setGuardian_Name("");
			motorInsuranceModel.setGuardian_Age("");
			motorInsuranceModel.setRelationship_with_Guardian("");
		}
	}
	
	@Override
	public <T> void setContactAddress(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		ProposerDetails proposerDetails = (ProposerDetails) t;
		
		motorInsuranceModel.setContactAddress1(proposerDetails.getResidenceAddressOne());
		motorInsuranceModel.setContactAddress2(proposerDetails.getResidenceAddressTwo());
		motorInsuranceModel.setContactAddress3(proposerDetails.getResidenceAddressThree());
		motorInsuranceModel.setContactAddress4(proposerDetails.getResidenceAddressFour());
		motorInsuranceModel.setContactCity(proposerDetails.getResidenceCity());
		motorInsuranceModel.setContactPincode(proposerDetails.getResidencePinCode());
		motorInsuranceModel.setAadharNumber(proposerDetails.getAadharNumber());
		motorInsuranceModel.setPanNumber(proposerDetails.getPanNumber());
		motorInsuranceModel.setUpdatePanAaadharLater(proposerDetails.getUpdatePanAaadharLater());
		/****** #141301 City – State in PDF - Phase II *****/
		if(StringUtils.isNotBlank(proposerDetails.getResidenceCity())) ipcServices.setContactState(motorInsuranceModel);
		LOGGER.info("CONTACT STATE ::: "+motorInsuranceModel.getContactState());
	}
	
	@Override
	public <T> void setVehicleRegistrationAddress(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		ProposerDetails proposerDetails = (ProposerDetails) t;
		
		motorInsuranceModel.setIsSameASRegistrationAddress(proposerDetails.getSameAdressReg());
		if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())) {
			motorInsuranceModel.setAddressOne(proposerDetails.getResidenceAddressOne());
			motorInsuranceModel.setAddressTwo(proposerDetails.getResidenceAddressTwo());
			motorInsuranceModel.setAddressThree(proposerDetails.getResidenceAddressThree());
			motorInsuranceModel.setAddressFour(proposerDetails.getResidenceAddressFour());
			motorInsuranceModel.setRegCity(proposerDetails.getResidenceCity());
			motorInsuranceModel.setRegPinCode(proposerDetails.getResidencePinCode());
		} else {
			motorInsuranceModel.setAddressOne(proposerDetails.getPermanentAddress1());
			motorInsuranceModel.setAddressTwo(proposerDetails.getPermanentAddress2());
			motorInsuranceModel.setAddressThree(proposerDetails.getPermanentAddress3());
			motorInsuranceModel.setAddressFour(proposerDetails.getPermanentAddress4());
			motorInsuranceModel.setRegCity(proposerDetails.getPermanentCity());
			motorInsuranceModel.setRegPinCode(proposerDetails.getPermanentPincode());
		}
	}
	
	@Override
	public <T> void setLongTermProductValues(MotorInsuranceModel motorInsuranceModel) throws Exception {
		formPost.setLongTermProductValues(motorInsuranceModel);
	}
	
	private void getCityStateDetails(MotorInsuranceModel motorInsuranceModel) {
		if (StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegisteredCity())) {
			CityStateLookupModel cityStateLookupModel = twoWheelerDAO
					.getCityState(motorInsuranceModel.getVehicleRegisteredCity());
			motorInsuranceModel.setState(cityStateLookupModel.getState());
			motorInsuranceModel.setZone(cityStateLookupModel.getZone());
			motorInsuranceModel.setCityCode(cityStateLookupModel.getCityCode());
			motorInsuranceModel.setStateCode(cityStateLookupModel.getStateCode());
			motorInsuranceModel.setRegion(cityStateLookupModel.getRegion());
		}
	}
	
}
