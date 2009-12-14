package com.hp.it.spf.user.profile.manager;

import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.log.LogConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Test user profile retriever which reads the profile attributes from available in class path
 * property file named <code>{site name}_{user profile ID}.properties</code>.
 * <p>
 * <strong>IMPORTANT:</strong> This class is a smaple/test implementation and should not be used in production environment.
 * </p>
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PropertyFileUserProfileRetriever implements IUserProfileRetriever
{
	private static final LogWrapper LOG = AuthenticatorHelper.getLog(PropertyFileUserProfileRetriever.class);

	/**
	 * Reads the user profile from a property file named {site name}_{user identifier}.properties.
	 * The file must be located in the classpath. The format of the file is the same as defined
	 * for <code>java.util.Properties</code> class. The "site name" part of the file name corresponds
	 * to site DNS name. The "user identifier" part of the file name is the profile ID value (not
	 * the user name).
	 * If the file cannot be found this method returns an empty map.
	 *
	 * @param userIdentifier profile ID of the user; note that this is not the user name but rather
	 * unique user ID such as HPP GUID
	 * @param request portal request
	 * @return Map containing the profile loaded from the file available in classpath.
	 * @throws UserProfileException If an error occurs when reading the file.
	 */
	public Map<String, Object> getUserProfile(String userIdentifier, HttpServletRequest request) throws UserProfileException
	{
		if (userIdentifier == null || userIdentifier.trim().equals("")) {
			return Collections.emptyMap();
		}

		String siteName = getSiteName(request);
		String profileFileName = siteName + "_" + userIdentifier + ".properties";

		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("Loading profile file for profile ID '" + userIdentifier + "' and site '" + siteName + "' (" + profileFileName + ")");
		}

		InputStream fileStream = getClass().getResourceAsStream("/" + profileFileName);
		if (fileStream == null) {
			LOG.warning("Unable to find profile file bundle for name: " + profileFileName);
			return Collections.emptyMap();
		}

		Properties fileContent = new Properties();
		try {
			try {
				fileContent.load(fileStream);
			}
			finally {
				fileStream.close();
			}
		}
		catch (IOException e) {
			throw new UserProfileException("Error loading profile file from classpath: " + profileFileName, e);
		}

		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("Profile loaded from '" + profileFileName + "': " + fileContent);
		}
		return asMap(fileContent);
	}

	/**
	 * Converts properties object to a map.
	 *
	 * @param props Properties object to convert
	 * @return map containing the entries from the <code>props</code>
	 */
	private Map<String, Object> asMap(Properties props)
	{
		Map<String, Object> result = new TreeMap<String, Object>();
		for (Map.Entry<Object, Object> prop : props.entrySet()) {
			result.put(String.valueOf(prop.getKey()), prop.getValue());
		}
		return result;
	}

	/**
	 * Returns DNS site name or "console" if none could be found in the request.
	 *
	 * @param request portal request
	 * @return site DNS name
	 */
	protected String getSiteName(HttpServletRequest request)
	{
		if (Utils.getEffectiveSite(request) == null) {
			return "console";
		}

		return Utils.getEffectiveSiteDNS(request);
	}

}
