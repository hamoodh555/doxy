package com.xerago.rsa.dto.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PREMIUMDETAILS")
public class GetQuoteResult extends ApiResponse {

	private String quoteId;
	private double premium;
	private double grossPremium;
	private double premiumwithoutcover;
	private String policyStartDate;
	private String policyExpiryDate;
	private double serviceTax;
	private double tpaCharges;
	private double familyDiscount;
	private String userId;
	
	private double premiumfor2Year;
	private double premiumfor3Year;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getTpaCharges() {
		return tpaCharges;
	}

	public void setTpaCharges(double tpaCharges) {
		this.tpaCharges = tpaCharges;
	}

	public double getFamilyDiscount() {
		return familyDiscount;
	}

	public void setFamilyDiscount(double familyDiscount) {
		this.familyDiscount = familyDiscount;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPolicyExpiryDate() {
		return policyExpiryDate;
	}

	public void setPolicyExpiryDate(String policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}

	public double getPremiumwithoutcover() {
		return premiumwithoutcover;
	}

	public void setPremiumwithoutcover(double premiumwithoutcover) {
		this.premiumwithoutcover = premiumwithoutcover;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public double getPremium() {
		return premium;
	}

	public void setPremium(double premium) {
		this.premium = premium;

	}

	public double getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(double grossPremium) {
		this.grossPremium = grossPremium;

	}

	public double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}

	public double getPremiumfor2Year() {
		return premiumfor2Year;
	}

	public void setPremiumfor2Year(double premiumfor2Year) {
		this.premiumfor2Year = premiumfor2Year;
	}

	public double getPremiumfor3Year() {
		return premiumfor3Year;
	}

	public void setPremiumfor3Year(double premiumfor3Year) {
		this.premiumfor3Year = premiumfor3Year;
	}
}
