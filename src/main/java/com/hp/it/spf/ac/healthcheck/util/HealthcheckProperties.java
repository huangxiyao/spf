/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.util;

import java.lang.String;
import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.ClassLoader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContext;

import com.hp.bco.pl.wpa.util.Environment;
import com.hp.it.spf.ac.status.ClosedStatus;
import com.hp.it.spf.ac.status.DownStatus;
import com.hp.it.spf.ac.status.HealthcheckStatus;
import com.hp.it.spf.ac.status.OpenStatus;


/**
 * Singleton class for the healthcheck application properties, loaded from the
 * <code>healthcheck.properties</code> file. Use the <code>instance</code>
 * method to get the singleton reference, then use the various getter methods to
 * obtain the configure property values from <code>healthcheck.properties</code>
 * (with reasonable defaults when properties are undefined or defined with
 * invalid values).
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class HealthcheckProperties {

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    private static String ENABLED_KEY = "enabled";

    private static String INITIAL_DELAY_KEY = "delay.initial";

    private static String REPEAT_DELAY_KEY = "delay.repeat";

    private static String PORTAL_HOST_KEY = "portal-pulse.request.host";

    private static String PORTAL_PORT_KEY = "portal-pulse.request.port";

    private static String PORTAL_URL_KEY = "portal-pulse.request.url";

    private static String PORTAL_BYTES_KEY = "portal-pulse.response.bytes";

    private static String PORTAL_PATTERN_KEY = "portal-pulse.response.pattern";

    private static String PORTAL_CLASS_KEY = "portal-pulse.response.class";

    private static String PORTAL_TIMEOUT_KEY = "portal-pulse.timeout";

    private static String PORTAL_COOKIES_KEY = "portal-pulse.cookies";

    private static String OPEN_SIGN_HOST_KEY = "open-sign.request.host";

    private static String OPEN_SIGN_PORT_KEY = "open-sign.request.port";

    private static String OPEN_SIGN_URL_KEY = "open-sign.request.url";

    private static String OPEN_SIGN_TIMEOUT_KEY = "open-sign.timeout";

    private static String THRESHOLD_KEY = "threshold.";

    private static String LOG_BYTES_KEY = "log.bytes";

    private static int DISABLED_NUMBER = -1;

    private static int MIN_REPEAT_DELAY = 5;

    private static String ENABLED_STRING = "yes";

    private static String DISABLED_STRING = "no";

    private static boolean DEFAULT_ENABLED = false;

    private static int DEFAULT_LOG_BYTES = DISABLED_NUMBER;

    private static int DEFAULT_PORTAL_BYTES = DISABLED_NUMBER;

    private static int DEFAULT_INITIAL_DELAY = 0;

    private static int DEFAULT_REPEAT_DELAY = 60;

    private static int DEFAULT_THRESHOLD = 1;

    private static int DEFAULT_PORT = 7001;

    private static int DEFAULT_PORTAL_TIMEOUT = 10;

    private static int DEFAULT_OPEN_SIGN_TIMEOUT = 10;

    private static String DEFAULT_HOST = "localhost";

    private static String DEFAULT_PORTAL_URL = "/";

    private static String DEFAULT_PORTAL_PATTERN = "^HTTP/1\\.. 200 .*$";

    private static String DEFAULT_PORTAL_CLASS = null;

    private static boolean DEFAULT_PORTAL_COOKIES = true;

    private static String DEFAULT_OPEN_SIGN_URL = "/open-sign/isOpen";

    private static String PROPERTIES_FILE = "healthcheck.properties";

    private static String DEFAULT_PROPERTIES_PATH = "/WEB-INF/services/healthcheck/properties/";

    // The singleton object reference.
    private static HealthcheckProperties singleton = null;

    // The healthcheck properties object.
    private Properties properties = null;

    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * Returns the previously-loaded reference to this object. If the object has
     * not previously been loaded from the <code>healthcheck.properties</code>
     * file, this method also forces a load as a side-effect. In that case,
     * <code>healthcheck.properties</code> is searched-for in the classpath,
     * and if not found anywhere in the classpath, then it is searched-for in
     * the healthcheck service properties directory inside the healthcheck Web
     * application (ie, <code>/WEB-INF/services/healthcheck/properties</code>).
     * If it is found nowhere, then default properties are assumed.
     */
    public static HealthcheckProperties instance() {

        return instance(false);

    } // end method instance

    /**
     * <p>
     * Returns either the previously-loaded reference to this object, or a
     * newly- loaded reference, depending on the given parameter. If the object
     * has not already been loaded from the <code>healthcheck.properties</code>
     * file, or the given parameter is true, then this method forces a load as a
     * side-effect. In that case, <code>healthcheck.properties</code> is
     * searched-for in the classpath, and if not found anywhere in the
     * classpath, then it is searched-for in the healthcheck service properties
     * directory inside the healthcheck Web application (ie,
     * <code>/WEB-INF/services/healthcheck/properties</code>). If it is found
     * nowhere, then default properties are assumed.
     * </p>
     * 
     * <b>Note:</b> Forcing a reload by setting the parameter true, does not
     * affect any previously-returned references. They will continue to refer to
     * the previously-loaded object, while the reference returned by this
     * invokation refers to the newly-loaded object.
     */
    public static HealthcheckProperties instance(boolean reload) {

        if ((singleton == null) || reload)
            singleton = new HealthcheckProperties();
        return singleton;

    } // end method instance

    /**
     * Returns the maximum number of characters to allow in the "message"
     * portion of a healthcheck log record. This value is obtained from the
     * <code>log.bytes</code> property in the
     * <code>healthcheck.properties</code> file. <i>-1</i> means unlimited.
     * If it is not defined in that file as a positive integer, then a default
     * value of <i>-1</i> is returned.
     * <p>
     * 
     * Note: this maximum message length applies only against the "message"
     * portion of a healthcheck log record. It does not count against the
     * timestamp or classname.
     */
    public int getLogMessageLimit() {

        int max = DEFAULT_LOG_BYTES;
        String value = this.lookupValue(LOG_BYTES_KEY);
        if (value != null) {
            try {
                max = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                max = DEFAULT_LOG_BYTES;
            }
            if (max <= 0) {
                max = DEFAULT_LOG_BYTES;
            }
        }
        return max;

    } // end method getLogMessageLimit

    /**
     * Returns the delay, in seconds, between startup and the first healthcheck.
     * This value is obtained from the <code>delay.initial</code> property in
     * the <code>healthcheck.properties</code> file. If it is not defined in
     * that file as a positive integer, then a default value of <i>0</i> is
     * returned.
     */
    public int getInitialDelay() {

        int initialDelay = DEFAULT_INITIAL_DELAY;
        String value = this.lookupValue(INITIAL_DELAY_KEY);
        if (value != null) {
            try {
                initialDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                initialDelay = DEFAULT_INITIAL_DELAY;
            }
            if (initialDelay <= 0) {
                initialDelay = DEFAULT_INITIAL_DELAY;
            }
        }
        return initialDelay;

    } // end method getInitialDelay

    /**
     * Returns the delay, in seconds, between repeating healthchecks. This value
     * is obtained from the <code>delay.r</code> property in the
     * <code>healthcheck.properties</code> file. The minimum valid value is 5
     * seconds. If a valid value is not defined in the properties file, then a
     * default value of <i>60</i> is returned.
     */
    public int getRepeatDelay() {

        int repeatDelay = DEFAULT_REPEAT_DELAY;
        String value = this.lookupValue(REPEAT_DELAY_KEY);
        if (value != null) {
            try {
                repeatDelay = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                repeatDelay = DEFAULT_REPEAT_DELAY;
            }
            if (repeatDelay < MIN_REPEAT_DELAY) {
                repeatDelay = DEFAULT_REPEAT_DELAY;
            }
        }
        return repeatDelay;

    } // end method getSuccessiveDelay

    /**
     * Returns the timeout, in seconds, for the portal pulse healthcheck. This
     * value is obtained from the <code>portal-pulse.timeout</code> property
     * in the <code>healthcheck.properties</code> file. Valid values are any
     * positive integer, or <code>-1</code> to indicate no timeout. If no
     * valid value was defined in the properties file, then a default value (<i>10</i>
     * seconds) is returned.
     */
    public int getPortalPulseTimeout() {

        int portalTimeout = DEFAULT_PORTAL_TIMEOUT;
        String value = this.lookupValue(PORTAL_TIMEOUT_KEY);
        if (value != null) {
            try {
                portalTimeout = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                portalTimeout = DEFAULT_PORTAL_TIMEOUT;
            }
            if ((portalTimeout <= 0) && (portalTimeout != DISABLED_NUMBER)) {
                portalTimeout = DEFAULT_PORTAL_TIMEOUT;
            }
        }
        return portalTimeout;

    } // end method getPortalPulseTimeout

    /**
     * Returns the timeout, in seconds, for the open sign healthcheck. This
     * value is obtained from the <code>open-sign.timeout</code> property in
     * the <code>healthcheck.properties</code> file. Valid values are any
     * positive integer, or <code>-1</code> to indicate no timeout. If no
     * valid value was defined in the properties file, then a default value (<i>10</i>
     * seconds) is returned.
     */
    public int getOpenSignTimeout() {

        int openSignTimeout = DEFAULT_OPEN_SIGN_TIMEOUT;
        String value = this.lookupValue(OPEN_SIGN_TIMEOUT_KEY);
        if (value != null) {
            try {
                openSignTimeout = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                openSignTimeout = DEFAULT_OPEN_SIGN_TIMEOUT;
            }
            if ((openSignTimeout <= 0) && (openSignTimeout != DISABLED_NUMBER)) {
                openSignTimeout = DEFAULT_OPEN_SIGN_TIMEOUT;
            }
        }
        return openSignTimeout;

    } // end method getOpenSignTimeout

    /**
     * Returns the number of successive healthchecks resulting in a given status
     * which must occur, before that status will take effect. The given status
     * indicates the particular threshold number to return - eg for any given
     * <code>OpenStatus</code>, the open threshold will be returned. By
     * default, the null threshold is returned (an undefined value).
     * <p>
     * 
     * In any case, the threshold values for different types of
     * <code>HealthcheckStatus</code> are obtained from the various
     * <code>threshold.<i>&lt;status-name&gt;</i></code> properties in the
     * <code>environment.properties</code> file. Valid values are any positive
     * integer (thus <code>2</code> means to wait for 2 consecutive results of
     * the given <code>HealthcheckStatus</code> type, while <code>1</code>
     * means to not wait, etc). If no valid value was defined in the properties
     * file for the given <code>HealthcheckStatus</code> type, then a default
     * value (<code>1</code>) is returned.
     */
    public int getThreshold(HealthcheckStatus hcs) {

        int threshold = DEFAULT_THRESHOLD;
        String key;
        String value;

        if (hcs != null) {
            key = THRESHOLD_KEY + hcs.getSimpleName();
            value = this.lookupValue(key);
            if (value != null) {
                try {
                    threshold = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    threshold = DEFAULT_THRESHOLD;
                }
                if (threshold <= 0) {
                    threshold = DEFAULT_THRESHOLD;
                }
            }
        }
        return threshold;

    } // end method getThreshold

    /**
     * Returns true if healthchecking is enabled, false otherwise. This value is
     * obtained from the <code>enabled</code> property in the
     * <code>healthcheck.properties</code> file. If no valid value was defined
     * in the properties file, then false is returned.
     */
    public boolean areHealthchecksEnabled() {

        boolean enabled = DEFAULT_ENABLED;
        String value = this.lookupValue(ENABLED_KEY);
        if (ENABLED_STRING.equalsIgnoreCase(value)) {
            enabled = true;
        } else if (DISABLED_STRING.equalsIgnoreCase(value)) {
            enabled = false;
        }
        return enabled;

    } // end method areHealthchecksEnabled

    /**
     * Returns true if cookie management is enabled for the portal pulse page
     * healthcheck, or false otherwise. Cookie management means that the
     * healthcheck driver will look for any cookies sent in the response from
     * the portal pulse page, and re-send them in the next request. A false
     * value means that any cookies sent by the portal pulse page will be
     * ignored by the healthcheck driver.
     * <p>
     * This value is obtained from the <code>portal-pulse.cookies</code>
     * property in the <code>healthcheck.properties</code> file. If no valid
     * value was defined in the properties file, then true is returned.
     */
    public boolean managePortalPulseCookies() {

        boolean enabled = DEFAULT_PORTAL_COOKIES;
        String value = this.lookupValue(PORTAL_COOKIES_KEY);
        if (ENABLED_STRING.equalsIgnoreCase(value)) {
            enabled = true;
        } else if (DISABLED_STRING.equalsIgnoreCase(value)) {
            enabled = false;
        }
        return enabled;

    } // end method managePortalPulseCookies

    /**
     * Returns the hostname (eg domain name) or IP address of the server against
     * which the portal pulse healthcheck should be requested. It could be the
     * value for the portal server, or it could be the value for a proxy server.
     * <p>
     * 
     * This value is obtained from the <code>portal-pulse.request.host</code>
     * property in the <code>healthcheck.properties</code> file. Note: this
     * method does not validate the property value at all, but just returns it
     * (or <code>localhost</code> if the property was not defined).
     */
    public String getPortalPulseHost() {

        String host = DEFAULT_HOST;
        String value = this.lookupValue(PORTAL_HOST_KEY);
        if ((value != null) && (!"".equals(value))) {
            host = value;
        }
        return host;

    } // end method getPortalPulseHost

    /**
     * Returns the hostname (eg domain name) or IP address of the server against
     * which the open-sign healthcheck should be requested. It could be the
     * value for the open sign server, or it could be the value for a proxy
     * server.
     * <p>
     * 
     * This value is obtained from the <code>open-sign.request.host</code>
     * property in the <code>healthcheck.properties</code> file. Note: this
     * method does not validate the property value at all, but just returns it
     * (or <code>localhost</code> if the property was not defined).
     */
    public String getOpenSignHost() {

        String host = DEFAULT_HOST;
        String value = this.lookupValue(OPEN_SIGN_HOST_KEY);
        if ((value != null) && (!"".equals(value))) {
            host = value;
        }
        return host;

    } // end method getOpenSignHost

    /**
     * Returns the port number of the server against which the portal pulse
     * healthcheck should be requested. It could be the port for the portal
     * server, or it could be the port of a proxy server.
     * <p>
     * 
     * This value is obtained from the <code>portal-pulse.request.port</code>
     * property in the <code>healthcheck.properties</code> file. If it is not
     * defined in that file as a positive integer, then <code>7001</code> is
     * returned.
     */
    public int getPortalPulsePort() {

        int port = DEFAULT_PORT;
        String value = this.lookupValue(PORTAL_PORT_KEY);
        if (value != null) {
            try {
                port = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                port = DEFAULT_PORT;
            }
            if (port <= 0) {
                port = DEFAULT_PORT;
            }
        }
        return port;

    } // end method getPortalPulsePort

    /**
     * Returns the port number of the server against which the open sign
     * healthcheck should be requested. It could be the port for the open sign
     * server, or it could be the port of a proxy server.
     * <p>
     * 
     * This value is obtained from the <code>open-sign.request.port</code>
     * property in the <code>healthcheck.properties</code> file. If it is not
     * defined in that file as a positive integer, then <code>7001</code> is
     * returned.
     */
    public int getOpenSignPort() {

        int port = DEFAULT_PORT;
        String value = this.lookupValue(OPEN_SIGN_PORT_KEY);
        if (value != null) {
            try {
                port = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                port = DEFAULT_PORT;
            }
            if (port <= 0) {
                port = DEFAULT_PORT;
            }
        }
        return port;

    } // end method getOpenSignPort

    /**
     * Returns the URL for the portal pulse healthcheck to be requested from the
     * portal pulse host and port. This may be a relative URL, giving the path
     * and query string (if any) for the portal pulse page on the portal server.
     * Or it may be an absolute URL, containing the portal pulse host and port.
     * <p>
     * 
     * In either case, this value is obtained from the
     * <code>portal-pulse.request.url</code> property in the
     * <code>healthcheck.properties</code> file. Note: this method does not
     * validate the property value at all, but just returns it (or
     * <code>/</code> if the property was not defined).
     */
    public String getPortalPulseUrl() {

        String url = DEFAULT_PORTAL_URL;
        String value = this.lookupValue(PORTAL_URL_KEY);
        if ((value != null) && (!"".equals(value))) {
            url = value;
        }
        return url;

    } // end method getPortalPulseUrl

    /**
     * Returns the URL for the open sign healthcheck to be requested from the
     * open sign host and port. This may be a relative URL, giving the path and
     * query string (if any) for the open sign page on the open sign server. Or
     * it may be an absolute URL, containing the open sign host and port.
     * <p>
     * 
     * In either case, this value is obtained from the
     * <code>open-sign.request.url</code> property in the
     * <code>healthcheck.properties</code> file. Note: this method does not
     * validate the property value at all, but just returns it (or
     * <code>/open-sign/isOpen</code> if the property was not defined).
     */
    public String getOpenSignUrl() {

        String url = DEFAULT_OPEN_SIGN_URL;
        String value = this.lookupValue(OPEN_SIGN_URL_KEY);
        if ((value != null) && (!"".equals(value))) {
            url = value;
        }
        return url;

    } // end method getOpenSignUrl

    /**
     * Returns the Java regular expression to be used when evaluating the portal
     * pulse response. Assuming PortalPulseHealthcheckClient is being used, then
     * if any part of any line in the response (up to the byte limit - see
     * getPortalPulseLimit) matches that regular expression, then the portal
     * pulse healthcheck is considered to have succeeded by the
     * HealthcheckDriver (see); otherwise it is considered to have failed. (If a
     * custom subclass of HealthcheckClient is being used as the portal pulse
     * page healthchecker, then the pattern is treated according to the custom
     * evaluate logic in that custom subclass.)
     * <p>
     * 
     * The pattern value is obtained from the
     * <code>portal-pulse.response.pattern</code> property in the
     * <code>healthcheck.properties</code> file. Note: this method does not
     * validate the property value at all, but just returns it. If the property
     * was not defined, a default pattern (which matches any HTTP status 200
     * response) is returned: <code>^HTTP/1\.. 200 .*$</code>
     */
    public String getPortalPulsePattern() {

        String pattern = DEFAULT_PORTAL_PATTERN;
        String value = this.lookupValue(PORTAL_PATTERN_KEY);
        if ((value != null) && (!"".equals(value))) {
            pattern = value;
        }
        return pattern;

    } // end method getPortalPulsePattern

    /**
     * Returns the name of the custom Java class to be used when evaluating the
     * portal pulse response. This overrides the default evaluator which relies
     * on the portal pulse pattern (see getPortalPulsePattern).
     * <p>
     * 
     * The classname is obtained from the
     * <code>portal-pulse.response.class</code> property in the
     * <code>healthcheck.properties</code> file. Note: this method does not
     * validate the property value at all, but just returns it. If the property
     * was not defined, <code>null</code> is returned.
     */
    public String getPortalPulseClass() {

        String name = DEFAULT_PORTAL_CLASS;
        String value = this.lookupValue(PORTAL_CLASS_KEY);
        if ((value != null) && (!"".equals(value))) {
            name = value;
        }
        return name;

    } // end method getPortalPulseClass

    /**
     * Returns the maximum number of bytes to read and evaluate in a portal
     * pulse response. This value is obtained from the
     * <code>portal-pulse.response.bytes</code> property in the
     * <code>healthcheck.properties</code> file. <i>-1</i> means unlimited.
     * If it is not defined in that file as a positive integer, then a default
     * value of <i>-1</i> is returned.
     * <p>
     */
    public int getPortalPulseLimit() {

        int max = DEFAULT_PORTAL_BYTES;
        String value = this.lookupValue(PORTAL_BYTES_KEY);
        if (value != null) {
            try {
                max = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                max = DEFAULT_PORTAL_BYTES;
            }
            if (max <= 0) {
                max = DEFAULT_PORTAL_BYTES;
            }
        }
        return max;

    } // end method getLogMessageLimit

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
    private HealthcheckProperties() {

        String errMsg = this.getClass().getName();
        InputStream in;
        ServletContext sc;

        in = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE);
        if (in != null) {
            System.out.println(errMsg + ": Found healthcheck properties file ["
                    + PROPERTIES_FILE + "] on classpath.  Loading.");
        } else {
            System.out.println(errMsg
                    + ": Unable to load healthcheck properties from file ["
                    + PROPERTIES_FILE + "] on classpath.  Reverting to ["
                    + DEFAULT_PROPERTIES_PATH + PROPERTIES_FILE
                    + "] within this Web application (if any).");
            sc = Environment.getInstance().getContext();
            if (sc != null)
                in = sc.getResourceAsStream(DEFAULT_PROPERTIES_PATH
                        + PROPERTIES_FILE);
            if (in != null)
                System.out.println(errMsg
                        + ": Found healthcheck properties file ["
                        + DEFAULT_PROPERTIES_PATH + PROPERTIES_FILE
                        + "] inside this Web application.  Loading.");
            else
                System.out.println(errMsg
                        + ": No such file.  Reverting to internal defaults.");
        }
        if (in != null) {
            this.properties = new Properties();
            try {
                this.properties.load(in);
            } catch (IOException e) {
                System.out.println(errMsg + ": IOException: [" + e.getMessage()
                        + "].  Reverting to internal defaults.");
            }
        }
        this.printConfiguration();

    } // end constructor

    protected void printConfiguration() {

        // Following System.out.println's to be converted to Log4J or similar
        // debug logging at a future date - DSJ 2006/11/29

        System.out.println(this.getClass().getName()
                + ": Loaded the following configuration:");
        System.out.println("--- enabled: [" + this.areHealthchecksEnabled()
                + "]");
        System.out.println("--- initial delay: [" + this.getInitialDelay()
                + "]");
        System.out.println("--- repeat delay: [" + this.getRepeatDelay() + "]");
        System.out.println("--- repeated-open threshold: ["
                + this.getThreshold(new OpenStatus()) + "]");
        System.out.println("--- repeated-closed threshold: ["
                + this.getThreshold(new ClosedStatus()) + "]");
        System.out.println("--- repeated-down threshold: ["
                + this.getThreshold(new DownStatus()) + "]");
        System.out.println("--- log message limit: ["
                + this.getLogMessageLimit() + "]");
        System.out.println("--- portal pulse host: ["
                + this.getPortalPulseHost() + "]");
        System.out.println("--- portal pulse port: ["
                + this.getPortalPulsePort() + "]");
        System.out.println("--- portal pulse URL: [" + this.getPortalPulseUrl()
                + "]");
        System.out.println("--- portal pulse timeout: ["
                + this.getPortalPulseTimeout() + "]");
        System.out.println("--- portal pulse success pattern: ["
                + this.getPortalPulsePattern() + "]");
        System.out.println("--- portal pulse response limit: ["
                + this.getPortalPulseLimit() + "]");
        System.out.println("--- portal pulse client class: ["
                + this.getPortalPulseClass() + "]");
        System.out.println("--- open sign host: [" + this.getOpenSignHost()
                + "]");
        System.out.println("--- open sign port: [" + this.getOpenSignPort()
                + "]");
        System.out
                .println("--- open sign URL: [" + this.getOpenSignUrl() + "]");
        System.out.println("--- open sign timeout: ["
                + this.getOpenSignTimeout() + "]");

    } // end method printConfiguration

    private String lookupValue(String key) {

        String value = null;
        if (this.properties != null)
            value = this.properties.getProperty(key);
        if (value != null)
            value = value.trim();
        return value;

    } // end method lookupValue

} // end class
