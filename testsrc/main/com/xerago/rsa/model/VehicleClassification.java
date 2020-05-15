package com.xerago.rsa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(
	name = "GetVehicleClassification",
	query = "select V.BIKE_SCOOTER as bikeOrScooter from VEHICLE_CLASSFICATION V where upper(V.MODEL_CODE) = upper(:modelCode) AND upper(V.VEHICLE_TYPE) = upper(:vehicleType) ",
        resultClass = VehicleClassification.class
	)
})
public class VehicleClassification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "BIKEORSCOOTER")
	private String bikeOrScooter;

	public String getBikeOrScooter() {
		return bikeOrScooter;
	}

	public void setBikeOrScooter(String bikeOrScooter) {
		this.bikeOrScooter = bikeOrScooter;
	}
	
	
}
