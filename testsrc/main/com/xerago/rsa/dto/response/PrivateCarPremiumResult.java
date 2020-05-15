package com.xerago.rsa.dto.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PREMIUMDETAILS")
public class PrivateCarPremiumResult extends ApiResponse {

	private String grossPremium;
	private String premiumWithoutCovers;
	private String basicPremiumAndNonElectricalAccessories;
	private String electricalAccessories;
	private String biFuelKit;
	private String fiberGlassTank;
	private String automobileAssociationDiscount;
	private String voluntaryDeductables;
	private String voluntaryDeductable;
	private String noClaimBonus;
	private String depreciationWaiver;
	private String engineProtector;
	private String NCBprotector;
	private String KeyReplacement;
	private String windShieldGlass;
	private String lifeTimeRoadTax;
	private String spareCar;
	private String invoicePriceInsurance;
	private String lossOfBaggage;
	private String totalOdPremium;
	private String basicPremiumIncludingPremiumForTppd;
	private String biFuelKitCng;
	private String personalAccidentBenefits;
	private String underSectionIiiOwnerDriver;
	private String unnamedPassengrs;
	private String paCoverToPaidDriver;
	private String toPaidDrivers;
	private String toEmployeses;
	private String totalLiabilityPremium;
	private String packagePremium;
	private String premium;
	private String quoteId;
	private String servicetax;
	private String ecess;
	private String krishiCess;
	private String policyStartDate;
	private String policyExpiryDate;
	private String versionNo;
	private String status;
	private String clientCode;
	
	private Integer policyTerm;
	private String nilIntermediationCoverPremium;
	private String bulkDealDiscountCoverPremium;
	
	private String additionalTowingChargesCoverPremium;
	
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getAutomobileAssociationDiscount() {
		return automobileAssociationDiscount;
	}

	public String getBasicPremiumAndNonElectricalAccessories() {
		return basicPremiumAndNonElectricalAccessories;
	}

	public String getBasicPremiumIncludingPremiumForTppd() {
		return basicPremiumIncludingPremiumForTppd;
	}

	public String getBiFuelKit() {
		return biFuelKit;
	}

	public String getBiFuelKitCng() {
		return biFuelKitCng;
	}

	public String getDepreciationWaiver() {
		return depreciationWaiver;
	}

	public String getEcess() {
		return ecess;
	}

	public String getElectricalAccessories() {
		return electricalAccessories;
	}

	public String getEngineProtector() {
		return engineProtector;
	}

	public String getFiberGlassTank() {
		return fiberGlassTank;
	}

	public String getGrossPremium() {
		return grossPremium;
	}

	public String getInvoicePriceInsurance() {
		return invoicePriceInsurance;
	}

	public String getLifeTimeRoadTax() {
		return lifeTimeRoadTax;
	}

	public String getLossOfBaggage() {
		return lossOfBaggage;
	}

	public String getNCBprotector() {
		return NCBprotector;
	}

	public String getNoClaimBonus() {
		return noClaimBonus;
	}

	public String getPackagePremium() {
		return packagePremium;
	}

	public String getPaCoverToPaidDriver() {
		return paCoverToPaidDriver;
	}

	public String getPersonalAccidentBenefits() {
		return personalAccidentBenefits;
	}

	public String getPolicyExpiryDate() {
		return policyExpiryDate;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public String getPremium() {
		return premium;
	}

	public String getPremiumWithoutCovers() {
		return premiumWithoutCovers;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getServicetax() {
		return servicetax;
	}

	public String getSpareCar() {
		return spareCar;
	}

	public String getStatus() {
		return status;
	}

	public String getToEmployeses() {
		return toEmployeses;
	}

	public String getToPaidDrivers() {
		return toPaidDrivers;
	}

	public String getTotalLiabilityPremium() {
		return totalLiabilityPremium;
	}

	public String getTotalOdPremium() {
		return totalOdPremium;
	}

	public String getUnderSectionIiiOwnerDriver() {
		return underSectionIiiOwnerDriver;
	}

	public String getUnnamedPassengrs() {
		return unnamedPassengrs;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public String getVoluntaryDeductable() {
		return voluntaryDeductable;
	}

	public String getVoluntaryDeductables() {
		return voluntaryDeductables;
	}

	public String getWindShieldGlass() {
		return windShieldGlass;
	}

	public void setAutomobileAssociationDiscount(String automobileAssociationDiscount) {
		this.automobileAssociationDiscount = automobileAssociationDiscount;
	}

	public void setBasicPremiumAndNonElectricalAccessories(String basicPremiumAndNonElectricalAccessories) {
		this.basicPremiumAndNonElectricalAccessories = basicPremiumAndNonElectricalAccessories;
	}

	public void setBasicPremiumIncludingPremiumForTppd(String basicPremiumIncludingPremiumForTppd) {
		this.basicPremiumIncludingPremiumForTppd = basicPremiumIncludingPremiumForTppd;
	}

	public void setBiFuelKit(String biFuelKit) {
		this.biFuelKit = biFuelKit;
	}

	public void setBiFuelKitCng(String biFuelKitCng) {
		this.biFuelKitCng = biFuelKitCng;
	}

	public void setDepreciationWaiver(String depreciationWaiver) {
		this.depreciationWaiver = depreciationWaiver;
	}

	public void setEcess(String ecess) {
		this.ecess = ecess;
	}

	public void setElectricalAccessories(String electricalAccessories) {
		this.electricalAccessories = electricalAccessories;
	}

	public void setEngineProtector(String engineProtector) {
		this.engineProtector = engineProtector;
	}

	public void setFiberGlassTank(String fiberGlassTank) {
		this.fiberGlassTank = fiberGlassTank;
	}

	public void setGrossPremium(String grossPremium) {
		this.grossPremium = grossPremium;
	}

	public void setInvoicePriceInsurance(String invoicePriceInsurance) {
		this.invoicePriceInsurance = invoicePriceInsurance;
	}

	public void setLifeTimeRoadTax(String lifeTimeRoadTax) {
		this.lifeTimeRoadTax = lifeTimeRoadTax;
	}

	public void setLossOfBaggage(String lossOfBaggage) {
		this.lossOfBaggage = lossOfBaggage;
	}

	public void setNCBprotector(String nCBprotector) {
		NCBprotector = nCBprotector;
	}

	public void setNoClaimBonus(String noClaimBonus) {
		this.noClaimBonus = noClaimBonus;
	}

	public void setPackagePremium(String packagePremium) {
		this.packagePremium = packagePremium;
	}

	public void setPaCoverToPaidDriver(String paCoverToPaidDriver) {
		this.paCoverToPaidDriver = paCoverToPaidDriver;
	}

	public void setPersonalAccidentBenefits(String personalAccidentBenefits) {
		this.personalAccidentBenefits = personalAccidentBenefits;
	}

	public void setPolicyExpiryDate(String policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public void setPremiumWithoutCovers(String premiumWithoutCovers) {
		this.premiumWithoutCovers = premiumWithoutCovers;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public void setServicetax(String servicetax) {
		this.servicetax = servicetax;
	}

	public void setSpareCar(String spareCar) {
		this.spareCar = spareCar;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setToEmployeses(String toEmployeses) {
		this.toEmployeses = toEmployeses;
	}

	public void setToPaidDrivers(String toPaidDrivers) {
		this.toPaidDrivers = toPaidDrivers;
	}

	public void setTotalLiabilityPremium(String totalLiabilityPremium) {
		this.totalLiabilityPremium = totalLiabilityPremium;
	}

	public void setTotalOdPremium(String totalOdPremium) {
		this.totalOdPremium = totalOdPremium;
	}

	public void setUnderSectionIiiOwnerDriver(String underSectionIiiOwnerDriver) {
		this.underSectionIiiOwnerDriver = underSectionIiiOwnerDriver;
	}

	public void setUnnamedPassengrs(String unnamedPassengrs) {
		this.unnamedPassengrs = unnamedPassengrs;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public void setVoluntaryDeductable(String voluntaryDeductable) {
		this.voluntaryDeductable = voluntaryDeductable;
	}

	public void setVoluntaryDeductables(String voluntaryDeductables) {
		this.voluntaryDeductables = voluntaryDeductables;
	}

	public void setWindShieldGlass(String windShieldGlass) {
		this.windShieldGlass = windShieldGlass;
	}

	public String getKeyReplacement() {
		return KeyReplacement;
	}

	public void setKeyReplacement(String keyReplacement) {
		KeyReplacement = keyReplacement;
	}

	public String getKrishiCess() {
		return krishiCess;
	}

	public void setKrishiCess(String krishiCess) {
		this.krishiCess = krishiCess;
	}

	public Integer getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(Integer policyTerm) {
		this.policyTerm = policyTerm;
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

	public String getAdditionalTowingChargesCoverPremium() {
		return additionalTowingChargesCoverPremium;
	}

	public void setAdditionalTowingChargesCoverPremium(String additionalTowingChargesCoverPremium) {
		this.additionalTowingChargesCoverPremium = additionalTowingChargesCoverPremium;
	}

}
