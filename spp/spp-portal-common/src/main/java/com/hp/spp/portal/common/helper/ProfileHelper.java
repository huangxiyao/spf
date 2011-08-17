package com.hp.spp.portal.common.helper;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.profile.Constants;
import com.vignette.portal.website.enduser.PortalContext;

public class ProfileHelper {

	private static Logger mLog = Logger.getLogger(ProfileHelper.class);

	public ProfileHelper() {
	}

	/**
	 * Checks whether the user is authenticated. Uses the presence of the
	 * attribute LoginId as the criteria for a user being authenticated. If it
	 * is assigned then returns true, otherwise returns false.
	 * 
	 * @param context
	 *            Used to find the profile in session.
	 * @return true if user is authenticated, otherwise false
	 */
	public boolean isUserAuthenticated(PortalContext context) {
		return getProfileValue(context, Constants.MAP_USERNAME).length() > 0;
	}

	/**
	 * Returns the value from the user profile.
	 * 
	 * @param context
	 *            Used to get the profile from the session.
	 * @param key
	 *            Profile attribute name
	 * @return Value of the attribute or "N/A" if the value is null or empty
	 */
	public String getOmnitureProfileValue(PortalContext context, String key) {
		String value = getProfileValue(context, key);

		if (value.equalsIgnoreCase("")) {
			value = "N/A";
		}
		return value;
	}

	/**
	 * Returns the value from the user profile.
	 * 
	 * @param context
	 *            Used to get the profile from the session.
	 * @param key
	 *            Profile attribute name
	 * @return Value of the attribute or empty string if the value is not found
	 */
	public String getProfileValue(PortalContext context, String key) {
		String value = null;

		if (mLog.isDebugEnabled()) {
			mLog.debug("Getting value for key in profile: " + key);
		}
		Map profile = getProfile(context);
		if (profile != null) {
			value = (String) profile.get(key);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("Value found: " + value);
		}

		if (value == null) {
			value = "";
		}
		return value;
	}

	/**
	 * Returns a map containing the user profile. If the profile has not been
	 * stored then will return null.
	 * 
	 * @param context
	 *            PortalContext from which to retrieve the session that contains
	 *            the map
	 * @return Map with user profile key/value pairs
	 */
	public Map getProfile(PortalContext context) {
		HttpSession session = context.getPortalRequest().getSession();
		return getProfile(session);
	}

	/**
	 * Returns a map containing the user profile. If the profile has not been
	 * stored then will return null.
	 * 
	 * @param session
	 *            HttpSession from which to retrieve the session that contains
	 *            the map
	 * @return Map with user profile key/value pairs
	 */
	public Map getProfile(HttpSession session) {
		Map profile = (Map) session.getAttribute(Constants.PROFILE_MAP);
		if (mLog.isDebugEnabled()) {
			mLog.debug("User profile in session: " + profile);
		}
		return profile;
	}

	/**
	 * Returns true or false if we are in simulation mode
	 * 
	 * @param context
	 *            PortalContext from which to retrieve the session that contains
	 *            the map
	 * @return true if we are in simulation mode else false
	 */
	public boolean isSimulationMode(PortalContext context) {
		HttpSession session = context.getPortalRequest().getSession();
		return isSimulationMode(session);
	}

	/**
	 * Returns true or false if we are in simulation mode
	 * 
	 * @param session
	 *            HttpSession from which to retrieve the session that contains
	 *            the map
	 * @return true if we are in simulation mode else false
	 */
	public boolean isSimulationMode(HttpSession session) {
		Object profile = session.getAttribute(Constants.PROFILE_MAP);
		if (profile != null && profile instanceof Map
				&& ((Map) profile).containsKey(Constants.MAP_SIMULATOR)
				&& ((Map) profile).get(Constants.MAP_SIMULATOR) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a map containing the profile of the simulator user. If the
	 * profile has not been stored then will return null.
	 * 
	 * @param context
	 *            PortalContext from which to retrieve the session that contains
	 *            the map
	 * @return Map with user profile key/value pairs
	 */
	public Map getSimulatorProfile(PortalContext context) {
		HttpSession session = context.getPortalRequest().getSession();
		return getSimulatorProfile(session);
	}

	/**
	 * Returns a map containing the profile of the simulator user. If the
	 * profile has not been stored then will return null.
	 * 
	 * @param session
	 *            HttpSession from which to retrieve the session that contains
	 *            the map
	 * @return Map with user profile key/value pairs
	 */
	public Map getSimulatorProfile(HttpSession session) {
		Map profile = (Map) session.getAttribute(Constants.PROFILE_MAP);
		Map simulatorProfile = null;
		if (profile != null) {
			simulatorProfile = (Map) profile.get(Constants.MAP_SIMULATOR);
		}
		if (mLog.isDebugEnabled()) {
			mLog
					.debug("Simulator User profile in session: "
							+ simulatorProfile);
		}
		return simulatorProfile;
	}

}
