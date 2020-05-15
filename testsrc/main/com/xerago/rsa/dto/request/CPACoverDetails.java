package com.xerago.rsa.dto.request;
/**
 * @author roshini
 * @since 28-12-2018
 * Request Class for CPA Cover Option
 */
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "cpaCoverDetails")
@XmlType
	(
	propOrder = 
			{
				"noEffectiveDrivingLicense",
				"cpaCoverWithInternalAgent",
				"standalonePAPolicy",
				"companyName",
				"policyNumber",
				"expiryDate",
			}
	)
public class CPACoverDetails {
	
	
	@ApiModelProperty(name = "noEffectiveDrivingLicense", example = "false", required = false)
	private boolean noEffectiveDrivingLicense;
	
	@ApiModelProperty(name = "cpaCoverWithInternalAgent",  example = "false", required = false )
	private boolean cpaCoverWithInternalAgent;
	
	@ApiModelProperty(name = "standalonePAPolicy",  example = "false", required = false )
	private boolean standalonePAPolicy;
	
	@ApiModelProperty(name = "companyName", example = "Royal Sundaram General Insurance Pvt Ltd.", required = false )
	private String companyName;
	
	@ApiModelProperty(name = "policyNumber", example = "345tf435rf4gsd", required = false )
	private String policyNumber;
	
	@ApiModelProperty(name = "expiryDate", example = "30/12/2018", required = false )
	private String expiryDate;

	public boolean isNoEffectiveDrivingLicense() {
		return noEffectiveDrivingLicense;
	}

	public void setNoEffectiveDrivingLicense(boolean noEffectiveDrivingLicense) {
		this.noEffectiveDrivingLicense = noEffectiveDrivingLicense;
	}

	public boolean isCpaCoverWithInternalAgent() {
		return cpaCoverWithInternalAgent;
	}

	public void setCpaCoverWithInternalAgent(boolean cpaCoverWithInternalAgent) {
		this.cpaCoverWithInternalAgent = cpaCoverWithInternalAgent;
	}

	public boolean isStandalonePAPolicy() {
		return standalonePAPolicy;
	}

	public void setStandalonePAPolicy(boolean standalonePAPolicy) {
		this.standalonePAPolicy = standalonePAPolicy;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
	
	
}
