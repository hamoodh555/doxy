package com.xerago.rsa.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VahanResponse{

	@JsonProperty("MODEL_NAME")
	private String makerModel;
	
	@JsonProperty("MAKER_NAME")
	private String makerName;
	
	@JsonProperty("VAHAN_RESPONSE")
	private String response;
	
	@JsonProperty("RESPONSE_MESSAGE")
	private String responseMessage;
	
	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getMakerModel() {
		return makerModel;
	}

	public void setMakerModel(String makerModel) {
		this.makerModel = makerModel;
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
