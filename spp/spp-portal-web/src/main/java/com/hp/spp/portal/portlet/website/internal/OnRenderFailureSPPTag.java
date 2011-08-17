
package com.hp.spp.portal.portlet.website.internal;

import javax.servlet.jsp.JspException;

import com.hp.spp.common.util.DiagnosticContext;
import com.vignette.portal.portlet.website.internal.OnRenderFailureTag;

/**
 * 
 * This class adds the "PortletRenderFailureSPP" in the diagnostic context, on receiving the portlet render error.
 * This value is retrieved in the CacheFilter.java to ensure the validity of the response
 * 		
 * @author mukesh
 *
 */
public class OnRenderFailureSPPTag extends OnRenderFailureTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 
	 *  Add the "PortletRenderFailureSPP" in the diagnostic context. 
	 *  valueToReturn = 1 ; indicate that there was a portlet error on the page.
	 */
	@Override
	public int doStartTag() throws JspException {
		int valueToReturn = super.doStartTag();
		if ( valueToReturn == 1 ){			
		DiagnosticContext.getThreadInstance().add("PortletRenderFailureSPP","Portlet Response not cachable");			
		}
		return valueToReturn;
	}

}
