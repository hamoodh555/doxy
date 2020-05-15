package com.xerago.rsa.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Project : DTC <code>IGenericConstants</code>constants declarations
 * @author :
 * @Creation_Date :
 *
 */
public interface Constants {

	String ZERO = "0";
	String ONE = "1";
	String TWO = "2";
	String THREE = "3";
	String FOUR = "4";
	String FIVE = "5";
	String SIX = "6";
	String SEVEN = "7";

	String CARD = "CARD";
	String NONE = "None";
	String CHEQUE = "CHEQUE";
	String OFFLINESWIPE = "offlineSwiping";

	String PAYMENT_MODE = "PAYMENT_MODE";
	String POLICY_NO = "POLICY_NO";
	String ISQUICKRENEW = "ISQUICKRENEW";
	String QUOTE_ID = "QUOTE_ID";
	String PLAN_NAME = "PLAN_NAME";
	String CARD_NUMBER = "CARD_NUMBER";
	String AGENT_ID = "AGENT_ID";
	String OA_CODE = "OA_CODE";
	String HOST_SYSTEM_CODE = "HOST_SYSTEM_CODE";
	String SUMINSURED = "SUMINSURED";
	String CAPS_SILVER = "SILVER";
	String CAPS_GOLD = "GOLD";
	String VERSION = "VERSION";
	String NAME = "NAME";
	String EMAIL_ID = "EMAIL_ID";
	String CONTACT_NUMBER = "CONTACT_NUMBER";
	String LIMITVALUE = "LIMITVALUE";
	String APIKEY = "310ZQmv/bYJMYrWQ1iYa7s43084=";

	String DY = "DY";
	String TY = "TY";
	String HY = "HY";
	String IF = "IF";
	String FY = "FY";
	String IY = "IY";
	String GY = "GY";
	String GE = "GE";
	String MY = "MY";

	String APA = "APA";
	String AME = "AME";
	String AHC = "AHC";
	String FHM = "FHM";
	String FDP = "FDP";
	String FMD = "FMD";
	String SSI = "SSI";
	String AMC = "AMC";
	String CTS = "CTS";
	String CITISUCCESS = "citisuccess";

	String N = "N";
	String CY = "CY";
	String PY = "PY";
	String NB = "NB";
	String CSY = "CSY";
	String REN = "REN";
	String TYS = "TYS";
	String TYM = "TYM";
	String NRI = "NRI";
	String INDIAN = "Indian";
	String RENEWAL_QUOTE_ID = "renewalQuoteId";
	String QUOTEID_STARTS_WITH = "QVPN";
	String QUOTEIDPM_STARTS_WITH = "QVPCV";

	String ACCIDENT_ISSURANCE = "Accident Insurance";
	String HEALTH_ISSURANCE = "Health Insurance";
	String LIFELINE_ISSURANCE = "Lifeline Insurance";
	String HOSPITAL_CASH_ISSURANCE = "Hospital Cash Insurance";
	String MOTOR_CAR_ISSURANCE = "Motor Private Car Insurance";
	String MOTOR_ISSURANCE = "Motor Insurance";
	// added for 2wheeler 23-07-2014
	String TWO_WHEELER_ISSURANCE = "Two Wheeler Insurance";
	Double[] TwoWheeler_Towing_SI = { 100.0, 200.0, 300.0 };
	Integer[] TwoWheeler_Towing_SI_new = { 100, 200, 300 };
	Double[] PrivateCar_Towing_SI = { 500.0, 1000.0, 1500.0 };

	String HOME_ISSURANCE = "Home Insurance";
	String COMBO_PRODUCT = "Combo Product";
	String SURGICARE_ISSURANCE = "Surgicare Insurance";

	String SINGLE_TRIP = "Single Trip";
	String MULTI_TRIP = "Multi Trip";
	String MULTI_TRIP_ = "MULTI TRIP_";
	String INCLUDING_US_CANADA = "Including USA/Canada";
	String EXCLUDING_US_CANADA = "Excluding USA/Canada";

	String STUDENT = "STUDENT";
	String PROFESSIONAL = "PROFESSIONAL";
	String VGC = "VGC";
	String VOC = "VOC";
	String VFA = "VFA";
	String VPC = "VPC";
	String VPCV = "VPCV";
	String VMC = "VMC";
	String IHP = "IHP";
	String TRUE = "true";
	String FALSE = "false";
	String ADD = "add";
	String NULL = "NULL";
	String NEXT = "next";
	String DELETE = "deleterow";
	String DELETE_ = "delete";
	String PARTNER = "partner";
	String AUTHORIZE = "authorize";
	//String PASSWORD = "password";
	String USERSCREATED = "UsersCreated";
	String NEWCUSTOMERSCREATED = "NewCustomersCreated";
	String QUOTESSAVED = "QuotesSaved";
	String CUSTOMERTRACKING = "CustomerTracking";
	String CUSTOMER_TRACKING_DETAILS = "Customer Tracking Details";
	String TIMEOUTCASES = "TimeOutCases";
	String TIME_OUT_CASES = "Time Out Cases";
	String ND = "ND";
	String NOT_DECLINED = "Not Declined";
	String USER_WITHOUT_POLICY = "UsersWithoutQuotesOrRenewals";
	String TYPE_USER_WITHOUT_POLICY = "Users Created but not Saved Quotes or Renewed";
	String DECLINEDQUOTES = "DeclinedQuotes";
	String DECLINED_QUOTES = "Declined Quotes";
	String BREAKININSURANCE = "BreakInInsurance";
	String BREAK_IN_INSURANCE = "Break In Insurance";

	String HOSPITALCASHPOLICIESPURCHASED = "HospitalCashPoliciesPurchased";
	String HOSPITAL_CASH_POLICIES_PURCHASED = "Hospital Cash Policies Purchased";
	String HOMEPOLICIESPURCHASED = "HomePoliciesPurchased";
	String HOME_POLICIES_PURCHASED = "Home Policies Purchased";
	String HOMESHIELDPOLICIESPURCHASED = "HomeShieldPoliciesPurchased";
	String HOME_SHIELD_POLICIES_PURCHASED = "Home Shield Policies Purchased";
	String ACCIDENTPOLICIESPURCHASED = "AccidentPoliciesPurchased";
	String ACCIDENT_POLICIES_PURCHASED = "Accident Policies Purchased";
	String NONMOTORRENEWALPOLICIESPURCHASED = "NonMotorRenewalPoliciesPurchased";
	String NON_MOTOR_RENEWAL_POLICIES_PURCHASED = "Non Motor Renewal Policies Purchased";
	String MOTORRENEWALPOLICIESPURCHASED = "MotorRenewalPoliciesPurchased";
	String MOTOR_RENEWAL_POLICIES_PURCHASED = "Motor Renewal Policies Purchased";
	String TRAVELPOLICIESPURCHASED = "TravelPoliciesPurchased";
	String TRAVEL_POLICIES_PURCHASED = "Travel Policies Purchased";
	String FAMILYPOLICIESPURCHASED = "FamilyPoliciesPurchased";
	String FAMILY_HEALTH_INSURANCE_POLICIES_PURCHASED = "Family Health Insurance Policies Purchased";
	String KEY_USER_FEEDBACK = "userFeedback";
	String USER_FEEDBACK = "User Feedback";
	String SBIHOSPCAHPRODUCTS = "sbiHospCahProducts";
	String SDPHOSPCAHPRODUCTS = "sdpHospCahProducts";
	String HOSPITAL_CASH_ONLINE_POLICIES_PURCHASED = "Hospital Cash Online Policies Purchased";
	String HEALTHSHIELDPOLICIESPURCHASED = "HealthShieldPoliciesPurchased";
	String SDPHEALTHSHIELDPOLICIESPURCHASED = "sdpHealthShieldPoliciesPurchased";
	String HEALTH_SHIELD_POLICIES_PURCHASED = "Health Shield Policies Purchased";
	String CAR_INSURANCE_POLICIES_PURCHASED = "Car Insurance Policies Purchased";
	String PUBLICMOTOR_INSURANCE_POLICIES_PURCHASED = "PublicMotor Insurance Policies Purchased";
	String CARINSURANCEXGENDOWNLOADV5 = "carInsuranceXgenDownloadV5";
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : Start */
	String FamilyGoodHealthIndividualXGenDownload = "FamilyGoodHealthIndividualXGenDownload";
	String FamilyGoodHealthFloaterXGenDownload = "FamilyGoodHealthFloaterXGenDownload";
	String IndividualPersonalAccidentXGenDownload = "IndividualPersonalAccidentXGenDownload";
	String TotalhealthPlusXGenDownload = "TotalHealthPlusXGenDownload";
	String VpcvXGenDownload = "vpcvInsuranceXgenDownload";
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : End */
	String CAR_XGEN_REPORT = "Car Xgen Report";
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : Start */
	String Family_GoodHealth_Floater_XgenReport = "FamilyGoodHealthFloater Xgen Report";
	String Family_GoodHealth_Individual_XgenReport = "FamilyGoodHealthIndiviual Xgen Report";
	String Family_GoodHealth_Individual_XGenDownload = "Family_GoodHealth_Individual_XGenDownload";
	String Individual_Personal_Accident_XgenRepot = "IndividualPersonalAccident Xgen Report";
	String Total_Health_plus_XgenRepot = "ToatlHealthPlus Xgen Report";
	String VPCV_XgenRepot = "VPCV Xgen Report";
	
	String Two_Wheeler_XGEN_REPORT = "Two Wheeler Xgen Report";
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : End */
	String CARINSURANCEPOLICIESPURCHASED = "CarInsurancePoliciesPurchased";
	String PUBLICMOTORINSURANCEPOLICIESPURCHASED = "PublicMotorInsurancePoliciesPurchased";
	String FAMILYGOODHEALTHPOLICIESPURCHASED = "FamilyGoodHealthPoliciesPurchased";
	String FAMILY_GOOD_HEALTH_POLICIES_PURCHASED = "Family Good Health Policies Purchased";
	String FamilyGoodHealthIndividualPoliciesPurchased = "FamilyGoodHealthIndividualPoliciesPurchased";
	String Family_Good_Health_Individual_Policies_Purchased = "Family Good Health Individual Policies Purchased";
	String ScbFamilyGoodHealthFloaterPoliciesPurchased = "ScbFamilyGoodHealthFloaterPoliciesPurchased";
	String Scb_Family_Good_Health_Floater_Policies_Purchased = "Scb Family Good Health Individual Policies Purchased";
	String AmsFamilyGoodHealthFloaterPoliciesPurchased = "AmsureFamilyGoodHealthFloaterPoliciesPurchased";
	String Ams_Family_Good_Health_Floater_Policies_Purchased = "Amsure Family Good Health Individual Policies Purchased";
	String PERSONALACCIDENTPOLICIESPURCHASED = "PersonalAccidentPoliciesPurchased";
	String PERSONAL_ACCIDENT_POLICIES_PURCHASED = "Personal Accident Policies Purchased";
	String TotalHealthPlusPoliciesPurchased = "TotalHealthPlusPoliciesPurchased";
	String Total_Health_Plus_Policies_Purchased = "Total Health Plus Policies Purchased";

	String GO = "go";
	String PAGE = "page";
	String BACK = "back";
	String FAILED = "Failed";
	String SUCCESS = "success";
	String FAILURE = "failure";
	String MOTOR_POLICY = "motorPolicy";
	String NONMOTOR_POLICY = "nonMotorPolicy";
	String VPC_COMPREHENSIVE = "VPC_Comprehensive";
	String PASSNGERCARRYINGCOMPREHENSIVE = "PassengerCarryingComprehensive";

	String NO = "No";
	String YES = "Yes";
	String MALE = "Male";
	String FEMALE = "Female";
	String OTHERS = "Others";
	String PLANNAME = "planName";
	String USER_REGISTRATION = "userRegistration";

	String RELATIONSHIP_LEGALGURDIAN = "Legal Guardian";
	String RELATIONSHIP_BROTHER = "Brother";
	String RELATIONSHIP_NIECE = "Niece";
	String RELATIONSHIP_AUNT = "Aunt";
	String RELATIONSHIP_NEPHEW = "Nephew";
	String RELATIONSHIP_UNCLE = "Uncle";
	String RELATIONSHIP_SON = "Son";
	String RELATIONSHIP_WIFE = "Wife";
	String RELATIONSHIP_SELF = "Self";
	String RELATIONSHIP_SISTER = "Sister";
	String RELATIONSHIP_GRANDSON = "Grand Son";
	String RELATIONSHIP_SPOUSE = "Spouse";
	String RELATIONSHIP_MOTHER = "Mother";
	String RELATIONSHIP_FATHER = "Father";
	String RELATIONSHIP_HUSBAND = "Husband";
	String RELATIONSHIP_DAUGHTER = "Daughter";
	String RELATIONSHIP_SONINLAW = "Son in Law";
	String RELATIONSHIP_BROTHERINLAW = "Brother in Law";
	String RELATIONSHIP_SISTERINLAW = "Sister in Law";
	String RELATIONSHIP_HOUSEWIFE = "Housewife";
	String RELATIONSHIP_GRANDFATHER = "Grand Father";
	String RELATIONSHIP_GRANDMOTHER = "Grand Mother";
	String RELATIONSHIP_MOTHERINLAW = "Mother in Law";
	String RELATIONSHIP_FATHERINLAW = "Father in Law";
	String RELATIONSHIP_GRANDDAUGHTER = "Grand Daughter";
	String RELATIONSHIP_DAUGHTERINLAW = "Daughter in Law";

	String GOLD = "Gold";
	String SILVER = "Silver";
	String PLATINUM = "Platinum";
	String EMPTY_STRING = "";
	String LOGGED_IN = "loggedIn";

	String PLAN_ACCIDENT_SHIELD = "Accident Shield Online";
	String PLAN_HEALTH_SHIELD = "Health Shield Online";
	String PLAN_HOSPITAL_CASH = "Hospital Cash Online";
	String PLAN_HOME_SHIELD = "Home Shield Online";
	String PRODUCT_HOME_CONTENT_NEW = "HOMECONTENTONLINE";
	String PLAN_HOME_CONTENT = "Home Content Online";
	String PLAN_TRAVEL_SHIELD = "Travel Shield Online";
	String PLAN_FAMILY_HEALTH = "Family Health Insurance Online";
	String PLAN_MOTOR_SHIELD = "Car Insurance Online";
	String PLAN_MOTOR_SHIELD_TWOWHEELER = "Twowheeler Insurance Online";
	String PLAN_FAMILY_GOOD_HEALTH = "Family Good Health Online";
	String PLAN_FAMILY_GOOD_HEALTH_Idividual = "Family Good Health Online - Individual";
	String PLAN_FAMILY_GOOD_HEALTH_Floater = "Family Good Health Online - Floater";
	String PLAN_TOTAL_HEALTH_PLUS = "Total Health Plus Online";
	String PLAN_PUBLICMOTOR_SHIELD = "Public Motor Insurance Online";
	// amsure
	String PLAN_FAMILY_GOOD_HEALTH_AMS = "Medical Expenses Coverage";
	// amsure
	
	String PLAN_FAMILY_PLUS = "Family Plus Online";

	String PRODUCT_ACCIDENT_SHIELD = "ACCIDENTSHIELD";
	String PRODUCT_HEALTH_SHIELD = "HEALTHSHIELDONLINE";
	String PRODUCT_HOSPITAL_CASH = "HEALTHSHIELD";
	String PRODUCT_HOME_SHIELD = "HOMESHIELDONLINE";
	String PRODUCT_HOME_CONTENT = "HOMESHIELD";
	String PRODUCT_TRAVEL_SHIELD = "TRAVELSHIELDONLINE";
	String PRODUCT_FAMILY_HEALTH = "FAMILYHEALTHPLAN";
	String PRODUCT_MOTOR_SHIELD = "MOTORSHIELDONLINE";
	String PRODUCT_FAMILY_GOOD_HEALTH = "FAMILYGOODHEALTHONLINE";
	String PRODUCT_TOTAL_HEALTH_PLUS = "TOTALHEALTHPLUSONLINE";
	String PRODUCT_Individual_Personal_Accident = "IndividualPersonalAccident";
	String PRODUCT_Total_Health_plus = "Total Health Plus";
	String PRODUCT_Vpcv = "PUBLICMOTORINSURANCEONLINE";
	String PRODUCT_TWOWHEELER = "TWOWHEELER";
	String PRODUCT_SMARTCASH = "SMARTCASH";
	// -- Added on 28/05/2016 for FamilyPlus pdf --
	String PRODUCT_FAMILYPLUS = "FAMILYPLUS";
	String PRODUCT_LIFELINE = "LIFELINE";
	// End --
	String PRODUCT_TRAVELSECURE = "Travel Secure Online";
	
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : Start */
	String PRODUCT_FAMILY_GOOD_HEALTH_FLOATER = "FAMILYGOODHEALTHFLOATERONLINE";
	String PRODUCT_PUBLICMOTOR_SHIELD = "PUBLICMOTORINSURANCEONLINE";
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : End */
	String PLAN_ID_ACCIDENT_SHIELD = "1721";
	String PLAN_ID_HEALTH_SHIELD = "1724";
	String PLAN_ID_HOSPITAL_CASH = "1699";
	String PLAN_ID_HOME_SHIELD = "1722";
	String PLAN_ID_HOME_CONTENT = "1700";
	String PLAN_ID_TRAVEL_SHIELD = "1723";
	String PLAN_ID_FAMILY_HEALTH = "1725";
	String PLAN_ID_MOTOR_SHIELD = "1726";
	String PLAN_ID_FAMILY_GOOD_HEALTH = "1727";
	String PLAN_ID_TOTAL_HEALTH_PLUS = "1730";

	String REQUEST_UPDATECONTACT_DETAILFORM = "objuUpdateContactDetailsForm";
	String REQUEST_EXISTING = "existing";
	String REQUEST_GETQUOTE_JSP = "getQuoteJsp";
	String REQUEST_CUSTOMER = "cust";
	String REQUEST_ID = "id";
	String REQUEST_TYPE = "type";
	String REQUEST_ACTIONTYPE = "actionType";
	String REQUEST_FROM = "from";
	String REQUEST_QUOTEID = "quoteId";
	String REQUEST_VEHICLE_INSEPECTION_TIME = "vehicleInspectionTime";
	String REQUEST_VEHICLE_INSEPECTION_DATE = "vehicleInspectionDate";
	String REQUEST_ISUPDATE_REQUIRED = "isUpdateRequired";

	String SESSION_USERINFORMATIONDETAILS = "userInformationDetails";
	String SESSION_REGISTERFORM = "registerForm";
	String SESSION_USERREGISTRATION = "userRegistrationForm";
	String SESSION_PLMPRODUCT = "plmproduct";
	String SESSION_PLMFORMP = "publicMotorPlan";
	String SESSION_FINAL_PRODUCT = "finalproduct";
	String SESSION_PRODUCTCODE = "productCode";
	String SESSION_QUICKRENEW = "quickRenewForm";
	String SESSION_LASTLOGIN_IP = "LastLoginIP";
	String SESSION_LASTLOGIN = "LastLogin";
	String SESSION_AUTHORIZEFORM = "authorizeForm";
	String SESSION_CUSTOMER_INFORMATION = "customerInformationDetails";
	String SESSION_ISFROM_RENEW = "isFromRenew";
	String SESSION_AUTHORIZEUSER = "authorizeUserForm";
	String SESSION_RSA_AGENT = "rsaAgent";
	String SESSION_DETAILMATCH = "detailsmismatch";
	String SESSION_CUSTOMERFORM = "customerForm";
	String SESSION_CITIES = "cities";
	String SESSION_STATES = "stateList";
	String SESSION_TOKEN = "token";
	String SESSION_PLM_PRODUCT = "plmproduct";
	String SESSION_VEHICLEMODEL_LIST = "vehicleModelList";
	String SESSION_PUBLICMOTORVEHICLEMODEL_LIST = "publicMotorvehicleModelList";
	String SESSION_DISPLAYHOME_PLAN = "displayHomePlan";
	String SESSION_BUSFORM = "buyForm";
	String SESSION_BUSFORM_01 = "buyForm1";
	String SESSION_BASEFORM = "baseForm";
	String SESSION_USERNAME = "username";
	String SESSION_CURRENTUSER = "CurrentUser";
	String SESSION_CURRENTUSER_TYPE = "CurrentUserType";
	String SESSION_POLICYNO = "policyNumber";
	String SESSION_AGENTID = "agentId";
	String SESSION_PARENT_AGENTID = "parentAgentId";
	String SESSION_CLIENT_CODE = "ClientCode";
	String SESSION_ACCIDENT_SHIELD = "accidentQuotePlan";
	String SESSION_ACCIDENT_SHIELD_PA = "accidentQuotePlanPA";
	String SESSION_RENEW_POLICYDETAILS = "renewPolicyDetailsForm";
	String SESSION_UPDATECUSTOMER_FOR_QUOTE = "updateCustForQuote";
	String SESSION_UPDATECUSTOMER_INFORMATION = "updateCustInfo";
	String SESSION_HEALTH_SHIELD = "healthShieldPlan";
	String SESSION_HOSPITAL_CASH = "healthQuotePlan";
	String SESSION_HOME_SHIELD = "homeShieldOnline";
	String SESSION_HOME_CONTENT = "homeQuotePlan";
	String SESSION_TRAVEL_SHIELD = "travelQuotePlan";
	String SESSION_FAMILY_HEALTH = "familyHealthPlan";
	String SESSION_MOTOR_SHIELD = "motorInsuranceForm";
	String SESSION_PUBLICMOTOR_SHIELD = "publicMotorInsuranceForm";
	String SESSION_EXPIRE_PAGE = "sessionExpirePage";
	String SESSION_PRODUCTNAME = "ProductName";
	String SESSION_FAMILY_GOOD_HEALTH = "familyGoodHealthPlan";
	String SESSION_FAMILY_GOOD_HEALTH_SCB = "scbFamilyGoodHealthPlan";
	String SESSION_FAMILY_GOOD_HEALTH_AMS = "amsFamilyGoodHealthPlan";
	String SESSION_ADMIN_UNDERWRTER = "familyGoodHealthUnderWriter";
	String SESSION_ADMIN_UNDERWRTER_UPDATE = "familyGoodHealthUnderWriterUpdate";
	String SESSION_TOTAL_HEALTH_PLUS = "totalHealthPlusPlan";
	String SESSION_PLAN_NAME = "PlanName";

	String PDF_HOMESHIELD = "HomeShieldOnline.pdf";
	String PDF_HOMECONTENT = "HomeContentOnline.pdf";
	String PDF_FAMILYHEALTH = "FamilyHealthPlan.pdf";
	String PDF_CARINSURANCE = "CarInsuranceOnline.pdf";
	String PDF_PUBLICMOTORINSURANCE = "PublicMotorInsuranceOnline.pdf";
	String PDF_TRAVELSHIELD = "TravelShieldOnline.pdf";
	String PDF_HEALTHSHIELD = "HealthShieldOnline.pdf";
	String PDF_HEALTHQUOTE = "HealthQuotePlan.pdf";
	String PDF_HOSPITAL_CASH = "HospitalCashOnline.pdf";
	String PDF_ACCIDENT_SHIELD = "AccidentShieldOnline.pdf";
	String PDF_PERSONAL_ACCIDENT = "PersonalAccidentOnline.pdf";
	String PDF_RENEWALCONFIRMATION = "RenewalConfirmation.pdf";
	String PDF_ACCIDENT_QUOTE = "AccidentQuotePlan.pdf";
	String PDF_FAMILYGOODHEALTH = "FamilyGoodHealthOnline.pdf";
	String PDF_TOTALHEALTHPLUS = "TotalHealthPlusOnline.pdf";
	String PDF_TWOWHEELER = "TwoWheelerOnline.pdf";
	String PDF_SMARTCASH = "SmartCashOnline.pdf";
	String PDF_TRAVELSECURE= "TravelSecureOnline.pdf";

	String TandC_PDF_HOMESHIELD = "home-shield-online-terms-conditions.pdf";
	String TandC_PDF_HOMECONTENT = "home-contentonline-terms-conditions.pdf";
	String TandC_PDF_TRAVELSHIELD_SINGLE_TRIP = "Travelshield-singletrip-terms.pdf";
	String TandC_PDF_TRAVELSHIELD_MULTI_TRIP = "Travelshield-multitrip-terms.pdf";
	String TandC_PDF_HOSPITAL_CASH = "hospital-cash-online-termsandconditions.pdf";
	String TandC_PDF_PERSONAL_ACCIDENT = "PA-Online-terms-conditions.pdf";
	String TandC_PDF_FAMILYGOODHEALTH = "Family-Good-Health-terms-conditions.pdf";
	String TandC_PDF_TOTALHEALTHPLUS = "TotalHealthPlusOnline-terms-conditions.pdf";
	String TandC_PDF_TRAVELSHIELD_SINGLE_TRIP_CITI = "CITI_Travel_Shield_Secure_Policy_wording.pdf";
	String TandC_PDF_TRAVELSHIELD_SINGLE_TRIP_DBS = "DBS_Travel_Shield_Secure_Policy_wording.pdf";
	String TandC_PDF_HOSPITAL_CASH_SBI = "SBIHospitalCashtermsandcondition.pdf";
	String TandC_PDF_SMARTCASH = "smartcash-online-terms-conditions.pdf";
	//String TandC_PDF_TWOWHEELER = "Two_Wheeler_Condition.pdf";
	//#132262 Motor email changes
	String TandC_PDF_TWOWHEELER = "TwoWheeler-Terms.pdf";	
	String TandC_PDF_MOTORSHIELD = "PrivateCarNewTerms.pdf";
	String TandC_PDF_VPCV_Comprehensive = "VPCV_Commercial-Terms.pdf";
	String TandC_PDF_VPCV_Liablity = "Commercial-Terms.pdf";
	/***************/
	String FORWARD_CARSHIELDQUOTE = "carShieldQuote";
	String FORWARD_PUBLICMOTORSHIELDQUOTE = "publicMotorShieldQuote";
	String FORWARD_FAMILYHEALTH_QUOTE = "familyHealthQuote";
	String FORWARD_TRANSACTIONHISTORY = "transHist";
	String FORWARD_REGISTER = "register";
	String FORWARD_BACKTOQUOTE = "backToQuote";
	String FORWARD_CUSTOMERINFO = "customerInfo";
	String FORWARD_PAYMENTGATEWAY = "paymentGateway";
	String FORWARD_CITI_PRODUCTAUTHORIZE = "citiProductAuthorize";
	String FORWARD_FAILURFOR_EXISITNG = "failureForExisting";
	String FORWARD_PROCESSPAYMENT = "processPayment";
	String FORWARD_RENEWALPOLICY_DETAILS = "renewalPolicyDetails";
	String FORWARD_VIEWADDITIONAL_CARDETAILS = "viewAdditionalCarDetails";
	String FORWARD_VIEWADDITIONAL_PUBLICMOTORDETAILS = "viewAdditionalPublicMotorDetails";
	String FORWARD_CITI_VIEWADDITIONAL_CARDETAILS = "citiViewAdditionalCarDetails";
	String FORWARD_NONMOTOR_POLICY = "nonMotorRenew";
	String FORWARD_CITI_NONMOTOR_POLICY = "citiNonMotorPolicy";
	String FORWARD_MOTORRENEW = "motorRenew";
	String FORWARD_CITI_MOTORPOLICY = "citiMotorPolicy";
	String FORWARD_CHEQUE_SUCCESS = "successCheque";
	String FORWARD_CITI_CHEQUE_SUCCESS = "citiSuccessCheque";
	String FORWARD_EXISTINGUSER_DETAILS = "existingUserDetails";
	String FORWARD_CITI_EXISTINGUSER_DETAILS = "citiExistingUserDetails";
	String FORWARD_VEHICLEDETAILS = "vehicleDetails";
	String FORWARD_CITI_VEHICLEDETAILS = "citiVehicleDetails";
	String FORWARD_CITI_VEHICLE_DETAILS = "citiVehilceDetails";
	String FORWARD_CITI_ERRORVEHIVLE_DETAILS = "citiErrorVehicleDetails";
	String FORWARD_CITI_RENEWVEHICLE_DETAILS = "citiErrorRenwVehicleDetails";
	String FORWARD_CARSHIELDOLD_QUOTE = "carShieldOldQuote";
	String FORWARD_PUBLICMOTORSHIELDOLD_QUOTE = "publicMotorShieldOldQuote";
	String FORWARD_CITI_CALCULATE_PREMIUM = "citiCalcPremium";
	String FORWARD_CITI_ERRORMESSAGE = "citiErrorMsg";
	String FORWARD_CARSHIELD_NEWQUOTE = "carShieldNewQuote";
	String FORWARD_PUBLICMOTOR_NEWQUOTE = "publicMotorNewQuote";
	String FORWARD_AGENT = "agentForward";
	String FORWARD_FIRSTLOGIN = "firstLogin";
	String FORWARD_ADMIN = "admin";
	String FORWARD_ADMIN_UNDERWRITER = "adminUnderWriter";
	String FORWARD_UPDATECITI_CUSTOMER_DETAILS = "updateCitiCustDetails";
	String FORWARD_QUICKRENEW_DETAILS = "quickRenewDetails";
	String FORWARD_RENEW_POLICY = "renewPolicy";
	String FORWARD_CUSTOMER_PAGE = "custPage";
	String FORWARD_CITI_CUSTOMERINFORMATION = "citiCustomerInfo";
	String FORWARD_BACKTO_PAYMENT_GATEWAY = "backToPaymentgateway";
	String FORWARD_CITIRENEW = "citiRenew";
	String FORWARD_BUYRENEW = "buyRenew";
	String FORWARD_CHANGE_USERNAME = "changeUserName";
	String FORWARD_CITICHANGE_USERNAME = "citiChangeUserName";
	String FORWARD_RETURNBACK = "returnBack";
	String FORWARD_CLAIMDETAILS_SAVED = "claimDetailsSaved";
	String FORWARD_CLAIMDETAILS = "claimDetails";
	String FORWARD_THANKYOU = "thankYou";
	String FORWARD_CHEQUE_PAYMENT = "chequePayments";
	String FORWARD_VPCV_APPROVAL = "vpcvApprovals";
	String FORWARD_CITIFAIL = "citiFail";
	String FORWARD_CITIFAILURE = "citiFailure";
	String FORWARD_NBFAILURE = "nbFailure";
	String FORWARD_CITI_SUCCESS = "citiSuccess";
	String FORWARD_FAQS = "faqs";
	String FORWARD_ADDSUCCESS = "addSuccess";
	String FORWARD_PRODUCTAUTHORIZE = "productAuthorize";
	String FORWARD_HOME_CONTENT = "viewCustomerHomeQuote";
	String FORWARD_HEALTH_QUOTE = "viewCustomerHealthQuote";
	String FORWARD_ACCIDENT_QUOTE = "viewCustomerAccidentQuote";
	String FORWARD_FAMILY_QUOTE = "viewCustomerFamilyHealthPlan";
	String FORWARD_TRAVEL_QUOTE = "viewCustomerTravelShieldOnline";
	String FORWARD_HOMESHIELD_QUOTE = "viewCustomerHomeShieldOnline";
	String FORWARD_HEALTHSHIELD_QUOTE = "viewCustomerHealthShieldOnline";
	String FORWARD_PLANANDPREMIUM = "planAndPremium";
	String FORWARD_FEATURES_AND_BENEFITS = "featuresAndBenefits";
	String FORWARD_TERMS_AND_CONDITIONS = "termsAndConditions";
	String FORWARD_CONTACTUS = "contactUs";
	String FORWARD_CARRENEW = "carRenew";
	String FORWARD_CARNEW = "carNew";
	String FORWARD_VEHICLE_DETAILS = "vehilceDetails";
	//String FORWARD_LOGINAGAIN_AFTER_PASSWORDCHANGE = "loginAgainAfterPwdChange";
	String FORWARD_CITIPREVIOUS_VEHICLE_DETAILS = "citiPreviousVehicleDetails";
	String FORWARD_PREVIOUS_VEHICLE_DETAILS = "previousVehicleDetails";
	String FORWARD_FAMILY_HEALTH_QUOTE = "familyHealthQuote";
	String FORWARD_BREAKIN_INSURANCE = "BreakInInsurance";
	String FORWARD_PUBLICMOTORBREAKIN_INSURANCE = "PublicMotorBreakInInsurance";
	String FORWARD_BUYPOLICY = "/BuyPolicy.jsp";
	String FORWARD_BASICDETAILS = "basicdetails";
	String FORWARD_CITI_BASICDETAILS = "citibasicdetails";
	String FORWARD_CITI_EXISITNG_BASIC_DETAILS = "citiExistingbasicdetails";
	String FORWARD_EXISITNG_BASIC_DETAILS = "existingbasicdetails";
	String FORWARD_FAMILYGOODHEALTH_QUOTE = "viewCustomerFamilyGoodHealthOnline";
	String FORWARD_TOTALHEALTHPLUS_QUOTE = "viewCustomerTotalHealthPlusOnline";

	String FORWARD_PREVIOUSADDITIONAL_CARDETAILS = "viewPreviousAdditionalCarDetails";
	String FORWARD_PREVIOUSADDITIONAL_PUBLICMOTORDETAILS = "viewPreviousAdditionalPubliMotorDetails";
	String FORWARD_CITIVIEW_PREVIOUSADDITIONAL_CARDETAILS = "citiViewPreviousAdditionalCarDetails";

	String FORWARD_ERROR_ACCIDENT = "err1AccidentQuotePlan";
	String FORWARD_ERROR_FAMILYFLOATER = "err1FamilyHealthPlan";
	String FORWARD_ERROR_FAMILYQUOTE = "err1FamilyHealthQuote";
	String FORWARD_ERROR_HEALTHSHIELD = "err1HealthShieldPlan";
	String FORWARD_ERROR_HOMECONTENT = "err1HomeQuotePlan";
	String FORWARD_ERROR_HOMESHIELD = "err1HomeShieldOnline";
	String FORWARD_ERROR_HOSPITALCASH = "err1HealthQuotePlan";
	String FORWARD_ERROR_TRAVELSHIELD = "err1TravelQuotePlan";
	String FORWARD_ERROR_CARSHIELD_NEW = "err1CarShieldNewQuote";
	String FORWARD_ERROR_CARSHIELD_OLD = "err1CarShieldOldQuote";
	String FORWARD_ERROR_CARSHIELD_NEWDETAILS = "err1NewCarDetails";
	String FORWARD_ERROR_CARSHIELD_OLDDETAILS = "err1OldCarDetails";
	String FORWARD_ERROR_FAMILYGOODHELATH = "err1FamilyGoodHealthPlan";
	String FORWARD_ERROR_TOTALHELATHPLUS = "err1TotalHealthPlusPlan";

	String FORWARD_EXCEPTION_ACCIDENT = "err2AccidentQuotePlan";
	String FORWARD_EXCEPTION_FAMILYFLOATER = "err2FamilyHealthPlan";
	String FORWARD_EXCEPTION_FAMILYQUOTE = "err2FamilyHealthQuote";
	String FORWARD_EXCEPTION_HEALTHSHIELD = "err2HealthShieldPlan";
	String FORWARD_EXCEPTION_HOMECONTENT = "err2HomeQuotePlan";
	String FORWARD_EXCEPTION_HOMESHIELD = "err2HomeShieldOnline";
	String FORWARD_EXCEPTION_HOSPITALCASH = "err2HealthQuotePlan";
	String FORWARD_EXCEPTION_TRAVELSHIELD = "err2TravelQuotePlan";
	String FORWARD_EXCEPTION_CARSHIELD_NEW = "err2CarShieldNewQuote";
	String FORWARD_EXCEPTION_CARSHIELD_OLD = "err2CarShieldOldQuote";
	String FORWARD_EXCEPTION_CARSHIELD_NEWDETAILS = "err2NewCarDetails";
	String FORWARD_EXCEPTION_CARSHIELD_OLDDETAILS = "err2OldCarDetails";
	String FORWARD_EXCEPTION_FAMILYGOODHELATH = "err2FamilyGoodHealthPlan";
	String FORWARD_EXCEPTION_TOTALHELATHPLUS = "err2TotalHealthPlusPlan";

	String FORWARD_ERROR_SIGNUP = "err1InSignUp";
	String FORWARD_ERROR_PAYMENTGATEWAY = "err1PaymentGateWayPage";
	String FORWARD_ERROR_NEWCUSTOMER = "err1NewCustomer";
	String FORWARD_ERROR_RENEWAL = "err1Renewal";
	String FORWARD_ERROR_CUSTOMER = "citiErrorCustomerInfo";
	String FORWARD_ERROR_UPDATECUSTOMER = "err1UpdateCustomer";

	String FORWARD_EXCEPTION_SIGNUP = "err2InSignUp";
	String FORWARD_EXCEPTION_PAYMENTGATEWAY = "err2PaymentGateWayPage";
	String FORWARD_EXCEPTION_NEWCUSTOMER = "err2NewCustomer";
	String FORWARD_EXCEPTION_UPDATECUSTOMER = "err2UpdateCustomer";
	String FORWARD_EXCEPTION_PRODUCTAUTH = "err2ProductAuthorize";

	String SESSION_DBPARTNER = "DBPartner";
	String DBPARTNER_CITI = "citibank";
	// for policyBazaar 28-05-2014
	String DBPARTNER_PBazaar = "AG018098";
	// for policyBazaar 28-05-2014
	String DBPARTNER_BBazaar = "AG022865";
	String IS_DBPARTNER = "isDPPartner";
	String IS_LNFINANCE = "isLnTFinance";

	String SBI = "sbi";
	String RSA = "RSA";
	String SCB = "SCB";
	String Edelweiss = "BA295000";
	String AMS = "AMS";
	String HEALTH_RSAI = "RSAIFHM";
	String AGENT_RSAI = "RSAI";
	String AGENT_CITI = "CITI";
	String AGENT_SCB = "SCB";
	String AGENT_AMS = "AMS";
	String AGENT_MIBL = "MIBL";
	String RSA_FULLNAME = "Royal Sundaram Online";

	int QUOTE_VALIDITY = 7;
	int QUOTE_EXPIRY = 30;
	double SERVICE_TAX_RATE = -1;
	String HRSMIN = "HH:mm";

	String USER = "U";
	String PURCHASE = "B";
	String TEMPORARY = "T";
	String ADMINUSER = "A";
	String INTERNAL_AGENT = "I";
	String INTERNAL_AGENT_O = "O";
	String INTERNAL_AGENT_Z = "Z";
	String INTERNAL_AGENT_N = "N";
	String INTERNAL_AGENT_D = "D";
	String INTERNAL_AGENT_I = "I";

	//Task #111778 A&M Insurance broker under campaign code Y006
	String AANDM_AGENT_T = "T";
	String AANDM_AGENT_Z = "Z";
	

	String REPORTUSER = "R";
	String UNDERWRITER = "W";

	String QUOTE_STATUS_DRAFT = "D";
	String QUOTE_STATUS_QUOTE = "Q";
	String QUOTE_STATUS_PURCHASED = "E";
	String PAYMENT_ACCEPTED = "Accepted";
	String PAYMENT = "payment";
	String NAME_SEX_INFORMATION = "namesAndSex";
	String PAYMENT_OFFLINE_SWIPING = "offlineSwiping";
	String ISFROM_PAYMENT_CONFIRMATION = "isFromPayConfirm";
	String RN = "RN";

	String SEPARATE_LINE = "//";
	String COMMA_SEPARATOR = ", ";
	String DATE_DEFAULT = "dd/mm/yyyy";
	String DATE_FORMAT = "dd/MM/yyyy";
	String OTHER_DATE_FORMAT = "yyyy-MM-dd";
	String DATE_FORMAT1 = "dd/MM/yyyy HH:mm:ss";
	String COMPLETE_DATE_FORMAT = "dd-MMMMMMMMM-yyyy";
	String COMPLETE_DATE_FORMAT_01 = "dd-MMMMMMMM-yyyy";
	String THIRD_DATE_FORMAT = "dd-MM-yyyy";
	String FOURTH_DATE_FORMAT = "dd-MMM-yyyy";
	String FIFTH_DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";
	String SIXTH_DATE_FORMAT = "dd-MMM-yy";
	String SEVENTH_DATE_FORMAT = "dd:MM:yyyy:hh:mm:ss";
	String EIGHT_DATE_FORMAT = "MM/dd/yyyy";
	String NINTH_DATE_FORMAT = "dd/MMM/yyyy HH:mm:ss";
	String TENTH_DATE_FORMAT = "dd-MM-yy hh:mm";
	String NEW_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	String CPC = "cpc";
	String GOOGLE = "google";
	String FIRST_COOKIE_ELEMENT = "__utmz";
	String SECOND_COOKIE_ELEMENT = "utmcsr";
	String THIRD_COOKIE_ELEMENT = "utmcmd";
	String FOURTH_COOKIE_ELEMENT = "utmgclid";

	String CALCULATE_PREMIUM = "calculatePremium";
	String PROCEED_DATA = "proceedData";
	String UPDATE_DATA = "updateData";
	String ADDROW = "addrow";
	String CALCULATE = "calculate";
	String UPDATE_METHOD = "updateMethod";
	String POPULATE_ENDDATE = "populateEndDate";
	String PAYMENT_TYPE = "paymentType";
	String SHOWPREMIUM = "showPremium";
	String PROCEED = "proceed";
	String DELETE_MEMEBER = "deleteMember";
	String ADD_MEMBER = "addMember";
	String PROCESS_QUOTE = "processQuote";
	String THIRDPARTY_ONLY = "Third party Only";
	String COMPANY = "Company";
	String VPCCOMPULSORY_PA = "VPC_CompulsoryPA";
	String INDIVIDUAL = "Individual";
	String VPC_LLEMPLOYEES = "VPC_LLEmployees";
	String VMC_LLEMPLOYEES = "VMC_LLEmployees";
	String CUSTOMER_DETAILS = "Customer Details";
    String VMC_PAPaidDriver="VMC_LLPaidDriverCover";
	String PLAN = "plan";
	String FAQ = "faq";
	String EDIT = "edit";
	String DISPLAY = "display";
	String CLICKED = "clicked";
	String FIRST_YEAR = "1 Year";
	String SECOND_YEAR = "2 Years";
	String FILE_EXTENSION = ".pdf";
	// String PARTNER_FILENAME = "/Partner.ini";
	String PDF_FILENAME = "/Properties.ini";

	String ACCIDENTSHIELD = "accidentShield";
	String HEALTHSHIELD = "healthShield";
	String HOSPITALCASH = "HospitalCash";
	String HOSPITALCASH_01 = "hospitalCash";
	String HOSPITALCASHONLINE = "HospitalCashOnline";
	String FAMILYHEALTH = "familyHealth";
	String TRAVELSHIELD = "travelShield";
	String HOMESHIELD = "homeShield";
	String HOMECONTENT = "homeContent";
	String HOMECONTENT_01 = "HomeContent";
	String HOMECONTENTONLINE = "HomeContentOnline";
	String FAMILYGOODHEALTH = "familyGoodHealth";
	String TOTALHEALTHPLUS = "totalHealthPlus";
	String LIFELINE = "LifeLine";

	String FILE_LOCATION = "C:/Program Files/Apache Group/Tomcat 4.1/webapps/AgentPortal/WEB-INF/classes/vam/rsa/projects/portal/util/CheckSumKey.txt";

	String CHANNEL = "Agents";
	String JURISDICTION = "India";
	String INDIA = "INDIA";
	String SALES_COMPANY = "Internet";

	int MAXIMUMCOUNT = 1000;
	String PACKAGE_ATTRIBUTE = "package";

	String TRAVEL_REPORT = "Travel Sheet";
	String HOMESHIELD_REPORT = "Home Shield";
	String HOMECONTENT_REPORT = "Home Content";
	String USERFEEDBACK_REPORT = "User Feedback";
	String CARINSURANCE_REPORT = "Car Insurance";
	String PUBLICMOTOR_REPORT = "PublicMotor Insurance";
	String FAMILYHEALTH_REPORT = "Family Health";
	String MOTORPOLICIES_REPORT = "Motor Policies";
	String NONMOTOR_POLICIES = "Non Motor Policies";
	String ACCIDENTSHIELD_REPORT = "Accident Shield";
	String PERSONAL_ACCIDENT_REPORT = "Personal Accident";
	String FAMILYGOODHEALTH_REPORT = "Family Good Health";
	String SCB_FAMILYGOODHEALTH_REPORT = "SCB Family Good Health";
	String AMS_FAMILYGOODHEALTH_REPORT = "AMS Family Good Health";

	String P_HOMEQUOTE_DETAILS = "P_HOMEQUOTE_DETAILS";
	String P_HOME_SHIELD_ONLINE = "P_HOME_SHIELD_ONLINE";
	String P_HEALTHQUOTE_DETAILS = "P_HEALTHQUOTE_DETAILS";
	String P_ACCIDENTQUOTE_DETAILS = "P_ACCIDENTQUOTE_DETAILS";
	String P_TRAVEL_QUOTE_DETAILS = "P_TRAVEL_QUOTE_DETAILS";
	String P_FGH_QUOTE_DETAILS = "P_FGH_QUOTE_DETAILS";

	String HYPOTHECATION = "Hypothecation";
	String HIRE_PURCHASE = "Hire purchase";
	String LEASE = "Lease";
	String VPC_OD_PRICING_AAM = "VPC_OwnDamageCover_pricingelement_AutoAssociationMembership";
	String VPC_ANTITHEFT = "VPC_AntiTheft";
	String VPC_PA_UNNAMED = "VPC_PAUnnamed";
	String VPC_PA_PAID_DRIVER = "VPC_PAPaidDriver";
	String VPC_FIBERGLASS = "VPC_FiberGlass";
	String DEP_WAIVER = "DepreciationWaiver";
	String WIND_SHIELD_GLASSS = "WindShieldGlass";
	String SPARE_CAR = "SpareCar";
	// Engine Protector Start    
    String AggravationCover="AggravationCover";
	// Engine Protector end 
    //NCB Protector start
    String NCBProtectorCover="NCBProtectorCover";
    //NCB Protector start
	String REG_ROAD_TAX = "RegistrationchargesRoadtax";
	String INVOICE_PRICE = "InvoicePrice";
	String LOSS_OF_BAGGAGE = "LossOfBaggage";
	String DD="test";
	String VPC_OD_DED_VOL_DED = "VoluntaryDeductible";
	String VPC_ELEC_ACC = "VPC_ElectAccessories";
	String CV_ELEC_ACC = "ElectircalAccessoriess";
	String VPC_CNG_LPG_OD = "VPC_CNGLPGforOD";
	String VPC_WLL_DRIVER = "VPC_WLLDriver";
	String VPC_LL_EMP = "VPC_LLEmployees";
	String NON_ELECTRICAL_ACCE = "NonElectricalAccessories";
	String VPC_ODBASICCOVER = "VPC_ODBasicCover";
	String VOLUNTARYDEDUCTIBLE = "VoluntaryDeductible";

	String ON = "on";
	String OFF = "off";

	String PLAN_1 = "PLAN 1";
	String PLAN_2 = "PLAN 2";
	String PLAN_3 = "PLAN 3";
	String PLAN_4 = "PLAN 4";
	String PLAN_5 = "PLAN 5";

	String CHECKSUM_1 = "checksum_1";
	String CHECKSUM_2 = "checksum_2";
	String CHECKSUM_3 = "checksum_3";
	String CHECKSUM_4 = "checksum_4";
	String CHECKSUM_5 = "checksum_5";

	String MOTORCYCLE = "MotorCycle";
	String MOTOR_CYCLE = "motorCycle";
	String PASSENGER_CARRYING = "PassengerCarrying";
	String MISCELLANEOUS_VEHICLE = "MiscellaneousVehicle";

	String POLICY_CONDITION = " where policyid=?";
	String COVERAGE_CONDITION = " where coverageid=?";

	String PROCEED_TO_CONTACT_DETAILS = "proceedToContactDetails";
	String PROCEED_TO_PAYMENT_GATEWAY_SELECTION = "proceedToPaymentGatewaySelection";
	String PROCEED_TO_VIEW_SUMMARY = "proceedToViewSummary";
	String PROCEED_TO_PAY = "proceedToPay";
	/**
	 * Start of changes by VAM for the related to deadlock 08/06/2011
	 * (dd/mm/yyyy) format
	 */
	String POLICY_CONDITION_TEMP = " where POLICYID = ? and  VERSION_NO =   (select  max(version_no) from POLICY_ATTRIBUTES_TEMP where POLICYID = ?)";
	String COVERAGE_CONDITION_TEMP = " where coverageid=?";

	String POLICY_PRICING_CONDITION_TEMP = " where POLICYID = ? and  VERSION_NO =   (select  max(version_no) from POLICY_PRICINGELEMENTS_TEMP where POLICYID = ?)";
	/**
	 * End of changes by VAM for the related to deadlock 08/06/2011 (dd/mm/yyyy)
	 * format
	 */

	// Version check start 09-06-2014
	String POLICY_PRICING_CONDITION_TEMP_VERSION = " where POLICYID = ? and  VERSION_NO = ? ";
	String POLICY_CONDITION_TEMP_VERSION = " where POLICYID = ? and  VERSION_NO =? ";

	/* For No claim Bonus percent issue - Ticket No:241025 - 20/07/2011 */
	String NO_CLAIM_BONUS_PERCENT = "noClaimBonusPercent";
	/* For No claim Bonus percent issue - Ticket No:241025 - 20/07/2011 */
	String SADMIN = "S";

	/* added for FamilyGoodHealth Xgen on 02/02/2012 : Start */
	String FGH_INDIVIDUAL_REPORT = "FGH INDIVIDUAL REPORT";
	String FGH_FLOATER_REPORT = "FGH FLOATER REPORT";
	String INDIVIDUALPERSONALACCIDENT_REPORT = "INDIVIDUALPERSONALACCIDENT REPORT";
	String TOTALHEALTHPLUS_REPORT = "TOTALHEALTHPLUS REPORT ";
	String VPCV_REPORT = "VPCV REPORT";
	/* added for FamilyGoodHealth Xgen on 02/02/2012 : End */

	String INSURED_CODE_PREFIX = "IC";
	String X_GEN_INSURED_CODE_PREFIX = "OZ";
	String NOMINEE_CODE_PREFIX = "NC";
	String GUARDIAN_CODE_PREFIX = "GC";
	
	char OTP_PENDING = 'P';
	char OTP_SUCCESS = 'S';
	char OTP_YES = 'Y';
	
	//Task #146177 - Two wheeler long term product - TECH 
	String LONG_TERM_MOTORCYCLE_POLICY_PREFIX = "VMNL"; 
	
	String MotorCyclePackage_LongTerm = "MotorCyclePackage_LongTerm";
	String MotorCyclePackage = "MotorCyclePackage";
	double Default_Rate_Discount = -0.25;
	
	String IMTEAM = "IMTEAM";
	String Policy_No_Validation = "[a-zA-Z0-9.,-/()]*";
	Pattern mobileNoValidation=Pattern.compile(MotorValidation.Mobile_No_Validation);
	Pattern emailValidation = Pattern.compile(MotorValidation.EMAIL_VALIDATION);
	Pattern stringValueWithSpace =Pattern.compile(MotorValidation.STRING_WITH_SPACE_VALIDATION);
	Pattern numericValidation = Pattern.compile(MotorValidation.NUEMERIC_VALIDATION);
	Pattern addressValidation = Pattern.compile(MotorValidation.address_VALIDATION);
	Pattern alphaNumreicWithSpace= Pattern.compile(MotorValidation.ALPHA_NUEMERIC_WITH_SPACE);
	SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	String PAN_CARD_VALIDATION = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
	String STRING_WITHOUT_SPECIAL_CHARACTERS_VALIDATION = "[a-zA-Z0-9 ]*";
	String Mobile_No_Validation = "[0-9]{1}[0-9]{9}";
	String TIME_FORMAT = " HH:mm:ss";
	String EIA_NO_VALIDATION = "^[a-zA-Z0-9_\\-]{9}+$";
	String PRODUCT_NAME = "MotorCyclePackage";
	String PINCODE_VALIDATION = "[0-9]{6}";

	
	 String AADHAAR_VALIDATION = "[0-9]{12}";
	 String DATE_VALIDATION = "[0-9][0-9]/(0|1)[0-9]/(19|20)[0-9]{2}";

	 //Type Of Cover
	 String LIABILITY_ONLY = "LiabilityOnly";
	 String BUNDLED = "Bundled";
	 String COMPREHENSIVE = "Comprehensive";
	 
	//Technical Discount Grid Names 
	 String AGENT_REGION = "AGENT-REGION";
	 String MODEL_AGENT = "MODEL-AGENT";
	 String MODEL_STATE = "MODEL-STATE";
	 String MODEL_REGION = "Model-Region";
	
	 List<String> nomineeRelationshipPossibleValues = Arrays.asList(
			 "Aunt","Brother In Law","Brother","Child","Cousin","Daughter-in-Law","Daughter",
			 "Employee","Employer","Father-in-law","Father","Grand Daughter","Grand Father",
			 "Grand Mother","Grand Son","Husband","Mother-in-law","Mother","Nephew","Niece","OTHERS",
			 "Self","Son-in-Law","Sister","Sister-in-Law","Son","Spouse","Uncle","Wife");

	 String VEHICLE_TYPE = "2 Wheeler";
	 String BIKE = "Bike";
	 String SCOOTER = "Scooter";
	 String ROLLOVER_TWOWHEELER = "RollOverTwoWheeler";
	 String BRANDNEW_TWOWHEELER = "BrandNewTwoWheeler";
	 
	 List<String> CoverFoxAgent = Arrays.asList("BA503452","BA502116","BA505126");
	 String STP = "STP";  //Straight through Process.
}
