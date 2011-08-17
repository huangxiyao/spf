package com.hp.spp.portal.login.business.rule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.hp.spp.profile.Constants;

public class SPPInactiveUserRule implements UPSRule{
	
	private String mHppID = "";
	private static final Logger mLog = Logger.getLogger(SPPInactiveUserRule.class);
	
	public Integer execute(HttpServletRequest request, Map userProfile, boolean isSimulation){
				
		UserManager userManager = UserManager.getInstance();
		try {
			
			mHppID = (String)userProfile.get(Constants.MAP_HPPID);
			// if the user doesn't exist, we do nothing. We will create it later
			
			if (!userManager.userExists(Constants.VGN_HPPID, mHppID)){
				return null;
			}
	
			// Retrieve the user status
			User user = userManager.getUser(Constants.VGN_HPPID, mHppID);
			Integer status = new Integer(Constants.STATUS_VIGNETTE_ACTIVE);
			if (user.getProperty(Constants.VGN_STATE) != null){
				status = (Integer) user.getProperty(Constants.VGN_STATE);
			}
	
			// If the user is not active, we return the url of the landing page
			if (status.intValue() == Constants.STATUS_VIGNETTE_INACTIVE) {
				if (mLog.isDebugEnabled()){
					mLog.debug("user with hppid "+ mHppID + " inactive in Vignette. Redirection to landing page.");
				}
				//The following code enables us to maintain users locale information when redirected to landing page.
				//The params are appended to redirection url in SessionVerificationFilter
				String languageCode = (String) userProfile.get(Constants.MAP_LANGUAGE);
				String countryCode = (String) userProfile.get(Constants.MAP_COUNTRY);
				request.getSession().removeAttribute(Constants.PROFILE_MAP);
				request.setAttribute(Constants.LOCALE_PARAM, "&cc="+countryCode+"&lang="+languageCode);
				return new Integer(Constants.ERRORCODE_RULE_SPPINACTIVE);
			}
		} catch (Exception e) {
			mLog.error("Not able to fetch user from database");
			return null;
		}
	
		return null;
	
	}

}
