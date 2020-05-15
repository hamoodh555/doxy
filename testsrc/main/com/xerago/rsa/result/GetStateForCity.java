package com.xerago.rsa.result;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "STATELIST")

public class GetStateForCity extends Result {
	
	private String stateName;
	private String stateCode;
	
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	

}
