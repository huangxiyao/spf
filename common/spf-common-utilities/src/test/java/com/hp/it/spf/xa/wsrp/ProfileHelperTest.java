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

	   helper.readObject(stack, "abc", 0);
	   assertEquals("reading simple string", "abc", stack.pop());
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "[abc]", 0);
	   assertTrue("Object is a list", stack.peek() instanceof List);
	   List list = (List) stack.pop();
	   assertEquals("List size", 1, list.size());
	   assertEquals("Element content", "abc", list.get(0));
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "[a, b, c]", 0);
	   assertTrue("Object is a list", stack.peek() instanceof List);
	   list = (List) stack.pop();
	   assertEquals("List size", 3, list.size());
	   assertEquals("List content", Arrays.asList(new String[] {"a", "b", "c"}), list);
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1}", 0);
	   assertTrue("Object is a map", stack.peek() instanceof Map);
	   Map map = (Map) stack.pop();
	   assertEquals("Map size", 1, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1, b=2}", 0);
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

	   helper.readObject(stack, "[a, , c]", 0);
	   List list = (List) stack.pop();
	   assertEquals("List size", 3, list.size());
	   assertEquals("List content", Arrays.asList(new String[] {"a", "", "c"}), list);
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "[a, ]", 0);
	   list = (List) stack.pop();
	   assertEquals("List size", 2, list.size());
	   assertEquals("List content", Arrays.asList(new String[] {"a", ""}), list);
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1, b=, c=3}", 0);
	   Map map = (Map) stack.pop();
	   assertEquals("Map size", 3, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertEquals("Value of 'b'", "", map.get("b"));
	   assertEquals("Value of 'c'", "3", map.get("c"));
	   assertTrue("Stack empty", stack.isEmpty());

	   helper.readObject(stack, "{a=1, b=}", 0);
	   map = (Map) stack.pop();
	   assertEquals("Map size", 2, map.size());
	   assertEquals("Value of 'a'", "1", map.get("a"));
	   assertEquals("Value of 'b'", "", map.get("b"));
	   assertTrue("Stack empty", stack.isEmpty());
	}

	public void testComplexWrite() {
	   ProfileHelper helper = new ProfileHelper();
	   StringBuffer sb = new StringBuffer();
	   helper.writeObject(sb, createProfile());
	   System.out.println(sb);
	}

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
