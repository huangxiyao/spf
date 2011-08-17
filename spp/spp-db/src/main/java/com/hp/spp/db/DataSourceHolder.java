package com.hp.spp.db;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class DataSourceHolder {
    private static final String DEFAULT_JNDI_NAME = "jdbc/Vignette_PORTALDS";
    private static Logger mLog = Logger.getLogger(DataSourceHolder.class);
    private static final String SPPDB_JNDI_NAME_KEY = "sppdb.jndi.name";

    private static DataSource mDataSource;

    private DataSourceHolder() {}

    /**
     * This method should be used <b>only for testing</b>.
     */
    public static void setTestDataSource(DataSource ds) {
        mDataSource = ds;
        mLog.warn("DataSource set to test data source: " + ds);
    }

    public static synchronized DataSource getDataSource() {
        if (mDataSource == null) {
            try {
                InitialContext ic = new InitialContext();
                String jndiName = getJndiName();
                if (mLog.isInfoEnabled()) {
                    mLog.info("Looking data source in JNDI using the following name: " + jndiName);
                }
                mDataSource = (DataSource) ic.lookup(jndiName);
                ic.close();
            } catch (Exception e) {
                throw new DatabaseException("Error retrieving datasource", e);
            }
        }
        return mDataSource;
    }

    private static String getJndiName() throws IOException {
        InputStream is = DataSourceHolder.class.getResourceAsStream("/spp-db.properties");
        if (is == null) {
            return DEFAULT_JNDI_NAME;
        }
        else {
            Properties props = new Properties();
            props.load(is);
            is.close();
            if (props.containsKey(SPPDB_JNDI_NAME_KEY)) {
                return props.getProperty(SPPDB_JNDI_NAME_KEY);
            }
            else {
                return DEFAULT_JNDI_NAME;
            }
        }
    }
}
