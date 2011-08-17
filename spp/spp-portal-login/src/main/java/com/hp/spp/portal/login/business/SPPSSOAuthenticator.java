package com.hp.spp.portal.login.business;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.epicentric.authentication.SSOUsernameRetriever;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.user.UserManager;
import com.hp.spp.portal.login.business.preprocess.Localizer;
import com.hp.spp.profile.Constants;
import com.hp.spp.hpp.supporttools.HPPHeaderHelper;

/**
 * Implementation of the VignetteSSOAuthenticator. Returns the user identifier to Vignette.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class SPPSSOAuthenticator implements SSOUsernameRetriever {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(SPPSSOAuthenticator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epicentric.authentication.SSOUsernameRetriever#
	 *      getSSOUsername(javax.servlet.http.HttpServletRequest)
	 */
	public String getSSOUsername(HttpServletRequest request) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("SPPSSOAuthenticator.getSSOUsername begin");
		}

		String userId = HPPHeaderHelper.getSMUser(request);
		if (mLog.isDebugEnabled()) {
			mLog.debug("user id from request header SM_USERDN [" + userId + "]");
		}

		// Debug
		try {
			Object userProfile = null;
			Object jsessionId = null;
			if (request.getSession() != null) {
				userProfile = request.getSession().getAttribute(Constants.PROFILE_MAP);
			}
			if (userProfile != null) {
				jsessionId = ((Map) userProfile).get(Constants.MAP_PORTALSESSIONID);
			}
			if ((userId == null || "".equals(userId)) && jsessionId != null) {
				mLog.warn("SM_USERDN empty but JSESSIONID [" + jsessionId + "] existing.");
			}
		} catch (Throwable t) {
			mLog.warn("Throwable in debug block " + t.getMessage());
		}

		// simulation
		HttpSession session = request.getSession();
		if (session != null) {
			Map userProfile = (Map) session.getAttribute(Constants.PROFILE_MAP);
			if (userProfile != null && userProfile.get(Constants.MAP_SIMULATOR) != null) {
				userId = (String) userProfile.get(Constants.MAP_USERNAME);
				if (mLog.isDebugEnabled())
					mLog.debug("user determined by simulation");
			}
		}

		// No HPP authentication
		if (userId == null || "".equals(userId)) {
			userId = (new Localizer()).getGuestUserForSSOAuthenticator(request);
			if (mLog.isDebugEnabled()) {
				mLog.debug("user determined by localizer");
			}
		}

		// HPP authentication but user not existing actually in Vignette
		if (!userExist(userId)) {
			userId = (new Localizer()).getGuestUserForSSOAuthenticator(request);
			if (mLog.isDebugEnabled()) {
				mLog.debug("user determined by localizer");
			}
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("user id : [" + userId + "]");
			mLog.debug("SPPSSOAuthenticator.getSSOUsername end");
		}
		return userId;
	}

	private boolean userExist(String userId) {
		UserManager userManager = UserManager.getInstance();
		try {
			return userManager.userExists(Constants.VGN_USERNAME, userId);
		} catch (EntityPersistenceException e) {
			String error = "error during retrieval of user :" + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error);
		}
	}
}
