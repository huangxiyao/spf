package com.hp.spp.wsrp.export.helper;

import org.hsqldb.jdbc.jdbcDataSource;

import com.hp.spp.db.DataSourceHolder;

public abstract class TestHelper {

	public static void setupTestDataSource() {
		jdbcDataSource ds = new jdbcDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:test");
		ds.setUser("sa");
		ds.setPassword("");
		DataSourceHolder.setTestDataSource(ds);
	}


}
