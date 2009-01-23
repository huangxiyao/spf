/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portal;

import java.util.HashMap;
import java.util.Map;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * The concrete subclass for parsing strings looking for tokens to substitute in
 * the portal context. This class is used heavily by the portal
 * FileInterpolator. The following token substitutions are supported by this
 * class (plus those supported by the base class). See the method documentation
 * here (and in the base class) for further description.
 * </p>
 * <dl>
 * <dt><code>&lt;GROUP:<i>groups</i>&gt;...&lt;/GROUP&gt;</code></dt>
 * </dl>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.TokenParser
 *      com.hp.it.spf.xa.interpolate.portal.FileInterpolator
 */
public class TokenParser extends com.hp.it.spf.xa.interpolate.TokenParser {

	/**
	 * Portal context.
	 */
	private PortalContext portalContext = null;

	/**
	 * Portal logging.
	 */
	private LogWrapper portalLog = new LogWrapper(FileInterpolator.class);

	/**
	 * <p>
	 * Constructs a new TokenParser for the given portal context. The default
	 * token-substitutions property file (<code>default_tokens.properties</code>)
	 * will be assumed, if subsequent parseToken calls find any
	 * <code>&lt;TOKEN:key&gt;</code> tokens.
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
	 * Constructs a new TokenParser for the given portlet request, and
	 * overriding the token-substitutions property file. The given file, instead
	 * of the default (<code>default_tokens.properties</code>) will be
	 * assumed, if subsequent parseToken calls find any
	 * <code>&lt;TOKEN:key&gt;</code> tokens.
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
	 * Get a portal URL for the given file pathname, localized (or not)
	 * depending on the boolean parameter (if true, the URL is for the
	 * best-candidate localized version of that file, otherwise it is just for
	 * the file itself). This should return null if the URL cannot be built (eg
	 * the file is not found). This method is implemented using I18nUtility
	 * getLocalizedFileURL (see).
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
				localized);
	}

	/**
	 * Returns the value of the given user property from the Vignette user
	 * object contained in the current portal context. Returns null if the given
	 * key is not found in the user object or the value it references is not a
	 * string. Also returns null if the user object is null or guest (ie the
	 * user is not logged-in).
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
	 * Log error.
	 * 
	 * @param msg
	 *            log
	 */
	protected void logError(String msg) {
		portalLog.error(msg);
	}
}