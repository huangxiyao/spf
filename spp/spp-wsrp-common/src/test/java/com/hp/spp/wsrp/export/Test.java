package com.hp.spp.wsrp.export;

import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.hsqldb.jdbc.jdbcDataSource;
import org.w3c.dom.Document;

import com.hp.spp.db.DataSourceHolder;
import com.hp.spp.wsrp.export.util.DOMUtils;

public abstract class Test extends TestCase {

	static {
		BasicConfigurator.configure();
	}
	
	protected void setupTestDataSource() {
		jdbcDataSource ds = new jdbcDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:test");
		ds.setUser("sa");
		ds.setPassword("");
		DataSourceHolder.setTestDataSource(ds);
	}

	protected Document readInputStr(String xml) throws Exception {
		return DOMUtils.initializeDocument(new StringReader(xml));
	}

}
