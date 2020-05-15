package com.xerago.rsa.model;

import java.io.Serializable;

public class NonElectronicValues implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nonElectronicNameOfElectronicAccessories;
	private String nonElectronicMakeModel;
	private String nonElectronicValue;

	public String getNonElectronicNameOfElectronicAccessories() {
		return nonElectronicNameOfElectronicAccessories;
	}

	public void setNonElectronicNameOfElectronicAccessories(String nonElectronicNameOfElectronicAccessories) {
		this.nonElectronicNameOfElectronicAccessories = nonElectronicNameOfElectronicAccessories;
	}

	public String getNonElectronicMakeModel() {
		return nonElectronicMakeModel;
	}

	public void setNonElectronicMakeModel(String nonElectronicMakeModel) {
		this.nonElectronicMakeModel = nonElectronicMakeModel;
	}

	public String getNonElectronicValue() {
		return nonElectronicValue;
	}

	public void setNonElectronicValue(String nonElectronicValue) {
		this.nonElectronicValue = nonElectronicValue;
	}

}
