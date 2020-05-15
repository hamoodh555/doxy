package com.xerago.rsa.webproxy.transform;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xerago.rsa.communication.IPCServices;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.domain.AgentMaster;
import com.xerago.rsa.domain.AgentPlanMapping;
import com.xerago.rsa.domain.GstMaster;
import com.xerago.rsa.dto.request.PosDetails;
import com.xerago.rsa.model.CityStateLookupModel;
import com.xerago.rsa.model.ElectronicValues;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.model.NonElectronicValues;
import com.xerago.rsa.tax.impl.GST;
import com.xerago.rsa.util.Constants;
import com.xerago.rsa.util.MotorValidation;

@Component
public class FormPost implements Request, Constants {

	private static final Logger LOGGER = LogManager.getRootLogger();
	public static final String FORM_POST_CPA_POLICY_TERM = "cpaPolicyTerm";

	@Autowired
	IPCServices ipcServices;
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	
	@Autowired
	GST gst;
	
	@Override
	public <T> MotorInsuranceModel parseValue(T t) throws Exception {
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();
		
		HashMap<String, String> formPost = (HashMap<String, String>) t;
		
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
		
		/**
		 * Generic Informations
		 */
		motorInsuranceModel.setProcessType(formPost.get("process"));
		motorInsuranceModel.setReqType(formPost.get("reqType"));
		motorInsuranceModel.setApiKey(formPost.get("apikey"));
		motorInsuranceModel.setTypeOfAction(formPost.get("typeOfAction"));
		motorInsuranceModel.setPlanName(PLAN_MOTOR_SHIELD_TWOWHEELER);
		motorInsuranceModel.setVehicleSubLine(formPost.get("vehicleSubLine"));
		motorInsuranceModel.setProductName(formPost.get("ProductName"));
		
		if("BrandnewTwowheeler".equalsIgnoreCase(formPost.get("ProductName"))){
			motorInsuranceModel.setProductName("BrandnewTwowheeler");
		}else{
			motorInsuranceModel.setProductName("RolloverTwowheeler");
			motorInsuranceModel.setIsPreviousPolicyHolder(TRUE);
		}

		motorInsuranceModel.setChannelCode(formPost.get("channel"));

		/**
		 * Task #146177 Two wheeler long term product - TECH
		 */
		int policyTerm = StringUtils.isNotBlank(formPost.get("policyTerm"))? Integer.parseInt(formPost.get("policyTerm")) : 1; 
		motorInsuranceModel.setPolicyTerm(policyTerm);
		motorInsuranceModel.setLiabilityPolicyTerm(policyTerm);
		
		String typeOfCover = StringUtils.isNotBlank(formPost.get("typeOfCover")) ? formPost.get("typeOfCover") : "" ;
		if("BrandNewTwoWheeler".equalsIgnoreCase(formPost.get("ProductName"))) {
			if(StringUtils.isBlank(formPost.get("typeOfCover"))){
				typeOfCover = "";
			}else if ("Bundled".equalsIgnoreCase(formPost.get("typeOfCover"))){
				typeOfCover = "Comprehensive";
				motorInsuranceModel.setPolicyTerm(1);
				motorInsuranceModel.setLiabilityPolicyTerm(5);
				if (NumberUtils.isParsable(formPost.get("cpaPolicyTerm"))) {
					motorInsuranceModel.setCpaPolicyTerm(NumberUtils.createInteger(formPost.get("cpaPolicyTerm")));
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
				if (NumberUtils.isParsable(formPost.get("cpaPolicyTerm"))) {
					motorInsuranceModel.setCpaPolicyTerm(NumberUtils.createInteger(formPost.get("cpaPolicyTerm")));
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
				}  else{
					typeOfCover = "Comprehensive";
				}
			}else
				typeOfCover = "Comprehensive";

		}
		if("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType()) && "BrandNewTwoWheeler".equalsIgnoreCase(formPost.get("ProductName"))) {
				typeOfCover = "Comprehensive";
				motorInsuranceModel.setPolicyTerm(5);
				motorInsuranceModel.setLiabilityPolicyTerm(5);
				motorInsuranceModel.setCpaPolicyTerm(5);
		} else 	if("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType()) && "RollOverTwoWheeler".equalsIgnoreCase(formPost.get("ProductName"))) {
				typeOfCover = "Comprehensive";
				motorInsuranceModel.setPolicyTerm(5);
				motorInsuranceModel.setLiabilityPolicyTerm(5);
				motorInsuranceModel.setCpaPolicyTerm(5);
			}

		motorInsuranceModel.setTypeOfCover(typeOfCover);

		// default to liability policy term for cpa if not set
		if (motorInsuranceModel.getCpaPolicyTerm() == 0) {
			motorInsuranceModel.setCpaPolicyTerm(
					NumberUtils.isParsable(formPost.get(FORM_POST_CPA_POLICY_TERM))
							? NumberUtils.createInteger(formPost.get(FORM_POST_CPA_POLICY_TERM))
							: motorInsuranceModel.getLiabilityPolicyTerm());
		}

		LOGGER.info("Vehcile Subline :: " + motorInsuranceModel.getVehicleSubLine() );
		motorInsuranceModel.setQuoteId(formPost.get("quoteId"));
		LOGGER.info("FormPOst::::"+formPost.get("quoteId"));
		motorInsuranceModel.setAgentId(StringUtils.isNotBlank(formPost.get("agentId")) ? formPost.get("agentId") : formPost.get("partner"));
		if (StringUtils.isBlank(motorInsuranceModel.getAgentId())) {
			motorInsuranceModel.setAgentId(AGENT_RSAI);
		}
		
		/**
		 * Campaign code capture
		 */
		googleCampaignCodeCapture(formPost, motorInsuranceModel);
						
		/**
		 * Vehicle Details 
		 */
		String yearOfManufacture = formPost.get("yearOfManufacture");
		try {
			if (yearOfManufacture != null && !"null".equalsIgnoreCase(yearOfManufacture)) {
				motorInsuranceModel.setYearOfManufacture(Integer.parseInt(yearOfManufacture));
			}
		} catch (Exception e) {
			motorInsuranceModel.setYearOfManufacture(0);
		}
		motorInsuranceModel.setVehicleRegisteredCity(formPost.get("carRegisteredCity"));
		motorInsuranceModel.setVehicleManufacturerName(formPost.get("vehicleManufacturerName"));
		motorInsuranceModel.setVehicleMakeCode(formPost.get("vehicleManufacturerName"));
		motorInsuranceModel.setVehicleModelCode(formPost.get("vehicleModelCode"));
		LOGGER.info("VehicleModelCode : " + motorInsuranceModel.getVehicleModelCode());
		motorInsuranceModel.setSource(formPost.get("source"));
		
		/**
		 * Task #342448 Manufacture list fetching not
		 * required for services Getting State, Zone, Region, CityCode,
		 * StateCode from getCityStateDetails()
		 */
		//ipcServices.getManufacturerNameList(motorInsuranceModel);
		getCityStateDetails(motorInsuranceModel);
		setIDV_PreviousPolicyDetails(formPost, motorInsuranceModel);
		
		/**
		 * Vehicle Details
		 */
		motorInsuranceModel.setVehicleRegisteredInTheNameOf(formPost.get("vehicleRegisteredInTheNameOf"));
		motorInsuranceModel.setCarOwnerShip(formPost.get("carOwnerShip"));
		if("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
			motorInsuranceModel.setCarOwnerShip("No");
		}
		motorInsuranceModel.setDrivingExperience(StringUtils.isNumeric(formPost.get("drivingExperience")) ? Double.parseDouble(formPost.get("drivingExperience")) : 0);
		
		/**
		 * Personal Information
		 */
		motorInsuranceModel.setStrTitle(formPost.get("strTitle"));
		
		motorInsuranceModel.setGender("Mr".equalsIgnoreCase(formPost.get("strTitle")) ? "Male" : "Female" );
		LOGGER.info("Aggregators Title -1 :: " + formPost.get("strTitle") + "Title -2 :  "
				+ motorInsuranceModel.getStrTitle());
	
		motorInsuranceModel.setStrFirstName(formPost.get("strFirstName"));
		motorInsuranceModel.setStrLastName(formPost.get("strLastName"));
		motorInsuranceModel.setStrEmail(formPost.get("strEmail"));
		motorInsuranceModel.setStrName(motorInsuranceModel.getStrFirstName());
		motorInsuranceModel.setStrMobileNo(formPost.get("strMobileNo"));
		motorInsuranceModel.setDateOfBirth(formPost.get("dateOfBirth"));
		//motorInsuranceModel.setGender(formPost.get("gender"));
		motorInsuranceModel.setEiaNumber(formPost.get("eiaNumber"));
		motorInsuranceModel.setIrName(formPost.get("irName"));
		LOGGER.info("Form DOB:"+formPost.get("dateOfBirth"));
		motorInsuranceModel.setStrPhoneNo(StringUtils.isNotBlank(formPost.get("strStdCode"))?formPost.get("strStdCode"):"" + "-" + (StringUtils.isNotBlank(formPost.get("strPhoneNo"))?formPost.get("strPhoneNo"):""));
		LOGGER.info("Occupation 1 : " + formPost.get("occupation") + "Condition ::"+ occupationList.containsKey(formPost.get("occupation")));
		if (occupationList.containsKey(formPost.get("occupation"))) {
			LOGGER.info("Occupation 2 : true" + formPost.get("occupation"));
			motorInsuranceModel.setOccupation(formPost.get("occupation"));
			motorInsuranceModel.setOccupationCode(occupationList.get(motorInsuranceModel.getOccupation()));
		} else {
			LOGGER.info("Occupation 3 :  false" + formPost.get("occupation"));
			motorInsuranceModel.setOccupation(formPost.get("occupation"));
			motorInsuranceModel.setOccupationCode(occupationList.get("Others"));
		}
		LOGGER.info("Occupation 4 :  " + motorInsuranceModel.getOccupationCode());
		
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
		LOGGER.info("strPolicyType ::::: " + strPolicyType);
		LOGGER.info("InceptionTime ::::: " + motorInsuranceModel.getInceptionTime());
		if (StringUtils.isNotBlank(motorInsuranceModel.getAgentId())
				&& DBPARTNER_CITI.equalsIgnoreCase(motorInsuranceModel.getAgentId())
				&& StringUtils.isNotBlank(strPolicyType) && THIRDPARTY_ONLY.equalsIgnoreCase(strPolicyType)) {
			motorInsuranceModel.setNoClaimBonusPercent("0");
		}

		motorInsuranceModel.setVoluntarydeductible(formPost.get("voluntarydeductible"));
		
		String mobileno = StringUtils.isNotBlank(motorInsuranceModel.getStrMobileNo())
				? motorInsuranceModel.getStrMobileNo().length() >= 5
						? motorInsuranceModel.getStrMobileNo().substring(0, 5) : motorInsuranceModel.getStrMobileNo()
				: "";
		String emailId = StringUtils.isNotBlank(motorInsuranceModel.getStrEmail())
				? motorInsuranceModel.getStrEmail().length() >= 5 ? motorInsuranceModel.getStrEmail().substring(0, 5)
						: motorInsuranceModel.getStrEmail()
				: "";
		motorInsuranceModel.setUserId(mobileno + emailId);
		
		setCovers(formPost, motorInsuranceModel);
		
		setAdditionalDetails(formPost, motorInsuranceModel);
		
		setLongTermProductValues(motorInsuranceModel);
		
		if("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
			LOGGER.info("I am getquote");						
			DateTime dateTime=new DateTime();
			dateTime=dateTime.plusYears(-21);
			DateTimeFormatter dateformat = DateTimeFormat.forPattern(DATE_FORMAT);
			dateformat.print(dateTime);
			LOGGER.info("DOB:"+dateformat.print(dateTime));
			motorInsuranceModel.setDateOfBirth(dateformat.print(dateTime));											
		}
		//Task #199795 - Aggregators Webservices
		motorInsuranceModel.setGstin(formPost.get("gstin"));
		
		AgentMaster agentMaster = twoWheelerDAO.getAgentMaster(motorInsuranceModel.getAgentId());
		motorInsuranceModel.setBranchCode(agentMaster.getBranchCode());
		//motorInsuranceModel.setoACode(agentMaster.getOaCode());
		motorInsuranceModel.setAgentCode(agentMaster.getAgCode());
		GstMaster gstMaster = gst.getGstMaster(motorInsuranceModel.getAgentId(), motorInsuranceModel.getPolicyStartDate());
		LOGGER.info("motorInsuranceModel.getStateCode() ::: ", motorInsuranceModel.getStateCode());
		motorInsuranceModel.setTaxType(gstMaster.getTaxType());
		motorInsuranceModel.setRSBranchState(gstMaster.getState());
		
		motorInsuranceModel.setIsVehicleInspected(formPost.get("isVehicleInspected"));
		motorInsuranceModel.setVirNumber(formPost.get("virNumber"));
		motorInsuranceModel.setVehicleInspectionDate(formPost.get("vehicleInspectionDate"));
		
		

		// Task #278458 POS DETAILS RS 154761 - TECH
		motorInsuranceModel.setIsPosOpted("true".equalsIgnoreCase(formPost.get("isPosOpted")) ? "Yes" : "No");
		motorInsuranceModel.setPosCode(StringUtils.isNotBlank(formPost.get("posCode")) ? formPost.get("posCode") : "");	
		motorInsuranceModel.setPosName(StringUtils.isNotBlank(formPost.get("posName")) ? formPost.get("posName") : "");
		motorInsuranceModel.setPosPAN(StringUtils.isNotBlank(formPost.get("posPan")) ? formPost.get("posPan").toUpperCase() : "");
		motorInsuranceModel.setPosAadhaar(StringUtils.isNotBlank(formPost.get("posAdhaar")) ? formPost.get("posAdhaar") : "");
		motorInsuranceModel.setUpdatePanAaadharLater(StringUtils.isNotBlank(formPost.get("updatePanAaadharLater"))
				? formPost.get("updatePanAaadharLater") : "");
		motorInsuranceModel.setPosMobile(StringUtils.isNotBlank(formPost.get("posMobile")) ? formPost.get("posMobile") : "");
		motorInsuranceModel.setPosLicenceExpDate(StringUtils.isNotBlank(formPost.get("posLicenceExpiryDate")) ? formPost.get("posLicenceExpiryDate") : "");
		
		if(formPost.get("technicalDiscount") != null)
			motorInsuranceModel.setTechnicalDiscountFromRequest(getDiscountModifiedValue(Double.parseDouble(formPost.get("technicalDiscount"))));

	
		//CPA cover changes for all motor products-163348 
		if("Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
				&& "No".equalsIgnoreCase(formPost.get("cpaCoverisRequired"))){
			motorInsuranceModel.setCpaCoverIsRequired("No");
			motorInsuranceModel.setNoEffectiveDrivingLicense(
						"NoEffectiveDrivingLicense".equalsIgnoreCase(formPost.get("cpacoverOption")) ? true : false);
			motorInsuranceModel.setCpaCoverWithInternalAgent(
						"CPACoverWithInternalAgent".equalsIgnoreCase(formPost.get("cpacoverOption")) ? true : false);
			motorInsuranceModel.setStandalonePAPolicy(
						"standalonePAPolicy".equalsIgnoreCase(formPost.get("cpacoverOption")) ? true : false);
			motorInsuranceModel.setCpaCoverCompanyName(
					StringUtils.isNotBlank(formPost.get("companyName")) ? formPost.get("companyName") : "" );
			motorInsuranceModel.setCpaCoverPolicyNumber(
					StringUtils.isNotBlank(formPost.get("policyNumber")) ? formPost.get("policyNumber") : "" );
			motorInsuranceModel.setCpaCoverExpiryDate(
								StringUtils.isNotBlank(formPost.get("expiryDate")) ? formPost.get("expiryDate") : "" );

			motorInsuranceModel.setPaToOwnerDriver("No");
		}else{
			if("Company".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
				motorInsuranceModel.setCpaCoverIsRequired("No");
			} else {
				motorInsuranceModel.setCpaCoverIsRequired("Yes");
			}

			motorInsuranceModel.setPaToOwnerDriver("Yes");

			// @Vigitha:
			// For 1 year if CPA SI default value 15 lacs
			//      so if we have given other value system should consider it as 15 lacs
			// refer Bug #302983
			if (StringUtils.isBlank(formPost.get("cpaSumInsured"))) {
				motorInsuranceModel.setCpaSumInsured(15_00_000d);
			} else {
				motorInsuranceModel.setCpaSumInsured(NumberUtils.isParsable(formPost.get("cpaSumInsured"))
						? NumberUtils.createDouble(formPost.get("cpaSumInsured"))
						: null);
			}
		}
		setNominationforPA(formPost, motorInsuranceModel);
		
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getTypeOfCover())
				&& StringUtils.equalsIgnoreCase("StandAlone", motorInsuranceModel.getTypeOfCover())) {
			motorInsuranceModel.setTpInsurer(formPost.get("tpInsurer"));
			motorInsuranceModel.setTpPolicyNumber(formPost.get("tpPolicyNumber"));
			motorInsuranceModel.setTpInceptionDate(formPost.get("tpInceptionDate"));
			motorInsuranceModel.setTpExpiryDate(formPost.get("tpExpiryDate"));
			motorInsuranceModel.setTpPolicyTerm(formPost.get("tpPolicyTerm") != null && NumberUtils.isNumber(formPost.get("tpPolicyTerm")) ? 
						Integer.parseInt(formPost.get("tpPolicyTerm")) : 0);
			motorInsuranceModel.setTpAddress1(formPost.get("tpAddress1"));
			motorInsuranceModel.setTpAddress2(formPost.get("tpAddress2"));
			if(formPost.get("tpCity") != null && StringUtils.isNotBlank(formPost.get("tpCity"))) {
				motorInsuranceModel.setTpCity(StringUtils.upperCase(formPost.get("tpCity")));
				CityStateLookupModel cityStateLookupModel = twoWheelerDAO
						.getCityState(formPost.get("tpCity"));
				if(cityStateLookupModel != null && cityStateLookupModel.getState() != null){
					motorInsuranceModel.setTpState(StringUtils.upperCase(cityStateLookupModel.getState()));
				}
			}
			motorInsuranceModel.setTpPincode(formPost.get("tpPincode"));
						
		}
		
		return motorInsuranceModel;
		
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
		HashMap<String, String> formPost = (HashMap<String, String>) t;
		motorInsuranceModel.setSearchEngine(formPost.get("SearchEngine"));
		motorInsuranceModel.setClientName(formPost.get("ClientName"));
		motorInsuranceModel.setCampaign(formPost.get("Campaign"));
		motorInsuranceModel.setAdGroup(formPost.get("Adgroup"));
		motorInsuranceModel.setSearchVsContent(formPost.get("Search_Vs_Content"));
		motorInsuranceModel.setKeyword(formPost.get("Keyword"));
		motorInsuranceModel.setReferralUrl(formPost.get("referer"));
		motorInsuranceModel.setCampaignName(formPost.get("campaignname"));
		motorInsuranceModel.setLeadType(formPost.get("LeadType"));
		motorInsuranceModel.setGclid(formPost.get("gclid"));
		motorInsuranceModel.setIpAddress(formPost.get("IPADDRESS"));
		
	}
	
	@Override
	public <T> void setIDV_PreviousPolicyDetails(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		HashMap<String, String> formPost = (HashMap<String, String>) t;

		LOGGER.info("motorInsuranceModel.getProductName() ::: " + motorInsuranceModel.getProductName());
		LOGGER.info("motorInsuranceModel.getProcessType() ::: " + motorInsuranceModel.getProcessType());

		if ("BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {

			motorInsuranceModel.setIsPreviousPolicyHolder("false");
			motorInsuranceModel.setVehicleRegistrationDate(formPost.get("vehicleregDate"));
//			motorInsuranceModel.setPreviousPolicyExpiryDate(formPost.get("vehicleregDate"));

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

			motorInsuranceModel.setVehicleRegistrationDate(formPost.get("vehicleregDate"));
			motorInsuranceModel.setPreviousPolicyExpiryDate(formPost.get("previousPolicyExpiryDate"));

			motorInsuranceModel.setPreviousPolicyType(formPost.get("previousPolicyType"));
			motorInsuranceModel.setPreviuosPolicyNumber(formPost.get("previuosPolicyNumber"));
			motorInsuranceModel.setPreviousInsurerName(formPost.get("previousInsurerName"));
			motorInsuranceModel.setClaimsMadeInPreviousPolicy(formPost.get("claimsMadeInPreviousPolicy")); // Expected values are 'Yes' or 'No'
			motorInsuranceModel.setNoClaimBonusPercent(formPost.get("noClaimBonusPercent")); // Expected values are 1 or 2 or 3 or 4 or 5 or 6
			motorInsuranceModel.setNoClaimBonusPercentinCurrent(formPost.get("ncbcureent")); // Expected values are 20 or 25 or 35 or 45 or 50

			motorInsuranceModel.setClaimAmountReceived(formPost.get("claimAmountReceived"));
			motorInsuranceModel.setClaimsReported(formPost.get("claimsReported"));
			motorInsuranceModel.setPreviousinsurersCorrectAddress(formPost.get("previousinsurersCorrectAddress"));

			/**
			 * Getting Idv, SeatingCapacity, EngineCapacity, VehicleClass,
			 * VehicleModelName, BodyStyle, VehicleMakeId, AddonModel, FuelType,
			 * ModifiedIDVfor2Year , ModifiedIDVfor3Year, VehicleAgeforyear2,
			 * VehicleAgeforyear3 from common services through IPC and these
			 * values are setting on called method
			 */
			ipcServices.getModelIdvResult(motorInsuranceModel);
		
			motorInsuranceModel.setDiscountidvPercent(StringUtils.isNotBlank(formPost.get("modify_your_idv")) ? formPost.get("modify_your_idv") : "0" );
			
			motorInsuranceModel.setDiscountIDVPercent1Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent2Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent3Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent4Year(motorInsuranceModel.getDiscountidvPercent());
			motorInsuranceModel.setDiscountIDVPercent5Year(motorInsuranceModel.getDiscountidvPercent());
			if ("RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId()) 
					&& ("Microsite".equalsIgnoreCase(motorInsuranceModel.getSource()) 
							||  "NewSite".equalsIgnoreCase(motorInsuranceModel.getSource())) ) {
				
				
				double orginalIdv = motorInsuranceModel.getIdv();
				long modifyYourIdv =  Long.parseLong(motorInsuranceModel.getDiscountidvPercent()); 
				double modifiedIdv = Math.round( motorInsuranceModel.getIdv() + ( motorInsuranceModel.getIdv() * modifyYourIdv / 100 ) );
				motorInsuranceModel.setIdv(modifiedIdv);
				
				motorInsuranceModel.setTotalIdv("" + orginalIdv);
				motorInsuranceModel.setIdvFor1Year(Double.parseDouble(motorInsuranceModel.getTotalIdv()));
				motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
				
				double orginalIdv2 = motorInsuranceModel.getIdvFor2Year();
				long modifyYourIdv2 =  Long.parseLong(motorInsuranceModel.getDiscountIDVPercent2Year());
				double modifiedIDVfor2Year = Math.round( orginalIdv2 + ( orginalIdv2 * modifyYourIdv2 / 100 ) );
				motorInsuranceModel.setModifiedIdvfor2Year(modifiedIDVfor2Year);
				
				double orginalIdv3 = motorInsuranceModel.getIdvFor3Year();
				long modifyYourIdv3 =  Long.parseLong(motorInsuranceModel.getDiscountIDVPercent3Year());
				double modifiedIDVfor3Year = Math.round( orginalIdv3 + ( orginalIdv3 * modifyYourIdv3 / 100 ) );
				motorInsuranceModel.setModifiedIdvfor3Year(modifiedIDVfor3Year);
				
				double orginalIdv4 =motorInsuranceModel.getIdvFor4Year();
				long modifyYourIdv4 =  Long.parseLong(motorInsuranceModel.getDiscountIDVPercent4Year());
				double modifiedIDVfor4Year = Math.round( orginalIdv4 + ( orginalIdv4 * modifyYourIdv4 / 100 ) );
				motorInsuranceModel.setModifiedIdvfor4Year(modifiedIDVfor4Year);
				
				double orginalIdv5 = motorInsuranceModel.getIdvFor5Year();
				long modifyYourIdv5 =  Long.parseLong(motorInsuranceModel.getDiscountIDVPercent5Year());
				double modifiedIDVfor5Year = Math.round( orginalIdv5 + ( orginalIdv5 * modifyYourIdv5 / 100 ) );
				motorInsuranceModel.setModifiedIdvfor5Year(modifiedIDVfor5Year);
				
				

			}else {
				
				if( StringUtils.isBlank(formPost.get("original_idv"))
						|| Double.parseDouble(formPost.get("original_idv")) == 0 ){
					motorInsuranceModel.setTotalIdv(""+motorInsuranceModel.getIdv());
					motorInsuranceModel.setDiscountidvPercent("0");
				}else {
					double orginalIdv = Math.round( Double.parseDouble(formPost.get("original_idv")) );
					double modifiedIdv = Math.round( StringUtils.isNotBlank(formPost.get("modified_idv_value")) ? Double.parseDouble(formPost.get("modified_idv_value")) : 0 );
					motorInsuranceModel.setTotalIdv("" + orginalIdv);
					motorInsuranceModel.setIdv(modifiedIdv == 0 ? orginalIdv : modifiedIdv);
				}
				
				motorInsuranceModel.setIdvFor1Year(Double.parseDouble(motorInsuranceModel.getTotalIdv()));
				motorInsuranceModel.setDiscountIDVPercent1Year(motorInsuranceModel.getDiscountidvPercent());
				motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
				
				if( StringUtils.isBlank(formPost.get("original_idv2"))
						|| Double.parseDouble(formPost.get("original_idv2")) == 0 ){
					motorInsuranceModel.setDiscountIDVPercent2Year("0");
					motorInsuranceModel.setModifiedIdvfor2Year(motorInsuranceModel.getIdvFor2Year());
				}else {
					double orginalIdv2 = Math.round( Double.parseDouble(formPost.get("original_idv2")) );
					double modifiedIdvValue2 = Math.round( StringUtils.isNotBlank(formPost.get("modified_idv_value2")) ? Double.valueOf(formPost.get("modified_idv_value2")) : 0 ); 
					
					motorInsuranceModel.setIdvFor2Year(orginalIdv2);
					motorInsuranceModel.setModifiedIdvfor2Year(modifiedIdvValue2 == 0 ? orginalIdv2 : modifiedIdvValue2);
				}
				
				if( StringUtils.isBlank(formPost.get("original_idv3"))
						|| Double.parseDouble(formPost.get("original_idv3")) == 0 ){
					motorInsuranceModel.setDiscountIDVPercent3Year("0");
					motorInsuranceModel.setModifiedIdvfor3Year(motorInsuranceModel.getIdvFor3Year());
				}else {
					double orginalIdv3 = Math.round( Double.parseDouble(formPost.get("original_idv3")) );
					double modifiedIdvValue3 = Math.round( StringUtils.isNotBlank(formPost.get("modified_idv_value3")) ? Double.valueOf(formPost.get("modified_idv_value3")) : 0 ); 
					
					motorInsuranceModel.setIdvFor3Year(orginalIdv3);
					motorInsuranceModel.setModifiedIdvfor3Year(modifiedIdvValue3 == 0 ? orginalIdv3 : modifiedIdvValue3);
				}
				
				if( StringUtils.isBlank(formPost.get("original_idv4"))
						|| Double.parseDouble(formPost.get("original_idv4")) == 0 ){
					motorInsuranceModel.setDiscountIDVPercent4Year("0");
					motorInsuranceModel.setModifiedIdvfor4Year(motorInsuranceModel.getIdvFor4Year());
				}else {
					double orginalIdv4 = Math.round( Double.parseDouble(formPost.get("original_idv4")) );
					double modifiedIdvValue4 = Math.round( StringUtils.isNotBlank(formPost.get("modified_idv_value4")) ? Double.valueOf(formPost.get("modified_idv_value4")) : 0 ); 
					
					motorInsuranceModel.setIdvFor4Year(orginalIdv4);
					motorInsuranceModel.setModifiedIdvfor4Year(modifiedIdvValue4 == 0 ? orginalIdv4 : modifiedIdvValue4);
				}
				
				if( StringUtils.isBlank(formPost.get("original_idv5"))
						|| Double.parseDouble(formPost.get("original_idv5")) == 0 ){
					motorInsuranceModel.setDiscountIDVPercent5Year("0");
					motorInsuranceModel.setModifiedIdvfor5Year(motorInsuranceModel.getIdvFor5Year());
				}else {
					double orginalIdv5 = Math.round( Double.parseDouble(formPost.get("original_idv5")) );
					double modifiedIdvValue5 = Math.round( StringUtils.isNotBlank(formPost.get("modified_idv_value5")) ? Double.valueOf(formPost.get("modified_idv_value5")) : 0 ); 
					
					motorInsuranceModel.setIdvFor5Year(orginalIdv5);
					motorInsuranceModel.setModifiedIdvfor5Year(modifiedIdvValue5 == 0 ? orginalIdv5 : modifiedIdvValue5);
				}


			}
			/* Task #249208 Policy bazaar IDV deviation */
			if("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
				
				  AgentPlanMapping agentPlanMapping = twoWheelerDAO.getAgentPlanMapping(motorInsuranceModel.getAgentId(), "TWOWHEELER", "MotorCyclePackage");
					if(agentPlanMapping != null 
								&& agentPlanMapping.getIdvDeviationPercent() != null
								&& StringUtils.isNotBlank(agentPlanMapping.getIdvDeviationPercent()) 
								&& Integer.parseInt(agentPlanMapping.getIdvDeviationPercent()) != 0){
						int idvDeviationPercentage = Math.abs(Integer.parseInt(agentPlanMapping.getIdvDeviationPercent()));
						
						if (StringUtils.isBlank(formPost.get("idv"))
									|| Double.parseDouble(formPost.get("idv")) == 0) {
								double idv = Double.parseDouble(motorInsuranceModel.getTotalIdv());
								LOGGER.info("idv -Db value =" + motorInsuranceModel.getIdv());
								double idvdec = (idv * idvDeviationPercentage) / 100;
								LOGGER.info("idvded =" + idvdec+" idvdeviation ="+(idv - idvdec));
								int idvdeviation =(int) Math.floor(idv - idvdec);
								motorInsuranceModel.setTotalIdv("" + idvdeviation);
								motorInsuranceModel.setIdv(idvdeviation);
								motorInsuranceModel.setIdvFor1Year(idvdeviation);
								motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv());
								motorInsuranceModel.setOriginalIdvFor1Year(String.valueOf(idvdeviation));
								LOGGER.info("idv =" + motorInsuranceModel.getIdv());
								LOGGER.info("TotalIdv =" + motorInsuranceModel.getTotalIdv());
								LOGGER.info("idv 1nd year = "+motorInsuranceModel.getIdvFor1Year());

								if (motorInsuranceModel.getPolicyTerm() >= 2 ) {
									idv = motorInsuranceModel.getIdvFor2Year();
									idvdec = (idv * idvDeviationPercentage) / 100;
									LOGGER.info("idv dev 2 : "+idvdec);
									idvdeviation = (int) Math.floor(idv - idvdec);
									LOGGER.info("idvdeviation 2nd year = "+idvdeviation);
									motorInsuranceModel.setIdvFor2Year(idvdeviation);
									motorInsuranceModel.setModifiedIdvfor2Year(idvdeviation);
									motorInsuranceModel.setOriginalIdvFor2Year(String.valueOf(idvdeviation));
			
									LOGGER.info("idv 2nd year = "+motorInsuranceModel.getIdvFor2Year());
									if (motorInsuranceModel.getPolicyTerm() >= 3 ) {
										
										idv = motorInsuranceModel.getIdvFor3Year();
										idvdec = (idv * idvDeviationPercentage) / 100;
										LOGGER.info("idv dev 3 : "+idvdec);
										idvdeviation = (int) Math.floor(idv - idvdec);
										LOGGER.info("idvdeviation 3nd year = "+idvdeviation);
										motorInsuranceModel.setIdvFor3Year(idvdeviation);
										motorInsuranceModel.setModifiedIdvfor3Year(idvdeviation);
										motorInsuranceModel.setOriginalIdvFor3Year(String.valueOf(idvdeviation));
										LOGGER.info("idv 3nd year = "+motorInsuranceModel.getIdvFor3Year());
										
										if (motorInsuranceModel.getPolicyTerm() >= 4 ) {
											
											idv = motorInsuranceModel.getIdvFor4Year();
											idvdec = (idv * idvDeviationPercentage) / 100;
											LOGGER.info("idv dev 4 : "+idvdec);
											idvdeviation = (int) Math.floor(idv - idvdec);
											LOGGER.info("idvdeviation 4th year = "+idvdeviation);
											motorInsuranceModel.setIdvFor4Year(idvdeviation);
											motorInsuranceModel.setModifiedIdvfor4Year(idvdeviation);
											motorInsuranceModel.setOriginalIdvFor4Year(String.valueOf(idvdeviation));
											LOGGER.info("idv 4th year = "+motorInsuranceModel.getIdvFor4Year());
											
											if (motorInsuranceModel.getPolicyTerm() >= 5 ) {
												
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
			}
					
			
			/* End - Task #249208 Policy bazaar IDV deviation */
		
	
		}
	}
	
	@Override
	public <T> void setCovers(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		HashMap<String, String> formPost = (HashMap<String, String>) t;

		//Do you Need Cover for Driver / Other Passengers / Two Wheeler Accessories?
		motorInsuranceModel.setCover_dri_othr_car_ass(formPost.get("cover_dri_othr_car_ass"));
		if("Yes".equalsIgnoreCase(motorInsuranceModel.getCover_dri_othr_car_ass())) {
			
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengers(
					formPost.get("personalaccidentcoverforunnamedpassengers")); // Personal Accident Cover for Unnamed Passengers
			
			motorInsuranceModel.setAccidentcoverforpaiddriver(formPost.get("accidentcoverforpaiddriver")); // Accident Cover for Paid Driver
			
			motorInsuranceModel.setLegalliabilitytopaiddriver(formPost.get("legalliabilitytopaiddriver")); //Legal Liability to be Paid to Driver
			
			motorInsuranceModel.setLegalliabilitytoemployees(formPost.get("legalliabilitytoemployees")); //Legal Liability to be Paid to Employee - If vehicle registered in the name of Company
			
			
			//Do you Need Cover for Non-Electrical Accessories?
			if ("yes".equalsIgnoreCase(formPost.get("cover_non_elec_acc"))) {
				List<NonElectronicValues> nonElectricalAccessoriesList = new ArrayList<NonElectronicValues>();
				long totneval = 0;
				for (int i = 0; i < 7; i++) {
					NonElectronicValues nonElectronicValues = new NonElectronicValues();
					LOGGER.info("nonElectricalAccessoriesList[" + i + "].nonElectronicValue" + "_i=" + i + ":"
							+ formPost.get("nonElectricalAccessoriesList[" + i + "].nonElectronicValue"));
					if (formPost.get("nonElectricalAccessoriesList[" + i + "].nonElectronicValue") != null
							&& !"".equalsIgnoreCase(formPost.get("nonElectricalAccessoriesList[" + i + "].nonElectronicValue"))) {
						nonElectronicValues.setNonElectronicNameOfElectronicAccessories(formPost.get("nonElectricalAccessoriesList["
								+ i + "].nonElectronicNameOfElectronicAccessories"));
						nonElectronicValues.setNonElectronicMakeModel(formPost.get("nonElectricalAccessoriesList[" + i
								+ "].nonElectronicMakeModel"));
						nonElectronicValues.setNonElectronicValue(formPost.get("nonElectricalAccessoriesList[" + i
								+ "].nonElectronicValue"));
						nonElectricalAccessoriesList.add(nonElectronicValues);
						totneval = totneval + Long.parseLong(nonElectronicValues.getNonElectronicValue());
						LOGGER.info("setNonElectronicNameOfElectronicAccessories-01::"
								+ nonElectronicValues.getNonElectronicNameOfElectronicAccessories());
					}
				}

				motorInsuranceModel.setValueofnonelectricalaccessories(totneval + "");
				motorInsuranceModel.setNonElectricalAccessoriesList(nonElectricalAccessoriesList);
				motorInsuranceModel.setNonElectronicValue(Long.toString(totneval));
			} else {
				motorInsuranceModel.setValueofnonelectricalaccessories(null);
				motorInsuranceModel.setNonElectricalAccessoriesList(null);
				motorInsuranceModel.setNonElectronicValue(null);
			}
			
			//Do you Need Cover for Electrical Accessories?
			if ("yes".equalsIgnoreCase(formPost.get("cover_elec_acc"))) {
				List<ElectronicValues> electricalAccessoriesList = new ArrayList<ElectronicValues>();
				long totneval = 0;
				LOGGER.info("cover_elec_acc-02 ::" + formPost.get("cover_elec_acc"));
				for (int i = 0; i < 7; i++) {
					ElectronicValues electronicValues = new ElectronicValues();

					LOGGER.info("electricalAccessoriesList[" + i + "].electronicValue" + "_i=" + i + ":"
							+ formPost.get("electricalAccessoriesList[" + i + "].electronicValue"));

					if (formPost.get("electricalAccessoriesList[" + i + "].electronicValue") != null
							&& !"".equalsIgnoreCase(formPost.get("electricalAccessoriesList[" + i + "].electronicValue"))) {
						LOGGER.info("cover_elec_acc-01 ::" + "_i=" + i + ":" + formPost.get("cover_elec_acc"));
						electronicValues.setElectronicNameOfElectronicAccessories(formPost.get("electricalAccessoriesList[" + i
								+ "].electronicNameOfElectronicAccessories"));
						electronicValues.setElectronicMakeModel(formPost.get("electricalAccessoriesList[" + i
								+ "].electronicMakeModel"));
						electronicValues.setElectronicValue(formPost.get("electricalAccessoriesList[" + i + "].electronicValue"));
						electricalAccessoriesList.add(electronicValues);
						totneval = totneval + Long.parseLong(electronicValues.getElectronicValue());
						LOGGER.info("setElectronicNameOfElectronicAccessories-01::"
								+ electronicValues.getElectronicNameOfElectronicAccessories());
						LOGGER.info("setElectronicNameOfElectronicAccessories-value--01::"
								+ electronicValues.getElectronicValue());
					}
				}

				motorInsuranceModel.setValueofelectricalaccessories(totneval + "");
				motorInsuranceModel.setElectricalAccessoriesList(electricalAccessoriesList);
				motorInsuranceModel.setElectronicValue(Long.toString(totneval));
			} else {
				motorInsuranceModel.setValueofelectricalaccessories(null);
				motorInsuranceModel.setElectricalAccessoriesList(null);
				motorInsuranceModel.setElectronicValue(null);
			}
			
		} else {
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengers("0.0");
			motorInsuranceModel.setAccidentcoverforpaiddriver("0.0");
			motorInsuranceModel.setLegalliabilitytopaiddriver("No");
			motorInsuranceModel.setLegalliabilitytoemployees("No");
			motorInsuranceModel.setValueofnonelectricalaccessories(null);
			motorInsuranceModel.setNonElectricalAccessoriesList(null);
			motorInsuranceModel.setNonElectronicValue(null);
			motorInsuranceModel.setValueofelectricalaccessories(null);
			motorInsuranceModel.setElectricalAccessoriesList(null);
			motorInsuranceModel.setElectronicValue(null);
		}
		/**
		 * Task #141300 Add-on cover additional Towing charges
		 */
		motorInsuranceModel.setTowingChargesCover(formPost.get("towingChargesCover"));
		if(Constants.YES.equalsIgnoreCase(motorInsuranceModel.getTowingChargesCover())) {
			
			if (StringUtils.isNotBlank(formPost.get("towingChargesCover_SI"))) {
				try {
					motorInsuranceModel.setAdditionalTowingChargesCover(formPost.get("towingChargesCover_SI"));
					motorInsuranceModel.setTowingChargesCoverSI(Double.parseDouble(formPost.get("towingChargesCover_SI")));
				} catch (NumberFormatException e) {
					LOGGER.info(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public <T> void setAdditionalDetails(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		HashMap<String, String> formPost = (HashMap<String, String>) t;
		motorInsuranceModel.setRegistrationNumber(formPost.get("registrationNumber"));
		motorInsuranceModel.setEngineNumber(formPost.get("engineNumber"));
		motorInsuranceModel.setChassisNumber(formPost.get("chassisNumber"));
		motorInsuranceModel.setIsCarFinanced(formPost.get("isCarFinanced"));
		motorInsuranceModel.setIsCarFinancedValue(formPost.get("isCarFinancedValue"));
		if(StringUtils.isNotBlank(formPost.get("financierlocation"))) 
			motorInsuranceModel.setFinancierName(formPost.get("financierName") +", "+formPost.get("financierlocation"));
		else
			motorInsuranceModel.setFinancierName(formPost.get("financierName"));
		
		setNominationforPA(formPost, motorInsuranceModel);
		
		setContactAddress(formPost, motorInsuranceModel);
		
		setVehicleRegistrationAddress(formPost, motorInsuranceModel);
		
	}

	@Override
	public <T> void setNominationforPA(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		HashMap<String, String> formPost = (HashMap<String, String>) t;
		
		if(INDIVIDUAL.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf()) && YES.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())) {
			motorInsuranceModel.setNominee_Name(formPost.get("nominee_Name"));
			motorInsuranceModel.setNominee_Age(formPost.get("nominee_Age"));
			motorInsuranceModel.setRelationship_with_nominee(formPost.get("relationship_with_nominee"));
			motorInsuranceModel.setGuardian_Name(formPost.get("guardian_Name"));
			motorInsuranceModel.setGuardian_Age(formPost.get("guardian_Age"));
			motorInsuranceModel.setRelationship_with_Guardian(formPost.get("relationship_with_Guardian"));
			motorInsuranceModel.setLegalliabilitytoemployees("No");
		}
		
		if (COMPANY.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())  ||
				("Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf()) 
						&& NO.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired()))) {
			motorInsuranceModel.setCompanyNameForCar(formPost.get("companyNameForCar"));
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
		HashMap<String, String> formPost = (HashMap<String, String>) t;
		motorInsuranceModel.setContactAddress1(formPost.get("contactAddress1"));
		motorInsuranceModel.setContactAddress2(formPost.get("contactAddress2"));
		motorInsuranceModel.setContactAddress3(formPost.get("contactAddress3"));
		motorInsuranceModel.setContactAddress4(formPost.get("contactAddress4"));
		motorInsuranceModel.setContactCity(formPost.get("contactCity"));
		motorInsuranceModel.setContactPincode(formPost.get("contactPincode"));
		motorInsuranceModel.setAadharNumber(formPost.get("aadharNumber"));
		motorInsuranceModel.setPanNumber(formPost.get("panNumber"));
		/****** #141301 City – State in PDF - Phase II *****/
		if(StringUtils.isNotBlank(formPost.get("contactCity"))) ipcServices.setContactState(motorInsuranceModel);
		LOGGER.info("CONTACT STATE::"+motorInsuranceModel.getContactState());
		
	}

	@Override
	public <T> void setVehicleRegistrationAddress(T t, MotorInsuranceModel motorInsuranceModel) throws Exception {
		HashMap<String, String> formPost = (HashMap<String, String>) t;
		motorInsuranceModel.setIsSameASRegistrationAddress(formPost.get("same_addr_reg"));
		motorInsuranceModel.setAddressOne(formPost.get("addressOne"));
		motorInsuranceModel.setAddressTwo(formPost.get("addressTwo"));
		motorInsuranceModel.setAddressThree(formPost.get("addressThree"));
		motorInsuranceModel.setAddressFour(formPost.get("addressFour"));
		motorInsuranceModel.setRegCity(formPost.get("regCity"));
		motorInsuranceModel.setRegPinCode(formPost.get("regPinCode"));
		
	}

	@Override
	public <T> void setLongTermProductValues(MotorInsuranceModel motorInsuranceModel) throws Exception {

		// IDV for 1 & 2 & 3 Year
		/*if ("BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())
				|| "RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {

			LOGGER.info("motorInsuranceModel.getAgentId() ::: " + motorInsuranceModel.getAgentId());
			*//**
			 * Other than RSAI we need to get the IDV values from input
			 *//*
			LOGGER.info("IDV:" + motorInsuranceModel.getIdv());

			if ("RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId())
					|| "SFSP0002".equalsIgnoreCase(motorInsuranceModel.getAgentId())
					|| "SFLC0001".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
				motorInsuranceModel.setIdvFor1Year(motorInsuranceModel.getIdv());
				motorInsuranceModel.getModifiedIdvfor2Year();
				LOGGER.info("SET IDV2:" + motorInsuranceModel.getModifiedIdvfor2Year());
				motorInsuranceModel.getModifiedIdvfor3Year();
				LOGGER.info("SET IDV3:" + motorInsuranceModel.getModifiedIdvfor3Year());
				motorInsuranceModel.getModifiedIdvfor4Year();
				LOGGER.info("SET IDV4:" + motorInsuranceModel.getModifiedIdvfor4Year());
				motorInsuranceModel.getModifiedIdvfor5Year();
				LOGGER.info("SET IDV5:" + motorInsuranceModel.getModifiedIdvfor5Year());
			} else {
				motorInsuranceModel.setIdv(motorInsuranceModel.getIdv());
				motorInsuranceModel.setIdvFor1Year(motorInsuranceModel.getIdvFor1Year());
				motorInsuranceModel.setIdvFor2Year(motorInsuranceModel.getIdvFor2Year());
				motorInsuranceModel.setIdvFor3Year(motorInsuranceModel.getIdvFor3Year());
				motorInsuranceModel.setIdvFor4Year(motorInsuranceModel.getIdvFor4Year());
				motorInsuranceModel.setIdvFor5Year(motorInsuranceModel.getIdvFor5Year());
			}
			if ("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
				motorInsuranceModel.getModifiedIdvfor2Year();
				LOGGER.info("SET IDV2:" + motorInsuranceModel.getModifiedIdvfor2Year());
				motorInsuranceModel.getModifiedIdvfor3Year();
				LOGGER.info("SET IDV3:" + motorInsuranceModel.getModifiedIdvfor3Year());
				motorInsuranceModel.getModifiedIdvfor4Year();
				LOGGER.info("SET IDV4:" + motorInsuranceModel.getModifiedIdvfor4Year());
				motorInsuranceModel.getModifiedIdvfor5Year();
				LOGGER.info("SET IDV5:" + motorInsuranceModel.getModifiedIdvfor5Year());
			}

		}*/

		// VechicleAge
		motorInsuranceModel.setVehicleAgeforyear2(motorInsuranceModel.getVehicleAgeforyear2());
		motorInsuranceModel.setVehicleAgeforyear3(motorInsuranceModel.getVehicleAgeforyear3());
		motorInsuranceModel.setVehicleAgeforyear4(motorInsuranceModel.getVehicleAgeforyear4());
		motorInsuranceModel.setVehicleAgeforyear5(motorInsuranceModel.getVehicleAgeforyear5());
		// Electrical value && Non-Electrical value
		int valueofelecAcc = StringUtils.isNotBlank(motorInsuranceModel.getValueofelectricalaccessories())
				? Integer.parseInt(motorInsuranceModel.getValueofelectricalaccessories())
				: 0;
		int valueofnonelecAcc = StringUtils.isNotBlank(motorInsuranceModel.getValueofnonelectricalaccessories())
				? Integer.parseInt(motorInsuranceModel.getValueofnonelectricalaccessories())
				: 0;

		if (motorInsuranceModel.getPolicyTerm() == 1) {

			// Electrical value
			motorInsuranceModel.setValueofelectricalaccessoriesforyear2("0");
			motorInsuranceModel.setValueofelectricalaccessoriesforyear3("0");
			motorInsuranceModel.setValueofelectricalaccessoriesforyear4("0");
			motorInsuranceModel.setValueofelectricalaccessoriesforyear5("0");

			// Non-Electrical value
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear2("0");
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear3("0");
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear4("0");
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear5("0");

			// AdditionalTowingChargesCover
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear2("0");
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear3("0");
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear4("0");
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear5("0");
		}
		if(motorInsuranceModel.getLiabilityPolicyTerm() == 1){
			// Personal Accident Cover for Unnamed Passengers or VMC_PAUnnamed
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear2("0");
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear3("0");
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear4("0");
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear5("0");

			// Accident Cover for Paid Driver or VMC_PAPaidDriver
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear2("0");
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear3("0");
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear4("0");
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear5("0");
		}

		if (motorInsuranceModel.getPolicyTerm() == 2) {

			// Electrical value
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear2(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc, "Covers", MOTORCYCLE));
			motorInsuranceModel.setValueofelectricalaccessoriesforyear3("0");
			motorInsuranceModel.setValueofelectricalaccessoriesforyear4("0");
			motorInsuranceModel.setValueofelectricalaccessoriesforyear5("0");

			// Non-Electrical value
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear2(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc, "Covers", MOTORCYCLE));
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear3("0");
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear4("0");
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear5("0");

			

			// AdditionalTowingChargesCover
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear2(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear3("0");
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear4("0");
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear5("0");
		}
		if(motorInsuranceModel.getLiabilityPolicyTerm() == 2){
			// Personal Accident Cover for Unnamed Passengers or VMC_PAUnnamed
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear2(
								StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
										: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear3("0");
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear4("0");
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear5("0");

						// Accident Cover for Paid Driver or VMC_PAPaidDriver
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear2(
								StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
										: motorInsuranceModel.getAccidentcoverforpaiddriver());
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear3("0");
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear4("0");
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear5("0");
		}
		if (motorInsuranceModel.getPolicyTerm() == 3) {

			// Electrical value
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear2(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc, "Covers", MOTORCYCLE));

			double valueofelecAcc2 = Double.valueOf(motorInsuranceModel.getValueofelectricalaccessoriesforyear2());
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear3(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc2, "Covers", MOTORCYCLE));
			motorInsuranceModel.setValueofelectricalaccessoriesforyear4("0");
			motorInsuranceModel.setValueofelectricalaccessoriesforyear5("0");
			
			// Non-Electrical value
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear2(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc, "Covers", MOTORCYCLE));
			double valueofnonelecAcc2 = Double
					.valueOf(motorInsuranceModel.getValueofnonelectricalaccessoriesforyear2());

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear3(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc2, "Covers", MOTORCYCLE));

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear4("0");
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear5("0");

			// AdditionalTowingChargesCover
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear2(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear3(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear4("0");
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear5("0");
		}
		
		if(motorInsuranceModel.getLiabilityPolicyTerm() == 3){
			// Personal Accident Cover for Unnamed Passengers or VMC_PAUnnamed
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear2(
								StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
										: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear3(
								StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
										: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear4("0");
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear5("0");

						// Accident Cover for Paid Driver or VMC_PAPaidDriver
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear2(
								StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
										: motorInsuranceModel.getAccidentcoverforpaiddriver());
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear3(
								StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
										: motorInsuranceModel.getAccidentcoverforpaiddriver());
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear4("0");
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear5("0");
		}
		if (motorInsuranceModel.getPolicyTerm() == 4) {

			// Electrical value
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear2(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc, "Covers", MOTORCYCLE));

			double valueofelecAcc2 = Double.valueOf(motorInsuranceModel.getValueofelectricalaccessoriesforyear2());
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear3(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc2, "Covers", MOTORCYCLE));
			double valueofelecAcc3 = Double.valueOf(motorInsuranceModel.getValueofelectricalaccessoriesforyear3());
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear4(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc3, "Covers", MOTORCYCLE));
			motorInsuranceModel.setValueofelectricalaccessoriesforyear5("0");
			
			
			// Non-Electrical value
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear2(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc, "Covers", MOTORCYCLE));
			double valueofnonelecAcc2 = Double
					.valueOf(motorInsuranceModel.getValueofnonelectricalaccessoriesforyear2());

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear3(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc2, "Covers", MOTORCYCLE));
			double valueofnonelecAcc3 = Double
					.valueOf(motorInsuranceModel.getValueofnonelectricalaccessoriesforyear3());

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear4(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc3, "Covers", MOTORCYCLE));
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear5("0");
			
			
			// AdditionalTowingChargesCover
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear2(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear3(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear4(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear5("0");
		}
		
		if(motorInsuranceModel.getLiabilityPolicyTerm() ==4){
			// Personal Accident Cover for Unnamed Passengers or VMC_PAUnnamed
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear2(
								StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
										: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear3(
								StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
										: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear4(
								StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
										: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
						motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear5("0");

						// Accident Cover for Paid Driver or VMC_PAPaidDriver
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear2(
								StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
										: motorInsuranceModel.getAccidentcoverforpaiddriver());
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear3(
								StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
										: motorInsuranceModel.getAccidentcoverforpaiddriver());
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear4(
								StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
										: motorInsuranceModel.getAccidentcoverforpaiddriver());
						motorInsuranceModel.setAccidentcoverforpaiddriverforyear5("0");

		}
		if (motorInsuranceModel.getPolicyTerm() == 5) {

			// Electrical value
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear2(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc, "Covers", MOTORCYCLE));

			double valueofelecAcc2 = Double.valueOf(motorInsuranceModel.getValueofelectricalaccessoriesforyear2());
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear3(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc2, "Covers", MOTORCYCLE));
			double valueofelecAcc3 = Double.valueOf(motorInsuranceModel.getValueofelectricalaccessoriesforyear3());
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear4(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc3, "Covers", MOTORCYCLE));
			double valueofelecAcc4 = Double.valueOf(motorInsuranceModel.getValueofelectricalaccessoriesforyear4());
			motorInsuranceModel
					.setValueofelectricalaccessoriesforyear5(valueofelecAcc == 0 ? String.valueOf(valueofelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofelecAcc4, "Covers", MOTORCYCLE));

			// Non-Electrical value
			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear2(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc, "Covers", MOTORCYCLE));
			double valueofnonelecAcc2 = Double
					.valueOf(motorInsuranceModel.getValueofnonelectricalaccessoriesforyear2());

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear3(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc2, "Covers", MOTORCYCLE));
			
			double valueofnonelecAcc3 = Double
					.valueOf(motorInsuranceModel.getValueofnonelectricalaccessoriesforyear3());

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear4(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc3, "Covers", MOTORCYCLE));
			
			double valueofnonelecAcc4 = Double
					.valueOf(motorInsuranceModel.getValueofnonelectricalaccessoriesforyear4());

			motorInsuranceModel.setValueofnonelectricalaccessoriesforyear5(
					valueofnonelecAcc == 0 ? String.valueOf(valueofnonelecAcc)
							: twoWheelerDAO.getAddonCoverDepreciation(valueofnonelecAcc4, "Covers", MOTORCYCLE));

		

			// AdditionalTowingChargesCover
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear2(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear3(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear4(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
			motorInsuranceModel.setAdditionalTowingChargesCoverforyear5(
					StringUtils.isBlank(motorInsuranceModel.getAdditionalTowingChargesCover()) ? "0"
							: motorInsuranceModel.getAdditionalTowingChargesCover());
		}
		
		if(motorInsuranceModel.getLiabilityPolicyTerm() ==5){
			// Personal Accident Cover for Unnamed Passengers or VMC_PAUnnamed
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear2(
					StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
							: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear3(
					StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
							: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear4(
					StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
							: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());
			motorInsuranceModel.setPersonalaccidentcoverforunnamedpassengersforyear5(
					StringUtils.isBlank(motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers()) ? "0"
							: motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers());

			// Accident Cover for Paid Driver or VMC_PAPaidDriver
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear2(
					StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
							: motorInsuranceModel.getAccidentcoverforpaiddriver());
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear3(
					StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
							: motorInsuranceModel.getAccidentcoverforpaiddriver());
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear4(
					StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
							: motorInsuranceModel.getAccidentcoverforpaiddriver());
			motorInsuranceModel.setAccidentcoverforpaiddriverforyear5(
					StringUtils.isBlank(motorInsuranceModel.getAccidentcoverforpaiddriver()) ? "0"
							: motorInsuranceModel.getAccidentcoverforpaiddriver());
		}
		
		
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
