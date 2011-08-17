package com.hp.spp.filters.access;

import junit.framework.Assert;
import org.apache.log4j.BasicConfigurator;

import java.lang.reflect.InvocationTargetException;

/**
 * LdapLogin test class. The purpose of this class is only ad-hoc testing and therefore it's
 * not a plain JUnit test. The reason for this is that it relies on information such as user's
 * email and password which is private, and used incorrectly can lead to the locking of user's
 * account.
 * <p>
 * IMPORTANT:
 * This class must be run with <tt>-Djavax.net.ssl.trustStore=/opt/bea/security/cacerts</tt>.
 * Its main method also takes 2 parameters: user's email and correct password
 */
public class TestLdapLogin {

	public static void main(String[] args) throws Exception{
		BasicConfigurator.configure();

		String email = args[0];
		String password = args[1];
		LdapLogin login = new LdapLogin();

		TestLdapLogin test = new TestLdapLogin(login, email, password);
		test.execute("testEmptyUser");
		test.execute("testEmptyPassword");
		test.execute("testEmptyUserPassword");
		test.execute("testIncorrectEmail");
		test.execute("testIncorrectPassword");
		test.execute("testSuccessfulLogin");
		System.out.println("All LdapLogin tests successful");
	}

	private LdapLogin mLogin;
	private String mEmail;
	private String mPassword;

	public TestLdapLogin(LdapLogin login, String email, String password) {
		mLogin = login;
		mEmail = email;
		mPassword = password;
	}

	public TestLdapLogin() {
	}

	private void execute(String test) throws NoSuchMethodException, IllegalAccessException {
		try {
			System.out.print(test + " - ");
			getClass().getDeclaredMethod(test, null).invoke(this, null);
			System.out.println("OK");
		}
		catch (InvocationTargetException e) {
			System.out.println("FAILED");
			e.printStackTrace(System.out);
		}
	}

	private void testEmptyUser() {
		try {
			Assert.assertFalse("Empty user", mLogin.login(LdapLogin.DEFAULT_URL, "", "xyz"));
		}
		catch (Exception e) {
			Assert.fail("Exception occured: " + e);
		}
	}

	private void testEmptyPassword() {
		try {
			Assert.assertFalse("Empty user", mLogin.login(LdapLogin.DEFAULT_URL, "john.smith-junior@hp.com", ""));
		}
		catch (Exception e) {
			Assert.fail("Exception occured: " + e);
		}
	}

	private void testEmptyUserPassword() {
		try {
			Assert.assertFalse("Empty user/password", mLogin.login(LdapLogin.DEFAULT_URL, "", ""));
		}
		catch (Exception e) {
			Assert.fail("Exception occured: " + e);
		}
	}

	private void testIncorrectPassword() {
		try {
			Assert.assertFalse("Incorrect password", mLogin.login(LdapLogin.DEFAULT_URL, mEmail, "incorrect_password"));
		}
		catch (Exception e) {
			Assert.fail("Exception occured: " + e);
		}
	}

	private void testIncorrectEmail() {
		try {
			Assert.assertFalse("Incorrect email", mLogin.login(LdapLogin.DEFAULT_URL, "aljsdflaj;54s'sdf", "incorrect_password"));
		}
		catch (Exception e) {
			Assert.fail("Exception occured: " + e);
		}
	}

	private void testSuccessfulLogin() {
		try {
			Assert.assertTrue("Login successful", mLogin.login(LdapLogin.DEFAULT_URL, mEmail, mPassword));
		}
		catch (Exception e) {
			Assert.fail("Exception occured: " + e);
		}
	}
}
