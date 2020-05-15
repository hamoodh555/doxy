package com.xerago.rsa.dto.request;
/**
 * @author roshini
 *  Task #278458 POS DETAILS RS 154761 - TECH
 */
import io.swagger.annotations.ApiModelProperty;

public class PosDetails {

	@ApiModelProperty(name = "name", required= false, example = "posName")
	private String name;
	
	@ApiModelProperty(name = "panNumber", required = false, example = "EHXPK1164B")
	private String pan;
	
	@ApiModelProperty(name = "aadhaar", required = false, example = "213412910874")
	private String aadhaar;
	
	@ApiModelProperty(name = "mobile", required = false, example = "9322332031")
	private String mobile;

	@ApiModelProperty(name = "licenceExpiryDate", required = false, example ="licenceExpiryDate" )
	private String licenceExpiryDate;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAadhaar() {
		return aadhaar;
	}

	public void setAadhaar(String aadhaar) {
		this.aadhaar = aadhaar;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLicenceExpiryDate() {
		return licenceExpiryDate;
	}

	public void setLicenceExpiryDate(String licenceExpiryDate) {
		this.licenceExpiryDate = licenceExpiryDate;
	}
	
	
	
	
	
}
