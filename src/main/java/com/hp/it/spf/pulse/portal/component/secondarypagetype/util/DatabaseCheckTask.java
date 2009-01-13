package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.config.PortalConfigUtils;

/**
 * A class for monitoring the health of the Vignette database.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @author <link href="hao.zhang2@hp.com"> Zhang, Hao </link>
 * @version TBD
 * @see GeneralComponentCheckTask
 * @see LogWrapper
 * @see Connection
 * @see Statement
 */
public class DatabaseCheckTask extends GeneralComponentCheckTask {

    /**
     * serialVersionUID
     * long
     */
    private static final long serialVersionUID = -7245998522253322126L;

    private static final String VIG_DB_NAME = 
        "Vignette Database"; // the name of VIG DB

    private static final String QUERY_STRING = 
        "select * from user_tables"; //The SQL for testing the database

    /*
     * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 5/29/2008.
     * Collect more information for the error logs, then can provide more information for 
     * troubleshooting.
     */
    // current class name 
    private static String thisClassname = DatabaseCheckTask.class.getName();
    
    /**
     * define the parameter for connecting vignette database
     */
    private String driver = null;
    private String url = null;
    private String user = null;
    private String password = null;
    /**
     * params read from the config file of healthcheck.xml
     * added by ck for CR: 1000813522 
     */
    private Map params = new HashMap();
    /**
     * the status of initialization
     */
    private boolean initSuccess = false; //false serves as flag meaning init failed

    /**
     * the log for vignette when throwing exception
     */
    private static final LogWrapper LOG = new LogWrapper(
            DatabaseCheckTask.class);
    /**
     * Set the attribute params, like url, pattern, trustStore,
     * trustStorePassoword, and so on.
     * added by ck for CR: 1000813522 
     * @param params
     *            attribute params like url, pattern, trustStore,
     *            trustStorePassoword, and so on
     */
    public void setParams(Map params) {
        this.params = params;
    }
    /**
     * constructor for DatabaseCheckTask
     */
    public DatabaseCheckTask() {
        super(VIG_DB_NAME);
    }

    /**
     * initial the database check task
     * 
     * @see GeneralComponentCheckTask#init()
     */
    public void init() {
        /*
         * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 6/10/2008.
         * Collect more information for the error logs, then can provide more information for 
         * troubleshooting.
         */
        String thisMethod = thisClassname + ".init(): ";
        String thisStep = thisMethod + "begin";
        LOG.info(thisStep);
        
        /*
         * use the parameter of vignette default database
         */
        this.driver = PortalConfigUtils.getProperty("default.db.driver");
        this.url = PortalConfigUtils.getProperty("default.db.url");
        this.user = PortalConfigUtils.getProperty("default.db.user");
        this.password = PortalConfigUtils.getProperty("default.db.password");
        
        try {
            thisStep = thisMethod + "check driver exists";
            LOG.info(thisStep);
            Class.forName(driver);
            initSuccess = true;
        } catch (Exception e) {
            LOG.error(thisStep + ": driver: " + driver
                      + ", url: " + url + ", user: " + user
                      + ", caught: " + e);
        }
        thisStep = thisMethod + "end";
        LOG.info(thisStep);
    }

    /**
     * test the health of vignette database
     * 
     * @see IComponentCheckTask#run()
     */
    public void run() {
        /*
         * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 6/10/2008.
         * Collect more information for the error logs, then can provide more information for 
         * troubleshooting.
         */
        String thisMethod = thisClassname + ".run(): ";
        String thisStep = thisMethod + "begin";
        LOG.info(thisStep);
        
        Connection con = null;
        Statement stmt = null;

        int tmpStatus = STATUS_FAIL; //STATUS_FAIL means failing of vig db
        long beginTime = System.currentTimeMillis(); //the current time
        
        try {
            if (!initSuccess) {
                throw new SQLException("database driver have not been loaded.");
            }
            
            thisStep = thisMethod + "get connection";
            LOG.info(thisStep);
            // connect the database
            con = DriverManager.getConnection(url, user, password);
            
            thisStep = thisMethod + "create statement";
            LOG.info(thisStep);
            stmt = con.createStatement();
            
            thisStep = thisMethod + "execute query";
            LOG.info(thisStep);
            // execute the SQL
            stmt.executeQuery(QUERY_STRING);

            tmpStatus = STATUS_PASS; //STATUS_FAIL means failing of vig db
        } catch (Exception e) {
            LOG.error(thisStep + ": driver: " + driver 
                    + ", url: " + url
                    + ", user: " + user 
                    + ", query: " + QUERY_STRING 
                    + ", caught: " + e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                LOG.error(e.toString());
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                LOG.error(e.toString());
            }
        }
        
        status = tmpStatus;
        responseTime = System.currentTimeMillis() - beginTime;
        
        thisStep = thisMethod + "end";
        LOG.info (thisStep);
    }

}
