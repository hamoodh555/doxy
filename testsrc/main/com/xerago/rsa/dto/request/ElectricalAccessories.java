package com.xerago.rsa.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ElectricalAccessories {
	
	@JsonProperty("electronicAccessoriesDetails")
	@JacksonXmlProperty(localName="electronicAccessoriesDetails")
	@JacksonXmlElementWrapper(useWrapping=false)
	private List<ElectricalAccessoriesDetails> electronicAccessoriesDetails;

	public List<ElectricalAccessoriesDetails> getElectronicAccessoriesDetails() {
		return electronicAccessoriesDetails;
	}

	public void setElectronicAccessoriesDetails(List<ElectricalAccessoriesDetails> electronicAccessoriesDetails) {
		this.electronicAccessoriesDetails = electronicAccessoriesDetails;
	}

	public ElectricalAccessories() {
		// TODO Auto-generated constructor stub
	}
}
