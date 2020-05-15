package com.xerago.rsa.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.swagger.annotations.ApiModelProperty;

public class NonElectricalAccessoriesDetails {

	@ApiModelProperty(value = "Name Of Non-Electronic Accessories", required = true, example = "MRkyDzbAVd")
	private String nameOfElectronicAccessories;
	
	@ApiModelProperty(value = "Make Model of Non-Electric Accessories", required = true, example = "jPwYFkokuD")
	private String makeModel;
	
	@ApiModelProperty(value = "Value of Non-Electric Accessories", required = true, example = "1176")
	private long value;

	@JsonProperty("NameOfElectronicAccessories")
	@JacksonXmlProperty(localName = "NameOfElectronicAccessories")
	public String getNameOfElectronicAccessories() {
		return nameOfElectronicAccessories;
	}

	public void setNameOfElectronicAccessories(String nameOfElectronicAccessories) {
		this.nameOfElectronicAccessories = nameOfElectronicAccessories;
	}

	@JsonProperty("MakeModel")
	@JacksonXmlProperty(localName = "MakeModel")
	public String getMakeModel() {
		return makeModel;
	}

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}

	@JsonProperty("Value")
	@JacksonXmlProperty(localName = "Value")
	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
