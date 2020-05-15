package com.xerago.rsa.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "basicPremiumAndNonElectricalAccessories", "electricalAccessories", "biFuelKit",
		"fiberGlassTank", "automobileAssociationDiscount", "voluntaryDeductables", "voluntaryDeductable",
		"noClaimBonus", "depreciationWaiver", "engineProtector", "ncbProtector", "windShieldGlass",
		"lifeTimeRoadTax", "spareCar", "invoicePriceInsurance", "lossOfBaggage", "totalOdPremium",
		"keyReplacement", "nilIntermediationCoverPremium", "bulkDealDiscountCoverPremium" })
public class OdPremium {
	
	@JsonProperty("BASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES")
	private String basicPremiumAndNonElectricalAccessories;
	
	@JsonProperty("ELECTRICAL_ACCESSORIES")
	private String electricalAccessories;
	
	@JsonProperty("BI_FUEL_KIT")
	private String biFuelKit;
	
	@JsonProperty("FIBER_GLASS_TANK")
	private String fiberGlassTank;
	
	@JsonProperty("AUTOMOBILE_ASSOCIATION_DISCOUNT")
	private String automobileAssociationDiscount;
	
	@JsonProperty("VOLUNTARY_DEDUCTABLES")
	private String voluntaryDeductables;
	
	@JsonProperty("VOLUNTARY_DEDUCTABLE")
	private String voluntaryDeductable;
	
	@JsonProperty("NO_CLAIM_BONUS")
	private String noClaimBonus;
	
	@JsonProperty("DEPRECIATION_WAIVER")
	private String depreciationWaiver;
	
	@JsonProperty("ENGINE_PROTECTOR")
	private String engineProtector;
	
	@JsonProperty("NCB_PROTECTOR")
	private String ncbProtector;
	
	@JsonProperty("WIND_SHIELD_GLASS")
	private String windShieldGlass;
	
	@JsonProperty("LIFE_TIME_ROAD_TAX")
	private String lifeTimeRoadTax;
	
	@JsonProperty("SPARE_CAR")
	private String spareCar;
	
	@JsonProperty("INVOICE_PRICE_INSURANCE")
	private String invoicePriceInsurance;
	
	@JsonProperty("LOSS_OF_BAGGAGE")
	private String lossOfBaggage;
	
	@JsonProperty("TOTAL_OD_PREMIUM")
	private String totalOdPremium;
	
	@JsonProperty("KEY_REPLACEMENT")
	private String keyReplacement;
	
	@JsonProperty("NIL_INTERMEDIATION_COVER_PREMIUM")
	private String nilIntermediationCoverPremium;
	
	@JsonProperty("BULKDEAL_DISCOUNT_COVER_PREMIUM")
	private String bulkDealDiscountCoverPremium;
	
	@JsonProperty("TOWING_CHARGES_COVER_PREMIUM")
	private String towingChargesCoverPremium;
	
	
	public String getTowingChargesCoverPremium() {
		return towingChargesCoverPremium;
	}
	public void setTowingChargesCoverPremium(String towingChargesCoverPremium) {
		this.towingChargesCoverPremium = towingChargesCoverPremium;
	}
	public String getNilIntermediationCoverPremium() {
		return nilIntermediationCoverPremium;
	}
	public void setNilIntermediationCoverPremium(String nilIntermediationCoverPremium) {
		this.nilIntermediationCoverPremium = nilIntermediationCoverPremium;
	}
	public String getBulkDealDiscountCoverPremium() {
		return bulkDealDiscountCoverPremium;
	}
	public void setBulkDealDiscountCoverPremium(String bulkDealDiscountCoverPremium) {
		this.bulkDealDiscountCoverPremium = bulkDealDiscountCoverPremium;
	}
	public String getBasicPremiumAndNonElectricalAccessories() {
		return basicPremiumAndNonElectricalAccessories;
	}
	public void setBasicPremiumAndNonElectricalAccessories(String basicPremiumAndNonElectricalAccessories) {
		this.basicPremiumAndNonElectricalAccessories = basicPremiumAndNonElectricalAccessories;
	}
	public String getElectricalAccessories() {
		return electricalAccessories;
	}
	public void setElectricalAccessories(String electricalAccessories) {
		this.electricalAccessories = electricalAccessories;
	}
	public String getBiFuelKit() {
		return biFuelKit;
	}
	public void setBiFuelKit(String biFuelKit) {
		this.biFuelKit = biFuelKit;
	}
	public String getFiberGlassTank() {
		return fiberGlassTank;
	}
	public void setFiberGlassTank(String fiberGlassTank) {
		this.fiberGlassTank = fiberGlassTank;
	}
	public String getAutomobileAssociationDiscount() {
		return automobileAssociationDiscount;
	}
	public void setAutomobileAssociationDiscount(String automobileAssociationDiscount) {
		this.automobileAssociationDiscount = automobileAssociationDiscount;
	}
	public String getVoluntaryDeductables() {
		return voluntaryDeductables;
	}
	public void setVoluntaryDeductables(String voluntaryDeductables) {
		this.voluntaryDeductables = voluntaryDeductables;
	}
	public String getVoluntaryDeductable() {
		return voluntaryDeductable;
	}
	public void setVoluntaryDeductable(String voluntaryDeductable) {
		this.voluntaryDeductable = voluntaryDeductable;
	}
	public String getNoClaimBonus() {
		return noClaimBonus;
	}
	public void setNoClaimBonus(String noClaimBonus) {
		this.noClaimBonus = noClaimBonus;
	}
	public String getDepreciationWaiver() {
		return depreciationWaiver;
	}
	public void setDepreciationWaiver(String depreciationWaiver) {
		this.depreciationWaiver = depreciationWaiver;
	}
	public String getEngineProtector() {
		return engineProtector;
	}
	public void setEngineProtector(String engineProtector) {
		this.engineProtector = engineProtector;
	}
	public String getNcbProtector() {
		return ncbProtector;
	}
	public void setNcbProtector(String ncbProtector) {
		this.ncbProtector = ncbProtector;
	}
	public String getWindShieldGlass() {
		return windShieldGlass;
	}
	public void setWindShieldGlass(String windShieldGlass) {
		this.windShieldGlass = windShieldGlass;
	}
	public String getLifeTimeRoadTax() {
		return lifeTimeRoadTax;
	}
	public void setLifeTimeRoadTax(String lifeTimeRoadTax) {
		this.lifeTimeRoadTax = lifeTimeRoadTax;
	}
	public String getSpareCar() {
		return spareCar;
	}
	public void setSpareCar(String spareCar) {
		this.spareCar = spareCar;
	}
	public String getInvoicePriceInsurance() {
		return invoicePriceInsurance;
	}
	public void setInvoicePriceInsurance(String invoicePriceInsurance) {
		this.invoicePriceInsurance = invoicePriceInsurance;
	}
	public String getLossOfBaggage() {
		return lossOfBaggage;
	}
	public void setLossOfBaggage(String lossOfBaggage) {
		this.lossOfBaggage = lossOfBaggage;
	}
	public String getTotalOdPremium() {
		return totalOdPremium;
	}
	public void setTotalOdPremium(String totalOdPremium) {
		this.totalOdPremium = totalOdPremium;
	}
	public String getKeyReplacement() {
		return keyReplacement;
	}
	public void setKeyReplacement(String keyReplacement) {
		this.keyReplacement = keyReplacement;
	}
	
	

}
