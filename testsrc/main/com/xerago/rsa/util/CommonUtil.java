package com.xerago.rsa.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.xerago.rsa.Regex;

public class CommonUtil implements Constants {

	private static final Logger LOGGER = LogManager.getRootLogger();

	Pattern stringValidation = Pattern.compile(Regex.STRING_VALIDATION);
	Pattern numericValidation = Pattern.compile(Regex.NUEMERIC_VALIDATION);
	Pattern addressValidation = Pattern.compile(Regex.ADDRESS);
	Pattern dateValidation = Pattern.compile(Regex.DATE_VALIDATION);
	Pattern stringWithoutSpaceValidation = Pattern.compile(Regex.STR_VAL_WITHOUT_SPACE);
	Pattern alphaNumericWithSpaceValidation = Pattern.compile(Regex.ALPHA_NUEMERIC_WITH_COMMA);

	public static String calcExpiryDateForCarShield(SimpleDateFormat objSDF, String strCurrentDt)
			throws ParseException {
		Calendar objCal = Calendar.getInstance();
		Date date = objSDF.parse(strCurrentDt);
		objCal.setTime(date);
		objCal.add(Calendar.YEAR, 1);
		objCal.add(Calendar.DAY_OF_MONTH, -1);
		Date date_ex = objCal.getTime();
		return objSDF.format(date_ex);
	}

	public static String calcExpiryDateForTravel(String strCurrentDate, String strProductType, int insurancePeriod) {
		String strExpiryDt = "";
		try {
			SimpleDateFormat objSDF = new SimpleDateFormat(SIXTH_DATE_FORMAT);
			Calendar objCal = Calendar.getInstance();
			Date dtDate;
			dtDate = objSDF.parse(strCurrentDate);
			objCal.setTime(dtDate);
			objCal.add(Calendar.YEAR, insurancePeriod);
			objCal.add(Calendar.DAY_OF_MONTH, -1);
			Date date_ex = objCal.getTime();
			strExpiryDt = objSDF.format(date_ex);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return strExpiryDt;
	}

	public static String checkJunk(String procStr) {
		if (procStr.contains("+")) {
			procStr = procStr.replaceAll("\\+", " ");
		}
		if (procStr.contains("%40")) {
			procStr = procStr.replaceAll("%40", "@");
		}
		if (procStr.contains("%2F")) {
			procStr = procStr.replaceAll("%2F", "/");
		}
		if (procStr.contains("%2C")) {
			procStr = procStr.replaceAll("%2C", ",");
		}
		if (procStr.contains("%26")) {
			procStr = procStr.replaceAll("%26", "&");
		}
		if (procStr.contains("%24")) {
			procStr = procStr.replaceAll("%24", "$");
		}
		if (procStr.contains("%3A")) {
			procStr = procStr.replaceAll("%3A", ":");
		}
		if (procStr.contains("%7C")) {
			procStr = procStr.replaceAll("%7C", "|");
		}
		if (procStr.contains("%3D")) {
			procStr = procStr.replaceAll("%3D", "=");
		}

		return procStr;
	}

	public static String checkNull(String processStrings) {
		String processString = null;
		if (processStrings == null || "null".equalsIgnoreCase(processStrings)
				|| "".equalsIgnoreCase(processStrings.trim())) {
			processString = "";
		}
		return processString;
	}

	public static String checkString(String str) {
		if (StringUtils.isBlank(str) || "null".equalsIgnoreCase(str)) {
			return "";
		}
		return str;
	}

	public static String generateCreationDate(SimpleDateFormat objSDF) {
		Calendar objCal = Calendar.getInstance();
		Date date = objCal.getTime();
		return objSDF.format(date);
	}

	public static int getAge(int year, int month, int day) {
		LocalDate birthdate = new LocalDate(year, month, day);
		LocalDate now = new LocalDate();
		Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
		return period.getYears();
	}

	public static String getPolicyPrefix(String isDPPartner, String DBPartner, String strPlanName) {
		String strPolicyPrefix = "";
		if (Constants.PLAN_ACCIDENT_SHIELD.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = PY;
		} else if (Constants.PLAN_HOSPITAL_CASH.equalsIgnoreCase(strPlanName)) {
			if (TRUE.equalsIgnoreCase(isDPPartner) && SBI.equalsIgnoreCase(DBPartner)) {
				strPolicyPrefix = CSY;
			} else {
				strPolicyPrefix = CY;
			}
		} else if (Constants.PLAN_HOME_CONTENT.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = DY;
		} else if (Constants.PLAN_HOME_SHIELD.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = DY;
		} else if (Constants.PLAN_TRAVEL_SHIELD.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = TY;
		} else if (Constants.PLAN_HEALTH_SHIELD.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = HY;
		} else if (Constants.PLAN_FAMILY_HEALTH.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = IF;
		} else if (Constants.PLAN_FAMILY_GOOD_HEALTH_Idividual.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = FY;
		} else if (Constants.PLAN_FAMILY_GOOD_HEALTH_Floater.equalsIgnoreCase(strPlanName)) {
			if (DBPartner != null && "scb".equalsIgnoreCase(DBPartner)) {
				strPolicyPrefix = GE;
			} else {
				strPolicyPrefix = IY;
			}
		} else if (Constants.PLAN_TOTAL_HEALTH_PLUS.equalsIgnoreCase(strPlanName)) {
			strPolicyPrefix = MY;
		}
		return strPolicyPrefix;
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	public Date getExpiryDate() throws ParseException {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		calendar.add(Calendar.DATE, Constants.QUOTE_EXPIRY);
		Date expiryDate = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
		return expiryDate;
	}

	public Date getValidityDate() throws ParseException {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		calendar.add(Calendar.DATE, Constants.QUOTE_VALIDITY);
		Date validityDate = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
		return validityDate;
	}

	public String setFileName(String strProductType, boolean isRenewal) {
		String fileName = "";
		LOGGER.info("strProductType::: " + strProductType);
		if (Constants.PRODUCT_ACCIDENT_SHIELD.equalsIgnoreCase(strProductType)) {
			fileName = PDF_ACCIDENT_SHIELD;
		} else if (Constants.PRODUCT_HOSPITAL_CASH.equalsIgnoreCase(strProductType)) {
			fileName = PDF_HOSPITAL_CASH;
		} else if (Constants.PRODUCT_HOME_CONTENT.equalsIgnoreCase(strProductType)) {
			fileName = PDF_HOMECONTENT;
		} else if (Constants.PRODUCT_HOME_SHIELD.equalsIgnoreCase(strProductType)) {
			fileName = PDF_HOMESHIELD;
		} else if (Constants.PRODUCT_TRAVEL_SHIELD.equalsIgnoreCase(strProductType)) {
			fileName = PDF_TRAVELSHIELD;
		} else if (Constants.PRODUCT_HEALTH_SHIELD.equalsIgnoreCase(strProductType)) {
			fileName = PDF_HEALTHSHIELD;
		} else if (Constants.PRODUCT_FAMILY_HEALTH.equalsIgnoreCase(strProductType)) {
			fileName = PDF_FAMILYHEALTH;
		} else if (Constants.PRODUCT_FAMILY_GOOD_HEALTH.equalsIgnoreCase(strProductType)) {
			fileName = PDF_FAMILYGOODHEALTH;
		} else if (Constants.PRODUCT_TOTAL_HEALTH_PLUS.equalsIgnoreCase(strProductType)) {
			fileName = PDF_TOTALHEALTHPLUS;
		} else if (Constants.PRODUCT_MOTOR_SHIELD.equalsIgnoreCase(strProductType)) {
			fileName = PDF_CARINSURANCE;
		} else if (Constants.PRODUCT_PUBLICMOTOR_SHIELD.equalsIgnoreCase(strProductType)) {
			fileName = PDF_PUBLICMOTORINSURANCE;
		} else if (Constants.PRODUCT_TWOWHEELER.equalsIgnoreCase(strProductType)) {
			fileName = PDF_TWOWHEELER;
		} else if (Constants.PRODUCT_SMARTCASH.equalsIgnoreCase(strProductType)) {
			fileName = PDF_SMARTCASH;
		} else if (Constants.PRODUCT_TRAVELSECURE.equalsIgnoreCase(strProductType)) {
			fileName = PDF_TRAVELSECURE;
		}

		if (isRenewal) {
			fileName = PDF_RENEWALCONFIRMATION;
		}
		return fileName;
	}

	public static int getAge(String dataOfBirth) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		LocalDate birthdate = dtf.parseLocalDate(dataOfBirth);
		LocalDate now = new LocalDate();
		Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
		return period.getYears();
	}

	// Type casting helpers

	/**
	 * 
	 * 
	 * @param value
	 *            numeric value as String
	 * @return int value of the String value. And on invalid number (NAN)
	 *         returns '0'
	 */
	public static int tryParseInt(String value) {
		if (value != null && !value.equals("")) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				LOGGER.error(value + " - Invalid numeric value. Can not be parsed.");
			}
		}
		return 0;
	}

	/**
	 *
	 * 
	 * @param value
	 *            numeric value as String
	 * @return double value of the String value. And on invalid number (NAN)
	 *         returns '0'
	 */
	public static double tryParseDouble(String value) {
		if (value != null && !value.equals("")) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				LOGGER.error(value + " - Invalid numeric value. Can not be parsed.");
			}
		}
		return 0.0;
	}

}
