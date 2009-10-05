package com.hp.it.spf.sso.portal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.StringReader;
import junit.framework.TestCase;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AnonUsersInputFileTest extends TestCase {

	public void testInputFileNotFound() {
		try {
			new AnonUsersInputFile("NON_EXISTING_INPUT_FILE_PATH");
			fail("FileNotFoundException expected!");
		}
		catch (FileNotFoundException e) {
			assertTrue(true);
		}
	}

	public void testEmptyInputFile() {
		AnonUsersInputFile inputFile = new AnonUsersInputFile(new BufferedReader(new StringReader("")));
		assertFalse("Iterator has elements", inputFile.hasNext());
	}

	public void testIncorrectInputFile() {
		String data = "\n# some comment\n,,,\nen,US,America/Los_Angeles\n,,,\n";

		AnonymousUserData userData = null;
		AnonUsersInputFile inputFile = new AnonUsersInputFile(new BufferedReader(new StringReader(data)));

		// 1st non-empty and non-comment line is invalid
		assertTrue("Iterator has next even though the line is invalid", inputFile.hasNext());
		try {
			userData = inputFile.next();
			fail("IllegalArgumentException is expected");
		} catch (IllegalArgumentException e) {
			assertTrue("IllegalArgumentException is expected", true);
		}

		// 2nd non-empty/non-comment line is valid
		assertTrue("Iterator has next valid value", inputFile.hasNext());
		userData = inputFile.next();
		assertEquals("Users timezone", "America/Los_Angeles", userData.getTimeZone());

		// 3rd non-emtpy/non-comment line is invalid
		assertTrue("Iterator has next event though the line is invalid", inputFile.hasNext());
		try {
			userData = inputFile.next();
			fail("IllegalArgumentException is expected");
		} catch (IllegalArgumentException e) {
			assertTrue("IllegalArgumentException is expected", true);
		}

		// there are no more lines
		assertFalse("There is no more lines", inputFile.hasNext());
	}

	public void testCommentsAndEmptyLinesIgnored() {
		AnonUsersInputFile inputFile = new AnonUsersInputFile(new BufferedReader(new StringReader("# a single comment line\n\n")));
		assertFalse("Iterator has elements", inputFile.hasNext());
	}

	public void testSupportedSeparators() {
		String data =
				"en\tUS\tAmerica/Los_Angeles\n" +
				"es,MX,America/Mexico_City\n" +
				"fr;FR;Europe/Paris";
		AnonUsersInputFile inputFile = new AnonUsersInputFile(new BufferedReader(new StringReader(data)));

		assertTrue("1st user available", inputFile.hasNext());
		AnonymousUserData user1 = inputFile.next();
		assertEquals("1st user language", "en", user1.getLanguage());
		assertEquals("1st user country", "US", user1.getCountry());
		assertEquals("1st user time zone", "America/Los_Angeles", user1.getTimeZone());

		assertTrue("2nd user available", inputFile.hasNext());
		AnonymousUserData user2 = inputFile.next();
		assertEquals("2nd user language", "es", user2.getLanguage());
		assertEquals("2nd user country", "MX", user2.getCountry());
		assertEquals("2nd user time zone", "America/Mexico_City", user2.getTimeZone());

		assertTrue("3rd user available", inputFile.hasNext());
		AnonymousUserData user3 = inputFile.next();
		assertEquals("3rd user language", "fr", user3.getLanguage());
		assertEquals("3rd user country", "FR", user3.getCountry());
		assertEquals("3rd user time zone", "Europe/Paris", user3.getTimeZone());

		assertFalse("More users available", inputFile.hasNext());
	}

	public void testUserDataWithEmptyCountry() {
		String data = "en,,America/Los_Angeles\n";
		AnonUsersInputFile inputFile = new AnonUsersInputFile(new BufferedReader(new StringReader(data)));

		AnonymousUserData userData = inputFile.next();
		assertNull("User country", userData.getCountry());
	}
}