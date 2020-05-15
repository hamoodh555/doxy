package com.xerago.rsa.util.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EmailMessageSender {

	private static final Logger LOGGER = LogManager.getRootLogger();

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	ObjectMapper objectMapper;

	public void sendMessage(MapMessage message, boolean isPdfMessage) throws IOException, JMSException {

		if (isPdfMessage) {
			LOGGER.info("microPdfQueue" + jmsTemplate);
			jmsTemplate.convertAndSend("microPdfQueue", message);
		} else {
			LOGGER.info("microEmailQueue" + jmsTemplate);
			LOGGER.info("microEmailQueue" + message);
			jmsTemplate.convertAndSend("microEmailQueue", message);
		}

	}

}
