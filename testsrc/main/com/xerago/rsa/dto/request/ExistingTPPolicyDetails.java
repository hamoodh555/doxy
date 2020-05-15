package com.xerago.rsa.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ExistingTPPolicyDetails {

	@ApiModelProperty(name = "Existing Third Party Policy Insurer Name.", required = false, example = "National Insurance Co. Ltd.")
	private String tpInsurer;

	@ApiModelProperty(name = "Existing Third party policy number.", required = false, example = "RTGRT4523fTRF")
	private String tpPolicyNumber;

	@ApiModelProperty(name = "Existing Third Patry policy start date.", required = false, example = "13/07/2018")
	private String tpInceptionDate;

	@ApiModelProperty(name = "Existing Third Patry policy Expiry Date.", required = false, example = "12/07/2020")
	private String tpExpiryDate;

	@ApiModelProperty(name = "Third party policy term", required = false, example = "1")
	private int tpPolicyTerm;
	
	@ApiModelProperty(name = "Address1", required = true, example = "Asdf Street")
	private String tpAddress1;
	
	@ApiModelProperty(name = "Address2", required = false, example = "Asdf Street")
	private String tpAddress2;
	
	@ApiModelProperty(name = "city name", required = true, example = "CHENNAI")
	private String tpCity;

	@ApiModelProperty(name = "pincode", required = true, example = "600096")
	private String tpPincode;

	public ExistingTPPolicyDetails() {

	}

	public String getTpInsurer() {
		return tpInsurer;
	}

	public void setTpInsurer(String tpInsurer) {
		this.tpInsurer = tpInsurer;
	}

	public String getTpPolicyNumber() {
		return tpPolicyNumber;
	}

	public void setTpPolicyNumber(String tpPolicyNumber) {
		this.tpPolicyNumber = tpPolicyNumber;
	}

	public String getTpInceptionDate() {
		return tpInceptionDate;
	}

	public void setTpInceptionDate(String tpInceptionDate) {
		this.tpInceptionDate = tpInceptionDate;
	}

	public String getTpExpiryDate() {
		return tpExpiryDate;
	}

	public void setTpExpiryDate(String tpExpiryDate) {
		this.tpExpiryDate = tpExpiryDate;
	}

	public int getTpPolicyTerm() {
		return tpPolicyTerm;
	}

	public void setTpPolicyTerm(int tpPolicyTerm) {
		this.tpPolicyTerm = tpPolicyTerm;
	}

	public String getTpAddress1() {
		return tpAddress1;
	}

	public void setTpAddress1(String tpAddress1) {
		this.tpAddress1 = tpAddress1;
	}

	public String getTpAddress2() {
		return tpAddress2;
	}

	public void setTpAddress2(String tpAddress2) {
		this.tpAddress2 = tpAddress2;
	}
	public String getTpCity() {
		return tpCity;
	}

	public void setTpCity(String tpCity) {
		this.tpCity = tpCity;
	}

	public String getTpPincode() {
		return tpPincode;
	}

	public void setTpPincode(String tpPincode) {
		this.tpPincode = tpPincode;
	}

}
