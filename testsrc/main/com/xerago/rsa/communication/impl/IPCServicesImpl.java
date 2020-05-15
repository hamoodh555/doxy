package com.xerago.rsa.communication.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xerago.rsa.communication.IPCServices;
import com.xerago.rsa.dto.request.CustomerDetailsRequest;
import com.xerago.rsa.dto.response.ApiResponse;
import com.xerago.rsa.dto.response.MakerModelResponse;
import com.xerago.rsa.dto.response.ModelIdvResult;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.model.MotorInsuranceModel;

@Component
public class IPCServicesImpl implements IPCServices {

	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Value("${com.xerago.rsa.common}")
	String commonServiceUrl;

	@Value("${com.xerago.rsa.d2c}")
	String d2cServiceUrl;
	
	@Override
	public void getManufacturerNameList(MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		// * to set model details , region details and vehicle age
		 
		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<String, String>();
		uriVariables.add("vehicleRegisteredCity", motorInsuranceModel.getVehicleRegisteredCity());
		uriVariables.add("vehicleSubLine", motorInsuranceModel.getVehicleSubLine());
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(commonServiceUrl+ "/Services/Common/LookupService/ManufacturerName");
		uriComponentsBuilder.queryParams(uriVariables);
		//RestTemplate restTemplate = new RestTemplate(); 
		HttpEntity<ApiResponse> manufacturerResponse = restTemplate.getForEntity(uriComponentsBuilder.build().encode().toUri(), ApiResponse.class);
		
		if(manufacturerResponse.getBody().getData() != null) {
			motorInsuranceModel.setState(manufacturerResponse.getBody().getData().getState());
			motorInsuranceModel.setZone(manufacturerResponse.getBody().getData().getZone());
			motorInsuranceModel.setRegion(manufacturerResponse.getBody().getData().getRegion());
			motorInsuranceModel.setCityCode(manufacturerResponse.getBody().getData().getCityCode());
			motorInsuranceModel.setStateCode(manufacturerResponse.getBody().getData().getStateCode());	
		}
	}

	@Override
	public void getModelIdvResult(MotorInsuranceModel motorInsuranceModel) throws Exception {
		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<String, String>();
		uriVariables.add("vehicleRegisteredCity", motorInsuranceModel.getVehicleRegisteredCity());
		uriVariables.add("yearOfManufacture", String.valueOf(motorInsuranceModel.getYearOfManufacture()));
		uriVariables.add("vehicleRegDate", motorInsuranceModel.getVehicleRegistrationDate());
		uriVariables.add("previousPolicyExpiryDate", motorInsuranceModel.getPreviousPolicyExpiryDate());
		uriVariables.add("vehicleModelCode", motorInsuranceModel.getVehicleModelCode());
		uriVariables.add("isPreviousPolicyHolder", motorInsuranceModel.getIsPreviousPolicyHolder());
		uriVariables.add("policyTerm", String.valueOf(motorInsuranceModel.getPolicyTerm()));
		uriVariables.add("quoteId", motorInsuranceModel.getQuoteId());
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(commonServiceUrl+ "/Services/Common/LookupService/ModelIdv");
		uriComponentsBuilder.queryParams(uriVariables);
		//RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ApiResponse> modelIdvResponse = restTemplate.getForEntity(uriComponentsBuilder.build().encode().toUri(), ApiResponse.class);
		
		if("S-0010".equalsIgnoreCase(modelIdvResponse.getBody().getCode())){
			ModelIdvResult modelIdvResult = modelIdvResponse.getBody().getData().getModelProperty();
			
			//IDV
			if(modelIdvResult.getIdv()!=null){
				motorInsuranceModel.setVehicleModelCode(modelIdvResult.getModelCode());
				motorInsuranceModel.setVehicleManufacturerName(modelIdvResult.getModelName());
				motorInsuranceModel.setServiceModelCode(modelIdvResult.getServiceModelCode());
				motorInsuranceModel.setCalculatedModelCode(modelIdvResult.getCalculatedModelCode());
				motorInsuranceModel.setIdv(modelIdvResult.getIdv());
				motorInsuranceModel.setIdvFor1Year(modelIdvResult.getIdv());
				motorInsuranceModel.setIdvFor2Year(Double.valueOf(modelIdvResult.getIdvDepreciationForYear2() != null ? modelIdvResult.getIdvDepreciationForYear2() : 0));
				motorInsuranceModel.setIdvFor3Year(Double.valueOf(modelIdvResult.getIdvDepreciationForYear3() != null ? modelIdvResult.getIdvDepreciationForYear3() : 0));
				motorInsuranceModel.setIdvFor4Year(Double.valueOf(modelIdvResult.getIdvDepreciationForYear4() != null ? modelIdvResult.getIdvDepreciationForYear4() : 0));
				motorInsuranceModel.setIdvFor5Year(Double.valueOf(modelIdvResult.getIdvDepreciationForYear5() != null ? modelIdvResult.getIdvDepreciationForYear5() : 0));
				
				motorInsuranceModel.setOriginalIdvFor1Year(String.valueOf(modelIdvResult.getIdv()));
				motorInsuranceModel.setOriginalIdvFor2Year(String.valueOf(modelIdvResult.getIdvDepreciationForYear2() != null ? modelIdvResult.getIdvDepreciationForYear2() : 0));
	            motorInsuranceModel.setOriginalIdvFor3Year(String.valueOf(modelIdvResult.getIdvDepreciationForYear3() != null ? modelIdvResult.getIdvDepreciationForYear3() : 0));
	            motorInsuranceModel.setOriginalIdvFor4Year(String.valueOf(modelIdvResult.getIdvDepreciationForYear4() != null ? modelIdvResult.getIdvDepreciationForYear4() : 0));
	            motorInsuranceModel.setOriginalIdvFor5Year(String.valueOf(modelIdvResult.getIdvDepreciationForYear5() != null ? modelIdvResult.getIdvDepreciationForYear5() : 0));

	            if("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
					motorInsuranceModel.setModifiedIdvfor1Year(motorInsuranceModel.getIdv()); //For RollOver GetAQuote
					motorInsuranceModel.setModifiedIdvfor2Year(motorInsuranceModel.getIdvFor2Year()); //For RollOver GetAQuote
					motorInsuranceModel.setModifiedIdvfor3Year(motorInsuranceModel.getIdvFor3Year()); //For RollOver GetAQuote
					motorInsuranceModel.setModifiedIdvfor4Year(motorInsuranceModel.getIdvFor4Year());
					motorInsuranceModel.setModifiedIdvfor5Year(motorInsuranceModel.getIdvFor5Year());
				} else {
					LOGGER.info("These values are applied on setIDV_PreviousPolicyDetails method");
				}	
				}else{
					
					motorInsuranceModel.setIdv(0.0);
					motorInsuranceModel.setIdvFor1Year(0.0);
					motorInsuranceModel.setIdvFor2Year(0.0);
					motorInsuranceModel.setIdvFor3Year(0.0);
					motorInsuranceModel.setIdvFor4Year(0.0);
					motorInsuranceModel.setIdvFor5Year(0.0);
					
					motorInsuranceModel.setOriginalIdvFor1Year("0");
					motorInsuranceModel.setOriginalIdvFor2Year("0");
					motorInsuranceModel.setOriginalIdvFor3Year("0");
					motorInsuranceModel.setOriginalIdvFor4Year("0");
					motorInsuranceModel.setOriginalIdvFor5Year("0");
					
				}
			//Vehicle Age
			motorInsuranceModel.setVehicleAge(modelIdvResult.getVehicleAge().intValue());
			motorInsuranceModel.setVehicleAgeforyear2(modelIdvResult.getVehicleAgeforyear2()!= null ? modelIdvResult.getVehicleAgeforyear2().intValue() : 0);
			motorInsuranceModel.setVehicleAgeforyear3(modelIdvResult.getVehicleAgeforyear3()!= null ? modelIdvResult.getVehicleAgeforyear3().intValue() : 0);
			motorInsuranceModel.setVehicleAgeforyear4(modelIdvResult.getVehicleAgeforyear4()!= null ? modelIdvResult.getVehicleAgeforyear4().intValue() : 0);
			motorInsuranceModel.setVehicleAgeforyear5(modelIdvResult.getVehicleAgeforyear5()!= null ? modelIdvResult.getVehicleAgeforyear5().intValue() : 0);
			
			motorInsuranceModel.setSeatingCapacity(Integer.valueOf(modelIdvResult.getSeatingCapacity()));
			motorInsuranceModel.setEngineCapacity(Integer.valueOf(
					modelIdvResult.getEngineCapacity().substring(0, modelIdvResult.getEngineCapacity().indexOf(" "))));
			motorInsuranceModel.setVehicleClass(modelIdvResult.getVehicleClass());
			motorInsuranceModel.setVehicleModelName(modelIdvResult.getModelName());
			motorInsuranceModel.setBodyStyle(modelIdvResult.getBodyType());
			motorInsuranceModel.setVehicleMakeId(modelIdvResult.getModelId());
			motorInsuranceModel.setAddonModel(modelIdvResult.getAddonCoverageValue());
			motorInsuranceModel.setFuelType(modelIdvResult.getFuelType());
		}
	}

	@Override
	public void customerRegistration(MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		LOGGER.info("inside customerRegistration");
		CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest();
		customerDetailsRequest.setEmailId(motorInsuranceModel.getStrEmail());
		customerDetailsRequest.setTitle(motorInsuranceModel.getStrTitle());
		customerDetailsRequest.setFirstName(motorInsuranceModel.getStrFirstName());
		customerDetailsRequest.setLastName(motorInsuranceModel.getStrLastName());
		customerDetailsRequest.setStdCode(motorInsuranceModel.getStrStdCode());
		customerDetailsRequest.setPhoneNo(motorInsuranceModel.getStrPhoneNo());
		customerDetailsRequest.setMobileNo(motorInsuranceModel.getStrMobileNo());
		customerDetailsRequest.setProduct(motorInsuranceModel.getProductName());
		customerDetailsRequest.setAgentId(motorInsuranceModel.getAgentId());
		customerDetailsRequest.setDateOfBirth(motorInsuranceModel.getDateOfBirth());
		customerDetailsRequest.setOccupation(motorInsuranceModel.getOccupation());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writeValueAsString(customerDetailsRequest),
				headers);
		//RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ApiResponse> response = restTemplate.exchange(
				d2cServiceUrl + "/Services/UserServices/UserRegistration", HttpMethod.POST, entity, ApiResponse.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			ApiResponse apiResponse = response.getBody();
		//	LOGGER.info("apiResponse.getData().getClientCode() ::: " + apiResponse.getData().getClientCode());
			/*
			 * if(StringUtils.isBlank(apiResponse.getData().getClientCode())) { throw new
			 * RuntimeException("Unable to process the UserRegistration"); }
			 */
			if (apiResponse.getData() != null) {
				motorInsuranceModel.setClientCode(apiResponse.getData().getClientCode());
			}
			
		} else {
			throw new RuntimeException("Unable to process the UserRegistration");
		}
		
	}

	@Override
	public void setContactState(MotorInsuranceModel motorInsuranceModel) throws Exception {
		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<String, String>();
		uriVariables.add("city", motorInsuranceModel.getContactCity());
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(commonServiceUrl+ "/Services/Common/LookupService/SearchState");
		uriComponentsBuilder.queryParams(uriVariables);
		//RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ApiResponse> searchState = restTemplate.getForEntity(uriComponentsBuilder.build().encode().toUri(), ApiResponse.class);
		LOGGER.info(searchState.getHeaders());
		LOGGER.info(" searchStateMsg :::"+ searchState.getBody().getMessage());
		LOGGER.info(" searchStateCode :::"+ searchState.getBody().getCode());
		if(searchState.getBody().getCode().startsWith("S-")){
		    motorInsuranceModel.setContactState(searchState.getBody().getData().getState());
		}
		
	}

	@Override
	public PremiumDetails fileUpload(HashMap<String, String> requestMap) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(new MediaType("application","xml")));
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.setAll(requestMap);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PremiumDetails> response = restTemplate.postForEntity( commonServiceUrl+"/Services/Common/LookupService", request , PremiumDetails.class );
		
		return response.getBody();
	}

	@Override
	public PremiumDetails checkUploadStatus(HashMap<String, String> requestMap) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(new MediaType("application","xml")));
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.setAll(requestMap);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PremiumDetails> response = restTemplate.postForEntity( commonServiceUrl+"/Services/Common/LookupService", request , PremiumDetails.class );
		
		return response.getBody();
	}
	
	public MakerModelResponse getVahanServicePlan(MotorInsuranceModel motorInsuranceModel,String Subline) throws Exception {
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("chassisNo", motorInsuranceModel.getChassisNumber());
		uriVariables.put("engineNo", motorInsuranceModel.getEngineNumber());
		uriVariables.put("registrationNumber", motorInsuranceModel.getRegistrationNumber());
		uriVariables.put("productName", motorInsuranceModel.getProductName());
		uriVariables.put("sublime",Subline);
		uriVariables.put("businessType", "NB");
		uriVariables.put("quoteId", motorInsuranceModel.getQuoteId());
       
		MakerModelResponse modelIdvResponse = restTemplate.getForObject(d2cServiceUrl + "/Services/Common/LookupService/vahanService/getMakerModelInfo?"
       		+ "chassisNo={chassisNo}&engineNo={engineNo}&registrationNumber={registrationNumber}"
       		+ "&productName={productName}&sublime={sublime}&businessType={businessType}&quoteId={quoteId}",
       		MakerModelResponse.class, uriVariables);
		return modelIdvResponse;
	}

}
