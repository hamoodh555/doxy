/**
 * 
 */
package com.xerago.rsa.service;

import javax.jms.MapMessage;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.dto.response.Data;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.PremiumDetailsData;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.result.UpdateVehicleDetailsResult;
import com.xerago.rsa.util.jms.EmailMessageSender;

/**
 * @author pandiaraj
 *
 */
@Service
public class GProposalServices {
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Autowired
	EmailMessageSender emailMessageSender;
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	public PremiumDetails gProposalService(MotorInsuranceModel motorInsuranceModel, String requestParam) throws Exception {
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status = new Status();
		Integer updateStatus = twoWheelerDAO.GproposalService(motorInsuranceModel.getQuoteId(), motorInsuranceModel);
		LOGGER.info("updateStatus ::: " + updateStatus);
		if (updateStatus == 1) {
			PremiumDetailsData premiumDetailsData = new PremiumDetailsData();
			premiumDetailsData.setClientCode(motorInsuranceModel.getClientCode());
			premiumDetailsData.setQuoteId(motorInsuranceModel.getQuoteId());
			premiumDetailsData.setPremium(motorInsuranceModel.getPremium() + "");
			premiumDetailsData.setDescriptions("Vehicle Additional details successfully checked");
			status.setMessage("Quote Approved,Proceed Buy Policy");
			status.setStatusCode("S-0005");
			premiumDetails.setData(premiumDetailsData);
			
			try{
				MapMessage mapMessage = new ActiveMQMapMessage();
				mapMessage.setString("quoteId", motorInsuranceModel.getQuoteId());
				mapMessage.setBoolean("isProposal", true);
				boolean isMailSent =twoWheelerDAO.getmailStatus(motorInsuranceModel.getQuoteId());
				if(isMailSent == true){
							LOGGER.info("Proposal Pdf trigger success");
					}else{
						emailMessageSender.sendMessage(mapMessage,true);
					}
			}catch (Exception e) {
				LOGGER.info("Proposal Pdf trigger failure", e);
			}

			
		} else {
			status.setMessage(motorInsuranceModel.getStatusFlag());
			status.setStatusCode(motorInsuranceModel.getStatusCode());
		}
		premiumDetails.setStatus(status);
		return premiumDetails;
	}

}
