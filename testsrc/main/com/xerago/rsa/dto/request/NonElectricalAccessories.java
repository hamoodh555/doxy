package com.xerago.rsa.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class NonElectricalAccessories {
	
	@JsonProperty("nonelectronicAccessoriesDetails")
	@JacksonXmlProperty(localName="nonelectronicAccessoriesDetails")
	@JacksonXmlElementWrapper(useWrapping=false)
	private List<NonElectricalAccessoriesDetails> nonelectronicAccessoriesDetails;

	public List<NonElectricalAccessoriesDetails> getNonelectronicAccessoriesDetails() {
		return nonelectronicAccessoriesDetails;
	}

	public void setNonelectronicAccessoriesDetails(List<NonElectricalAccessoriesDetails> nonelectronicAccessoriesDetails) {
		this.nonelectronicAccessoriesDetails = nonelectronicAccessoriesDetails;
	}

	public NonElectricalAccessories() {
		// TODO Auto-generated constructor stub
	}

	

}
