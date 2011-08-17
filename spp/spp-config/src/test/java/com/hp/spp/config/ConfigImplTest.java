package com.hp.spp.config;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class ConfigImplTest extends TestCase {
	public void testGetEntries() throws Exception {
	   HashMapConfigEntryDAO mDao = new HashMapConfigEntryDAO();
	   mDao.save(new ConfigEntry("a", "a", null));
	   mDao.save(new ConfigEntry("b", 2, null));
	   mDao.save(new ConfigEntry("c", false, null));
	   ConfigImpl cfg = new ConfigImpl(mDao);

	   Iterator entries = cfg.getEntries();

	   ConfigEntry a = (ConfigEntry) entries.next();
	   assertEquals("a value", "a", cfg.getValue("a"));
	   assertEquals("a type", ConfigEntry.TYPE_STRING, a.getType());

	   ConfigEntry b = (ConfigEntry) entries.next();
	   assertEquals("b value", 2, cfg.getIntValue("b"));
	   assertEquals("b type", ConfigEntry.TYPE_INT, b.getType());

	   ConfigEntry c = (ConfigEntry) entries.next();
	   assertEquals("c value", false, cfg.getBooleanValue("c"));
	   assertEquals("c type", ConfigEntry.TYPE_BOOLEAN, c.getType());
	}

	public void testGetEntriesWithPattern() throws Exception {
	   HashMapConfigEntryDAO mDao = new HashMapConfigEntryDAO();
	   mDao.save(new ConfigEntry("a.b.1", 1, null));
	   mDao.save(new ConfigEntry("a.b.2", 1, null));
	   mDao.save(new ConfigEntry("x.b.1", 1, null));
	   mDao.save(new ConfigEntry("y.b.1", 1, null));
	   mDao.save(new ConfigEntry("z.c.1", 1, null));
	   ConfigImpl cfg = new ConfigImpl(mDao);

	   assertTrue("a.b.* entries",
			 sameNames(new String[] {"a.b.1", "a.b.2"}, cfg.getEntries("a.b.*")));
	   assertTrue("*.b.* entries",
			 sameNames(new String[] {"a.b.1", "a.b.2", "x.b.1", "y.b.1"}, cfg.getEntries("*.b.*")));
	   assertTrue("*.b.1 entries",
			 sameNames(new String[] {"a.b.1", "x.b.1", "y.b.1"}, cfg.getEntries("*.b.1")));
	}

	private boolean sameNames(String[] names1, Iterator configEntries) {
		Set s1 = new HashSet(Arrays.asList(names1));
		Set s2 = new HashSet();
		while (configEntries.hasNext()) {
			s2.add(((ConfigEntry) configEntries.next()).getName());
		}
		return s1.equals(s2);
	}

}
