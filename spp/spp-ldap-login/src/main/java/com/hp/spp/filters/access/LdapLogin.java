package com.hp.spp.filters.access;

import com.hp.spp.config.Config;

import javax.naming.Context;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import javax.naming.InvalidNameException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public class LdapLogin {
	private static final Logger mLog = Logger.getLogger(LdapLogin.class);

	static final String DEFAULT_URL = "ldaps://ldap.hp.com:636/o=hp.com";

	public boolean login(String email, String password) throws NamingException {
		return login(Config.getValue("SPP.LdapLogin.Url", DEFAULT_URL), email, password);
	}

	@SuppressWarnings("unchecked")
	protected boolean login(String ldapUrl, String email, String password) throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_PRINCIPAL, "uid=" + email + ", ou=people, o=hp.com");
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			// create the context to force authentication
			new InitialDirContext(env);
		}
		catch (AuthenticationException e) {
			// this happens when the credentials are incorrect
			if (mLog.isDebugEnabled()) {
				mLog.debug("Unable to create InitialDirContext", e);
			}
			return false;
		}
		catch (InvalidNameException e) {
			// this happens when the input data (email, password) have incorrect format
			if (mLog.isDebugEnabled()) {
				mLog.debug("Unable to create InitialDirContext", e);
			}
			return false;
		}
		catch (OperationNotSupportedException e) {
			// this happens when the password is empty
			if (mLog.isDebugEnabled()) {
				mLog.debug("Unable to create InitialDirContext", e);
			}
			return false;
		}

		return true;
	}

}
