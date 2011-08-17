package com.hp.spp.portal.common.site;

import com.hp.spp.db.DataSourceHolder;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.hsqldb.jdbc.jdbcDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;

public abstract class TestFixture extends TestCase {

    static { BasicConfigurator.configure(); }

    protected jdbcDataSource mDataSource;

    protected void setUp() throws Exception {
        mDataSource = new jdbcDataSource();
        mDataSource.setDatabase("jdbc:hsqldb:mem:test");
        mDataSource.setUser("sa");
        mDataSource.setPassword("");
        runScript("/com/hp/spp/portal/common/site/testSetUp.sql");
		DataSourceHolder.setTestDataSource(mDataSource);
	}

    protected void tearDown() throws Exception {
        runScript("/com/hp/spp/portal/common/site/testTearDown.sql");
		DataSourceHolder.setTestDataSource(null);
	}

    private void runScript(String resourcePath) throws Exception {
        StringWriter sw = new StringWriter();
        InputStream is = getClass().getResourceAsStream(resourcePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            sw.write(line);
        }
        br.close();
        String[] statements = sw.toString().split(";");
        Connection conn = mDataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            for (int i = 0, len = statements.length; i < len; ++i) {
                String sql = statements[i].trim();
                if (!"".equals(sql)) {
                    Statement stmt = conn.createStatement();
                    stmt.execute(statements[i]);
                    stmt.close();
                }
            }
        }
        finally {
            conn.close();
        }
    }
}
