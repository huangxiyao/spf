package com.hp.spp.servlets.monitoring;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class JMSConnector {

	private static Logger mLogger = Logger.getLogger(JMSConnector.class);

	private TopicPublisher mPublisher;

	private TopicSubscriber mSubscriber;

	private TopicConnection mConnection;
	
	private TopicSession mPubSession;

	/* Constructor. Establish JMS publisher and subscriber */
	public JMSConnector(String topicName) throws Exception {

		// InitialContext jndi = new InitialContext(env);
		InitialContext jndi = new InitialContext();

		// Look up a JMS connection factory
		TopicConnectionFactory conFactory = (TopicConnectionFactory) jndi
				.lookup("CacheJMSConnectionFactory");

		// Create a JMS connection
		mConnection = conFactory.createTopicConnection();

		// Create two JMS session objects
		mPubSession = mConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		TopicSession subSession = mConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Look up a JMS topic
		Topic chatTopic = (Topic) jndi.lookup(topicName);

		// Create a JMS publisher and subscriber
		mPublisher = mPubSession.createPublisher(chatTopic);
		mSubscriber = subSession.createSubscriber(chatTopic);

		// Start the JMS connection; allows messages to be delivered
		mConnection.start();
	}

	/* Create and send message using topic publisher */
	public void writeMessage(String text) throws JMSException {
		TextMessage message = mPubSession.createTextMessage();
		message.setText(text);
		mPublisher.publish(message);
	}

	public String readMessage() {
		String messageText = null;
		try {
			// wait ten seconds for the message
			TextMessage message = (TextMessage)mSubscriber.receive(10000);
			messageText = message.getText();
		} catch (JMSException e) {
			mLogger.error("Could not read message. Wait time was 10 seconds", e);
		}
		return messageText;
	}

	/* Close the JMS connection */
	public void close() throws JMSException {
		mConnection.close();
	}
}
