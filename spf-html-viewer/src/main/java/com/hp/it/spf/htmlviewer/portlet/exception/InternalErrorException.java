/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
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
 * A system exception used by the <code>html-viewer</code> portlet code
 * whenever a system error has occurred. For example, when no view filename is
 * configured by the time that the
 * {@link com.hp.it.spf.htmlviewer.portlet.web.ViewController} executes.
 * </p>
 * <p>
 * The error diagnostics representing the system error(s) are set into this
 * exception by the constructor. The constructor also sets the WPAP Timber
 * logging status to indicate a fatal error was encountered. A WPAP Timber log
 * entry is generated for each error in the diagnostics. logging.
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

	private LinkedHashMap<String, String> errors = null;

	/**
	 * Constructs the <code>InternalErrorException</code> given a map of error
	 * codes and diagnostic messages - one for each error condition. Also sets
	 * the WPAP Timber logging status indicator for a fatal error, and generates
	 * error log entries for each error condition in the diagnostics.
	 * 
	 * @param pRequest -
	 *            The portlet request
	 * @param pErrors -
	 *            A map of error codes to (optional) diagnostic messages.
	 */
	public InternalErrorException(PortletRequest pRequest,
			LinkedHashMap<String, String> pErrors) {
		super(Consts.ERROR_CODE_INTERNAL);
		this.errors = pErrors;
		Transaction trans = TransactionImpl.getTransaction(pRequest);
		if (trans != null) {
			if (pErrors != null) {
				Iterator i = pErrors.keySet().iterator();
				String errorCode, errorDiagnostic;
				while (i.hasNext()) {
					errorCode = (String) i.next();
					errorDiagnostic = pErrors.get(errorCode);
					if (errorDiagnostic != null) { 
						trans.addError(this, errorDiagnostic, errorCode);
						trans.addContextInfo(errorCode, errorDiagnostic);
					} else {
						trans.addError(this, null, errorCode);
						trans.addContextInfo(errorCode, null);
					}
				}
			} else {
				trans.addError(this);
			}
			trans.setStatusIndicator(StatusIndicator.FATAL);
		}
	}
}