package com.xerago.rsa.model;

import java.io.Serializable;

public class CoverageModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String isselected;
	private String coverageName;
	private String isComposite;
	private String isMandatory;
	private String requestId;
	private String parentCoverageId;
	private String coverageDescription;
	private String limitName;
	private String limitType;
	private String defaultLimitvalue;
	private String deductibleName;
	private String deductibleType;
	private String defaultDeductiblevalue;
	private String packageName;
	private String productName;
	private int id;
	private String limitDescription;
	private String deductibleDescription;
	private String productId;
	private String coverageDisplayType;

	// PricingElements

	private String pricingelementname;

	public String getCoverageDescription() {
		return coverageDescription;
	}

	public String getCoverageDisplayType() {
		return coverageDisplayType;
	}

	public String getCoverageName() {
		return coverageName;
	}

	public String getDeductibleDescription() {
		return deductibleDescription;
	}

	public String getDeductibleName() {
		return deductibleName;
	}

	public String getDeductibleType() {
		return deductibleType;
	}

	public String getDefaultDeductiblevalue() {
		return defaultDeductiblevalue;
	}

	public String getDefaultLimitvalue() {
		return defaultLimitvalue;
	}

	public int getId() {
		return id;
	}

	public String getIsComposite() {
		return isComposite;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public String getIsselected() {
		return isselected;
	}

	public String getLimitDescription() {
		return limitDescription;
	}

	public String getLimitName() {
		return limitName;
	}

	public String getLimitType() {
		return limitType;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getParentCoverageId() {
		return parentCoverageId;
	}

	public String getPricingelementname() {
		return pricingelementname;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setCoverageDescription(String coverageDescription) {
		this.coverageDescription = coverageDescription;
	}

	public void setCoverageDisplayType(String coverageDisplayType) {
		this.coverageDisplayType = coverageDisplayType;
	}

	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}

	public void setDeductibleDescription(String deductibleDescription) {
		this.deductibleDescription = deductibleDescription;
	}

	public void setDeductibleName(String deductibleName) {
		this.deductibleName = deductibleName;
	}

	public void setDeductibleType(String deductibleType) {
		this.deductibleType = deductibleType;
	}

	public void setDefaultDeductiblevalue(String defaultDeductiblevalue) {
		this.defaultDeductiblevalue = defaultDeductiblevalue;
	}

	public void setDefaultLimitvalue(String defaultLimitvalue) {
		this.defaultLimitvalue = defaultLimitvalue;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIsComposite(String isComposite) {
		this.isComposite = isComposite;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public void setIsselected(String isselected) {
		this.isselected = isselected;
	}

	public void setLimitDescription(String limitDescription) {
		this.limitDescription = limitDescription;
	}

	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setParentCoverageId(String parentCoverageId) {
		this.parentCoverageId = parentCoverageId;
	}

	public void setPricingelementname(String pricingelementname) {
		this.pricingelementname = pricingelementname;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
