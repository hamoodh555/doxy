/**
 * 
 */
package com.xerago.rsa.dto.request;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.xerago.rsa.bean.validation.groups.CalculatePremium;
import com.xerago.rsa.bean.validation.groups.QuoteIdEmailID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "request")
public class Request {

	@NotNull(groups = { CalculatePremium.class })
	@Valid
	private AuthenticationDetails authenticationDetails;

	/*@NotNull(groups = { GetQuote.class })
	@Valid
	private Product product;*/

	@NotNull(groups = { CalculatePremium.class })
	@Valid
	private ProposerDetails proposerDetails;
	
	@NotNull(groups = { CalculatePremium.class })
	@Valid
	private VehicleDetails vehicleDetails;

	@JsonProperty("gaTrackerData")
	@JacksonXmlProperty(localName = "gaTrackerData")
	private GaTrackerData gaTrackerData;

	@NotNull(groups={QuoteIdEmailID.class})
	@NotBlank(groups={QuoteIdEmailID.class}) 
	private String quoteId;

	private String process;

	/**
	 * @return the proposerDetails
	 */
	public ProposerDetails getProposerDetails() {
		return proposerDetails;
	}

	/**
	 * @param proposerDetails the proposerDetails to set
	 */
	public void setProposerDetails(ProposerDetails proposerDetails) {
		this.proposerDetails = proposerDetails;
	}

	/**
	 * @return the vehicleDetails
	 */
	public VehicleDetails getVehicleDetails() {
		return vehicleDetails;
	}

	/**
	 * @param vehicleDetails the vehicleDetails to set
	 */
	public void setVehicleDetails(VehicleDetails vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	/**
	 * @return the authenticationDetails
	 */
	public AuthenticationDetails getAuthenticationDetails() {
		return authenticationDetails;
	}

	/**
	 * @param authenticationDetails the authenticationDetails to set
	 */
	public void setAuthenticationDetails(AuthenticationDetails authenticationDetails) {
		this.authenticationDetails = authenticationDetails;
	}

	/**
	 * @return the gaTrackerData
	 */
	public GaTrackerData getGaTrackerData() {
		return gaTrackerData;
	}

	/**
	 * @param gaTrackerData
	 *            the gaTrackerData to set
	 */
	public void setGaTrackerData(GaTrackerData gaTrackerData) {
		this.gaTrackerData = gaTrackerData;
	}

	/**
	 * @return the quoteId
	 */
	public String getQuoteId() {
		return quoteId;
	}

	/**
	 * @param quoteId
	 *            the quoteId to set
	 */
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * @return the process
	 */
	public String getProcess() {
		return process;
	}

	/**
	 * @param process
	 *            the process to set
	 */
	public void setProcess(String process) {
		this.process = process;
	}

}
