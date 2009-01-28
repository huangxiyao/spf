/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.exception.portal;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * <p>
 * This class provides exception utility methods for creating the
 * <code>PortalURI</code> object to the Service Portal system error page.
 * </p>
 * 
 * @author sunnyee
 * @version TBD
 */
public class ExceptionUtil {

	// ------------------------------------------------------------- Constants

	// Map session scope attribute name
	public static String SESSION_ATTR_SYSTEM_ERROR_DATA = "SPF_SYSTEM_ERROR_DATA";

	// Map attribute names
	public static String PAGE_TITLE_ATTR = "errorTitle";
	public static String ERROR_CODE_ATTR = "errorCode";
	public static String ERROR_MESSAGE_ATTR = "errorMessage";

	/**
	 * Create a PortalURI for redirecting to the SPF system error secondary
	 * page, and show the given error code in the page (along with the default
	 * title and message for a system error). A default error code is displayed
	 * if null is provided.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @param errorCode
	 *            An error code of arbitrary format (SPF does not impose a
	 *            format) to display on the system error secondary page.
	 * @return PortalURI For redirecting to the system error secondary page.
	 */
	static public PortalURI redirectSystemErrorPage(
			PortalContext portalContext, String errorCode) {
		return redirectSystemErrorPage(portalContext, null, errorCode, null);
	}

	/**
	 * Create a PortalURI for redirecting to the SPF system error secondary
	 * page, and show the given title, error code, and message in the page.
	 * Defaults for these are displayed in the page if null is provided.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @param errorTitle
	 *            The title to display on the system error secondary page.
	 * @param errorCode
	 *            An error code of arbitrary format (SPF does not impose a
	 *            format) to display on the system error secondary page.
	 * @param errorMessage
	 *            The error message to display on the system error secondary
	 *            page page.
	 * @return PortalURI For redirecting to the system error secondary page.
	 */
	static public PortalURI redirectSystemErrorPage(
			PortalContext portalContext, String errorTitle, String errorCode,
			String errorMessage) {

		PortalURI systemErrorPage = portalContext
				.createDisplayURI(Consts.PAGE_FRIENDLY_ID_SYSTEM_ERROR);

		// Use map object to store title, error code, and error message to
		// pass to JSP.
		HttpSession session = portalContext.getPortalRequest().getSession();
		HashMap map = new HashMap(3);
		if (errorTitle != null) {
			map.put(PAGE_TITLE_ATTR, errorTitle);
		}
		if (errorCode != null) {
			map.put(ERROR_CODE_ATTR, errorCode);
		}
		if (errorMessage != null) {
			map.put(ERROR_MESSAGE_ATTR, errorMessage);
		}
		session.setAttribute(SESSION_ATTR_SYSTEM_ERROR_DATA, map);

		return systemErrorPage;
	}
}
