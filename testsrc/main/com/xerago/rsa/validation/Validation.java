package com.xerago.rsa.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.xerago.rsa.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.xerago.rsa.dao.DAOLayer;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.domain.AgentPlanMapping;
import com.xerago.rsa.domain.DModifyidvRangeMaster;
import com.xerago.rsa.domain.DPoscodeMaster;
import com.xerago.rsa.domain.DQuoteDetails;
import com.xerago.rsa.domain.GstinStateMaster;
import com.xerago.rsa.domain.InsuranceRepositoryMaster;
import com.xerago.rsa.dto.response.ApiResponse;
import com.xerago.rsa.model.MotorInsuranceModel;

import static com.xerago.rsa.util.ConstantVariables.GET_QUOTE_PROCESS;

@Component
public class Validation implements Constants, ValidationConstants {

	@Autowired
	TwoWheelerValidation twoWheelerValidation;
	
	@Autowired
	private RestTemplate restTemplate;


	@Value("${com.xerago.rsa.common}")
	String commonServiceUrl;

	
	@Autowired
	DAOLayer daoLayer;

	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	private static final Log LOGGER = LogFactory.getLog(Validation.class);

	public List<String> validateGetQuote(MotorInsuranceModel motorInsuranceModel) throws Exception {
		List<String> errorCode = new ArrayList<>();

		Pattern objDatePattern = Pattern.compile(ValidationConstants.DATE_VALIDATION);
		SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date dtCurrent = new Date();
		String strCurrentDt = objSimpleDateFormat.format(dtCurrent);
		Date dtToday = objSimpleDateFormat.parse(objSimpleDateFormat.format(dtCurrent));

		if (motorInsuranceModel.getYearOfManufacture() <= 0) {
			errorCode.add("E-0006");
		}
		
	
		/*if ("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {
			 
			if (motorInsuranceModel.getYearOfManufacture() >= 0) {
				 int maxYear = Calendar.getInstance().get(Calendar.YEAR);
				LOGGER.info("maxYear =" + maxYear);
				int minYear = Calendar.getInstance().get(Calendar.YEAR) - 10;
				LOGGER.info("minYear =" + minYear);
				if (!(minYear <= motorInsuranceModel.getYearOfManufacture()
						&& maxYear >= motorInsuranceModel.getYearOfManufacture())) {
					errorCode.add("E-0405");
				}
			}
			 }*/
		if (StringUtils.isBlank(motorInsuranceModel.getVehicleRegisteredCity())) {
			errorCode.add("E-0007");
		} else {
			ApiResponse getStateForCity = getStateValueForCity(motorInsuranceModel.getVehicleRegisteredCity());
			if (getStateForCity.getData() == null || StringUtils.isBlank(getStateForCity.getData().getState())) {
				errorCode.add("E-0007");
			}
		}

		if (StringUtils.isBlank(motorInsuranceModel.getVehicleRegistrationDate())
				|| motorInsuranceModel.getVehicleRegistrationDate().equalsIgnoreCase(DATE_DEFAULT)) {
			errorCode.add("E-0008");
		} else if (objDatePattern.matcher(motorInsuranceModel.getVehicleRegistrationDate()).find()) {
			Date vehicleRegistrationDate = objSimpleDateFormat.parse(motorInsuranceModel.getVehicleRegistrationDate());
			if (!TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
				if (vehicleRegistrationDate.before(dtToday)) {
					errorCode.add("E-0009");

				}
				long differenceInDays = PortalDateConversion.diffDates(strCurrentDt,
						motorInsuranceModel.getVehicleRegistrationDate());
				LOGGER.info("differenceInDays =" + differenceInDays);
				if (differenceInDays > 30) {
					errorCode.add("E-0010");

				}

			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vehicleRegistrationDate);

			int vehicleRegYear = calendar.get(Calendar.YEAR);
			int yearOfManufacture = motorInsuranceModel.getYearOfManufacture();

			if (vehicleRegYear > 0 && yearOfManufacture > 0) {
				if (vehicleRegYear < yearOfManufacture) {
					errorCode.add("E-0011");

				}
				if (vehicleRegYear > yearOfManufacture + 1) {
					errorCode.add("E-0012");

				}
			}

		} else {
			errorCode.add("E-0013");
		}

		if (StringUtils.isBlank(motorInsuranceModel.getVehicleManufacturerName())) {
			errorCode.add("E-0014");

		}
		
		if (motorInsuranceModel.getSeatingCapacity() == 0 && motorInsuranceModel.getEngineCapacity() == 0) {
			errorCode.add("E-0017");
		}
		
		validateIDV(motorInsuranceModel, errorCode);
		
        if (StringUtils.isBlank(motorInsuranceModel.getVehicleModelCode())
				|| "0".equalsIgnoreCase(motorInsuranceModel.getVehicleModelCode())) {
			errorCode.add("E-0019");

		}

		if (StringUtils.isBlank(motorInsuranceModel.getVehicleModelName())) {
			errorCode.add("E-0019");
		}

		int yearOfManufacture = motorInsuranceModel.getYearOfManufacture();
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegistrationDate())){
			Date vehicleRegistrationDate = objSimpleDateFormat.parse(motorInsuranceModel.getVehicleRegistrationDate());
			LOGGER.info("Start Date:: "+motorInsuranceModel.getPolicyStartDate());
			if (StringUtils.isNotBlank(motorInsuranceModel.getPolicyStartDate())) {
				Date policyStartDate = objSimpleDateFormat.parse(motorInsuranceModel.getPolicyStartDate());
				if (vehicleRegistrationDate.after(policyStartDate)) {
					errorCode.add("E-0040");
				}
			}
			
			if (TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
				if (vehicleRegistrationDate.after(dtToday)) {
					errorCode.add("E-0043");
				}
			}
			
			if (!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()) && !DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
					&& StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())) {
				
				LOGGER.info("motorInsuranceModel.getPreviousPolicyExpiryDate() ::: " + motorInsuranceModel.getPreviousPolicyExpiryDate()+"vehicleRegistrationDate::;"+vehicleRegistrationDate);
				
				Date previousPolicyExpiryDate = objSimpleDateFormat.parse(motorInsuranceModel.getPreviousPolicyExpiryDate());
				if (previousPolicyExpiryDate.before(vehicleRegistrationDate)) {
					errorCode.add("E-0038");
				}
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(previousPolicyExpiryDate);
				int preExpYear = calendar.get(Calendar.YEAR);
				if (preExpYear > 0 && yearOfManufacture > 0 && preExpYear < yearOfManufacture) {
					errorCode.add("E-0039");
				}
				
				
               long differenceInDays = PortalDateConversion.diffDates(strCurrentDt,motorInsuranceModel.getPreviousPolicyExpiryDate());
			   int VehicleAgeindays = (int) (PortalDateConversion.diffDates(motorInsuranceModel.getVehicleRegistrationDate(), strCurrentDt));
			  
                LOGGER.info("VehicleAgeindays = "+VehicleAgeindays +" :: differenceInDays = "+differenceInDays);
				if (VehicleAgeindays < 2557) {
					LOGGER.info("Below 7 years ");
					if (differenceInDays > 60) {
						errorCode.add("E-0041");
					} else if (differenceInDays < -365 && !"DBS".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
						errorCode.add("E-0042");
					}
				} else {
					LOGGER.info("Above 7 years ");
					if (differenceInDays > 60) {
						errorCode.add("E-0041");
					} else if (differenceInDays < 0) {
						errorCode.add("E-0063");
					}
				}
				
			}
		}
		
		Pattern stringValidation = Pattern.compile(ValidationConstants.STRING_VALIDATION);
		Pattern mobileValidation = Pattern.compile(ValidationConstants.MOBILE_VALIDATION);
		
		if ( StringUtils.isNotBlank(motorInsuranceModel.getStrFirstName()) ) {
			if(!stringValidation.matcher(motorInsuranceModel.getStrFirstName()).find()){
				LOGGER.info("strFirstName  Validation failed");
				errorCode.add("E-0203");
			}
			
		}else{ 
			LOGGER.info("strFirstName  Validation failed");
			errorCode.add("E-0204");
		}
		
		if ( StringUtils.isBlank(motorInsuranceModel.getStrEmail())) {
			errorCode.add("E-0207");
		}
		
		if ( StringUtils.isNotBlank(motorInsuranceModel.getStrMobileNo())) {
			if (!mobileValidation.matcher(motorInsuranceModel.getStrMobileNo()).find()) {
				errorCode.add("E-0210");
			}
		} 
		/*LOGGER.info("motorInsuranceModel.getCpaPolicyTerm() = "+motorInsuranceModel.getCpaPolicyTerm());
		validateCpaTerm(motorInsuranceModel, errorCode);
		validateCpaSumInsured(motorInsuranceModel, errorCode);*/

		//Title Validation
		/*LOGGER.info("motorInsuranceModel.getTitle() = "+motorInsuranceModel.getStrTitle());
		if(motorInsuranceModel.getStrTitle() == null ){
			errorCode.add("E-0201");
		}else if(!checkTitle(motorInsuranceModel.getStrTitle())){
			errorCode.add("E-0201");
		}*/
			
		//vehicle subline validation
		
		  if(StringUtils.isBlank(motorInsuranceModel.getVehicleSubLine())){
		  errorCode.add("E-0044"); } else if
		 (!MOTOR_CYCLE.equalsIgnoreCase(motorInsuranceModel.getVehicleSubLine())) {
		  errorCode.add("E-0045"); }
		 
				
		//Swagger Validation
		LOGGER.info("Product Name:::"+motorInsuranceModel.getProductName());
		if (CollectionUtils.isNotEmpty(errorCode))
			return errorCode;
		else {
			errorCode = twoWheelerValidation.validateGetQuoteService(motorInsuranceModel);
			return errorCode;
		}
		
		
	}

	/**
	 * @author Paul [Paul Samuel V]
	 * @author roshini
	 *
	 * Validates CPA Term, if CPA is enabled (enabled by default, <code>cpaCoverIsRequired</code> is "no" if disabled)
	 *
	 * @param motorInsuranceModel - motor insurance model after model parsing
	 * @param errorCodes error code list - inOut ref var
	 *
	 * <hr/>
	 *
	 * Updated - 25 Feb 2019 - roshini
	 *
	 * for <b>Product - RollOverTwoWheeler</b>
	 *
	 * If the Type of cover is LiabilityOnly then,
	 * <code>policyTerm</code> will be '0' and LiabilityPolicyTerm, <code>cpaPolicyTerm</code> are '3'(default values).
	 *
	 * @see	com.xerago.rsa.webproxy.transform.FormPost#parseValue(Object)
	 * @see com.xerago.rsa.webproxy.transform.JsonXml#parseValue(Object)
	 *
	 * Updated - 18 March 2019 
	 * @author roshini
	 * CPA Policy term can be equal to policy term or less than liability policy term.
	 * <hr/>
	 * 
	 */
	private void validateCpaTerm(MotorInsuranceModel motorInsuranceModel, List<String> errorCodes) {
		if (motorInsuranceModel != null
				&& YES.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())
				&& !GET_QUOTE_PROCESS.equalsIgnoreCase(motorInsuranceModel.getProcessType())
				&& !StringUtils.containsIgnoreCase(motorInsuranceModel.getTypeOfCover(),"standalone") ) {

			if ("BrandnewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())
					&& INDIVIDUAL.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
					&& YES.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())) {

				if (motorInsuranceModel.getCpaPolicyTerm() < 1
						|| motorInsuranceModel.getCpaPolicyTerm() > Math.max(
						motorInsuranceModel.getPolicyTerm(), motorInsuranceModel.getLiabilityPolicyTerm())) {
					errorCodes.add("E-0350");
				}
			} else if (isRolloverQuote(motorInsuranceModel)
					&& YES.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())) {

				if (motorInsuranceModel.getLiabilityPolicyTerm() != motorInsuranceModel.getCpaPolicyTerm()
							&& (motorInsuranceModel.getCpaPolicyTerm() > motorInsuranceModel.getLiabilityPolicyTerm())){
					errorCodes.add("E-0351");
				}
			}

		}
	}

	/**
	 * @author Paul [Paul Samuel V]
	 *
	 * Validates CPA Sum insured, if CPA is enabled (enabled by default, <code>cpaCoverIsRequired</code> is "no" if disabled)
	 *
	 * @param motorInsuranceModel - motor insurance model after model parsing
	 * @param errorCodes error code list - inOut ref var
	 */
	private void validateCpaSumInsured(MotorInsuranceModel motorInsuranceModel, List<String> errorCodes) {
		if (motorInsuranceModel != null
				&& YES.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())
				&& !GET_QUOTE_PROCESS.equalsIgnoreCase(motorInsuranceModel.getProcessType()) 
				&& !StringUtils.containsIgnoreCase(motorInsuranceModel.getTypeOfCover(),"standalone") ) {

			if (motorInsuranceModel.getCpaSumInsured() != null) {

				// CPA sum insured can be availed between 15 lacs to 50lacs with multiples of lacs only.
				if (motorInsuranceModel.getCpaSumInsured() < 15_00_000
						|| motorInsuranceModel.getCpaSumInsured() > 50_00_000) {
					errorCodes.add("E-0424");
				}

				// CPA SI shall be incremented by multiples of 1 lac
				if (motorInsuranceModel.getCpaSumInsured() % 1_00_000 != 0) {
					errorCodes.add("E-0425");
				}
			}
			// cpa SI is optional - will be defaulted to 15 lacs at IF request formation
		}
	}

	private static boolean isRolloverQuote(MotorInsuranceModel motorInsuranceModel) {
		return ConstantVariables.PRODUCT_NAME_ROLLOVER_2_WHEELER.equalsIgnoreCase(motorInsuranceModel.getProductName())
				|| "true".equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder());
	}

	public boolean checkTitle(String inputTitle){
		List<String> titles = new LinkedList<String>();
		titles.add("MR");
		titles.add("MS");
		titles.add("MRS");
		return StringUtils.isNotBlank(inputTitle) ? titles.contains(inputTitle.toUpperCase()) ? true : false : false;
	}

	public List<String> validateCalculatePremium(MotorInsuranceModel motorInsuranceModel) throws Exception {
		List<String> errorCode = validateGetQuote(motorInsuranceModel);
		if (errorCode != null && !errorCode.isEmpty()) return errorCode;
		/*
		 * Task #269726 External reference number in calculate premium
		 * request-148728
		 */
		
	
		
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getStrMobileNo())) {
			if (StringUtils.length(motorInsuranceModel.getStrMobileNo()) != 10)
				errorCode.add("E-0217");
			if (!Constants.numericValidation.matcher(motorInsuranceModel.getStrMobileNo()).matches())
				errorCode.add("E-0210");
		} else
			errorCode.add("E-0216");
		
		
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getExternalReferenceNumber())) {
			if (motorInsuranceModel.getExternalReferenceNumber().length() > 50) {
				errorCode.add("E-0407");
			} else if (!StringUtils.isAlphanumeric(motorInsuranceModel.getExternalReferenceNumber())) {
				errorCode.add("E-0408");
			}
		}
		
	
/*
 * Task #262655 DOB and Gender need to be mandatory
 * 
 * 
 * 
 * 
 */
		/*if(motorInsuranceModel.getGender()==null|| motorInsuranceModel.getGender().equals("")){
			errorCode.add("E-0225");
		}*/
		
	if(StringUtils.isBlank(motorInsuranceModel.getGender())){
			errorCode.add("E-0202");
		}
	
		if(StringUtils.isBlank(motorInsuranceModel.getDateOfBirth())){
			errorCode.add("E-0212");
		}
		
		/*
		 * Task #269726 External reference number in calculate premium
		 * request-148728
		 */

		SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date dtCurrent = new Date();
		Date dtToday = objSimpleDateFormat.parse(objSimpleDateFormat.format(dtCurrent));
		
		if (StringUtils.isBlank(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
			errorCode.add("E-0023");
		}

		if (StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
				&& COMPANY.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
			String strCompName = motorInsuranceModel.getCompanyNameForCar();
			if (strCompName == null || strCompName.length() <= 0) {
				errorCode.add("E-0024");

			}
		}
		
				if ("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName()) && TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {

			if (!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())
					&& ((!"Yes".equalsIgnoreCase(motorInsuranceModel.getIsBreakinInsurance())
							&& !"Yes".equalsIgnoreCase(motorInsuranceModel.getIsUsedCar()))
					|| (!DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
							&& StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())))) {

				if (StringUtils.isBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())
						|| motorInsuranceModel.getPreviousPolicyExpiryDate().equalsIgnoreCase(DATE_DEFAULT)) {
					errorCode.add("E-0029");
				}

				if (StringUtils.isBlank(motorInsuranceModel.getPreviousPolicyType())) {
					errorCode.add("E-0030");

				}

				if (StringUtils.isBlank(motorInsuranceModel.getClaimsMadeInPreviousPolicy())) {
					errorCode.add("E-0032");

				} else if (YES.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())) {
					if (StringUtils.isBlank(motorInsuranceModel.getClaimsReported())) {
						errorCode.add("E-0033");

					}
					/*if (StringUtils.isBlank(motorInsuranceModel.getClaimAmountReceived())) {
						errorCode.add("E-0034");

					} else {
						try {
							long claimAmtRec = Long.parseLong(motorInsuranceModel.getClaimAmountReceived());
							if (claimAmtRec == 0) {
								errorCode.add("E-0035");

							} else if (claimAmtRec > 100000) {
								errorCode.add("E-0036");

							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
							errorCode.add("E-0037");

						}
					}*/
					
					LOGGER.info("product = "+motorInsuranceModel.getTypeOfCover());
				}else if (NO.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())) {
					if (StringUtils.isBlank(motorInsuranceModel.getNoClaimBonusPercent())) {
						errorCode.add("E-0031");
					}
				}
			}
			
			if (StringUtils.isBlank(motorInsuranceModel.getCarOwnerShip())) {
				errorCode.add("E-0305");

			} else {
				if (!(YES.equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip())
						|| NO.equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip()))) {
					errorCode.add("E-0306");

				}
			}

			validateIDV(motorInsuranceModel, errorCode);
			
			LOGGER.info("motorInsuranceModel.getPreviousInsurerName()::" + motorInsuranceModel.getPreviousInsurerName());
			// Task #187070 Previous Insurer - RSA - VPC - Start
			if ("ROYAL SUNDARAM GENERAL INSURANCE CO. LIMITED".equalsIgnoreCase(motorInsuranceModel.getPreviousInsurerName())) {
				errorCode.add("E-0057");
			}

		} else {
			Calendar calendar = Calendar.getInstance();
			LOGGER.info("Start Date:: "+motorInsuranceModel.getPolicyStartDate());
			Date policyStartDate = objSimpleDateFormat.parse(motorInsuranceModel.getPolicyStartDate());
			calendar.setTime(policyStartDate);

			if (policyStartDate.before(dtToday)) {
				errorCode.add("E-0021");

			}
			int policyInceptionYear = calendar.get(Calendar.YEAR);
			int yearOfManufacture = motorInsuranceModel.getYearOfManufacture();
			if (policyInceptionYear > 0 && yearOfManufacture > 0 && policyInceptionYear < yearOfManufacture) {
				errorCode.add("E-0022");

			}
		}

		
		if(!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()) && !"StandAlone".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())
				&&"RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())
				&& "TRUE".equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())){
				if (motorInsuranceModel.getPolicyTerm() == 0) 
					errorCode.add("E-0307");
				if (motorInsuranceModel.getPolicyTerm() >= 4) 
					errorCode.add("E-0316");	
				//CPA Policy Term
				validateCpaTerm(motorInsuranceModel, errorCode);
				validateCpaSumInsured(motorInsuranceModel, errorCode);
		}else if("LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())
				&&"RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())
				&& "TRUE".equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())){
				if (motorInsuranceModel.getLiabilityPolicyTerm() == 0) 
					errorCode.add("E-0307");
				if (motorInsuranceModel.getLiabilityPolicyTerm() >= 4) 
					errorCode.add("E-0316");	
				//CPA Policy Term
				validateCpaTerm(motorInsuranceModel, errorCode);
				validateCpaSumInsured(motorInsuranceModel, errorCode);
		}else {
			if(StringUtils.isBlank(motorInsuranceModel.getTypeOfCover()))
				errorCode.add("E-0327");
			//CPA Policy Term
			validateCpaTerm(motorInsuranceModel, errorCode);
			validateCpaSumInsured(motorInsuranceModel, errorCode);
		}

		
			
			
		
		//[Task #141300] Add-on cover additional Towing charges
		if(Constants.YES.equalsIgnoreCase(motorInsuranceModel.getTowingChargesCover())) {
			List<Double> towing_SI = Arrays.asList(Constants.TwoWheeler_Towing_SI);
			LOGGER.info("motorInsuranceModel.getTowingChargesCoverSI() ::: " + motorInsuranceModel.getTowingChargesCoverSI());
			if(!towing_SI.contains(motorInsuranceModel.getTowingChargesCoverSI())) {
				errorCode.add("E-0318");
			}
		}

		LOGGER.info("motorInsuranceModel.getIsPreviousPolicyHolder() ::: " + motorInsuranceModel.getIsPreviousPolicyHolder());

		if (StringUtils.isNotBlank(motorInsuranceModel.getDateOfBirth())) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			try {
				sdf.parse(motorInsuranceModel.getDateOfBirth());
			} catch (Exception e) {
				LOGGER.info("DATE_FORMAT parse exception");
				errorCode.add("E-0213");

			}
		}
		
		
		
		/**
		 * Task #285932 Validation to block duplicate policy- 160301
		 */
      /* LOGGER.info("EngineNo::::"+motorInsuranceModel.getEngineNumber()+"ChassisNumber::::"+motorInsuranceModel.getChassisNumber());
		
       if("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
	       if(!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()) && 
	    		    !motorInsuranceModel.getPreviousInsurerName().toLowerCase().contains("royal") &&
					!"AG023760".equalsIgnoreCase(motorInsuranceModel.getAgentId()) && 
					!"AG030843".equalsIgnoreCase(motorInsuranceModel.getAgentId()) &&
					!"AG018098".equalsIgnoreCase(motorInsuranceModel.getAgentId()) &&
					twoWheelerDAO.getDPolicyVehicleDetails(motorInsuranceModel.getEngineNumber(),
							motorInsuranceModel.getChassisNumber(),motorInsuranceModel.getRegistrationNumber())){
					errorCode.add("E-0349");
			}
       }*/
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getGstin()) && (!StringUtils.isAlphanumeric(motorInsuranceModel.getGstin())
				|| motorInsuranceModel.getGstin().length() != 15)) {
			errorCode.add("E-0356");
		}
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getGstin())
				&& (StringUtils.isAlphanumeric(motorInsuranceModel.getGstin())
						&& motorInsuranceModel.getGstin().length() == 15)) {
		LOGGER.info("city::: " + motorInsuranceModel.getContactCity());
		String getStateForCity = twoWheelerDAO.getStateForCity(motorInsuranceModel.getContactCity());
		LOGGER.info("getStateForCity::" + getStateForCity);
		String gs = motorInsuranceModel.getGstin();
		GstinStateMaster gstinStateMaster = twoWheelerDAO.getGstinStateMaster(getStateForCity);
		if (gstinStateMaster != null && StringUtils.isNotBlank(gs)) {
			String gstinstatecode = gstinStateMaster.getStateCode();
			String gsc = gs.substring(0, 2);
			if (!gsc.equals(gstinstatecode)) {
				errorCode.add("E-0346");
			}
		}
		}

		if (StringUtils.isBlank(motorInsuranceModel.getContactState())) {
			errorCode.add("E-0377");
		}
		
		
		//Task #278458 - POS DETAILS RS 154761 - TECH
		if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsPosOpted())) {

			boolean isPosCode = false;
			if (StringUtils.isNotBlank(motorInsuranceModel.getPosCode())) {
				DPoscodeMaster dPoscodeDetails = daoLayer.get(DPoscodeMaster.class, motorInsuranceModel.getPosCode());
				if (dPoscodeDetails == null) {
					errorCode.add("E-0317");

				} else
					isPosCode = true;
			} else if (StringUtils.isBlank(motorInsuranceModel.getPosName())
					|| StringUtils.isBlank(motorInsuranceModel.getPosPAN())
					//Task #305179 NEW REQUIREMENT FOR POS ALM:176950
					/*|| StringUtils.isBlank(motorInsuranceModel.getPosAadhaar())*/
					|| StringUtils.isBlank(motorInsuranceModel.getPosMobile())
					|| StringUtils.isBlank(motorInsuranceModel.getPosLicenceExpDate())) {
				errorCode.add("E-0365");

			}

			if (!isPosCode) {
				if (StringUtils.isNotBlank(motorInsuranceModel.getPosName())
						&& (!Pattern.matches(Constants.STRING_WITHOUT_SPECIAL_CHARACTERS_VALIDATION,
								motorInsuranceModel.getPosName()) || motorInsuranceModel.getPosName().length() > 30)) {
					errorCode.add("E-0360");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosPAN()) && (!(motorInsuranceModel.getPosPAN().length() == 10)
						|| !Pattern.matches(Constants.PAN_CARD_VALIDATION, motorInsuranceModel.getPosPAN()))) {

					errorCode.add("E-0361");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosAadhaar())
						&& (!StringUtils.isNumeric(motorInsuranceModel.getPosAadhaar()) && !Pattern.matches(Constants.AADHAAR_VALIDATION, motorInsuranceModel.getPosAadhaar())
								|| !(motorInsuranceModel.getPosAadhaar().length() == 12))) {

					errorCode.add("E-0362");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosMobile())
						&& (!(motorInsuranceModel.getPosMobile().length() == 10)
								|| !Pattern.matches(Constants.Mobile_No_Validation, motorInsuranceModel.getPosMobile()))) {

					errorCode.add("E-0363");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosLicenceExpDate())) {
					try {
						Date date = objSimpleDateFormat.parse(motorInsuranceModel.getPosLicenceExpDate());
						if (!motorInsuranceModel.getPosLicenceExpDate().equals(objSimpleDateFormat.format(date)) 
								|| !Pattern.matches(Constants.DATE_VALIDATION,motorInsuranceModel.getPosLicenceExpDate())) {
							errorCode.add("E-0364");

						}
					} catch (Exception e) {
						errorCode.add("E-0364");

					}
				}
			}

		}
		
		LOGGER.info("CpaCoverIsRequired() == "+motorInsuranceModel.getCpaCoverIsRequired());
		//Task #296434 CPA cover changes for all motor products-163348 
		if("Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
				&& !"StandAlone".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())) {
			
			if(StringUtils.isBlank(motorInsuranceModel.getCpaCoverIsRequired()))
				errorCode.add("E-0411");
			
			if("No".equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())){
				/*if( !motorInsuranceModel.isNoEffectiveDrivingLicense() & !motorInsuranceModel.isCpaCoverWithInternalAgent() & !motorInsuranceModel.isStandalonePAPolicy()){
					errorCode.add("E-0412");	
				}else*/ 
				if(motorInsuranceModel.isNoEffectiveDrivingLicense() 
						& motorInsuranceModel.isCpaCoverWithInternalAgent() 
						& motorInsuranceModel.isStandalonePAPolicy()){
					errorCode.add("E-0417");	
				}else {
					if(motorInsuranceModel.isCpaCoverWithInternalAgent() || motorInsuranceModel.isStandalonePAPolicy() ){
						if (motorInsuranceModel.isCpaCoverWithInternalAgent() & motorInsuranceModel.isStandalonePAPolicy()){
							errorCode.add("E-0416");	
						}
						if (motorInsuranceModel.isNoEffectiveDrivingLicense() & motorInsuranceModel.isStandalonePAPolicy()){
							errorCode.add("E-0418");	
						}
						if (motorInsuranceModel.isNoEffectiveDrivingLicense() & motorInsuranceModel.isCpaCoverWithInternalAgent()){
							errorCode.add("E-0419");	
						}
						
						if(StringUtils.isNotBlank(motorInsuranceModel.getCpaCoverPolicyNumber()) ){
							//errorCode.add("E-0420");
							if(!Pattern.matches(Constants.Policy_No_Validation,motorInsuranceModel.getCpaCoverPolicyNumber()))
								errorCode.add("E-0414");
						}
						if(StringUtils.isNotBlank(motorInsuranceModel.getCpaCoverExpiryDate())){
							//errorCode.add("E-0422");
						 if(!motorInsuranceModel.getCpaCoverExpiryDate().equals(objSimpleDateFormat.format(objSimpleDateFormat.parse(motorInsuranceModel.getCpaCoverExpiryDate()))) 
								|| !Pattern.matches(Constants.DATE_VALIDATION,motorInsuranceModel.getCpaCoverExpiryDate())
								|| !MotorUtils.checkExpiryDateCpaPolicyCover(motorInsuranceModel.getCpaCoverExpiryDate()) ) 
							errorCode.add("E-0415");
						}
						/*if(StringUtils.isBlank(motorInsuranceModel.getCpaCoverCompanyName()) )
							errorCode.add("E-0421");
						else{
							if(!twoWheelerDAO.getPreviousInsurer(motorInsuranceModel.getCpaCoverCompanyName()) ){
								errorCode.add("E-0413");	
							}
						}*/
					}
				   }
				}
		}
		
		

		allowTPPolicy(motorInsuranceModel, errorCode);

		
		//Swagger Validation
		if (CollectionUtils.isNotEmpty(errorCode))
			return errorCode;
		else {
			errorCode = twoWheelerValidation.validateCalculatePremiumService(motorInsuranceModel);
			return errorCode;
		}
	}
	public List<String> validateUpdateVehicledetails(MotorInsuranceModel motorInsuranceModel) throws Exception {
		List<String> errorCode = validateCalculatePremium(motorInsuranceModel);
		if (errorCode != null && !errorCode.isEmpty()) return errorCode;

		SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date dtCurrent = new Date();
		String strCurrentDt = objSimpleDateFormat.format(dtCurrent);
			
		
		//For QuoteId
		if(StringUtils.isNotBlank(motorInsuranceModel.getQuoteId())){
			DQuoteDetails dquoteDetails =twoWheelerDAO.getQuoteDetails(motorInsuranceModel.getQuoteId());
				if (dquoteDetails.getQuoteId()!=null) {
					LOGGER.info("the d quate Detiails is"+dquoteDetails.getQuoteId());
			StringUtils.equalsIgnoreCase(dquoteDetails.getQuoteId(), motorInsuranceModel.getQuoteId());
				}
				else{
					errorCode.add("E-0064");
				}
			}
		else{
		     errorCode.add("E-0301");
			}

		if (StringUtils.isNotBlank(motorInsuranceModel.getGstin()) && (!StringUtils.isAlphanumeric(motorInsuranceModel.getGstin())
				|| motorInsuranceModel.getGstin().length() != 15)) {
			errorCode.add("E-0356");
		}
		
		if( !YES.equalsIgnoreCase(motorInsuranceModel.getUpdatePanAaadharLater()) ){
			if (StringUtils.isNotBlank(motorInsuranceModel.getPanNumber()) && !Pattern.matches(Constants.PAN_CARD_VALIDATION, motorInsuranceModel.getPanNumber())) {
				errorCode.add("E-0361");
			}
			if (StringUtils.isNotBlank(motorInsuranceModel.getAadharNumber())&& !Pattern.matches(Constants.AADHAAR_VALIDATION, motorInsuranceModel.getAadharNumber())){
				errorCode.add("E-0222");
			}	
		}
		
		/**
		 * Task #285932 Validation to block duplicate policy- 160301
		 */
       LOGGER.info("EngineNo::::"+motorInsuranceModel.getEngineNumber()+"ChassisNumber::::"+motorInsuranceModel.getChassisNumber());
      if(!CoverFoxAgent.contains(motorInsuranceModel.getAgentId())) {
    	  if(isRolloverQuote(motorInsuranceModel)) {
				if (twoWheelerDAO.getDPolicyVehicleDetails(motorInsuranceModel.getRegistrationNumber())){
					errorCode.add("E-0349");
				}	
			}else {
    	   if( twoWheelerDAO.getDPolicyVehicleDetails(motorInsuranceModel.getEngineNumber(),motorInsuranceModel.getChassisNumber(),motorInsuranceModel.getRegistrationNumber())){
			errorCode.add("E-0349");
    	   }
			}
       }
		
		if(StringUtils.isNotBlank(motorInsuranceModel.getEiaNumber())){
			if(!Pattern.matches(Constants.EIA_NO_VALIDATION,motorInsuranceModel.getEiaNumber())){
				errorCode.add("E-0226");
			}
			
		}
		InsuranceRepositoryMaster insuranceRepositoryMaster= null;
		if(StringUtils.isNotBlank(motorInsuranceModel.getIrName())){
			String irName=motorInsuranceModel.getIrName();
			
			insuranceRepositoryMaster= daoLayer.getIrName(irName);
				if (insuranceRepositoryMaster!=null) {
					LOGGER.info("the in surance repository master is"+insuranceRepositoryMaster.getIrName());
					StringUtils.equalsIgnoreCase(insuranceRepositoryMaster.getIrName(), motorInsuranceModel.getIrName());
				}
				else{
					errorCode.add("E-0227");
				}
				
			}
		

		if (TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {

			//Ignore Previous Policy Details if it is mentioned as Breakin from request
			if (!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())
					&& ((!"Yes".equalsIgnoreCase(motorInsuranceModel.getIsBreakinInsurance())
							&& !"Yes".equalsIgnoreCase(motorInsuranceModel.getIsUsedCar()))
					|| (!DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
							&& StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())))) {

				if (StringUtils.isBlank(motorInsuranceModel.getPreviuosPolicyNumber()) && !"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())) {
					errorCode.add("E-0366");
				}

				if (StringUtils.isBlank(motorInsuranceModel.getPreviousInsurerName()) && !"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())) {
					errorCode.add("E-0368");

				}
				
				if (StringUtils.isNotBlank(motorInsuranceModel.getPreviuosPolicyNumber())) {
					if (!Pattern.matches(Constants.Policy_No_Validation, motorInsuranceModel.getPreviuosPolicyNumber())) {
						errorCode.add("E-0367");
					}

					if (motorInsuranceModel.getPreviuosPolicyNumber().length() < 3) {
						errorCode.add("E-0370");

					}

					if (motorInsuranceModel.getPreviuosPolicyNumber().length() > 30) {
						errorCode.add("E-0369");

					}
				}
				
				if (StringUtils.isNotBlank(motorInsuranceModel.getPreviousInsurerName())) {
					if (motorInsuranceModel.getPreviousInsurerName().length() < 5) {
						errorCode.add("E-0371");

					}

					if (motorInsuranceModel.getPreviousInsurerName().length() > 150) {
						errorCode.add("E-0372");

					}
				}
				
				long differenceInDays = PortalDateConversion.diffDates(strCurrentDt,motorInsuranceModel.getPreviousPolicyExpiryDate());
				int VehicleAgeindays = (int) (PortalDateConversion.diffDates(motorInsuranceModel.getVehicleRegistrationDate(), strCurrentDt));
				int vehicleage = (int) (PortalDateConversion.diffDates(strCurrentDt, motorInsuranceModel.getVehicleRegistrationDate()) / 365);
				
				int differenceInDaysLimit = 0;
				if(VehicleAgeindays < 2557) {
					if ("DBS".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
						differenceInDaysLimit=-2557;
					}else{
						differenceInDaysLimit = -365;
					}
				}
				else {
					differenceInDaysLimit = 0;
				}
				LOGGER.info("Difference in days:: "+differenceInDays);
				Set<AgentPlanMapping> AgentPlanMapping = twoWheelerDAO.getAgentMaster(motorInsuranceModel.getAgentId()).getAgentPlanMappings();
				boolean checkagentId = false;
				for (AgentPlanMapping planUnderAgentID : AgentPlanMapping) {
					LOGGER.info("PlanUnderAgentId:: "+planUnderAgentID.getPlanType());
					if("TWOWHEELER".equalsIgnoreCase(planUnderAgentID.getPlanType())) {
						checkagentId = true;
					}
				}
				
				boolean graceperiodcheck = (checkagentId || !motorInsuranceModel.isSevenDaysBreakIn()) ? differenceInDays < differenceInDaysLimit
						: differenceInDays < 0;
				LOGGER.info("graceperiodcheck =" + (differenceInDays < differenceInDaysLimit));
				/*boolean graceperiodcheck = ("RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId())
						|| "DBS".equalsIgnoreCase(motorInsuranceModel.getAgentId())
						|| Constants.YES.equalsIgnoreCase(motorInsuranceModel.getIsVehicleInspected())
						|| motorInsuranceModel.isSevenDaysBreakIn()) ? differenceInDays < -90 : differenceInDays < 0;*/
				LOGGER.info("graceperiodcheck =" + graceperiodcheck);

				if (graceperiodcheck) {
					errorCode.add("E-0042");
				}
				
			}   
			
			// Task #186468 Develop Break In service for Policy Bazaar
		/*	if (Constants.YES.equalsIgnoreCase(motorInsuranceModel.getIsVehicleInspected())) {

				if (StringUtils.isBlank(motorInsuranceModel.getVirNumber())) {
					errorCode.add("E-0322");

				}

				if (StringUtils.isBlank(motorInsuranceModel.getVehicleInspectionDate())) {
					errorCode.add("E-0323");

				} else {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT1);
						SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(Constants.DATE_FORMAT);
						LOGGER.info("motorInsuranceModel.getVehicleInceptionDate() ::: "
								+ motorInsuranceModel.getVehicleInspectionDate());
						Date vehicleInspectionDate = simpleDateFormat.parse(motorInsuranceModel.getVehicleInspectionDate());
						String vehicleInspectionDateStr = simpleDateFormat1.format(vehicleInspectionDate);
						if (PortalDateConversion.diffDates(vehicleInspectionDateStr, strCurrentDt) < 0) {
							errorCode.add("E-0324");

						}
						LOGGER.info("PD::"+motorInsuranceModel.getPreviousQuoteDate());
						if (motorInsuranceModel.getPreviousQuoteDate() != null) {
							String previousQuoteDate = simpleDateFormat1.format(motorInsuranceModel.getPreviousQuoteDate());
							LOGGER.info("previousQuoteDate =" + previousQuoteDate);
							if (PortalDateConversion.diffDates(previousQuoteDate, vehicleInspectionDateStr) < 0) {
								errorCode.add("E-0325");

							}
						}
					} catch (ParseException e) {
						errorCode.add("E-0323");
					}
				}
			}*/

		}
		
		
		

		if (StringUtils.isBlank(motorInsuranceModel.getContactCity())) {
			errorCode.add("E-0376");

		} else {
			ApiResponse getStateForCity = getStateValueForCity(motorInsuranceModel.getVehicleRegisteredCity());
			if (getStateForCity.getData() == null || StringUtils.isBlank(getStateForCity.getData().getState())) {
				errorCode.add("E-0007");
			}
		}

		if (StringUtils.isBlank(motorInsuranceModel.getRegistrationNumber())
				&& TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
			errorCode.add("E-0101");

		}
		if (StringUtils.isBlank(motorInsuranceModel.getEngineNumber())) {
			errorCode.add("E-0102");

		}
		if (StringUtils.isBlank(motorInsuranceModel.getChassisNumber())) {
			errorCode.add("E-0103");

		}
		if (StringUtils.isBlank(motorInsuranceModel.getIsCarFinanced())) {
			errorCode.add("E-0104");

		} else if (YES.equalsIgnoreCase(motorInsuranceModel.getIsCarFinanced())) {
			if (StringUtils.isBlank(motorInsuranceModel.getIsCarFinancedValue())) {
				errorCode.add("E-0105");

			}
			if (StringUtils.isBlank(motorInsuranceModel.getFinancierName())) {
				errorCode.add("E-0106");

			}
		}

		if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsPosOpted())) {

			boolean isPosCode = false;
			if (StringUtils.isNotBlank(motorInsuranceModel.getPosCode())) {
				DPoscodeMaster dPoscodeDetails = daoLayer.get(DPoscodeMaster.class, motorInsuranceModel.getPosCode());
				if (dPoscodeDetails == null) {
					errorCode.add("E-0317");

				} else
					isPosCode = true;
			} else if (StringUtils.isBlank(motorInsuranceModel.getPosName())
					|| StringUtils.isBlank(motorInsuranceModel.getPosPAN())
					//Task #305179 NEW REQUIREMENT FOR POS ALM:176950
					/*|| StringUtils.isBlank(motorInsuranceModel.getPosAadhaar())*/
					|| StringUtils.isBlank(motorInsuranceModel.getPosMobile())
					|| StringUtils.isBlank(motorInsuranceModel.getPosLicenceExpDate())) {
				errorCode.add("E-0365");

			}

			if (!isPosCode) {
				if (StringUtils.isNotBlank(motorInsuranceModel.getPosName())
						&& (!Pattern.matches(Constants.STRING_WITHOUT_SPECIAL_CHARACTERS_VALIDATION,
								motorInsuranceModel.getPosName()) || motorInsuranceModel.getPosName().length() > 30)) {
					errorCode.add("E-0360");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosPAN()) && (!(motorInsuranceModel.getPosPAN().length() == 10)
						|| !Pattern.matches(Constants.PAN_CARD_VALIDATION, motorInsuranceModel.getPosPAN()))) {

					errorCode.add("E-0361");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosAadhaar())
						&& (!StringUtils.isNumeric(motorInsuranceModel.getPosAadhaar())
								|| !(motorInsuranceModel.getPosAadhaar().length() == 12))) {

					errorCode.add("E-0362");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosMobile())
						&& (!(motorInsuranceModel.getPosMobile().length() == 10)
								|| !Pattern.matches(Constants.Mobile_No_Validation, motorInsuranceModel.getPosMobile()))) {

					errorCode.add("E-0363");

				}

				if (StringUtils.isNotBlank(motorInsuranceModel.getPosLicenceExpDate())) {
					try {
						Date date = objSimpleDateFormat.parse(motorInsuranceModel.getPosLicenceExpDate());
						if (!motorInsuranceModel.getPosLicenceExpDate().equals(objSimpleDateFormat.format(date))) {
							errorCode.add("E-0364");

						}
					} catch (Exception e) {
						errorCode.add("E-0364");

					}
					
					
				}
			}

		}
		validateTPPolicyDetails(motorInsuranceModel, errorCode);

		//validateCpaTerm(motorInsuranceModel, errorCode);
		//validateCpaSumInsured(motorInsuranceModel, errorCode);

		//Swagger Validation
		if (CollectionUtils.isNotEmpty(errorCode))
			return errorCode;
		else {
			errorCode = twoWheelerValidation.validateUpdateVehicleDetailsService(motorInsuranceModel);
			return errorCode;
		}
	}
	
	
	public List<String> validateGProposal(MotorInsuranceModel motorInsuranceModel) {
		
		Pattern emailValidation = Pattern.compile(ValidationConstants.EMAIL_VALIDATION);
		List<String> errorCode = new ArrayList<>();
		
		if(StringUtils.isBlank(motorInsuranceModel.getQuoteId())){
			errorCode.add("E-0301");
			LOGGER.info("Quote Id is mandatory");
		}
		
		if(motorInsuranceModel.getPremium() <= 0){
			errorCode.add("E-0302");
			LOGGER.info("Premium is mandatory");
		}
		
		if(StringUtils.isBlank(motorInsuranceModel.getEmailId()) || !emailValidation.matcher(GenericUtils.checkString(motorInsuranceModel.getEmailId())).find()){
			errorCode.add("E-0303");
			LOGGER.info("Email id is mandatory and should be in regular format");
		}
		
		return errorCode;
	}
	
	public ApiResponse getStateValueForCity(String carRegisterationCity) throws Exception {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("reg_city", carRegisterationCity);
		return restTemplate.getForObject(commonServiceUrl + "/Services/Common/LookupService/SearchState?city={reg_city}",
				ApiResponse.class, uriVariables);

	}

	/**
	 * 
	 * @param motorInsuranceModel
	 * @param errorCode
	 * @author roshini
	 * @since 2019-06-14
	 */
	private void validateTPPolicyDetails(MotorInsuranceModel motorInsuranceModel, List<String> errorCode) {
		
		if (StringUtils.isNotBlank(motorInsuranceModel.getTypeOfCover())
				&& StringUtils.equalsIgnoreCase(motorInsuranceModel.getTypeOfCover(),"StandAlone")) {
			if (StringUtils.isBlank(motorInsuranceModel.getTpInsurer())) {
				errorCode.add("E-0428");
			}
			
			if (StringUtils.isBlank(motorInsuranceModel.getTpAddress1())) {
				errorCode.add("E-0465");
			}else if (StringUtils.isNotBlank(motorInsuranceModel.getTpAddress1()) && motorInsuranceModel.getTpAddress1().length() > 100) {
				errorCode.add("E-0466");
			}
			if(StringUtils.isBlank(motorInsuranceModel.getTpCity())) {
				errorCode.add("E-0467");
			} else if (StringUtils.isNotBlank(motorInsuranceModel.getTpCity())
					&& StringUtils.isBlank(motorInsuranceModel.getTpState())) {
				errorCode.add("E-0468");
			}
			if(StringUtils.isBlank(motorInsuranceModel.getTpPincode())) {
				errorCode.add("E-0469");
			}else if (StringUtils.isNotBlank(motorInsuranceModel.getTpPincode()) 
					&& !Pattern.matches(Constants.PINCODE_VALIDATION, motorInsuranceModel.getTpPincode())) {
				errorCode.add("E-0470");
			}
			if (StringUtils.isBlank(motorInsuranceModel.getTpPolicyNumber())) {
				errorCode.add("E-0429");
			} else if (StringUtils.isNotBlank(motorInsuranceModel.getTpPolicyNumber()) 
					&& !Pattern.matches(Constants.Policy_No_Validation, motorInsuranceModel.getTpPolicyNumber())) {
				errorCode.add("E-0430");
			}
			if(motorInsuranceModel.getPolicyTerm() > 5) {
				errorCode.add("E-0439");
			}
			if(motorInsuranceModel.getTpPolicyTerm() > 5) {
				errorCode.add("E-0437");
			}
			if (motorInsuranceModel.getTpPolicyTerm() == 0 ) {
				errorCode.add("E-0431");
			} 
			
			if (StringUtils.isBlank(motorInsuranceModel.getTpInceptionDate()) ||  !Pattern.matches(Constants.DATE_VALIDATION,
					motorInsuranceModel.getTpInceptionDate())) {
				errorCode.add("E-0433");
			} else if (StringUtils.isBlank(motorInsuranceModel.getTpExpiryDate()) ||  !Pattern.matches(Constants.DATE_VALIDATION,
					motorInsuranceModel.getTpExpiryDate())) {
				errorCode.add("E-0434");
			} else {
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				try {
					Date tpInceptionDate = format.parse(motorInsuranceModel.getTpInceptionDate());
					Date policyStartDate = format.parse(motorInsuranceModel.getPolicyStartDate());
					Date tpExpiryDate = format.parse(motorInsuranceModel.getTpExpiryDate());
					Date policyExpiryDate = format.parse(new MotorValidation().setExpiryDate(motorInsuranceModel));;
					LOGGER.info("tp date :: "+tpInceptionDate);
					LOGGER.info("policy start date :: "+policyStartDate);
					LOGGER.info("tpExpiryDate = "+tpExpiryDate);
					LOGGER.info("policyExpiryDate = "+policyExpiryDate);
					if(tpInceptionDate.after(policyStartDate)) {
						errorCode.add("E-0436");
					}
					/*if(tpExpiryDate.before(policyExpiryDate)){
						errorCode.add("E-0438");	
					}*/
				} catch (ParseException e) {
					LOGGER.info("TP Date Format Error");
					errorCode.add("E-0435");
				}
			}
		}
	}
	
	
	/**
	 * <p>
	 * Insured Declared Value Logic for RollOverTwowheeler
	 * </p>
	 * <p>
	 * Check IDV and Modified IDV
	 * </p>
	 * <p>
	 * First year IDV should be greater than 2nd year and third year.Second year
	 * IDV should be greater than third year.
	 * </p>
	 * <ul>
	 * <li>Modified IDV comparison with table Limits taken from
	 * DModifiedIDVRangeMaster.</li>
	 * <li>Modified IDV percentage comparison.</li>
	 * <li>Modified IDV comparison with Original IDV value.</li>
	 * </ul>
	 * 
	 * @param motorInsuranceModel
	 * @param errorCode
	 * @author roshini
	 * @since 2019-06-14
	 */
	private  List<String>  validateIDV(MotorInsuranceModel motorInsuranceModel, List<String> errorCode) {
		if("RollOverTwowheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {
		if (!StringUtils.containsIgnoreCase( motorInsuranceModel.getTypeOfCover(),"Liability")) {

			if (!TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
				if (motorInsuranceModel.getPolicyTerm() == 2) {
					if (motorInsuranceModel.getIdvFor2Year() == 0) {
						errorCode.add("E-0309");
					}
				}
				if (motorInsuranceModel.getPolicyTerm() == 3) {
					if (motorInsuranceModel.getIdvFor3Year() == 0) {
						errorCode.add("E-0312");
					}
				}
				if (motorInsuranceModel.getPolicyTerm() == 4) {

					if (motorInsuranceModel.getIdvFor4Year() == 0) {
						errorCode.add("E-0452");
					}
				}
				if (motorInsuranceModel.getPolicyTerm() == 5) {
					if (motorInsuranceModel.getIdvFor5Year() == 0) {
						errorCode.add("E-0453");
					}
				}
			} else {
				LOGGER.info("idv deviaTION...");
				if (motorInsuranceModel.getPolicyTerm() >= 2) {

					if (motorInsuranceModel.getIdvFor2Year() == 0) {
						errorCode.add("E-0309");
					}
					double idv = motorInsuranceModel.getIdvFor1Year();
					LOGGER.info("..." + idv);
					double idv2 = motorInsuranceModel.getIdvFor2Year();
					LOGGER.info("......." + idv2);
					if (idv < idv2) {
						errorCode.add("E-0314");
					}

					if (motorInsuranceModel.getIdvFor2Year() != 0) {

						double idvfor2ndYear = motorInsuranceModel.getIdvFor2Year();

						if (motorInsuranceModel.getModifiedIdvfor2Year() != 0) {

							double modifiedIDVfor2ndYear = motorInsuranceModel.getModifiedIdvfor2Year();

							if (idvfor2ndYear < modifiedIDVfor2ndYear) {

								if (StringUtils.isBlank(motorInsuranceModel.getDiscountIDVPercent2Year())) {
									errorCode.add("E-0310");
								}
							}
						} else {
							errorCode.add("E-0308");
						}
					} else {
						errorCode.add("E-0309");
					}
				}

				if (motorInsuranceModel.getPolicyTerm() >= 3) {

					if (motorInsuranceModel.getIdvFor3Year() == 0) {
						errorCode.add("E-0312");
					}
					double idv2 = motorInsuranceModel.getIdvFor2Year();
					double idv3 = motorInsuranceModel.getIdvFor3Year();
					if (idv2 < idv3) {
						errorCode.add("E-0315");
					}

					if (motorInsuranceModel.getIdvFor3Year() != 0) {

						double idvfor3ndYear = motorInsuranceModel.getIdvFor3Year();
						if (motorInsuranceModel.getModifiedIdvfor3Year() != 0) {

							double modifiedIDVfor3ndYear = motorInsuranceModel.getModifiedIdvfor3Year();

							if (idvfor3ndYear < modifiedIDVfor3ndYear) {
								if (StringUtils.isBlank(motorInsuranceModel.getDiscountIDVPercent3Year())) {
									errorCode.add("E-0313");
								}
							}

						} else {
							errorCode.add("E-0312");
						}
					} else {
						errorCode.add("E-0311");
					}
				}
				if (motorInsuranceModel.getPolicyTerm() >= 4) {

					if (motorInsuranceModel.getIdvFor4Year() == 0) {
						errorCode.add("E-0442");
					}
					double idv3 = motorInsuranceModel.getIdvFor3Year();
					double idv4 = motorInsuranceModel.getIdvFor4Year();
					if (idv3 < idv4) {
						errorCode.add("E-0443");
					}
					if (motorInsuranceModel.getIdvFor4Year() != 0) {

						double idvfor4thYear = motorInsuranceModel.getIdvFor4Year();
						if (motorInsuranceModel.getModifiedIdvfor4Year() != 0) {

							double modifiedIDVfor4thYear = motorInsuranceModel.getModifiedIdvfor4Year();

							if (idvfor4thYear < modifiedIDVfor4thYear) {
								if (StringUtils.isBlank(motorInsuranceModel.getDiscountIDVPercent4Year())) {
									errorCode.add("E-0446");
								}
							}
						} else {
							errorCode.add("E-0444");
						}
					} else {
						errorCode.add("E-0445");
					}
				}
				if (motorInsuranceModel.getPolicyTerm() >= 5) {

					if (motorInsuranceModel.getIdvFor5Year() == 0) {
						errorCode.add("E-0447");

					}
					double idv4 = motorInsuranceModel.getIdvFor4Year();
					double idv5 = motorInsuranceModel.getIdvFor5Year();
					if (idv4 < idv5) {
						errorCode.add("E-0448");
					}
					if (motorInsuranceModel.getIdvFor5Year() != 0) {

						double idvfor5thYear = motorInsuranceModel.getIdvFor5Year();
						if (motorInsuranceModel.getModifiedIdvfor5Year() != 0) {

							double modifiedIDVfor5thYear = motorInsuranceModel.getModifiedIdvfor5Year();
							if (idvfor5thYear < modifiedIDVfor5thYear) {
								if (StringUtils.isBlank(motorInsuranceModel.getDiscountIDVPercent5Year())) {
									errorCode.add("E-0451");
								}
							}
						} else {
							errorCode.add("E-0449");
						}
					} else {
						errorCode.add("E-0450");
					}
				}
			}
			
			if (StringUtils.isBlank(motorInsuranceModel.getTotalIdv())) {
				errorCode.add("E-0357");
			}
		

		long discountIdvPercent = Long.parseLong(motorInsuranceModel.getDiscountidvPercent());
		int minIdvDiscount = -20;
		int maxIdvDiscount = 30;

		DModifyidvRangeMaster dModifyidvRangeMaster = twoWheelerDAO
				.getDModifyidvRangeMaster(motorInsuranceModel.getAgentId(), "TWOWHEELER", "MotorCyclePackage");
		if (dModifyidvRangeMaster != null) {
			minIdvDiscount = dModifyidvRangeMaster.getMinValue();
			maxIdvDiscount = dModifyidvRangeMaster.getMaxValue();
		}

		if (discountIdvPercent < minIdvDiscount || discountIdvPercent > maxIdvDiscount) {
			errorCode.add("E-0358");
		} else {
			LOGGER.info("motorInsuranceModel.getTotalIdv()= " + motorInsuranceModel.getTotalIdv());
			LOGGER.info("getOridinalIdvFor1stYear= " + motorInsuranceModel.getOriginalIdvFor1Year());

			if(motorInsuranceModel.getOriginalIdvFor1Year() == null 
					|| StringUtils.isBlank(motorInsuranceModel.getOriginalIdvFor1Year())){
				errorCode.add("E-0017");
				return errorCode;
			}
			
			long minLimitForDeviation = Math
					.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor1Year())
							+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor1Year()) * minIdvDiscount / 100))
					- 5;
			long maxLimitForDeviation = Math
					.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor1Year())
							+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor1Year()) * maxIdvDiscount / 100))
					+ 5;

			LOGGER.info("\n minLimitForDeviation = " + minLimitForDeviation + "\n maxLimitForDeviation = "
					+ maxLimitForDeviation);
			if (!(minLimitForDeviation < motorInsuranceModel.getIdvFor1Year())
					|| !(maxLimitForDeviation > motorInsuranceModel.getIdvFor1Year())) {
				LOGGER.info("IDV going beyond the min limit ");
				errorCode.add("E-0406");
			} else {

				double orginalIdv = motorInsuranceModel.getIdvFor1Year();
				// long modifiedIdv = (long) motorInsuranceModel.getIdv();
				double modifiedIdv = motorInsuranceModel.getModifiedIdvfor1Year();
				LOGGER.info("discount percent:: " + discountIdvPercent);
				double actualModifiedIdv = Math.round(orginalIdv + (orginalIdv * discountIdvPercent / 100));
				LOGGER.info("Actual idv:: " + actualModifiedIdv);
				LOGGER.info("orginalIdv = "+orginalIdv);
				LOGGER.info("modifiedIdv = " + modifiedIdv);
				if (!(minLimitForDeviation < modifiedIdv) || !(maxLimitForDeviation > modifiedIdv)) {
					LOGGER.info("IDV going beyond the min limit ");
					errorCode.add("E-0406");
				} else {

					if (modifiedIdv > (actualModifiedIdv + 5) || modifiedIdv < (actualModifiedIdv - 5)) {
						errorCode.add("E-0359");
					}

					if (motorInsuranceModel.getPolicyTerm() >= 2) {
						LOGGER.info("motorInsuranceModel.getIdvFor2Year() = " + motorInsuranceModel.getIdvFor2Year());
						LOGGER.info("motorInsuranceModel.getOridinalIdvFor2ndYear() = "
								+ motorInsuranceModel.getOriginalIdvFor2Year());
						minLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor2Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor2Year())
												* minIdvDiscount / 100))
								- 5;
						maxLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor2Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor2Year())
												* maxIdvDiscount / 100))
								+ 5;
						LOGGER.info("\nminLimitForDeviation 2nd yr = " + minLimitForDeviation
								+ "\nmaxLimitForDeviation = " + maxLimitForDeviation);
						if (!(minLimitForDeviation < motorInsuranceModel.getIdvFor2Year())
								|| !(maxLimitForDeviation > motorInsuranceModel.getIdvFor2Year())) {
							LOGGER.info("IDV going beyond the min limit  ");
							errorCode.add("E-0407");
						} else {
							orginalIdv = motorInsuranceModel.getIdvFor2Year();
							modifiedIdv = (long) motorInsuranceModel.getModifiedIdvfor2Year();
							LOGGER.info("Modified Idv:: " + modifiedIdv);
							actualModifiedIdv = Math.round(orginalIdv + (orginalIdv * discountIdvPercent / 100));
							LOGGER.info("actual idv:: " + actualModifiedIdv);
							if (!(minLimitForDeviation < modifiedIdv) || !(maxLimitForDeviation > modifiedIdv)) {
								LOGGER.info("IDV going beyond the min limit 2 ");
								errorCode.add("E-0407");
							} else {
								if (modifiedIdv > (actualModifiedIdv + 5) || modifiedIdv < (actualModifiedIdv - 5)) {
									errorCode.add("E-0374");
								}
							}
						}

					}

					if (motorInsuranceModel.getPolicyTerm() >= 3) {
						LOGGER.info("motorInsuranceModel.getIdvFor3Year() = " + motorInsuranceModel.getIdvFor3Year());
						LOGGER.info("motorInsuranceModel.getOridinalIdvFor3rdYear() = "
								+ motorInsuranceModel.getOriginalIdvFor3Year());
						minLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor3Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor3Year())
												* minIdvDiscount / 100))
								- 5;
						maxLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor3Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor3Year())
												* maxIdvDiscount / 100))
								+ 5;
						LOGGER.info("\nminLimitForDeviation 3rd yr = " + minLimitForDeviation
								+ "\nmaxLimitForDeviation = " + maxLimitForDeviation);
						if (!(minLimitForDeviation < motorInsuranceModel.getIdvFor3Year())
								|| !(maxLimitForDeviation > motorInsuranceModel.getIdvFor3Year())) {
							LOGGER.info("IDV going beyond the min limit ");
							errorCode.add("E-0408");
						} else {

							orginalIdv = motorInsuranceModel.getIdvFor3Year();
							LOGGER.info("Modified Idv:: " + modifiedIdv);
							modifiedIdv = (long) motorInsuranceModel.getModifiedIdvfor3Year();
							actualModifiedIdv = Math.round(orginalIdv + (orginalIdv * discountIdvPercent / 100));
							if (!(minLimitForDeviation < modifiedIdv) || !(maxLimitForDeviation > modifiedIdv)) {
								LOGGER.info("IDV going beyond the min limit 3");
								errorCode.add("E-0408");
							} else {

								if (modifiedIdv > (actualModifiedIdv + 5) || modifiedIdv < (actualModifiedIdv - 5)) {
									errorCode.add("E-0375");
								}
							}
						}
					}
					if (motorInsuranceModel.getPolicyTerm() >= 4) {
						LOGGER.info("motorInsuranceModel.getIdvFor4Year() = " + motorInsuranceModel.getIdvFor4Year());
						LOGGER.info("motorInsuranceModel.getOridinalIdvFor4thYear() = "
								+ motorInsuranceModel.getOriginalIdvFor4Year());
						minLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor4Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor4Year())
												* minIdvDiscount / 100))
								- 5;
						maxLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor4Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor4Year())
												* maxIdvDiscount / 100))
								+ 5;
						LOGGER.info("\nminLimitForDeviation 4th yr = " + minLimitForDeviation
								+ "\nmaxLimitForDeviation = " + maxLimitForDeviation);
						if (!(minLimitForDeviation < motorInsuranceModel.getIdvFor4Year())
								|| !(maxLimitForDeviation > motorInsuranceModel.getIdvFor4Year())) {
							LOGGER.info("IDV going beyond the min limit ");
							errorCode.add("E-0454");
						} else {

							orginalIdv = motorInsuranceModel.getIdvFor4Year();
							LOGGER.info("Modified Idv:: " + modifiedIdv);
							modifiedIdv = (long) motorInsuranceModel.getModifiedIdvfor4Year();
							actualModifiedIdv = Math.round(orginalIdv + (orginalIdv * discountIdvPercent / 100));
							if (!(minLimitForDeviation < modifiedIdv) || !(maxLimitForDeviation > modifiedIdv)) {
								LOGGER.info("IDV going beyond the min limit 4");
								errorCode.add("E-0454");
							} else {

								if (modifiedIdv > (actualModifiedIdv + 5) || modifiedIdv < (actualModifiedIdv - 5)) {
									errorCode.add("E-0456");
								}
							}
						}
					}
					if (motorInsuranceModel.getPolicyTerm() >= 5) {
						LOGGER.info("motorInsuranceModel.getIdvFor5Year() = " + motorInsuranceModel.getIdvFor5Year());
						LOGGER.info("motorInsuranceModel.getOridinalIdvFor5thYear() = "
								+ motorInsuranceModel.getOriginalIdvFor4Year());
						minLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor5Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor5Year())
												* minIdvDiscount / 100))
								- 5;
						maxLimitForDeviation = Math
								.round(Double.parseDouble(motorInsuranceModel.getOriginalIdvFor5Year())
										+ (Double.parseDouble(motorInsuranceModel.getOriginalIdvFor5Year())
												* maxIdvDiscount / 100))
								+ 5;
						LOGGER.info("\nminLimitForDeviation 5th yr = " + minLimitForDeviation
								+ "\nmaxLimitForDeviation = " + maxLimitForDeviation);
						if (!(minLimitForDeviation < motorInsuranceModel.getIdvFor5Year())
								|| !(maxLimitForDeviation > motorInsuranceModel.getIdvFor5Year())) {
							LOGGER.info("IDV going beyond the min limit ");
							errorCode.add("E-0455");
						} else {

							orginalIdv = motorInsuranceModel.getIdvFor5Year();
							LOGGER.info("Modified Idv:: " + modifiedIdv);
							modifiedIdv = (long) motorInsuranceModel.getModifiedIdvfor5Year();
							actualModifiedIdv = Math.round(orginalIdv + (orginalIdv * discountIdvPercent / 100));
							if (!(minLimitForDeviation < modifiedIdv) || !(maxLimitForDeviation > modifiedIdv)) {
								LOGGER.info("IDV going beyond the min limit 4");
								errorCode.add("E-0455");
							} else {

								if (modifiedIdv > (actualModifiedIdv + 5) || modifiedIdv < (actualModifiedIdv - 5)) {
									errorCode.add("E-0457");
								}
							}
						}
					}
				}

			}
		}
		}
	   }
		return errorCode;
	}

	/**
	 * TP ONLY TWO WHEELER Third party policy option based on vehicle type
	 * (Bike, Scooter).
	 * 
	 * @param motorInsuranceModel
	 * @param errorCode
	 * @since 2019-09-23
	 */
	public void allowTPPolicy(MotorInsuranceModel motorInsuranceModel, List<String> errorCode) {
		LOGGER.info("Type Of cover : " + motorInsuranceModel.getTypeOfCover());
		LOGGER.info("Product Name = " + motorInsuranceModel.getProductName());
		LOGGER.info("City = " + motorInsuranceModel.getVehicleRegisteredCity());
		LOGGER.info("Region = " + motorInsuranceModel.getRegion());
		LOGGER.info("make = " + motorInsuranceModel.getVehicleManufacturerName());
		LOGGER.info("model = " + motorInsuranceModel.getVehicleModelCode());

		if (StringUtils.containsIgnoreCase(motorInsuranceModel.getTypeOfCover(), LIABILITY_ONLY)) {
			String bikeOrScooter = twoWheelerDAO.getVehicleClassification(motorInsuranceModel.getVehicleModelCode(),
					VEHICLE_TYPE);
			if (StringUtils.isNotBlank(bikeOrScooter) && StringUtils.equalsIgnoreCase(SCOOTER, bikeOrScooter)) {
				boolean agentAvail = twoWheelerDAO.getAgentForTPPolicy(motorInsuranceModel.getAgentId(), bikeOrScooter);
				if (agentAvail) {
					boolean allowTPPolicyByCity = twoWheelerDAO.allowTPPolicyByCityOrRegion(
							motorInsuranceModel.getAgentId(), motorInsuranceModel.getProductName(), "NB", bikeOrScooter,
							motorInsuranceModel.getVehicleRegisteredCity(), true);
					if (!allowTPPolicyByCity) {
						errorCode.add("E-0464");
					}
				}

			} else if (StringUtils.isNotBlank(bikeOrScooter) && StringUtils.equalsIgnoreCase(BIKE, bikeOrScooter)) {
				boolean agentAvail = twoWheelerDAO.getAgentForTPPolicy(motorInsuranceModel.getAgentId(), bikeOrScooter);
				if (agentAvail) {
					boolean allowTPPolicyByRegion = twoWheelerDAO.allowTPPolicyByCityOrRegion(
							motorInsuranceModel.getAgentId(), motorInsuranceModel.getProductName(), "NB", bikeOrScooter,
							motorInsuranceModel.getRegion(), false);
					if (!allowTPPolicyByRegion) {
						errorCode.add("E-0464");
					}
				}
			} 
		}
	}

}
