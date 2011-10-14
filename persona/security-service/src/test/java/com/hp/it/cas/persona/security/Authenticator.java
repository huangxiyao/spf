package com.hp.it.cas.persona.security;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

class Authenticator {

	private static final String AUTHENTICATION_URL = "ldaps://ldap.hp.com:636";
	private static final String APPLICATIONS = "cn=%s,ou=Applications,o=hp.com";

	void authenticate(String identifier, String password) throws AuthenticationException {

		try
		{
			Hashtable<String, String> env = new Hashtable<String, String>();
//			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, AUTHENTICATION_URL);
			env.put(Context.SECURITY_PRINCIPAL, String.format(APPLICATIONS, identifier));
			env.put(Context.SECURITY_CREDENTIALS, password);

			DirContext authenticate = new InitialDirContext(env);
			authenticate.close();
		}
		catch (AuthenticationException ex) {
			throw ex;
		}
		catch (NamingException ex)
		{
			throw new RuntimeException("Unable to access Enterprise Directory.", ex);
		}
	}

	public static void main(String[] args) throws AuthenticationException {
		Authenticator authenticator = new Authenticator();
		authenticator.authenticate("APP-999999", "123qwe!@#QWE");
		System.out.println("Successful authentication");
	}
}
