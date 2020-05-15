package com.xerago.rsa.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtcToTpSurveyorResponse {
	
	@JsonProperty("ErrorCode")
	private String errorCode;
	
	@JsonProperty("ErrorMessage")
	private String errorMessage;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "DtcToTpSurveyorResponse [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}
	
}
