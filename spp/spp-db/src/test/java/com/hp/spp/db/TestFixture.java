package com.hp.spp.db;

import org.hsqldb.jdbc.jdbcDataSource;
import org.apache.log4j.BasicConfigurator;

import java.io.StringWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

public abstract class TestFixture extends TestCase {

    static { BasicConfigurator.configure(); }
    protected static final RowMapper<TestObject> TOM = new TestObjectMapper();

    protected jdbcDataSource mDataSource;

    protected void setUp() throws Exception {
        mDataSource = new jdbcDataSource();
        mDataSource.setDatabase("jdbc:hsqldb:mem:test");
        mDataSource.setUser("sa");
        mDataSource.setPassword("");
        runScript("/com/hp/spp/db/testSetUp.sql");
		DataSourceHolder.setTestDataSource(mDataSource);
	}

    protected void tearDown() throws Exception {
        runScript("/com/hp/spp/db/testTearDown.sql");
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

    protected static class TestObjectMapper implements RowMapper<TestObject> {
        public TestObject mapRow(ResultSet row, int rowNum) throws SQLException {
            return new TestObject(row.getInt("id"), row.getString("name"));
        }
    }

    protected static class TestObject {
        private int mId;
        private String mName;

        public TestObject(int mId, String mName) {
            this.mId = mId;
            this.mName = mName;
        }

        public int getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }
}
