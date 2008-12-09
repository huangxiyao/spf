/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;

/**
 * A base class for miscellaneous utility methods for both portal and portlet
 * frameworks.
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @version TBD
 * @see com.hp.it.cas.spf.portal.common.utils.Utils
 *      com.hp.it.cas.spf.portlet.common.utils.Utils
 */
public class Utils {
    /**
     * add private constructor to prevent creating instance
     */
    protected Utils() {

    }

    private static final int BUFFER_LENGTH = 50;

    /**
     * <p>
     * Convert any XML special characters in the given string, into their XML
     * character entities. The resulting string is safe to embed in surrounding
     * XML/HTML content.
     * </p>
     * 
     * @param value
     *            A string.
     * @return The escaped/converted string.
     */
    public static String escapeXml(final String value) {
        if (value == null) {
            return null;
        }
        char[] content = value.toCharArray();
        StringBuffer result = new StringBuffer(content.length + BUFFER_LENGTH);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            case '\'':
                result.append("&#39;");
                break;
            default:
                result.append(content[i]);
                break;
            }
        }
        return result.toString();
    }

    /**
     * Deprecated, not used at present.
     * 
     * get i18n from properties files
     * 
     * @param baseFile
     *            resource bundle's name
     * @param key
     *            key of the value
     * @param locale
     *            locale of the resource bundle
     * @return String
     * @throws NullPointerException
     *             NullPointerException
     * @throws ClassCastException
     *             ClassCastException
     * 
     * public static String getResourceBundlePropert(String baseFile, String
     * key, Locale locale) throws NullPointerException, ClassCastException {
     * ResourceBundle myResources = null;
     * 
     * try { if (locale != null) { myResources =
     * ResourceBundle.getBundle(baseFile, locale); } else { myResources =
     * ResourceBundle.getBundle(baseFile); } return myResources.getString(key);
     *  } catch (MissingResourceException ex) { return null; }
     *  }
     */

    /**
     * Not used at present.
     * 
     * Read Email HTML Template
     * 
     * @param url
     *            file url
     * @return string of html template
     * 
     * public static String readHtmlTempalte(URL url) { if (url == null) {
     * return null; }
     * 
     * try { InputStreamReader reader; reader = new
     * InputStreamReader(url.openStream()); CharArrayWriter writer = new
     * CharArrayWriter(); for (int c = reader.read(); c != -1; c =
     * reader.read()) { writer.write(c); }
     * 
     * reader.close(); return writer.toString(); } catch (IOException e) { //
     * TODO Auto-generated catch block e.printStackTrace(); return null; } }
     */

    /**
     * <p>
     * Decode a base-64 encoded string of UTF-8 bytecodes, returning the decoded
     * value. Note that the input string is assumed to base-64 encode a series
     * of UTF-8 bytes; if this is not the case, the returned string will be
     * corrupt.
     * </p>
     * 
     * @param in
     *            A base64-encoded string.
     * @return The decoded string.
     */
    public static String decodeBase64(String base64Codes) {
        return decodeBase64(base64Codes, "UTF-8");
    }

    /**
     * <p>
     * Decode a base-64 encoded string of bytecodes using the given encoding,
     * returning the decoded value. Returns null if the given encoding is not
     * supported.
     * </p>
     * 
     * @param in
     *            A base64-encoded string.
     * @param enc
     *            The character encoding that was used for the encoded bytes. If
     *            null, the system default encoding is assumed.
     * @return The decoded string.
     */
    public static String decodeBase64(String base64Codes, String enc) {
        if (base64Codes == null) {
            return null;
        }
        try {
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
                    ASCIIUtility.getBytes(base64Codes));
            InputStream base64Stream = new BASE64DecoderStream(
                    bytearrayinputstream);

            int k = bytearrayinputstream.available();
            byte abyte0[] = new byte[k];
            k = base64Stream.read(abyte0, 0, k);
            if (enc == null) {
                return new String(abyte0, 0, k);
            } else {
                return new String(abyte0, 0, k, enc);
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * <p>
     * Get an input stream for a file; this uses the class loader to search for
     * the given file anywhere in the current classpath. If the file cannot be
     * found in the classpath, then null is returned. Also returns null if the
     * given filename is null
     * </p>
     * 
     * @param filename
     *            The file name.
     * @return The corresponding input stream (null if not found).
     */
    public static InputStream getResourceAsStream(String resource) {
        if (resource == null) {
            return null;
        }
        resource = resource.trim();
        String stripped = resource;
        stripped.replaceAll("/+", "/");
        if (stripped.equals("") || stripped.equals("/")) {
        	return null;
        }
        if (stripped.startsWith("/")) {
        	stripped = stripped.substring(1);
        }
        InputStream stream = null;
        try {
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            if (classLoader != null) {
                stream = classLoader.getResourceAsStream(stripped);
            }
            if (stream == null) {
                stream = Utils.class.getResourceAsStream(resource);
            }
            if (stream == null) {
                stream = Utils.class.getClassLoader().getResourceAsStream(
                        stripped);
            }
        } catch (Exception e) {
            return null;
        }
        return stream;
    }
}