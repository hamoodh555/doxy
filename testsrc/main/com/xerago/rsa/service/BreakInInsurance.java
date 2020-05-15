package com.xerago.rsa.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.PremiumDetailsData;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.model.MotorInsuranceModel;

@Service
public class BreakInInsurance extends Process {

	private static final Logger LOGGER = LogManager.getRootLogger();

	public PremiumDetails breakInInsurance(MotorInsuranceModel motorInsuranceModel) {
		PremiumDetailsData data = new PremiumDetailsData();
		PremiumDetails premiumDetails = new PremiumDetails();
		LOGGER.info("TwoWheelerPrivateCar breakInInsuranceDetails");
		Status status = new Status();
		boolean breakinstatus = breakInInsurance(motorInsuranceModel.getApiKey(), motorInsuranceModel.getAgentId(),
				motorInsuranceModel.getQuoteId(), motorInsuranceModel.getStrTitle(), motorInsuranceModel.getStrFirstName(),
				motorInsuranceModel.getStrLastName(), motorInsuranceModel.getStrEmail());
		if (breakinstatus) {
			data.setDescription("BreakInInsurance Quote and Quote yet to Approve");
			premiumDetails.setData(data);
			status.setStatusCode("S-0005");
			status.setMessage("BreakInInsurance Quote and Quote yet to Approve");
			premiumDetails.setStatus(status);
		} else {
			data.setDescription("BreakInInsurance Quote and Quote Approved");
			premiumDetails.setData(data);
			status.setStatusCode("S-0006");
			status.setMessage("BreakInInsurance Quote and Quote Approved");
			premiumDetails.setStatus(status);
		}
		return premiumDetails;
	}

}
