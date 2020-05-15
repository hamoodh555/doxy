package com.xerago.rsa.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {
	
	public Status() {
	}
	
	@JsonProperty("StatusCode")
	private String statusCode;
	
	@JsonProperty("Message")
	private String message;
	
	private BigDecimal PremiumPaid;
    private String status;
    private String policyNumber;
    private String buyDate;
    private String policyInceptionDate;
	private String policyExpiryDate;
	private String policyDownloadLink;
	private String quotenumber;
	 
	public String getQuotenumber() {
		return quotenumber;
	}
	public void setQuotenumber(String quotenumber) {
		this.quotenumber = quotenumber;
	}
	public BigDecimal getPremiumPaid() {
		return PremiumPaid;
	}
	public void setPremiumPaid(BigDecimal premiumPaid) {
		PremiumPaid = premiumPaid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getPolicyInceptionDate() {
		return policyInceptionDate;
	}
	public void setPolicyInceptionDate(String policyInceptionDate) {
		this.policyInceptionDate = policyInceptionDate;
	}
	public String getPolicyExpiryDate() {
		return policyExpiryDate;
	}
	public void setPolicyExpiryDate(String policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}
	public String getPolicyDownloadLink() {
		return policyDownloadLink;
	}
	public void setPolicyDownloadLink(String policyDownloadLink) {
		this.policyDownloadLink = policyDownloadLink;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	 
	 

}
