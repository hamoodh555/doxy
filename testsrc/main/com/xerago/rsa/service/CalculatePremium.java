package com.xerago.rsa.service;

import javax.jms.MapMessage;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.xerago.rsa.domain.DUpdatedClientDetails;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.util.jms.EmailMessageSender;;

@Service
public class CalculatePremium extends Process {
	
	@Autowired
	EmailMessageSender emailMessageSender;
	
	@Override
	public void setPremiumDetailsStatus(PremiumDetails premiumDetails, Status status,
			MotorInsuranceModel motorInsuranceModel, DUpdatedClientDetails dUpdatedClientDetails) throws Exception {
		status.setStatusCode("S-0002");
		status.setMessage("Premium Calculated and Quote Saved Successfully");
		premiumDetails.setStatus(status);
		
		if ("RSAI".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
			MapMessage mapMessage  = new ActiveMQMapMessage();
			mapMessage.setString("quoteId", motorInsuranceModel.getQuoteId());
			mapMessage.setBoolean("isProposal", true);
			mapMessage.setString("motorInsuranceModel", new Gson().toJson(motorInsuranceModel));
			mapMessage.setString("updatedClientDetails", new Gson().toJson(dUpdatedClientDetails));
			mapMessage.setString("process", "TwoWheelerProposalMail");
			mapMessage.setString("user", "admin");
			mapMessage.setString("username", "admin");
			mapMessage.setString("password", "admin");
			emailMessageSender.sendMessage(mapMessage, false);
		}
		super.setPremiumDetailsStatus(premiumDetails, status, motorInsuranceModel, dUpdatedClientDetails);
	}
}
