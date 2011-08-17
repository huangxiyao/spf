package com.hp.spp.servlets.monitoring;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

public class JMSMonitoringServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(JMSMonitoringServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("JMSMonitoringServlet called");
		}

		String responseMsg = "KO";

		try {
			if (JMSResponseOK() == true) {
				responseMsg = "OK";
			}
		} catch (Exception e) {
			logger.error("Error JMS Topic", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("JMSMonitoringServlet response message: "
					+ responseMsg);
		}

		response.getWriter().print(responseMsg);
	}

	private boolean JMSResponseOK() throws RemoteException, ServiceException,
			JMSException, Exception {

		JMSConnector connector = new JMSConnector("SPPTestJMSTopic");
		String valueToSend = "test";
		connector.writeMessage(valueToSend);
		
		String message = connector.readMessage();

		if (logger.isDebugEnabled()) {
			logger.debug("The sent value on JMS is [" + valueToSend
					+ "] and the read value is [" + message
					+ "]");
		}
		connector.close();
		if (valueToSend.equals(message)) {
			return true;
		} else {
			return false;
		}
	}

}
