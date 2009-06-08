package com.hp.it.spf.xa.wsrp;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class DefaultValueEncoder implements ValueEncoder
{
	public StringBuffer appendEncoded(StringBuffer buf, String stringToEncode)
	{
		if (stringToEncode != null && stringToEncode.length() > 0) {
			for (int i = 0, len = stringToEncode.length(); i < len; ++i) {
				char c = stringToEncode.charAt(i);
				if (isDelimiter(c)) {
					buf.append('\\').append(c);
				} else {
					buf.append(c);
				}
			}
		}

		return buf;
	}

	protected boolean isDelimiter(char c)
	{
		return c == '[' || c == ']' || c == '{' || c == '}' || c == ',' || c == '=';
	}

	public String decodeFragment(String s, int startPosition, int endPosition)
	{
		if (s == null || s.length() == 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer(endPosition - startPosition);
		for (int i = startPosition; i < endPosition; ++i) {
			char c = s.charAt(i);
			if (i + 1 < endPosition && c == '\\') {
				char c2 = s.charAt(i + 1);
				if (isDelimiter(c2)) {
					// do nothing, skip '\' character
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
