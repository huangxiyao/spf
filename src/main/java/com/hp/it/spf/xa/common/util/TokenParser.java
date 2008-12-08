/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 */
package com.hp.it.spf.xa.common.util;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * An abstract base class for parsing strings looking for tokens to substitute
 * in portal and portlet contexts. See the concrete portal and portlet
 * subclasses for further information. This base class and its subclasses are
 * used heavily by the portal and portlet FileInterpolator classes. See the
 * method documentation for a description of the various token substitutions
 * which are supported.
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.cas.spf.portal.utils.TokenParser
 *      com.hp.it.cas.spf.portlet.utils.TokenParser
 *      com.hp.it.spf.xa.common.util.FileInterpolator
 *      com.hp.it.cas.spf.portal.utils.FileInterpolator
 *      com.hp.it.cas.spf.portlet.utils.FileInterpolator
 * 
 */
public abstract class TokenParser {

	/**
	 * This class attribute will hold the pathname (relative to the class
	 * loader) of the token-substitution file to use by default. The file
	 * extension <code>.properties</code> is required and assumed.
	 */
	private static final String DEFAULT_SUBS_PATHNAME = "default_tokens";

	/**
	 * This class attribute is the token for the user's ISO 639-1 language code.
	 */
	private static final String TOKEN_LANGUAGE_CODE = "<LANGUAGE-CODE>";

	/**
	 * This class attribute is the token for the user's RFC 3066 language tag.
	 */
	private static final String TOKEN_LANGUAGE_TAG = "<LANGUAGE-TAG>";

	/**
	 * This class attribute is the token for a localized content URL.
	 */
	private static final String TOKEN_LOCALIZED_CONTENT_URL = "<LOCALIZED-CONTENT-URL:";

	/**
	 * This class attribute is the token for an unlocalized content URL.
	 */
	private static final String TOKEN_CONTENT_URL = "<CONTENT-URL:";

	/**
	 * This class attribute is the token for a keyname to be substituted from
	 * the token-substitutions file.
	 */
	/* Added by CK for 1000790073 */
	private static final String TOKEN_TOKEN = "<TOKEN:";

	/**
	 * This class attribute is the token for the user's email address.
	 */
	private static final String TOKEN_EMAIL = "<EMAIL>";

	/**
	 * This class attribute is the token for the current portal site.
	 */
	/* Added by CK for 1000790073 */
	private static final String TOKEN_SITE = "<SITE>";

	private int containerIndex; // For container parsing.
	private int containerLevel; // For container parsing.
	private String newContent; // For container parsing.
	private String oldContent; // For container parsing.
	private String containerStartToken; // For container parsing.
	private String containerEndToken; // For container parsing.
	private ContainerMatcher containerMatcher; // For container parsing.

	private static final int FOUND_CONTAINER_START_MATCH = 3; // container
	// parsing
	private static final int FOUND_CONTAINER_START_NOMATCH = 2; // container
	// parsing
	private static final int FOUND_CONTAINER_END = 1; // container parsing
	private static final int FOUND_END = 0; // container parsing
	private static final String TOKEN_CONTAINER_OR = "|"; // container parsing

	/* Added by CK for 1000790073 */
	/*
	 * This class attribute will hold the base filename of the
	 * token-substitution file to use. It is initialized with the name of the
	 * default token-substitution file (not used in R3.1.1, reserved for future
	 * use). (It may be overwritten by the concrete subclass constructor ?that
	 * is what will happen when the linkout portlet instantiates it.)
	 */
	protected String subsFilePath = DEFAULT_SUBS_PATHNAME;

	/**
	 * <p>
	 * Get a URL for the given file pathname, localized (or not) depending on
	 * the boolean parameter (if true, the URL is for the best-candidate
	 * localized version of that file, otherwise it is just for the file
	 * itself). This should return null if the URL cannot be built (eg the file
	 * is not found). Different action by portal and portlet, so therefore this
	 * is an abstract method.
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
	 *            Decide to localize or not
	 * @return The content URL
	 */
	protected abstract String getContentURL(String baseFilePath,
			boolean localized);

	/**
	 * An inner class interface for deciding whether a content section enclosed
	 * within a container token should be included in the interpolated content.
	 * Different action by portal and portlet, and also different depending on
	 * the type of container token, so therefore this is an interface. It should
	 * be implemented in the concrete subclass for each type of container token
	 * that subclass (portal or portlet) may encounter.
	 */
	protected abstract class ContainerMatcher {
		protected Object subjectOfComparison = null;

		/**
		 * Constructor.
		 * 
		 * @param subject
		 *            An object regarding the current user request against which
		 *            to compare the container keys.
		 */
		protected ContainerMatcher(Object subject) {
			subjectOfComparison = subject;
		}

		/**
		 * Return true if the subject of comparison matches the given container
		 * key. Otherwise return false.
		 * 
		 * @param containerKey
		 *            A key from a container token.
		 * @return true
		 */
		protected abstract boolean match(String containerKey);
	}

	/**
	 * Log an error. Different action by portal and portlet, so therefore this
	 * is an abstract method.
	 * 
	 * @param msg
	 *            log
	 */
	protected abstract void logError(String msg);

	/**
	 * <p>
	 * Parses the given string, substituting the given ISO 639-1 language code
	 * for the <code>&lt;LANGUAGE-CODE&gt;</code> token. For example:
	 * <code>&lt;a
	 * href="https://ovsc.hp.com?lang=&lt;LANGUAGE-CODE&gt;"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?lang=ja"&gt;go to OVSC&lt;/a&gt;</code>
	 * when you provide the language code "ja". If you provide a null language
	 * code, the token is replaced with blank. If you provide null content, null
	 * is returned.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param lang
	 *            The ISO 639-1 language code.
	 * 
	 * @return The interpolated string.
	 */
	public String parseLanguageCode(String content, String lang) {
		if (content == null) {
			return null;
		}
		if (lang == null) {
			lang = "";
		}
		return content.replaceAll(TOKEN_LANGUAGE_CODE, lang);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given RFC 3066 language tag for
	 * the <code>&lt;LANGUAGE-TAG&gt;</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?lang=&lt;LANGUAGE-TAG&gt;"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?lang=zn-CN"&gt;go to OVSC&lt;/a&gt;</code>
	 * when you provide the language tag "zh-CN". If you provide a null language
	 * tag, the token is replaced with blank. If you provide null content, null
	 * is returned.
	 * </p>
	 * 
	 * @param lang
	 *            The RFC 3066 language tag.
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseLanguageTag(String content, String lang) {
		if (content == null) {
			return null;
		}
		if (lang == null) {
			lang = "";
		}
		return content.replaceAll(TOKEN_LANGUAGE_TAG, lang);
	}

	/**
	 * <p>
	 * Parses the given string, converting the
	 * <code>&lt;LOCALIZED-CONTENT-URL:<i>pathname</i>&gt;</code> token into
	 * a URL for the best-candidate localized form of that <i>pathname</i>.
	 * Actual implementation depends on portal or portlet context (eg for
	 * getting the locale). For example: <code>&lt;img
	 * src="&lt;LOCALIZED-CONTENT-URL:/images/done.jpg&gt;"&gt; is returned as:
	 * &lt;img src="/<i>implementation-dependent</i>/images/done_fr.jpg"&gt;</code>
	 * when the user is French and the French version of <code>done.jpg</code>
	 * (ie <code>done_fr.jpg</code>) exists. If a content URL cannot be
	 * determined (eg the file in the <i>pathname</i> is not found), then the
	 * token is replaced with blank. If you provide null content, null is
	 * returned.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseLocalizedContentURL(String content) {
		return parseContentURL(content, TOKEN_LOCALIZED_CONTENT_URL, true);
	}

	/**
	 * <p>
	 * Parses the given string, converting the
	 * <code>&lt;CONTENT-URL:<i>pathname</i>&gt;</code> token into a URL for
	 * that <i>pathname</i>. Localization is not performed. Actual
	 * implementation depends on portal or portlet context. For example:
	 * <code>&lt;img src="&lt;CONTENT-URL:/images/picture.jpg&gt;"&gt;</code>
	 * is returned as:
	 * <code>&lt;img src="<i>implementation-dependent</i>/images/picture.jpg"&gt;</code>.
	 * If a content URL cannot be determined (eg the file in the <i>pathname</i>
	 * is not found), then the token is replaced with blank. If you provide null
	 * content, null is returned.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseNoLocalizedContentURL(String content) {
		return parseContentURL(content, TOKEN_CONTENT_URL, false);
	}

	/**
	 * Parses the given string, converting the given URL token into proper form.
	 * 
	 * @param localized
	 *            the localized
	 * @param type
	 *            the type
	 * @param content
	 *            the content
	 * @param request
	 *            the request
	 * @return the string
	 */
	private String parseContentURL(String content, String type,
			boolean localized) {
		if (content == null) {
			return null;
		}
		String regEx = "(" + type + ".*?>)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String s = m.group();
			String sub = s.substring(s.indexOf(type) + type.length(), s
					.length() - 1);
			try {
				String path = getContentURL(sub, localized);
				if (path == null) {
					content = content.replaceAll(s, "");
					throw new Exception();
				}
				content = content.replaceAll(s, path);
			} catch (Exception e) {
				logError("Cannot get the localized content URL for file: "
						+ sub);
			}
		}
		return content;
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given email address for the
	 * <code>&lt;EMAIL&gt;</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?email=&lt;EMAIL&gt;"&gt;go to OVSC&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="https://ovsc.hp.com?email=me@foo.com"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * when you provide the email address "me@foo.com". If you provide a null
	 * email, the token is replaced with blank. If you provide null content,
	 * null is returned.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param email
	 *            The email address.
	 * @return The interpolated string.
	 */
	public String parseEmail(String content, String email) {
		if (content == null) {
			return null;
		}
		if (email == null) {
			email = "";
		}
		return content.replaceAll(TOKEN_EMAIL, email);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given site name for the
	 * <code>&lt;SITE&gt;</code> token. The site name is intended to be the
	 * name for the virtual portal site. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?site=&lt;SITE&gt;"&gt;go to OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a href="https://ovsc.hp.com?site=acme"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * when you provide the site name "acme". If you provide a null site name,
	 * the token is replaced with blank. If you provide null content, null is
	 * returned.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param siteName
	 *            The site name
	 * @return The interpolated string.
	 */
	/* Added by CK for 1000790073 */
	public String parseSite(String content, String siteName) {
		if (content == null) {
			return null;
		}
		if (siteName == null) {
			siteName = "";
		}
		return content.replaceAll(TOKEN_SITE, siteName);
	}

	/**
	 * <p>
	 * Parses the given string, converting the
	 * <code>&lt;TOKEN:<i>key</i>&gt;</code> token into the value for that
	 * key in the token-substitutions file (<code>default_tokens.properties</code>
	 * by default, unless overridden in the constructor). For example:
	 * <code>&lt;img src="&lt;TOKEN:url.image&gt;"&gt;</code> is returned as:
	 * <code>&lt;img src="http://foo.hp.com/images/picture.jpg"&gt;</code>
	 * assuming the token-substitutions file contains the following property:
	 * <code>url.image=http://foo.hp.com/images/picture.jpg</code>. If the
	 * token key is not found in the file, then the token is replaced with
	 * blank. If you provide null content, null is returned.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	/* Added by CK for 1000790073 */
	public String parseToken(String content) {
		if (content == null) {
			return null;
		}
		String regEx = "(" + TOKEN_TOKEN + ".*?>)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String str = m.group();
			String key = str.substring(str.indexOf(TOKEN_TOKEN)
					+ TOKEN_TOKEN.length(), str.length() - 1);
			String token = getToken(key);
			if (token != null) {
				content = content.replaceAll(str, token);
			} else {
				content = content.replaceAll(str, "");
			}
		}
		return content;

	}

	/**
	 * Get token key value from token substitutions file, using the
	 * PropertyResourceBundleManager to hot-load the properties file from
	 * anywhere searched by the class loader.
	 * 
	 * @param key
	 *            token key
	 * @return value
	 * @see com.hp.it.spf.xa.common.util.PropertyResourceBundleManager
	 */
	/* Added by CK for 1000790073 */
	private String getToken(String key) {
		String tokenValue = null;
		try {
			ResourceBundle resBundle = PropertyResourceBundleManager
					.getBundle(this.subsFilePath);
			if (resBundle != null) {
				tokenValue = resBundle.getString(key);
			}
			return tokenValue;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Parses the given content string for any container content denoted by
	 * start and end tokens with the given name; such content is deleted if the
	 * user does not qualify for it (as determined by the given
	 * ContainerMatcher). Container content is denoted by a start tag like
	 * <code>&lt;FOO:keys&gt;</code> and a corresponding end tag like
	 * <code>&lt;/FOO&gt;</code>, where <code>FOO</code> is the given token
	 * name, and <code>keys</code> may include one or more attribute values,
	 * delimited by "|" for a logical-or. The given ContainerMatcher will be
	 * passed those attribute values to determine whether the user data (ie the
	 * subject of comparison) matches any of them. Container markup may be
	 * nested for logical-and. All markup for the particular container tokens is
	 * removed from the returned content regardless.
	 * </p>
	 * <p>
	 * If you provide null or blank content or token name, then the provided
	 * content is simply returned. If you provide a null ContainerMatcher, then
	 * all sections of content enclosed by the particular container tokens are
	 * removed from the content. Likewise, if the <code>keys</code> in the
	 * parsed container start token are null or blank, then that
	 * container-enclosed section will be removed.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param tokenName
	 *            The name of the container token - eg <code>FOO</code> for
	 *            <code>&lt;FOO:<i>keys</i>&gt;</code> and
	 *            <code>&lt;/FOO&gt;</code> start and end tokens.
	 * @param matcher
	 *            The ContainerMatcher.
	 * @return The interpolated string.
	 */
	public String parseContainer(String content, String tokenName,
			ContainerMatcher matcher) {

		if (content == null || tokenName == null) {
			return content;
		}
		tokenName = tokenName.toUpperCase().trim();
		if (content.equals("") || tokenName.equals("")) {
			return content;
		}

		// Initialize global variables.
		containerIndex = 0;
		containerLevel = 0;
		newContent = "";
		oldContent = content;
		containerStartToken = "<" + tokenName + ":";
		containerEndToken = "</" + tokenName + ">";
		containerMatcher = matcher;

		// Parse for container tokens using recursive algorithm. Return the
		// parsed content.
		parseContainerRecursively(true);
		return newContent;
	}

	/**
	 * Parses the container content recursively for parseContainer method. Used
	 * to parse a level block of content.
	 * 
	 * @param isIncluded
	 *            Controls whether contained-enclosed content is to be included
	 *            in the output content or skipped.
	 */
	private void parseContainerRecursively(boolean isIncluded) {

		int result;

		/*
		 * Get each next container token in turn, until end of content. This has
		 * the side -effect of buffering the content to display, as appropriate,
		 * into the newContent global variable. If a start-container token is
		 * found, recurse a level (setting the display control variable based on
		 * whether the container match showed the user was qualified for the
		 * container or not). If an end-container token is found, and we have
		 * recursed at least a level, return - it means we are done parsing this
		 * recursion's job.
		 */
		while ((result = nextContainer((containerLevel == 0) || isIncluded)) != FOUND_END) {
			if (result == FOUND_CONTAINER_START_MATCH) {
				containerLevel++;
				parseContainerRecursively(isIncluded && true);
			} else if (result == FOUND_CONTAINER_START_NOMATCH) {
				containerLevel++;
				parseContainerRecursively(isIncluded && false);
			} else if ((result == FOUND_CONTAINER_END) && (containerLevel > 0)) {
				containerLevel--;
				break;
			}
		}
		return;
	}

	/**
	 * Parses the next container token (either a start or end token) from the
	 * content and returns a result code. The possible codes are: FOUND_END (end
	 * of content), FOUND_CONTAINER_END (found end token),
	 * FOUND_CONTAINER_START_MATCH (found start token and user is qualified to
	 * get the content - ie the ContainerMatch was successful), or
	 * FOUND_CONTAINER_END_MATCH (found end token and user is not qualified - ie
	 * the ContainerMatch failed). This method also has the side-effect of
	 * accumulating the parsed content into the newContent global variable.
	 * 
	 * @param isIncluded
	 *            Controls whether contained-enclosed content is to be included
	 *            in the output content or skipped.
	 */
	private int nextContainer(boolean isIncluded) {

		int j, k;
		String containerKeyString;
		String[] containerKeys;

		if (containerIndex >= oldContent.length()) {
			return FOUND_END;
		}

		/*
		 * Parse for start and end tokens. Use whichever comes first and ignore
		 * the other until a later invokation of this method.
		 */
		j = oldContent.indexOf(containerStartToken, containerIndex);
		k = oldContent.indexOf(containerEndToken, containerIndex);

		/*
		 * If start token found first, parse out the keys string and test the
		 * keys. Only logical-or is now supported in the keys string -
		 * logical-and is not supported (but usually can be accomplished by
		 * nesting container tokens). Return the proper result code depending on
		 * whether the test passed or not. Note: even if the test passed, it can
		 * still be reverted to no-include if an outer container token failed,
		 * or parseContainer (the top-level method) called with the no-include
		 * flag.
		 */
		if ((j != -1) && ((j < k) || (k == -1))) {
			/*
			 * Display all the content, if allowed by outer, up until the new
			 * start tag.
			 */
			if (isIncluded) {
				newContent += oldContent.substring(containerIndex, j);
			}
			/*
			 * Find the ending ">" of the container start tag - if not found,
			 * finish, ignore the rest of the content.
			 */
			k = oldContent.indexOf(">", j);
			if (k == -1) {
				containerIndex = oldContent.length();
				return FOUND_END;
			}
			containerKeyString = oldContent.substring(j
					+ containerStartToken.length(), k);
			containerKeyString = containerKeyString.trim();
			if ((containerKeyString.length() > 0) && (containerMatcher != null)) {
				containerKeys = getContainerKeys(containerKeyString);
				for (int i = 0; i < containerKeys.length; i++) {
					isIncluded = isIncluded
							&& containerMatcher.match(containerKeys[i]);
				}
			} else {
				isIncluded = isIncluded && false;
			}
			containerIndex = k + 1;
			return isIncluded ? FOUND_CONTAINER_START_MATCH
					: FOUND_CONTAINER_START_NOMATCH;
		}

		/*
		 * If end token was found first, display all the rest of the content, up
		 * to the end token, if allowed, and finish. The rest of the content
		 * will be parsed by the caller in the recursion.
		 */
		if ((k != -1) && ((k < j) || (j == -1))) {
			if (isIncluded) {
				newContent += oldContent.substring(containerIndex, k);
			}
			containerIndex = k + containerEndToken.length();
			return FOUND_CONTAINER_END;
		}

		/*
		 * Otherwise we hit the end of the content. Display everything up to the
		 * end of the content, if allowed.
		 */
		if (isIncluded) {
			newContent += oldContent.substring(containerIndex);
		}
		containerIndex = oldContent.length();
		return FOUND_END;
	}

	/**
	 * Get container keys by splitting on "|" character.
	 */
	private String[] getContainerKeys(String content) {
		String containerKeys[] = null;
		containerKeys = content.split("\\" + TOKEN_CONTAINER_OR);
		return containerKeys;
	}

}
