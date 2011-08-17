package com.hp.spp.cache;

import junit.framework.TestCase;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import java.util.Properties;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class CacheMonitorTest extends TestCase {

	static {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
	}

	private GeneralCacheAdministrator mAdmin;
	private GeneralCacheAdministrator mAdmin2;
	private CacheMonitor mMonitor;

	protected void setUp() throws Exception {
		Properties cacheConfig = new Properties();
		cacheConfig.setProperty("cache.event.listeners", CacheMonitorNotifier.class.getName());
		mAdmin = new GeneralCacheAdministrator(cacheConfig);
		mAdmin2 = new GeneralCacheAdministrator(cacheConfig);
		mMonitor = CacheMonitor.getInstance();
	}

	protected void tearDown() throws Exception {
		mAdmin.destroy();
		mMonitor.destroy();
	}

	public void testAddCacheEntryCallback() throws Exception {
		assertEquals("Keys are empty", Collections.EMPTY_SET, mMonitor.getAllKeys());
		assertEquals("Groups are empty", Collections.EMPTY_SET, mMonitor.getAllGroups());

		mAdmin.putInCache("key1", "TEST", new String[] {"group1", "group2"});
		assertEquals("Keys", set(new String[] {"key1"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"group1", "group2"}), mMonitor.getAllGroups());

		mAdmin.putInCache("key2", "TEST2", new String[] {"group2", "group3"});
		assertEquals("Keys", set(new String[] {"key1", "key2"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"group1", "group2", "group3"}), mMonitor.getAllGroups());
	}

	public void testRemoveCacheEntryCallback() throws Exception {
		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});
		assertEquals("Keys", set(new String[] {"key1", "key2"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"g1", "g2", "g3"}), mMonitor.getAllGroups());

		mAdmin.removeEntry("key1");
		assertEquals("Keys after removal", set(new String[] {"key2"}), mMonitor.getAllKeys());
		assertEquals("Groups after removal", set(new String[] {"g1", "g2", "g3"}), mMonitor.getAllGroups());

		mAdmin.removeEntry("key1");
		assertEquals("Keys after duplicate removal", set(new String[] {"key2"}), mMonitor.getAllKeys());
		// groups are not removed with entries - they must be removed explicitly
		assertEquals("Groups after duplicate removal", set(new String[] {"g1", "g2", "g3"}), mMonitor.getAllGroups());

		// flush acts same as delete for monitor
		mAdmin.flushEntry("key2");
		assertEquals("Keys after second key removal", Collections.EMPTY_SET, mMonitor.getAllKeys());
		// groups are not removed with entries - they must be removed explicitly
		assertEquals("Groups after second key removal", set(new String[] {"g1", "g2", "g3"}), mMonitor.getAllGroups());
	}

	public void testRemoveGroupCallback() throws Exception {
		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});
		mAdmin.putInCache("key3", "TEST3", new String [] {"g3"});

		mAdmin.flushGroup("g2");
		assertEquals("Keys", set(new String[] {"key3"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"g1", "g3"}), mMonitor.getAllGroups());
	}

	public void testRemoveAllForCacheCallback() throws Exception {
		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});

		mAdmin.flushAll();
		assertEquals("Keys", Collections.EMPTY_SET, mMonitor.getAllKeys());
		assertEquals("Groups", Collections.EMPTY_SET, mMonitor.getAllGroups());
	}

	public void testMonitorForTwoCacheAdmins() throws Exception {
		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});
		mAdmin2.putInCache("key1", "TEST3", new String[] {"g1"});
		mAdmin2.putInCache("key3", "TEST4", new String[] {"g2", "g4"});

		assertEquals("Keys", set(new String[] {"key1", "key2", "key3"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"g1", "g2", "g3", "g4"}), mMonitor.getAllGroups());

		mAdmin.flushEntry("key1");
		assertEquals("Keys", set(new String[] {"key1", "key2", "key3"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"g1", "g2", "g3", "g4"}), mMonitor.getAllGroups());

		mAdmin2.flushGroup("g4");
		assertEquals("Keys", set(new String[] {"key1", "key2"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"g1", "g2", "g3"}), mMonitor.getAllGroups());
	}

	public void testMonitorFlushForTwoCacheAdmins() throws Exception {
		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});
		mAdmin2.putInCache("key1", "TEST3", new String[] {"g1"});
		mAdmin2.putInCache("key3", "TEST4", new String[] {"g2", "g4"});

		mMonitor.flushEntry("key1");
		assertEquals("Keys after monitor.flushEntry", set(new String[] {"key2", "key3"}), mMonitor.getAllKeys());
		// flushing a key does not change the groups
		assertEquals("Groups after monitor.flushEntry", set(new String[] {"g1", "g2", "g3", "g4"}), mMonitor.getAllGroups());

		mMonitor.flushGroup("g2");
		assertEquals("Keys after monitor.flushGroup", Collections.EMPTY_SET, mMonitor.getAllKeys());
		assertEquals("Groups after monitor.flushGroup", set(new String[] {"g1", "g3", "g4"}), mMonitor.getAllGroups());

		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});
		mAdmin2.putInCache("key1", "TEST3", new String[] {"g1"});
		mAdmin2.putInCache("key3", "TEST4", new String[] {"g2", "g4"});
		assertEquals("Keys", set(new String[] {"key1", "key2", "key3"}), mMonitor.getAllKeys());
		assertEquals("Groups", set(new String[] {"g1", "g2", "g3", "g4"}), mMonitor.getAllGroups());

		mMonitor.flushAll();
		assertEquals("Keys after monitor.flushAll", Collections.EMPTY_SET, mMonitor.getAllKeys());
		assertEquals("Groups after monitor.flushAll", Collections.EMPTY_SET, mMonitor.getAllGroups());
	}

	public void testGetFromCacheForTwoCacheAdmins() throws Exception {
		mAdmin.putInCache("key1", "TEST", new String[] {"g1", "g2"});
		mAdmin.putInCache("key2", "TEST2", new String[] {"g2", "g3"});
		mAdmin2.putInCache("key1", "TEST3", new String[] {"g1"});
		mAdmin2.putInCache("key3", "TEST4", new String[] {"g2", "g4"});

		List cachedValues;

		cachedValues = mMonitor.getFromCache("non_existing_key");
		assertNotNull("cached value list is not null", cachedValues);
		assertTrue("cached value list is empty", cachedValues.isEmpty());

		cachedValues = mMonitor.getFromCache("key1");
		assertNotNull("cached value list is not null", cachedValues);
		assertEquals("cached values count", 2, cachedValues.size());
		assertEquals("cached values", set(new String[] {"TEST", "TEST3"}), new TreeSet(cachedValues));

		cachedValues = mMonitor.getFromCache("key3");
		assertEquals("cached values", set(new String[] {"TEST4"}), new TreeSet(cachedValues));
	}

	private SortedSet set(String[] array) {
		return new TreeSet(Arrays.asList(array));
	}
}
