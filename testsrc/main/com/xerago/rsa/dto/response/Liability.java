package com.xerago.rsa.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "basicPremiumIncludingPremiumForTppd", "biFuelKitCng", "personalAccidentBenefits",
	"underSectionIIIOwnerDriver", "unnamedPassengrs", "paCoverToPaidDriver", "toPaidDrivers",
	"toEmployeses", "totalLiabilityPremium" })
public class Liability {

	@JsonProperty("BASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD")
	private String basicPremiumIncludingPremiumForTppd;
	
	@JsonProperty("BI_FUEL_KIT_CNG")
	private String biFuelKitCng;
	
	@JsonProperty("PERSONAL_ACCIDENT_BENEFITS")
	private String personalAccidentBenefits;
	
	@JsonProperty("UNDER_SECTION_III_OWNER_DRIVER")
	private String underSectionIIIOwnerDriver;
	
	@JsonProperty("UNNAMED_PASSENGRS")
	private String unnamedPassengrs;
	
	@JsonProperty("PA_COVER_TO_PAID_DRIVER")
	private String paCoverToPaidDriver;
	
	@JsonProperty("TO_PAID_DRIVERS")
	private String toPaidDrivers;
	
	@JsonProperty("TO_EMPLOYESES")
	private String toEmployeses;
	
	@JsonProperty("TOTAL_LIABILITY_PREMIUM")
	private String totalLiabilityPremium;
	
	public String getBasicPremiumIncludingPremiumForTppd() {
		return basicPremiumIncludingPremiumForTppd;
	}
	public void setBasicPremiumIncludingPremiumForTppd(String basicPremiumIncludingPremiumForTppd) {
		this.basicPremiumIncludingPremiumForTppd = basicPremiumIncludingPremiumForTppd;
	}
	public String getBiFuelKitCng() {
		return biFuelKitCng;
	}
	public void setBiFuelKitCng(String biFuelKitCng) {
		this.biFuelKitCng = biFuelKitCng;
	}
	public String getPersonalAccidentBenefits() {
		return personalAccidentBenefits;
	}
	public void setPersonalAccidentBenefits(String personalAccidentBenefits) {
		this.personalAccidentBenefits = personalAccidentBenefits;
	}
	public String getUnderSectionIIIOwnerDriver() {
		return underSectionIIIOwnerDriver;
	}
	public void setUnderSectionIIIOwnerDriver(String underSectionIIIOwnerDriver) {
		this.underSectionIIIOwnerDriver = underSectionIIIOwnerDriver;
	}
	public String getUnnamedPassengrs() {
		return unnamedPassengrs;
	}
	public void setUnnamedPassengrs(String unnamedPassengrs) {
		this.unnamedPassengrs = unnamedPassengrs;
	}
	public String getPaCoverToPaidDriver() {
		return paCoverToPaidDriver;
	}
	public void setPaCoverToPaidDriver(String paCoverToPaidDriver) {
		this.paCoverToPaidDriver = paCoverToPaidDriver;
	}
	public String getToPaidDrivers() {
		return toPaidDrivers;
	}
	public void setToPaidDrivers(String toPaidDrivers) {
		this.toPaidDrivers = toPaidDrivers;
	}
	public String getToEmployeses() {
		return toEmployeses;
	}
	public void setToEmployeses(String toEmployeses) {
		this.toEmployeses = toEmployeses;
	}
	public String getTotalLiabilityPremium() {
		return totalLiabilityPremium;
	}
	public void setTotalLiabilityPremium(String totalLiabilityPremium) {
		this.totalLiabilityPremium = totalLiabilityPremium;
	}

}
