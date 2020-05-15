package com.xerago.rsa.webproxy.transform;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class XmlJsonMarshaller {
	
	@Autowired
	@Qualifier("ObjectMapperWithoutRoot")
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("ObjectMapperWithRoot")
	private ObjectMapper objectMapperWithRoot;
	
	@Autowired
	XmlMapper xmlMapper;
	
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	public String marshalResponse(Object classObject, String mediaType, boolean includeRoot) {
		try {
			LOGGER.info("classObject ::: " + objectMapper.writeValueAsString(classObject));
			if (classObject == null || classObject instanceof String) {
				return (String) classObject;
			} else {
				if (StringUtils.isNotBlank(mediaType) && StringUtils.containsIgnoreCase(mediaType, "xml")) {
					return xmlMapper.writeValueAsString(classObject);
				} else {
					if (includeRoot) {
						return objectMapperWithRoot.writeValueAsString(classObject);
					} else {
						return objectMapper.writeValueAsString(classObject);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(),e );
		}
		return null;
	}
}
