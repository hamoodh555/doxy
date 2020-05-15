package com.xerago.rsa.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xerago.rsa.domain.DQuoteDetails;

public class BuyPolicyUtils {

	private static final Log LOGGER = LogFactory.getLog(BuyPolicyUtils.class);

	public static String getExpiryDate(String strQuoteId, String strInceptionDate, String strPeriodOfInsurance) {
		String strExpiryDt = "";
		LOGGER.info("getExpiryDate method call strInceptionDate:::: " + strInceptionDate);

		try {
			int ip = 0;
			if (!GenericUtils.isNotValidString(strPeriodOfInsurance)) {
				ip = Integer.parseInt(strPeriodOfInsurance);
			}
			SimpleDateFormat objSDF = new SimpleDateFormat("dd-MMM-yyyy");
			Date dtCurrent = new Date(strInceptionDate);
			Calendar objCal = Calendar.getInstance();
			objCal.setTime(dtCurrent);
			objCal.add(Calendar.YEAR, ip);
			objCal.add(Calendar.DAY_OF_MONTH, -1);
			Date dtExpiry = objCal.getTime();
			strExpiryDt = objSDF.format(dtExpiry);
		} catch (Exception e) {
			// LOGGER.error("Exception raised is ", e);
			throw e;
		}
		return strExpiryDt;
	}

	public static String getExpiryDateLongTerm(String strQuoteId, String strInceptionDate, String strPeriodOfInsurance)
			throws Exception {
		String strExpiryDt = "";
		LOGGER.info("strQuoteId ::: " + strInceptionDate);
		LOGGER.info("strInceptionDate ::: " + strInceptionDate);
		LOGGER.info("strPeriodOfInsurance ::: " + strPeriodOfInsurance);

		try {
			int ip = 0;
			if (!GenericUtils.isNotValidString(strPeriodOfInsurance)) {
				ip = Integer.parseInt(strPeriodOfInsurance);
			}
			SimpleDateFormat objSDF = new SimpleDateFormat("dd-MMM-yyyy");
			Date dtCurrent = new SimpleDateFormat("dd/MM/yyyy").parse(strInceptionDate);
			LOGGER.info("dtCurrent ::: " + objSDF.format(dtCurrent));
			Calendar objCal = Calendar.getInstance();
			objCal.setTime(dtCurrent);
			LOGGER.info("objCal 1 ::: " + objSDF.format(objCal.getTime()));
			objCal.add(Calendar.YEAR, ip);
			LOGGER.info("objCal 2 ::: " + objSDF.format(objCal.getTime()));
			objCal.add(Calendar.DAY_OF_MONTH, -1);
			LOGGER.info("objCal 3 ::: " + objSDF.format(objCal.getTime()));
			Date dtExpiry = objCal.getTime();
			strExpiryDt = objSDF.format(dtExpiry);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;
		}
		return strExpiryDt;
	}
}
