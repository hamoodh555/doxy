package com.xerago.rsa.dto.request;

import com.xerago.rsa.dto.response.Person;

public class CustomerDetailsRequest extends Person {
	
	private String quoteId;
	private String crossSellQuoteId;
	private String password;
	private boolean isEnPwd;
	private String agentId = "RSAI";
	
	//GATrack
	private String searchEngine;
	private String clientName;
	private String adgroup;
	private String keyword;
	private String searchVsContent;
	private String ipaddress;
	private String referralUrl;
	private String product;
	private String campaignName;
	private String clientCode;
	private String gclid;
	
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public String getCrossSellQuoteId() {
		return crossSellQuoteId;
	}
	public void setCrossSellQuoteId(String crossSellQuoteId) {
		this.crossSellQuoteId = crossSellQuoteId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnPwd() {
		return isEnPwd;
	}
	public void setEnPwd(boolean isEnPwd) {
		this.isEnPwd = isEnPwd;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getSearchEngine() {
		return searchEngine;
	}
	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getAdgroup() {
		return adgroup;
	}
	public void setAdgroup(String adgroup) {
		this.adgroup = adgroup;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSearchVsContent() {
		return searchVsContent;
	}
	public void setSearchVsContent(String searchVsContent) {
		this.searchVsContent = searchVsContent;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getReferralUrl() {
		return referralUrl;
	}
	public void setReferralUrl(String referralUrl) {
		this.referralUrl = referralUrl;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getGclid() {
		return gclid;
	}
	public void setGclid(String gclid) {
		this.gclid = gclid;
	}


}
