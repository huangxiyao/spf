/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portal;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.User;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * The concrete subclass for parsing strings looking for tokens to substitute in
 * the portal context. This class is used heavily by the portal
 * {@link FileInterpolator}. This class supports the exact same token
 * substitutions as the base class - see the base class method documentation for
 * further description.
 * </p>
 * <dl>
 * <dt><code>&lt;GROUP:<i>groups</i>&gt;...&lt;/GROUP&gt;</code></dt>
 * </dl>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.interpolate.portal.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.TokenParser</code>
 */
public class TokenParser extends com.hp.it.spf.xa.interpolate.TokenParser {

	/**
	 * Portal context.
	 */
	private PortalContext portalContext = null;

	/**
	 * <p>
	 * Constructs a new <code>TokenParser</code> for the given portal context.
	 * The default token-substitutions property file (<code>default_includes.properties</code>)
	 * will be assumed, if subsequent {@link #parseInclude(String)} calls find any
	 * <code>&lt;INCLUDE:key&gt;</code> tokens.
	 * </p>
	 * 
	 * @param portalContext
	 *            The portal context
	 */
	public TokenParser(PortalContext portalContext) {
		this.portalContext = portalContext;
	}

	/**
	 * <p>
	 * Constructs a new <code>TokenParser</code> for the given portal context,
	 * and overriding the token-substitutions property file. The given file,
	 * instead of the default (<code>default_includes.properties</code>) will
	 * be assumed, if subsequent {@link #parseInclude(String)} calls find any
	 * <code>&lt;INCLUDE:key&gt;</code> tokens.
	 * </p>
	 * 
	 * @param portalContext
	 *            The portal context
	 * @param theSubsFilePath
	 *            The token-substitution filename and path (relative to the
	 *            class loader)
	 */
	public TokenParser(PortalContext portalContext, String theSubsFilePath) {
		this.portalContext = portalContext;
		if (theSubsFilePath != null) {
			this.subsFilePath = theSubsFilePath;
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>TokenParser</code> for the given portal context
	 * and locale, and overriding the token-substitutions property file. The
	 * given file, instead of the default (<code>default_includes.properties</code>)
	 * will be assumed, if subsequent {@link #parseInclude(String)} calls find
	 * any <code>&lt;INCLUDE:key&gt;</code> tokens. In addition, the given
	 * locale will be used instead of the one in the portal context during the
	 * parsing (but if the given locale is null, then the one in the portal
	 * context will be used).
	 * </p>
	 * 
	 * @param portalContext
	 *            The portal context
	 * @param pLocale
	 *            The locale to use (if null, uses the one in the portal
	 *            context)
	 * @param theSubsFilePath
	 *            The token-substitution filename and path (relative to the
	 *            class loader)
	 */
	public TokenParser(PortalContext portalContext, Locale pLocale,
			String theSubsFilePath) {
		this.portalContext = portalContext;
		this.locale = pLocale;
		if (theSubsFilePath != null) {
			this.subsFilePath = theSubsFilePath;
		}
	}

	/**
	 * <p>
	 * Get a portal URL for the given file pathname, localized (or not) for the
	 * current locale depending on the boolean parameter (if true, the URL is
	 * for the best-candidate localized version of that file, otherwise it is
	 * just for the file itself). This should return null if the URL cannot be
	 * built (eg the file is not found). This method is implemented using
	 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility#getLocalizedFileURL(PortalContext, String, Locale, boolean)}
	 * (see). The current locale is the one provided to the constructor (or the
	 * one in the portlet request if that was null).
	 * </p>
	 * <p>
	 * The given file pathname should be a base filename of a file that has been
	 * loaded into the portal as a "secondary support file".
	 * </p>
	 * 
	 * @param baseFilePath
	 *            A base filename for a secondary support file
	 * @param localized
	 *            Decide to localize or not
	 * @return The content URL
	 */
	protected String getContentURL(String baseFilePath, boolean localized) {

		if (portalContext == null) {
			return null;
		}
		return I18nUtility.getLocalizedFileURL(portalContext, baseFilePath,
				locale, localized);
	}

	/**
	 * Returns the value of the given user property from the Vignette user
	 * object contained in the current portal context. Returns null if the given
	 * key is not found in the user object or the value it references is not a
	 * string. Also returns null if the user object is null or guest (ie the
	 * user is not logged-in). This method is implemented using
	 * {@link com.hp.it.spf.xa.misc.portal.Utils#getUserProperty(PortalContext, String)}
	 * (see).
	 * 
	 * @param key
	 *            The user property name
	 * @return The user property value
	 */
	protected String getUserProperty(String key) {
		if (portalContext == null || key == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext, key);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the locale from the one provided to the constructor, or if that is
	 * null, then from the one in the portal context provided to the
	 * constructor. Returns null if both the locale and portal context were
	 * null.
	 * 
	 * @return The current preferred locale for the user
	 */
	protected Locale getLocale() {
		if ((locale == null) && (portalContext != null)) {
			return I18nUtility.getLocale(portalContext.getPortalRequest()
					.getRequest());
		}
		return locale;
	}

	/**
	 * Get the email address from the portal <code>User</code> object in the
	 * portal context (portal request) provided to the constructor. Returns null
	 * if this has not been set in the request (eg, when the user is not
	 * logged-in), or the portal context provided to the constructor was null.
	 * 
	 * @return email
	 */
	protected String getEmail() {
		if (portalContext == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext,
					Consts.PROPERTY_EMAIL_ID);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the first (given) name from the portal <code>User</code> object in
	 * the portal context (portal request) provided to the constructor. Returns
	 * null if this has not been set in the request (eg, when the user is not
	 * logged-in), or the portal context provided to the constructor was null.
	 * 
	 * @return first (given) name
	 */
	protected String getFirstName() {
		if (portalContext == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext,
					Consts.PROPERTY_FIRSTNAME_ID);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the last (family) name from the portal <code>User</code> object in
	 * the portal context (portal request) provided to the constructor. Returns
	 * null if this has not been set in the request (eg, when the user is not
	 * logged-in), or the portal context provided to the constructor was null.
	 * 
	 * @return last (family) name
	 */
	protected String getLastName() {
		if (portalContext == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext,
					Consts.PROPERTY_LASTNAME_ID);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the site name for the current portal request from the portal context
	 * provided to the constructor. Returns null if this has not been set in the
	 * request, or the portal context provided to the constructor was null.
	 * 
	 * @return site name
	 */
	protected String getSite() {
		if (portalContext == null) {
			return null;
		}
		try {
			String siteName = portalContext.getCurrentSite().getDNSName();
			return siteName;
		} catch (Exception e) {
			return null;
		}
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
		if (portalContext == null) {
			return null;
		}
		return portalContext.getCurrentBasePortalURI();
	}

	/**
	 * Get the portal request URL for the current request. This is the URL which
	 * was opened by the browser in order to invoke this page. It is obtained
	 * from the portal context provided to the constructor. Returns null if this
	 * has not been set in the request, or the context provided to the
	 * constructor was null.
	 * 
	 * @return request URL string
	 */
	protected String getRequestURL() {
		if (portalContext == null) {
			return null;
		}
		try {
			HttpServletRequest request = portalContext.getHttpServletRequest();
			return Utils.getRequestURL(request);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the authorization groups from the portlet request provided to the
	 * constructor. Returns null if these have not been set in the request, or
	 * the request provided to the constructor was null.
	 * 
	 * @return list of groups
	 */
	protected String[] getGroups() {
		if (portalContext == null) {
			return null;
		}
		return Utils.getGroups(portalContext);
	}

	/**
	 * Return true if the user is logged-in, false otherwise. The login status
	 * is indicated in the portal context given to the constructor; false is
	 * also returned if that context was null.
	 * 
	 * @return true if logged-in, false if not logged-in
	 */
	protected boolean getLoginStatus() {
		if (portalContext == null) {
			return false;
		}
		try {
			HttpSession session = portalContext.getHttpServletRequest()
					.getSession();
			User currentUser = SessionUtils.getCurrentUser(session);
			return !currentUser.isGuestUser();
		} catch (Exception e) {
			return false;
		}
	}

}