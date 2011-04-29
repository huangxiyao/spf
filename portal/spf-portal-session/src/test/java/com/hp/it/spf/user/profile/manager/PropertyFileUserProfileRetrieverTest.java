package com.hp.it.spf.user.profile.manager;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PropertyFileUserProfileRetrieverTest
{
	private final PropertyFileUserProfileRetriever mRetriever = new PropertyFileUserProfileRetriever() {
		@Override
		protected String getSiteName(HttpServletRequest request) { return "TEST_SITE"; }
	};

	@Test
	public void testEmptyMapWhenUserIdNull() {
		assertThat(mRetriever.getUserProfile(null, null).isEmpty(), is(true));
	}

	@Test
	public void testEmptyMapWhenFileNotFound() {
		assertThat(mRetriever.getUserProfile("NON_EXISTING_USER_ID", null).isEmpty(), is(true));
	}

	@Test
	public void testNonEmptyMapWhenFileExists() {
		Map<String, Object> profile = mRetriever.getUserProfile("property_retriever_test_user_id", null);
		assertThat("Map has 3 items", profile.size(), is(3));
		for (Map.Entry<String, Object> entry : profile.entrySet()) {
			assertThat("Value is of type String", entry.getValue() instanceof String, is(true));
		}
	}

}
