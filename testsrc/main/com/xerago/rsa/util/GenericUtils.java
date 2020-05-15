/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xerago.rsa.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*import com.xerago.project.portal.service.forms.RenewPolicyDetailsForm;*/

public class GenericUtils implements Constants {

	private static final Log LOGGER = LogFactory.getLog(GenericUtils.class);

	public static String checkString(String str) {
		if (StringUtils.isBlank(str) || NULL.equalsIgnoreCase(str) || "null".equalsIgnoreCase(str)) {
			return "";
		}
		return str.trim();
	}

	public static void clearSession(HttpServletRequest request) {
		Enumeration enumeration = request.getSession().getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String object = (String) enumeration.nextElement();
			if (!SESSION_DISPLAYHOME_PLAN.equalsIgnoreCase(object)) {
				request.getSession().removeAttribute(object);
			}
		}
	}

	/**
	 * To convert Date data type of DB to dd/MM/yyyy format
	 * 
	 * @param String
	 *            <code>strDate</code>
	 * @return String
	 */
	public static String convertDate(String strDate) {
		String strResultDate = "";
		try {
			SimpleDateFormat objSdfDB = new SimpleDateFormat(OTHER_DATE_FORMAT);
			SimpleDateFormat objSdfAppl = new SimpleDateFormat(DATE_FORMAT);

			Date dtFrmt = null;
			if (strDate != null) {
				dtFrmt = objSdfDB.parse(strDate);
			}
			if (dtFrmt != null) {
				strResultDate = objSdfAppl.format(dtFrmt);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return strResultDate;
	}

	/** To get the value of session variable passed as the parameter */
	public static String getSessionVariable(HttpServletRequest request, String sessionVariable) {
		String value = (String) request.getSession().getAttribute(sessionVariable);
		if (StringUtils.isBlank(value) || NULL.equalsIgnoreCase(value)) {
			value = "";
		}
		return value;
	}

	public static boolean isNotValidString(String string) {
		boolean isNotValidString = false;
		if (StringUtils.isBlank(string) || "dd/mm/yyyy".equalsIgnoreCase(string)) {
			isNotValidString = true;
		}
		return isNotValidString;
	}

	public static boolean matchesAny(String str, String[] potentialMatches) {
		for (String match : potentialMatches) {
			if (match.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To get Policy in case if Renewals
	 * 
	 * @param HttpServletRequest
	 *            <code>request</code>
	 * @param String
	 *            <code>quoteId</code>
	 * @return String
	 */
	/*
	 * public static String getPolicyNumber(String strQuoteId,
	 * RenewPolicyDetailsForm objRenewPolicyDetailsForm) { String
	 * strPolicyNumber = ""; if (strQuoteId.length() <= 0) {
	 * LOGGER.info("policy id from renewal form	::	" +
	 * objRenewPolicyDetailsForm.getPolicyNumber()); strPolicyNumber =
	 * objRenewPolicyDetailsForm.getPolicyNumber(); int polNumLength =
	 * strPolicyNumber.length(); LOGGER.info("policyNumber length 	::	" +
	 * strPolicyNumber.length()); String strModifiedPolicyNo =
	 * strPolicyNumber.substring(polNumLength - 2);
	 * LOGGER.info("Number to be incremented	::	" + strModifiedPolicyNo); long
	 * lnPolNo = Long.parseLong(strModifiedPolicyNo); lnPolNo = lnPolNo + 1;
	 * String strPolNum = lnPolNo + ""; if (strPolNum.length() == 2) {
	 * strPolicyNumber = strPolicyNumber.substring(0, 14) + strPolNum; } else if
	 * (strPolNum.length() == 1) { strPolicyNumber =
	 * strPolicyNumber.substring(0, 15) + strPolNum; } } return strPolicyNumber;
	 * }
	 */

	/**
	 * To get Policy in case if Renewals
	 * 
	 * @param HttpServletRequest
	 *            <code>request</code>
	 * @param String
	 *            <code>quoteId</code>
	 * @return String
	 */

	public static boolean matchesAnyIgnoreCase(String str, String[] potentialMatches) {
		for (String match : potentialMatches) {
			if (match.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To validate Customer Tracking details
	 * 
	 * @param BaseForm
	 *            <code>objBaseForm</code>
	 * @param ActionMessages
	 *            <code>messages</code>
	 * @return int
	 */

}
