/**
 * 
 */
package com.xerago.rsa.dto.response;

/**
 * @author pandiaraj
 *
 */
public class FieldErrorResource {
	
	public FieldErrorResource() {
		// TODO Auto-generated constructor stub
	}
	
	private String resource;
	private String field;
	private String message;
	private Object valueGiven;
	private String errorCode;
	
	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param path the field to set
	 */
	public void setField(String path) {
		this.field = path;
	}
	/**
	 * @return the valueGiven
	 */
	public Object getValueGiven() {
		return valueGiven;
	}
	/**
	 * @param valueGiven the valueGiven to set
	 */
	public void setValueGiven(Object valueGiven) {
		this.valueGiven = valueGiven;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
