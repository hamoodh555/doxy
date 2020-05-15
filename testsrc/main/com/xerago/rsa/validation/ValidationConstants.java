package com.xerago.rsa.validation;

public interface ValidationConstants {

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

	/** Citibank user name hard coded. */
	//public static final String CITIBANK_USERID = "transaction@citibank.com";

	/** Citibank password hard coded. */
	//public static final String CITIBANK_PASSWORD = "Password6";

	//public static final String CITIBANK_CLIENT_ID = "C12345";
	
	public static final String PAN_CARD_VALIDATION = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
	
	public static final String STRING_WITHOUT_SPECIAL_CHARACTERS_VALIDATION = "[a-zA-Z0-9 ]*";
	
	public final static String MOBILE_VALIDATION = "(?!0{10})[0-9][0-9]{9}";
	
	public static final String Mobile_No_Validation = "[1-9]{1}[0-9]{9}";
	
	public static final String Policy_No_Validation = "[a-zA-Z0-9.,-/()]*";

	
}

