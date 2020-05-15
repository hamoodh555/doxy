package com.xerago.rsa.bean.validation.constraints;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Fields {
	
	private static final Logger LOGGER = LogManager.getRootLogger();

	private Fields(){
		
	}
	
	public static boolean isAnyNotBlank(Object object) {
		boolean proceed = false;
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				Object o = field.get(object);
				field.setAccessible(false);
				if(field.getType() == String.class && o !=null && StringUtils.isNotBlank((String)o)) {
					proceed = true;
				}
				
				if(field.getType() == Date.class && o !=null) {
					proceed = true;
				}
	
				if(proceed){
					break;
				}
			}
			
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		
		return proceed;
		
		
	}
}
