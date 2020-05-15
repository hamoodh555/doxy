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
	name = "GetCityState",
	query = "select city.name as city, state.name as state,zone.name as zone, region.name as region,city.code as citycode,state.code as statecode, region.code AS region_code from commonitem city inner join commonitem state on(state.mastername = 'State' and state.item_id =city.reference_code_1)  inner join commonitem zone on(zone.mastername= 'Zone' and zone.item_id = city.reference_code_2)  inner join commonitem region on(region.mastername = 'Region' and region.item_id = city.reference_code_3) where upper(city.name) = upper(:city) order by city.code",
        resultClass = CityStateLookupModel.class
	)
})
public class CityStateLookupModel implements Serializable{

	private String city;
	private String cityCode;
	private String state;
	private String stateCode;
	private String zone;
	private String region;
	private String regionCode;
	
	@Id
	@Column(name="CITY")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name="CITYCODE")
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	@Column(name="STATE")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Column(name="STATECODE")
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	@Column(name="ZONE")
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	
	@Column(name="REGION")
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	@Column(name="REGION_CODE")
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	
}
