package com.hp.it.spf.wsrp.log;

import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.misc.Utils;
import com.vignette.portal.log.LogWrapper;

import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;
import javax.servlet.http.HttpServletRequest;

/**
 * A handler capturing WSRP execution time. This class relies on {@link TimeRecorder} to
 * peform this logging.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class WsrpTimeRecordingHandler extends BasicHandler {
	private static final LogWrapper LOG = new LogWrapper(WsrpTimeRecordingHandler.class);


	public void invoke(MessageContext messageContext) throws AxisFault {
		if (!Predicates.isWsrpBaseCall(messageContext)) {
			return;
		}

		try {
			TimeRecorder timeRecorder = getTimeRecorder(messageContext);
			// TimeRecorder is null for console requests
			if (timeRecorder != null) {
				if (!messageContext.getPastPivot()) {
					timeRecorder.recordStart(Operation.WSRP_CALL);
				}
				else {
					timeRecorder.recordEnd(Operation.WSRP_CALL);
				}
			}
		}
		catch (Exception e) {
			// Don't retrhow this exception as are in the loging code so the error is due to
			// logging infrastructure and probably does not impact proper application operation
			LOG.error("Error occured while recording time", e);
		}
	}

	@Override
	public void onFault(MessageContext messageContext) {
		if (!Predicates.isWsrpBaseCall(messageContext)) {
			return;
		}

		try {
			TimeRecorder timeRecorder = getTimeRecorder(messageContext);
			// TimeRecorder is null for console requests
			if (timeRecorder != null) {
				if (TimeRecorder.isOperationRecordingEnabled(Operation.WSRP_CALL)) {
					SOAPFault fault = getFault(messageContext);
					if (fault != null) {
						timeRecorder.recordError(Operation.WSRP_CALL, fault.getFaultCode() + ": " + fault.getFaultString());
					}
					else {
						timeRecorder.recordError(Operation.WSRP_CALL, null);
					}
				}
			}
		}
		catch (Exception e) {
			// Don't rethrow this exception as we are already in an error handler. Just log it.
			LOG.error("Error occured while logging WSRP error", e);
		}
	}

	private SOAPFault getFault(MessageContext messageContext) throws SOAPException {
		Message responseMessage = messageContext.getResponseMessage();
		if (responseMessage != null) {
			SOAPBody body = responseMessage.getSOAPPart().getEnvelope().getBody();
			if (body != null && body.hasFault()) {
				return body.getFault();
			}
		}
		return null;
	}


	private TimeRecorder getTimeRecorder(MessageContext messageContext) throws Exception {
		final String TIME_RECORDER_MC_KEY = TimeRecorder.class.getName();

		// Let's check first if we already have it in the context. This would be the case
		// when this method is called during web service response because it that case
		// we cannot retrieve the request as the required data is not available.
		TimeRecorder timeRecorder = (TimeRecorder) messageContext.getProperty(TIME_RECORDER_MC_KEY);
		if (timeRecorder != null) {
			return timeRecorder;
		}

		HttpServletRequest request = Utils.retrieveRequest(messageContext);
		if (request == null) {
			LOG.error("Cannot find request in based on messageContext content");
		}
		timeRecorder = (TimeRecorder) request.getAttribute(TimeRecorder.REQUEST_KEY);

		// We are probably in the web service request now. Let's save the timeRecorder we got
		// from the portal request in the messageContext so it can be used when this handler
		// gets called for web service response.
		if (timeRecorder != null) {
			messageContext.setProperty(TIME_RECORDER_MC_KEY, timeRecorder);
		}
		return timeRecorder;
	}

}
