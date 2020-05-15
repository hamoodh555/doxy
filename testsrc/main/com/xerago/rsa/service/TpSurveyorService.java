package com.xerago.rsa.service;

import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.domain.DPolicyDetails;
import com.xerago.rsa.domain.DPolicyVehicleDetails;
import com.xerago.rsa.domain.DQuoteDetails;
import com.xerago.rsa.domain.DUpdatedClientDetails;
import com.xerago.rsa.dto.request.DtcToTpSurveyorRequest;
import com.xerago.rsa.dto.response.DtcToTpSurveyorResponse;
import com.xerago.rsa.model.DTpSurveyReport;
import com.xerago.rsa.model.DTpSurveyorUseDetails;

@Service
public class TpSurveyorService {

	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${tpSurveyorUrl}")
	private String url;
	
	public void makeSurveyorCall(String quoteId) {
		Date requestTime = new Date();
		Date responseTime = null;
		String status = "Error";
		String request =null;
		String response =null;
		DtcToTpSurveyorRequest requestObj =null;
		DtcToTpSurveyorResponse tpSurveyorResponse = new DtcToTpSurveyorResponse();
		try{
			DQuoteDetails dQuoteDetails = twoWheelerDAO.getQuoteDetails(quoteId);
			DUpdatedClientDetails dUpdatedClientDetails = new ArrayList<DUpdatedClientDetails>(dQuoteDetails.getDUpdatedClientDetailses()).get(0);
			DPolicyDetails dPolicyDetails = new ArrayList<DPolicyDetails>(dQuoteDetails.getDPolicyDetailses()).get(0);
			DPolicyVehicleDetails dPolicyVehicleDetails = new ArrayList<DPolicyVehicleDetails>(dPolicyDetails.getDPolicyVehicleDetailses()).get(0);
			
			requestObj = new DtcToTpSurveyorRequest();
			requestObj.setReferenceNo(quoteId);
			String vehicleType=null;
			if( "VPC_Comprehensive".equalsIgnoreCase(dQuoteDetails.getPlanName()) ){
				vehicleType = "privatePassengerCar";
			}else if( "MotorCyclePackage".equalsIgnoreCase(dQuoteDetails.getPlanName()) ){
				vehicleType = "motorCycle";
			}else{
				vehicleType = "commercialVehicle";
			}
			requestObj.setVehicleType(vehicleType);
			requestObj.setCaseType("CUSTOMERPORTAL");
			requestObj.setCustomerName(dUpdatedClientDetails.getFirstName());
			requestObj.setCustomerContactNumber(dUpdatedClientDetails.getMobile());
			requestObj.setMailId(dUpdatedClientDetails.getEmail());
			requestObj.setFuelType(dPolicyVehicleDetails.getFuelType());
			requestObj.setVehicleMake(dPolicyVehicleDetails.getMakeName());
			requestObj.setVehicleModel(dPolicyVehicleDetails.getModelCode());
			requestObj.setYearOfManufacture(String.valueOf(dPolicyVehicleDetails.getYearOfManufacture()));
			requestObj.setRegistrationNumber(dPolicyVehicleDetails.getRegistrationNumber());
			requestObj.setChassisNumber(dPolicyVehicleDetails.getChassisNumber());
			requestObj.setEngineNumber(dPolicyVehicleDetails.getEngineNumber());
			

			requestObj.setUserName("");
			requestObj.setPassword("");
			requestObj.setOdometerReading("");
			requestObj.setVehicleColor("");
			requestObj.setAddress("");
			requestObj.setFieldExecutiveContactNumber("");
			requestObj.setFieldExecutiveNameOrId("");
			
			request = objectMapper.writeValueAsString(requestObj);
			LOGGER.info("request: "+ request);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Accept", "application/json");
			
			HttpEntity<DtcToTpSurveyorRequest> requestEntity = new HttpEntity<DtcToTpSurveyorRequest>(requestObj, headers);
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			response = responseEntity.getBody();
			responseTime = new Date();
			
			tpSurveyorResponse = objectMapper.readValue(response, DtcToTpSurveyorResponse.class);
			status = tpSurveyorResponse.getErrorMessage();


		}catch(Exception e){
			LOGGER.info("Make Surveyor Call",e);
			e.printStackTrace();
		}finally{
			
			DTpSurveyorUseDetails dTpSurveyorUseDetails = new DTpSurveyorUseDetails();
			dTpSurveyorUseDetails.setQuoteId(quoteId);
			dTpSurveyorUseDetails.setRequestTime(requestTime);
			dTpSurveyorUseDetails.setResponseTime(responseTime);
			dTpSurveyorUseDetails.setServiceName("MakeSurveyorCall");
			dTpSurveyorUseDetails.setStatus(status);
			twoWheelerDAO.saveDetails(dTpSurveyorUseDetails, request, response);
			
			DTpSurveyReport dTpSurveyReport = new DTpSurveyReport();
			dTpSurveyReport.setQuoteId(requestObj.getReferenceNo());
			if(tpSurveyorResponse.getErrorMessage()!=null) {
			dTpSurveyReport.setStatus("Success".equalsIgnoreCase(tpSurveyorResponse.getErrorMessage()) ? "Initiated" : "Error");
			}else {
				dTpSurveyReport.setStatus("Error");
			}
			twoWheelerDAO.saveOrUpdate(dTpSurveyReport);
			
		}
		
	}
	
}
