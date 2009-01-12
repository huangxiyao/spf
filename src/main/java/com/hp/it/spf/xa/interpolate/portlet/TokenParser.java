/**
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portlet;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * <p>
 * The concrete subclass for parsing strings looking for tokens to substitute in
 * the portlet context. This class is used heavily by the portlet
 * FileInterpolator - see that class for complete token documentation. See the
 * method documentation here (and in the base class) for a description of the
 * various token substitutions which are supported.
 * </p>
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.TokenParser
 *      com.hp.it.spf.xa.interpolate.portlet.FileInterpolator
 */
public class TokenParser extends com.hp.it.spf.xa.interpolate.TokenParser {

	/**
	 * This class attribute is the name of the container token for a group
	 * section.
	 */
	private static final String TOKEN_GROUP = "GROUP";

	/**
	 * This class attribute is the name of the container token for a portlet
	 * section.
	 */
	private static final String TOKEN_PORTLET = "PORTLET";

	/**
	 * Portlet request.
	 */
	private PortletRequest request = null;

	/**
	 * Portlet response.
	 */
	private PortletResponse response = null;

	/**
	 * Portlet logging.
	 */
	private Log portletLog = LogFactory.getLog(FileInterpolator.class);

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
	 * ContainerMatcher for portlet parsing. The constructor stores a portlet ID
	 * into the class. The match method returns true if the given portlet ID is
	 * a substring (case-insensitive) of the stored portlet ID.
	 */
	protected class PortletMatcher extends ContainerMatcher {

		protected PortletMatcher(String portletID) {
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
	 * <p>
	 * Constructs a new TokenParser for the given portlet request and response.
	 * The default token-substitutions property file (<code>default_tokens.properties</code>)
	 * will be assumed, if subsequent parseToken calls find any
	 * <code>&lt;TOKEN:key&gt;</code> tokens.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request
	 * @param response
	 *            The portlet response
	 */
	public TokenParser(PortletRequest request, PortletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * <p>
	 * Constructs a new TokenParser for the given portlet request and response,
	 * and overriding the token-substitutions property file. The given file,
	 * instead of the default (<code>default_tokens.properties</code>) will
	 * be assumed, if subsequent parseToken calls find any
	 * <code>&lt;TOKEN:key&gt;</code> tokens.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request
	 * @param response
	 *            The portlet response
	 * @param subsFilePath
	 *            The token-substitution filename and path (relative to the
	 *            class loader)
	 */
	/* Added by CK for 1000790073 */
	public TokenParser(PortletRequest request, PortletResponse response,
			String subsFilePath) {
		this.request = request;
		if (subsFilePath != null) {
			this.subsFilePath = subsFilePath;
		}
	}

	/**
	 * <p>
	 * Get a URL for the given file pathname, localized (or not) depending on
	 * the boolean parameter (if true, the URL is for the best-candidate
	 * localized version of that file, otherwise it is just for the file
	 * itself). This should return null if the URL cannot be built (eg the file
	 * is not found). This method is implemented using I18nUtility
	 * getLocalizedFileURL (see).
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

		if (request == null) {
			return null;
		}
		String path = I18nUtility.getLocalizedFileURL(request, response,
				baseFilePath, localized);
		if (path == null || path.trim().length() == 0) {
			return null;
		}
		return path;
	}

	/**
	 * Returns the value of the given user property from the P3P user
	 * information contained in the portlet request given to the constructor.
	 * Returns null if the given key is not found in the user information, or
	 * the value it references is not a string. Also returns null if the user
	 * information is null or guest (ie the user is not logged-in).
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
	 * Log error.
	 * 
	 * @param msg
	 *            log message
	 */
	protected void logError(String msg) {
		if (portletLog.isErrorEnabled()) {
			portletLog.error(msg);
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
	 * Parses the string for any <code>&lt;PORTLET:<i>ids</i>&gt;</code>
	 * content; such content is deleted if the given portlet IDs do not qualify
	 * (otherwise only the special markup is removed). The <i>ids</i> may
	 * include one or more portlet IDs, delimited by "|" for a logical-or.
	 * <code>&lt;PORTLET:<i>ids</i>&gt;</code> markup may be nested for
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
	 *  &lt;PORTLET:abc|def&gt;
	 *  This content is to be displayed only by portlets whose ID's contain abc or def.
	 *  &lt;/PORTLET&gt;
	 * </pre>
	 * 
	 * <p>
	 * If the given portlet IDs include AbcXyz, the returned content string is:
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
	 * But if the given portlet IDs are just xyz, the returned content string
	 * is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all portlets to display.
	 *  
	 *  
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned. If you provide null or
	 * empty portlet IDs, all <code>&lt;PORTLET&gt;</code>-enclosed sections
	 * are removed from the content.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param portletIds
	 *            The portlet IDs.
	 * @return The interpolated string.
	 */
	public String parsePortlet(String content, String portletID) {

		return super.parseContainer(content, TOKEN_PORTLET, new PortletMatcher(
				portletID));
	}

}
