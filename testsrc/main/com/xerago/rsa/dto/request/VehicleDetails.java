package com.xerago.rsa.dto.request;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xerago.rsa.bean.validation.groups.VehicleDetailsGroup;

import io.swagger.annotations.ApiModelProperty;

public class VehicleDetails {
	
	@NotNull(groups={VehicleDetailsGroup.class})
	@NotBlank(groups={VehicleDetailsGroup.class}, message= "Please enter your Engine Number")
	@Size(min = 1, max = 30, groups={VehicleDetailsGroup.class})
	@ApiModelProperty(value = "A Engine Number of the vehicle. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "3312312312")
	private String engineNumber;
	
	@NotNull(groups={VehicleDetailsGroup.class})
	@NotBlank(groups={VehicleDetailsGroup.class}, message= "Please enter your Chasis Number")
	@Size(min = 1, max = 30, groups={VehicleDetailsGroup.class})
	@ApiModelProperty(value = "A Chassis Number of the vehicle. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "31823114312")
	private String chassisNumber;
	
	@ApiModelProperty(value = "Is the Vehicle Financed. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "Yes", allowableValues = "Yes, No")
	private String isTwoWheelerFinanced;
	
	@ApiModelProperty(value = "Type of Finance. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "Hypothecation", allowableValues = "Hypothecation, Hire purchase, Lease")
	private String isTwoWheelerFinancedValue;
	
	@ApiModelProperty(value = "Financier Name and Location. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "Reliance")
	@Size(min = 1, max = 50, groups={VehicleDetailsGroup.class})
	private String financierName;
	
	@NotNull(groups={VehicleDetailsGroup.class})
	@NotBlank(groups={VehicleDetailsGroup.class}, message= "Please enter Vehilcle Subline")
	@Size(min = 1, max = 30, groups={VehicleDetailsGroup.class})
	@ApiModelProperty(value = "A value is motorCycle.Applicable for Both BrandNew and RollOver", required = true, example = "motorCycle", allowableValues = "motorCycle")
	private String vehicleSubLine;
	
	@ApiModelProperty(value = "Model code of motorCycle", required = true, example = "ZWTV301")
	private String vehicleModelCode;
	
	@ApiModelProperty(value = "Flexi Plan. Not Required While Get Quote", required = true, example = "Flexi Plan")
	private String planOpted;
	
	@ApiModelProperty(value = "Manufacture Year of the vehicle for Both BrandNew and RollOver", required = true, example = "2017", position=1)
	private String yearOfManufacture;
	
	@ApiModelProperty(value = "Default value is 1. Not Required While Get Quote", required = true, example = "1",hidden=true)
	private String drivingExperience;
	
	@ApiModelProperty(hidden = true)
	private String leadType;
	
	@ApiModelProperty(value = "Modify Voluntary Deductible. Not Required While Get Quote", required = true, example = "500", allowableValues = "0, 500, 750, 1000, 1500, 3000")
	private int voluntaryDeductible;
	
	@ApiModelProperty(value = "Manufacturer Name of Vehicle for Both BrandNew and RollOver", required = true, example = "MAHINDRA")
	private String vehicleManufacturerName;
	
	@ApiModelProperty(value = "A Insured Declared Vehicle value of vehicle", required = true, example = "38433")
	private double idv;
	
	@ApiModelProperty(value = "Discount IDV percent. Not Required While Get Quote", required = false, allowableValues = "-20%, -15%, -10%, -5%, 5%, 10%, 15%, 20%, 25%, 30%", example = "-10")
	private long discountIdvPercent;
	
	@ApiModelProperty(value = "Formula is (idv + ((discountIdvPercent/100) * idv)). Not Required While Get Quote", required = true, example = "30747")
	private double modifiedIdv;
	
	@ApiModelProperty(value = "Vehicle Mostly Driven On.Applicable for Both BrandNew and RollOver", required = true, example = "City roads",hidden=true)
	private String vehicleMostlyDrivenOn;
	
	@ApiModelProperty(value = "Registeration Date of vehicle for Both BrandNew and RollOver", required = true, example = "15/07/2017")
	private String vehicleRegDate;
	
	@ApiModelProperty(value="Allowed Only for RollOver",required = true, example = "Company", allowableValues = "Individual, Company")
	private String vehicleRegisteredInTheNameOf;
	
	@ApiModelProperty(value = "A Model Name of vehicle for Both BrandNew and RollOver", required = true, example = "Pantero Kick Alloy-2 Seater")
	private String modelName;
	
	@ApiModelProperty(required = true, example = "Comprehensive", allowableValues = "Comprehensive" ,value="Not Required While Get Quote")
	private String previousPolicyType; 
	
	@ApiModelProperty(value = "Previous Policy No of existing policy. Not Required While Get Quote", required = true, example = "NGoD18fWoRgy")
	private String previousPolicyNo; 
	
	@ApiModelProperty(value = "Previous Insurer Name of existing policy. Not Required While Get Quote,Applicable for RollOver", required = true, example = "BAJAJ ALLIANZ GENERAL INSURANCE COMPANY LTD")
	private String previousInsurerName;
	
	@ApiModelProperty(value = "A Registeration Number of the vehicle. Not Required While Get Quote", required = true, example = "TN 01 AB 1234")
	private String registrationNumber; 
	
	@ApiModelProperty(value = "For Rollover TwoWheeler value is true and for Brandnew TwoWheeler is false", required = true, example = "false", allowableValues = "true, false")
	private String isPreviousPolicyHolder;
	
	@ApiModelProperty(value = "Previous Policy Expiry Date of existing policy", required = true, example = "15/07/2018")
	private String previousPolicyExpiryDate;
	
	@ApiModelProperty(value = "For Rollover, a value is 'RolloverTwoWheeler'. For Brandnew, a value is BrandNewTwoWheeler", required = true, example = "RolloverTwoWheeler")
	private String productName;
	
	@ApiModelProperty(value = "If Vehicle Registered in Name of 'Company' then its Required. Not Required While Get Quote", required = false, example = "xyz")
	@Size(min = 1, max = 50, groups={VehicleDetailsGroup.class})
	private String companyNameForCar;
	
	@ApiModelProperty(value = "Fuel Type of vehicle. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "Petrol")
	private String fuelType;
	
	@ApiModelProperty(value = "A region of car registeration city", required = true, example = "South Region")
	private String region;
	
	@ApiModelProperty(value = "Vehicle registeration city for Both BrandNew and RollOver", required = true, example = "CHENNAI", position=2)
	private String carRegisteredCity;
	
	@ApiModelProperty(value = "", required = false, example = "", hidden = true)
	private String searchEngine;
	
	@ApiModelProperty(value = "", required = false, example = "", hidden = true)
	private String isProductCheck;
	
	@ApiModelProperty(value = "Engine Capacity", required = true, example = "125 CC")
	private String engineCapacityAmount;
	
	@ApiModelProperty(value = "Personal Accident Cover for Unnamed Passengers. Not Required While Get Quote", required = true, example = "50000", allowableValues = "30000, 50000, 100000")
	private String personalAccidentCoverForUnnamedPassengers;
	
	@ApiModelProperty(value = "Accident Cover for Paid Driver. Not Required While Get Quote", required = true, example = "50000", allowableValues = "50000 and 100000")
	private String accidentCoverForPaidDriver;
	
	@ApiModelProperty(value = "Legal Liability to be Paid to Driver. Not Required While Get Quote", required = true, example = "Yes", allowableValues = "Yes, No")
	private String legalliabilityToPaidDriver;
	
	@ApiModelProperty(value = "Legal Liability to be Paid to Employee. If vehicle registered in the name of Company then only you can opt this cover. Not Required While Get Quote", required = true, example = "Yes", allowableValues = "Yes, No")
	private String legalliabilityToEmployees;
	
	@ApiModelProperty(value = "Policy Start Date of policy. Not Required While Get Quote", required = true, example = "30/12/2017", hidden = true)
	private String policyStartDate;
	
	@ApiModelProperty(value = "Vehicle Details. Not Required While Get Quote", required = true, example = "")
	private NonElectricalAccessories nonElectricalAccesories;
	
	@ApiModelProperty(value = "A Array of ElectricalAccessories. Not Required While Get Quote", required = true, example = "")
	private ElectricalAccessories electricalAccessories;
	
	/** Start myInsurance club two wheeler - 1/7/16 - **/
	@ApiModelProperty(value="GetQuote Only for RollOver",required = true, example = "No", allowableValues = "No")
	private String claimsMadeInPreviousPolicy;
	
	@ApiModelProperty(value = "If ncbprevious is 0 then 1, 2-20, 3-25, 4-35, 5-45, 6-50 GetQuote Only for RollOver", required = true, example = "6")
	private String noClaimBonusPercent;
	
	@ApiModelProperty(value = "A value of current year NCB. If ncbprevious is 0 then current year is 20(0-20), 20-25, 25-35, 35-45, 45-50, 50-50", required = true, example = "50")
	private String ncbcurrent;

	@ApiModelProperty(value = "If claimsMadeInPreviousPolicy is Yes then value is required. Not Required While Get Quote,Only for RollOver", required = false, example = "1000")
	private String claimAmountReceived;
	
	@ApiModelProperty(value = "If claimsMadeInPreviousPolicy is Yes then value is required. Not Required While Get Quote,,Only for RollOver", required = true, example = "0", allowableValues = "1, 2, 3, 4, 5")
	private String claimsReported;
	
	@ApiModelProperty(required = true, example = "40", allowableValues = "0, 20, 25, 35, 45, 50")
	private String ncbprevious;
	
	@ApiModelProperty(value = "Was the Two Wheeler ownership changed in the past 12 months?.Applicable for RollOver", required = true, example = "No", allowableValues = "Yes, No")
	private String vechileOwnerShipChanged;
	/** End myInsurance club two wheeler - 1/7/16 - **/
	
	// Insta Policy - tag missing in request 18/08/2016
	@ApiModelProperty(required = false, example = "Yes", allowableValues = "Yes, No", value="Not Required While Get Quote")
	private String cover_non_elec_acc;
	
	@ApiModelProperty(required = false, example = "Yes", allowableValues = "Yes, No", value="Not Required While Get Quote")
	private String cover_elec_acc;
	/***********/
	
	@ApiModelProperty(value = "If its blank then value is considered as 1", required = true, example = "3", allowableValues = "1, 2, 3")
	private Integer policyTerm;
	
	@ApiModelProperty(value = "A Insured Declared Vehicle value of vehicle if policy term is more than 2 Years. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "33629")
	private double idvFor2Year;
	
	@ApiModelProperty(value = "If PolicyTerm is more than 2 Years then value is required. Not Required While Get QuoteApplicable for RollOver", required = false, allowableValues = "-20%, -15%, -10%, -5%, 5%, 10%, 15%, 20%, 25%, 30%", example = "-10")
	private long discountIDVPercent2Year;
	
	@ApiModelProperty(value = "If PolicyTerm is more than 2 Years then value is required. Formula is (idvFor2Year + ((discountIDVPercent2Year/100) * idvFor2Year)). Not Required While Get Quote.Applicable for RollOver", required = true, example = "26904")
	private double modifiedIDVfor2Year;
	
	@ApiModelProperty(value = "A Insured Declared Vehicle value of vehicle if policy term is 3 Years. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "28825")
	private double idvFor3Year;
	
	@ApiModelProperty(value = "If PolicyTerm is 3 Years then value is required. Not Required While Get QuoteApplicable for RollOver", required = false, allowableValues = "-20%, -15%, -10%, -5%, 5%, 10%, 15%, 20%, 25%, 30%", example = "-10")
	private long discountIDVPercent3Year;
	
	@ApiModelProperty(value = "If PolicyTerm is 3 Years then value is required. Formula is (idvFor3Year + ((discountIDVPercent3Year/100) * idvFor3Year)). Not Required While Get Quote.Applicable for RollOver", required = true, example = "23060")
	private double modifiedIDVfor3Year;
	
	@ApiModelProperty(value = "A Insured Declared Vehicle value of vehicle if policy term is 4 Years. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "28825")
	private double idvFor4Year;
	
	@ApiModelProperty(value = "If PolicyTerm is 4 Years then value is required. Not Required While Get QuoteApplicable for RollOver", required = false, allowableValues = "-20%, -15%, -10%, -5%, 5%, 10%, 15%, 20%, 25%, 30%", example = "-10")
	private long discountIDVPercent4Year;
	
	@ApiModelProperty(value = "If PolicyTerm is 4 Years then value is required. Formula is (idvFor4Year + ((discountIDVPercent4Year/100) * idvFor4Year)). Not Required While Get Quote.Applicable for RollOver", required = true, example = "23060")
	private double modifiedIDVfor4Year;
	
	@ApiModelProperty(value = "A Insured Declared Vehicle value of vehicle if policy term is 5 Years. Not Required While Get Quote.Applicable for Both BrandNew and RollOver", required = true, example = "28825")
	private double idvFor5Year;
	
	@ApiModelProperty(value = "If PolicyTerm is 5 Years then value is required. Not Required While Get QuoteApplicable for RollOver", required = false, allowableValues = "-20%, -15%, -10%, -5%, 5%, 10%, 15%, 20%, 25%, 30%", example = "-10")
	private long discountIDVPercent5Year;
	
	@ApiModelProperty(value = "If PolicyTerm is 5 Years then value is required. Formula is (idvFor5Year + ((discountIDVPercent5Year/100) * idvFor4Year)). Not Required While Get Quote.Applicable for RollOver", required = true, example = "23060")
	private double modifiedIDVfor5Year;
	
		
	@ApiModelProperty(value = "The Possible Values are Yes/No. Not Required While Get Quote", required = true, example = "Yes")
	private String towingChargesCover;
	
	@ApiModelProperty(value = "If 'towingChargesCover' field is 'Yes' the field is required. . Not Required While Get Quote", required = false, example = "300")
	private String towingChargesCover_SI;
	
	@ApiModelProperty(value = "If 'isVehicleInspected' field is 'Yes' or 'No' the field is required.",example = "Yes")
	private String isVehicleInspected;
	
	@ApiModelProperty(value="VehicleInspectionDate is for BreakinInsurance.",example="22/04/2017 04:00:00")
	private String vehicleInspectionDate;
	
	@ApiModelProperty(value="IsBreakinInsurance.",example="")
	private String isBreakinInsurance;

	@JsonProperty("VIRNumber")
	private String virNumber;

	@ApiModelProperty(value = "Type of Cover ", example = "LiabilityOnly")
	private String typeOfCover;
	
	@ApiModelProperty(value = "campaignDiscount", example = "0.5")
	private Double campaignDiscount;
	
	@ApiModelProperty(value = "technicalDiscount", example = "10", required = false)
	private Double technicalDiscount;
	
	@ApiModelProperty(value = "cpaPolicyTerm", example = "1")
	private int cpaPolicyTerm;
	
	@ApiModelProperty(value="cpaCoverisRequired",example="YES")
	private String cpaCoverisRequired;
	
	@ApiModelProperty(value = "only for CPACoverDetails details")
	private CPACoverDetails cpaCoverDetails;

	@ApiModelProperty(value = "only for CPACoverDetails details, Use Integer or Decimal Value only", example = "1500000")
	private Double cpaSumInsured;

	public String getTypeOfCover() {
		return typeOfCover;
	}
	public void setTypeOfCover(String typeOfCover) {
		this.typeOfCover = typeOfCover;
	}
	public String getVirNumber() {
		return virNumber;
	}
	public void setVirNumber(String virNumber) {
		this.virNumber = virNumber;
	}
	public String getIsVehicleInspected() {
		return isVehicleInspected;
	}
	public void setIsVehicleInspected(String isVehicleInspected) {
		this.isVehicleInspected = isVehicleInspected;
	}
	public String getVehicleInspectionDate() {
		return vehicleInspectionDate;
	}
	public void setVehicleInspectionDate(String vehicleInspectionDate) {
		this.vehicleInspectionDate = vehicleInspectionDate;
	}
	public String getIsBreakinInsurance() {
		return isBreakinInsurance;
	}
	public void setIsBreakinInsurance(String isBreakinInsurance) {
		this.isBreakinInsurance = isBreakinInsurance;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getPreviousPolicyType() {
		return previousPolicyType;
	}
	public void setPreviousPolicyType(String previousPolicyType) {
		this.previousPolicyType = previousPolicyType;
	}

	public String getPreviousPolicyNo() {
		return previousPolicyNo;
	}
	public void setPreviousPolicyNo(String previousPolicyNo) {
		this.previousPolicyNo = previousPolicyNo;
	}
	public String getPreviousInsurerName() {
		return previousInsurerName;
	}
	public void setPreviousInsurerName(String previousInsurerName) {
		this.previousInsurerName = previousInsurerName;
	}
	public String getPreviousPolicyExpiryDate() {
		return previousPolicyExpiryDate;
	}
	public void setPreviousPolicyExpiryDate(String previousPolicyExpiryDate) {
		this.previousPolicyExpiryDate = previousPolicyExpiryDate;
	}
	public String getVehicleModelCode() {
		return vehicleModelCode;
	}
	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}
	public String getPlanOpted() {
		return planOpted;
	}
	public void setPlanOpted(String planOpted) {
		this.planOpted = planOpted;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}
	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	public String getYearOfManufacture() {
		return yearOfManufacture;
	}
	public void setYearOfManufacture(String yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}
	public String getDrivingExperience() {
		return drivingExperience;
	}
	public void setDrivingExperience(String drivingExperience) {
		this.drivingExperience = drivingExperience;
	}
	public int getVoluntaryDeductible() {
		return voluntaryDeductible;
	}
	public void setVoluntaryDeductible(int voluntaryDeductible) {
		this.voluntaryDeductible = voluntaryDeductible;
	}
	public String getVehicleManufacturerName() {
		return vehicleManufacturerName;
	}
	public void setVehicleManufacturerName(String vehicleManufacturerName) {
		this.vehicleManufacturerName = vehicleManufacturerName;
	}
	public double getIdv() {
		return idv;
	}
	public void setIdv(double idv) {
		this.idv = idv;
	}
	public String getVehicleMostlyDrivenOn() {
		return vehicleMostlyDrivenOn;
	}
	public void setVehicleMostlyDrivenOn(String vehicleMostlyDrivenOn) {
		this.vehicleMostlyDrivenOn = vehicleMostlyDrivenOn;
	}
	public String getVehicleRegDate() {
		return vehicleRegDate;
	}
	public void setVehicleRegDate(String vehicleRegDate) {
		this.vehicleRegDate = vehicleRegDate;
	}
	public String getVehicleRegisteredInTheNameOf() {
		return vehicleRegisteredInTheNameOf;
	}
	public void setVehicleRegisteredInTheNameOf(String vehicleRegisteredInTheNameOf) {
		this.vehicleRegisteredInTheNameOf = vehicleRegisteredInTheNameOf;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getIsPreviousPolicyHolder() {
		return isPreviousPolicyHolder;
	}
	public void setIsPreviousPolicyHolder(String isPreviousPolicyHolder) {
		this.isPreviousPolicyHolder = isPreviousPolicyHolder;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCompanyNameForCar() {
		return companyNameForCar;
	}
	public void setCompanyNameForCar(String companyNameForCar) {
		this.companyNameForCar = companyNameForCar;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public String getChassisNumber() {
		return chassisNumber;
	}
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}
	public String getIsTwoWheelerFinanced() {
		return isTwoWheelerFinanced;
	}
	public void setIsTwoWheelerFinanced(String isTwoWheelerFinanced) {
		this.isTwoWheelerFinanced = isTwoWheelerFinanced;
	}
	public String getIsTwoWheelerFinancedValue() {
		return isTwoWheelerFinancedValue;
	}
	public void setIsTwoWheelerFinancedValue(String isTwoWheelerFinancedValue) {
		this.isTwoWheelerFinancedValue = isTwoWheelerFinancedValue;
	}
	public String getFinancierName() {
		return financierName;
	}
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	public String getVehicleSubLine() {
		return vehicleSubLine;
	}
	public void setVehicleSubLine(String vehicleSubLine) {
		this.vehicleSubLine = vehicleSubLine;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}


	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCarRegisteredCity() {
		return carRegisteredCity;
	}
	public void setCarRegisteredCity(String carRegisteredCity) {
		this.carRegisteredCity = carRegisteredCity;
	}

	public String getSearchEngine() {
		return searchEngine;
	}
	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}
	public String getIsProductCheck() {
		return isProductCheck;
	}
	public void setIsProductCheck(String isProductCheck) {
		this.isProductCheck = isProductCheck;
	}
	public String getEngineCapacityAmount() {
		return engineCapacityAmount;
	}
	public void setEngineCapacityAmount(String engineCapacityAmount) {
		this.engineCapacityAmount = engineCapacityAmount;
	}
	public String getPersonalAccidentCoverForUnnamedPassengers() {
		return personalAccidentCoverForUnnamedPassengers;
	}
	public void setPersonalAccidentCoverForUnnamedPassengers(String personalAccidentCoverForUnnamedPassengers) {
		this.personalAccidentCoverForUnnamedPassengers = personalAccidentCoverForUnnamedPassengers;
	}
	public String getAccidentCoverForPaidDriver() {
		return accidentCoverForPaidDriver;
	}
	public void setAccidentCoverForPaidDriver(String accidentCoverForPaidDriver) {
		this.accidentCoverForPaidDriver = accidentCoverForPaidDriver;
	}
	public String getLegalliabilityToPaidDriver() {
		return legalliabilityToPaidDriver;
	}
	public void setLegalliabilityToPaidDriver(String legalliabilityToPaidDriver) {
		this.legalliabilityToPaidDriver = legalliabilityToPaidDriver;
	}
	public String getLegalliabilityToEmployees() {
		return legalliabilityToEmployees;
	}
	public void setLegalliabilityToEmployees(String legalliabilityToEmployees) {
		this.legalliabilityToEmployees = legalliabilityToEmployees;
	}
	public String getLeadType() {
		return leadType;
	}
	public void setLeadType(String leadType) {
		this.leadType = leadType;
	}
	public NonElectricalAccessories getNonElectricalAccesories() {
		return nonElectricalAccesories;
	}
	public void setNonElectricalAccesories(NonElectricalAccessories nonElectricalAccesories) {
		this.nonElectricalAccesories = nonElectricalAccesories;
	}
	public ElectricalAccessories getElectricalAccessories() {
		return electricalAccessories;
	}
	public void setElectricalAccessories(ElectricalAccessories electricalAccessories) {
		this.electricalAccessories = electricalAccessories;
	}
	/** Start myInsurance club two wheeler - 1/7/16 - **/
	public String getClaimsMadeInPreviousPolicy() {
		return claimsMadeInPreviousPolicy;
	}
	public void setClaimsMadeInPreviousPolicy(String claimsMadeInPreviousPolicy) {
		this.claimsMadeInPreviousPolicy = claimsMadeInPreviousPolicy;
	}
	public String getNoClaimBonusPercent() {
		return noClaimBonusPercent;
	}
	public void setNoClaimBonusPercent(String noClaimBonusPercent) {
		this.noClaimBonusPercent = noClaimBonusPercent;
	}
	public String getNcbcurrent() {
		return ncbcurrent;
	}
	public void setNcbcurrent(String ncbcurrent) {
		this.ncbcurrent = ncbcurrent;
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
	public String getNcbprevious() {
		return ncbprevious;
	}
	public void setNcbprevious(String ncbprevious) {
		this.ncbprevious = ncbprevious;
	}
	public String getVechileOwnerShipChanged() {
		return vechileOwnerShipChanged;
	}
	public void setVechileOwnerShipChanged(String vechileOwnerShipChanged) {
		this.vechileOwnerShipChanged = vechileOwnerShipChanged;
	}

	/** End myInsurance club two wheeler - 1/7/16 - **/

	// Insta Policy - tag missing in request 18/08/2016
	public String getCover_non_elec_acc() {
		return cover_non_elec_acc;
	}
	public void setCover_non_elec_acc(String cover_non_elec_acc) {
		this.cover_non_elec_acc = cover_non_elec_acc;
	}
	public String getCover_elec_acc() {
		return cover_elec_acc;
	}
	public void setCover_elec_acc(String cover_elec_acc) {
		this.cover_elec_acc = cover_elec_acc;
	}
	public Integer getPolicyTerm() {
		return policyTerm;
	}
	public void setPolicyTerm(Integer policyTerm) {
		this.policyTerm = policyTerm;
	}
	public double getIdvFor2Year() {
		return idvFor2Year;
	}
	public void setIdvFor2Year(double idvFor2Year) {
		this.idvFor2Year = idvFor2Year;
	}
	public long getDiscountIDVPercent2Year() {
		return discountIDVPercent2Year;
	}
	public void setDiscountIDVPercent2Year(long discountIDVPercent2Year) {
		this.discountIDVPercent2Year = discountIDVPercent2Year;
	}
	public double getModifiedIDVfor2Year() {
		return modifiedIDVfor2Year;
	}
	public void setModifiedIDVfor2Year(double modifiedIDVfor2Year) {
		this.modifiedIDVfor2Year = modifiedIDVfor2Year;
	}
	public double getIdvFor3Year() {
		return idvFor3Year;
	}
	public void setIdvFor3Year(double idvFor3Year) {
		this.idvFor3Year = idvFor3Year;
	}
	public long getDiscountIDVPercent3Year() {
		return discountIDVPercent3Year;
	}
	public void setDiscountIDVPercent3Year(long discountIDVPercent3Year) {
		this.discountIDVPercent3Year = discountIDVPercent3Year;
	}
	public double getModifiedIDVfor3Year() {
		return modifiedIDVfor3Year;
	}
	public void setModifiedIDVfor3Year(double modifiedIDVfor3Year) {
		this.modifiedIDVfor3Year = modifiedIDVfor3Year;
	}
	public String getTowingChargesCover() {
		return towingChargesCover;
	}
	public void setTowingChargesCover(String towingChargesCover) {
		this.towingChargesCover = towingChargesCover;
	}
	public String getTowingChargesCover_SI() {
		return towingChargesCover_SI;
	}
	public void setTowingChargesCover_SI(String towingChargesCover_SI) {
		this.towingChargesCover_SI = towingChargesCover_SI;
	}
	/*******************/
	public long getDiscountIdvPercent() {
		return discountIdvPercent;
	}
	public void setDiscountIdvPercent(long discountIdvPercent) {
		this.discountIdvPercent = discountIdvPercent;
	}
	public double getModifiedIdv() {
		return modifiedIdv;
	}
	public void setModifiedIdv(double modifiedIdv) {
		this.modifiedIdv = modifiedIdv;
	}
	public double getIdvFor4Year() {
		return idvFor4Year;
	}
	public void setIdvFor4Year(double idvFor4Year) {
		this.idvFor4Year = idvFor4Year;
	}
	public long getDiscountIDVPercent4Year() {
		return discountIDVPercent4Year;
	}
	public void setDiscountIDVPercent4Year(long discountIDVPercent4Year) {
		this.discountIDVPercent4Year = discountIDVPercent4Year;
	}
	public double getModifiedIDVfor4Year() {
		return modifiedIDVfor4Year;
	}
	public void setModifiedIDVfor4Year(double modifiedIDVfor4Year) {
		this.modifiedIDVfor4Year = modifiedIDVfor4Year;
	}
	public double getIdvFor5Year() {
		return idvFor5Year;
	}
	public void setIdvFor5Year(double idvFor5Year) {
		this.idvFor5Year = idvFor5Year;
	}
	public long getDiscountIDVPercent5Year() {
		return discountIDVPercent5Year;
	}
	public void setDiscountIDVPercent5Year(long discountIDVPercent5Year) {
		this.discountIDVPercent5Year = discountIDVPercent5Year;
	}
	public double getModifiedIDVfor5Year() {
		return modifiedIDVfor5Year;
	}
	public void setModifiedIDVfor5Year(double modifiedIDVfor5Year) {
		this.modifiedIDVfor5Year = modifiedIDVfor5Year;
	}
	public Double getCampaignDiscount() {
		return campaignDiscount;
	}
	public void setCampaignDiscount(Double campaignDiscount) {
		this.campaignDiscount = campaignDiscount;
	}

	public Double getTechnicalDiscount() {
		return technicalDiscount;
	}
	public void setTechnicalDiscount(Double technicalDiscount) {
		this.technicalDiscount = technicalDiscount;
	}
	public int getCpaPolicyTerm() {
		return cpaPolicyTerm;
	}
	public void setCpaPolicyTerm(int cpaPolicyTerm) {
		this.cpaPolicyTerm = cpaPolicyTerm;
	}
	public String getCpaCoverIsRequired() {
		return cpaCoverisRequired;
	}
	public void setCpaCoverisRequired(String cpaCoverIsRequired) {
		this.cpaCoverisRequired = cpaCoverIsRequired;
	}
	public CPACoverDetails getCpaCoverDetails() {
		return cpaCoverDetails;
	}
	public void setCpaCoverDetails(CPACoverDetails cpaCoverDetails) {
		this.cpaCoverDetails = cpaCoverDetails;
	}

	public String getCpaCoverisRequired() {
		return cpaCoverisRequired;
	}

	public Double getCpaSumInsured() {
		return cpaSumInsured;
	}

	public void setCpaSumInsured(Double cpaSumInsured) {
		this.cpaSumInsured = cpaSumInsured;
	}
}
