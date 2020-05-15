package com.xerago.rsa.result;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MODELPROPERTY")
public class GetModelIdvResult extends Result implements Serializable {

	private String engineCapacity;
	private String Idv;
	private String BodyType;
	private String FuelType;
	private String vehicleClass;
	private String addonCoverageValue;
	private String modelId;
	private String seatingCapacity;
	private String modelcodeid;

	private String IDVDepreciationforyear2;
	private String IDVDepreciationforyear3;
	private String businessStatus;
	
	private Integer vehicleAgeforyear2;
	private Integer vehicleAgeforyear3;
	
	public String getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(String engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	public String getIdv() {
		return Idv;
	}

	public void setIdv(String idv) {
		Idv = idv;
	}

	public String getBodyType() {
		return BodyType;
	}

	public void setBodyType(String bodyType) {
		BodyType = bodyType;
	}

	public String getFuelType() {
		return FuelType;
	}

	public void setFuelType(String fuelType) {
		FuelType = fuelType;
	}

	public String getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public String getAddonCoverageValue() {
		return addonCoverageValue;
	}

	public void setAddonCoverageValue(String addonCoverageValue) {
		this.addonCoverageValue = addonCoverageValue;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(String seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public String getModelcodeid() {
		return modelcodeid;
	}

	public void setModelcodeid(String modelcodeid) {
		this.modelcodeid = modelcodeid;
	}

	public String getIDVDepreciationforyear2() {
		return IDVDepreciationforyear2;
	}

	public void setIDVDepreciationforyear2(String iDVDepreciationforyear2) {
		IDVDepreciationforyear2 = iDVDepreciationforyear2;
	}

	public String getIDVDepreciationforyear3() {
		return IDVDepreciationforyear3;
	}

	public void setIDVDepreciationforyear3(String iDVDepreciationforyear3) {
		IDVDepreciationforyear3 = iDVDepreciationforyear3;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public Integer getVehicleAgeforyear2() {
		return vehicleAgeforyear2;
	}

	public void setVehicleAgeforyear2(Integer vehicleAgeforyear2) {
		this.vehicleAgeforyear2 = vehicleAgeforyear2;
	}

	public Integer getVehicleAgeforyear3() {
		return vehicleAgeforyear3;
	}

	public void setVehicleAgeforyear3(Integer vehicleAgeforyear3) {
		this.vehicleAgeforyear3 = vehicleAgeforyear3;
	}

}
