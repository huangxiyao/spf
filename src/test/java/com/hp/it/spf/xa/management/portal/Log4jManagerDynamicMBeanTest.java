package com.hp.it.spf.xa.management.portal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class Log4jManagerDynamicMBeanTest {

	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();
		Logger.getLogger("a.b").setLevel(Level.DEBUG);
		Logger.getLogger("a.b.c").setLevel(Level.DEBUG);
		Logger.getLogger("a.b.c.d").setLevel(Level.DEBUG);
	}

	@Test
	public void testGetAllLoggersLevels() {
		Log4jManagerDynamicMBean mbean = createTestMBean();
		List<String> expected = Arrays.asList("a.b: DEBUG", "a.b.c: DEBUG", "a.b.c.d: DEBUG");
		assertEquals("All loggers levels", expected, mbean.getAllLoggersLevels());
	}

	@Test
	public void testGetLoggersLevels() {
		Log4jManagerDynamicMBean mbean = createTestMBean();
		List<String> expected = Arrays.asList("a.b: DEBUG", "a.b.c: DEBUG", "a.b.c.d: DEBUG");
		assertEquals("All loggers levels if pattern is null", 
				expected, mbean.getLoggersLevels(null));
		
		List<String> expected2 = Arrays.asList("a.b.c: DEBUG", "a.b.c.d: DEBUG");
		assertEquals("Selected loggers levels only", expected2, mbean.getLoggersLevels("b.c"));
		
		assertEquals("Empty array if none matching loggers found", 
				Collections.emptyList(), mbean.getLoggersLevels("z"));
	}

	@Test
	public void testSetLoggersLevels() {
		Log4jManagerDynamicMBean mbean = createTestMBean();
		
		assertEquals("No loggers modified for incorrect level", 
				Collections.emptyList(), mbean.setLoggersLevels("a", "incorrect"));
		assertLevels("a.b", "DEBUG", "a.b.c", "DEBUG", "a.b.c.d", "DEBUG");
		
		List<String> expected = Arrays.asList("a.b.c: ERROR", "a.b.c.d: ERROR");
		assertEquals("Selected loggers modified", 
				expected, mbean.setLoggersLevels("b.c", "ERROR"));
		assertLevels("a.b", "DEBUG", "a.b.c", "ERROR", "a.b.c.d", "ERROR");
	}

	@Test
	public void testGetLoggersForLevel() {
		Log4jManagerDynamicMBean mbean = createTestMBean();

		assertNotNull("Never null is returned", mbean.getLoggersForLevel(null));
		assertNotNull("Never null is returned", mbean.getLoggersForLevel("invalid"));

		assertEquals("No loggers returned for invalid level",
				Collections.emptyList(), mbean.getLoggersLevels("invalid"));

		assertEquals("No loggers returned for valid level if no logger has that level",
				Collections.emptyList(), mbean.getLoggersLevels("FATAL"));

		assertEquals("All loggers returned if all have DEBUG level",
				new HashSet<String>(Arrays.asList("a.b", "a.b.c", "a.b.c.d")),
				new HashSet<String>(mbean.getLoggersForLevel("DEBUG")));

		Logger.getLogger("a.b.c").setLevel(Level.INFO);
		assertEquals("Only loggers with selected level returned",
				Arrays.asList("a.b.c"), mbean.getLoggersForLevel("INFO"));
		assertEquals("Only loggers with selected level returned",
				new HashSet<String>(Arrays.asList("a.b", "a.b.c.d")),
				new HashSet<String>(mbean.getLoggersForLevel("DEBUG")));

	}


	private void assertLevels(String... data) {
		for (int i = 0, len = data.length/2; i < len; i+=2) {
			assertEquals("Level for " + Logger.getLogger(data[i]), 
					data[i+1], Logger.getLogger(data[i]).getEffectiveLevel().toString());
		}
	}

	private Log4jManagerDynamicMBean createTestMBean()
	{
		return new Log4jManagerDynamicMBean() {
			@Override
			void setPortalSubsystemLevel(Level level, Logger logger)
			{
			}
		};
	}

}
