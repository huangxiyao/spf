package com.hp.spp.portal.login.business.rule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hp.spp.profile.Constants;

public class UPSInactiveUserRule implements UPSRule {
	
	private static final Logger mLog = Logger.getLogger(UPSInactiveUserRule.class);

	public Integer execute(HttpServletRequest request, Map userProfile,	boolean isSimulation) {
		
		String upsStatus = (String) userProfile.get(Constants.MAP_STATUS);
		if (upsStatus == null)
			return null;
		int status = Constants.STATUS_UPS_INACTIVE;
		try {
			status = Integer.parseInt(upsStatus);
		} catch (NumberFormatException e) {
			if (mLog.isDebugEnabled())
				mLog.debug("ups status has not a number format");
			return null;
		}
		if (status == Constants.STATUS_UPS_INACTIVE){
			//The following code enables us to maintain users locale information when redirected to landing page.
			//The params are appended to redirection url in SessionVerificationFilter
			String languageCode = (String) userProfile.get(Constants.MAP_LANGUAGE);
			String countryCode = (String) userProfile.get(Constants.MAP_COUNTRY);
			request.getSession().removeAttribute(Constants.PROFILE_MAP);
			request.setAttribute(Constants.LOCALE_PARAM, "&cc="+countryCode+"&lang="+languageCode);
			return new Integer(Constants.ERRORCODE_RULE_UPSINACTIVE);
		}
		return null;
	}

}