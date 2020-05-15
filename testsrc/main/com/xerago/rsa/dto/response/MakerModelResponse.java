package com.xerago.rsa.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "makerName",
    "modelName",
    "response",
})*/
public class MakerModelResponse{

	@JsonProperty("modelName")
	private String modelName;
	
	@JsonProperty("makerName")
	private String makerName;
	
	@JsonProperty("response")
	private String response;
	
	public String getMakerModel() {
		return modelName;
	}

	public void setMakerModel(String modelName) {
		this.modelName = modelName;
	}

	public String getMakerName() {
		return makerName;
	}

	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}

}
