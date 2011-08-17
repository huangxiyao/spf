package com.hp.spp.common.util.format;

import java.util.Hashtable;

/**
 *
 * Class : Javascript.java This class provides with static method converting
 * an input string into the string to be writen into an javascript command.
 * <br>
 * It replace ' and " by \' and \" in order to avoid any javascript error on 
 * client browser side
 * <p>
 * DESCRIPTION
 * <p>
 * HISTORY
 * <ul>
 * <li> Creation. XX/XX/XXXX </li>
 * <li> last modification 11 déc. 02 </li>
 * </ul>
 * </p>
 * @author		fmillevi franck.milleville@cgey.com
 * @since		JDK 1.2.2
 * @see		class#function
 * @version	0.1
 * 
 */
public class Javascript
{
	static final Hashtable convert= new Hashtable(300);
	/**
	 * Method encode. This method replaces ' and " characters by \' and \"
	 * the ' charactere can be see as ' or &#146; or &#8216 (Microsoft one)
	 * @param str
	 * @return String
	 */
	static final public String encode(String s)
	{
		int length= s.length();
		StringBuffer buffer= new StringBuffer();
		String entity;
		int entityLength;
		for (int i= 0; i < length; i++)
		{
			char c= s.charAt(i);
			if (c == '\'') 
			{
				// check if the previous character is not a \
				if (i>0)
				{
					if (s.charAt( i-1 )!= '\\')
					{
						buffer.append("\\'");
					}
					else
					{
						buffer.append(c);
					}
				}
				else
				{
					buffer.append("\\'");
				}
			}
			else if (c == '"') 
			{
				// check if the previous character is not a \
				if (i>0)
				{
					if (s.charAt( i-1 )!= '\\')
					{
						buffer.append("\\\"");
					}
					else
					{
						buffer.append(c);
					}
				}
				else
				{
					buffer.append("\\\"");
				}
			}
			else if (c == '&')
			// Check if it's a known entity
			{
				entityLength= s.substring(i).indexOf(";");
				if (entityLength > 0)
					// it seems to be an entity 
				{
					entity= s.substring(i).substring(0, entityLength+1);
					if (Javascript.convert.containsKey(entity))
					{
						buffer.append((String) Javascript.convert.get(entity));
						i += entityLength;
					}
					else
					{
						buffer.append(entity);
						i += entityLength;
					}
				}
				else
				{
					buffer.append(c);
				}
			}
			else
			{
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
	static final void add(String entity, String equivalent)
	{
		//Javascript.convert.put(entity, "AAAA");
		Javascript.convert.put(entity, equivalent);
	}
	static {
		add("&#39;", "\\'");
		add("&#146;", "\\'");
		add("&#8216;", "\\'");
		add("&#8217;", "\\'");
		add("&#8218;", "\\'");
		add("&quot;", "\\\"");
		add("&#34;", "\\\"");
		add("&#8220;", "\\\"");
		add("&#8221;", "\\\"");
		add("&#8222;", "\\\"");
	}
}
