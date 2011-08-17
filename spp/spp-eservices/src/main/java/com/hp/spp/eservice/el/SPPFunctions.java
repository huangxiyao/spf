package com.hp.spp.eservice.el;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.hp.spp.config.Config;

public class SPPFunctions {
	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(SPPFunctions.class);

	private static final String PRIVATE_KEY_CONFIG_PREFIX = "SPP.privateKey.";

	public static final String SPP_DELIMITER = ";";

	public static String inGroup(String a, String b) {
		String[] groupList = b.split(";");
		if (groupList != null) {
			for (int i = 0; i < groupList.length; i++) {
				if (a.equals(groupList[i]))
					return "T";
			}
		}
		return "F";
	}

	/**
	 * Accepts a string content and signs it using a Base-64 encoded private key
	 * stored as "configKey" value in SPP's config table. The signed data is
	 * once again Base64 encoded.
	 *
	 * @param contentToSign -
	 *            the string to sign.
	 * @param siteName -
	 *            the name of the SPP site.
	 * @return Base-64 encoded string signed using private key or null if there
	 *         is an error.
	 * @see sun.misc.BASE64Decoder#encode(byte [])
	 * @see sun.misc.BASE64Decoder#decode(String)
	 */

	public static String digitalSignature(String contentToSign, String siteName) {
		try {

			if (contentToSign == null || "".equals(contentToSign)) {
				mLog.error("Content to sign is null or empty");
				return null;
			}

			if (siteName == null || "".equals(siteName)) {
				mLog.error("Sitename  is null or empty");
				return null;
			}

			String configKey = PRIVATE_KEY_CONFIG_PREFIX + siteName;
			String encodedContentStr = Config.getValue(configKey);

			/* Decode the primary key */
			byte[] content = new BASE64Decoder()
					.decodeBuffer(encodedContentStr);

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			PrivateKey privateKey = (PrivateKey) keyFactory
					.generatePrivate(keySpec);
			Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
			dsa.initSign(privateKey);
			dsa.update(contentToSign.getBytes("UTF-8"));

			/* Encode the primary key */
			String encodedData = new BASE64Encoder().encode(dsa.sign());
			return encodedData;
		} catch (Exception ex) {
			mLog.error(ex);
			return null;
		}

	}

	/**
	 * Accepts string delimited by SPP's standard delimiter - ";" and replaces
	 * it with the delimiter to replace (Note: it does not support regular
	 * expressions)
	 *
	 * again Base64 encoded.
	 *
	 * @param str -
	 *            the input string.
	 * @param delimToReplace -
	 *            the delimiter to replace, this is any valid java
	 * @return String with delimiter (;) replaced by delimToReplace
	 * @see java.lang.String#replace(String, String)
	 */

	public static String replaceDelimiter(String str, String delimToReplace) {

		if (str == null || "".equals(str)) {
			mLog.error("Target string to replace is null or empty");
			return null;
		}

		if (delimToReplace == null || "".equals(delimToReplace)) {
			mLog.error("Delimiter to replace  is null or empty");
			return null;
		}

		return str.replace(SPP_DELIMITER, delimToReplace);
	}

	
	/**
	 * This method is used as a filter for filtering the multiple value attributes
	 * value to a relevant string. It takes as input a string which represents
	 * the multiValueAttribute that has to be filtered and another string which
	 * represents the filter criteria.
	 * Each attribute in the multiple attribute value string and in the filter or
	 * pattern string must be delimited by the standard SPP delimiter character ;
	 * This function supports wild-card matching. for example, if the multiple value
	 * attributes string is <code> "SPP_USER_CONSOLE;GPP_USER_CONSOLE;SPP_SHOWALL"</code> and
	 * the pattern string is <code> "*CONSOLE"</code>, the filtered string will be
	 * <code>"SPP_USER_CONSOLE;GPP_USER_CONSOLE</code>
	 *
	 * @param multiValueString - The string value of the multi-value attribute
	 * @param patternString - The pattern or filter String which will be matched
	 * against the multiValueString to filter it.
	 *
	 *@return String: A string containing ALL concatenated matches found in the
	 * multiValueString with the patternString. Multiple values in the resultant
	 * string will be delimited by the standard SPP delimiter character ;
	 * if the patternString matches with the multiValueString
	 * or returns an empty String if no matches were found or if the input
	 * values were empty or null. This method does not return a null.
	 *
	 */
	public static String filterMultiValueAttribute (String multiValueString,
			String patternString) {

		StringBuffer matchedOrFilteredString = new StringBuffer();

		if (multiValueString == null || "".equals(multiValueString)
				|| patternString == null || "".equals(patternString)) {

			//Exit early. Could also throw IllegalArgumentExcpetion but
			//decided not to.
			return matchedOrFilteredString.toString();
		}

		String [] singleValues = multiValueString.split(SPP_DELIMITER);

		String [] singlePatternValue = patternString.split(SPP_DELIMITER);

		//Loop through patternValues first
		for(int j = 0; j < singlePatternValue.length; j++ ) {
			//Construct a compiled Patter with the singlePatternValue.
			//This is used later for checking against wild cards. This is constructed
			//here so that it does not need to be reconstructed everytime in the loop below.
			Pattern singlePatternValueRegExp = Pattern.compile(singlePatternValue[j].replaceAll("\\*", ".*?"));
			
			for (int i = 0; i < singleValues.length; i++){
				if(singlePatternValue[j].equals(singleValues[i])) {

					matchedOrFilteredString.append(singleValues[i]).append(SPP_DELIMITER);

					// Check for wildcards
				} else if (singlePatternValueRegExp.matcher(singleValues[i]).matches()) { 
					
					matchedOrFilteredString.append(singleValues[i]).append(SPP_DELIMITER);  

				}

			}
		}

		//Remove the trailing 'SPP_DELIMITER'
		if (matchedOrFilteredString.length()>0) {
			matchedOrFilteredString.deleteCharAt(matchedOrFilteredString.length() - 1);

		}

		return matchedOrFilteredString.toString();

	}


}
