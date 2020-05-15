package com.xerago.rsa.web;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xerago.rsa.bean.validation.groups.ProposerDetails;
import com.xerago.rsa.cassandra.dao.CassandraInsertions;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.domain.DPolicyPurchasedDetails;
import com.xerago.rsa.domain.DQuoteDetails;
import com.xerago.rsa.domain.DUpdatedClientDetails;
import com.xerago.rsa.dto.request.CalculatePremiumRequest;
import com.xerago.rsa.dto.request.GProposalRequest;
import com.xerago.rsa.dto.response.FieldErrorResource;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.PremiumDetailsData;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.exception.IFoundryException;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.service.ProcessFactory;
import com.xerago.rsa.validation.Validation;
import com.xerago.rsa.webproxy.transform.RequestFactory;
import com.xerago.rsa.webproxy.transform.XmlJsonMarshaller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

//@ControllerAdvice
@RestController
@Transactional
//TODO : Validate the request
public class WSTwoWheeler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LogManager.getRootLogger();

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	Validator validator;

	@Resource(name = "errorcodeMessages")
	private Map<String, String> errorcodeMessages;

	@Autowired
	ProcessFactory twoWheelerServiceFactory;
	
	@Autowired
	RequestFactory requestFactory; 
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Autowired
	XmlJsonMarshaller xmlJsonMarshaller;
	
	@Autowired
	CassandraInsertions cassandraInsertions; 
	
	@Autowired
	Validation validation;

	@Value("${kong.f5.uri:http://10.100.20.35:9898}")
	private String mailerServiceUrl;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ApiIgnore
	String home() {
		return "Welcome to Two Wheeler MicroServices";
	}
	
	@RequestMapping(value = "/Services/Product/TwoWheeler/getQuote", method = RequestMethod.POST, consumes = {
			"application/json", "application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseBody
	@ApiOperation(value="To perform get Quote process")
	public String getQuoteDetails(
			@ApiParam(name = "GetQuoteRequest", value = "value to be send", required = true) @RequestBody CalculatePremiumRequest calculatePremiumRequest,
			@RequestHeader HttpHeaders headers, HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
		
		ThreadContext.put("emailId", calculatePremiumRequest.getProposerDetails().getEmailId());
		
		Date requestTime = new Date();
		String requestString = "";
		
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();
		String responseString = null;
		
		try {
			requestString = xmlJsonMarshaller.marshalResponse(calculatePremiumRequest,
					headers.getContentType().getType(), false);
			if (twoWheelerDAO.validateApiKey(calculatePremiumRequest.getAuthenticationDetails().getApikey(),calculatePremiumRequest.getAuthenticationDetails().getAgentId())) {
			calculatePremiumRequest.setProcess("getAQuote");
			motorInsuranceModel = requestFactory.getMotorInsuranceModel(headers.getContentType().toString(), calculatePremiumRequest);
			LOGGER.info("STP = "+calculatePremiumRequest.getSource());
				List<String> errorCodes = validation.validateGetQuote(motorInsuranceModel);/*
											 * StringUtils.equalsIgnoreCase("NewSite",
											 * calculatePremiumRequest.getSource()) ? new ArrayList<String>() :
											 */
				if (errorCodes.isEmpty()) {
				
				boolean isquotepurchased = StringUtils.isNotBlank(motorInsuranceModel.getQuoteId()) ? twoWheelerDAO.checkQuotePurchasedStatus(motorInsuranceModel.getQuoteId()) : false;

				if (!isquotepurchased) {
					premiumDetails = (PremiumDetails) twoWheelerServiceFactory.getService(motorInsuranceModel);
				} else {
					LOGGER.info("The (" + motorInsuranceModel.getQuoteId() + ") has been purchased already or Doesn't exists.");
					status = new Status();
					status.setMessage("The (" + motorInsuranceModel.getQuoteId() + ") has been purchased already or Doesn't exists.");
					premiumDetails.setStatus(status);
				}
			} else {
				LOGGER.info("E-0002,  Input Values Failed " + motorInsuranceModel.getStatusFlag());
				status = new Status();
				status.setStatusCode(errorCodes.get(0));
				status.setMessage(errorcodeMessages.get(errorCodes.get(0)));
				premiumDetails.setStatus(status);
			}	
			}else{
				status = new Status();
				status.setMessage("API Key and AgentId must have to access the our WebServices");
				status.setStatusCode("E-0001");
				premiumDetails.setStatus(status);
			}
			
		} catch (IFoundryException e) {
			LOGGER.info(e.getMessage(), e);
			status = new Status();
			status.setStatusCode("E-0003");
			status.setMessage(e.getMessage());
			premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			
			status = new Status();
			status.setStatusCode("E-0001");
			status.setMessage("While processing your requests we facing some difficulties. Please try again later!");
			premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			if (responseString == null)
				responseString = xmlJsonMarshaller.marshalResponse(premiumDetails,
						CollectionUtils.isNotEmpty(headers.getAccept()) ? headers.getAccept().get(0).toString() : null, true);

			cassandraInsertions.saveDWebserviceUseDetails(
					motorInsuranceModel != null ? motorInsuranceModel.getQuoteId() : null,
					requestTime,
					motorInsuranceModel != null ? motorInsuranceModel.getAgentId() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getClientCode() : null,
					null,
					motorInsuranceModel != null ? motorInsuranceModel.getStrEmail() : null,
//					premiumDetails.getData() != null ? premiumDetails.getData().getGrossPremium() != null ? Double.parseDouble(premiumDetails.getData().getGrossPremium()) : 0 : 0,
					premiumDetails.getData() != null && premiumDetails.getData().getGrossPremium() != null
							&& NumberUtils.isParsable(premiumDetails.getData().getGrossPremium())
							? NumberUtils.createDouble(premiumDetails.getData().getGrossPremium()) : null,
					requestString,
					request.getServletPath(),
					responseString, "V0", "N", request);

			ThreadContext.clearAll();
		}
		
		return responseString;
	}
	
	@RequestMapping(value = "/Services/Product/TwoWheeler/CalculatePremium", method = RequestMethod.POST, consumes = {
			"application/json", "application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseBody
	@ApiOperation(value="To perform calculatepremium process")
	public String getCalculatePremium(
			@ApiParam(name = "calculatePremiumRequest", value = "value to be send", required = true)
			@RequestBody CalculatePremiumRequest calculatePremiumRequest,
			@RequestHeader HttpHeaders headers,
			HttpServletResponse response,
			HttpServletRequest request) {
		
		ThreadContext.put("emailId", calculatePremiumRequest.getProposerDetails().getEmailId());
		
		Date requestTime = new Date();
		String requestString = "";
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();
		String responseString = null;

		try {
			requestString = xmlJsonMarshaller.marshalResponse(calculatePremiumRequest,
					headers.getContentType().getType(), false);
			
			if (twoWheelerDAO.validateApiKey(calculatePremiumRequest.getAuthenticationDetails().getApikey(),calculatePremiumRequest.getAuthenticationDetails().getAgentId())) {
			calculatePremiumRequest.setProcess("CalculatePremium");
			motorInsuranceModel = requestFactory.getMotorInsuranceModel(headers.getContentType().toString(), calculatePremiumRequest);
			List<String> errorCodes = validation.validateCalculatePremium(motorInsuranceModel);
			if (errorCodes.isEmpty()) {

				LOGGER.info("XXXXXXXXXXXXXXXXXXXXXX CHANNEL: " + calculatePremiumRequest.getChannelcode());
				
				boolean isquotepurchased = StringUtils.isNotBlank(motorInsuranceModel.getQuoteId()) ? twoWheelerDAO.checkQuotePurchasedStatus(motorInsuranceModel.getQuoteId()) : false;
				
				if (!isquotepurchased) {
					premiumDetails = (PremiumDetails) twoWheelerServiceFactory.getService(motorInsuranceModel);
				} else {

					DQuoteDetails dQuoteDetails = twoWheelerDAO.getQuoteDetails(motorInsuranceModel.getQuoteId());
					dQuoteDetails.setChannelCode(calculatePremiumRequest.getChannelcode());
					List<DPolicyPurchasedDetails> dPolicyPurchasedList = new ArrayList<>(dQuoteDetails.getDPolicyPurchasedDetails());
					List<DUpdatedClientDetails> dUpdatedClientDetailsList = new ArrayList<>(dQuoteDetails.getDUpdatedClientDetailses());
					DUpdatedClientDetails dUpdatedClientDetails = dUpdatedClientDetailsList.isEmpty() ? null : dUpdatedClientDetailsList.get(0);
					PremiumDetailsData  premiumDetailsData = new PremiumDetailsData();
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					premiumDetailsData.setQuoteId(dQuoteDetails.getQuoteId());
					premiumDetailsData.setPolicyNumber(dPolicyPurchasedList.stream().map(e -> e.getPolicyNumber()).collect(Collectors.joining(",")));
					premiumDetailsData.setBuyDate(dPolicyPurchasedList.get(0).getBuyDate());
					premiumDetailsData.setInceptionDate(dPolicyPurchasedList.get(0).getStartDate());
					premiumDetailsData.setExpiryDate(dPolicyPurchasedList.get(0).getExpiryDate());
					premiumDetailsData.setPremiumPaid(dPolicyPurchasedList.get(0).getPremium().toString());
					StringBuilder downloadLink = new StringBuilder(mailerServiceUrl+"/Services/Mailer/DownloadPdf");
					downloadLink.append("?quoteId="+dQuoteDetails.getQuoteId()+"&type=PurchasedPdf");
					downloadLink.append("&expiryDate="+formatter.format(dPolicyPurchasedList.get(0).getExpiryDate()));
					if(dUpdatedClientDetails != null && StringUtils.isNotBlank(dUpdatedClientDetails.getDob()) ){
						downloadLink.append("&proposerDob="+dUpdatedClientDetails.getDob());
					}
					premiumDetailsData.setPolicyDownloadLink(downloadLink.toString());
					status.setMessage("Your Policy has been already been Purchased");
					premiumDetails.setData(premiumDetailsData);
					premiumDetails.setStatus(status);
				}
			} else {
				LOGGER.info("E-0002,  Input Values Failed " + motorInsuranceModel.getStatusFlag());
				status = new Status();
				status.setStatusCode(errorCodes.get(0));
				status.setMessage(errorcodeMessages.get(errorCodes.get(0)));
				premiumDetails.setStatus(status);
			}
			}else{
				status = new Status();
				status.setMessage("API Key and AgentId must have to access the our WebServices");
				status.setStatusCode("E-0001");
				premiumDetails.setStatus(status);
			}
			
		} catch (IFoundryException e) {
			LOGGER.info(e.getMessage(), e);
			status = new Status();
			status.setStatusCode("E-0003");
			status.setMessage(e.getMessage());
			premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			status = new Status();
			status.setStatusCode("E-0001");
			status.setMessage("While processing your requests we facing some difficulties. Please try again later!");
			premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			if (responseString == null)
				responseString = xmlJsonMarshaller.marshalResponse(premiumDetails,
						CollectionUtils.isNotEmpty(headers.getAccept()) ? headers.getAccept().get(0).toString() : null, true);

			ThreadContext.clearAll();
			cassandraInsertions.saveDWebserviceUseDetails(
					motorInsuranceModel != null ? motorInsuranceModel.getQuoteId() : null,
					requestTime,
					motorInsuranceModel != null ? motorInsuranceModel.getAgentId() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getClientCode() : null,
					null,
					motorInsuranceModel != null ? motorInsuranceModel.getStrEmail() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getPremium() : null,
					requestString,
					request.getServletPath(),
					responseString, "V0", "N", request);
		}

		return responseString;
	}

	@RequestMapping(value = "/Services/Product/TwoWheeler/UpdateVehicleDetails", method = RequestMethod.POST, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseBody
	@ApiOperation(value="To perform UpdateVehicleDetails process")
	public String updateVehivleDetails(@ApiParam(name = "UpdateVehicleDetailsRequest", value = "value to be send", required = true) @RequestBody CalculatePremiumRequest calculatePremiumRequest,
			@RequestParam(required = false) String quoteId,@RequestHeader HttpHeaders headers, HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
		
		ThreadContext.put("emailId", calculatePremiumRequest.getProposerDetails().getEmailId());
		
		Date requestTime = new Date();
		String requestString = "";
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();
		String responseString = null;

		try {
			
			requestString = xmlJsonMarshaller.marshalResponse(calculatePremiumRequest,
					headers.getContentType().getType(), false);
			
			if (twoWheelerDAO.validateApiKey(calculatePremiumRequest.getAuthenticationDetails().getApikey(),calculatePremiumRequest.getAuthenticationDetails().getAgentId())) {
			calculatePremiumRequest.setProcess("UpdateVehicleDetails");
			
			motorInsuranceModel = requestFactory.getMotorInsuranceModel(headers.getContentType().toString(), calculatePremiumRequest);
			
			if(StringUtils.isNotBlank(quoteId)) {
				motorInsuranceModel.setQuoteId(quoteId);	
			}
			//[Task #228791] Break-In Insurance for Two Wheeler - TECH
			if(StringUtils.isNotBlank(motorInsuranceModel.getQuoteId())) {
				motorInsuranceModel.setPreviousQuoteDate(twoWheelerDAO.getQuoteDetails(motorInsuranceModel.getQuoteId()).getQuoteDate());
			}
			List<String> errorCodes = validation.validateUpdateVehicledetails(motorInsuranceModel);
			if (errorCodes.isEmpty()) {
				boolean isquotepurchased = StringUtils.isNotBlank(motorInsuranceModel.getQuoteId()) ? twoWheelerDAO.checkQuotePurchasedStatus(motorInsuranceModel.getQuoteId()) : false;
				if (!isquotepurchased) {
					premiumDetails = (PremiumDetails) twoWheelerServiceFactory.getService(motorInsuranceModel);
				} else {

					DQuoteDetails dQuoteDetails = twoWheelerDAO.getQuoteDetails(motorInsuranceModel.getQuoteId());
					List<DPolicyPurchasedDetails> dPolicyPurchasedList = new ArrayList<>(dQuoteDetails.getDPolicyPurchasedDetails());
					List<DUpdatedClientDetails> dUpdatedClientDetailsList = new ArrayList<>(dQuoteDetails.getDUpdatedClientDetailses());
					DUpdatedClientDetails dUpdatedClientDetails = dUpdatedClientDetailsList.isEmpty() ? null : dUpdatedClientDetailsList.get(0);
					PremiumDetailsData  premiumDetailsData = new PremiumDetailsData();
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					premiumDetailsData.setQuoteId(dQuoteDetails.getQuoteId());
					premiumDetailsData.setPolicyNumber(dPolicyPurchasedList.stream().map(e -> e.getPolicyNumber()).collect(Collectors.joining(",")));
					premiumDetailsData.setBuyDate(dPolicyPurchasedList.get(0).getBuyDate());
					premiumDetailsData.setInceptionDate(dPolicyPurchasedList.get(0).getStartDate());
					premiumDetailsData.setExpiryDate(dPolicyPurchasedList.get(0).getExpiryDate());
					premiumDetailsData.setPremiumPaid(dPolicyPurchasedList.get(0).getPremium().toString());
					StringBuilder downloadLink = new StringBuilder(mailerServiceUrl+"/Services/Mailer/DownloadPdf");
					downloadLink.append("?quoteId="+dQuoteDetails.getQuoteId()+"&type=PurchasedPdf");
					downloadLink.append("&expiryDate="+formatter.format(dPolicyPurchasedList.get(0).getExpiryDate()));
					if(dUpdatedClientDetails != null && StringUtils.isNotBlank(dUpdatedClientDetails.getDob()) ){
						downloadLink.append("&proposerDob="+dUpdatedClientDetails.getDob());
					}
					premiumDetailsData.setPolicyDownloadLink(downloadLink.toString());
					status.setMessage("Your Policy has been already been Purchased");
					premiumDetails.setData(premiumDetailsData);
					premiumDetails.setStatus(status);
					
				}
			} else {
				LOGGER.info("E-0002,  Input Values Failed " + motorInsuranceModel.getStatusFlag() + errorCodes.get(0));
				status = new Status();
				status.setStatusCode(errorCodes.get(0));
				status.setMessage(errorcodeMessages.get(errorCodes.get(0)));
				premiumDetails.setStatus(status);
				premiumDetails.setData(null);
			}	
			}else{
				status = new Status();
				status.setMessage("API Key and AgentId must have to access the our WebServices");
				status.setStatusCode("E-0001");
				premiumDetails.setStatus(status);
			}
			
		} catch (IFoundryException e) {
			LOGGER.info(e.getMessage(), e);
			status = new Status();
			status.setStatusCode("E-0003");
			status.setMessage(e.getMessage());
			premiumDetails.setData(null);
			premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			status = new Status();
			status.setStatusCode("E-0001");
			status.setMessage("While processing your requests we facing some difficulties. Please try again later!");
			premiumDetails.setData(null);
			premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			if (responseString == null)
				responseString = xmlJsonMarshaller.marshalResponse(premiumDetails,
						CollectionUtils.isNotEmpty(headers.getAccept()) ? headers.getAccept().get(0).toString() : null, true);

			ThreadContext.clearAll();
			cassandraInsertions.saveDWebserviceUseDetails(
					motorInsuranceModel != null ? motorInsuranceModel.getQuoteId() : null,
					requestTime,
					motorInsuranceModel != null ? motorInsuranceModel.getAgentId() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getClientCode() : null,
					null,
					motorInsuranceModel != null ? motorInsuranceModel.getStrEmail() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getPremium() : null,
					requestString,
					request.getServletPath(),
					responseString, "V2", "N", request);
		}
		return responseString;
	}

	@RequestMapping(value = "/Services/Product/TwoWheeler/GProposalService", method = RequestMethod.POST, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseBody
	@ApiOperation(value="To perform GProposalService process")
	public String gProposalService(@ApiParam(name = "gProposalRequest", value = "value to be send", required = true) @RequestBody GProposalRequest gProposalRequest,
			@RequestHeader HttpHeaders headers, HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
		
		ThreadContext.put("emailId", gProposalRequest.getEmailId());
		Date requestTime = new Date();
		String requestString = "";
		
		PremiumDetails premiumDetails = new PremiumDetails();
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();
		String responseString = null;

		try {

			requestString = xmlJsonMarshaller.marshalResponse(gProposalRequest,
					headers.getContentType().getType(), false);
			if (twoWheelerDAO.validateApiKey(gProposalRequest.getAuthenticationDetails().getApikey(),gProposalRequest.getAuthenticationDetails().getAgentId())) {
			LOGGER.info("isquotepurchased ",gProposalRequest.getQuoteId());
			boolean isquotepurchased = StringUtils.isNotBlank(gProposalRequest.getQuoteId()) ? twoWheelerDAO.checkQuotePurchasedStatus(gProposalRequest.getQuoteId()) : false;
			LOGGER.info("isquotepurchased ",isquotepurchased);
			motorInsuranceModel.setProcessType("GProposalService");
			motorInsuranceModel.setQuoteId(gProposalRequest.getQuoteId());
			motorInsuranceModel.setEmailId(gProposalRequest.getEmailId());
			// @Paul - NOTE - will throw a number format exception - refactor this to handle this scenario in future
			try {
				motorInsuranceModel.setPremium(Double.parseDouble(gProposalRequest.getPremium()));	
			} catch (NullPointerException | NumberFormatException e) {
				motorInsuranceModel.setPremium(0); // We will handle this validation
			}
			

			if (!isquotepurchased) {
				LOGGER.info("11");
				premiumDetails = (PremiumDetails) twoWheelerServiceFactory.getService(motorInsuranceModel);
			}else{
				DQuoteDetails dQuoteDetails = twoWheelerDAO.getQuoteDetails(gProposalRequest.getQuoteId());
				List<DPolicyPurchasedDetails> dPolicyPurchasedList = new ArrayList<>(dQuoteDetails.getDPolicyPurchasedDetails());
				List<DUpdatedClientDetails> dUpdatedClientDetailsList = new ArrayList<>(dQuoteDetails.getDUpdatedClientDetailses());
				DUpdatedClientDetails dUpdatedClientDetails = dUpdatedClientDetailsList.isEmpty() ? null : dUpdatedClientDetailsList.get(0);
				PremiumDetailsData  premiumDetailsData = new PremiumDetailsData();
				Status status = new Status();
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				premiumDetailsData.setQuoteId(dQuoteDetails.getQuoteId());
				premiumDetailsData.setPolicyNumber(dPolicyPurchasedList.stream().map(e -> e.getPolicyNumber()).collect(Collectors.joining(",")));
				premiumDetailsData.setBuyDate(dPolicyPurchasedList.get(0).getBuyDate());
				premiumDetailsData.setInceptionDate(dPolicyPurchasedList.get(0).getStartDate());
				premiumDetailsData.setExpiryDate(dPolicyPurchasedList.get(0).getExpiryDate());
				premiumDetailsData.setPremiumPaid(dPolicyPurchasedList.get(0).getPremium().toString());
				LOGGER.info("1" +mailerServiceUrl);
				StringBuilder downloadLink = new StringBuilder(mailerServiceUrl+"/Services/Mailer/DownloadPdf");
				downloadLink.append("?quoteId="+dQuoteDetails.getQuoteId()+"&type=PurchasedPdf");
				downloadLink.append("&expiryDate="+formatter.format(dPolicyPurchasedList.get(0).getExpiryDate()));
				if( dUpdatedClientDetails != null && StringUtils.isNotBlank(dUpdatedClientDetails.getDob()) ){
					downloadLink.append("&proposerDob="+dUpdatedClientDetails.getDob());
				}
				premiumDetailsData.setPolicyDownloadLink(downloadLink.toString());
				status.setMessage("Your Policy has been already been Purchased");
				premiumDetails.setData(premiumDetailsData);
				premiumDetails.setStatus(status);
				
			
			}
			}else{
				Status status = new Status();
				status.setMessage("API Key and AgentId must have to access the our WebServices");
				status.setStatusCode("E-0001");
				premiumDetails.setStatus(status);
			}
		}  catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			  Status status = new Status(); 	                         
			  status.setStatusCode("E-0001"); 	                        
			 status.setMessage("While processing your requests we facing some difficulties. Please try again later!");
			 premiumDetails.setStatus(status);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			LOGGER.info("headers.getAccept().get(0).toString() ::: " + headers.getAccept().get(0).toString());
			if (responseString == null)
				responseString = xmlJsonMarshaller.marshalResponse(premiumDetails, headers.getAccept().get(0).toString(), true);
			ThreadContext.clearAll();
			cassandraInsertions.saveDWebserviceUseDetails(
					motorInsuranceModel != null ? motorInsuranceModel.getQuoteId() : null,
					requestTime,
					motorInsuranceModel != null ? motorInsuranceModel.getAgentId() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getClientCode() : null,
					gProposalRequest.getIsOTPVerified(),
					motorInsuranceModel != null ? motorInsuranceModel.getEmailId() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getPremium() : null,
					requestString,
					request.getServletPath(),
					responseString, "V3", "N", request);
		}
		return responseString;
	}
	@ExceptionHandler(JsonProcessingException.class)
	void handleJsonProcessingException(HttpServletResponse response) throws java.io.IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(Exception.class)
	void handleException(HttpServletResponse response) throws java.io.IOException {
		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"While processing your requests we facing some difficulties. Please try again later!");
	}
	
	private PremiumDetails handleInvalidRequest(Set<ConstraintViolation<CalculatePremiumRequest>> violations) {
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		status.setStatusCode("E-0002");
		status.setMessage("Invalid Request");
		premiumDetails.setStatus(status);
		try {
			LOGGER.info("violations size ::: " + violations.size());
			Iterator<ConstraintViolation<CalculatePremiumRequest>> i = violations.iterator();
			List<FieldErrorResource> errorResources = new ArrayList<FieldErrorResource>();
			while (i.hasNext()) {
				FieldErrorResource fieldErrorResource = new FieldErrorResource();
				ConstraintViolation<CalculatePremiumRequest> c = i.next();
				String a = c.getInvalidValue() != null ? c.getInvalidValue().toString() : null;

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				if (c.getInvalidValue() == null) {
					a = "null";
				}
				if (c.getInvalidValue() instanceof Date) {
					a = sdf.format(c.getInvalidValue());
				}
				
				if (c.getInvalidValue() instanceof ProposerDetails) {
					BeanWrapperImpl wrapper = new BeanWrapperImpl(c.getInvalidValue());
					Object o = wrapper.getPropertyValue(c.getPropertyPath().toString().split("\\.")[1]);
					if (o instanceof Date) {
						a = new SimpleDateFormat("dd/MM/yyyy").format(o);
					}

					if (o instanceof String) {
						a = (String) o;
					}

					if (o instanceof Integer) {
						a = String.valueOf(o);
					}
					if (o == null) {
						a = "null";
					}
				}

				fieldErrorResource.setValueGiven(a);
				fieldErrorResource.setMessage(c.getMessage());
				LOGGER.info("c.getLeafBean() ::: " + c.getExecutableParameters());
				fieldErrorResource.setField(
						"[" + c.getPropertyPath().toString().replaceAll("travellerDetails.", "travellerDetails") + "]");

				errorResources.add(fieldErrorResource);
			}
			LOGGER.info("errorResources.size() ::: " + errorResources.size());
			premiumDetails.setFieldErrors(errorResources);
		} catch (Exception e2) {
			LOGGER.info(e2.getMessage(), e2);
		}
		return premiumDetails;

	}
}
