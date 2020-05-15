/**
 * 
 */
package com.xerago.rsa.util;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author pandiaraj
 *
 */
@Component
public class SMSSender {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	public void send(ActiveMQMapMessage activeMQMapMessage) throws Exception {
		this.jmsTemplate.convertAndSend("smsQueue", activeMQMapMessage );
		LOGGER.info("smsQueue triggered Successfully.... Posted into JMSQueue for this " + activeMQMapMessage.getString("QuoteId"));
	}
}
