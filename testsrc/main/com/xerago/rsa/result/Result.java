package com.xerago.rsa.result;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Result {

	private String statusCode;
	private String message;

	public String getMessage() {
		return message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
