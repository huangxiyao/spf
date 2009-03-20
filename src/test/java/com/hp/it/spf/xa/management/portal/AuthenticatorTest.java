package com.hp.it.spf.xa.management.portal;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;


/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AuthenticatorTest
{
	private Authenticator mTestAuthenticator = new Authenticator(null, null) {
		@Override
		protected Subject doAuthenticate(String username, String password) throws LoginException
		{
			return null;
		}
	};

	@Test(expected = SecurityException.class)
	public void testAuthenticateNullCredentials() {
		mTestAuthenticator.authenticate(null);
	}

	@Test(expected = SecurityException.class)
	public void testAuthenticateIncorrectCredentialsType() {
		mTestAuthenticator.authenticate("test");
	}

	@Test(expected = SecurityException.class)
	public void testAuthenticateIncorrectArraySize() {
		mTestAuthenticator.authenticate(new String[] {"a"});
	}

	@Test
	public void testAuthenticateCorrectCredentials() {
		final String expectedUsername = "john";
		final String expectedPassword = "doe";
		final boolean[] doAuthenticateCalled = { false };
		Authenticator authenticator = new Authenticator(null, null) {
			@Override
			protected Subject doAuthenticate(String username, String password) throws LoginException
			{
				doAuthenticateCalled[0] = true;
				assertThat("Username", username, is(expectedUsername));
				assertThat("Password", password, is(expectedPassword));
				return null;
			}
		};
		authenticator.authenticate(new String[] {expectedUsername, expectedPassword});
		assertThat("doAuthenticate called", doAuthenticateCalled[0], is(true));
	}
}
