package com.hp.it.spf.xa.wsrp;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class UnicodeEscapeValueEncoder extends DefaultValueEncoder
{
	@Override
	public StringBuffer appendEncoded(StringBuffer buf, String stringToEncode)
	{
		if (stringToEncode != null && stringToEncode.length() > 0) {
			for (int i = 0, len = stringToEncode.length(); i < len; ++i) {
				char ch = stringToEncode.charAt(i);
				if (ch < 127) {
					if (isDelimiter(ch) || ch == '\\') {
						buf.append('\\').append(ch);
					}
					else {
						buf.append(ch);
					}
				}
				else {
					buf.append('\\');
					buf.append('u');
					buf.append(toHex((ch >> 12) & 0xF));
					buf.append(toHex((ch >>  8) & 0xF));
					buf.append(toHex((ch >>  4) & 0xF));
					buf.append(toHex( ch        & 0xF)); 					
				}
			}
		}
		return buf;
	}

	@Override
	public String decodeFragment(String s, int startPosition, int endPosition)
	{
		if (s == null || s.length() == 0) {
			return s;
		}
		StringBuffer buf = new StringBuffer(endPosition - startPosition);
		int off = startPosition;
		while (off < endPosition) {
			char ch = s.charAt(off++);
			if (ch == '\\') {
				ch = s.charAt(off++);
				if (ch == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						ch = s.charAt(off++);
						switch (ch) {
							case '0': case '1': case '2': case '3': case '4':
							case '5': case '6': case '7': case '8': case '9':
								value = (value << 4) + ch - '0';
								break;
							case 'a': case 'b': case 'c':
							case 'd': case 'e': case 'f':
								value = (value << 4) + 10 + ch - 'a';
								break;
							case 'A': case 'B': case 'C':
							case 'D': case 'E': case 'F':
								value = (value << 4) + 10 + ch - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed \\uxxxx encoding: " + s.substring(startPosition, endPosition));
						}
					}
					buf.append((char) value);
				}
				else {
					// we skipped '\' as its first occurrence is an escape
					buf.append(ch);
				}
			}
			else {
				buf.append(ch);
			}
		}

		return buf.toString();
	}


	/**
	 * Convert a nibble to a hex character
	 *
	 * @param	nibble	the nibble to convert.
	 * @return hexadecimal digit conrresponding to the nibble
	 */
	private static char toHex(int nibble)
	{
		return HEX_DIGIT[(nibble & 0xF)];
	}

	/**
	 * A table of hex digits
	 */
	private static final char[] HEX_DIGIT = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};
}
