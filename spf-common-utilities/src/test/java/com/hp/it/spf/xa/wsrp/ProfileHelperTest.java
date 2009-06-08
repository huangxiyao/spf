package com.hp.it.spf.xa.wsrp;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Stack;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ProfileHelperTest extends TestCase {

	public void testWriteRead() {
	   ProfileHelper helper = new ProfileHelper();

	   Map profile = createProfile();
	   String profileStr = helper.profileToString(profile);
	   Map profile2 = helper.profileFromString(profileStr);

	   assertEquals("Written and read profiles", profile, profile2);
	}

	public void testRead() {
	   Stack stack = new Stack();
	   ProfileHelper helper = new ProfileHelper();

	   helper.readObject(stack, "abc", 0, new DefaultValueEncoder());
	   assertEquals("reading simple string", "abc", stack.pop());
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "[abc]", 0, new DefaultValueEncoder());
	   assertTrue("Object is a list", stack.peek() instanceof List);
	   List list = (List) stack.pop();
	   assertEquals("List size", 1, list.size());
	   assertEquals("Element content", "abc", list.get(0));
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "[a, b, c]", 0, new DefaultValueEncoder());
	   assertTrue("Object is a list", stack.peek() instanceof List);
	   list = (List) stack.pop();
	   assertEquals("List size", 3, list.size());
	   assertEquals("List content", Arrays.asList("a", "b", "c"), list);
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1}", 0, new DefaultValueEncoder());
	   assertTrue("Object is a map", stack.peek() instanceof Map);
	   Map map = (Map) stack.pop();
	   assertEquals("Map size", 1, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1, b=2}", 0, new DefaultValueEncoder());
	   assertTrue("Object is a map", stack.peek() instanceof Map);
	   map = (Map) stack.pop();
	   assertEquals("Map size", 2, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertEquals("Value of 'b'", "2", map.get("b"));
	   assertTrue("Stack empty", stack.isEmpty());
	}

	public void testEmptyValues() {
	   Stack stack = new Stack();
	   ProfileHelper helper = new ProfileHelper();

	   helper.readObject(stack, "[a, , c]", 0, new DefaultValueEncoder());
	   List list = (List) stack.pop();
	   assertEquals("List size", 3, list.size());
	   assertEquals("List content", Arrays.asList("a", "", "c"), list);
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "[a, ]", 0, new DefaultValueEncoder());
	   list = (List) stack.pop();
	   assertEquals("List size", 2, list.size());
	   assertEquals("List content", Arrays.asList("a", ""), list);
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1, b=, c=3}", 0, new DefaultValueEncoder());
	   Map map = (Map) stack.pop();
	   assertEquals("Map size", 3, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertEquals("Value of 'b'", "", map.get("b"));
	   assertEquals("Value of 'c'", "3", map.get("c"));
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1, b=}", 0, new DefaultValueEncoder());
	   map = (Map) stack.pop();
	   assertEquals("Map size", 2, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertEquals("Value of 'b'", "", map.get("b"));
	   assertTrue("Stack empty", stack.isEmpty());
	}

	public void testComplexWrite() {
	   ProfileHelper helper = new ProfileHelper();
	   StringBuffer sb = new StringBuffer();
	   helper.writeObject(sb, createProfile(), new DefaultValueEncoder());
	   System.out.println(sb);
	}

	public void testNonAsciiValues() throws Exception {
		ProfileHelper helper = new ProfileHelper();
		Map<String, String> profile = new HashMap<String, String>();
		profile.put("FirstName", "W\u0142adys\u0142aw");
		profile.put("LastName", "Jagie\u0142\u0142o");
		profile.put("Role", "King of Poland");

		String encodedString = helper.profileToString(profile, new UnicodeEscapeValueEncoder());
		assertTrue("FirstName was encoded", encodedString.indexOf("W\\u0142adys\\u0142aw") != -1);
		assertTrue("LastName was encoded", encodedString.indexOf("Jagie\\u0142\\u0142o") != -1);
		assertTrue("Role was not encoded", encodedString.indexOf("King of Poland") != -1);

		Map decodedProfile = helper.profileFromString(encodedString, new UnicodeEscapeValueEncoder());
		assertEquals("FirstName", "W\u0142adys\u0142aw", decodedProfile.get("FirstName"));
		assertEquals("LastName", "Jagie\u0142\u0142o", decodedProfile.get("LastName"));
		assertEquals("Role", "King of Poland", decodedProfile.get("Role"));
	}

	public void testComplexProfileWithUnicodeEscapeEncodingHelper() {
		ProfileHelper helper = new ProfileHelper();
		Map profile = createProfile();
		profile.put("FirstName", "W\u0142adys\u0142aw");
		profile.put("LastName", "Jagie\u0142\u0142o");
		
		String encodedProfile = helper.profileToString(profile, new UnicodeEscapeValueEncoder());
		Map decodedProfile = helper.profileFromString(encodedProfile, new UnicodeEscapeValueEncoder());

		assertEquals("Encoded and decoded profile", profile.toString(), decodedProfile.toString());
	}

	@SuppressWarnings("unchecked")
	private Map createProfile() {
	   Map profile = new HashMap();
	   profile.put("UserId", "123");
	   profile.put("UserStatus", "Active");

	   List hpRoles = new ArrayList(2);
	   profile.put("HPRoles", hpRoles);

	   Map role = new HashMap();
	   hpRoles.add(role);
	   role.put("HPRoleId", "abc");
	   role.put("HPRoleName", "ServerAdmin");

	   role = new HashMap();
	   hpRoles.add(role);
	   role.put("HPRoleId", "def");
	   role.put("HPRoleName", "SiteAdmin");

	   List programs = new ArrayList(2);
	   profile.put("Programs", programs);

	   Map program = new HashMap();
	   programs.add(program);
	   program.put("ProgramCode", "p1");
	   program.put("ProgramName", "p1 [name]");

	   List certifications = new ArrayList(2);
	   program.put("Certifications", certifications);

	   Map certification = new HashMap();
	   certifications.add(certification);
	   certification.put("Code", "c1");
	   certification.put("Name", "c1 {name}");

	   certification = new HashMap();
	   certifications.add(certification);
	   certification.put("Code", "c2");
	   certification.put("Name", "c2 name");

	   program = new HashMap();
	   programs.add(program);
	   program.put("ProgramCode", "p2");
	   program.put("ProgramName", "p2 name");

	   certifications = new ArrayList(2);
	   program.put("Certifications", certifications);

	   certification = new HashMap();
	   certifications.add(certification);
	   certification.put("Code", "c1");
	   certification.put("Name", "c1 name");

	   certification = new HashMap();
	   certifications.add(certification);
	   certification.put("Code", "c3");
	   certification.put("Name", "c3 name");

	   return profile;
	}

}