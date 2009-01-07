/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.portlet.PortletResponse;

/**
 * Container class for common utility methods used within the HTMLViewer
 * portlet.
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 */
public class Utils {

	private static String TOKEN_HREF_START = "<a ";
	private static String TOKEN_HREF_END = "</a>";

	protected Utils() {
	}

	/**
	 * Add onclick="..." to HREF to open link in new browser without button.
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
	public static String addButtonlessChildLauncher(PortletResponse response,
			String content) {

		String regEx = "(" + TOKEN_HREF_START + ".*?" + TOKEN_HREF_END + ")";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(content);
		boolean mustAddJavascript = true;

		while (m.find()) {
			String s = m.group();// found string
			String slow = s.toLowerCase();

			// Add onclick when href tag exists and onclick does not exist
			if (slow.indexOf("href") > -1 && slow.indexOf("onclick") == -1) {
				String news = s.substring(3);
				news = TOKEN_HREF_START + "onclick=\""
						+ getButtonlessChildLauncherOnclick(response) + "\" "
						+ news;
				if (mustAddJavascript) {
					news += getButtonlessChildLauncherJavascript(response);
				}
				int starti = content.indexOf(s);
				int endi = starti + s.length();
				String a = content.substring(0, starti);
				String b = content.substring(endi);
				content = a + news + b;
				mustAddJavascript = false;
			}
		}

		return content;
	}

	/**
	 * Add class="..." to HREF and make link look according to desired style. No
	 * longer used.
	 * 
	 * @param content
	 *            old HTML content
	 * 
	 * @param className
	 *            name of the style, to use in the class="..." attribute
	 * 
	 * @return String new content after parsing
	 * @deprecated
	 */
	public static String addLinkStyle(String content, String className) {

		String regEx = "(" + TOKEN_HREF_START + ".*?" + TOKEN_HREF_END + ")";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(content);

		while (m.find()) {
			String s = m.group();// found string
			String slow = s.toLowerCase();

			// Add class when href tag exists and class does not exist
			if (slow.indexOf("href") > -1 && slow.indexOf("class") == -1) {
				String news = s.substring(3);
				news = TOKEN_HREF_START + " class=\"" + className + "\" "
						+ news;
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
	 * Return the <code>onclick</code> event handler string for a link which
	 * is to be launched as a buttonless child window.
	 * 
	 * @param response
	 * @return the <code>onclick</code> event handler string - for example,
	 *         <code>callButtonlessWindow_<i>&lt;portlet-namespace&gt;</i>(this);return false;</code>
	 */
	private static String getButtonlessChildLauncherOnclick(
			PortletResponse response) {
		return "callButtonlessWindow_" + response.getNamespace()
				+ "(this);return false;";
	}

	/**
	 * Return the <code>script</code> tag and content for a Javascript
	 * function which launches a buttonless child window. For example (linefeeds
	 * added for readability:
	 * 
	 * <pre><code>
	 * &lt;script type=&quot;text/javascript&quot; language=&quot;javascript&quot; charset=&quot;utf-8&quot;&gt;
	 *   function callButtonlessWindow_&lt;portlet-namespace&gt;(ev) {
	 *     window.open (ev,'LinkoutWindow','top=0,left=0,...');
	 *   }
	 * &lt;/script&gt;
	 * </code></pre>
	 * 
	 * @param response
	 * @return the <code>script</code> string
	 */
	private static String getButtonlessChildLauncherJavascript(
			PortletResponse response) {

		String javascript = "";
		javascript += "<script type=\"text/javascript\" language=\"javascript\" charset=\"utf-8\">";
		javascript += "function callButtonlessWindow_"
				+ response.getNamespace() + "(ev) { ";
		javascript += "window.open (ev,'LinkoutWindow','top=0,left=0,location=no,toolbar=no,menubar=no,status=no,resizable=yes,scrollbars=yes');";
		javascript += " }</script>";
		return javascript;

	}
}
