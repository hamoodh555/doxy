package com.xerago.rsa.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.domain.DPolicyCoverages;
import com.xerago.rsa.domain.DPolicyDetails;
import com.xerago.rsa.domain.DPolicyVehicleDetails;
import com.xerago.rsa.domain.DPoscodeDetails;
import com.xerago.rsa.domain.DQuoteDetails;
import com.xerago.rsa.domain.DUpdatedClientDetails;
import com.xerago.rsa.domain.GstMaster;
import com.xerago.rsa.dto.response.ApiResponse;
import com.xerago.rsa.dto.response.Liability;
import com.xerago.rsa.dto.response.OdPremium;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.PremiumDetailsData;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.dto.response.VahanResponse;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.tax.impl.GST;
import com.xerago.rsa.util.Constants;
import com.xerago.rsa.util.MotorValidation;

@Service
public class UpdateVehicleDetails extends Process {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Value("${com.xerago.rsa.d3clocal}")
	private String d2cServiceUrlLocal;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	GST gst;
	
	@Override
	public void setPremiumDetailsStatus(PremiumDetails premiumDetails, Status status, MotorInsuranceModel motorInsuranceModel, DUpdatedClientDetails dUpdatedClientDetails)  throws Exception{
		if (premiumDetails.getData() != null) {
			PremiumDetailsData premiumDetailsData = new PremiumDetailsData();
			VahanResponse vahanResponse = new VahanResponse();	
			
			if(!"BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
				LOGGER.info("getIDV1 ::: "+motorInsuranceModel.getIdv());
				if(motorInsuranceModel.getPolicyTerm()==1){
					premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
				}else if(motorInsuranceModel.getPolicyTerm()==2){
					premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor2Year());
				}else if(motorInsuranceModel.getPolicyTerm()==3){
					premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor3Year());
				}else if(motorInsuranceModel.getPolicyTerm()==4){
					premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor4Year());
				}else if(motorInsuranceModel.getPolicyTerm()==5){
					premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor5Year());
				}else {
					premiumDetailsData.setIdv(motorInsuranceModel.getTotalIdv()!=null ? Double.parseDouble(motorInsuranceModel.getTotalIdv()) :0);
				}
				
			} else {
				LOGGER.info("getIDV2 ::: "+motorInsuranceModel.getIdv());
				if(motorInsuranceModel.getPolicyTerm()==1){
					premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor1Year());
				}else if(motorInsuranceModel.getPolicyTerm()==2){
					premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor2Year());
				}else if(motorInsuranceModel.getPolicyTerm()==3){
					premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor3Year());
				}else if(motorInsuranceModel.getPolicyTerm()==4){
					premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor4Year());
				}else if(motorInsuranceModel.getPolicyTerm()==5){
					premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
				}else {
					premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
				}
				LOGGER.info("getProductName::: "+premiumDetailsData.getIdv());
			}
			
			vahanResponse.setMakerName(motorInsuranceModel.getVahanMakerName());
			vahanResponse.setMakerModel(motorInsuranceModel.getVahanModelName());
			vahanResponse.setResponse(motorInsuranceModel.getVahanResponse());
			vahanResponse.setResponseMessage(motorInsuranceModel.getVahanMessage());
			premiumDetailsData.setVahanResponse(vahanResponse);
			
			if(Constants.AGENT_RSAI.equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
				//premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
				premiumDetailsData.setPremium(premiumDetails.getData().getPremium());
				premiumDetailsData.setQuoteId(premiumDetails.getData().getQuoteId());
				premiumDetailsData.setPolicyTerm(premiumDetails.getData().getPolicyTerm());
				premiumDetailsData.setLiabilityPolicyTerm(premiumDetails.getData().getLiabilityPolicyTerm());
				premiumDetailsData.setClientCode(motorInsuranceModel.getClientCode());
				premiumDetailsData.setDescription("Vehicle Additional details updation success");
				premiumDetails.setData(premiumDetailsData);
				status.setMessage("Vehicle Additional details updation success");
			} else {
				premiumDetailsData = premiumDetails.getData();
				premiumDetailsData.setClientCode(motorInsuranceModel.getClientCode());
				premiumDetailsData.setDescription("Vehicle Additional details updation success");
				status.setMessage("Premium Calculated and Vehicle details saved successfully");
			}
			LOGGER.info("motorInsuranceModel.getClientCode() :::: " + motorInsuranceModel.getClientCode());
			
			//status = new Status(); 
			//status.setMessage("Premium Calculated and Vehicle details saved successfully");
			status.setStatusCode("S-0005");
			premiumDetails.setStatus(status);
		} else {
			//status = new Status(); 
			status.setMessage("Vehicle Additional details updation failure");
			status.setStatusCode("E-0001");
			premiumDetails.setData(null);
			premiumDetails.setStatus(status);
		}
		super.setPremiumDetailsStatus(premiumDetails, status, motorInsuranceModel, dUpdatedClientDetails);
	}
	/**
	 * @author roshini [Roshini G]
	 * <h1> STP Update Vehicle Details.</h1>
	 * <p> GST values are updated by given contact city.
	 * @param motorInsuranceModel
	 * @return
	 * @since 2019-12-10
	 */
	public PremiumDetails updateVehicleDetails(MotorInsuranceModel motorInsuranceModel) {
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		try {
			String quoteId = motorInsuranceModel.getQuoteId();
			DQuoteDetails dQuoteDetails = twoWheelerDAO.get(DQuoteDetails.class, quoteId);
			if(dQuoteDetails == null) {
				status.setMessage("Please Provide Correct QuoteId.");
				status.setStatusCode("E-0064");
				premiumDetails.setData(null);
				premiumDetails.setStatus(status);
				return premiumDetails;
			}
			DPolicyDetails dPolicyDetails = new ArrayList<DPolicyDetails>(dQuoteDetails.getDPolicyDetailses()).get(0);
			DPolicyVehicleDetails dPolicyVehicleDetails = new ArrayList<DPolicyVehicleDetails>(dPolicyDetails.getDPolicyVehicleDetailses()).get(0);
			LOGGER.info("quote id :: "+quoteId);
			if(dQuoteDetails != null) {
				
				dQuoteDetails =	setGST(dQuoteDetails, motorInsuranceModel);
				motorInsuranceModel.setPremium(dQuoteDetails.getPremium().doubleValue());
				Date dtInception = null;
				Date dtExpiry = null;

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
				dtInception = simpleDateFormat.parse(motorInsuranceModel.getPolicyStartDate());
				dtExpiry = simpleDateFormat.parse(new MotorValidation().setExpiryDate(motorInsuranceModel));
				new java.sql.Date(dtInception.getTime());
				new java.sql.Date(dtExpiry.getTime());
				
			    dPolicyDetails  = setDPolicyDetails(motorInsuranceModel, dQuoteDetails, dtInception, dtExpiry, dPolicyDetails);
				dPolicyVehicleDetails = setDPolicyVehicelDetails(motorInsuranceModel, dPolicyDetails, dPolicyVehicleDetails);
				DUpdatedClientDetails updatedClientDetails = setDUpdatedClientDetails(motorInsuranceModel, dQuoteDetails);
				DPoscodeDetails dPoscodeDetails = setDPoscodeDetails(motorInsuranceModel,dQuoteDetails);
				setDTPExistingPolicyDetails(motorInsuranceModel);
				Integer returnCode = 0;
				returnCode = twoWheelerDAO.setInsetData(dQuoteDetails,dPolicyDetails, dPolicyVehicleDetails, updatedClientDetails, dPoscodeDetails);
				LOGGER.info("returnCode - "+returnCode);
				if(returnCode == 1) {
					PremiumDetailsData data = new PremiumDetailsData();
					data.setPremium(dQuoteDetails.getPremium().toString());
					data.setPolicyTerm(motorInsuranceModel.getPolicyTerm() );
					data.setLiabilityPolicyTerm(String.valueOf(motorInsuranceModel.getLiabilityPolicyTerm()));
					data.setQuoteId(motorInsuranceModel.getQuoteId());
					premiumDetails.setData(data);
					setPremiumDetailsStatus(premiumDetails, status, motorInsuranceModel, updatedClientDetails);
				} else {
					status.setMessage("Vehicle Additional details updation failure");
					status.setStatusCode("E-0001");
					premiumDetails.setData(null);
					premiumDetails.setStatus(status);
				}
			}
		
		} catch (Exception e) {
			LOGGER.info("STP Flow Failed :: "+e.getMessage(), e);
			status.setMessage("Update Vehicle Details Failed.");
			status.setStatusCode("E-0004");
			premiumDetails.setData(null);
			premiumDetails.setStatus(status);
		}
		return premiumDetails;
	}

	private DQuoteDetails setGST(DQuoteDetails dQuoteDetails, MotorInsuranceModel motorInsuranceModel) {
		
		BigDecimal cgst = dQuoteDetails.getCgst();
		BigDecimal sgst = dQuoteDetails.getSgst();
		BigDecimal igst = dQuoteDetails.getIgst();
		BigDecimal ugst = dQuoteDetails.getUgst();

		BigDecimal cgstRate = dQuoteDetails.getCgstRate();
		BigDecimal sgstRate = dQuoteDetails.getSgstRate();
		BigDecimal igstRate = dQuoteDetails.getIgstRate();
		BigDecimal ugstRate = dQuoteDetails.getUgstRate();

		BigDecimal totalGst = cgst.add(sgst).add(igst).add(ugst);
		LOGGER.info("totalGst = " + totalGst);
		// GST Validation
		GstMaster gstMaster;
		try {
			gstMaster = gst.getGstMaster(motorInsuranceModel.getAgentId(), motorInsuranceModel.getPolicyStartDate());
			if (gstMaster != null && StringUtils.isNotBlank(motorInsuranceModel.getContactState())
					&& StringUtils.isNotBlank(gstMaster.getState())
					&& StringUtils.equalsIgnoreCase(motorInsuranceModel.getContactState(), gstMaster.getState())) {
				LOGGER.info("gstMaster State :: " + gstMaster.getState());
				LOGGER.info("gstMaster city :: " + gstMaster.getCity());
				LOGGER.info("CONTACT STATE ::: " + motorInsuranceModel.getContactState());
				cgst = BigDecimal.valueOf(totalGst.doubleValue() / 2);
				sgst = BigDecimal.valueOf(totalGst.doubleValue() / 2);
				igst = BigDecimal.valueOf(0.0);
				ugst = BigDecimal.valueOf(0.0);

				cgstRate = BigDecimal.valueOf(0.09);
				sgstRate = BigDecimal.valueOf(0.09);
				igstRate = BigDecimal.valueOf(0.0);
				ugstRate = BigDecimal.valueOf(0.0);
			} else if (gstMaster != null && StringUtils.isNotBlank(motorInsuranceModel.getContactState())
					&& StringUtils.isNotBlank(gstMaster.getState())
					&& !StringUtils.equalsIgnoreCase(motorInsuranceModel.getContactState(), gstMaster.getState())) {
				LOGGER.info("gstMaster State :: " + gstMaster.getState());
				LOGGER.info("gstMaster city :: " + gstMaster.getCity());
				LOGGER.info("CONTACT STATE ::: " + motorInsuranceModel.getContactState());
				cgst = BigDecimal.valueOf(0.0);
				sgst = BigDecimal.valueOf(0.0);
				igst = totalGst;
				ugst = BigDecimal.valueOf(0.0);

				cgstRate = BigDecimal.valueOf(0.0);
				sgstRate = BigDecimal.valueOf(0.0);
				igstRate = BigDecimal.valueOf(0.18);
				ugstRate = BigDecimal.valueOf(0.0);
			}
		} catch (Exception e) {
			LOGGER.error("Error:: in GST Values :: " + e.getMessage(), e);
		}
		dQuoteDetails.setCgst(cgst);
		dQuoteDetails.setSgst(sgst);
		dQuoteDetails.setIgst(igst);
		dQuoteDetails.setUgst(ugst);

		dQuoteDetails.setCgstRate(cgstRate);
		dQuoteDetails.setSgstRate(sgstRate);
		dQuoteDetails.setIgstRate(igstRate);
		dQuoteDetails.setUgstRate(ugstRate);
		dQuoteDetails.setStatus('Q');
		return dQuoteDetails;
	}
	
	
}
