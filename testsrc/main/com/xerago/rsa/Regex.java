package com.xerago.rsa;

public class Regex {

	public static final String EMAIL = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

	public static final String MOBILE = "^[789]\\d{9}$";

	public static final String PINCODE = "^[1-9]\\d{5}$";

	public static final String PANNO = "^[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}$";

	public static final String NAME = "[a-zA-Z\\s']+";

	public static final String ADDRESS = "^[a-zA-Z0-9\\s\\.,\\/\\#]+$";

	public static final String CITY = "^[a-zA-Z0-9\\s\\.,\\-]+$";

	public static final String PASSPORTNO = "^([a-zA-Z]{1})(\\d{7})$";

	public static final String RELATION = "^[a-zA-Z0-9\\s\\.,\\-]+$";

	public static final String TUITION_FEE_PER_MONTH = "^\\d{1,7}$";

	public static final String ADDRESS_EMPTY_OR_STRING = "^$|^[a-zA-Z0-9\\s\\.,\\/\\#]+$";

	public static final String CONTACT_NUMBER = "^\\d{1,15}$";

	/* Numeric validation */
	public final static String NUEMERIC_VALIDATION = "^[0-9]*$";

	/* String validation with space */
	public final static String STRING_VALIDATION = "^[a-zA-Z ]*$";

	/* Checking credit card length.Its length should be 16digit. */
	public final static String CREDIT_CARD_LEN = "^\\d{14,16}\\d*$";

	/* At least one numeric should be present with length compulsory 7. */
	public final static String ATLEAST_ONE_NUEMERIC = "(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,10})$";

	/* String validation with without space */
	public final static String STR_VAL_WITHOUT_SPACE = "^[a-zA-Z]*$";

	/* Alphabets Numeric Space / - , are allowed in this */
	public final static String ALPHA_NEMERIC_VALIDATION = "^[0-9a-zA-Z-/, ]*$";

	/* Policy validation */
	public final static String POLICY_VALIDATION = "[a-zA-Z][a-zA-Z0-9]{15}$";

	/* Date validation */
	public final static String DATE_VALIDATION = "[0-9][0-9]/(0|1)[0-9]/(19|20)[0-9]{2}";

	/* Alphabets Numeric Space / - are allowed in this */
	public final static String ALPHA_NUEMERIC_WITH_COMMA = "^[0-9a-zA-Z,-/ ]*$";

	private Regex() {

	}

}
