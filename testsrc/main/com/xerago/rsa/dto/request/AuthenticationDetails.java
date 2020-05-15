package com.xerago.rsa.dto.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="AuthenticationDetails", description = "TODO", parent = CalculatePremiumRequest.class)
public class AuthenticationDetails {
	
	
	@ApiModelProperty(value = "API key to access the application", required = true, example = "310ZQmv/bYJMYrWQ1iYa7s43084=")
	private String apikey;
	
	@NotNull
	@NotBlank
	@ApiModelProperty(value = "AgentId", required = true, example = "RSAI")
	private String agentId;
	
	/**
	 * @return the apikey
	 */
	public String getApikey() {
		return apikey;
	}
	/**
	 * @param apikey the apikey to set
	 */
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
	public void setApiKey(String apiKey) {
		this.apikey = apiKey;
	}
	/**
	 * @return the agentId
	 */
	public String getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	
}
