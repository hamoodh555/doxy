/**
 * 
 */
package com.xerago.rsa.dto.response;

import java.util.List;

/**
 * @author pandiaraj
 *
 */

public class Data extends Person{

	private String agentId;
	private String clientCode;
	private Boolean isUserExist;
	private Boolean isNewPassword;
	private Boolean isLogin;
	private String lastLoginTime;
	private String lastLoginIp;
	private String userType;
	private String description;
	
	private List<String> manufacturerList;
	
	private String region;
	private String state;
	private String stateCode;
	private String city;
	private String cityCode;
	private String zone;
	private String district;
	private String pincode;

	private ModelIdvResult modelProperty;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Boolean getIsUserExist() {
		return isUserExist;
	}

	public void setIsUserExist(Boolean isUserExist) {
		this.isUserExist = isUserExist;
	}

	public Boolean getIsNewPassword() {
		return isNewPassword;
	}

	public void setIsNewPassword(Boolean isNewPassword) {
		this.isNewPassword = isNewPassword;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getManufacturerList() {
		return manufacturerList;
	}

	public void setManufacturerList(List<String> manufacturerList) {
		this.manufacturerList = manufacturerList;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public ModelIdvResult getModelProperty() {
		return modelProperty;
	}

	public void setModelProperty(ModelIdvResult modelProperty) {
		this.modelProperty = modelProperty;
	}

	
	
}
