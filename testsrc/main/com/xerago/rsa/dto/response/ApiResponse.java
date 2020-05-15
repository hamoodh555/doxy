/**
 * 
 */
package com.xerago.rsa.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author pandiaraj
 *
 */
@JsonPropertyOrder({ "data", "code", "message"})
@JsonIgnoreProperties
public class ApiResponse {

	private String code;
	private String message;
	
	private Data data;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
		
}
