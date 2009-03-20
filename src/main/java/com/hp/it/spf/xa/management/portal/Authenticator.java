package com.hp.it.spf.xa.management.portal;

import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.log.LogConfiguration;

import javax.management.remote.JMXAuthenticator;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Set;
import java.security.Principal;

import weblogic.security.services.Authentication;

/**
 * Class used to authenticate access to the the JMXConnectorServer.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class Authenticator implements JMXAuthenticator
{
	private static final LogWrapper LOG = new LogWrapper(Authenticator.class);

	private String mRealm;
	private Set<String> mGroupNames;

	public Authenticator(String realm, Set<String> groupNames)
	{
		mRealm = (realm == null ? null : realm.trim());
		if (groupNames == null || groupNames.isEmpty()) {
			mGroupNames = null;
		}
		else {
			mGroupNames = groupNames;
		}
	}

	/**
	 * Authenticates user in the server.
	 * @param credentials 2-element String array containing username and password
	 * @return authenticated user
	 * @throws SecurityException if credentials are incorrect or access is controlled through
	 * groups and the authenticated user is not part of these groups.
	 */
	public Subject authenticate(Object credentials) throws SecurityException
	{
		if (credentials == null) {
			throw new SecurityException("Authentication required!");
		}
		if (!(credentials instanceof String[])) {
			throw new SecurityException("Credentials should be String[] instead of " +
					credentials.getClass().getName());
		}
		String[] credentialsContent = (String[]) credentials;
		if (credentialsContent.length != 2) {
			throw new SecurityException("Credentials should have 2 elements and not " +
					credentialsContent.length);
		}
		String username = credentialsContent[0];
		String password = credentialsContent[1];
		if (username == null || username.trim().length() == 0 ||
				password == null || password.trim().length() == 0)
		{
			throw new SecurityException("Username and password required!");
		}

		try {
			Subject subject = doAuthenticate(username, password);
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Authenticated user: " + subject);
			}
			if (mGroupNames != null) {
				for (Principal principal : subject.getPrincipals()) {
					if (mGroupNames.contains(principal.getName())) {
						return subject;
					}
				}
				throw new SecurityException("User not authorized!");
			}
			return subject;
		}
		catch (LoginException e) {
			// Authentication errors are expected (e.g. incorrect password), therefore we don't want
			// to log them at ERROR level. Let's use DEBUG in case we want to know exactly what
			// went wrong.
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Authentication error: " + e, e);
			}
			throw new SecurityException("Authentication failed!");
		}
	}


	/**
	 * Performs the actual authentication.
	 * @param username username as specified in for example JConsole
	 * @param password password as specified in for example JConsole
	 * @return authenticated user
	 * @throws LoginException If an error occured while authenticating user.
	 */
	protected Subject doAuthenticate(final String username, final String password) throws LoginException
	{
		CallbackHandler callbackHandler = new CallbackHandler()
		{
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
			{
				for (int i = 0, len = callbacks.length; i < len; i++) {
					Callback callback = callbacks[i];
					if (callback instanceof NameCallback) {
						NameCallback nameCallback = (NameCallback) callback;
						nameCallback.setName(username);
					}
					else if (callback instanceof PasswordCallback) {
						PasswordCallback passwordCallback = (PasswordCallback) callback;
						passwordCallback.setPassword(password.toCharArray());
					}
					else {
						throw new UnsupportedCallbackException(callback);
					}
				}
			}
		};

		if (mRealm == null) {
			return Authentication.login(callbackHandler);
		}
		else {
			return Authentication.login(mRealm, callbackHandler);
		}
	}
}
