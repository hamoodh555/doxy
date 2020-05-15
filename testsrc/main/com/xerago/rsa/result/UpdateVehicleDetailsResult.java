package com.xerago.rsa.result;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "UpdateVehicleDetails")
public class UpdateVehicleDetailsResult extends Result{

	private String Description;
	private String versionNo;
	private String quoteId;
	private String premium;
	private String clientCode;
	private String email;	
	private PrivateCarPremiumBreakUps premiumBreakUps;
	
	private String policyTerm;

	public PrivateCarPremiumBreakUps getPremiumBreakUps() {
		return premiumBreakUps;
	}

	public void setPremiumBreakUps(PrivateCarPremiumBreakUps premiumBreakUps) {
		this.premiumBreakUps = premiumBreakUps;
	}

	public String getClientCode() {
		return clientCode;
	}

	public String getDescription() {
		return Description;
	}

	public String getPremium() {
		return premium;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(String policyTerm) {
		this.policyTerm = policyTerm;
	}

}
