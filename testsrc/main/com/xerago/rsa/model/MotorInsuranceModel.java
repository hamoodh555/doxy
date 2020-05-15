package com.xerago.rsa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.valuemomentum.plm.parser.Product;
import com.xerago.rsa.rating.model.MotorProduct;

public class MotorInsuranceModel extends MotorProduct implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strTitle;
	private String strFirstName;
	private String strLastName;
	private String strName;

	private String strEmail;
	private String strStdCode;
	private String strPhoneNo;
	private String strMobileNo;
	
	private String contactAddress1;
	private String contactAddress2;
	private String contactAddress3;
	private String contactAddress4;
	private String contactCity;
	private String contactPincode;
	private String contactOtherCity;
	
	private String addressOne;
	private String addressTwo;
	private String addressThree;
	private String addressFour;
	private String otherCity;
	private String pinCode;
	private String clientCode;
	
	private String nominee_Name;
	private String nominee_Age;
	private String relationship_with_nominee;
	private String guardian_Name;
	private String guardian_Age;
	private String relationship_with_Guardian;

	private String wsStatus;
	
	private String clientName;
	private String searchEngine;
	private String campaignName;
	private String adGroup;
	private String keyword;
	private String searchVsContent;
	private String ipAddress;
	private String referralUrl;
	private String gclid;
	private String leadType;
	private String campaign;
	private String userId;

	// Bug #148321 &cover_dri_othr_car_ass Validation Issue
	private String cover_dri_othr_car_ass;
	// Bug #148321 &cover_dri_othr_car_ass Validation Issue

	// Insurance Quote for Brand New Car
	private String vehicleMakeCode = "";
	private String vehicleMakeId = "";

	private String vehicleModelName;
	private String vehicleRegistrationRegion;

	private double premiumWithOutAddOnCov;

	// Additional Covers & Discounts
	private String legalliabilitytoemployees;
	private String legalliabilitytopaiddriver;
	private String personalaccidentcoverforunnamedpassengers;
	private String accidentcoverforpaiddriver;
	private String valueofelectricalaccessories;
	private String valueofnonelectricalaccessories;
	private String fibreglass;

	private boolean automobileAssociationMembership;
	private String carFittedWithAntiTheftDevice = "false";
	private String voluntarydeductible;

	// Brand New Car Information
	private String registrationNumber;

	private String engineCapacityAmount;
	private String isCarFinanced;
	private String isCarFinancedValue;
	private String financierName;
	private String otherSafetyFeatureInstalled;
	private String purposeOfCarUsed;
	private String vehicleMostlyDrivenOn;
	private String parkingDuringDay;
	private String parkingDuringNight;
	private String isAccepted;
	private String isBreakinInsurance;
	private String isUsedCar;
	private String companyNameForCar;

	// New model information
	private String rtoCity;
	private String rtoState;
	private String manufacturer;
	private String model;
	private String mobile;

	private String quoteId;
	private String externalReferenceNumber;
	private String policyNumber;
	
	/** Previous policy details */
	private String previousPolicyExpiryDateDeclaration;
	private String previousPolicyType;
	
	private String noClaimBonusPercentinCurrent;

	private String noClaimBonusDeclaration;
	private String previousPolicyExpiryDateAndClaimcDeclaration;

	private String previuosPolicyNumber;
	private String previousInsurerName;
	private String previousinsurersCorrectAddress;
	private String previousinsurersCity;
	private String previousinsurersPincode;

	private List<ElectronicValues> electricalAccessoriesList;
	private String[] electronicNameOfElectronicAccessories;
	private String[] electronicMakeModel;
	private String electronicValue;

	private List<NonElectronicValues> nonElectricalAccessoriesList;
	private String[] nonElectronicNameOfElectronicAccessories;
	private String[] nonElectronicMakeModel;
	private String nonElectronicValue;

	private String drivingLicense;
	private String roadworthyCondition;
	private String claimAmountReceived;
	private String claimsReported;
	private String proposalDate;
	private String inceptionTime;

	private double additionalDiscount;
	private double totalDiscount;
	
	/** Additional properties added for renewals */

	private String totalIdv;
	private double serviceTax;
	private double educationCess;
	private double krishiKalyanCess;

	private String buyTime;

	private Product product;
	private Map<String, String> premiumBreakUpMap;

	// Leap of Good Faith Factor start 22/06/2015

	

	private String statusCode;
	private boolean insertWS;

	// Task #146177 Two wheeler long term product - TECH
	
	private String discountIDVPercent1Year;
	private String discountIDVPercent2Year;
	private String discountIDVPercent3Year;
	private String discountIDVPercent4Year;
	private String discountIDVPercent5Year;

	private String valueofelectricalaccessoriesforyear2;
	private String valueofnonelectricalaccessoriesforyear2;
	private String valueofelectricalaccessoriesforyear3;
	private String valueofnonelectricalaccessoriesforyear3;
	private String valueofelectricalaccessoriesforyear4;
	private String valueofnonelectricalaccessoriesforyear4;
	private String valueofelectricalaccessoriesforyear5;
	private String valueofnonelectricalaccessoriesforyear5;

	private Integer vehicleAgeforyear2;
	private Integer vehicleAgeforyear3;
	private Integer vehicleAgeforyear4;
	private Integer vehicleAgeforyear5;

	private String accidentcoverforpaiddriverforyear2;
	private String accidentcoverforpaiddriverforyear3;
	private String accidentcoverforpaiddriverforyear4;
	private String accidentcoverforpaiddriverforyear5;

	private String personalaccidentcoverforunnamedpassengersforyear2;
	private String personalaccidentcoverforunnamedpassengersforyear3;
	private String personalaccidentcoverforunnamedpassengersforyear4;
	private String personalaccidentcoverforunnamedpassengersforyear5;

	private double femaleDiscountRate;

	private double nilIntermediationCoverPremium;
	private double bulkDealDiscountCoverPremium;

	// towing change
	
	private String additionalTowingChargesCover;
	private String additionalTowingChargesCoverforyear2;
	private String additionalTowingChargesCoverforyear3;
	private String additionalTowingChargesCoverforyear4;
	private String additionalTowingChargesCoverforyear5;
	private double additionalTowingChargesCoverPremium;

	private double rateDiscountPremium;
	private double rateDiscount;
	
	private String fileRC;
	private String fileRN;
	private String statusFlag;
	private int fileRCSize;
	private int fileRNSize;
		
	private String respType;
	private String reqType;

	private String apiKey;
	
	
	private String addonModel;
	private String occupation;
	private String isSameASRegistrationAddress;
	private String regPinCode;
	private String discountidvPercent;
	private String optedAdditonalCover;
	private String optedDisount;
	private String agentCode;
	private String businessStatus;
	private String paToOwnerDriver;
	private String regOtherCity;
	private String regCity;
	private String typeOfAction;
	
	
	private String virNumber;
	private String vehicleInspectionDate;
	private Date previousQuoteDate;
	
	private boolean isSevenDaysBreakIn;
	private String typeOfCover;
	
	private boolean enableCampaignDiscount;
	private double campaignDiscount;
	private Double campaignDiscountRequest;
	private Double technicalDiscountFromRequest;
	private int cpaPolicyTerm;
	

	private String originalIdvFor1Year;
	private String originalIdvFor2Year;
	private String originalIdvFor3Year;
	private String originalIdvFor4Year;
	private String originalIdvFor5Year;
	private String channelCode;
	
	private String cpaCoverIsRequired;
	private boolean noEffectiveDrivingLicense;
	private boolean cpaCoverWithInternalAgent;
	private boolean standalonePAPolicy;
	private String cpaCoverCompanyName;
	private String cpaCoverPolicyNumber;
	private String cpaCoverExpiryDate;

	
	
	private String serviceModelCode;
	private String calculatedModelCode;
	
	public String getOriginalIdvFor1Year() {
		return originalIdvFor1Year;
	}
	public void setOriginalIdvFor1Year(String originalIdvFor1Year) {
		this.originalIdvFor1Year = originalIdvFor1Year;
	}
	public String getOriginalIdvFor2Year() {
		return originalIdvFor2Year;
	}
	public void setOriginalIdvFor2Year(String originalIdvFor2Year) {
		this.originalIdvFor2Year = originalIdvFor2Year;
	}
	public String getOriginalIdvFor3Year() {
		return originalIdvFor3Year;
	}
	public void setOriginalIdvFor3Year(String originalIdvFor3Year) {
		this.originalIdvFor3Year = originalIdvFor3Year;
	}
	public String getTypeOfCover() {
		return typeOfCover;
	}
	public void setTypeOfCover(String typeOfCover) {
		this.typeOfCover = typeOfCover;
	}
	public Date getPreviousQuoteDate() {
		return previousQuoteDate;
	}
	public void setPreviousQuoteDate(Date previousQuoteDate) {
		this.previousQuoteDate = previousQuoteDate;
	}
	public boolean isSevenDaysBreakIn() {
		return isSevenDaysBreakIn;
	}
	public void setSevenDaysBreakIn(boolean isSevenDaysBreakIn) {
		this.isSevenDaysBreakIn = isSevenDaysBreakIn;
	}
	public String getOptedAdditonalCover() {
		return optedAdditonalCover;
	}
	public void setOptedAdditonalCover(String optedAdditonalCover) {
		this.optedAdditonalCover = optedAdditonalCover;
	}
	
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getBusinessStatus() {
		return businessStatus;
	}
	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}
	public String getStrTitle() {
		return strTitle;
	}
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	public String getStrFirstName() {
		return strFirstName;
	}
	public void setStrFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}
	public String getStrLastName() {
		return strLastName;
	}
	public void setStrLastName(String strLastName) {
		this.strLastName = strLastName;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStrEmail() {
		return strEmail;
	}
	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}
	public String getStrStdCode() {
		return strStdCode;
	}
	public void setStrStdCode(String strStdCode) {
		this.strStdCode = strStdCode;
	}
	public String getStrPhoneNo() {
		return strPhoneNo;
	}
	public void setStrPhoneNo(String strPhoneNo) {
		this.strPhoneNo = strPhoneNo;
	}
	public String getStrMobileNo() {
		return strMobileNo;
	}
	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}
	public String getContactAddress1() {
		return contactAddress1;
	}
	public void setContactAddress1(String contactAddress1) {
		this.contactAddress1 = contactAddress1;
	}
	public String getContactAddress2() {
		return contactAddress2;
	}
	public void setContactAddress2(String contactAddress2) {
		this.contactAddress2 = contactAddress2;
	}
	public String getContactAddress3() {
		return contactAddress3;
	}
	public void setContactAddress3(String contactAddress3) {
		this.contactAddress3 = contactAddress3;
	}
	public String getContactAddress4() {
		return contactAddress4;
	}
	public void setContactAddress4(String contactAddress4) {
		this.contactAddress4 = contactAddress4;
	}
	public String getContactCity() {
		return contactCity;
	}
	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}
	public String getContactPincode() {
		return contactPincode;
	}
	public void setContactPincode(String contactPincode) {
		this.contactPincode = contactPincode;
	}
	public String getContactOtherCity() {
		return contactOtherCity;
	}
	public void setContactOtherCity(String contactOtherCity) {
		this.contactOtherCity = contactOtherCity;
	}
	public String getAddressOne() {
		return addressOne;
	}
	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}
	public String getAddressTwo() {
		return addressTwo;
	}
	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}
	public String getAddressThree() {
		return addressThree;
	}
	public void setAddressThree(String addressThree) {
		this.addressThree = addressThree;
	}
	public String getAddressFour() {
		return addressFour;
	}
	public void setAddressFour(String addressFour) {
		this.addressFour = addressFour;
	}
	public String getOtherCity() {
		return otherCity;
	}
	public void setOtherCity(String otherCity) {
		this.otherCity = otherCity;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getNominee_Name() {
		return nominee_Name;
	}
	public void setNominee_Name(String nominee_Name) {
		this.nominee_Name = nominee_Name;
	}
	public String getNominee_Age() {
		return nominee_Age;
	}
	public void setNominee_Age(String nominee_Age) {
		this.nominee_Age = nominee_Age;
	}
	public String getRelationship_with_nominee() {
		return relationship_with_nominee;
	}
	public void setRelationship_with_nominee(String relationship_with_nominee) {
		this.relationship_with_nominee = relationship_with_nominee;
	}
	public String getGuardian_Name() {
		return guardian_Name;
	}
	public void setGuardian_Name(String guardian_Name) {
		this.guardian_Name = guardian_Name;
	}
	public String getGuardian_Age() {
		return guardian_Age;
	}
	public void setGuardian_Age(String guardian_Age) {
		this.guardian_Age = guardian_Age;
	}
	public String getRelationship_with_Guardian() {
		return relationship_with_Guardian;
	}
	public void setRelationship_with_Guardian(String relationship_with_Guardian) {
		this.relationship_with_Guardian = relationship_with_Guardian;
	}
	public String getWsStatus() {
		return wsStatus;
	}
	public void setWsStatus(String wsStatus) {
		this.wsStatus = wsStatus;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSearchEngine() {
		return searchEngine;
	}
	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getAdGroup() {
		return adGroup;
	}
	public void setAdGroup(String adGroup) {
		this.adGroup = adGroup;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSearchVsContent() {
		return searchVsContent;
	}
	public void setSearchVsContent(String searchVsContent) {
		this.searchVsContent = searchVsContent;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getReferralUrl() {
		return referralUrl;
	}
	public void setReferralUrl(String referralUrl) {
		this.referralUrl = referralUrl;
	}
	public String getGclid() {
		return gclid;
	}
	public void setGclid(String gclid) {
		this.gclid = gclid;
	}
	public String getLeadType() {
		return leadType;
	}
	public void setLeadType(String leadType) {
		this.leadType = leadType;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCover_dri_othr_car_ass() {
		return cover_dri_othr_car_ass;
	}
	public void setCover_dri_othr_car_ass(String cover_dri_othr_car_ass) {
		this.cover_dri_othr_car_ass = cover_dri_othr_car_ass;
	}
	public String getVehicleMakeCode() {
		return vehicleMakeCode;
	}
	public void setVehicleMakeCode(String vehicleMakeCode) {
		this.vehicleMakeCode = vehicleMakeCode;
	}
	public String getVehicleMakeId() {
		return vehicleMakeId;
	}
	public void setVehicleMakeId(String vehicleMakeId) {
		this.vehicleMakeId = vehicleMakeId;
	}
	public String getVehicleModelName() {
		return vehicleModelName;
	}
	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}
	
	public String getVehicleRegistrationRegion() {
		return vehicleRegistrationRegion;
	}
	public void setVehicleRegistrationRegion(String vehicleRegistrationRegion) {
		this.vehicleRegistrationRegion = vehicleRegistrationRegion;
	}
	
	public double getPremiumWithOutAddOnCov() {
		return premiumWithOutAddOnCov;
	}
	public void setPremiumWithOutAddOnCov(double premiumWithOutAddOnCov) {
		this.premiumWithOutAddOnCov = premiumWithOutAddOnCov;
	}
	public String getLegalliabilitytoemployees() {
		return legalliabilitytoemployees;
	}
	public void setLegalliabilitytoemployees(String legalliabilitytoemployees) {
		this.legalliabilitytoemployees = legalliabilitytoemployees;
	}
	public String getLegalliabilitytopaiddriver() {
		return legalliabilitytopaiddriver;
	}
	public void setLegalliabilitytopaiddriver(String legalliabilitytopaiddriver) {
		this.legalliabilitytopaiddriver = legalliabilitytopaiddriver;
	}
	public String getPersonalaccidentcoverforunnamedpassengers() {
		return personalaccidentcoverforunnamedpassengers;
	}
	public void setPersonalaccidentcoverforunnamedpassengers(String personalaccidentcoverforunnamedpassengers) {
		this.personalaccidentcoverforunnamedpassengers = personalaccidentcoverforunnamedpassengers;
	}
	public String getAccidentcoverforpaiddriver() {
		return accidentcoverforpaiddriver;
	}
	public void setAccidentcoverforpaiddriver(String accidentcoverforpaiddriver) {
		this.accidentcoverforpaiddriver = accidentcoverforpaiddriver;
	}
	public String getValueofelectricalaccessories() {
		return valueofelectricalaccessories;
	}
	public void setValueofelectricalaccessories(String valueofelectricalaccessories) {
		this.valueofelectricalaccessories = valueofelectricalaccessories;
	}
	public String getValueofnonelectricalaccessories() {
		return valueofnonelectricalaccessories;
	}
	public void setValueofnonelectricalaccessories(String valueofnonelectricalaccessories) {
		this.valueofnonelectricalaccessories = valueofnonelectricalaccessories;
	}
	public String getFibreglass() {
		return fibreglass;
	}
	public void setFibreglass(String fibreglass) {
		this.fibreglass = fibreglass;
	}
	public boolean isAutomobileAssociationMembership() {
		return automobileAssociationMembership;
	}
	public void setAutomobileAssociationMembership(boolean automobileAssociationMembership) {
		this.automobileAssociationMembership = automobileAssociationMembership;
	}
	public String getCarFittedWithAntiTheftDevice() {
		return carFittedWithAntiTheftDevice;
	}
	public void setCarFittedWithAntiTheftDevice(String carFittedWithAntiTheftDevice) {
		this.carFittedWithAntiTheftDevice = carFittedWithAntiTheftDevice;
	}
	public String getVoluntarydeductible() {
		return voluntarydeductible;
	}
	public void setVoluntarydeductible(String voluntarydeductible) {
		this.voluntarydeductible = voluntarydeductible;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getEngineCapacityAmount() {
		return engineCapacityAmount;
	}
	public void setEngineCapacityAmount(String engineCapacityAmount) {
		this.engineCapacityAmount = engineCapacityAmount;
	}
	public String getIsCarFinanced() {
		return isCarFinanced;
	}
	public void setIsCarFinanced(String isCarFinanced) {
		this.isCarFinanced = isCarFinanced;
	}
	public String getIsCarFinancedValue() {
		return isCarFinancedValue;
	}
	public void setIsCarFinancedValue(String isCarFinancedValue) {
		this.isCarFinancedValue = isCarFinancedValue;
	}
	public String getFinancierName() {
		return financierName;
	}
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	public String getOtherSafetyFeatureInstalled() {
		return otherSafetyFeatureInstalled;
	}
	public void setOtherSafetyFeatureInstalled(String otherSafetyFeatureInstalled) {
		this.otherSafetyFeatureInstalled = otherSafetyFeatureInstalled;
	}
	public String getPurposeOfCarUsed() {
		return purposeOfCarUsed;
	}
	public void setPurposeOfCarUsed(String purposeOfCarUsed) {
		this.purposeOfCarUsed = purposeOfCarUsed;
	}
	public String getVehicleMostlyDrivenOn() {
		return vehicleMostlyDrivenOn;
	}
	public void setVehicleMostlyDrivenOn(String vehicleMostlyDrivenOn) {
		this.vehicleMostlyDrivenOn = vehicleMostlyDrivenOn;
	}
	public String getParkingDuringDay() {
		return parkingDuringDay;
	}
	public void setParkingDuringDay(String parkingDuringDay) {
		this.parkingDuringDay = parkingDuringDay;
	}
	public String getParkingDuringNight() {
		return parkingDuringNight;
	}
	public void setParkingDuringNight(String parkingDuringNight) {
		this.parkingDuringNight = parkingDuringNight;
	}
	public String getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}
	public String getIsBreakinInsurance() {
		return isBreakinInsurance;
	}
	public void setIsBreakinInsurance(String isBreakinInsurance) {
		this.isBreakinInsurance = isBreakinInsurance;
	}
	public String getCompanyNameForCar() {
		return companyNameForCar;
	}
	public void setCompanyNameForCar(String companyNameForCar) {
		this.companyNameForCar = companyNameForCar;
	}
	public String getRtoCity() {
		return rtoCity;
	}
	public void setRtoCity(String rtoCity) {
		this.rtoCity = rtoCity;
	}
	public String getRtoState() {
		return rtoState;
	}
	public void setRtoState(String rtoState) {
		this.rtoState = rtoState;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	public String getPreviousPolicyExpiryDateDeclaration() {
		return previousPolicyExpiryDateDeclaration;
	}
	public void setPreviousPolicyExpiryDateDeclaration(String previousPolicyExpiryDateDeclaration) {
		this.previousPolicyExpiryDateDeclaration = previousPolicyExpiryDateDeclaration;
	}
	public String getPreviousPolicyType() {
		return previousPolicyType;
	}
	public void setPreviousPolicyType(String previousPolicyType) {
		this.previousPolicyType = previousPolicyType;
	}
	
	public String getNoClaimBonusPercentinCurrent() {
		return noClaimBonusPercentinCurrent;
	}
	public void setNoClaimBonusPercentinCurrent(String noClaimBonusPercentinCurrent) {
		this.noClaimBonusPercentinCurrent = noClaimBonusPercentinCurrent;
	}
	public String getNoClaimBonusDeclaration() {
		return noClaimBonusDeclaration;
	}
	public void setNoClaimBonusDeclaration(String noClaimBonusDeclaration) {
		this.noClaimBonusDeclaration = noClaimBonusDeclaration;
	}
	public String getPreviousPolicyExpiryDateAndClaimcDeclaration() {
		return previousPolicyExpiryDateAndClaimcDeclaration;
	}
	public void setPreviousPolicyExpiryDateAndClaimcDeclaration(String previousPolicyExpiryDateAndClaimcDeclaration) {
		this.previousPolicyExpiryDateAndClaimcDeclaration = previousPolicyExpiryDateAndClaimcDeclaration;
	}
	public String getPreviuosPolicyNumber() {
		return previuosPolicyNumber;
	}
	public void setPreviuosPolicyNumber(String previuosPolicyNumber) {
		this.previuosPolicyNumber = previuosPolicyNumber;
	}
	public String getPreviousInsurerName() {
		return previousInsurerName;
	}
	public void setPreviousInsurerName(String previousInsurerName) {
		this.previousInsurerName = previousInsurerName;
	}
	public String getPreviousinsurersCorrectAddress() {
		return previousinsurersCorrectAddress;
	}
	public void setPreviousinsurersCorrectAddress(String previousinsurersCorrectAddress) {
		this.previousinsurersCorrectAddress = previousinsurersCorrectAddress;
	}
	public String getPreviousinsurersCity() {
		return previousinsurersCity;
	}
	public void setPreviousinsurersCity(String previousinsurersCity) {
		this.previousinsurersCity = previousinsurersCity;
	}
	public String getPreviousinsurersPincode() {
		return previousinsurersPincode;
	}
	public void setPreviousinsurersPincode(String previousinsurersPincode) {
		this.previousinsurersPincode = previousinsurersPincode;
	}
	public List<ElectronicValues> getElectricalAccessoriesList() {
		return electricalAccessoriesList;
	}
	public void setElectricalAccessoriesList(List<ElectronicValues> electricalAccessoriesList) {
		this.electricalAccessoriesList = electricalAccessoriesList;
	}
	public String[] getElectronicNameOfElectronicAccessories() {
		return electronicNameOfElectronicAccessories;
	}
	public void setElectronicNameOfElectronicAccessories(String[] electronicNameOfElectronicAccessories) {
		this.electronicNameOfElectronicAccessories = electronicNameOfElectronicAccessories;
	}
	public String[] getElectronicMakeModel() {
		return electronicMakeModel;
	}
	public void setElectronicMakeModel(String[] electronicMakeModel) {
		this.electronicMakeModel = electronicMakeModel;
	}
	public String getElectronicValue() {
		return electronicValue;
	}
	public void setElectronicValue(String electronicValue) {
		this.electronicValue = electronicValue;
	}
	public List<NonElectronicValues> getNonElectricalAccessoriesList() {
		return nonElectricalAccessoriesList;
	}
	public void setNonElectricalAccessoriesList(List<NonElectronicValues> nonElectricalAccessoriesList) {
		this.nonElectricalAccessoriesList = nonElectricalAccessoriesList;
	}
	public String[] getNonElectronicNameOfElectronicAccessories() {
		return nonElectronicNameOfElectronicAccessories;
	}
	public void setNonElectronicNameOfElectronicAccessories(String[] nonElectronicNameOfElectronicAccessories) {
		this.nonElectronicNameOfElectronicAccessories = nonElectronicNameOfElectronicAccessories;
	}
	public String[] getNonElectronicMakeModel() {
		return nonElectronicMakeModel;
	}
	public void setNonElectronicMakeModel(String[] nonElectronicMakeModel) {
		this.nonElectronicMakeModel = nonElectronicMakeModel;
	}
	public String getNonElectronicValue() {
		return nonElectronicValue;
	}
	public void setNonElectronicValue(String nonElectronicValue) {
		this.nonElectronicValue = nonElectronicValue;
	}
	public String getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	public String getRoadworthyCondition() {
		return roadworthyCondition;
	}
	public void setRoadworthyCondition(String roadworthyCondition) {
		this.roadworthyCondition = roadworthyCondition;
	}
	public String getClaimAmountReceived() {
		return claimAmountReceived;
	}
	public void setClaimAmountReceived(String claimAmountReceived) {
		this.claimAmountReceived = claimAmountReceived;
	}
	public String getClaimsReported() {
		return claimsReported;
	}
	public void setClaimsReported(String claimsReported) {
		this.claimsReported = claimsReported;
	}
	public String getProposalDate() {
		return proposalDate;
	}
	public void setProposalDate(String proposalDate) {
		this.proposalDate = proposalDate;
	}
	public String getInceptionTime() {
		return inceptionTime;
	}
	public void setInceptionTime(String inceptionTime) {
		this.inceptionTime = inceptionTime;
	}

	public double getAdditionalDiscount() {
		return additionalDiscount;
	}
	public void setAdditionalDiscount(double additionalDiscount) {
		this.additionalDiscount = additionalDiscount;
	}
	public double getTotalDiscount() {
		return totalDiscount;
	}
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	public String getTotalIdv() {
		return totalIdv;
	}
	public void setTotalIdv(String totalIdv) {
		this.totalIdv = totalIdv;
	}
	public double getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}
	public double getEducationCess() {
		return educationCess;
	}
	public void setEducationCess(double educationCess) {
		this.educationCess = educationCess;
	}
	public double getKrishiKalyanCess() {
		return krishiKalyanCess;
	}
	public void setKrishiKalyanCess(double krishiKalyanCess) {
		this.krishiKalyanCess = krishiKalyanCess;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Map<String, String> getPremiumBreakUpMap() {
		return premiumBreakUpMap;
	}
	public void setPremiumBreakUpMap(Map<String, String> premiumBreakUpMap) {
		this.premiumBreakUpMap = premiumBreakUpMap;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public boolean isInsertWS() {
		return insertWS;
	}
	public void setInsertWS(boolean insertWS) {
		this.insertWS = insertWS;
	}
	public String getDiscountIDVPercent1Year() {
		return discountIDVPercent1Year;
	}
	public void setDiscountIDVPercent1Year(String discountIDVPercent1Year) {
		this.discountIDVPercent1Year = discountIDVPercent1Year;
	}
	public String getDiscountIDVPercent2Year() {
		return discountIDVPercent2Year;
	}
	public void setDiscountIDVPercent2Year(String discountIDVPercent2Year) {
		this.discountIDVPercent2Year = discountIDVPercent2Year;
	}
	public String getDiscountIDVPercent3Year() {
		return discountIDVPercent3Year;
	}
	public void setDiscountIDVPercent3Year(String discountIDVPercent3Year) {
		this.discountIDVPercent3Year = discountIDVPercent3Year;
	}
	public String getValueofelectricalaccessoriesforyear2() {
		return valueofelectricalaccessoriesforyear2;
	}
	public void setValueofelectricalaccessoriesforyear2(String valueofelectricalaccessoriesforyear2) {
		this.valueofelectricalaccessoriesforyear2 = valueofelectricalaccessoriesforyear2;
	}
	public String getValueofnonelectricalaccessoriesforyear2() {
		return valueofnonelectricalaccessoriesforyear2;
	}
	public void setValueofnonelectricalaccessoriesforyear2(String valueofnonelectricalaccessoriesforyear2) {
		this.valueofnonelectricalaccessoriesforyear2 = valueofnonelectricalaccessoriesforyear2;
	}
	public String getValueofelectricalaccessoriesforyear3() {
		return valueofelectricalaccessoriesforyear3;
	}
	public void setValueofelectricalaccessoriesforyear3(String valueofelectricalaccessoriesforyear3) {
		this.valueofelectricalaccessoriesforyear3 = valueofelectricalaccessoriesforyear3;
	}
	public String getValueofnonelectricalaccessoriesforyear3() {
		return valueofnonelectricalaccessoriesforyear3;
	}
	public void setValueofnonelectricalaccessoriesforyear3(String valueofnonelectricalaccessoriesforyear3) {
		this.valueofnonelectricalaccessoriesforyear3 = valueofnonelectricalaccessoriesforyear3;
	}
	public Integer getVehicleAgeforyear2() {
		return vehicleAgeforyear2;
	}
	public void setVehicleAgeforyear2(Integer vehicleAgeforyear2) {
		this.vehicleAgeforyear2 = vehicleAgeforyear2;
	}
	public Integer getVehicleAgeforyear3() {
		return vehicleAgeforyear3;
	}
	public void setVehicleAgeforyear3(Integer vehicleAgeforyear3) {
		this.vehicleAgeforyear3 = vehicleAgeforyear3;
	}
	public String getAccidentcoverforpaiddriverforyear2() {
		return accidentcoverforpaiddriverforyear2;
	}
	public void setAccidentcoverforpaiddriverforyear2(String accidentcoverforpaiddriverforyear2) {
		this.accidentcoverforpaiddriverforyear2 = accidentcoverforpaiddriverforyear2;
	}
	public String getAccidentcoverforpaiddriverforyear3() {
		return accidentcoverforpaiddriverforyear3;
	}
	public void setAccidentcoverforpaiddriverforyear3(String accidentcoverforpaiddriverforyear3) {
		this.accidentcoverforpaiddriverforyear3 = accidentcoverforpaiddriverforyear3;
	}
	public String getPersonalaccidentcoverforunnamedpassengersforyear2() {
		return personalaccidentcoverforunnamedpassengersforyear2;
	}
	public void setPersonalaccidentcoverforunnamedpassengersforyear2(
			String personalaccidentcoverforunnamedpassengersforyear2) {
		this.personalaccidentcoverforunnamedpassengersforyear2 = personalaccidentcoverforunnamedpassengersforyear2;
	}
	public String getPersonalaccidentcoverforunnamedpassengersforyear3() {
		return personalaccidentcoverforunnamedpassengersforyear3;
	}
	public void setPersonalaccidentcoverforunnamedpassengersforyear3(
			String personalaccidentcoverforunnamedpassengersforyear3) {
		this.personalaccidentcoverforunnamedpassengersforyear3 = personalaccidentcoverforunnamedpassengersforyear3;
	}
	public double getFemaleDiscountRate() {
		return femaleDiscountRate;
	}
	public void setFemaleDiscountRate(double femaleDiscountRate) {
		this.femaleDiscountRate = femaleDiscountRate;
	}
	public double getNilIntermediationCoverPremium() {
		return nilIntermediationCoverPremium;
	}
	public void setNilIntermediationCoverPremium(double nilIntermediationCoverPremium) {
		this.nilIntermediationCoverPremium = nilIntermediationCoverPremium;
	}
	public double getBulkDealDiscountCoverPremium() {
		return bulkDealDiscountCoverPremium;
	}
	public void setBulkDealDiscountCoverPremium(double bulkDealDiscountCoverPremium) {
		this.bulkDealDiscountCoverPremium = bulkDealDiscountCoverPremium;
	}
	public String getAdditionalTowingChargesCover() {
		return additionalTowingChargesCover;
	}
	public void setAdditionalTowingChargesCover(String additionalTowingChargesCover) {
		this.additionalTowingChargesCover = additionalTowingChargesCover;
	}
	public String getAdditionalTowingChargesCoverforyear2() {
		return additionalTowingChargesCoverforyear2;
	}
	public void setAdditionalTowingChargesCoverforyear2(String additionalTowingChargesCoverforyear2) {
		this.additionalTowingChargesCoverforyear2 = additionalTowingChargesCoverforyear2;
	}
	public String getAdditionalTowingChargesCoverforyear3() {
		return additionalTowingChargesCoverforyear3;
	}
	public void setAdditionalTowingChargesCoverforyear3(String additionalTowingChargesCoverforyear3) {
		this.additionalTowingChargesCoverforyear3 = additionalTowingChargesCoverforyear3;
	}
	public double getAdditionalTowingChargesCoverPremium() {
		return additionalTowingChargesCoverPremium;
	}
	public void setAdditionalTowingChargesCoverPremium(double additionalTowingChargesCoverPremium) {
		this.additionalTowingChargesCoverPremium = additionalTowingChargesCoverPremium;
	}
	public double getRateDiscountPremium() {
		return rateDiscountPremium;
	}
	public void setRateDiscountPremium(double rateDiscountPremium) {
		this.rateDiscountPremium = rateDiscountPremium;
	}
	public double getRateDiscount() {
		return rateDiscount;
	}
	public void setRateDiscount(double rateDiscount) {
		this.rateDiscount = rateDiscount;
	}
	public String getFileRC() {
		return fileRC;
	}
	public void setFileRC(String fileRC) {
		this.fileRC = fileRC;
	}
	public String getFileRN() {
		return fileRN;
	}
	public void setFileRN(String fileRN) {
		this.fileRN = fileRN;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public int getFileRCSize() {
		return fileRCSize;
	}
	public void setFileRCSize(int fileRCSize) {
		this.fileRCSize = fileRCSize;
	}
	public int getFileRNSize() {
		return fileRNSize;
	}
	public void setFileRNSize(int fileRNSize) {
		this.fileRNSize = fileRNSize;
	}
	
	public String getRespType() {
		return respType;
	}
	public void setRespType(String respType) {
		this.respType = respType;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getAddonModel() {
		return addonModel;
	}
	public void setAddonModel(String addonModel) {
		this.addonModel = addonModel;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getIsSameASRegistrationAddress() {
		return isSameASRegistrationAddress;
	}
	public void setIsSameASRegistrationAddress(String isSameASRegistrationAddress) {
		this.isSameASRegistrationAddress = isSameASRegistrationAddress;
	}
	public String getRegPinCode() {
		return regPinCode;
	}
	public void setRegPinCode(String regPinCode) {
		this.regPinCode = regPinCode;
	}
	public String getDiscountidvPercent() {
		return discountidvPercent;
	}
	public void setDiscountidvPercent(String discountidvPercent) {
		this.discountidvPercent = discountidvPercent;
	}
	public String getPaToOwnerDriver() {
		return paToOwnerDriver;
	}
	public void setPaToOwnerDriver(String paToOwnerDriver) {
		this.paToOwnerDriver = paToOwnerDriver;
	}
	public String getOptedDisount() {
		return optedDisount;
	}
	public void setOptedDisount(String optedDisount) {
		this.optedDisount = optedDisount;
	}
	public String getRegOtherCity() {
		return regOtherCity;
	}
	public void setRegOtherCity(String regOtherCity) {
		this.regOtherCity = regOtherCity;
	}
	public String getRegCity() {
		return regCity;
	}
	public void setRegCity(String regCity) {
		this.regCity = regCity;
	}
	public String getTypeOfAction() {
		return typeOfAction;
	}
	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}
	public String getIsUsedCar() {
		return isUsedCar;
	}
	public void setIsUsedCar(String isUsedCar) {
		this.isUsedCar = isUsedCar;
	}
	
	public String getVirNumber() {
		return virNumber;
	}
	public void setVirNumber(String virNumber) {
		this.virNumber = virNumber;
	}
	public String getVehicleInspectionDate() {
		return vehicleInspectionDate;
	}
	public void setVehicleInspectionDate(String vehicleInspectionDate) {
		this.vehicleInspectionDate = vehicleInspectionDate;
	}
	public String getExternalReferenceNumber() {
		return externalReferenceNumber;
	}
	public void setExternalReferenceNumber(String externalReferenceNumber) {
		this.externalReferenceNumber = externalReferenceNumber;
	}
	public String getDiscountIDVPercent4Year() {
		return discountIDVPercent4Year;
	}
	public void setDiscountIDVPercent4Year(String discountIDVPercent4Year) {
		this.discountIDVPercent4Year = discountIDVPercent4Year;
	}
	public String getDiscountIDVPercent5Year() {
		return discountIDVPercent5Year;
	}
	public void setDiscountIDVPercent5Year(String discountIDVPercent5Year) {
		this.discountIDVPercent5Year = discountIDVPercent5Year;
	}
	public Integer getVehicleAgeforyear4() {
		return vehicleAgeforyear4;
	}
	public void setVehicleAgeforyear4(Integer vehicleAgeforyear4) {
		this.vehicleAgeforyear4 = vehicleAgeforyear4;
	}
	public Integer getVehicleAgeforyear5() {
		return vehicleAgeforyear5;
	}
	public void setVehicleAgeforyear5(Integer vehicleAgeforyear5) {
		this.vehicleAgeforyear5 = vehicleAgeforyear5;
	}
	public String getValueofelectricalaccessoriesforyear4() {
		return valueofelectricalaccessoriesforyear4;
	}
	public void setValueofelectricalaccessoriesforyear4(String valueofelectricalaccessoriesforyear4) {
		this.valueofelectricalaccessoriesforyear4 = valueofelectricalaccessoriesforyear4;
	}
	public String getValueofnonelectricalaccessoriesforyear4() {
		return valueofnonelectricalaccessoriesforyear4;
	}
	public void setValueofnonelectricalaccessoriesforyear4(String valueofnonelectricalaccessoriesforyear4) {
		this.valueofnonelectricalaccessoriesforyear4 = valueofnonelectricalaccessoriesforyear4;
	}
	public String getValueofelectricalaccessoriesforyear5() {
		return valueofelectricalaccessoriesforyear5;
	}
	public void setValueofelectricalaccessoriesforyear5(String valueofelectricalaccessoriesforyear5) {
		this.valueofelectricalaccessoriesforyear5 = valueofelectricalaccessoriesforyear5;
	}
	public String getValueofnonelectricalaccessoriesforyear5() {
		return valueofnonelectricalaccessoriesforyear5;
	}
	public void setValueofnonelectricalaccessoriesforyear5(String valueofnonelectricalaccessoriesforyear5) {
		this.valueofnonelectricalaccessoriesforyear5 = valueofnonelectricalaccessoriesforyear5;
	}
	public String getAccidentcoverforpaiddriverforyear4() {
		return accidentcoverforpaiddriverforyear4;
	}
	public void setAccidentcoverforpaiddriverforyear4(String accidentcoverforpaiddriverforyear4) {
		this.accidentcoverforpaiddriverforyear4 = accidentcoverforpaiddriverforyear4;
	}
	public String getAccidentcoverforpaiddriverforyear5() {
		return accidentcoverforpaiddriverforyear5;
	}
	public void setAccidentcoverforpaiddriverforyear5(String accidentcoverforpaiddriverforyear5) {
		this.accidentcoverforpaiddriverforyear5 = accidentcoverforpaiddriverforyear5;
	}
	public String getPersonalaccidentcoverforunnamedpassengersforyear4() {
		return personalaccidentcoverforunnamedpassengersforyear4;
	}
	public void setPersonalaccidentcoverforunnamedpassengersforyear4(
			String personalaccidentcoverforunnamedpassengersforyear4) {
		this.personalaccidentcoverforunnamedpassengersforyear4 = personalaccidentcoverforunnamedpassengersforyear4;
	}
	public String getPersonalaccidentcoverforunnamedpassengersforyear5() {
		return personalaccidentcoverforunnamedpassengersforyear5;
	}
	public void setPersonalaccidentcoverforunnamedpassengersforyear5(
			String personalaccidentcoverforunnamedpassengersforyear5) {
		this.personalaccidentcoverforunnamedpassengersforyear5 = personalaccidentcoverforunnamedpassengersforyear5;
	}
	public String getAdditionalTowingChargesCoverforyear4() {
		return additionalTowingChargesCoverforyear4;
	}
	public void setAdditionalTowingChargesCoverforyear4(String additionalTowingChargesCoverforyear4) {
		this.additionalTowingChargesCoverforyear4 = additionalTowingChargesCoverforyear4;
	}
	public String getAdditionalTowingChargesCoverforyear5() {
		return additionalTowingChargesCoverforyear5;
	}
	public void setAdditionalTowingChargesCoverforyear5(String additionalTowingChargesCoverforyear5) {
		this.additionalTowingChargesCoverforyear5 = additionalTowingChargesCoverforyear5;
	}
	public boolean isEnableCampaignDiscount() {
		return enableCampaignDiscount;
	}
	public void setEnableCampaignDiscount(boolean enableCampaignDiscount) {
		this.enableCampaignDiscount = enableCampaignDiscount;
	}
	
	
	public double getCampaignDiscount() {
		return campaignDiscount;
	}
	public void setCampaignDiscount(double campaignDiscount) {
		this.campaignDiscount = campaignDiscount;
	}
	public Double getCampaignDiscountRequest() {
		return campaignDiscountRequest;
	}
	public void setCampaignDiscountRequest(Double campaignDiscountRequest) {
		this.campaignDiscountRequest = campaignDiscountRequest;
	}

	public Double getTechnicalDiscountFromRequest() {
		return technicalDiscountFromRequest;
	}
	public void setTechnicalDiscountFromRequest(Double technicalDiscountFromRequest) {
		this.technicalDiscountFromRequest = technicalDiscountFromRequest;
	}
	public int getCpaPolicyTerm() {
		return cpaPolicyTerm;
	}
	public void setCpaPolicyTerm(int cpaPolicyTerm) {
		this.cpaPolicyTerm = cpaPolicyTerm;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	public String getCpaCoverIsRequired() {
		return cpaCoverIsRequired;
	}
	public void setCpaCoverIsRequired(String cpaCoverIsRequired) {
		this.cpaCoverIsRequired = cpaCoverIsRequired;
	}
	public boolean isNoEffectiveDrivingLicense() {
		return noEffectiveDrivingLicense;
	}
	public void setNoEffectiveDrivingLicense(boolean noEffectiveDrivingLicense) {
		this.noEffectiveDrivingLicense = noEffectiveDrivingLicense;
	}
	public boolean isCpaCoverWithInternalAgent() {
		return cpaCoverWithInternalAgent;
	}
	public void setCpaCoverWithInternalAgent(boolean cpaCoverWithInternalAgent) {
		this.cpaCoverWithInternalAgent = cpaCoverWithInternalAgent;
	}
	public boolean isStandalonePAPolicy() {
		return standalonePAPolicy;
	}
	public void setStandalonePAPolicy(boolean standalonePAPolicy) {
		this.standalonePAPolicy = standalonePAPolicy;
	}
	public String getCpaCoverCompanyName() {
		return cpaCoverCompanyName;
	}
	public void setCpaCoverCompanyName(String cpaCoverCompanyName) {
		this.cpaCoverCompanyName = cpaCoverCompanyName;
	}
	public String getCpaCoverPolicyNumber() {
		return cpaCoverPolicyNumber;
	}
	public void setCpaCoverPolicyNumber(String cpaCoverPolicyNumber) {
		this.cpaCoverPolicyNumber = cpaCoverPolicyNumber;
	}
	public String getCpaCoverExpiryDate() {
		return cpaCoverExpiryDate;
	}
	public void setCpaCoverExpiryDate(String cpaCoverExpiryDate) {
		this.cpaCoverExpiryDate = cpaCoverExpiryDate;
	}

	
	public String getOriginalIdvFor4Year() {
		return originalIdvFor4Year;
	}
	public void setOriginalIdvFor4Year(String originalIdvFor4Year) {
		this.originalIdvFor4Year = originalIdvFor4Year;
	}
	public String getOriginalIdvFor5Year() {
		return originalIdvFor5Year;
	}
	public void setOriginalIdvFor5Year(String originalIdvFor5Year) {
		this.originalIdvFor5Year = originalIdvFor5Year;
	}
	public String getServiceModelCode() {
		return serviceModelCode;
	}
	public void setServiceModelCode(String serviceModelCode) {
		this.serviceModelCode = serviceModelCode;
	}
	public String getCalculatedModelCode() {
		return calculatedModelCode;
	}
	public void setCalculatedModelCode(String calculatedModelCode) {
		this.calculatedModelCode = calculatedModelCode;
	}
	
}