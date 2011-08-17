package com.hp.spp.common.util.format;
/**
 *
 * Class : Entities.java handles ASCII to HTML entities translation
 * <p>
 * DESCRIPTION
 * <p>
 * HISTORY
 * <ul>
 * <li> Creation. 13 nov. 02 </li>
 * <li> last modification 13 nov. 02 </li>
 * </ul>
 * </p>
 * @author		fmillevi (franck.milleville@cgey.com)
 * @since		JDK 1.2.2
 * @see		class#function
 * @version	0.1
 * 
 */
//standard include
import java.util.*;
//
public class Entities
{
	static final Hashtable decoder= new Hashtable(300);
	static final Hashtable equival= new Hashtable(300);
	static final String[] encoder= new String[0x100];
	static final String decode(String entity)
	{
		if (entity.charAt(entity.length() - 1) == ';')
			// remove trailing semicolon
			entity= entity.substring(0, entity.length() - 1);
		if (entity.charAt(1) == '#')
		{
			int start= 2;
			int radix= 10;
			if (entity.charAt(2) == 'X' || entity.charAt(2) == 'x')
			{
				start++;
				radix= 16;
			}
			Character c= new Character((char) Integer.parseInt(entity.substring(start), radix));
			return c.toString();
		}
		else
		{
			String s= (String) decoder.get(entity);
			if (s != null)
				return s;
			else
				return "";
		}
	}

	/**
	 * Method encode.
	 * @author fmillevi
	 * @param s string to be conver
	 * @return String converted string with stressing
	 */
	public final static String encode4Javascript(String s)
	{
		int length= s.length();
		StringBuffer buffer= new StringBuffer();
		String entity;
		int entityLength;
		for (int i= 0; i < length; i++)
		{
			char c= s.charAt(i);
			int j= (int) c;
			// Check if it's a known entity
			if (c == '&')
			{
				entityLength= s.substring(i).indexOf(";");
				if (entityLength > 0 && s.charAt(i+1) != ' ')
					// it seems to be an entity 
				{
					entity= s.substring(i).substring(0, entityLength);
					if (entity.charAt(1) == '#')
					{
						j= new Integer(entity.substring(2)).intValue();
						//if (j < 0x100)
						{
							buffer.append((char)j);
						}
						//else
						//{
						//	buffer.append(entity);
						//	buffer.append(';');
						//}
					}
					else
					{
						if (decoder.containsKey(entity))
						{
							buffer.append((String) decoder.get(entity));
						}
						else
						{
							buffer.append(entity);
							buffer.append(';');
						}
					}

					i += entityLength;
					continue;
				}
				else
				{
					buffer.append(c);
				}
			}
			else if (c == '\n' || c == '\r')
			{
				buffer.append(' ');
			}
			else 
			{
				buffer.append(c);
			}
		}
		return Javascript.encode( buffer.toString() );
	}
	/**
	 * Method encode.
	 * @author fmillevi
	 * @param s string to be conver
	 * @return String converted string with stressing
	 */
	static final public String encode(String s)
	{
		return encode(s, false);
	}
	/**
	 * Method encode.
	 * @author fmillevi
	 * @param s string to be conver
	 * @param stressingLess true when the stressing not be take into account
	 * @return String converted string
	 */
	static final public String encode(String s, boolean stressingLess)
	{
		int length= s.length();
		StringBuffer buffer= new StringBuffer();
		String entity;
		int entityLength;
		for (int i= 0; i < length; i++)
		{
			char c= s.charAt(i);
			int j= (int) c;
			// Check if it's a known entity
			if (c == '&')
			{
				entityLength= s.substring(i).indexOf(";");
				if (entityLength > 0)
					// it seems to be an entity 
				{
					entity= s.substring(i).substring(0, entityLength);
					// it's a known entity
					if (stressingLess)
					{
						// check if stressingLess conversion 
						//if (equival.get(s.substring(i, entityLength)) != null)
						if (equival.containsKey(entity))
						{
							// append content and go farther
							buffer.append(equival.get(entity));
							i += entityLength;
							continue;
						}
						else
						{
							if (entity.charAt(1) == '#')
							{
								j= new Integer(entity.substring(2)).intValue();
								if (j < 0x100)
								{

									if (encoder[j]!="" && encoder[j]!=null && equival.containsKey(encoder[j]))
										// it's a known entity
									{
										// append content and go farther
										buffer.append((String) equival.get(encoder[j]));
										i += entityLength;
										continue;
									}
								}
							}
						}
						buffer.append(entity);
						buffer.append(';');
						i += entityLength;
						continue;
					}
					else
					{
						buffer.append(entity);
						buffer.append(';');
						i += entityLength;
						continue;
					}
				}
			}
			if (j < 0x100 && encoder[j] != null)
			{
				if (stressingLess)
				{
					// check if stressingLess conversion 
					if (equival.containsKey(encoder[j]))
					{
						buffer.append(equival.get(encoder[j]));
						continue;
					}
				}
				buffer.append(encoder[j]); // have a named encoding
				buffer.append(';');
			}
			else if (j < 0x80)
			{
				buffer.append(c); // use ASCII value
			}
			else
			{
				buffer.append("&#"); // use numeric encoding
				buffer.append((int) c);
				buffer.append(';');
			}
		}
		return buffer.toString();
	}

	static final void add(String entity, int value)
	{
		decoder.put(entity, (new Character((char) value)).toString());
		if (value < 0x100)
			encoder[value]= entity;
	}

	static final void add(String entity, int value, String equivalent)
	{
		equival.put(entity, equivalent);
		decoder.put(entity, (new Character((char) value)).toString());
		if (value < 0x100)
			encoder[value]= entity;
	}

	static {
		add("&nbsp", 160, " ");
		add("&iexcl", 161);
		add("&cent", 162);
		add("&pound", 163);
		add("&curren", 164);
		add("&yen", 165);
		add("&brvbar", 166);
		add("&sect", 167);
		add("&uml", 168);
		add("&copy", 169);
		add("&ordf", 170);
		add("&laquo", 171);
		add("&not", 172);
		add("&shy", 173);
		add("&reg", 174);
		add("&macr", 175);
		add("&deg", 176);
		add("&plusmn", 177);
		add("&sup2", 178);
		add("&sup3", 179);
		add("&acute", 180);
		add("&micro", 181);
		add("&para", 182);
		add("&middot", 183);
		add("&cedil", 184);
		add("&sup1", 185);
		add("&ordm", 186);
		add("&raquo", 187);
		add("&frac14", 188);
		add("&frac12", 189);
		add("&frac34", 190);
		add("&iquest", 191);
		add("&Agrave", 192, "A");
		add("&Aacute", 193, "A");
		add("&Acirc", 194, "A");
		add("&Atilde", 195, "A");
		add("&Auml", 196, "A");
		add("&Aring", 197, "A");
		add("&AElig", 198, "AE");
		add("&Ccedil", 199, "C");
		add("&Egrave", 200, "E");
		add("&Eacute", 201, "E");
		add("&Ecirc", 202, "E");
		add("&Euml", 203, "E");
		add("&Igrave", 204, "I");
		add("&Iacute", 205, "I");
		add("&Icirc", 206, "I");
		add("&Iuml", 207, "I");
		add("&ETH", 208);
		add("&Ntilde", 209, "N");
		add("&Ograve", 210, "O");
		add("&Oacute", 211, "O");
		add("&Ocirc", 212, "O");
		add("&Otilde", 213, "O");
		add("&Ouml", 214, "O");
		add("&times", 215);
		add("&Oslash", 216, "O");
		add("&Ugrave", 217, "U");
		add("&Uacute", 218, "U");
		add("&Ucirc", 219, "U");
		add("&Uuml", 220, "U");
		add("&Yacute", 221, "Y");
		add("&THORN", 222);
		add("&szlig", 223);
		add("&agrave", 224, "a");
		add("&aacute", 225, "a");
		add("&acirc", 226, "a");
		add("&atilde", 227, "a");
		add("&auml", 228, "a");
		add("&aring", 229, "a");
		add("&aelig", 230, "ae");
		add("&ccedil", 231, "c");
		add("&egrave", 232, "e");
		add("&eacute", 233, "e");
		add("&ecirc", 234, "e");
		add("&euml", 235, "e");
		add("&igrave", 236, "i");
		add("&iacute", 237, "i");
		add("&icirc", 238, "i");
		add("&iuml", 239, "i");
		add("&eth", 240);
		add("&ntilde", 241, "n");
		add("&ograve", 242, "o");
		add("&oacute", 243, "o");
		add("&ocirc", 244, "o");
		add("&otilde", 245, "o");
		add("&ouml", 246, "o");
		add("&divide", 247);
		add("&oslash", 248);
		add("&ugrave", 249, "u");
		add("&uacute", 250, "u");
		add("&ucirc", 251, "u");
		add("&uuml", 252);
		add("&yacute", 253, "y");
		add("&thorn", 254);
		add("&yuml", 255, "y");
		add("&fnof", 402);
		add("&Alpha", 913);
		add("&Beta", 914);
		add("&Gamma", 915);
		add("&Delta", 916);
		add("&Epsilon", 917);
		add("&Zeta", 918);
		add("&Eta", 919);
		add("&Theta", 920);
		add("&Iota", 921);
		add("&Kappa", 922);
		add("&Lambda", 923);
		add("&Mu", 924);
		add("&Nu", 925);
		add("&Xi", 926);
		add("&Omicron", 927);
		add("&Pi", 928);
		add("&Rho", 929);
		add("&Sigma", 931);
		add("&Tau", 932);
		add("&Upsilon", 933);
		add("&Phi", 934);
		add("&Chi", 935);
		add("&Psi", 936);
		add("&Omega", 937);
		add("&alpha", 945);
		add("&beta", 946);
		add("&gamma", 947);
		add("&delta", 948);
		add("&epsilon", 949);
		add("&zeta", 950);
		add("&eta", 951);
		add("&theta", 952);
		add("&iota", 953);
		add("&kappa", 954);
		add("&lambda", 955);
		add("&mu", 956);
		add("&nu", 957);
		add("&xi", 958);
		add("&omicron", 959);
		add("&pi", 960);
		add("&rho", 961);
		add("&sigmaf", 962);
		add("&sigma", 963);
		add("&tau", 964);
		add("&upsilon", 965);
		add("&phi", 966);
		add("&chi", 967);
		add("&psi", 968);
		add("&omega", 969);
		add("&thetasym", 977);
		add("&upsih", 978);
		add("&piv", 982);
		add("&bull", 8226);
		add("&hellip", 8230);
		add("&prime", 8242);
		add("&Prime", 8243);
		add("&oline", 8254);
		add("&frasl", 8260);
		add("&weierp", 8472);
		add("&image", 8465);
		add("&real", 8476);
		add("&trade", 8482);
		add("&alefsym", 8501);
		add("&larr", 8592);
		add("&uarr", 8593);
		add("&rarr", 8594);
		add("&darr", 8595);
		add("&harr", 8596);
		add("&crarr", 8629);
		add("&lArr", 8656);
		add("&uArr", 8657);
		add("&rArr", 8658);
		add("&dArr", 8659);
		add("&hArr", 8660);
		add("&forall", 8704);
		add("&part", 8706);
		add("&exist", 8707);
		add("&empty", 8709);
		add("&nabla", 8711);
		add("&isin", 8712);
		add("&notin", 8713);
		add("&ni", 8715);
		add("&prod", 8719);
		add("&sum", 8721);
		add("&minus", 8722);
		add("&lowast", 8727);
		add("&radic", 8730);
		add("&prop", 8733);
		add("&infin", 8734);
		add("&ang", 8736);
		add("&and", 8743);
		add("&or", 8744);
		add("&cap", 8745);
		add("&cup", 8746);
		add("&int", 8747);
		add("&there4", 8756);
		add("&sim", 8764);
		add("&cong", 8773);
		add("&asymp", 8776);
		add("&ne", 8800);
		add("&equiv", 8801);
		add("&le", 8804);
		add("&ge", 8805);
		add("&sub", 8834);
		add("&sup", 8835);
		add("&nsub", 8836);
		add("&sube", 8838);
		add("&supe", 8839);
		add("&oplus", 8853);
		add("&otimes", 8855);
		add("&perp", 8869);
		add("&sdot", 8901);
		add("&lceil", 8968);
		add("&rceil", 8969);
		add("&lfloor", 8970);
		add("&rfloor", 8971);
		add("&lang", 9001);
		add("&rang", 9002);
		add("&loz", 9674);
		add("&spades", 9824);
		add("&clubs", 9827);
		add("&hearts", 9829);
		add("&diams", 9830);
		add("&quot", 34);
		add("&amp", 38);
		add("&lt", 60);
		add("&gt", 62);
		add("&OElig", 338, "OE");
		add("&oelig", 339, "oe");
		add("&Scaron", 352, "S");
		add("&scaron", 353, "s");
		add("&Yuml", 376, "Y");
		add("&circ", 710);
		add("&tilde", 732);
		add("&ensp", 8194);
		add("&emsp", 8195);
		add("&thinsp", 8201);
		add("&zwnj", 8204);
		add("&zwj", 8205);
		add("&lrm", 8206);
		add("&rlm", 8207);
		add("&ndash", 8211);
		add("&mdash", 8212);
		add("&lsquo", 8216);
		add("&rsquo", 8217);
		add("&sbquo", 8218);
		add("&ldquo", 8220);
		add("&rdquo", 8221);
		add("&bdquo", 8222);
		add("&dagger", 8224);
		add("&Dagger", 8225);
		add("&permil", 8240);
		add("&lsaquo", 8249);
		add("&rsaquo", 8250);
		add("&euro", 8364);
	}
}