package com.xerago.rsa.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xerago.rsa.Regex;
import com.xerago.rsa.model.MotorInsuranceModel;

public class MotorValidation implements Constants {

	private static final Logger LOGGER = LogManager.getRootLogger();

	public static final String NUEMERIC_VALIDATION = "^[0-9]*$";
	/* Numeric validation */
	public static final String numericValidation = "^[0-9]*$";

	/* String validation with space */
	public static final String STRING_VALIDATION = "^[a-zA-Z ]*$";
	public static final String STRING_VALIDATION_1 = "^[a-zA-Z ][^0-9]*$";
	public static final String FINANCER_NAME_VALIDATION = "^[a-zA-Z, ]*$";
	public static final String stringValidation1 = "^[a-zA-Z ][^0-9]*$";
	/* Checking credit card length.Its length should be 16digit. */
	public static final String CREDIT_CARD_LEN = "^\\d{14,16}\\d*$";

	/* At least one numeric should be present with length compulsory 7. */
	public static final String ATLEAST_ONE_NUEMERIC = "(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,10})$";

	/* String validation with without space */
	public static final String STR_VAL_WITHOUT_SPACE = "^[a-zA-Z]*$";
	public static final String Mobile_No_Validation = "[0-9]{1}[0-9]{9}";

	/* Alphabets Numeric Space / - , are allowed in this */
	public static final String ALPHA_NEMERIC_CALIDATION = "^[0-9a-zA-Z-/, ]*$";

	/* Policy validation */
	public static final String POLICY_VALIDATION = "[a-zA-Z][a-zA-Z0-9]{15}$";

	/* Date validation */
	public static final String DATE_VALIDATION = "[0-9][0-9]/(0|1)[0-9]/(19|20)[0-9]{2}";

	/* At least one alphabet should be present with length compulsory 7. */
	// public static final String passportValidation =
	// "(?!^[0-9]*$)^([a-zA-Z0-9]{8})$";
	public static final String PASSPORT_VALIDATION = "[a-zA-Z][a-zA-Z0-9]{6,7}$";
	// public static final String passportValidation =
	// "^[a-zA-Z].*(?=.{2,8})(?=.*\\d)(?=.*[a-zA-Z]).*$";

	public static final String VEH_REG_NUM_VALIDATION = "^[a-zA-Z].*(?=.{3,})(?=.*\\d)(?=.*[a-zA-Z'']).*$";
	/* Alphabets Numeric WithoutSpace / - , are allowed in this */
	public static final String ALPHA_NUEMERIC_WITHOUT_SPACE = "^[0-9a-zA-Z]*$";

	/* Alphabets Numeric Space / - are allowed in this */
	public static final String ALPHA_NUEMERIC_WITH_COMMA = "^[0-9a-zA-Z,-/ ]*$";

	/* Email validation is done here */
	public static final String EMAIL_VALIDATION = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	/* Date validation */
	public static final String dateValidation = "[0-9][0-9]/(0|1)[0-9]/(19|20)[0-9]{2}";

	/* Email validation is done here */
	public static final String emailValidation = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	/*
	 * At lease one alphabet and numberic should be present with length
	 * compulsory 7.
	 */
	public static final String ATLEAT_ONE_ALPHA_ONE_NUEMERIC = "(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8})$";

	public static final String ADDRESS_VALIDATION = "^[0-9a-zA-Z-/,:.#() ]*$";
	
	public static final String address_VALIDATION =  "[a-zA-Z0-9.,/()]*";
	
	public static final String STRING_WITH_SPACE_VALIDATION ="^[a-zA-Z\\s]*$";
	public static final String ALPHA_NUEMERIC_WITH_SPACE = "^[0-9a-zA-Z\\s]*$";
	/** Citibank user name hard coded. */
	//public static final String CITIBANK_USERID = "transaction@citibank.com";

	/** Citibank password hard coded. */
	//public static final String CITIBANK_PASSWORD = "Password6";

	//public static final String CITIBANK_CLIENT_ID = "C12345";

	public static int calculateVehicleAge(String CarRegisteredCity, String vehicleSubLine, String yearOfManufacture,
			String vehicleManufacturerName, String region, String vehicleregDate, String PolicyStartDate) {
		LOGGER.info("calculateVehicleAge: 1");
		int inVehicleAgeInYears = 0;
		try {
			Pattern objPattern = Pattern.compile(Regex.DATE_VALIDATION);
			double vehicleAgeInDays = 0;
			if (objPattern.matcher(vehicleregDate).find() && objPattern.matcher(PolicyStartDate).find()) {
				vehicleAgeInDays = PortalDateConversion.diffDates(vehicleregDate, PolicyStartDate);

				vehicleAgeInDays++;
				LOGGER.info("calculateVehicleAge vehicleAgeInDays::::: " + vehicleAgeInDays);
				if (vehicleAgeInDays < 365) {
					inVehicleAgeInYears = vehicleAgeInDays < 182 ? -1 : 0;
				} else {
					inVehicleAgeInYears = (int) Math.ceil(vehicleAgeInDays / 365);
				}
			}
			LOGGER.info("calculateVehicleAge::::: inVehicleAgeInYears " + inVehicleAgeInYears);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("calculateVehicleAge: 2:::: " + inVehicleAgeInYears);
		return inVehicleAgeInYears;
	}

	public static String checkNull(String str) {
		if (StringUtils.isBlank(str) || "NULL".equalsIgnoreCase(str)) {
			str = "";
		}
		return str;
	}

	public static BigDecimal checkNullForBigdecimal(String str) {
		if (StringUtils.isBlank(str) || "".equalsIgnoreCase(str) || NULL.equalsIgnoreCase(str)) {
			return new BigDecimal("0");
		}
		return new BigDecimal(str);
	}

	public static Byte checkNullForByte(String str) {
		if (StringUtils.isBlank(str) || "".equalsIgnoreCase(str) || NULL.equalsIgnoreCase(str)) {
			return 0;
		}
		return Byte.valueOf(str);
	}

	public static String checkNullForNumb(String str) {
		if (StringUtils.isBlank(str)) {
			return "0";
		}
		return str;
	}

	public static long checkNullOrNot(Object str) {
		if (str != null) {
			return ((BigDecimal) str).longValue();

		} else {
			return 0;
		}

	}

	public static String checkString(String string) {
		if (StringUtils.isBlank(string) || NULL.equalsIgnoreCase(string)) {
			string = "";
		}
		return string;
	}

	public static long diffDates(String start, String end) {
		long lnDiffInDays = 0;
		String dtEnd = "";
		try {
			if (end != null) {
				dtEnd = StringUtils.trim(end);
			}
			if (StringUtils.isNotBlank(start) && !DATE_DEFAULT.equalsIgnoreCase(start)
					&& StringUtils.isNotBlank(end) && !DATE_DEFAULT.equalsIgnoreCase(dtEnd) ) {
				DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				Date d1 = dateFormat.parse(start);
				Date d2 = dateFormat.parse(dtEnd);

				Calendar calStart = Calendar.getInstance();
				calStart.setTime(d1);
				Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(d2);
				Calendar d = Calendar.getInstance();
				d.setTimeInMillis(calEnd.getTimeInMillis() - calStart.getTimeInMillis());
				lnDiffInDays = d.getTime().getTime() / 86400000;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return lnDiffInDays;
	}

	public static Date GetConvertionDate(String dateInString) {
		Date date = null;

		try {

			SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
			date = objSDF.parse(dateInString);

			LOGGER.info("GetConvertionDate String::: " + dateInString);
			LOGGER.info("GetConvertionDate ::: " + date);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return date;
	}

	public static String GetCurrentDate() {
		SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
		String strCurrentDate = objSDF.format(new Date());
		return strCurrentDate;

	}

	public static String validTill() {

		Calendar calendar = new GregorianCalendar();
		calendar.getTime();
		calendar.add(Calendar.DATE, 7);

		return new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime());

	}

	public static String GetCurrentDateInDDMMMYYY() {
		SimpleDateFormat objSDF = new SimpleDateFormat(FOURTH_DATE_FORMAT);
		String strCurrentDate = objSDF.format(new Date());
		return strCurrentDate;

	}

	// MM to MMM for month convertion dd/mm/yy
	public static String DateInDDMMMYYY(String strInceptionDt) {
		SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat objSDFMM = new SimpleDateFormat(FOURTH_DATE_FORMAT);
		String strCurrentDate = "";
		Date varDate = null;
		LOGGER.info("strInceptionDt  " + strInceptionDt);
		try {
			varDate = objSDF.parse(strInceptionDt);
			LOGGER.info("strInceptionDt2  " + varDate);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		strCurrentDate = objSDFMM.format(varDate);
		LOGGER.info("strCurrentDate  DateInDDMMMYYY" + strCurrentDate);
		return strCurrentDate;
	}

	public static String DateInDDMMMYYY1(String strInceptionDt) {
		SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat objSDFMM = new SimpleDateFormat("dd-MMM-yyyy");
		String strDate = "";
		Date varDate = null;
		LOGGER.info("strInceptionDt::: " + strInceptionDt);
		try {
			varDate = objSDF.parse(strInceptionDt);
			LOGGER.info("strInceptionDt2::: " + varDate);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		strDate = objSDFMM.format(varDate);
		LOGGER.info("strDate  DateInDD-MMM-YYY::: " + strDate);
		return strDate;

	}

	// MM to MMM for month convertion input yyyy/mm/dd
	public static String DateInYYMMMDD(String strInceptionDt) {
		SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat objSDFMM = new SimpleDateFormat("dd-MMM-yyyy");
		String strCurrentDate = "";
		Date varDate = null;
		LOGGER.info("DateInYYMMMDD strInceptionDt1  " + strInceptionDt);
		try {
			varDate = objSDF.parse(strInceptionDt);
			LOGGER.info("strInceptionDt2  " + varDate);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		strCurrentDate = objSDFMM.format(varDate);
		LOGGER.info("strInceptionDt3  " + strCurrentDate);

		return strCurrentDate;

	}

	public static String getCurrentTime() {
		SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// objSDF.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		Calendar objCal = Calendar.getInstance();
		Date dt = objCal.getTime();
		String strCreationDt = objSDF.format(dt);
		// LOGGER.info("strCreationDt : "+strCreationDt);
		return strCreationDt;
	}

	public static String getCurrentTimeinDDMMYYY() {
		SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
		// objSDF.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		Calendar objCal = Calendar.getInstance();
		Date dt = objCal.getTime();
		String strCreationDt = objSDF.format(dt);
		// LOGGER.info("strCreationDt : "+strCreationDt);
		return strCreationDt;
	}

	public static String getDateTime() {

		Date d1 = new Date();
		SimpleDateFormat objsdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		objsdf1.format(d1);

		return objsdf1.format(d1);

	}

	public static String GetDateInDDMMMYYY(Date date) {
		SimpleDateFormat objSDF = new SimpleDateFormat(FOURTH_DATE_FORMAT);
		String strCurrentDate = objSDF.format(date);
		return strCurrentDate;
	}

	public static String removespecialchar(String str) {

		LOGGER.info("before " + str);
		str = str.replaceAll("[(<,*>#&;\\\\/+)^]", " ");
		LOGGER.info("after " + str);
		return str;
	}

	public static void setPolicyStartDate(MotorInsuranceModel motorInsuranceModel) {
		try {
			SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
			Calendar objCalendar = new GregorianCalendar();
			if (!DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
					&& StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())) {
				Date dtPolicyExpiry = objSDF.parse(motorInsuranceModel.getPreviousPolicyExpiryDate());
				objCalendar.setTime(dtPolicyExpiry);
				objCalendar.add(Calendar.DATE, 1);
				Date dtPolicyStart = objCalendar.getTime();
				String strPolStartDate = objSDF.format(dtPolicyStart);
				motorInsuranceModel.setPolicyStartDate(strPolStartDate);
			}
			/*if (motorInsuranceModel.isPreviousPolicyHolder()
					&& !DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getVehicleRegistrationDate())
					&& StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegistrationDate())) {
				Date dtVehRegistration = objSDF.parse(motorInsuranceModel.getVehicleRegistrationDate());
				objCalendar.setTime(dtVehRegistration);
				Date dtPolicyStart = objCalendar.getTime();
				String strPolStartDate = objSDF.format(dtPolicyStart);
				motorInsuranceModel.setPolicyStartDate(strPolStartDate);
				LOGGER.info("Policy start date" + motorInsuranceModel.getPolicyStartDate());
			}*/
			LOGGER.info("Policy start date : " + motorInsuranceModel.getPolicyStartDate());
		} catch (Exception e) {
			LOGGER.error("Exception raised is ", e);
		}
	}

	public static String setPolicyStartDate(String PreviousPolicyExpiryDate, String IsPreviousPolicyHolde,
			String VehicleRegistrationDate) {
		String vehicledate = "";
		LOGGER.info("PreviousPolicyExpiryDate :: " + PreviousPolicyExpiryDate);
		LOGGER.info("IsPreviousPolicyHolde :: " + IsPreviousPolicyHolde);
		LOGGER.info("VehicleRegistrationDate :: " + VehicleRegistrationDate);

		try {
			SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
			Calendar objCalendar = new GregorianCalendar();
			if (PreviousPolicyExpiryDate != null && !DATE_DEFAULT.equalsIgnoreCase(PreviousPolicyExpiryDate)
					&& StringUtils.isNotBlank(PreviousPolicyExpiryDate)) {
				Date dtPolicyExpiry = objSDF.parse(PreviousPolicyExpiryDate.replace("\n", ""));
				objCalendar.setTime(dtPolicyExpiry);
				objCalendar.add(Calendar.DATE, 1);
				Date dtPolicyStart = objCalendar.getTime();
				String strPolStartDate = objSDF.format(dtPolicyStart);
				vehicledate = strPolStartDate;
			}
			if (!TRUE.equalsIgnoreCase(IsPreviousPolicyHolde)) {
				LOGGER.info("Policy Holder in method::::::" + IsPreviousPolicyHolde);
				if (!DATE_DEFAULT.equalsIgnoreCase(VehicleRegistrationDate)
						&& StringUtils.isNotBlank(VehicleRegistrationDate)) {
					Date dtVehRegistration = objSDF.parse(VehicleRegistrationDate);
					objCalendar.setTime(dtVehRegistration);
					Date dtPolicyStart = objCalendar.getTime();
					String strPolStartDate = objSDF.format(dtPolicyStart);
					vehicledate = strPolStartDate;
					LOGGER.info("Policy start date in method:::::: " + vehicledate);
				}
			}
			LOGGER.info("Policy start date ::::::: " + vehicledate);
		} catch (Exception e) {
			LOGGER.info("Exception raised is ", e);
		}

		return vehicledate;
	}
	public static String setPolicyStartDateForTwoWheeler(String previousPolicyExpiryDate, String IsPreviousPolicyHolde,
			String VehicleRegistrationDate, String typeOfCover,MotorInsuranceModel motorInsuranceModel) {
		String vehicledate = "";
		LOGGER.info("PreviousPolicyExpiryDate :: " + previousPolicyExpiryDate);
		LOGGER.info("IsPreviousPolicyHolde :: " + IsPreviousPolicyHolde);
		LOGGER.info("VehicleRegistrationDate :: " + VehicleRegistrationDate);
		
		try {
			 SimpleDateFormat objSDF = new SimpleDateFormat(DATE_FORMAT);
			 SimpleDateFormat objSDF1 = new SimpleDateFormat("HH:mm:ss");
			 Calendar objCalendar = new GregorianCalendar();
			
			if (!TRUE.equalsIgnoreCase(IsPreviousPolicyHolde)) {
				LOGGER.info("Policy Holder in method::::::" + IsPreviousPolicyHolde);
				if (!DATE_DEFAULT.equalsIgnoreCase(VehicleRegistrationDate) 
						&& StringUtils.isNotBlank(VehicleRegistrationDate)) {
					Date dtVehRegistration = objSDF.parse(VehicleRegistrationDate);
					objCalendar.setTime(dtVehRegistration);
					Date dtPolicyStart = objCalendar.getTime();
					String strPolStartDate = objSDF.format(dtPolicyStart);
					vehicledate = strPolStartDate;
					LOGGER.info("Policy start date in method:::::: " + vehicledate);
				}
			} else {
				if (!DATE_DEFAULT.equalsIgnoreCase(previousPolicyExpiryDate)
						&& StringUtils.isNotBlank(previousPolicyExpiryDate)) {
					Date dtPolicyExpiry = objSDF.parse(previousPolicyExpiryDate.replace("\n", ""));
					Date currentDate = objSDF.parse(objSDF.format(new Date()));
					if (dtPolicyExpiry.before(currentDate)) {	
						
						long diffInDays = diffDates(previousPolicyExpiryDate,objSDF.format(new Date()));
						long vehicleAge = diffDates(VehicleRegistrationDate,objSDF.format(new Date()));
						LOGGER.info("previousPolicyExpiryDate  : "+previousPolicyExpiryDate +"vehicleAge = "+vehicleAge+", diffInDays : "+diffInDays );
						if("LiabilityOnly".equalsIgnoreCase(typeOfCover)){
							objCalendar.setTime(new Date());
							objCalendar.add(Calendar.DATE, 4);
							LOGGER.info(" diffInDays : "+diffInDays);
						} else {
							if(vehicleAge < 2557 && diffInDays <= 365){ 
								objCalendar.setTime(new Date());
								objCalendar.add(Calendar.DATE, 3);
								LOGGER.info(" diffInDays : "+diffInDays);
							}else{
								objCalendar.setTime(new Date());
							}
						}
					} else {
							objCalendar.setTime(dtPolicyExpiry);
							objCalendar.add(Calendar.DATE, 1);
					}
					//objCalendar.add(Calendar.DATE, 1);
					Date dtPolicyStart = objCalendar.getTime();
					String strPolStartDate = objSDF.format(dtPolicyStart);
					String inceptionTime = objSDF1.format(dtPolicyStart);
					vehicledate = strPolStartDate;
					//Task #317705 GI Products on POS – Inception date and Time
					motorInsuranceModel.setInceptionTime(inceptionTime);
				} else {
					// Task #329605 Unable to Triggered NB-Liability breakin logic for 1 year 
					//If the type of cover is liability only and previous policy expire date is empty. T+4 days logic.
					if (StringUtils.isBlank(previousPolicyExpiryDate)
							&& "LiabilityOnly".equalsIgnoreCase(typeOfCover)) {
						objCalendar.setTime(new Date());
						objCalendar.add(Calendar.DATE, 4);
					} else {
						objCalendar.setTime(new Date());
					}
					Date dtPolicyStart = objCalendar.getTime();
					String strPolStartDate = objSDF.format(dtPolicyStart);
					vehicledate = strPolStartDate;
				}
			}
			
			LOGGER.info("Policy start date ::::::: " + vehicledate);
		} catch ( Exception e) {
			LOGGER.info("Exception raised is ", e);
		}

		return vehicledate;
	}

	public int GetShort(Integer integer) {
		int shortvalue = 0;
		try {

			shortvalue = Integer.valueOf(integer);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return shortvalue;
	}

	public String setExpiryDate(MotorInsuranceModel motorInsuranceModel) {
		String expiryDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date todaysDate = sdf.parse(motorInsuranceModel.getPolicyStartDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(todaysDate);
			cal.add(Calendar.YEAR, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date expDate = cal.getTime();
			expiryDate = sdf.format(expDate);
			motorInsuranceModel.setPolicyExpiryDate(expiryDate);
			int policyTerm = motorInsuranceModel.getPolicyTerm() > motorInsuranceModel.getLiabilityPolicyTerm() ? motorInsuranceModel.getPolicyTerm() : motorInsuranceModel.getLiabilityPolicyTerm();
			LOGGER.info("policyTerm = "+policyTerm);
			LOGGER.info("motorInsuranceModel.getVehicleSubLine() ::: " + motorInsuranceModel.getVehicleSubLine());
			if ("motorCycle".equalsIgnoreCase(motorInsuranceModel.getVehicleSubLine())) {
				// Task #146177 Two wheeler long term product - TECH
				expiryDate = BuyPolicyUtils.getExpiryDateLongTerm("", motorInsuranceModel.getPolicyStartDate(),
						String.valueOf(policyTerm));
				LOGGER.info("expiryDate 1 ::: " + expiryDate);
				Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(expiryDate);
				expiryDate = sdf.format(date);
				LOGGER.info("expiryDate 2 ::: " + expiryDate);
				motorInsuranceModel.setPolicyExpiryDate(expiryDate);
			}
			
			if(motorInsuranceModel.getPolicyTerm() == 1 && motorInsuranceModel.getLiabilityPolicyTerm()== 5
					&& "Comprehensive".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())){
				
				Date oDStartDate = sdf.parse(motorInsuranceModel.getPolicyStartDate());
				Date oDExpiryDate = MotorUtils.getOdExpiryDate(oDStartDate);
				String odExdate=sdf.format(oDExpiryDate);
				motorInsuranceModel.setOwnDamageExpiryDate(odExdate);
				
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("Policy expiry date ::::::: " + expiryDate);
		return expiryDate;
	}

	public static Date getDateObject(String DDMMMYY) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		Date date = new Date();
		try {
			date = sdf.parse(DDMMMYY);
		} catch (Exception e) {
			LOGGER.info("getDateObject Exception :: " + e.getMessage(), e);
			date = null;
			return date;
		}
		return date;
	}

	public static Date getDateObject1(String DDMMMYYYY) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		try {
			date = sdf.parse(DDMMMYYYY);
		} catch (Exception e) {
			LOGGER.info("getDateObject Exception :: " + e.getMessage(), e);
			date = null;
			return date;
		}
		return date;
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static String DateInDDMMMYYYY(String strInceptionDt) {
		SimpleDateFormat objSDF = new SimpleDateFormat(THIRD_DATE_FORMAT);
		SimpleDateFormat objSDFMM = new SimpleDateFormat("dd-MMM-yyyy");
		String strDate = "";
		Date varDate = null;
		LOGGER.info("strInceptionDt::: " + strInceptionDt);
		try {
			varDate = objSDF.parse(strInceptionDt);
			LOGGER.info("strInceptionDt2::: " + varDate);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		strDate = objSDFMM.format(varDate);
		LOGGER.info("strDate  DateInDD-MMM-YYY::: " + strDate);
		return strDate;

	}

	public static String getCurrentDateTimeDDMMMYYYHHMMSS() {
		String dateTime = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			dateTime = sdf.format(date);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return dateTime;
	}

}
