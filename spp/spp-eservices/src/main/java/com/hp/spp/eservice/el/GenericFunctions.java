package com.hp.spp.eservice.el;

import org.apache.commons.lang.StringUtils;

/**
 * @author ageymond
 * 
 * This class implement all JSTL fucntions availabale at the url :
 * http://java.sun.com/products/jsp/jstl/1.1/docs/tlddocs/fn/tld-summary.html
 */
public class GenericFunctions {

	public static boolean contains(String a, String b) {
		if (a.indexOf(b) > -1)
			return true;
		else
			return false;
	}

	public static boolean containsIgnoreCase(String a, String b) {
		return contains(a.toUpperCase(), b.toUpperCase());
	}

	public static String escapeXml(String a) {

		a = StringUtils.replace(a, "&", "&amp;");
		a = StringUtils.replace(a, "<", "&lt;");
		a = StringUtils.replace(a, ">", "&gt;");
		a = StringUtils.replace(a, "\"", "&quot;");
		a = StringUtils.replace(a, "'", "&apos;");
		return a;
	}

	public static String join(String[] a, String b) {
		// ${fn:join(array, ";")}
		StringBuffer joinResult = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			joinResult.append(a[i]);
			if (i < a.length - 1)
				joinResult.append(';');
		}
		return joinResult.toString();
	}

	public static int length(String a) {
		return a.length();
	}

	public static String replace(String a, String b, String c) {
		return a.replaceAll(b, c);
	}

	public static String[] split(String a, String b) {
		return a.split(b);
	}

	public static boolean startsWith(String a, String b) {
		return a.startsWith(b);
	}

	/**
	 * This method exists because it was originally spelt like this,
	 * though the official JSTL function is 'startsWith'. This method
	 * will remain so that any expressions written with the 'startWith'
	 * spelling continue to work.
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean startWith(String a, String b) {
		return startsWith(a, b);
	}

	/**
	 * This method exists because it was originally spelt like this,
	 * though the official JSTL function is 'startsWith'. This method
	 * will remain so that any expressions written with the 'startWith'
	 * spelling continue to work.
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static String subString(String a, int b, int c) {
		return substring(a, b, c);
	}

	public static String substring(String a, int b, int c) {
		return a.substring(b, c);
	}

	public static String substringAfter(String a, String b) {
		return a.substring(a.indexOf(b)+b.length(),a.length());
	}

	public static String substringBefore(String a, String b) {
		return a.substring(0,a.indexOf(b));
	}

	public static String toUpperCase(String a) {
		return a.toUpperCase();
	}

	public static String toLowerCase(String a) {
		return a.toLowerCase();
	}

	public static String trim(String a) {
		return a.trim();
	}

	public static String concat(String a, String b) {
		return a + b;
	}

}
