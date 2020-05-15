package com.xerago.rsa.service;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.model.MotorInsuranceModel;

@Component
@Transactional
public class ProcessFactory {

	private static final Logger LOGGER = LogManager.getRootLogger();

	@Autowired
	ApplicationContext context;

	public Object getService(MotorInsuranceModel motorInsuranceModel) {
		PremiumDetails premiumDetails = null;
		String[] a = context.getBeanDefinitionNames();
		LOGGER.info(Arrays.toString(a));
		try {
			LOGGER.info("process ::: " + motorInsuranceModel.getProcessType());
			if ("GetAQuote".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
				
				premiumDetails = context.getBean(GetAQuote.class).getQuote(motorInsuranceModel);
				
			} else if ("CalculatePremium".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
				
				/*premiumDetails = context.getBean("calculatePremium" + motorInsuranceModel.getAgentId(), CalculatePremium.class)
								.calculatePremium(motorInsuranceModel);*/
				premiumDetails = context.getBean(CalculatePremium.class).calculatePremium(motorInsuranceModel);
				
			} else if ("UpdateVehicleDetails".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {

				if(StringUtils.equalsIgnoreCase("STP", motorInsuranceModel.getSource())) {
					premiumDetails = context.getBean(UpdateVehicleDetails.class).updateVehicleDetails(motorInsuranceModel);
				} else {
					premiumDetails = context.getBean(UpdateVehicleDetails.class).calculatePremium(motorInsuranceModel);
				}
				
			}else if("BreakInInsurance".equalsIgnoreCase(motorInsuranceModel.getProcessType())){
				
				premiumDetails = context.getBean(BreakInInsurance.class).breakInInsurance(motorInsuranceModel);
				
			}else if ("GProposalService".equalsIgnoreCase(motorInsuranceModel.getProcessType())) {
				
				return context.getBean(GProposalServices.class).gProposalService(motorInsuranceModel, "");
				
			} else {
				
				throw new IllegalArgumentException("Unknown Process");
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return premiumDetails;
	}
}
