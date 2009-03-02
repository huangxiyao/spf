package com.hp.it.spf.xa.management.portal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class Log4jManagerDynamicMBeanTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
		Logger.getLogger("a.b");
		Logger.getLogger("a.b.c");
		Logger.getLogger("a.b.c.d");
	}
	
	public void testGetAllLoggersLevels() {
		Log4jManagerDynamicMBean mbean = new Log4jManagerDynamicMBean();
		List<String> expected = Arrays.asList("a.b: DEBUG", "a.b.c: DEBUG", "a.b.c.d: DEBUG");
		assertEquals("All loggers levels", expected, mbean.getAllLoggersLevels());
	}
	
	public void testGetLoggersLevels() {
		Log4jManagerDynamicMBean mbean = new Log4jManagerDynamicMBean();
		List<String> expected = Arrays.asList("a.b: DEBUG", "a.b.c: DEBUG", "a.b.c.d: DEBUG");
		assertEquals("All loggers levels if pattern is null", 
				expected, mbean.getLoggersLevels(null));
		
		List<String> expected2 = Arrays.asList("a.b.c: DEBUG", "a.b.c.d: DEBUG");
		assertEquals("Selected loggers levels only", expected2, mbean.getLoggersLevels("b.c"));
		
		assertEquals("Empty array if none matching loggers found", 
				Collections.emptyList(), mbean.getLoggersLevels("z"));
	}
	
	public void testSetLoggersLevels() {
		Log4jManagerDynamicMBean mbean = new Log4jManagerDynamicMBean();
		
		assertEquals("No loggers modified for incorrect level", 
				Collections.emptyList(), mbean.setLoggersLevels("a", "incorrect"));
		assertLevels("a.b", "DEBUG", "a.b.c", "DEBUG", "a.b.c.d", "DEBUG");
		
		List<String> expected = Arrays.asList("a.b.c: ERROR", "a.b.c.d: ERROR");
		assertEquals("Selected loggers modified", 
				expected, mbean.setLoggersLevels("b.c", "ERROR"));
		assertLevels("a.b", "DEBUG", "a.b.c", "ERROR", "a.b.c.d", "ERROR");
	}

	private void assertLevels(String... data) {
		for (int i = 0, len = data.length/2; i < len; i+=2) {
			assertEquals("Level for " + Logger.getLogger(data[i]), 
					data[i+1], Logger.getLogger(data[i]).getEffectiveLevel().toString());
		}
	}

	
}
