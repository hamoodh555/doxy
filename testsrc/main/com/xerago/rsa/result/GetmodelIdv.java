package com.xerago.rsa.result;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MODELPROPERTY")
public class GetmodelIdv implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8899855925027105867L;
	private String EngineCapacity;
	private String Idv;
	private String BodyType;
	private String FuelType;
	private String statusCode;
	private String message;
	private String vehicleclass;
	private String addoncoveragevalue;
	private String modelId;
	private String seatingCcapacity;
	private String modelcodeid;

	public String getAddoncoveragevalue() {
		return addoncoveragevalue;
	}

	public String getBodyType() {
		return BodyType;
	}

	public String getEngineCapacity() {
		return EngineCapacity;
	}

	public String getFuelType() {
		return FuelType;
	}

	public String getIdv() {
		return Idv;
	}

	public String getMessage() {
		return message;
	}

	public String getModelcodeid() {
		return modelcodeid;
	}

	public String getModelId() {
		return modelId;
	}

	public String getSeatingCcapacity() {
		return seatingCcapacity;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public String getVehicleclass() {
		return vehicleclass;
	}

	public void setAddoncoveragevalue(String addoncoveragevalue) {
		this.addoncoveragevalue = addoncoveragevalue;
	}

	public void setBodyType(String bodyType) {
		BodyType = bodyType;
	}

	public void setEngineCapacity(String engineCapacity) {
		EngineCapacity = engineCapacity;
	}

	public void setFuelType(String fuelType) {
		FuelType = fuelType;
	}

	public void setIdv(String idv) {
		Idv = idv;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setModelcodeid(String modelcodeid) {
		this.modelcodeid = modelcodeid;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public void setSeatingCcapacity(String seatingCcapacity) {
		this.seatingCcapacity = seatingCcapacity;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setVehicleclass(String vehicleclass) {
		this.vehicleclass = vehicleclass;
	}

}
