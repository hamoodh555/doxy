package com.xerago.rsa.model;

import java.io.Serializable;

public class ElectronicValues implements Serializable {

	private static final long serialVersionUID = 1L;

	private String electronicNameOfElectronicAccessories;
	private String electronicMakeModel;
	private String electronicValue;

	public String getElectronicNameOfElectronicAccessories() {
		return electronicNameOfElectronicAccessories;
	}

	public void setElectronicNameOfElectronicAccessories(String electronicNameOfElectronicAccessories) {
		this.electronicNameOfElectronicAccessories = electronicNameOfElectronicAccessories;
	}

	public String getElectronicMakeModel() {
		return electronicMakeModel;
	}

	public void setElectronicMakeModel(String electronicMakeModel) {
		this.electronicMakeModel = electronicMakeModel;
	}

	public String getElectronicValue() {
		return electronicValue;
	}

	public void setElectronicValue(String electronicValue) {
		this.electronicValue = electronicValue;
	}
}