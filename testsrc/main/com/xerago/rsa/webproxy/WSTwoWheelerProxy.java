package com.xerago.rsa.webproxy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xerago.rsa.cassandra.dao.CassandraInsertions;
import com.xerago.rsa.communication.IPCServices;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.service.ProcessFactory;
import com.xerago.rsa.util.MotorUtils;
import com.xerago.rsa.validation.Validation;
import com.xerago.rsa.webproxy.transform.RequestFactory;
import com.xerago.rsa.webproxy.transform.XmlJsonMarshaller;

import io.swagger.annotations.ApiOperation;

@RestController
@Transactional
@RequestMapping(value = "/Services/Product")
public class WSTwoWheelerProxy {

	private static final Logger LOGGER = LogManager.getRootLogger();

	@Autowired
	XmlJsonMarshaller xmlJsonMarshaller;
	
	@Autowired
	CassandraInsertions cassandraInsertions; 
	
	@Autowired
	ProcessFactory twoWheelerServiceFactory;

	@Autowired
	RequestFactory requestFactory;

	@Autowired
	TwoWheelerDAO twoWheelerDAO;

	@Resource(name = "errorcodeMessages")
	private Map<String, String> errorcodeMessages;

	@Autowired
	IPCServices ipcServices;

	@Autowired
	Validation validation;

	@ApiOperation(hidden = true, value = "")
	@RequestMapping(value = "/TwoWheelerService", method = RequestMethod.POST, consumes = {
			"application/x-www-form-urlencoded" }, produces = { "application/json", "application/xml" })
	@ResponseBody
	public String getTwoWheelerService(@RequestParam Map<String, String> requestString,
			@RequestHeader HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {
		LOGGER.info("WebProxyController -  getTwoWheelerService Start");
		
		Date requestTime = new Date();		
		PremiumDetails premiumDetails = new PremiumDetails();
		String responseCode = "";
		Boolean isquotepurchased = false;
		MotorInsuranceModel motorInsuranceModel = new MotorInsuranceModel();
		
		HashMap<String, String> requestMap = MotorUtils.getTrimRequestString(requestString);
		try {
			ThreadContext.put("emailId", requestMap.get("strEmail"));
			LOGGER.info("requestString ::: " + requestMap);
			LOGGER.info("requestString process ::: " + requestMap.get("process"));
			if ("fileUpload".equalsIgnoreCase(requestMap.get("process"))) {

				premiumDetails = ipcServices.fileUpload(requestMap);

			} else if ("checkUploadStatus".equalsIgnoreCase(requestMap.get("process"))) {

				premiumDetails = ipcServices.checkUploadStatus(requestMap);

			} else {

				motorInsuranceModel = requestFactory
						.getMotorInsuranceModel(headers.getContentType().toString(), requestMap);
				List<String> errorCodes = getErrorCodes(motorInsuranceModel);

				if (CollectionUtils.isEmpty(errorCodes)) {

					if (StringUtils.isNotBlank(motorInsuranceModel.getQuoteId())) {
						LOGGER.info("WStwowheelerproxy:::" + motorInsuranceModel.getQuoteId());
						isquotepurchased = twoWheelerDAO.checkQuotePurchasedStatus(motorInsuranceModel.getQuoteId());
					}
					if (!isquotepurchased) {
						premiumDetails = (PremiumDetails) twoWheelerServiceFactory.getService(motorInsuranceModel);

					} else {
						LOGGER.info("The (" + motorInsuranceModel.getQuoteId()
								+ ") has been purchased already or Doesn't exists.");
						Status status = new Status();
						status.setMessage("The (" + motorInsuranceModel.getQuoteId()
								+ ") has been purchased already or Doesn't exists.");
						premiumDetails.setStatus(status);
					}
				} else {
					LOGGER.info("E-0002,  Input Values Failed " + motorInsuranceModel.getStatusFlag());
					Status status = new Status();
					status.setStatusCode(errorCodes.get(0));
					status.setMessage(errorcodeMessages.get(errorCodes.get(0)));
					premiumDetails.setStatus(status);
				}
			}

			responseCode = xmlJsonMarshaller.marshalResponse(premiumDetails, requestMap.get("reqType"), true);

		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseCode = "{\"PREMIUMDETAILS\" :{\"Status\" : {\"StatusCode\" : \"E-0002\",\"Message\" : \"Premium Calculation FAILED. UNABLE TO PROCESS REQUEST\"}}}";
		} finally {
			cassandraInsertions.saveDWebserviceUseDetails(
					motorInsuranceModel != null ? motorInsuranceModel.getQuoteId() : null,
					requestTime,
					motorInsuranceModel != null ? motorInsuranceModel.getAgentId() : null,
					motorInsuranceModel != null ? motorInsuranceModel.getClientCode() : null,
					null,
					motorInsuranceModel != null ? motorInsuranceModel.getStrEmail() : null,
					premiumDetails.getData() != null && premiumDetails.getData().getGrossPremium() != null
							&& NumberUtils.isParsable(premiumDetails.getData().getGrossPremium())
							? NumberUtils.createDouble(premiumDetails.getData().getGrossPremium()) : null,
					MotorUtils.requestFormPostToString(requestMap),
					motorInsuranceModel.getProcessType(),
					responseCode, "V0", "N", request);

			ThreadContext.clearAll();
		}
		LOGGER.info("getTwoWheelerService  - Response " + responseCode);
		ThreadContext.clearAll();
		return responseCode;
	}

	private List<String> getErrorCodes(MotorInsuranceModel motorInsuranceModel) throws Exception {
		if ("getAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType()))
			return validation.validateGetQuote(motorInsuranceModel);
		else if ("CalculatePremium".equalsIgnoreCase(motorInsuranceModel.getProcessType()))
			return validation.validateCalculatePremium(motorInsuranceModel);
		else if ("UpdateVehicleDetails".equalsIgnoreCase(motorInsuranceModel.getProcessType()))
			return validation.validateUpdateVehicledetails(motorInsuranceModel);
		else
			return null;
	}

}
