package com.xerago.rsa.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.valuemomentum.plm.parser.Attributes;
import com.valuemomentum.plm.parser.Coverage;
import com.valuemomentum.plm.parser.Deductibles;
import com.valuemomentum.plm.parser.Limits;
import com.valuemomentum.plm.parser.PricingElements;
import com.valuemomentum.plm.parser.Product;
import com.xerago.rsa.model.MotorInsuranceModel;

public class MotorUtils implements Constants {
	private static final Log LOGGER = LogFactory.getLog(MotorUtils.class);

	private static void getCoverageDetails(Coverage cov, Map<String, String> coveragesMap) {
		if (cov != null) {
			String covName = cov.getCoverageName();
			coveragesMap.put(covName, String.valueOf(cov.getPremium()));
			/*
			 * LOGGER.info("cov.getPremium() ::" + cov.getPremium());
			 * LOGGER.info("String.valueOf(cov.getPremium()) ::" +
			 * String.valueOf(cov.getPremium()));
			 */
			List<Limits> limitList = cov.getLimits();
			if (limitList != null && !limitList.isEmpty()) {
				Limits limit = limitList.get(0);
				if (limit.getLimitName() != null) {
					coveragesMap.put(covName + "_limit_" + limit.getLimitName(),
							String.valueOf(limit.getSelectedLimitValue()));
				}
			}
			List<Deductibles> dedList = cov.getDeductibles();
			if (dedList != null && !dedList.isEmpty()) {
				Deductibles ded = dedList.get(0);
				if (ded.getDeductibleName() != null) {
					coveragesMap.put(covName + "_deductible_" + ded.getDeductibleName(),
							"" + ded.getSelectedDeductibleValue());
				}
			}
			List<Attributes> attList = cov.getAttributes();
			if (attList != null && !attList.isEmpty()) {
				for (int i = 0; i < attList.size(); i++) {
					Attributes att = attList.get(i);
					if (att.getAttributeName() != null) {
						// LOGGER.info("Attributes Name ----
						// "+att.getAttributeName());
						coveragesMap.put(covName + "_attribute_" + att.getAttributeName(),
								att.getSelecetdAttributeValue());
					}
				}
			}
			List<PricingElements> priceList = cov.getPricingElements();
			if (priceList != null && !priceList.isEmpty()) {
				for (int i = 0; i < priceList.size(); i++) {
					PricingElements price = priceList.get(i);
					if (price.getPricingElementName() != null) {
						// LOGGER.info("PricingElementName ::
						// "+price.getPricingElementName());
						coveragesMap.put(covName + "_pricingelement_" + price.getPricingElementName(),
								"" + price.getPricingElementValue());
					}
				}
			}
		}
	}

	public static Map<String, String> getCoveragesMap(Product prod) {
		Map<String, String> coveragesMap = new HashMap<String, String>();
		if (prod != null) {
			List<Coverage> coveragesList = prod.getCoverages();
			if (coveragesList != null && !coveragesList.isEmpty()) {
				for (int i = 0; i < coveragesList.size(); i++) {
					Coverage cov = coveragesList.get(i);
					getCoverageDetails(cov, coveragesMap);
					List<Coverage> subCoverages = cov.getSubCoverages();
					if (subCoverages != null && !subCoverages.isEmpty()) {
						for (int j = 0; j < subCoverages.size(); j++) {
							Coverage subCov = subCoverages.get(j);
							getCoverageDetails(subCov, coveragesMap);
						}
					}
				}
			}
		}
		return coveragesMap;
	}

	public static long getValue(Map<String, String> coverageMap, String key) {
		String value = coverageMap.get(key);
		if (value == null || value.trim().length() < 1) {
			return Math.round((double) 0);
		}
		return Double.valueOf(value).longValue();
	}

	public static double getValue1(Map<String, String> coverageMap, String key) {
		String value = coverageMap.get(key);
		if (value == null || value.trim().length() < 1) {
			return Math.round((double) 0);
		}
		return new BigDecimal(Double.valueOf(value), MathContext.DECIMAL64)
				.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		//return Math.round(Double.valueOf(value));
	}

	public static double getValue2(Map<String, String> coverageMap, String key) {
		String value = coverageMap.get(key);
		if (value == null || value.trim().length() < 1) {
			return 0;
		}
		return Double.valueOf(value);
	}
	
	Pattern emailValidation = Pattern.compile(MotorValidation.EMAIL_VALIDATION);
	
	Pattern alphaNumericWithoutSpaceValidation = Pattern.compile(MotorValidation.ALPHA_NUEMERIC_WITHOUT_SPACE);
	Pattern atleastonealphaoneNumericWithoutSpaceValidation = Pattern
			.compile(MotorValidation.ATLEAT_ONE_ALPHA_ONE_NUEMERIC);
	// Pattern loginPwd =
	// Pattern.compile(ConstantVariables.ATLEAT_ONE_ALPHA_ONE_NUEMERIC_SOME_SPECIAL);
	Pattern stringValidation = Pattern.compile(MotorValidation.STRING_VALIDATION);
	Pattern numericValidation = Pattern.compile(MotorValidation.NUEMERIC_VALIDATION);

	Pattern addressValidation = Pattern.compile(MotorValidation.ADDRESS_VALIDATION);

	Pattern dateValidation = Pattern.compile(MotorValidation.dateValidation);

	Pattern stringWithoutSpaceValidation = Pattern.compile(MotorValidation.STR_VAL_WITHOUT_SPACE);

	Pattern alphaNumericWithSpaceValidation = Pattern.compile(MotorValidation.ALPHA_NUEMERIC_WITH_COMMA);

	public String formatInceptionDate(String input) {
		if (input != null && !NULL.equalsIgnoreCase(input)) {
			SimpleDateFormat gridformat = null;
			String outgoingDate = null;
			gridformat = new SimpleDateFormat(DATE_FORMAT);
			outgoingDate = gridformat.format(input);
			return outgoingDate;
		}
		return null;
	}


	public String indianFormat(Double n) {
		Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		String returnValue = format.format(new BigDecimal(n + ""));
		returnValue = returnValue.replaceAll("Rs. ", "");
		returnValue = returnValue.replace(".00", "");
		return returnValue + " ";
	}


	public boolean validateMotorInsuranceGProposal(MotorInsuranceModel motorInsuranceModel) {

		if (StringUtils.isBlank(motorInsuranceModel.getQuoteId())) {
			motorInsuranceModel.setStatusFlag("E-0301");
			LOGGER.info("Quote Id is mandatory");
			return false;
		}

		if (motorInsuranceModel.getPremium() <= 0) {
			motorInsuranceModel.setStatusFlag("E-0302");
			LOGGER.info("Premium is mandatory");
			return false;
		}

		if (StringUtils.isBlank(motorInsuranceModel.getEmailId())
				|| !emailValidation.matcher(GenericUtils.checkString(motorInsuranceModel.getEmailId())).find()) {
			motorInsuranceModel.setStatusFlag("E-0303");
			LOGGER.info("Email id is mandatory and should be in regular format");
			return false;
		}

		return true;

	}

	public static List<String> getCoverageVMC_Details() {
		List<String> labels = new ArrayList<String>();
		labels.add("VMC_ODCommercialCover");
		labels.add("VMC_ODCommercialCover_pricingelement_AutoAssociationMembership");
		labels.add("VMC_ODCommercialCover_pricingelement_Commission_rate");
		labels.add("VMC_ODCommercialCover_pricingelement_SideCarDiscount_rate");
		labels.add("VMC_ODBasicCover_pricingelement_BasePremium");
		labels.add("VMC_ODCommercialCover_pricingelement_VoluntaryDed");
		labels.add("VMC_ODBasicCover_pricingelement_Commission_rate");
		labels.add("VMC_CommercialLiabilityCover");
		labels.add("VMC_ODCommercialCover_pricingelement_SideCarDiscount");
		labels.add("VMC_ODCommercialCover_pricingelement_NoCliamDiscount");
		labels.add("VMC_ODBasicCover_limit_ODLimit");
		labels.add("VMC_ODCommercialCover_pricingelement_AntiTheftDiscount");
		labels.add("VMC_LiabilityCover_limit_LiabilityLimit");
		labels.add("VMC_ODCommercialCover_pricingelement_ImportedVehicleLoading");
		labels.add("VMC_PAOwnerDriverCover");
		labels.add("VMC_ODBasicCover_deductible_VMC_VolDeductible");
		labels.add("VMC_LiabilityCover_pricingelement_BasePremium");
		labels.add("VMC_ODBasicCover_pricingelement_Commission");
		labels.add("EducationCess_pricingelement_Ecess");
		labels.add("EducationCess_pricingelement_AdditionalEductaionalCess");
		labels.add("VMC_ODBasicCover");
		labels.add("VMC_LiabilityCover_pricingelement_TPPDDiscount");
		labels.add("VMC_ODCommercialCover_pricingelement_Commission");
		labels.add("VMC_ODBasicCover_pricingelement_TechnicalDiscount");
		labels.add("VMC_ODCommercialCover_pricingelement_DiscountforHandicapped");
		labels.add("VMC_LiabilityCover");
		labels.add("EducationCess");
		labels.add("ServiceeTax");

		// Task #146177 Two wheeler long term product - TECH

		labels.add("VMC_CommercialLiabilityCover_pricingelement_PremiumFor1stYear");
		labels.add("VMC_CommercialLiabilityCover_pricingelement_PremiumFor2ndYear");
		labels.add("VMC_CommercialLiabilityCover_pricingelement_PremiumFor3rdYear");

		labels.add("VMC_LiabilityCover_pricingelement_PremiumFor1stYear");
		labels.add("VMC_LiabilityCover_pricingelement_PremiumFor2ndYear");
		labels.add("VMC_LiabilityCover_pricingelement_PremiumFor3rdYear");

		labels.add("VMC_ODBasicCover_pricingelement_PremiumFor1stYear");
		labels.add("VMC_ODBasicCover_pricingelement_PremiumFor2ndYear");
		labels.add("VMC_ODBasicCover_pricingelement_PremiumFor3rdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_AntiTheftDiscountForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_AntiTheftDiscountForThirdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_AutoAssociationMembershipForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_AutoAssociationMembershipForThirdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_DiscountforHandicappedForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_DiscountforHandicappedForThirdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_ImportedVehicleLoadingForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_ImportedVehicleLoadingForThirdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_NoCliamDiscountForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_NoCliamDiscountForThirdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_PremiumFor1stYear");
		labels.add("VMC_ODCommercialCover_pricingelement_PremiumFor2ndYear");
		labels.add("VMC_ODCommercialCover_pricingelement_PremiumFor3ndYear");

		labels.add("VMC_ODCommercialCover_pricingelement_SideCarDiscountForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_SideCarDiscountForThirdYear");

		labels.add("VMC_ODCommercialCover_pricingelement_VoluntaryDeductibleForSecondYear");
		labels.add("VMC_ODCommercialCover_pricingelement_VoluntaryDeductibleForThirdYear");

		labels.add("NilIntermediationRateCover");
		labels.add("NilIntermediationRateCover_pricingelement_PremiumFor1stYear");
		labels.add("NilIntermediationRateCover_pricingelement_PremiumFor2ndYear");
		labels.add("NilIntermediationRateCover_pricingelement_PremiumFor3rdYear");

		labels.add("BulkDealDiscountCover");
		labels.add("BulkDealDiscountCover_pricingelement_PremiumFor1stYear");
		labels.add("BulkDealDiscountCover_pricingelement_PremiumFor2ndYear");
		labels.add("BulkDealDiscountCover_pricingelement_PremiumFor3rdYear");

		labels.add("AdditionalTowingChargesCover");
		labels.add("AdditionalTowingChargesCover_pricingelement_PremiumFor1stYear");
		labels.add("AdditionalTowingChargesCover_pricingelement_PremiumFor2ndYear");
		labels.add("AdditionalTowingChargesCover_pricingelement_PremiumFor3rdYear");
		labels.add("AdditionalTowingChargesCover_pricingelement_BasePremium");
		labels.add("AdditionalTowingChargesCover_pricingelement_Commission");
		return labels;
	}

	public static String getClaimFreeYears(String ncbExpiringpolicy) {
		HashMap<String, String> mapNCB = new HashMap<String, String>();
		mapNCB.put("0", "1");
		mapNCB.put("20", "2");
		mapNCB.put("25", "3");
		mapNCB.put("35", "4");
		mapNCB.put("45", "5");
		mapNCB.put("50", "6");
		return StringUtils.isNotBlank(mapNCB.get(ncbExpiringpolicy)) ? mapNCB.get(ncbExpiringpolicy) : "0";
	}
	
	public static String getncbExpiringpolicy(String claimFreeYears) {
		HashMap<String, String> mapNCB = new HashMap<String, String>();
		mapNCB.put("1", "0");
		mapNCB.put("2", "20");
		mapNCB.put("3", "25");
		mapNCB.put("4", "35");
		mapNCB.put("5", "45");
		mapNCB.put("6", "50");
		return StringUtils.isNotBlank(mapNCB.get(claimFreeYears)) ? mapNCB.get(claimFreeYears) : "0";
	}

	public static String getNCBCurrentPolicy(String ncbExpiringpolicy) {
		HashMap<String, String> mapNCB = new HashMap<String, String>();
		mapNCB.put("0", "20");
		mapNCB.put("20", "25");
		mapNCB.put("25", "35");
		mapNCB.put("35", "45");
		mapNCB.put("45", "50");
		mapNCB.put("50", "50");
		return StringUtils.isNotBlank(mapNCB.get(ncbExpiringpolicy)) ? mapNCB.get(ncbExpiringpolicy) : "0";
	}

	/**
	 * Trimming prefix and suffix of key and values 
	 * @param requestString
	 */
	public static HashMap<String, String> getTrimRequestString(Map<String, String> requestString) {
		HashMap<String, String> requet = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : requestString.entrySet()) {
			requet.put(entry.getKey().trim(), entry.getValue().trim());
		}
		return requet;
	}
	
	public static double tryParseDouble(String stringToBeParsed) {
		try {
			if (stringToBeParsed == null || "".equalsIgnoreCase(stringToBeParsed)) {
				return 0;
			}
			return Double.parseDouble(stringToBeParsed);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return 0;
		}
	}
	
	public static Date getOdExpiryDate(Date policyStartDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(policyStartDate);
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date expDate = cal.getTime();
		return expDate;
	}

	public static boolean checkExpiryDateCpaPolicyCover(String expiryDate){
		boolean validExpiryDate = false;
		
		SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
		Date currentDate;
		Date dtPolicyExpiry;
		LOGGER.info("expiryDate = "+expiryDate);
		if (!DATE_DEFAULT.equalsIgnoreCase(expiryDate)
				&& StringUtils.isNotBlank(expiryDate)) {
			try {
				dtPolicyExpiry = objSDF.parse(expiryDate.replace("\n", ""));
				LOGGER.info("dtPolicyExpiry = "+dtPolicyExpiry);
				currentDate = objSDF.parse(objSDF.format(new Date()));
				LOGGER.info("currentDate = "+currentDate);
				if(dtPolicyExpiry.before(currentDate) ){
					validExpiryDate = false;	
				} else
					validExpiryDate = true;	
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				validExpiryDate = false;
				e.printStackTrace();
			}
		}


		LOGGER.info("validExpiryDate = "+validExpiryDate);	
		return validExpiryDate;
	}

	public static List<String> getLiabilityCovers() {
		List<String> liabilityCovers = new ArrayList<String>();
		liabilityCovers.add("VMC_PAUnnamed");
		liabilityCovers.add("VMC_PAPaidDriver");	
		liabilityCovers.add("VMC_LiabilityCover");
		liabilityCovers.add("VMC_CommercialLiabilityCover");
		liabilityCovers.add("VMC_LLPaidDriverCover");
		liabilityCovers.add("VMC_PAOwnerDriverCover");
		liabilityCovers.add("VMC_LLEMPLOYEES");
		
		return liabilityCovers;
	}
	/**
	 * 
	 * @param requestMap
	 * @return
	 * @author madhan
	 */
	public static String requestFormPostToString(Map<String,String> requestMap) {
		String requestUrlString="";
		StringBuilder queryString= new StringBuilder();
		
		for(Map.Entry<String,String> mapVal : requestMap.entrySet())
    	   queryString.append(mapVal.getKey() + "=" + mapVal.getValue() + "&");
    	
    	requestUrlString = queryString.toString().endsWith("&")? queryString.toString().substring(0,queryString.toString().length()-1) : queryString.toString();
    	LOGGER.info("request Param String ::: " + requestUrlString);
    	
    	return requestUrlString;
	}
	
	public static double getRoundedValue(Map<String, String> coverageMap, String key) {
		String value = coverageMap.get(key);
		if (value == null || value.trim().length() < 1) {
			return 0;
		}
		return new BigDecimal(Double.valueOf(value), MathContext.DECIMAL64)
				.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
}
