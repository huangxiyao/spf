package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import java.util.HashMap;
import java.util.Map;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;
import com.vignette.portal.log.LogWrapper;

/**
 * A class for monitoring the health of HPP webservices.
 * 
 * @author <link href="hao.zhang2@hp.com"> Zhang, Hao </link>
 * @version TBD
 * @see GeneralComponentCheckTask
 * @see LogWrapper
 * @see PassportService
 * @see PassportParametersManager
 */
public class HPPWebServiceCheckTask extends GeneralComponentCheckTask {

    /**
     * serialVersionUID
     * long
     */
    private static final long serialVersionUID = 1226125392530686357L;

    private static final String HPP_WEBSERVICE_NAME = 
        "HPP Web Service"; // the name of HPP webservice
    /**
     * params read from the config file of healthcheck.xml
     * added by ck for CR: 1000813522 
     */
    private Map params = new HashMap();
    /*
     * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 5/29/2008.
     * Collect more information for the error logs, then can provide more information for 
     * troubleshooting.
     */
    // current class name 
    private static String thisClassname = HPPWebServiceCheckTask.class.getName();
    
    /**
     * the log for vignette when throwing exception
     */
    private static final LogWrapper LOG = new LogWrapper(
            HPPWebServiceCheckTask.class);
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
     * constructor for HPPWebServiceCheckTask
     */
    public HPPWebServiceCheckTask() {
        super(HPP_WEBSERVICE_NAME);
    }

    /**
     * test the HPP webservice,now just test the login service
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
        
        int tmpStatus = STATUS_FAIL; // STATUS_FAIL means failing of the HPP ws
        long beginTime = System.currentTimeMillis(); // the current time
        
        try {
            thisStep = thisMethod + "get Web service";
            LOG.info(thisStep);
            // the instance of new webservice
            PassportService ws = new PassportService();

            thisStep = thisMethod + "get Web service parameters";
            LOG.info(thisStep);
            // login using admin account
            PassportParametersManager wsManagerInstance = PassportParametersManager
                    .getInstance();
            String adminUserName = wsManagerInstance.getAdminUser();
            String adminUserPWD = wsManagerInstance.getAdminPassword();

            thisStep = thisMethod + "execute Web service test: adminUserName: " + adminUserName;
            LOG.info(thisStep);
            ws.login(adminUserName, adminUserPWD);

            tmpStatus = STATUS_PASS; // STATUS_PASS means failing of the HPP ws
        } catch (Exception e) {
            LOG.error(thisStep  + ", caught: " + e);
        }
        
        status = tmpStatus;
        responseTime = System.currentTimeMillis() - beginTime;
        
        thisStep = thisMethod + "end";
        LOG.info (thisStep);
    }
}