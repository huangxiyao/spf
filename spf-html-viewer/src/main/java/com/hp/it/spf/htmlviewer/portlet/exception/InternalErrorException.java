/*
 * Project: Shared Portal Framework Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.htmlviewer.portlet.exception;

import java.util.LinkedHashMap;
import java.util.Iterator;

import javax.portlet.PortletRequest;

import com.hp.frameworks.wpa.portlet.transaction.Transaction;
import com.hp.frameworks.wpa.portlet.transaction.TransactionImpl;
import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.exception.portlet.SystemException;
import com.hp.websat.timber.model.StatusIndicator;

/**
 * <p>
 * A system exception used by the <code>html-viewer</code> portlet code whenever
 * a system error has occurred. For example, when no view filename is configured
 * by the time that the
 * {@link com.hp.it.spf.htmlviewer.portlet.web.ViewController} executes.
 * </p>
 * <p>
 * The error diagnostics or exception representing the system error are set into
 * this exception by the constructor. The constructor also sets the WPAP Timber
 * logging status to indicate a fatal error was encountered, and records the
 * error into the proper WPAP Timber logs (business, error, and errortrace).
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.exception.portlet.SystemException</code>
 */
public class InternalErrorException extends SystemException {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs the <code>InternalErrorException</code> given an error code
     * and diagnostic message describing the error condition. Also sets the WPAP
     * Timber logging status indicator for a fatal error, and generates log
     * entries for the error condition. Log entries include:
     * <ul>
     * <li>Error code and diagnostic message are added to the business log.</li>
     * <li>An error log entry for this <code>InternalErrorException</code>,
     * including the error code, diagnostic, and any other accumulated context
     * info.</li>
     * <li>A stacktrace for this <code>InternalErrorException</code> in the
     * errortrace log.
     * </ul>
     * 
     * @param pRequest
     *            The portlet request.
     * @param pErrorCode
     *            The error code (a short unique keyname representing the
     *            error).
     * @param pErrorMsg
     *            The error message (a longer diagnostic message describing the
     *            error in more detail).
     */
    public InternalErrorException(PortletRequest pRequest, String pErrorCode,
	    String pErrorMsg) {
	super(pErrorCode, pErrorMsg);
	log(pRequest);
    }

    /**
     * Constructs the <code>InternalErrorException</code> given a root-cause
     * exception (or similar <code>Throwable</code>). Also sets the WPAP Timber
     * logging status indicator for a fatal error, and generates log entries for
     * the error condition. Log entries include:
     * <ul>
     * <li>Root-cause exception name and message are added to the business log.</li>
     * <li>An error log entry for this <code>InternalErrorException</code>,
     * including the root-cause exception name and message, and any other
     * accumulated context info.</li>
     * <li>A stacktrace for the root-cause exception in the errortrace log.
     * </ul>
     * 
     * @param pRequest
     *            The portlet request.
     * @param pEx
     *            The root-cause exception.
     */
    public InternalErrorException(PortletRequest pRequest, Throwable pEx) {
	super(Consts.ERROR_CODE_INTERNAL, pEx,
		"An unexpected exception occurred: " + pEx.getClass().getName());
	log(pRequest);
    }

    // //////////////////////////////////////

    private void log(PortletRequest pRequest) {
	Transaction trans = TransactionImpl.getTransaction(pRequest);
	if (trans != null) {
	    String errorCode = getErrorCode();
	    String errorMsg = getErrorMessage();
	    Throwable errorCause = this;
	    if (getNext() != null) {
		errorCause = getNext();
	    }
	    trans.addError(errorCause, errorMsg, errorCode);
	    trans.addContextInfo(errorCode, errorMsg);
	    trans.setStatusIndicator(StatusIndicator.FATAL);
	}
    }
}