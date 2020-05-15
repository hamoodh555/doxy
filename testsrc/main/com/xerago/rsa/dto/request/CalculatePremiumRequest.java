package com.xerago.rsa.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.xerago.rsa.bean.validation.groups.CalculatePremium;
import com.xerago.rsa.bean.validation.groups.QuoteIdEmailID;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "CALCULATEPREMIUMREQUEST")
@ApiModel(value="CalculatePremiumRequest", description = "TODO")
public class CalculatePremiumRequest {
	
	@ApiModelProperty(hidden=false, value = "A value is generated by application on calculatepremium response, need to give on updatevehicledetails, gproposal requests")
	private String quoteId;
	
	@ApiModelProperty(hidden = true)
	private double premium;
	
	@ApiModelProperty(value = "Process that should be held on server (While get Quote, the value should be 'getAQuote')", required = true, example = "CalucalutePremium")
	private String process;
	
	@NotNull(groups = { CalculatePremium.class })
	@Valid
	@JsonProperty("authenticationDetails")
	@ApiModelProperty(value = "Authentication Details have to perform the request to application ", required = true, example = "")
	private AuthenticationDetails authenticationDetails;
	
	@NotNull(groups = { CalculatePremium.class })
	@Valid
	@ApiModelProperty(value = "Proposer Details", required = false, example = "")
	private ProposerDetails proposerDetails;
	
	@ApiModelProperty(value = "Vehicle Details", required = true, example = "")
	private VehicleDetails vehicleDetails;
	
	@ApiModelProperty(value = "Possible values are Microsite/Webservice/NewSite. For calulatepremium process, the values is Webservice and for UpdateVehicleDetails process the values is Microsite", required = true, example = "Microsite")
	private String source;
	
	/*Task #269726 External reference number in calculate premium request-148728*/
	private String externalReferenceNumber;
	/*Task #269726 External reference number in calculate premium request-148728*/

	@JacksonXmlProperty(localName = "channel")
	@JsonProperty("channel")
	private String channelcode;
	
	// Task #278458 POS DETAILS RS 154761 - TECH
	@ApiModelProperty(value = "isPosOpted", required = false, example = "false")
	private boolean isPosOpted;
		
	@ApiModelProperty(name = "posCode", required = false, example = "INV/POSP/A/000006")
	private String posCode;
		
	@ApiModelProperty(value = "only for POS details")
	private PosDetails posDetails;
	
	
	@ApiModelProperty(value = "Existing TP Policy Details", required = false, example = "")
	private ExistingTPPolicyDetails existingTPPolicyDetails;
	
	/**
	 * @return the process
	 */
	public String getProcess() {
		return process;
	}
	/**
	 * @param process the process to set
	 */
	public void setProcess(String process) {
		this.process = process;
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
	public AuthenticationDetails getAuthenticationDetails() {
		return authenticationDetails;
	}
	public void setAuthenticationDetails(AuthenticationDetails authenticationDetails) {
		this.authenticationDetails = authenticationDetails;
	}
	public ProposerDetails getProposerDetails() {
		return proposerDetails;
	}
	public void setProposerDetails(ProposerDetails proposerDetails) {
		this.proposerDetails = proposerDetails;
	}
	public VehicleDetails getVehicleDetails() {
		return vehicleDetails;
	}
	public void setVehicleDetails(VehicleDetails vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getExternalReferenceNumber() {
		return externalReferenceNumber;
	}
	public void setExternalReferenceNumber(String externalReferenceNumber) {
		this.externalReferenceNumber = externalReferenceNumber;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public boolean isPosOpted() {
		return isPosOpted;
	}
	public void setPosOpted(boolean isPosOpted) {
		this.isPosOpted = isPosOpted;
	}
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
	public PosDetails getPosDetails() {
		return posDetails;
	}
	public void setPosDetails(PosDetails posDetails) {
		this.posDetails = posDetails;
	}
	
	public ExistingTPPolicyDetails getExistingTPPolicyDetails() {
		return existingTPPolicyDetails;
	}

	public void setExistingTPPolicyDetails(ExistingTPPolicyDetails existingTPPolicyDetails) {
		this.existingTPPolicyDetails = existingTPPolicyDetails;
	}
	
	
	@Override
	public String toString() {
		return "CalculatePremiumRequest{" +
				"quoteId='" + quoteId + '\'' +
				", premium=" + premium +
				", process='" + process + '\'' +
				", authenticationDetails=" + authenticationDetails +
				", proposerDetails=" + proposerDetails +
				", vehicleDetails=" + vehicleDetails +
				", source='" + source + '\'' +
				", externalReferenceNumber='" + externalReferenceNumber + '\'' +
				", channelcode='" + channelcode + '\'' +
				'}';
	}
}
