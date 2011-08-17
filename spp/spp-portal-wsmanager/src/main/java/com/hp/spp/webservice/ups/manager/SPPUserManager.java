package com.hp.spp.webservice.ups.manager;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.PropertyNotFoundException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.spp.common.util.DiagnosticContext;
import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.hpp.admin.HPPAdminPasswordHelper;
import com.hp.spp.hpp.exception.HPPAdminException;
import com.hp.spp.portal.common.dao.CommonDAO;
import com.hp.spp.portal.common.dao.CommonDAOCacheImpl;
import com.hp.spp.portal.common.dao.CommonDAOSQLManagerImpl;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.portal.common.sql.PortalCommonDAO;
import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
import com.hp.spp.portal.common.util.HPUrlLocator;
import com.hp.spp.profile.Constants;
import com.hp.spp.webservice.ugs.manager.SPPUserGroupManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Class which take care of the management of user.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 */
public class SPPUserManager {
	private static final Logger mLog = Logger.getLogger(SPPUserManager.class);

	/**
	 * Key to find the url of ups.
	 */
	//private static final String mUPSURL = "SPP.ups.url";

	/**
	 * Retrieve a user profile. Actually are missing MAP_HOMEPAGE and
	 * MAP_PORTALSESSIONID in the map
	 * 
	 * @param hppId
	 *            the hppid of the user
	 * @param site
	 *            the site
	 * @param simulated
	 *            if the profile is for simulation or not
	 * @return the profile of the user
	 */
	public Map getUserProfile(String hppId, String site, boolean simulated) {
		Map userProfile = getUserProfileWithoutGroups(hppId, site, simulated);

		// retrieve user groups from ugs
		(new SPPUserGroupManager()).computeGroups(userProfile);

		return userProfile;
	}

	//FIXME (slawek) - I introduced this method because as we discovered that during simulation
	//UGS web service was called twice. As this method is used by other components I kept
	//its behavior as-is and extracted a method that does not call UGS that will be used
	//in simulation.
	//This is a dirty hack to minimize risk. Overall, this code MUST BE refactored to ease
	//its maintenance.
	public Map getUserProfileWithoutGroups(String hppId, String site, boolean simulated) {
		// retrieve initial config
		Map userProfile = new HashMap();
		String adminQueryId = null;
		String region = null;

         adminQueryId = SiteManager.getInstance().getSite(site).getAdminUPSQueryId();
         if(adminQueryId == null){
             throw new IllegalStateException("AdminQueryID is not available for site: "+site+ ". Check for site settings");
         }
         region = SiteManager.getInstance().getSite(site).getSiebelRegion();
         if(region == null ){
             throw new IllegalStateException("Siebel region is not available for site: "+site+ ". Check for site settings");
         }


		// retrieve password
		String adminLogin = null;
		String adminPassword = null;
		try {
			HPPAdminPasswordHelper adminUtil = new HPPAdminPasswordHelper();
			adminLogin = adminUtil.fetchHPPAdminLoginId(site);
			adminPassword = adminUtil.fetchHPPAdminPassword(site);
		} catch (HPPAdminException e) {
			String error = "No admin login or password retrieved for site ["
					+ site + "]: " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		}

		// retrieve admin session token
		PassportService ws = new PassportService();
		String adminSessionToken = null;
		try {
			adminSessionToken = (ws.login(adminLogin, adminPassword))
					.getSessionToken();
		} catch (PassportServiceException e) {
			String error = "PassportServiceException for adminLogin ["
					+ adminLogin + "]: " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		}

		// flag for simulation
		String flagSimulated = "Standard";
		if (simulated)
			flagSimulated = "Simulated";

		// retrieve user profile from ups
		Integer error = retrieveProfileFromUPS(userProfile, adminQueryId, null,
				hppId, site, null, adminSessionToken, region, flagSimulated);

		// Error in UPS invocation
		if (error != null) {
			mLog.error("error during ups invocation");
			throw new IllegalStateException(
					"No Profile returned from UPS for user [" + hppId
							+ "]. Check the hppid passed.");
		}

		// for debug only
		if (mLog.isDebugEnabled()) {
			mLog.debug("map returned from ups");
			Iterator it = userProfile.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				mLog.info("[" + key + "] : [" + userProfile.get(key) + "]");
			}
		}

		// add site (some profiles do not return actually a site)
		userProfile.put(Constants.MAP_SITE, site);
		computeSiteIdentifier(userProfile, site);

		// create minimal user if needed
		if (!existUser((String) userProfile.get(Constants.MAP_HPPID))) {
			if (mLog.isDebugEnabled())
				mLog.debug("create minimal user during getUserProfile");
			createMinimalUser(userProfile);
		}
		return userProfile;
	}

	/**
	 * Retrieve the query id, the region and pass the call to UPS.
	 * 
	 * @param userId
	 *            id of the user (SM_USERDN)
	 * @param hppId
	 *            hpp id of the user (SM_UNIVERSALID)
	 * @param site
	 *            name of the site accessed
	 * @param sessionToken
	 *            session token of the user
	 * @param userProfile
	 *            map contening the profile of the error
	 * 
	 * @return an error code
	 */
	public Integer constructQueryAndRetrieveProfileFromUPS(String userId,
														   String hppId, String site, String sessionToken, Map userProfile) {
		// retrieve user profile from ups
		CommonDAO commonDAO = CommonDAOCacheImpl.getInstance();
		String queryId = commonDAO.getUPSQueryId(site);

		String region = SiteManager.getInstance().getSite(site).getSiebelRegion();
        if(region == null ){
            throw new IllegalStateException("Siebel region is not available for site: "+site+ ". Check for site settings");
        }

		Integer error = this.retrieveProfileFromUPS(userProfile, queryId,
				userId, hppId, site, sessionToken, null, region, "Standard");
		return error;
	}

	/**
	 * Fills in the provided <tt>userProfile</tt> map with user information.
	 *
	 * This class relies on {@link UserProfileServiceFactory} to create the appropriate instance of
	 * {@link UserProfileService}.
	 *
	 * @see UserProfileServiceFactory
	 * @see HPPUserProfileServiceClient
	 * @see MockUserProfileService
	 */
	public Integer retrieveProfileFromUPS(Map userProfile, String queryId,
			String userId, String hppId, String site, String sessionToken,
			String adminSessionToken, String region, String loginType) {

		Integer errorCode = null;

		UserProfileServiceFactory upsFactory = new UserProfileServiceFactory();
		UserProfileService ups = upsFactory.newUserProfileService(site);
		errorCode = ups.getUserProfile(userProfile, queryId, userId, hppId, site, sessionToken, adminSessionToken, region, loginType);

		if (errorCode != null) {
			return errorCode;
		}

		// check that the user profile contains the minimal data
		checkMinimalAttributes(userProfile);

		// no error
		return null;
	}



	/*
	 * Check that minimal attributes are in the profile. See SPP 2.0 Standards.
	 */
	private void checkMinimalAttributes(Map userProfile) {
		String missingAttribute = null;
		if (userProfile.get(Constants.MAP_LANGUAGE) == null
				|| "".equals(userProfile.get(Constants.MAP_LANGUAGE)))
			missingAttribute = Constants.MAP_LANGUAGE;
		if (userProfile.get(Constants.MAP_COUNTRY) == null
				|| "".equals(userProfile.get(Constants.MAP_COUNTRY)))
			missingAttribute = Constants.MAP_COUNTRY;
		if (userProfile.get(Constants.MAP_USERNAME) == null
				|| "".equals(userProfile.get(Constants.MAP_USERNAME)))
			missingAttribute = Constants.MAP_USERNAME;
		if (userProfile.get(Constants.MAP_HPPID) == null
				|| "".equals(userProfile.get(Constants.MAP_HPPID)))
			missingAttribute = Constants.MAP_HPPID;
		if (userProfile.get(Constants.MAP_FIRSTNAME) == null
				|| "".equals(userProfile.get(Constants.MAP_FIRSTNAME)))
			missingAttribute = Constants.MAP_FIRSTNAME;
		if (userProfile.get(Constants.MAP_LASTNAME) == null
				|| "".equals(userProfile.get(Constants.MAP_LASTNAME)))
			missingAttribute = Constants.MAP_LASTNAME;
		if (userProfile.get(Constants.MAP_EMAIL) == null
				|| "".equals(userProfile.get(Constants.MAP_EMAIL)))
			missingAttribute = Constants.MAP_EMAIL;
		if (userProfile.get(Constants.MAP_STATUS) == null
				|| "".equals(userProfile.get(Constants.MAP_STATUS)))
			missingAttribute = Constants.MAP_STATUS;

		if (missingAttribute != null)
			throw new IllegalStateException(
					"Profile returned from UPS does not "
							+ "contain minimal attribute [" + missingAttribute
							+ "].");
	}

	/**
	 * Compute the user profile after retrieval profile from UPS.
	 * 
	 * @param request
	 *            the incoming request
	 * @param userId
	 *            the user id
	 * @param hppId
	 *            the hpp id
	 * @param site
	 *            the site name
	 * @param userProfile
	 *            the user profile obtained from UPS
	 * @return an error code, null if no error
	 */
	public Integer computePostUPSProfile(HttpServletRequest request,
			String userId, String hppId, String site, Map userProfile) {

		// adapt the data
		if (mLog.isDebugEnabled())
			mLog.debug("adapt the data");
		userProfile.put(Constants.MAP_SITE, site);
		userProfile.put(Constants.MAP_HOMEPAGE, SiteURLHelper.getURLHomePage(request));

		Map transformedUserProfile = transformUserProfile(userProfile, request.getSession().getId());

		// copy the contents of the transformed map into user profile map this method manipulates.
		// The map is cleared first to make sure that it's copy is exact with transformedUserProfile.
		// We have to modify this map so the modifications are visible outside of this method - kinda
		// obvious but we had a bug around that.
		userProfile.clear();
		userProfile.putAll(transformedUserProfile);

		// create minimal user if needed
		if (!existUser((String) userProfile.get(Constants.MAP_HPPID))) {
			if (mLog.isDebugEnabled())
				mLog.debug("create minimal user during computePostUPSProfile");
			createMinimalUser(userProfile);
		}

		// compute the groups
		if (mLog.isDebugEnabled())
			mLog.debug("compute the groups");
		(new SPPUserGroupManager()).computeGroups(userProfile);

		// refresh user Vignette
		if (mLog.isDebugEnabled())
			mLog.debug("refresh user Vignette");
		refreshVignetteUser(userProfile);

		return null;
	}

	/**
	 * Transforms the incoming <tt>userProfile</tt> map by calculating SPP-specific attributes and
	 * then creating new map with the initial map contents and these new attributes.
	 * @param userProfile incoming user profile
	 * @param sessionId user's session id
	 * @return new map containing <tt>userProfile</tt> map entries plus SPP-specific attributes
	 */
	private Map transformUserProfile(Map userProfile, String sessionId) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("content of the UPS user profile");
			for (Iterator iter = userProfile.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				mLog.debug("[" + key + "] [" + userProfile.get(key) + "]");
			}
		}

		Map adaptedUserProfile = new HashMap(userProfile);

		// from SPP
		adaptedUserProfile.put(Constants.MAP_PORTALSESSIONID, sessionId);
		adaptedUserProfile.put(Constants.MAP_LASTLOGINDATE, String
				.valueOf(System.currentTimeMillis()));
		adaptedUserProfile.put(Constants.MAP_SITE, userProfile.get(Constants.MAP_SITE));
		adaptedUserProfile.put(Constants.MAP_HOMEPAGE, userProfile.get(Constants.MAP_HOMEPAGE));
		// Add IsSimulating only if not present yet. This value is present during simulation.
		// It is normally not present during login.
		if (!adaptedUserProfile.containsKey(Constants.MAP_IS_SIMULATING)) {
			adaptedUserProfile.put(Constants.MAP_IS_SIMULATING, "false");
		}
		computeSiteIdentifier(adaptedUserProfile, (String) userProfile.get(Constants.MAP_SITE));

		if (mLog.isDebugEnabled()) {
			mLog.debug("content of the adapted user profile");
			for (Iterator iter = adaptedUserProfile.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				mLog.debug("[" + key + "] [" + adaptedUserProfile.get(key)
						+ "]");
			}
		}
		return adaptedUserProfile;
	}

	private void computeSiteIdentifier(Map userProfile, String siteName) {
		// if access to console and Site Identifier is fullfiled, no change to
		// allow preview of 3P Portlets
		if ("console".equals(siteName)
				&& userProfile.get(Constants.MAP_SITE_ID) != null)
			return;

		// else retrieve site identifier from database
		PortalCommonDAO portalCommonDAO = PortalCommonDAOCacheImpl
				.getInstance();
		String siteIdentifier = portalCommonDAO.getSiteIdentifier(siteName);
		userProfile.put(Constants.MAP_SITE_ID, siteIdentifier);
	}

	private boolean existUser(String hppId) {
		UserManager userManager = UserManager.getInstance();
		try {
			return userManager.userExists(Constants.VGN_HPPID, hppId);
		} catch (EntityPersistenceException e) {
			String error = "error during retrieval of user :" + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		}
	}

	private void createMinimalUser(Map userProfileSession) {
		// If the user is not already created in Vignette, We must create it
		// now!
		UserManager userManager = UserManager.getInstance();
		try {
			String userId = (String) userProfileSession
					.get(Constants.MAP_USERNAME);
			String hppId = (String) userProfileSession.get(Constants.MAP_HPPID);
			mLog.warn("creation of the user with name [" + userId
					+ "] and hppid [" + hppId + "]");
			Map userProfileVignette = new HashMap();
			userProfileVignette.put(Constants.VGN_USERNAME, userId);
			userProfileVignette.put(Constants.VGN_FIRSTNAME,
					(String) userProfileSession.get(Constants.MAP_FIRSTNAME));
			userProfileVignette.put(Constants.VGN_LASTNAME,
					(String) userProfileSession.get(Constants.MAP_LASTNAME));
			userProfileVignette.put(Constants.VGN_HPPID, hppId);
			userProfileVignette.put(Constants.VGN_EMAIL,
					(String) userProfileSession.get(Constants.MAP_EMAIL));
			userProfileVignette.put(Constants.VGN_DOMAIN,
					"spp realm");
			userProfileVignette.put(Constants.VGN_STATE, new Integer(
					Constants.STATUS_VIGNETTE_ACTIVE));

			userManager.createUser(userProfileVignette);
		} catch (UniquePropertyValueConflictException e) {
			String error = "error during creation of user :" + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		} catch (EntityPersistenceException e) {
			String error = "error during creation of user :" + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		}
	}

	/*
	 * Check if the user is active in Vignette. If not, return an url of
	 * redirection.
	 */
	private Integer checkActiveUser(String hppId, String site) {
		UserManager userManager = UserManager.getInstance();
		try {
			// if the user doesn't exist, we do nothing. We will create it later
			if (!userManager.userExists(Constants.VGN_HPPID, hppId))
				return null;

			// Retrieve the user status
			User user = userManager.getUser(Constants.VGN_HPPID, hppId);
			Integer status = new Integer(Constants.STATUS_VIGNETTE_ACTIVE);
			if (user.getProperty(Constants.VGN_STATE) != null)
				status = (Integer) user.getProperty(Constants.VGN_STATE);

			// If the user is not active, we return the url of the landing page
			if (status.intValue() == Constants.STATUS_VIGNETTE_INACTIVE) {
				if (mLog.isDebugEnabled())
					mLog
							.debug("user with hppid "
									+ hppId
									+ " inactive in Vignette. Redirection to landing page.");
				return new Integer(3);
			}
		} catch (Exception e) {
			String error = "error during retrieval of user with hpp_id ["
					+ hppId + "]. " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		}

		return null;
	}

	/*
	 * Return an URL of redirection if UPS status is not active
	 */
	Integer analyzeUPSStatus(Map userProfile) {
		String upsStatus = (String) userProfile.get(Constants.MAP_STATUS);
		if (upsStatus == null)
			return null;

		int status = Constants.STATUS_UPS_ACTIVE;
		try {
			status = Integer.parseInt(upsStatus);
		} catch (NumberFormatException e) {
			if (mLog.isDebugEnabled())
				mLog.debug("ups status has not a number format");
			return null;
		}
		if (status == Constants.STATUS_UPS_PENDING)
			return new Integer(2);
		if (status == Constants.STATUS_UPS_INACTIVE)
			return new Integer(3);
		return null;
	}

	/*
	 * Refresh the User in Vignette database
	 */
	private void refreshVignetteUser(Map userProfileSession) {
		String hppId = (String) userProfileSession.get(Constants.MAP_HPPID);

		// Retrieve the user
		UserManager userManager = UserManager.getInstance();
		User user = null;
		try {
			user = userManager.getUser(Constants.VGN_HPPID, hppId);
		} catch (EntityPersistenceException e) {
			String error = "error during retrieval of user with hpp_id ["
					+ hppId + "]. " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		} catch (EntityNotFoundException e) {
			String error = "error during retrieval of user with hpp_id ["
					+ hppId + "]. " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error, e);
		}

		// Transform the map session in a map with attributes name for Vignette
		Map userProfileVignette = transformToVignetteMap(userProfileSession);

		// Check if there is a need to update the vignette user
		if (isNeedToUpdateVignetteUser(user, userProfileVignette)) {
			Date date = new Date();
			userProfileVignette.put(Constants.VGN_UPDATEDDATE, date);
			userProfileSession.put(Constants.MAP_UPDATEDDATE, String
					.valueOf(date.getTime()));
			mLog.debug("Update User by API");
			updateVignetteUserProperties(user, userProfileVignette);
		} else {
			// update the vignette user at least with the last login date
			// do not user Vignette API as it cause lot of broadcaster calls
			mLog.debug("Update User by SQL");
			CommonDAO commonDAO = CommonDAOSQLManagerImpl.getInstance();
			commonDAO.updateUserLastLoginDate(hppId, new Date());
		}

		// update vignette language metastore
		String languageFromSession = ((String)userProfileSession.get(Constants.MAP_LANGUAGE));
		String languageForLocale = languageFromSession.substring(0, 2);
		String countryForLocale = (languageFromSession.length() == 5) ? languageFromSession.substring(3, 5).toUpperCase() : (String) userProfileSession.get(Constants.MAP_COUNTRY);
		if (mLog.isDebugEnabled()) {
				mLog.debug("Language for Locale: "+languageForLocale+" Country for Locale: "+countryForLocale);
		}
		Locale locale = new Locale(languageForLocale, countryForLocale);
		if(mLog.isDebugEnabled()) {
				mLog.debug("Locale Used: "+ locale);
		}
		Locale localeVignette = I18nUtils.getUserLocale(user);
		if (localeVignette == null || !localeVignette.equals(locale)) {
			mLog.debug("Update local of user to [" + locale.getDisplayName()+"]");
			I18nUtils.setUserLocale(user, locale);
		}
	}

	/*
	 * Convert the names of the attributes in the map session in Vignette user
	 * map
	 */
	private Map transformToVignetteMap(Map userProfileSession) {

		Map userProfileVignette = new HashMap();
		userProfileVignette.put(Constants.VGN_COUNTRY, userProfileSession
				.get(Constants.MAP_COUNTRY));
		userProfileVignette.put(Constants.VGN_FIRSTNAME, userProfileSession
				.get(Constants.MAP_FIRSTNAME));
		userProfileVignette.put(Constants.VGN_LASTNAME, userProfileSession
				.get(Constants.MAP_LASTNAME));
		userProfileVignette.put(Constants.VGN_HPPID, userProfileSession
				.get(Constants.MAP_HPPID));
		// Vignette language code is only on 2 digits
		String language = (String) userProfileSession
				.get(Constants.MAP_LANGUAGE);
		userProfileVignette.put(Constants.VGN_LANGUAGE, language
				.substring(0, 2));
		// username are stored by Vignette in lower case
		String userName = ((String) userProfileSession
				.get(Constants.MAP_USERNAME)).toLowerCase();
		userProfileVignette.put(Constants.VGN_USERNAME, userName);
		// email are stored by Vignette in lower case
		String email = ((String) userProfileSession.get(Constants.MAP_EMAIL))
				.toLowerCase();
		userProfileVignette.put(Constants.VGN_EMAIL, email);

		return userProfileVignette;
	}

	/*
	 * Compare the data of the user with the data in the map
	 */
	private boolean isNeedToUpdateVignetteUser(User user, Map userMap) {
		boolean isNeed = false;

		// iterate on the map
		Iterator iter = userMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object value = userMap.get(key);
			if (mLog.isDebugEnabled())
				mLog.debug(key + '=' + value);

			Object userValue = null;
			try {
				userValue = user.getProperty(key);
				// compare the value from the map with the value from the user
				if (value != null && userValue != null
						&& !value.equals(userValue)) {
					isNeed = true;
					mLog.debug("previous value '" + userValue
							+ "'. Need to update");
					break;
				}
			} catch (PropertyNotFoundException e) {
				String info = "The [" + key
						+ "] is not a valide Vignette entity property. "
						+ "It doesn't enter in the comparison.";
				if (mLog.isDebugEnabled())
					mLog.debug(info);
			}
		}

		if (mLog.isDebugEnabled())
			mLog.debug("isNeedToUpdateVignetteUser :" + isNeed);

		return isNeed;
	}

	/*
	 * Update Vignette user by comparison with the userMap
	 */
	private void updateVignetteUserProperties(User user, Map userProfile) {
		if (user != null && userProfile != null) {

			Iterator iter = userProfile.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object newValue = userProfile.get(key);
				if (mLog.isDebugEnabled())
					mLog.debug("check if " + key
							+ " is a Vignette user property");
				// keep only Vignette properties
				try {
					Object originalValue = user.getProperty(key);
					if (originalValue == null)
						originalValue = "";
					if (newValue != null
							&& !newValue.toString().equals(
									originalValue.toString())) {
						user.setProperty(key, newValue);
						if (mLog.isDebugEnabled())
							mLog.debug("set key:" + key + '=' + newValue);
					}
				} catch (PropertyNotFoundException e) {
					String info = "The ["
							+ key
							+ "] is not a valide Vignette entity property. It's not set";
					if (mLog.isDebugEnabled())
						mLog.debug(info);
				} catch (UniquePropertyValueConflictException e) {
					String error = "error during setting property of user with display name ["
							+ user.getDisplayName() + "]. " + e.getMessage();
					mLog.error(error);
					throw new IllegalStateException(error, e);
				} catch (EntityPersistenceException e) {
					String error = "error during setting property of user with display name ["
							+ user.getDisplayName() + "]. " + e.getMessage();
					mLog.error(error);
					throw new IllegalStateException(error, e);
				}
			}
			try {
				user.save();
			} catch (EntityPersistenceException e) {
				String error = "error during save user with display name ["
						+ user.getDisplayName() + "]. " + e.getMessage();
				mLog.error(error);
				throw new IllegalStateException(error, e);
			}
		}
	}

	/*
	 * put the profile in session
	 */
	public void storeProfileInSession(HttpServletRequest request,
			Map userProfile) {
		HttpSession session = request.getSession(true);

		session.setAttribute(Constants.PROFILE_MAP, userProfile);

		// remove url locator to force recomputation
		session.removeAttribute(HPUrlLocator.URL_LOCATOR_MAP);
	}

	/**
	 * Retrieve the attribute names of the UPS query.
	 * 
	 * @param queryId
	 *            the UPS query
	 * @param configKey
	 *            the name of the config key
	 * @return the attribute names of the UPS query
	 * @throws ConfigException
	 */
	public List getQueryAttributeNames(String queryId, String configKey)
			throws ConfigException {
		List attributeNames = new ArrayList();
		String attributeNamesLinked;

		attributeNamesLinked = Config.getValue(configKey);
		StringTokenizer tokenizer = new StringTokenizer(attributeNamesLinked,
				";");
		while (tokenizer.hasMoreTokens()) {
			attributeNames.add(tokenizer.nextToken());
		}
		return attributeNames;
	}

}
