/**
 * 
 */
package com.xerago.rsa.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

/**
 * @author pandiaraj
 *
 */
@JsonRootName(value = "PREMIUMDETAILS")
@JsonPropertyOrder({
	"status",
	"data",
	"PremiumForOneYear",
	"PremiumForTwoYears",
	"PremiumForThreeYears",
	"PremiumForLiabilityOnly",
	"fieldErrors" })

public class PremiumDetails {

	private Status status;

	private PremiumDetailsData data;

	private PremiumDetailsData PremiumForOneYear;

	private PremiumDetailsData PremiumForTwoYears;

	private PremiumDetailsData PremiumForThreeYears;
	
	private PremiumDetailsData PremiumForLiabilityOnlyForOneYear;
	
	private PremiumDetailsData PremiumForLiabilityOnlyForTwoYears;
	
	private PremiumDetailsData PremiumForLiabilityOnlyForThreeYears;
	
	private PremiumDetailsData PremiumForStandAloneForOneYear;
	
	private PremiumDetailsData PremiumForStandAloneForTwoYears;
	
	private PremiumDetailsData PremiumForStandAloneForThreeYears;
	
	private PremiumDetailsData PremiumForStandAloneForFourYears;

	private PremiumDetailsData PremiumForStandAloneForFiveYears;



	List<FieldErrorResource> fieldErrors;
	public List<FieldErrorResource> getFieldErrors() {
		return fieldErrors;
	}

	@JsonProperty("FieldErrors")
	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	@JsonProperty("Status")
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	@JsonProperty("DATA")
	public PremiumDetailsData getData() {
		return data;
	}

	public void setData(PremiumDetailsData data) {
		this.data = data;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForOneYear")
	public PremiumDetailsData getPremiumForOneYear() {
		return PremiumForOneYear;
	}

	public void setPremiumForOneYear(PremiumDetailsData premiumForOneYear) {
		PremiumForOneYear = premiumForOneYear;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForTwoYears")
	public PremiumDetailsData getPremiumForTwoYears() {
		return PremiumForTwoYears;
	}

	public void setPremiumForTwoYears(PremiumDetailsData premiumForTwoYears) {
		PremiumForTwoYears = premiumForTwoYears;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForThreeYears")
	public PremiumDetailsData getPremiumForThreeYears() {
		return PremiumForThreeYears;
	}

	public void setPremiumForThreeYears(PremiumDetailsData premiumForThreeYears) {
		PremiumForThreeYears = premiumForThreeYears;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForLiabilityOnlyForOneYear")
	public PremiumDetailsData getPremiumForLiabilityOnlyForOneYear() {
		return PremiumForLiabilityOnlyForOneYear;
	}

	public void setPremiumForLiabilityOnlyForOneYear(PremiumDetailsData premiumForLiabilityOnlyForOneYear) {
		PremiumForLiabilityOnlyForOneYear = premiumForLiabilityOnlyForOneYear;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForLiabilityOnlyForTwoYears")
	public PremiumDetailsData getPremiumForLiabilityOnlyForTwoYears() {
		return PremiumForLiabilityOnlyForTwoYears;
	}

	public void setPremiumForLiabilityOnlyForTwoYears(PremiumDetailsData premiumForLiabilityOnlyForTwoYears) {
		PremiumForLiabilityOnlyForTwoYears = premiumForLiabilityOnlyForTwoYears;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForLiabilityOnlyForThreeYears")
	public PremiumDetailsData getPremiumForLiabilityOnlyForThreeYears() {
		return PremiumForLiabilityOnlyForThreeYears;
	}

	public void setPremiumForLiabilityOnlyForThreeYears(PremiumDetailsData premiumForLiabilityOnlyForThreeYears) {
		PremiumForLiabilityOnlyForThreeYears = premiumForLiabilityOnlyForThreeYears;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForStandAloneForOneYear")
	public PremiumDetailsData getPremiumForStandAloneForOneYear() {
		return PremiumForStandAloneForOneYear;
	}

	public void setPremiumForStandAloneForOneYear(PremiumDetailsData premiumForStandAloneForOneYear) {
		PremiumForStandAloneForOneYear = premiumForStandAloneForOneYear;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForStandAloneForTwoYears")
	public PremiumDetailsData getPremiumForStandAloneForTwoYears() {
		return PremiumForStandAloneForTwoYears;
	}

	public void setPremiumForStandAloneForTwoYears(PremiumDetailsData premiumForStandAloneForTwoYears) {
		PremiumForStandAloneForTwoYears = premiumForStandAloneForTwoYears;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForStandAloneForThreeYears")
	public PremiumDetailsData getPremiumForStandAloneForThreeYears() {
		return PremiumForStandAloneForThreeYears;
	}

	public void setPremiumForStandAloneForThreeYears(PremiumDetailsData premiumForStandAloneForThreeYears) {
		PremiumForStandAloneForThreeYears = premiumForStandAloneForThreeYears;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForStandAloneForFourYears")
	public PremiumDetailsData getPremiumForStandAloneForFourYears() {
		return PremiumForStandAloneForFourYears;
	}

	public void setPremiumForStandAloneForFourYears(PremiumDetailsData premiumForStandAloneForFourYears) {
		PremiumForStandAloneForFourYears = premiumForStandAloneForFourYears;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("PremiumForStandAloneForFiveYears")
	public PremiumDetailsData getPremiumForStandAloneForFiveYears() {
		return PremiumForStandAloneForFiveYears;
	}

	public void setPremiumForStandAloneForFiveYears(PremiumDetailsData premiumForStandAloneForFiveYears) {
		PremiumForStandAloneForFiveYears = premiumForStandAloneForFiveYears;
	}
	
	

}
