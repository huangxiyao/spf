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
 * the portal context. This class is used heavily by the portal FileInterpolator -
 * see that class for complete token documentation. See the method documentation
 * here (and in the base class) for a description of the various token
 * substitutions which are supported.
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.TokenParser
 *      com.hp.it.spf.xa.interpolate.portal.FileInterpolator
 */
public class TokenParser extends com.hp.it.spf.xa.interpolate.TokenParser {

	/**
	 * This class attribute is the name of the container token for a group
	 * section.
	 */
	private static final String TOKEN_GROUP = "GROUP";

	/**
	 * Portal context.
	 */
	private PortalContext portalContext = null;

	/**
	 * Portal logging.
	 */
	private LogWrapper portalLog = new LogWrapper(FileInterpolator.class);

	/**
	 * ContainerMatcher for group parsing. The constructor stores an array of
	 * group names into the class. The match method returns true if the given
	 * group name exactly matches (case-insensitive) any of the stored group
	 * names.
	 */
	protected class GroupMatcher extends ContainerMatcher {

		protected GroupMatcher(String[] groups) {
			super(groups);
		}

		protected boolean match(String containerKey) {
			String[] groups = (String[]) subjectOfComparison;
			if (groups != null && containerKey != null) {
				containerKey = containerKey.trim();
				if (!containerKey.equals("")) {
					for (int i = 0; i < groups.length; i++) {
						if (groups[i] != null) {
							if (groups[i].trim().equalsIgnoreCase(containerKey)) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}
	}

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
	 * Parses the string for any <code>&lt;GROUP:<i>groups</i>&gt;</code>
	 * content; such content is deleted if the given user groups do not qualify
	 * (otherwise only the special markup is removed). The <i>groups</i> may
	 * include one or more group names, delimited by "|" for a logical-or.
	 * <code>&lt;GROUP:<i>groups</i>&GT;</code> markup may be nested for
	 * logical-and.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  &lt;GROUP:abc|def&gt;
	 *  This content is only for members of the abc or def groups.
	 *    &lt;GROUP:xyz&gt;
	 *    This content is only for members of both the xyz group 
	 *    and the abc or def groups.
	 *    &lt;/GROUP&gt;
	 *  &lt;/GROUP&gt;
	 * </pre>
	 * 
	 * <p>
	 * If the given user groups include abc, def, and xyz, the returned content
	 * string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for members of the abc or def groups.
	 *  
	 *    This content is only for members of both the xyz group 
	 *    and the abc or def groups.
	 *    
	 *    
	 * </pre>
	 * 
	 * <p>
	 * But if the given user groups include only abc, the returned content
	 * string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for members of the abc or def groups.
	 *  
	 *  
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned. If you provide null or
	 * empty groups, all <code>&lt;GROUP&gt;</code>-enclosed sections are
	 * removed from the content.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param userGroups
	 *            The user groups.
	 * @return The interpolated string.
	 */
	public String parseGroup(String content, String[] userGroups) {

		return super.parseContainer(content, TOKEN_GROUP, new GroupMatcher(
				userGroups));
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
		String path = I18nUtility.getLocalizedFileURL(portalContext,
				baseFilePath, localized);
		if (path == null || path.trim().length() == 0) {
			return null;
		} else {
			String contextPath = portalContext.getPortalRequest()
					.getContextPath();
			if (contextPath == null || contextPath.trim().length() == 0) {
				return null;
			} else if (!path.startsWith(contextPath)) {
				return contextPath + "/" + path;
			} else {
				return path;
			}
		}
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