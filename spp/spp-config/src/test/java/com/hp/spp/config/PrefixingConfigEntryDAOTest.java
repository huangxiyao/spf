package com.hp.spp.config;

import junit.framework.TestCase;

public class PrefixingConfigEntryDAOTest extends TestCase {

	public void testLoad() throws Exception {
		ConfigEntryDAO delegate = new HashMapConfigEntryDAO();
		delegate.save(new ConfigEntry("portal.name1", "pv1", null));
		delegate.save(new ConfigEntry("wsrp.name1", "wv1", null));
		delegate.save(new ConfigEntry("name1", "v1", null));
		delegate.save(new ConfigEntry("portal.name2", "pv2", null));
		delegate.save(new ConfigEntry("name3", "v3", null));

		PrefixingConfigEntryDAO dao;

		dao = new PrefixingConfigEntryDAO(delegate, "portal");
		assertEquals("prefixed value returned", "pv1", dao.load("name1").getValue());
		assertEquals("nonprefixed value returned", "pv1", dao.load("portal.name1").getValue());
		assertEquals("prefixed value returned", "pv2", dao.load("name2").getValue());
		assertEquals("nonprefixed value returned", "v3", dao.load("name3").getValue());

		dao = new PrefixingConfigEntryDAO(delegate, "wsrp");
		assertEquals("prefixed value returned", "wv1", dao.load("name1").getValue());
		assertNull("no prefixed or general value defined", dao.load("name2"));
		assertEquals("nonprefixed value retruned", "v3", dao.load("name3").getValue());

		dao = new PrefixingConfigEntryDAO(delegate, null);
		assertEquals("nonprefixed value returned", "v1", dao.load("name1").getValue());
		assertNull("no prefixed or general value defined", dao.load("name2"));
		assertEquals("nonprefixed value returned", "v3", dao.load("name3").getValue());
	}
}
