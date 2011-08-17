package com.hp.spp.wsrp.context;

import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.log4j.Logger;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;

import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;

public class WsrpTimeRecordingHandler extends BasicHandler {
	private static final Logger mLog = Logger.getLogger(WsrpTimeRecordingHandler.class);

	public void invoke(MessageContext messageContext) throws AxisFault {
		if (UserContextInjector.isWsrpBaseCall(messageContext)) {
			TimeRecorder timeRecorder = (TimeRecorder) messageContext.getProperty(UserContextInjector.TIME_RECORDER_MC_KEY);
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
	}

	public void onFault(MessageContext messageContext) {
		if (UserContextInjector.isWsrpBaseCall(messageContext)) {
			TimeRecorder timeRecorder = (TimeRecorder) messageContext.getProperty(UserContextInjector.TIME_RECORDER_MC_KEY);
			// TimeRecorder is null for console requests
			if (timeRecorder != null) {
				// If this was WSRP_PROFILE error it would be already recorded so we don't want to
				// record it 2nd time here.
				boolean isFaultWhileInjectingProfile = (messageContext.getProperty(UserContextInjector.WSRP_PROFILE_ERROR_FLAG) != null);
				try {
					if (!isFaultWhileInjectingProfile && TimeRecorder.isOperationRecordingEnabled(Operation.WSRP_CALL)) {
						SOAPFault fault = getFault(messageContext);
						if (fault != null) {
							timeRecorder.recordError(Operation.WSRP_CALL, fault.getFaultCode() + ": " + fault.getFaultString());
						}
						else {
							timeRecorder.recordError(Operation.WSRP_CALL, null);
						}
					}
				}
				catch (SOAPException e) {
					// Don't rethrow this exception as we are already in an error handler. Just log it.
					mLog.error("Error occured while logging WSRP error", e);
				}
			}
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
}
