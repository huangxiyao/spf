package com.hp.it.spf.xa.wsrp;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
interface ValueEncoder
{
	StringBuffer appendEncoded(StringBuffer buf, String stringToEncode);

	String decodeFragment(String s, int startPosition, int endPosition);
}
