/**
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portlet;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * <p>
 * The concrete subclass for parsing strings looking for tokens to substitute in
 * the portlet context. This class is used heavily by the portlet
 * {@link FileInterpolator}. The following token substitutions are supported by
 * this class (plus those supported by the parent class). See the method
 * documentation here (and in the parent class) for further description.
 * <b>Note:</b> The <code>&lt;</code> and <code>&gt;</code> symbols may be
 * used in place of <code>{</code> and <code>}</code>, if you prefer.
 * </p>
 * <dl>
 * <dt><code>{PORTLET:<i>ids</i>}...{/PORTLET}</code></dt>
 * <dt><code>{ROLE:<i>roles</i>}...{/ROLE}</code></dt>
 * </dl>
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.interpolate.portlet.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.TokenParser</code>
 */
public class TokenParser extends com.hp.it.spf.xa.interpolate.TokenParser {

	/**
	 * This class attribute is the name of the container token for a portlet
	 * section.
	 */
	private static final String TOKEN_PORTLET_CONTAINER = "PORTLET";

	/**
	 * This class attribute is the name of the container token for a role
	 * section.
	 */
	private static final String TOKEN_ROLE_CONTAINER = "ROLE";

	/**
	 * Portlet request.
	 */
	private PortletRequest request = null;

	/**
	 * Portlet response.
	 */
	private PortletResponse response = null;

	/**
	 * <code>ContainerMatcher</code> for portlet parsing. The constructor
	 * stores a portlet ID into the class. The match method returns true if the
	 * given portlet ID is a substring (case-insensitive) of the stored portlet
	 * ID.
	 */
	protected class PortletContainerMatcher extends ContainerMatcher {

		protected PortletContainerMatcher(String portletID) {
			super(portletID);
		}

		protected boolean match(String containerKey) {
			String portletID = (String) subjectOfComparison;
			if (portletID != null && containerKey != null) {
				containerKey = containerKey.trim().toUpperCase();
				portletID = portletID.trim().toUpperCase();
				if (!portletID.equals("") && !containerKey.equals("")) {
					if (portletID.indexOf(containerKey) > -1)
						return true;
				}
			}
			return false;
		}
	}

	/**
	 * <code>ContainerMatcher</code> for role parsing. The constructor stores
	 * a portlet request into the class. The match method returns true if the
	 * stored portlet request is in the role indicated by the given role name.
	 */
	protected class RoleContainerMatcher extends ContainerMatcher {

		protected RoleContainerMatcher(PortletRequest request) {
			super(request);
		}

		protected boolean match(String containerKey) {
			PortletRequest request = (PortletRequest) subjectOfComparison;
			if (request != null && containerKey != null) {
				containerKey = containerKey.trim();
				if (request.isUserInRole(containerKey))
					return true;
			}
			return false;
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>TokenParser</code> for the given portlet request
	 * and response. The default token-substitutions property file (<code>default_tokens.properties</code>)
	 * will be assumed, if subsequent {@link #parseToken(String)} calls find any
	 * <code>{TOKEN:key}</code> tokens.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pResponse
	 *            The portlet response
	 */
	public TokenParser(PortletRequest pRequest, PortletResponse pResponse) {
		this.request = pRequest;
		this.response = pResponse;
	}

	/**
	 * <p>
	 * Constructs a new <code>TokenParser</code> for the given portlet request
	 * and response, and overriding the token-substitutions property file. The
	 * given file, instead of the default (<code>default_tokens.properties</code>)
	 * will be assumed, if subsequent {@link #parseToken(String)} calls find any
	 * <code>{TOKEN:key}</code> tokens.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pResponse
	 *            The portlet response
	 * @param pSubsFilePath
	 *            The token-substitution filename and path (relative to the
	 *            class loader)
	 */
	public TokenParser(PortletRequest pRequest, PortletResponse pResponse,
			String pSubsFilePath) {
		this.request = pRequest;
		this.response = pResponse;
		if (pSubsFilePath != null) {
			this.subsFilePath = pSubsFilePath;
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>TokenParser</code> for the given portlet
	 * request, response, and locale, and overriding the token-substitutions
	 * property file. The given file, instead of the default (<code>default_tokens.properties</code>)
	 * will be assumed, if subsequent {@link #parseToken(String)} calls find any
	 * <code>{TOKEN:key}</code> tokens. In addition, the given locale will be
	 * used instead of the one in the portlet request during the parsing (but if
	 * the given locale is null, then the one in the portlet request will be
	 * used).
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pResponse
	 *            The portlet response
	 * @param pLocale
	 *            The locale to use (if null, uses the one in the portlet
	 *            request)
	 * @param pSubsFilePath
	 *            The token-substitution filename and path (relative to the
	 *            class loader)
	 */
	public TokenParser(PortletRequest pRequest, PortletResponse pResponse,
			Locale pLocale, String pSubsFilePath) {
		this.request = pRequest;
		this.response = pResponse;
		this.locale = pLocale;
		if (pSubsFilePath != null) {
			this.subsFilePath = pSubsFilePath;
		}
	}

	/**
	 * <p>
	 * Get a URL for the given file pathname, localized (or not) for the current
	 * locale depending on the boolean parameter (if true, the URL is for the
	 * best-candidate localized version of that file, otherwise it is just for
	 * the file itself). This method is implemented using
	 * {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility#getLocalizedFileURL(PortletRequest, PortletResponse, String, Locale, boolean)}
	 * (see). The current locale is the one provided to the constructor (or the
	 * one in the portlet request if that was null).
	 * </p>
	 * <p>
	 * The given file pathname should be a base filename and path relative to
	 * the portlet bundle directory.
	 * </p>
	 * 
	 * @param baseFilePath
	 *            A base filename including path relative to portlet bundle
	 *            directory if needed
	 * @param localized
	 *            decide to localize or not
	 * @return The content URL
	 */
	protected String getContentURL(String baseFilePath, boolean localized) {

		if (request == null || response == null) {
			return null;
		}
		return I18nUtility.getLocalizedFileURL(request, response, baseFilePath,
				locale, localized);
	}

	/**
	 * Returns the value of the given user property from the P3P user
	 * information contained in the portlet request given to the constructor.
	 * Returns null if the given key is not found in the user information, or
	 * the value it references is not a string. Also returns null if the user
	 * information is null or guest (ie the user is not logged-in). This method
	 * is implemented using
	 * {@link com.hp.it.spf.xa.misc.portlet.Utils#getUserProperty(PortletRequest, String)}
	 * (see).
	 * 
	 * @param key
	 *            The user property name
	 * @return The user property value
	 */
	protected String getUserProperty(String key) {
		if (request == null || key == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(request, key);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the locale from the one provided to the constructor, or if that is
	 * null, then from the one in the portlet request provided to the
	 * constructor. Returns null if both the locale and portlet request were
	 * null.
	 * 
	 * @return The current preferred locale for the user
	 */
	protected Locale getLocale() {
		if ((locale == null) && (request != null)) {
			return request.getLocale();
		}
		return locale;
	}

	/**
	 * Get the email address from the user profile map (<code>PortletRequest.USER_INFO</code>)
	 * in the portlet request provided to the constructor. Returns null if this
	 * has not been set in the request (eg, when the user is not logged-in), or
	 * the request provided to the constructor was null.
	 * 
	 * @return email
	 */
	protected String getEmail() {
		if (request == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(request, Consts.KEY_EMAIL);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the first (given) name from the user profile map (<code>PortletRequest.USER_INFO</code>)
	 * in the portlet request provided to the constructor. Returns null if this
	 * has not been set in the request (eg, when the user is not logged-in), or
	 * the request provided to the constructor was null.
	 * 
	 * @return first (given) name
	 */
	protected String getFirstName() {
		if (request == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(request,
					Consts.KEY_FIRST_NAME);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the last (family) name from the user profile map (<code>PortletRequest.USER_INFO</code>)
	 * in the portlet request provided to the constructor. Returns null if this
	 * has not been set in the request (eg, when the user is not logged-in), or
	 * the request provided to the constructor was null.
	 * 
	 * @return last (family) name
	 */
	protected String getLastName() {
		if (request == null) {
			return null;
		}
		try {
			return (String) Utils
					.getUserProperty(request, Consts.KEY_LAST_NAME);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the site name from the portlet request provided to the constructor.
	 * Returns null if this has not been set in the request, or the request
	 * provided to the constructor was null.
	 * 
	 * @return site name
	 */
	protected String getSite() {
		if (request == null) {
			return null;
		}
		return Utils.getPortalSiteName(request);
	}

	/**
	 * Get the portal site root (ie home page) URL for the current portal site,
	 * from the portlet request provided to the constructor. Returns null if
	 * this has not been set in the request, or the request provided to the
	 * constructor was null.
	 * 
	 * @return site URL string
	 */
	protected String getSiteURL() {
		if (request == null) {
			return null;
		}
		return Utils.getPortalSiteURL(request);
	}

	/**
	 * Get the portal request URL for the current request. This is the URL which
	 * was opened by the browser in order to invoke this portlet. It is obtained
	 * from the portlet request provided to the constructor. Returns null if
	 * this has not been set in the request, or the request provided to the
	 * constructor was null.
	 * 
	 * @return request URL string
	 */
	protected String getRequestURL() {
		if (request == null) {
			return null;
		}
		return Utils.getPortalRequestURL(request);
	}

	/**
	 * Get the authorization groups from the portlet request provided to the
	 * constructor. Returns null if these have not been set in the request, or
	 * the request provided to the constructor was null.
	 * 
	 * @return list of groups
	 */
	protected String[] getGroups() {
		if (request == null) {
			return null;
		}
		return Utils.getGroups(request);
	}

	/**
	 * Return true if the user is logged-in, false otherwise. The login status
	 * is indicated in the portlet request provided to the constructor; false is
	 * also returned if that request was null.
	 * 
	 * @return true if logged-in, false if not logged-in
	 */
	protected boolean getLoginStatus() {
		if (request == null) {
			return false;
		}
		return Utils.isAuthenticatedUser(request);
	}

	/**
	 * Get the portlet ID (ie the Vignette portlet friendly ID) from the portlet
	 * request provided to the constructor. Returns null if this has not been
	 * set in the request, or the request provided to the constructor was null.
	 * 
	 * @return portlet ID
	 */
	protected String getPortletID() {
		if (request == null) {
			return null;
		}
		return Utils.getPortletID(request);
	}

	/**
	 * <p>
	 * Parses the given string, performing all of the token substitutions and
	 * interpolations supported by this class and the parent class. For a list
	 * and description of all the supported tokens, see the other parse methods -
	 * this method calls all of them in series. The order of token evaluation is
	 * as follows:
	 * </p>
	 * <ol>
	 * <li>First, all the tokens parsed by the superclass
	 * {@link com.hp.it.spf.xa.interpolate.TokenParser#parse(String)}</li>
	 * <li>Then <code>{PORTLET:<i>portlets</i>}...{/PORTLET}</code></li>
	 * <li>Then <code>{ROLE:<i>roles</i>}...{/ROLE}</code></li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>Note:</b> The <code>&lt;</code> and <code>&gt;</code> symbols are
	 * also supported for the token boundaries, in place of <code>{</code> and
	 * <code>}</code>.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parse(String content) {

		if (content == null) {
			return null;
		}

		// start parsing and substituting the tokens, using the superclass
		content = super.parse(content);

		// parse the portlet token
		content = parsePortletContainer(content);

		// parse the role token
		content = parseRoleContainer(content);

		// Done.
		return (content);
	}

	/**
	 * <p>
	 * Parses the string for any <code>{PORTLET:<i>ids</i>}</code> content;
	 * such content is deleted if the current portlet ID does not qualify
	 * (otherwise only the special markup is removed). The <i>ids</i> may
	 * include one or more portlet IDs, delimited by "|" for a logical-or.
	 * <code>{PORTLET:<i>ids</i>}</code> markup may be nested for
	 * logical-and (however since any one portlet has only one portlet ID, the
	 * desire to logical-and seems unlikely).
	 * </p>
	 * 
	 * <p>
	 * <b>Note:</b> As of this writing, the portlet ID's should be portlet
	 * friendly ID's provided by Vignette automatically. A portlet ID provided
	 * by Vignette matches the one in the token, if the token is a
	 * case-insensitive substring of it.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all portlets to display.
	 *  {PORTLET:abc|def}
	 *  This content is to be displayed only by portlets whose ID's contain abc or def.
	 *  {/PORTLET}
	 * </pre>
	 * 
	 * <p>
	 * If the given portlet ID is <code>AbcXyz</code>, the returned content
	 * string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all portlets to display.
	 *  
	 *  This content is to be displayed only by portlets whose ID's contain abc or def.
	 *     
	 * </pre>
	 * 
	 * <p>
	 * But if the given portlet ID is just <code>xyz</code>, the returned
	 * content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all portlets to display.
	 *  
	 *  
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned. The current portlet ID is
	 * obtained from the {@link #getPortletID()} method - if it returns a null
	 * or empty portlet ID, all <code>{PORTLET}</code>-enclosed sections are
	 * removed from the content.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parsePortletContainer(String content) {

		return super.parseContainer(content, TOKEN_PORTLET_CONTAINER,
				new PortletContainerMatcher(getPortletID()));
	}

	/**
	 * <p>
	 * Parses the string for any <code>{ROLE:<i>roles</i>}</code> content;
	 * such content is deleted if the given role names do not qualify (otherwise
	 * only the special markup is removed). The <i>roles</i> may include one or
	 * more role names, delimited by "|" for a logical-or.
	 * <code>{ROLE:<i>roles</i>&GT;</code> markup may be nested for
	 * logical-and.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  {ROLE:abc|def}
	 *  This content is only for users in the abc or def roles.
	 *    {ROLE:xyz}
	 *    This content is only for users in both the xyz role 
	 *    and the abc or def roles.
	 *    {/ROLE}
	 *  {/ROLE}
	 * </pre>
	 * 
	 * <p>
	 * If the given roles include abc, def, and xyz, the returned content string
	 * is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for users in the abc or def roles.
	 *  
	 *    his content is only for users in both the xyz role  
	 *    and the abc or def roles.
	 *    
	 *    
	 * </pre>
	 * 
	 * <p>
	 * But if the given roles include only abc, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for users in the abc or def roles.
	 *  
	 *  
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned. If you provide null or
	 * empty roles, all <code>{ROLE}</code>-enclosed sections are removed
	 * from the content.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseRoleContainer(String content) {

		return super.parseContainer(content, TOKEN_ROLE_CONTAINER,
				new RoleContainerMatcher(request));
	}
}
