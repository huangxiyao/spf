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

/**
 * <p>This class provides exception utility methods for creating 
 * the <code>PortalURI</code> object to the Service Portal system
 * error page.
 * </p>
 * 
 * @author sunnyee
 * @version TBD
 */	
public class ExceptionUtil {

	// ------------------------------------------------------------- Constants
	
	// Secondary page type ID for the system error page with no horizontal
	// navigation in the C-frame.
	public static String NO_NAV_ERROR_TEMPLATE = "ERROR";
	
	// Secondary page type ID for the system error page with horizontal
	// navigation in the C-frame.
	public static String NAV_ERROR_TEMPLATE = "ANON_SP_ERROR";
	
	// Map session scope attribute name
	public static String SYSTEM_ERROR_PAGE_MAP = "systemErrorPage";
	
	// Map attribute names
	public static String PAGE_TITLE_ATTR = "title";
	public static String ERROR_CODE_ATTR = "errorCode";
	public static String ERROR_MESSAGE_ATTR = "errorMessage";
	
	/**
	 * Create a PortalURI for redirecting to the SPF System Error page with
	 * no horizontal navigation in the C-frame, and show error code in page.
	 */
	static public PortalURI redirectSystemErrorPage(PortalContext portalContext, 
            String errorCode) {
		return redirectSystemErrorPage( portalContext, errorCode, false);
		
	}
		
	/**
	 * Create a PortalURI for redirecting to the SPF System Error page with
	 * or without horizontal navigation in the C-frame as indicated by 
	 * the showNavMenu parameter, and show error code in page.
	 */
	static public PortalURI redirectSystemErrorPage(PortalContext portalContext, 
            String errorCode, boolean showNavMenu) {
		
	    PortalURI systemErrorPage = showNavMenu ? 
	    		portalContext.createDisplayURI(NAV_ERROR_TEMPLATE) :
	    		portalContext.createDisplayURI(NO_NAV_ERROR_TEMPLATE);
	    
	    // Use map object to store error code to pass to JSP.
    	HttpSession session = portalContext.getPortalRequest().getSession();
    	HashMap map = new HashMap(1);
    	map.put(ERROR_CODE_ATTR, errorCode);
    	session.setAttribute(SYSTEM_ERROR_PAGE_MAP, map);
	    
	    return systemErrorPage;
		
	}

	/**
	 * Create a PortalURI for redirecting to the SPF System Error page with no
	 * horizontal navigation in the C-frame, and show title string, error code 
	 * string, and error message in page.
	 */
	static public PortalURI redirectSystemErrorPage(PortalContext portalContext, 
            String title, String errorCode, String errorMessage) { 
		
		return redirectSystemErrorPage(portalContext, 
            title, errorCode, errorMessage, false); 
	}
		
	/**
	 * Create a PortalURI for redirecting to the SPF System Error page with or
	 * without horizontal navigation in the C-frame as indicated by the
	 * showNavMenu parameter.   Show title string, error code string, 
	 * and error message in page.
	 */
	static public PortalURI redirectSystemErrorPage(PortalContext portalContext, 
            String title, String errorCode, String errorMessage,
            boolean showNavMenu) {
		
		
	    PortalURI systemErrorPage = showNavMenu ? 
	    		portalContext.createDisplayURI(NAV_ERROR_TEMPLATE) :
	    		portalContext.createDisplayURI(NO_NAV_ERROR_TEMPLATE);
	    
	    // Use map object to store title, error code, and error message to
	    // pass to JSP.
    	HttpSession session = portalContext.getPortalRequest().getSession();
    	HashMap map = new HashMap(3);
	    if (title != null) {
	    	map.put(PAGE_TITLE_ATTR, title);
	    }
	    if (errorCode != null) {
	    	map.put(ERROR_CODE_ATTR, errorCode);
	    }
	    if (errorMessage != null) {
	    	map.put(ERROR_MESSAGE_ATTR, errorMessage);
	    }
    	session.setAttribute(SYSTEM_ERROR_PAGE_MAP, map);
	    
	    return systemErrorPage;
		
	}
	
}
