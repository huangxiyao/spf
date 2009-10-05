/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;

/**
 * This is the test class for PersonaUserProfileRetriever class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @version 1.0
 */
public class PersonaUserProfileRetrieverTest {

    /**
     * Test method for {PersonaUserProfileRetriever#getUserProfile}.
     */
    @Test
    public void testGetUserProfile() {
//        IUserProfileRetriever retriever = UserProfileRetrieverFactory.createUserProfileImpl();
//        Map<String, String> userProfiles = retriever.getUserProfile(EUserIdentifierType.EXTERNAL_USER,
//                                                                    null,
//                                                                    null);
//        assertTrue(userProfiles.size() == 0);
    }

	@Test
	public void testConvertSimpleValues() {
		Map<String, Object> userProfile = new HashMap<String, Object>();

		Map<String, Collection<String>> simpleValues = new HashMap<String, Collection<String>>();
		simpleValues.put("1", (Collection<String>) null);
		simpleValues.put("2", stringCollection());
		simpleValues.put("3", stringCollection("a", "b", "c"));

		PersonaUserProfileRetriever retriever = new PersonaUserProfileRetriever();
		retriever.convertSimpleValues(userProfile, simpleValues);

		assertThat("null value", userProfile.get("1"), nullValue());
		assertThat("Type is List", userProfile.get("2"), instanceOf(List.class));
		assertThat("List is empty", ((List) userProfile.get("2")).size(), is(0));
		assertEquals("Non-empty list", ((List) userProfile.get("3")), Arrays.asList("a", "b", "c"));
	}


	public void testConvertCompoundValues() {
		Map<String, Object> userProfile = new HashMap<String, Object>();

		Map<String, Collection<ICompoundUserAttributeValue>> compoundValues =
				new HashMap<String, Collection<ICompoundUserAttributeValue>>();
		compoundValues.put("1", (Collection<ICompoundUserAttributeValue>) null);
		compoundValues.put("2", compoundCollection());
		compoundValues.put("3", compoundCollection(
				"{1 : a, 2 : b, 3 : c}",
				"{1 : x, 2 : y, 3 : z}"));


		PersonaUserProfileRetriever retriever = new PersonaUserProfileRetriever();
		retriever.convertCompoundValues(userProfile, compoundValues);

		assertThat("null value", userProfile.get("1"), nullValue());
		assertThat("Type is List", userProfile.get("2"), instanceOf(List.class));
		assertThat("List is empty", ((List) userProfile.get("2")).size(), is(0));
		assertThat("Non-empty list size", ((List) userProfile.get("3")).size(), is(2));

		Map<String, String> firstValue = (Map<String, String>) ((List) userProfile.get("3")).get(0);
		assertThat("First value is not null", firstValue, notNullValue());
		assertThat("First value's 1", firstValue.get("1"), is("a"));
		assertThat("First value's 2", firstValue.get("2"), is("b"));
		assertThat("First value's 3", firstValue.get("3"), is("c"));


		Map<String, String> secondValue = (Map<String, String>) ((List) userProfile.get("3")).get(1);
		assertThat("Second value is not null", secondValue, notNullValue());
		assertThat("Second value's 1", secondValue.get("1"), is("a"));
		assertThat("Second value's 2", secondValue.get("2"), is("b"));
		assertThat("Second value's 3", secondValue.get("3"), is("c"));
	}


	private Collection<String> stringCollection(String ... items) {
		return Collections.unmodifiableCollection(Arrays.asList(items));
	}

	private Collection<ICompoundUserAttributeValue> compoundCollection(String... items) {
		Set<ICompoundUserAttributeValue> result = new HashSet<ICompoundUserAttributeValue>();
		for (String item : items) {
			result.add(new TestCompoundUserAttributeValue(item));
		}
		return result;
	}

	private class TestCompoundUserAttributeValue extends HashMap<String, String> implements ICompoundUserAttributeValue {

		private TestCompoundUserAttributeValue(String entries)
		{
			entries = entries.trim();
			entries = entries.substring(0, entries.length()-1);
			for (String entry : entries.split(",")) {
				String[] entryItems = entry.split(":");
				put(entryItems[0].trim(), entryItems[1].trim());
			}
		}

		public String getInstanceIdentifier()
		{
			throw new UnsupportedOperationException("Not supported in the test class");
		}

		public void remove()
		{
			throw new UnsupportedOperationException("Not supported in the test class");
		}
	}

}
