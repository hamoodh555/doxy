/**
 * 
 */
package com.xerago.rsa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author pandiaraj
 *
 */
@Configuration

public class Properties {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	@Autowired
	Environment environment;
	
	public String getProperty(String key) {
		String value = "";
		try {
			value= environment.getProperty(key);
			LOGGER.info("value:: " + value);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} 
		return value;
	}
	
}
