package com.xerago.rsa.util;

public class ConstantVariables {
	/* Numeric validation */
	public final static String NUEMERIC_VALIDATION = "^[0-9]*$";
	/* Numeric validation */
	public final static String numericValidation = "^[0-9]*$";

	/* String validation with space */
	public final static String STRING_VALIDATION = "^[a-zA-Z ]*$";
	public final static String STRING_VALIDATION_1 = "^[a-zA-Z ][^0-9]*$";
	public final static String FINANCER_NAME_VALIDATION = "^[a-zA-Z, ]*$";
	public final static String stringValidation1 = "^[a-zA-Z ][^0-9]*$";
	/* Checking credit card length.Its length should be 16digit. */
	public final static String CREDIT_CARD_LEN = "^\\d{14,16}\\d*$";

	/* At least one numeric should be present with length compulsory 7. */
	public final static String ATLEAST_ONE_NUEMERIC = "(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,10})$";

	/* String validation with without space */
	public final static String STR_VAL_WITHOUT_SPACE = "^[a-zA-Z]*$";

	/* Alphabets Numeric Space / - , are allowed in this */
	public final static String ALPHA_NEMERIC_CALIDATION = "^[0-9a-zA-Z-/, ]*$";

	/* Policy validation */
	public final static String POLICY_VALIDATION = "[a-zA-Z][a-zA-Z0-9]{15}$";

	/* Date validation */
	public final static String DATE_VALIDATION = "[0-9][0-9]/(0|1)[0-9]/(19|20)[0-9]{2}";

	/* At least one alphabet should be present with length compulsory 7. */
	// public final static String passportValidation =
	// "(?!^[0-9]*$)^([a-zA-Z0-9]{8})$";
	public final static String PASSPORT_VALIDATION = "[a-zA-Z][a-zA-Z0-9]{6,7}$";
	// public final static String passportValidation =
	// "^[a-zA-Z].*(?=.{2,8})(?=.*\\d)(?=.*[a-zA-Z]).*$";

	public final static String VEH_REG_NUM_VALIDATION = "^[a-zA-Z].*(?=.{3,})(?=.*\\d)(?=.*[a-zA-Z'']).*$";
	/* Alphabets Numeric WithoutSpace / - , are allowed in this */
	public final static String ALPHA_NUEMERIC_WITHOUT_SPACE = "^[0-9a-zA-Z]*$";

	/* Alphabets Numeric Space / - are allowed in this */
	public final static String ALPHA_NUEMERIC_WITH_COMMA = "^[0-9a-zA-Z,-/ ]*$";

	/* Email validation is done here */
	public final static String EMAIL_VALIDATION = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	/* Date validation */
	public final static String dateValidation = "[0-9][0-9]/(0|1)[0-9]/(19|20)[0-9]{2}";

	/* Email validation is done here */
	public final static String emailValidation = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	/*
	 * At lease one alphabet and numberic should be present with length
	 * compulsory 7.
	 */
	public final static String ATLEAT_ONE_ALPHA_ONE_NUEMERIC = "(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8})$";

	public final static String ADDRESS_VALIDATION = "^[0-9a-zA-Z-/,:.#() ]*$";

	/** Citibank user name hard coded. */
	//public final static String CITIBANK_USERID = "transaction@citibank.com";

	/** Citibank password hard coded. */
	//public final static String CITIBANK_PASSWORD = "Password6";

	//public final static String CITIBANK_CLIENT_ID = "C12345";

	public static final String PRODUCT_NAME_ROLLOVER_2_WHEELER = "RolloverTwoWheeler";

	public static final String GET_QUOTE_PROCESS = "getAQuote";

}