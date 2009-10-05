package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.vignette.portal.log.LogWrapper;

/**
 * The web page availability check task is a simple J2SE class. Its purpose is
 * to check the availability of a web page.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @version TBD
 */
public class WebPageCheckTask extends GeneralComponentCheckTask {

    /**
     * serialVersionUID
     * long
     */
    private static final long serialVersionUID = 883230842692786187L;

    /**
     * the log object from vignette used for logging in case exceptions happen.
     */
    private static final LogWrapper LOG = new LogWrapper(WebPageCheckTask.class);
    
    /*
     * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 6/10/2008.
     * Collect more information for the error logs, then can provide more information for 
     * troubleshooting.
     */
    // current class name 
    private static String thisClassname = WebPageCheckTask.class.getName();

    /*
     * the minimum length of a valid url, this is used to check if the url is
     * valid.
     */
    private final int minURLLength = 7;

    /*
     * params read from the config file of healthcheck.xml
     */
    private Map params = new HashMap();

    /**
     * Construction method for WebPageCheckTask It passes the name to its
     * parent's construction method simplily.
     * 
     * @param name
     *            used for getName() when necessary
     * @see IComponentCheckTask#getName()
     * @see GeneralComponentCheckTask#GeneralComponentCheckTask(String)
     */
    public WebPageCheckTask(String name) {
        super(name);
    }

    /**
     * Set the attribute params, like url, pattern, trustStore,
     * trustStorePassoword, and so on.
     * 
     * @param params
     *            attribute params like url, pattern, trustStore,
     *            trustStorePassoword, and so on
     */
    public void setParams(Map params) {
        this.params = params;
    }

    /**
     * Implement of IComponentCheckTask#init() It provide the trustStore and
     * trustStorePassowrd values if needed.
     * 
     * @see ComponentCheckTaskk#init()
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
        
        String trustStore = (String)params.get("trustStore");
        String trustStorePassword = (String)params.get("trustStorePassword");

        thisStep = thisMethod + "set ssl parameters and register protocol";
        LOG.info(thisStep);
        if (trustStore != null && trustStorePassword != null) { // set ssl
            // parameters
            System.setProperty("javax.net.sll.trustStore", trustStore);
            System.setProperty("javax.net.ssl.trustStorePassword",
                    trustStorePassword);
            Protocol myhttps = new Protocol("https",
                    new ExSecureProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", myhttps);
        } else {
            LOG.info(thisStep + ": no ssl configuration");
        }
        thisStep = thisMethod + "end";
        LOG.info(thisStep);
    }

    /**
     * This method is invoked by health check page whenever checking the status
     * of a desired web page. It will get the content of the destination web
     * url, and check if the content contains pattern string(if configed in the
     * configuration file), then set the status and response time at last.
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
        
        String url = (String)params.get("url");
        
        thisStep = thisMethod + "check url parameter";
        LOG.info(thisStep);
        if (url == null || url.length() < minURLLength) { // check if the url
            // parameter is valid
            status = STATUS_FAIL;
            thisStep = thisMethod + "end";
            LOG.info(thisStep);
            return;
        }
        String pattern = (String)params.get("pattern");

        HttpClient httpClient = null;
        PostMethod postMethod = null;
        GetMethod redirectMethod = null;

        int tmpStatus = STATUS_FAIL;
        long beginTime = System.currentTimeMillis();

        try {
            // add 2 cookies
            /*
             * HttpState initialState = new HttpState(); Cookie cookie0 = new
             * Cookie(".hp.com", "HP_SP_SET_REMEMBER_USER_FLAG", "0", "/", 365 *
             * 24 * 3600, false); initialState.addCookie(cookie0); String user =
             * (String)params.get("USERNAME"); if (user == null) user = "";
             * Cookie cookie1 = new Cookie(".hp.com", "HP_SP_ENTER_USERID",
             * user, "/", 1 * 24 * 3600, false);
             * initialState.addCookie(cookie1);
             */// removed by Gao, Hua-Kun 2006/11/30
            /*
             * Added by xie xingxing(xxie@hp.com) for QXCR1000813689 at 6/10/2008.
             * Collect more information for the error logs, then can provide more information for 
             * troubleshooting.
             */
            thisStep = thisMethod + "get HttpClient";
            LOG.info(thisStep);
            httpClient = new HttpClient();
            // httpClient.setState(initialState); //removed by Gao, Hua-Kun
            // 2006/11/30
            thisStep = thisMethod + "get PostMethod";
            LOG.info(thisStep);
            postMethod = new PostMethod(url);
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) { // add parameters to the postMethod object
                Map.Entry entry = (Map.Entry)it.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();
                if (!"url".equalsIgnoreCase(name)
                        && !"pattern".equalsIgnoreCase(name)
                        && !"trustStore".equalsIgnoreCase(name)
                        && !"trustStorePassword".equalsIgnoreCase(name)) {
                    postMethod.addParameter(name, value);
                }
            }

            // change the default cookie policy to CookiePolicy.NETSCAPE
            postMethod.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);

            thisStep = thisMethod + "execute HttpClient";
            LOG.info(thisStep);
            int statusCode = httpClient.executeMethod(postMethod);
            
            thisStep = thisMethod + "check response";
            LOG.info(thisStep);
            String redirectLocation;
            Header locationHeader = postMethod.getResponseHeader("location");
            String content = "";

            boolean redirect = false;
            // check if the response status code is a redirection code
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
                    || statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_SEE_OTHER
                    || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {
                redirect = true;
            }

            // if need direct, then do it, notice: post method need to do
            // redirection manualy.
            if (locationHeader != null && redirect) {
                redirectLocation = locationHeader.getValue();
                postMethod.releaseConnection();
                postMethod = null;
                /*
                 * Added by xie xingxing(xxie@hp.com) for QXCR1000813689 at 6/10/2008.
                 * Collect more information for the error logs, then can provide more information for 
                 * troubleshooting.
                 */
                thisStep = thisMethod + "redirected: get GetMethod";
                LOG.info(thisStep);
                redirectMethod = new GetMethod(redirectLocation);
                redirectMethod.getParams().setCookiePolicy(
                        CookiePolicy.NETSCAPE);
                
                thisStep = thisMethod + "execute HttpClient";
                LOG.info(thisStep);
                statusCode = httpClient.executeMethod(redirectMethod);
                byte[] responseBody = redirectMethod.getResponseBody();
                content = new String(responseBody);
            } else if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = postMethod.getResponseBody();
                content = new String(responseBody);
            } else {
                LOG.error("can't get the return content, response code: "
                        + statusCode);
            }

            if (pattern != null && pattern.length() > 0) { // check the return
                // content if has the desired pattern
                content = content.trim().toLowerCase();
                pattern = pattern.toLowerCase();
                if (content.indexOf(pattern) >= 0) {
                    tmpStatus = STATUS_PASS;
                } else {
                    LOG.error("can't get the expected string: " + pattern);
                    // LOG.error("can't get the expected string \"" + pattern +
                    // "\" in " + content);
                }
            } else {
                tmpStatus = STATUS_PASS;
            }

        } catch (Exception e) {
            LOG.error(thisStep  + ": url: " + url 
                    + ", pattern: " + pattern
                    + ", httpClient: " + httpClient 
                    + ", postMethod: " + postMethod
                    + ", redirectMethod: " + redirectMethod
                    + ", caught: " + e);
        } finally {
            try {
                if (postMethod != null) { // postMethod should be released if
                    // it is not null.
                    postMethod.releaseConnection();
                    postMethod = null;
                }
            } catch (Exception e) {
                LOG.error(e.toString());
            }
            try {
                if (redirectMethod != null) { // redirectMethod should be
                    // released if it is not null.
                    redirectMethod.releaseConnection();
                    redirectMethod = null;
                }

            } catch (Exception e) {
                LOG.error(e.toString());
            }
        }

        // update status and responseTime
        status = tmpStatus;
        responseTime = System.currentTimeMillis() - beginTime;
        
        thisStep = thisMethod + "end";
        LOG.info (thisStep);
    }
}
