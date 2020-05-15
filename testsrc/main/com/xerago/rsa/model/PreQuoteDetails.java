package com.xerago.rsa.model;

public class PreQuoteDetails {

	private static final long serialVersionUID = 6929063786102509605L;

	private double grossPremium;
	private double netPremium;
	private double premiumWithoutCovers;
	private String quoteID;
	private double serviceTax;
	private String userID;
	
	public double getGrossPremium() {
		return grossPremium;
	}
	public void setGrossPremium(double grossPremium) {
		this.grossPremium = grossPremium;
	}
	public double getNetPremium() {
		return netPremium;
	}
	public void setNetPremium(double netPremium) {
		this.netPremium = netPremium;
	}
	public double getPremiumWithoutCovers() {
		return premiumWithoutCovers;
	}
	public void setPremiumWithoutCovers(double premiumWithoutCovers) {
		this.premiumWithoutCovers = premiumWithoutCovers;
	}
	public String getQuoteID() {
		return quoteID;
	}
	public void setQuoteID(String quoteID) {
		this.quoteID = quoteID;
	}
	public double getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
}
