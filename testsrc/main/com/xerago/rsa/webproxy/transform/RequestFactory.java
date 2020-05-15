/**
 * 
 */
package com.xerago.rsa.webproxy.transform;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.xerago.rsa.model.MotorInsuranceModel;

/**
 * @author pandiaraj
 *
 */
@Component
public class RequestFactory {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	ApplicationContext context;
	
	public <T> MotorInsuranceModel getMotorInsuranceModel(String acceptType, T t) throws Exception {
		MotorInsuranceModel motorInsuranceModel = null;
		LOGGER.info("acceptType ::: " + acceptType);
		if(acceptType.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED)) {
			motorInsuranceModel = context.getBean("formPost", FormPost.class).parseValue(t);
		} else if(acceptType.equalsIgnoreCase(MediaType.APPLICATION_JSON) || acceptType.equalsIgnoreCase(MediaType.APPLICATION_XML)) {
			motorInsuranceModel = context.getBean("jsonXml", JsonXml.class).parseValue(t);
		} else {
			throw new IllegalArgumentException("Unknown Request");
		}
		return motorInsuranceModel;
	}

}
