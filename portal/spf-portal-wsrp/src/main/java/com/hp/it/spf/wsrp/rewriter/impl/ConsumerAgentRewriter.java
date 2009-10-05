package com.hp.it.spf.wsrp.rewriter.impl;


import oasis.names.tc.wsrp.v1.types.RegistrationData;

import org.apache.axis.MessageContext;

import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.rewriter.IRewriter;

/**
 * Rewrites the registration data by trimming the consumer agent name to 50 characters.
 * VAP 7.4 consumer agent value contains not only the portal name but also the version and build
 * number which makes the WSRP4J producer failing as its database column storing this data is
 * too small. 
 *  
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ConsumerAgentRewriter implements IRewriter {

	/**
	 * Max size of the user agent which will be sent.
	 */
	static final int CONSUMER_AGENT_MAX_SIZE = 50;
	
	/**
	 * Position at which the user agent name will be split to include its tail.
	 */
	private static final int CONSUMER_AGENT_SPLIT_POS = 20;
	
	/**
	 * Rewrites user agent value including in the result its head and tail.
	 * @param data registration data
	 */
	public void rewrite(Object data) {
		if (!(data instanceof RegistrationData)) {
			throw new IllegalArgumentException("RegistrationData expected but was: " + 
					(data == null ? "null" : data.getClass().getName()));
		}
		
		RegistrationData registrationData = (RegistrationData) data;
		String consumerAgent = registrationData.getConsumerAgent();
		if (consumerAgent == null || consumerAgent.length() <= CONSUMER_AGENT_MAX_SIZE) {
			return;
		}
		
		// Vignette sends in consumer agent value a version and build number in the tail.
		// Let's keep the tail.
		registrationData.setConsumerAgent(
				consumerAgent.substring(0, CONSUMER_AGENT_SPLIT_POS) + 
				"..." + 
				consumerAgent.substring(consumerAgent.length() - (CONSUMER_AGENT_MAX_SIZE - CONSUMER_AGENT_SPLIT_POS - 3)));
		
	}

	/**
	 * @param messageContext web service call message context
	 * @return <tt>true</tt> if the message context corresponds to WSRP4J producer "register" call.
	 */
	public boolean shouldApply(MessageContext messageContext) {
		return Predicates.isWsrp4jProducer(messageContext) && 
			Predicates.isAnyOfMethods(messageContext, "v1:register");
	}

}
