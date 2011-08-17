package com.hp.spp.portal.secondarypage.pagedisplay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.spp.profile.Constants;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

public class SPPPageDisplayAction extends BaseAction {
	
	public PortalURI execute(PortalContext portalContext) throws ActionException {
		HttpServletRequest request = portalContext.getPortalRequest().getRequest();

		Map userProfile = (Map) request.getSession().getAttribute(Constants.PROFILE_MAP) ;

		String key = null ;
		key = "com.vignette.portal.attribute.portlet.ContentDisplayPortlet.StandardParameters";
		request.setAttribute(key, userProfile);
		key = "com.vignette.portal.attribute.portlet.ContentListPortlet.StandardParameters";
		request.setAttribute(key, userProfile);
		key = "com.vignette.portal.attribute.portlet.DownloadablesAreaPortlet.StandardParameters";
		request.setAttribute(key, userProfile);

		return null;
	}
}
