/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.wsrp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Class with several helper methods working on the SPF profile representation.
 *
 * This class contains methods that can serialize and deserialize the profile
 * map into and from String object. The grammar is as follows: <code>
 * Profile: Map
 * Map: "{" (AttributeName "=" AttributeValue ", ")* "}"
 * AttributeName: String
 * AttributeValue: String | Map | List
 * List: "[" (AttributeValue ", ")* "]
 * </code>
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version 1.0
 */
public class ProfileHelper {


	/**
	 * Returns a map that contains only the profile entries for which the values
	 * are represented as strings. The entries that are Maps or Lists are not
	 * copied from <tt>profile</tt> map.
	 *
	 * @param profile
	 *            profile map to copy data from
	 * @return map containing only entries with String values.
	 */
	public Map toLegacyProfile(Map profile) {
		if (profile == null) {
			return null;
		}
		Map result = new HashMap();
		for (Iterator it = profile.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue() instanceof String) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	/**
	 * Returns a map that contains only the profile entries for which the values
	 * are represented as strings. The entries that are Maps or Lists are not
	 * copied from <tt>profile</tt> map.
	 *
	 * @param profile
	 *            profile map to copy data from
	 * @return map containing only entries with String values.
	 * @throws ParserConfigurationException
	 */
	public Document profileToLegacyXml(Map profile) throws ParserConfigurationException {
		if (profile == null) {
			return null;
		}
		Map legacyProfile = toLegacyProfile(profile);
		Document document = null;

		// New Document creation
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructor = factory.newDocumentBuilder();

		document = constructor.newDocument();

		Element root = document.createElement("user");
		Iterator it = legacyProfile.keySet().iterator();

		while (it.hasNext()) {
			String paramName = (String) it.next();
			Element paramNode = document.createElement("attribute");
			paramNode.setAttribute("name", paramName);
			String paramMapValue = (String) legacyProfile.get(paramName);
			if (paramMapValue.indexOf(";") != 0) {
				String[] insideParamMap = paramMapValue.split(";");

				for (int i = 0; i < insideParamMap.length; i++) {
					if (!insideParamMap[i].equals(null)
							&& !insideParamMap[i].trim().equalsIgnoreCase("")) {
						Element paramNodeValue = document.createElement("value");
						Node paramValue = document.createTextNode(insideParamMap[i]);
						paramNodeValue.appendChild(paramValue);
						paramNode.appendChild(paramNodeValue);
					}
				}

			} else {
				Element paramNodeValue = document.createElement("value");
				Node paramValue = document.createTextNode(paramMapValue);
				paramNodeValue.appendChild(paramValue);
				paramNode.appendChild(paramNodeValue);
			}

			root.appendChild(paramNode);
		}
		document.appendChild(root);
		return document;
	}


	/**
	 * Serializes the profile map to String using "{" and "}" separators for
	 * Maps and "[" and "]" separators for lists.
	 *
	 * @param profile
	 *            profile map
	 * @return profile map String representation
	 */
	public String profileToString(Map profile) {
		StringBuffer sb = new StringBuffer();
		writeMap(sb, profile);
		return sb.toString();
	}

	/**
	 * Reads profile map from a String representation using "{" and "}"
	 * separators for Maps and "[" and "]" separators for lists. The separator
	 * characters are properly un-escaped.
	 *
	 * @param s
	 *            string represnetation of the profile
	 * @return deserialized from string profile map
	 * @throws IllegalArgumentException
	 *             If the string <tt>s</tt> has incorrect format
	 */
	public Map profileFromString(String s) {
		Stack stack = new Stack();
		readMap(stack, s, 0);
		return (Map) stack.pop();
	}

	void writeObject(StringBuffer sb, Object obj) {
		if (obj == null) {
			sb.append("");
		} else if (obj instanceof Map) {
			writeMap(sb, (Map) obj);
		} else if (obj instanceof List) {
			writeList(sb, (List) obj);
		} else {
			escape(sb, String.valueOf(obj));
		}
	}

	private void writeMap(StringBuffer sb, Map map) {
		sb.append('{');
		if (map != null && !map.isEmpty()) {
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				sb.append(entry.getKey()).append('=');
				writeObject(sb, entry.getValue());
				if (it.hasNext()) {
					sb.append(", ");
				}
			}
		}
		sb.append('}');
	}

	private void writeList(StringBuffer sb, List lst) {
		sb.append('[');
		if (lst != null && !lst.isEmpty()) {
			for (Iterator it = lst.iterator(); it.hasNext();) {
				writeObject(sb, it.next());
				if (it.hasNext()) {
					sb.append(", ");
				}
			}
		}
		sb.append(']');
	}

	private void escape(StringBuffer sb, String s) {
		if (s == null || s.length() == 0) {
			return;
		}
		for (int i = 0, len = s.length(); i < len; ++i) {
			char c = s.charAt(i);
			if (c == '[' || c == ']' || c == '{' || c == '}' || c == ','
					|| c == '=') {
				sb.append('\\').append(c);
			} else {
				sb.append(c);
			}
		}
	}

	int readObject(Stack stack, String s, int pos) {
		if (s == null) {
			throw new IllegalArgumentException("String cannot be null");
		}
		if (s.length() == 0) {
			stack.push("");
			return pos;
		} else {
			char c = s.charAt(pos);
			if (c == '[') {
				return readList(stack, s, pos);
			} else if (c == '{') {
				return readMap(stack, s, pos);
			} else if (!stack.isEmpty()) {
				if (stack.peek() instanceof List) {
					// look for end of element or end of the list
					int posEnd = pos;
					while (posEnd < s.length() && !eqAt(s, posEnd, ',')
							&& !eqAt(s, posEnd, ']')) {
						posEnd++;
					}
					if (posEnd == s.length()) {
						throw new IllegalArgumentException(
								"Error parsing list - missing closing ']'");
					}
					stack.push(unescape(s, pos, posEnd));
					return posEnd;
				} else if (stack.peek() instanceof Map) {
					// look for end of entry or end of the map
					int posEnd = pos;
					while (posEnd < s.length() && !eqAt(s, posEnd, ',')
							&& !eqAt(s, posEnd, '}')) {
						posEnd++;
					}
					if (posEnd == s.length()) {
						throw new IllegalArgumentException(
								"Error parsing map - missing closing '}'");
					}
					stack.push(unescape(s, pos, posEnd));
					return posEnd;
				}
			} else {
				stack.push(unescape(s, pos, s.length()));
				return s.length();
			}
		}
		throw new IllegalStateException("How do we get here?");
	}

	private int readMap(Stack stack, String s, int pos) {
		if (s.charAt(pos) != '{') {
			throw new IllegalArgumentException("Error parsing map: "
					+ s.substring(pos));
		}
		Map map = new HashMap();
		stack.push(map);
		// skip '{'
		pos += 1;
		while (pos < s.length() && !eqAt(s, pos, '}')) {
			pos = readKey(stack, s, pos);
			String name = (String) stack.pop();
			// skip '='
			pos += 1;
			pos = readObject(stack, s, pos);
			Object value = stack.pop();
			map.put(name, value);
			// skip ", "
			if (pos + 2 < s.length() && eqAt(s, pos, ',')
					&& s.charAt(pos + 1) == ' ') {
				pos += 2;
			}
		}
		if (pos >= s.length()) {
			throw new IllegalArgumentException("Error parsing list");
		}
		// skip '}'
		pos += 1;
		return pos;
	}

	private int readKey(Stack stack, String s, int pos) {
		int posEnd = pos;
		while (posEnd < s.length() && !eqAt(s, posEnd, '=')) {
			posEnd++;
		}
		if (posEnd == s.length()) {
			throw new IllegalArgumentException("Error parsing map key: "
					+ s.substring(pos));
		}
		stack.push(s.substring(pos, posEnd));
		return posEnd;
	}

	private int readList(Stack stack, String s, int pos) {
		if (s.charAt(pos) != '[') {
			throw new IllegalArgumentException("Error parsing list: "
					+ s.substring(pos));
		}
		List list = new ArrayList();
		stack.push(list);
		// skip '['
		pos += 1;
		while (pos < s.length() && !eqAt(s, pos, ']')) {
			pos = readObject(stack, s, pos);
			list.add(stack.pop());
			// skip ", "
			if (pos + 2 < s.length() && eqAt(s, pos, ',')
					&& s.charAt(pos + 1) == ' ') {
				pos += 2;
				if (eqAt(s, pos, ']')) {
					list.add("");
				}
			}
		}
		if (pos >= s.length()) {
			throw new IllegalArgumentException("Error parsing list");
		}
		// skip ']'
		pos += 1;
		return pos;
	}

	private String unescape(String s, int startPos, int endPos) {
		if (s == null || s.length() == 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer(endPos - startPos);
		for (int i = startPos; i < endPos; ++i) {
			char c = s.charAt(i);
			if (i + 1 < endPos && c == '\\') {
				char c2 = s.charAt(i + 1);
				if (c2 == '{' || c2 == '}' || c2 == '[' || c2 == ']'
						|| c2 == ',' || c2 == '=') {
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

	private boolean eqAt(String s, int pos, char c) {
		return s.charAt(pos) == c && (pos == 0 || s.charAt(pos - 1) != '\\');
	}
}
