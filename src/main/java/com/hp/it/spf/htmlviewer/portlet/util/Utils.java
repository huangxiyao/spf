/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Container class for common utility methods used within the HTMLViewer
 * portlet.
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 */
public class Utils {

	private static String TOKEN_HREF_START = "<a";
	private static String TOKEN_HREF_END = "</a>";

	protected Utils() {
	}

	/**
	 * Add onclick="..." to HREF and open new browser without button at some
	 * time
	 * 
	 * @param content
	 *            old HTML content
	 * 
	 * @param onclickString
	 *            like onclick="callButtonlessWindow_<portletnamespace>(this);return
	 *            false;"
	 * 
	 * @return String new content after parsing
	 */
	public static String addButtonLess(String content, String onclickString) {

		String regEx = "(" + TOKEN_HREF_START + ".*?" + TOKEN_HREF_END + ")";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(content);

		while (m.find()) {
			String s = m.group();// found string
			String slow = s.toLowerCase();

			// Add onclick when href tag exists and onclick does not exist
			if (slow.indexOf("href") > -1 && slow.indexOf("onclick") == -1) {
				String news = s.substring(2);
				news = TOKEN_HREF_START + " " + onclickString + " " + news;
				int starti = content.indexOf(s);
				int endi = starti + s.length();
				String a = content.substring(0, starti);
				String b = content.substring(endi);
				content = a + news + b;
			}
		}

		return content;
	}

	/**
	 * Add class="..." to HREF and make link look according to normal style
	 * 
	 * @param content
	 *            old HTML content
	 * 
	 * @param classString
	 *            like class="fs-linkout-link-color"
	 * 
	 * @return String new content after parsing
	 */
	public static String addCssStyle(String content, String classString) {

		String regEx = "(" + TOKEN_HREF_START + ".*?" + TOKEN_HREF_END + ")";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(content);

		while (m.find()) {
			String s = m.group();// found string
			String slow = s.toLowerCase();

			// Add class when href tag exists and class does not exist
			if (slow.indexOf("href") > -1 && slow.indexOf("class") == -1) {
				String news = s.substring(2);
				news = TOKEN_HREF_START + " " + classString + " " + news;
				int starti = content.indexOf(s);
				int endi = starti + s.length();
				String a = content.substring(0, starti);
				String b = content.substring(endi);
				content = a + news + b;
			}
		}

		return content;
	}
}
