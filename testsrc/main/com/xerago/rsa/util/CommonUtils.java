package com.xerago.rsa.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtils {
	private static final Log LOGGER = LogFactory.getLog(CommonUtils.class);
	// Product
	public static final String PRODUCT_NAME = "VPC_Comprehensive";
	public static final String PRODUCT_NAME_PM = "PassengerCarryingComprehensive";
	public static final String PRODUCT_NAME_VGC = "CommercialGoodsComprehensive";
	// Transaction Type
	public static final String NB_TRANSACTION_TYPE = "NB";
	public static final String RN_TRANSACTION_TYPE = "RN";
	public static final String END_TRANSACTION_TYPE = "EN";

	// Rate Plan Type
	public static final String TARIFF = "Tariff";

	// Customer Type
	public static final String INDIVIDUAL = "IndividualCustomer";
	public static final String CORPORATE = "CorporateCustomer";

	// Motor Sublines
	public static final String INSURABLE_COMMERCIAL_VEHICLE = "commercialVehicle";
	public static final String INSURABLE_PRIVATE_PASSENGER_CAR = "privatePassengerCar";
	public static final String INSURABLE_PRIVATE_PASSENGER_PM = "commercialVehicle";
	public static final String INSURABLE_MOTOR_CYCLE = "motorCycle";

	// Add on Coverage Constants
	public static final String COVERAGE_ROADTAX = "RoadTax";
	public static final String COVERAGE_DEPRICIATIONWAIVER = "DepreciationWaiver";
	public static final String COVERAGE_ENGINEPROTECTOR = "AggravationCover";
	public static final String COVERAGE_WINDSHIELDGLASS = "WindShieldGlass";
	public static final String COVERAGE_ENGINEPROTECTOR_Deductibles = "AggravationCover_Without Deductibles";
	public static final String COVERAGE_KeyReplacement_WithoutDeductibles = "KeyReplacementCover_Without Deductibles";
	// NCBProtectorCover
	public static final String COVERAGE_NCBPROTECTOR = "NCBProtectorCover_Plan 1";

	public static final String COVERAGE_SPARECAR = "SpareCar";
	public static final String COVERAGE_INVOICEPRICE = "InvoicePrice";
	public static final String COVERAGE_LOSSOFBAGGAGE = "LossOfBaggage";

	public static final float DEFAULTVALUE_ROADTAX = 0.0f;
	public static final float DEFAULTVALUE_DEPRICIATIONWAIVER = 0.0f;
	public static final float DEFAULTVALUE_ENGINEPROTECTOR = 0.0f;
	public static final float DEFAULTVALUE_WINDSHIELDGLASS = 0.0f;
	public static final float DEFAULTVALUE_SPARECAR = 0.0f;
	public static final float DEFAULTVALUE_INVOICEPRICE = 0.0f;
	public static final float DEFAULTVALUE_LOSSOFBAGGAGE = 0.0f;
	// NCBProtectorCover
	public static final float DEFAULTVALUE_NCBPROTECTOR = 0.0f;

	// KeyReplacementCover
	public static final float DEFAULTVALUE_KEYREPLACEMENT = 0.0f;

	public static Map<String, String> productQuotePrefix;
	public static Map<String, String> productPolicyPrefix;
	public static Map<String, String> claimsFreeYearsMap;
	public static Map<String, String> productQuoteforPublicMotorPrefix;
	public static Map<String, String> productpolicyforPublicMotorPrefix;
	public static Map<String, String> productQuoteforVGCPrefix;
	public static Map<String, String> productpolicyforVGCPrefix;

	static {
		productQuotePrefix = new HashMap<String, String>();
		productPolicyPrefix = new HashMap<String, String>();
		productQuoteforPublicMotorPrefix = new HashMap<String, String>();
		productpolicyforPublicMotorPrefix = new HashMap<String, String>();
		productQuoteforVGCPrefix = new HashMap<String, String>();
		productpolicyforVGCPrefix = new HashMap<String, String>();

		claimsFreeYearsMap = new HashMap<String, String>();
		productQuotePrefix.put("VPC_Comprehensive", "QVPN");
		productPolicyPrefix.put("VPC_Comprehensive", "VPN");
		productQuotePrefix.put("VPC_Comprehensive_policyBazaar", "QVPB");
		productPolicyPrefix.put("VPC_Comprehensive_policyBazaar", "VPB");
		productQuoteforPublicMotorPrefix.put("VPCV_Comprehensive", "QVPCV");
		/*
		 * start code modifiacation 16072014 for Policy number code change issue
		 */
		// productpolicyforPublicMotorPrefix.put("VPCV_Comprehensive", "VPV");
		productpolicyforPublicMotorPrefix.put("VNC_Comprehensive", "VNC");
		productpolicyforPublicMotorPrefix.put("VNT_Comprehensive", "VNT");
		productQuoteforVGCPrefix.put("VGC_Comprehensive", "QVGC");
		// productpolicyforVGCPrefix.put("VGC_Comprehensive", "VGC");
		productpolicyforVGCPrefix.put("VGC_Comprehensive", "VGN");
		/*
		 * end code modifiacation 16072014 for Policy number code change issue
		 */

		claimsFreeYearsMap.put("0", "1");
		claimsFreeYearsMap.put("20", "2");
		claimsFreeYearsMap.put("25", "3");
		claimsFreeYearsMap.put("35", "4");
		claimsFreeYearsMap.put("45", "5");
		claimsFreeYearsMap.put("50", "6");
		claimsFreeYearsMap.put("65", "7");
	}

	public static Calendar getXMLCalObj(String input) {
		LOGGER.info("getXMLCalObj::: " + input);
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.S'Z'");
			Date inputDate = (Date) sdf.parse(getXMLDate(input));
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			return cal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	public static String getXMLDate(String input) {
		LOGGER.info("getXMLDate::: " + input);
		Calendar cal = Calendar.getInstance();
		String xmldate = "";
		String year = "";
		String month = "";
		String day = "";
		String yearOne = "";
		String yearTemp = "";
		String hours = "";
		String minutes = "";
		String seconds = "";
		if (input != null && !"null".equalsIgnoreCase(input)) {
			if (StringUtils.contains(input, ' ')) {
				StringTokenizer str = new StringTokenizer(input, "/");
				// int totalTokens = str.countTokens();
				day = str.nextToken();
				month = str.nextToken();
				year = str.nextToken();
				yearOne = year.substring(0, year.indexOf(' '));
				yearTemp = year.substring(year.indexOf(' '));
				StringTokenizer strOne = new StringTokenizer(yearTemp, ":");
				hours = strOne.nextToken();
				minutes = strOne.nextToken();
				seconds = strOne.nextToken();
				xmldate = yearOne.trim() + "-" + month + "-" + day + "T" + hours.trim() + ":" + minutes.trim() + ":"
						+ seconds.trim() + ".0Z";
			} else {

				StringTokenizer str = new StringTokenizer(input, "/");
				// int totalTokens = str.countTokens();
				day = str.nextToken();
				month = str.nextToken();
				year = str.nextToken();
				xmldate = year + "-" + month + "-" + day + "T" + cal.get(Calendar.HOUR_OF_DAY) + ":"
						+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + ".0Z";
			}

		}
		LOGGER.info("output -- " + xmldate);
		return xmldate;
	}

	
}