/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.portal.filter;

import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.ClassLoader;

import java.io.InputStream;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Properties;
import javax.servlet.FilterConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A container class for storing the admission control filter configuration in
 * more-easily-accessible fashion.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class AdmissionControlFilterConfig {

    // ///////////////////////////////////////////////////////////
    // PRIVATE STATIC ATTRIBUTES
    // ///////////////////////////////////////////////////////////

	private static Log LOG = LogFactory.getLog(AdmissionControlFilterConfig.class);

    private static Integer DEFAULT_CLOSING_GRACE = new Integer(0);

    private static Integer DEFAULT_CLOSED_GRACE = new Integer(0);

    private static String DEFAULT_HEALTHCHECK_ROOT = "/healthcheck";

    private static String DEFAULT_PRIV_CLIENT_SESSION_KEY = "com.hp.ac.privileged";

    private static String DEFAULT_ADMITTED_CLIENT_SESSION_KEY = "com.hp.ac.admitted";

    private static String DEFAULT_ADMITTED_CLIENT_QUERY_KEY = "ac.admitted";

    private static String DEFAULT_CLOSING_TIME_REQUEST_KEY = "com.hp.ac.closingTime";

    // Use the SiteMinder / HP Passport TARGET query key as the default.
    private static String DEFAULT_SSO_TARGET_QUERY_KEY = "TARGET";

    private static String ADMITTED_CLIENT_SESSION_KEY_PARAM = "admittedClientSessionKey";

    private static String ADMITTED_CLIENT_QUERY_KEY_PARAM = "admittedClientQueryKey";

    private static String PRIV_CLIENT_SESSION_KEY_PARAM = "privilegedClientSessionKey";

    private static String CLOSING_TIME_REQUEST_KEY_PARAM = "closingTimeRequestKey";

    private static String SSO_TARGET_QUERY_KEY_PARAM = "ssoTargetQueryKey";

    private static String POOL_HOSTNAMES_PARAM = "poolHostnames";

    private static String THIS_SITE_HOSTNAMES_PARAM = "thisSiteHostnames";

    private static String OTHER_SITE_HOSTNAMES_PARAM = "otherSiteHostnames";

    private static String SITE_COOKIE_NAME_PARAM = "siteCookieName";

    private static String SITE_COOKIE_VALUE_PARAM = "siteCookieValue";

    private static String SITE_COOKIE_DOMAIN_PARAM = "siteCookieDomain";

    private static String SITE_COOKIE_PATH_PARAM = "siteCookiePath";

    private static String PRIV_CLIENT_ADDRS_PARAM = "privilegedClientAddresses";

    private static String PRIV_CLIENT_QUERY_KEY_PARAM = "privilegedClientQueryKey";

    private static String SITE_CLOSING_GRACE_PARAM = "siteClosingGracePeriod";

    private static String SITE_CLOSED_GRACE_PARAM = "siteClosedGracePeriod";

    private static String DEFAULT_POOL_PARAM = "defaultPool";

    private static String HEALTHCHECK_ROOT_PARAM = "healthcheckRoot";

    private static String WILDCARD_POOL_ID = "*";

    private static String PROPERTIES_FILE = "ac-filter.properties";

    // ///////////////////////////////////////////////////////////
    // PRIVATE INSTANCE ATTRIBUTES
    // ///////////////////////////////////////////////////////////

    private String defaultPool;

    private String healthcheckRoot;

    private String privilegedClientSessionKey;

    private String closingTimeRequestKey;

    private String admittedClientSessionKey;

    private String admittedClientQueryKey;

    private String ssoTargetQueryKey;

    // This is a <code>String hostname</code> to <code>String poolID</code> map.
    // It may include the wildcard pool ID, "*".
    private HashMap hostnameToPool;

    // This is a <code>String poolID</code> to <code>String
    // primaryPoolHostname</code> map. It may include the wildcard pool ID, "*".
    private HashMap poolToPoolHostname;

    // This is a <code>String poolID</code> to <code>HashSet
    // poolHostnames</code> map. It may include the wildcard pool ID, "*".
    private HashMap poolToPoolHostnames;

    // This is a <code>String poolID</code> to <code>String
    // primaryThisSiteHostname</code> map. It may include the wildcard pool ID,
    // "*".
    private HashMap poolToThisSiteHostname;

    // This is a <code>String poolID</code> to <code>HashSet
    // thisSiteHostnames</code> map. It may include the wildcard pool ID, "*".
    private HashMap poolToThisSiteHostnames;

    // This is a <code>String poolID</code> to <code>HashSet
    // otherSiteHostnames</code> map. It may inclue the wildcard pool ID, "*".
    private HashMap poolToOtherSiteHostnames;

    // This is a <code>String poolID</code> to <code>String cookieName</code>
    // map. It may include the wildcard pool ID, "*".
    private HashMap poolToSiteCookieName;

    // This is a <code>String poolID</code> to <code>String cookieValue</code>
    // map. It may include the wildcard pool ID, "*".
    private HashMap poolToSiteCookieValue;

    // This is a <code>String poolID</code> to <code>String cookieDomain</code>
    // map. It may include the wildcard pool ID, "*".
    private HashMap poolToSiteCookieDomain;

    // This is a <code>String poolID</code> to <code>String cookiePath</code>
    // map. It may include the wildcard pool ID, "*".
    private HashMap poolToSiteCookiePath;

    // This is a <code>String poolID</code> to <code>String[]
    // addressPatterns</code> map. It may include the wildcard pool ID, "*".
    private HashMap poolToPrivilegedClientAddresses;

    // This is a <code>String poolID</code> to <code>String queryKey</code> map.
    // It may include the wildcard pool ID, "*".
    private HashMap poolToPrivilegedClientQueryKey;

    // This is a <code>String poolID</code> to <code>Integer seconds</code> map.
    // It may include the wildcard pool ID, "*".
    private HashMap poolToClosingGrace;

    // This is a <code>String poolID</code> to <code>Integer seconds</code> map.
    // It may include the wildcard pool ID, "*".
    private HashMap poolToClosedGrace;

    // ///////////////////////////////////////////////////////////
    // PUBLIC METHODS
    // ///////////////////////////////////////////////////////////
    /**
     * Loads the admission control filter properties from the given filter
     * configuration. This filter configuration (a set of name-value pairs) may
     * be defined as init params in the filter definition in the web.xml. Or,
     * such init params may be superceded by a Java properties file named
     * ac-filter.properties, located anywhere in the <code>CLASSPATH</code>.
     * Whether the properties are loaded from the properties file, or the init
     * params, their syntax/semantics are the same:
     * <p>
     * 
     * <table border="1">
     * <tr>
     * <th>Property Name</th>
     * <th>Property Value</th>
     * </tr>
     * <tr>
     * <td><i>admittedClientSessionKey</i></td>
     * <td>The session attribute name to use for recording that the remote
     * client is in a session - ie has already gone through poolwide load
     * balancing. Default is <code>com.hp.ac.admitted</code>.</td>
     * </tr>
     * <tr>
     * <td><i>admittedClientQueryKey</i></td>
     * <td>The query parameter name to use for recording that the remote client
     * is in a session - ie has already gone through poolwide load balancing.
     * Default is <code>ac.admitted</code>.</td>
     * </tr>
     * <tr>
     * <td><i>privilegedClientSessionKey</i></td>
     * <td>The session attribute name to use for recording that the remote
     * client is for a staff user - ie is to be treated as privileged for
     * purposes of admission control. Default is
     * <code>com.hp.ac.privileged</code>.</td>
     * </tr>
     * <tr>
     * <td><i>ssoTargetQueryKey</i></td>
     * <td>The query parameter name used by single-sign-on (if any) to record
     * the target URL. Default is <code>TARGET</code> (ie, what HP Passport /
     * SiteMinder WebAgent uses).</td>
     * </tr>
     * <tr>
     * <td><i>closingTimeRequestKey</i></td>
     * <td>The request attribute name to use for recording the closing time, if
     * any. Default is <code>com.hp.ac.closingTime</code>.</td>
     * </tr>
     * <tr>
     * <td><i>healthcheckRoot</i></td>
     * <td>The servlet context root path to use for accessing the healthcheck
     * application - ie the context root path under which the healthcheck
     * application has been deployed in the application server. Default is
     * <code>/healthcheck</code>.</td>
     * </tr>
     * <tr>
     * <td><i>defaultPool</i></td>
     * <td>The pool ID to assume by default, if the request from the remote
     * client did not contain a <code>Host</code> header defined in any of the
     * hostname properties (see below). There is no default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]poolHostnames</i></td>
     * <td>The comma-delimited list of pool hostnames for the particular pool
     * ID. The first listed hostname is considered the primary. The pool ID is
     * optional and useful only to distinguish 2+ pools from one another. There
     * is no default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]thisSiteHostnames</i></td>
     * <td>The comma-delimited list of site hostnames for the particular pool
     * ID which correspond to this site where the filter is running. The first
     * listed hostname is considered the primary. The pool ID is optional and
     * useful only to distinguish 2+ pools from one another. There is no
     * default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]otherSiteHostnames</i></td>
     * <td>The comma-delimited list of site hostnames for the particular pool
     * ID which correspond to other sites than the one here executing. The first
     * listed hostname is considered the primary. The pool ID is optional and
     * useful only to distinguish 2+ pools from one another. There is no
     * default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]siteCookieName</i></td>
     * <td>The name to use when setting a cookie which records which site is in
     * use (ie, this site), when in the particular pool ID. The pool ID is
     * optional and useful only to distinguish 2+ pools from one another. There
     * is no default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]siteCookieValue</i></td>
     * <td>The value to use when setting a cookie which records which site is
     * in use (ie, this site), when in the particular pool ID. The pool ID is
     * optional and useful only to distinguish 2+ pools from one another. The
     * primary site hostname for this site is the default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]siteCookieDomain</i></td>
     * <td>The domain to use when setting a cookie which records which site is
     * in use (ie, this site), when in the particular pool ID. The pool ID is
     * optional and useful only to distinguish 2+ pools from one another. The
     * primary site hostname for this site is the default.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]siteCookiePath</i></td>
     * <td>The path to use when setting a cookie which records which site is in
     * use (ie, this site), when in the particular pool ID. The pool ID is
     * optional and useful only to distinguish 2+ pools from one another. The
     * default is <code>/</code>.</td>
     * </tr>
     * <tr>
     * <td><i>[id.]privilegedClientAddresses</i></td>
     * <td>The comma-delimited list of IP addresses or leading substrings
     * thereof, of remote clients which should be considered staff users by
     * default, when in the particular pool ID. The pool ID is optional and
     * useful only to distinguish 2+ pools from one another. There is no
     * default.</td>
     * </tr>
     * <td><i>[id.]privilegedClientQueryKey</i></td>
     * <td>The name of a query parameter which, if it exists, controls whether
     * the remote client should be considered a staff user or not, when in the
     * particular pool ID. The pool ID is optional and useful only to
     * distinguish 2+ pools from one another. There is no default.</td>
     * </tr>
     * <td><i>[id.]siteClosingGracePeriod</i></td>
     * <td>The number of seconds to wait between the stopping of the open-sign
     * application and the entry of the site into the "closing" state, when in
     * the particular pool ID. The pool ID is optional and useful only to
     * distinguish 2+ pools from one another. Until then, it is still considered
     * "open". The default is zero.</td>
     * </tr>
     * <td><i>[id.]siteClosedGracePeriod</i></td>
     * <td>The number of seconds to wait between entry into the "closing"
     * state, and entry into the "closed" state, when in the particular pool ID.
     * The pool ID is optional and useful only to distinguish 2+ pools from one
     * another. During the interim, existing end-users will be allowed to
     * continue, but new end-users will be directed away. The default is zero -
     * meaning there is no "closing" state per se, the system goes directly into
     * the "closed" state.</td>
     * </tr>
     * </table>
     * <p>
     * 
     * Note: in each case, if a property is missing or unreadable, it is simply
     * silently not loaded. No startup error occurs, although the filter's
     * subsequent request handling may be erroneous.
     */
    protected AdmissionControlFilterConfig(FilterConfig fconfig) {

        Object properties = fconfig;
        Properties props;
        InputStream in;
        String errMsg;

        HashMap pools;
        Iterator entries;
        Map.Entry entry;
        String id, value, join;
        String name, domain, path, query;
        HashMap pool;
        String[] hostnames, addresses;
        Integer grace;
        int i;

        // Check whether to load from external properties file or FilterConfig
        // init params.

        errMsg = this.getClass().getName()
                + ": Unable to load admission control filter properties from file ["
                + PROPERTIES_FILE
                + "] on classpath.  Reverting to <init-param> set in web.xml (if any).";
        props = new Properties();
        in = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE);
        if (in == null) {
            LOG.warn(errMsg);
        } else {
            try {
                LOG.info(this.getClass().getName()
                        + ": Found admission control properties file ["
                        + PROPERTIES_FILE + "] on classpath.  Loading.");
                props.load(in);
                properties = props;
            } catch (Exception e) {
                LOG.warn(errMsg, e);
            }
        }

        // Load the defaultPool.

        this.defaultPool = this.normalize(this.getProperty(properties,
                DEFAULT_POOL_PARAM));

        // Load the healthcheckRoot.

        this.healthcheckRoot = this.normalize(this.getProperty(properties,
                HEALTHCHECK_ROOT_PARAM));
        if (this.healthcheckRoot == null)
            this.healthcheckRoot = DEFAULT_HEALTHCHECK_ROOT;

        // Load the privilegedClientSessionKey.

        this.privilegedClientSessionKey = this.normalize(this.getProperty(
                properties, PRIV_CLIENT_SESSION_KEY_PARAM));
        if (this.privilegedClientSessionKey == null)
            this.privilegedClientSessionKey = DEFAULT_PRIV_CLIENT_SESSION_KEY;

        // Load the closingTimeSessionKey.

        this.closingTimeRequestKey = this.normalize(this.getProperty(
                properties, CLOSING_TIME_REQUEST_KEY_PARAM));
        if (this.closingTimeRequestKey == null)
            this.closingTimeRequestKey = DEFAULT_CLOSING_TIME_REQUEST_KEY;

        // Load the admittedClientSessionKey.

        this.admittedClientSessionKey = this.normalize(this.getProperty(
                properties, ADMITTED_CLIENT_SESSION_KEY_PARAM));
        if (this.admittedClientSessionKey == null)
            this.admittedClientSessionKey = DEFAULT_ADMITTED_CLIENT_SESSION_KEY;

        // Load the admittedClientQueryKey.

        this.admittedClientQueryKey = this.normalize(this.getProperty(
                properties, ADMITTED_CLIENT_QUERY_KEY_PARAM));
        if (this.admittedClientQueryKey == null)
            this.admittedClientQueryKey = DEFAULT_ADMITTED_CLIENT_QUERY_KEY;

        // Load the ssoTargetQueryKey.

        this.ssoTargetQueryKey = this.normalize(this.getProperty(properties,
                SSO_TARGET_QUERY_KEY_PARAM));
        if (this.ssoTargetQueryKey == null)
            this.ssoTargetQueryKey = DEFAULT_SSO_TARGET_QUERY_KEY;

        // Load the hostnameToPool HashMap. No defaults.

        this.hostnameToPool = new HashMap();
        // Explode the filter properties. The returned HashMap maps from pool
        // IDs to pools, for every unique pool ID found in the properties.
        // Each pool is a HashMap in turn, containing name-value pairs for each
        // of the properties using that pool ID.
        pools = this.explode(properties);

        LOG.debug("--- exploded: [" + pools + "]");
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            join = "";
            // Load from the poolHostnames and siteHostnames properties. Note
            // each property value may itself contain a comma-delimited list of
            // hostnames, so we must split around that.
            value = (String) pool.get(POOL_HOSTNAMES_PARAM);
            if (value != null)
                join += value + ",";
            value = (String) pool.get(THIS_SITE_HOSTNAMES_PARAM);
            if (value != null)
                join += value + ",";
            value = (String) pool.get(OTHER_SITE_HOSTNAMES_PARAM);
            if (value != null)
                join += value + ",";
            // Split the hostnames on comma, trim and lowercase them, and
            // eliminate any blank ones. Then add them into the map.
            hostnames = this.split(join);
            for (i = 0; i < hostnames.length; i++)
                this.hostnameToPool.put(hostnames[i], id);
        }

        // Load the pooltoPoolHostname and poolToPoolHostnames HashMaps. No
        // defaults.

        this.poolToPoolHostname = new HashMap();
        this.poolToPoolHostnames = new HashMap();
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            value = (String) pool.get(POOL_HOSTNAMES_PARAM);
            if (value != null) {
                hostnames = this.split(value);
                if (hostnames.length > 0) {
                    this.poolToPoolHostname.put(id, hostnames[0]);
                    this.poolToPoolHostnames.put(id, this.toHashSet(hostnames));
                }
            }
        }

        // Load the poolToThisSiteHostname and poolToThisSiteHostnames HashMaps.
        // No defaults.

        this.poolToThisSiteHostname = new HashMap();
        this.poolToThisSiteHostnames = new HashMap();
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            value = (String) pool.get(THIS_SITE_HOSTNAMES_PARAM);
            if (value != null) {
                hostnames = this.split(value);
                if (hostnames.length > 0) {
                    this.poolToThisSiteHostname.put(id, hostnames[0]);
                    this.poolToThisSiteHostnames.put(id, this
                            .toHashSet(hostnames));
                }
            }
        }

        // Load the poolToOtherSiteHostnames HashMap. No defaults.

        this.poolToOtherSiteHostnames = new HashMap();
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            value = (String) pool.get(OTHER_SITE_HOSTNAMES_PARAM);
            if (value != null) {
                hostnames = this.split(value);
                if (hostnames.length > 0)
                    this.poolToOtherSiteHostnames.put(id, this
                            .toHashSet(hostnames));
            }
        }

        // Load the poolToSiteCookie HashMaps. No defaults.

        this.poolToSiteCookieName = new HashMap();
        this.poolToSiteCookieValue = new HashMap();
        this.poolToSiteCookieDomain = new HashMap();
        this.poolToSiteCookiePath = new HashMap();
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            name = (String) pool.get(SITE_COOKIE_NAME_PARAM);
            if (name != null)
                this.poolToSiteCookieName.put(id, name);
            value = (String) pool.get(SITE_COOKIE_VALUE_PARAM);
            if (value != null)
                this.poolToSiteCookieValue.put(id, value);
            domain = (String) pool.get(SITE_COOKIE_DOMAIN_PARAM);
            if (domain != null)
                this.poolToSiteCookieDomain.put(id, domain);
            path = (String) pool.get(SITE_COOKIE_PATH_PARAM);
            if (path != null)
                this.poolToSiteCookiePath.put(id, path);
        }

        // Load the poolToPrivileged HashMaps. No defaults.

        this.poolToPrivilegedClientAddresses = new HashMap();
        this.poolToPrivilegedClientQueryKey = new HashMap();
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            query = (String) pool.get(PRIV_CLIENT_QUERY_KEY_PARAM);
            if (query != null)
                this.poolToPrivilegedClientQueryKey.put(id, query);
            value = (String) pool.get(PRIV_CLIENT_ADDRS_PARAM);
            if (value != null) {
                addresses = this.split(value);
                if (addresses.length > 0)
                    this.poolToPrivilegedClientAddresses.put(id, (List) Arrays
                            .asList(addresses));
            }
        }

        // Load the poolToGrace HashMaps.

        this.poolToClosingGrace = new HashMap();
        this.poolToClosedGrace = new HashMap();
        entries = pools.entrySet().iterator();
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            id = (String) entry.getKey();
            pool = (HashMap) entry.getValue();
            value = (String) pool.get(SITE_CLOSING_GRACE_PARAM);
            if (value != null) {
                try {
                    grace = new Integer(value);
                    if (grace.intValue() >= 0)
                        this.poolToClosingGrace.put(id, grace);
                } catch (NumberFormatException e) {
                }
            }
            value = (String) pool.get(SITE_CLOSED_GRACE_PARAM);
            if (value != null) {
                try {
                    grace = new Integer(value);
                    if (grace.intValue() >= 0)
                        this.poolToClosedGrace.put(id, grace);
                } catch (NumberFormatException e) {
                }
            }
        }

        // Print the configuration before returning.

        this.printConfiguration();

    } // end constructor

    /**
     * Returns the pool ID to assume by default (ie, when the hostname in the
     * request does not match any pool or site hostname in the configuration).
     * Loaded by the class constructor from the <code>defaultPool</code>
     * filter initialization parameter. When no such parameter is defined, this
     * attribute is null (ie, no default pool is defined).
     */
    protected String getDefaultPool() {

        return this.defaultPool;

    } // end method getDefaultPool

    /**
     * Returns the server-relative URL (ie, path) for the context root of the
     * Admission Control healthcheck Web application deployed on this server.
     * Loaded by the class constructor from the <code>healthcheckRoot</code>
     * filter initialization parameter. When no such parameter is defined, the
     * default healthcheck root is returned: <i>/healthcheck</i>
     */
    protected String getHealthcheckRoot() {

        return this.healthcheckRoot;

    } // end method getHealthcheckRoot

    /**
     * Returns the HTTP session attribute name to use for the privileged-client
     * flag. Loaded by the class constructor from the
     * <code>privilegedClientSessionKey</code> filter initialization
     * parameter. When no such parameter is defined, the default value is
     * returned: <i>com.hp.ac.privileged</i>
     */
    protected String getPrivilegedClientSessionKey() {

        return this.privilegedClientSessionKey;

    } // end method getPrivilegedClientSessionKey

    /**
     * Returns the HTTP request attribute name to use for the closing-time
     * timestamp. Loaded by the class constructor from the
     * <code>closingTimeRequestKey</code> filter initialization parameter.
     * When no such parameter is defined, the default value is returned:
     * <i>com.hp.ac.closingTime</i>
     */
    protected String getClosingTimeRequestKey() {

        return this.closingTimeRequestKey;

    } // end method getClosingTimeRequestKey

    /**
     * Returns the HTTP session attribute name to use for the admitted-client
     * flag. Loaded by the class constructor from the
     * <code>admittedClientSessionKey</code> filter initialization parameter.
     * When no such parameter is defined, the default value is returned:
     * <i>com.hp.ac.admitted</i>
     */
    protected String getAdmittedClientSessionKey() {

        return this.admittedClientSessionKey;

    } // end method getAdmittedClientSessionKey

    /**
     * Returns the query string parameter name to use for the admitted-client
     * flag. Loaded by the class constructor from the
     * <code>admittedClientQueryKey</code> filter initialization parameter.
     * When no such parameter is defined, the default value is returned:
     * <i>admitted</i>
     */
    protected String getAdmittedClientQueryKey() {

        return this.admittedClientQueryKey;

    } // end method getAdmittedClientQueryKey

    /**
     * Returns the query string parameter name to use for the single-sign-on
     * (SSO) target URL. Loaded by the class constructor from the
     * <code>ssoTargetQueryKey</code> filter initialization parameter. When no
     * such parameter is defined, the default value is returned: <i>TARGET</i>
     * (the value used by HP Passport / SiteMinder WebAgent).
     */
    protected String getSsoTargetQueryKey() {

        return this.ssoTargetQueryKey;

    } // end method getSsoTargetQueryKey

    /**
     * Returns all of the pool ID's defined in the configuration, as a set of
     * unique pool ID's. If none have been defined, the set contains just the
     * wildcard pool ID, '*'.
     */
    protected HashSet getPools() {

        HashSet pools = new HashSet();
        pools.addAll(this.hostnameToPool.values());
        pools.add(this.defaultPool);
        return pools;

    } // end method getPools

    /**
     * Returns the pool ID corresponding to the given hostname. The given
     * hostname is first normalized (trimmed and lowercased). The returned pool
     * ID may be the wildcard value, "*" (asterisk). The returned pool ID is
     * trimmed. The mappings on which this is based are loaded by the class
     * constructor from the <code><i>[id.]</i>poolHostnames</code>,
     * <code><i>[id.]</i>thisSiteHostnames</code>, and
     * <code><i>[id.]</i>otherSiteHostnames</code> filter initialization
     * parameters. When no such parameters are defined, or the given hostname is
     * not found in the mappings, null is returned.
     */
    protected String getPool(String hostname) {

        String poolID = null;
        if (hostname != null) {
            hostname = hostname.trim().toLowerCase();
            poolID = (String) this.hostnameToPool.get(hostname);
        }
        return poolID;

    } // end method getPool

    /**
     * Returns the <b>primary</b> pool hostname for the given pool ID. The
     * returned hostname is normalized (trimmed and lowercase). The mappings on
     * which this is based are loaded by the class constructor from the
     * <code><i>[id.]</i>poolHostnames</code> filter initialization
     * parameters. If the wildcard "*" pool ID is given, then only mappings with
     * undefined or wildcard pool IDs are consulted. Otherwise the given pool ID
     * is looked-up in those mappings, and if not found, then the wildcard is
     * applied by default and the lookup is tried again. When no applicable
     * mappings are defined, or the given pool ID is not found in the mappings
     * (nor is there a wildcard or undefined mapping), null is returned.
     */
    protected String getPoolHostname(String poolID) {

        return (String) this.getFromPool(this.poolToPoolHostname, poolID);

    } // end method getPoolHostname

    /**
     * Returns <b>all</b> of the pool hostnames for the given pool ID. The
     * returned hostnames are normalized (trimmed and lowercase). The mappings
     * on which this is based are loaded by the class constructor from the
     * <code><i>[id.]</i>poolHostnames</code> filter initialization
     * parameters. If the wildcard "*" pool ID is given, then only mappings with
     * undefined or wildcard pool IDs are consulted. Otherwise the given pool ID
     * is looked-up in those mappings, and if not found, then the wildcard is
     * applied by default and the lookup is tried again. When no applicable
     * mappings are defined, or the given pool ID is not found in the mappings
     * (nor is there a wildcard or undefined mapping), null is returned.
     */
    protected HashSet getPoolHostnames(String poolID) {

        return (HashSet) this.getFromPool(this.poolToPoolHostnames, poolID);

    } // end method getPoolHostnames

    /**
     * Returns the <b>primary</b> site hostname for this site, for the given
     * pool ID. The returned hostname is normalized (trimmed and lowercase). The
     * mappings on which this is based are loaded by the class constructor from
     * the <code><i>[id.]</i>thisSiteHostnames</code> filter initialization
     * parameters. If the wildcard "*" pool ID is given, then only mappings with
     * undefined or wildcard pool IDs are consulted. Otherwise the given pool ID
     * is looked-up in those mappings, and if not found, then the wildcard is
     * applied by default and the lookup is tried again. When no applicable
     * mappings are defined, or the given pool ID is not found in the mappings
     * (nor is there a wildcard or undefined mapping), null is returned.
     */
    protected String getThisSiteHostname(String poolID) {

        return (String) this.getFromPool(this.poolToThisSiteHostname, poolID);

    } // end method getThisSiteHostname

    /**
     * Returns <b>all</b> of the site hostnames for this site, for the given
     * pool ID. The returned hostnames are normalized (trimmed and lowercase).
     * The mappings on which this is based are loaded by the class constructor
     * from the <code><i>[id.]</i>thisSiteHostnames</code> filter
     * initialization parameters. If the wildcard "*" pool ID is given, then
     * only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected HashSet getThisSiteHostnames(String poolID) {

        return (HashSet) this.getFromPool(this.poolToThisSiteHostnames, poolID);

    } // end method getPoolHostnames

    /**
     * Returns <b>all</b> of the site hostnames for other sites, for the given
     * pool ID. The returned hostnames are normalized (trimmed and lowercase).
     * The mappings on which this is based are loaded by the class constructor
     * from the <code><i>[id.]</i>otherSiteHostnames</code> filter
     * initialization parameters. If the wildcard "*" pool ID is given, then
     * only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected HashSet getOtherSiteHostnames(String poolID) {

        return (HashSet) this
                .getFromPool(this.poolToOtherSiteHostnames, poolID);

    } // end method getPoolHostnames

    /**
     * Returns the site cookie name for the given pool ID. The returned cookie
     * name is trimmed. The mappings on which this is based are loaded by the
     * class constructor from the <code><i>[id.]</i>siteCookieName</code>
     * filter initialization parameters. If the wildcard "*" pool ID is given,
     * then only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected String getSiteCookieName(String poolID) {

        return (String) this.getFromPool(this.poolToSiteCookieName, poolID);

    } // end method getSiteCookieName

    /**
     * Returns the site cookie value for the given pool ID. The returned cookie
     * value is trimmed. The mappings on which this is based are loaded by the
     * class constructor from the <code><i>[id.]</i>siteCookieValue</code>
     * filter initialization parameters. If the wildcard "*" pool ID is given,
     * then only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected String getSiteCookieValue(String poolID) {

        return (String) this.getFromPool(this.poolToSiteCookieValue, poolID);

    } // end method getSiteCookieValue

    /**
     * Returns the site cookie domain for the given pool ID. The returned cookie
     * domain is trimmed. The mappings on which this is based are loaded by the
     * class constructor from the <code><i>[id.]</i>siteCookieDomain</code>
     * filter initialization parameters. If the wildcard "*" pool ID is given,
     * then only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected String getSiteCookieDomain(String poolID) {

        return (String) this.getFromPool(this.poolToSiteCookieDomain, poolID);

    } // end method getSiteCookieDomain

    /**
     * Returns the site cookie path for the given pool ID. The returned cookie
     * path is trimmed. The mappings on which this is based are loaded by the
     * class constructor from the <code><i>[id.]</i>siteCookiePath</code>
     * filter initialization parameters. If the wildcard "*" pool ID is given,
     * then only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected String getSiteCookiePath(String poolID) {

        return (String) this.getFromPool(this.poolToSiteCookiePath, poolID);

    } // end method getSiteCookiePath

    /**
     * Returns the privileged client IP address patterns for the given pool ID.
     * These are the patterns for client IP addresses which are to be treated as
     * staff users in the respective pool. They are returned normalized (trimmed
     * and lowercase). The mappings on which this is based are loaded by the
     * class constructor from the
     * <code><i>[id.]</i>privilegedClientAddresses</code> filter
     * initialization parameters. If the wildcard "*" pool ID is given, then
     * only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected List getPrivilegedClientAddresses(String poolID) {

        return (List) this.getFromPool(this.poolToPrivilegedClientAddresses,
                poolID);

    } // end method getPrivilegedClientAddresses

    /**
     * Returns the privileged client query string parameter name for the given
     * pool ID. This is the query string parameter name which, when present in
     * the request at the beginning of a session, causes clients to be treated
     * as either staff or end users in the respective pool. It is returned
     * normalized (trimmed). The mappings on which this is based are loaded by
     * the class constructor from the
     * <code><i>[id.]</i>privilegedClientQueryKey</code> filter
     * initialization parameters. If the wildcard "*" pool ID is given, then
     * only mappings with undefined or wildcard pool IDs are consulted.
     * Otherwise the given pool ID is looked-up in those mappings, and if not
     * found, then the wildcard is applied by default and the lookup is tried
     * again. When no applicable mappings are defined, or the given pool ID is
     * not found in the mappings (nor is there a wildcard or undefined mapping),
     * null is returned.
     */
    protected String getPrivilegedClientQueryKey(String poolID) {

        return (String) this.getFromPool(this.poolToPrivilegedClientQueryKey,
                poolID);

    } // end method getPrivilegedClientQueryKey

    /**
     * Returns the <b>closing</b> grace period for the given pool ID. This is
     * the time to delay the effects of the <b>closing</b> state. This
     * attribute is loaded by the class constructor from the
     * <code><i>[id.]</i>siteClosingGracePeriod</code> initialization
     * parameters. When no such parameters are defined, or defined with
     * non-positive-integer values, this method returns zero.
     */
    protected Integer getClosingGrace(String poolID) {

        Integer grace = (Integer) this.getFromPool(this.poolToClosingGrace,
                poolID);
        if (grace == null)
            grace = DEFAULT_CLOSING_GRACE;
        return grace;

    } // end method getClosingGrace

    /**
     * Returns the <b>closed</b> grace period for the given pool ID. This is
     * the time to delay the effects of the <b>closed</b> state. This attribute
     * is loaded by the class constructor from the
     * <code><i>[id.]</i>siteClosedGracePeriod</code> initialization
     * parameters. When no such parameters are defined, or defined with
     * non-positive-integer values, this method returns zero.
     */
    protected Integer getClosedGrace(String poolID) {

        Integer grace = (Integer) this.getFromPool(this.poolToClosedGrace,
                poolID);
        if (grace == null)
            grace = DEFAULT_CLOSED_GRACE;
        return grace;

    } // end method getClosedGrace

    protected void printConfiguration() {

        LOG.info(this.getClass().getName()
                + ": Loaded the following configuration:");
        LOG.info("--- healthcheck root: ["
                + this.getHealthcheckRoot() + "]");
        LOG.info("--- privileged client session key: ["
                + this.getPrivilegedClientSessionKey() + "]");
        LOG.info("--- closing time request key: ["
                + this.getClosingTimeRequestKey() + "]");
        LOG.info("--- admitted client session key: ["
                + this.getAdmittedClientSessionKey() + "]");
        LOG.info("--- admitted client query key: ["
                + this.getAdmittedClientQueryKey() + "]");
        LOG.info("--- SSO target query key: ["
                + this.getSsoTargetQueryKey() + "]");
        LOG.info("--- default pool ID: [" + this.getDefaultPool()
                + "]");

        HashSet pools = this.getPools();
        Iterator it = pools.iterator();
        String pool;
        while (it.hasNext()) {
            pool = (String) it.next();
            LOG.info("--- for pool ID: [" + pool + "]:");
            LOG.info("------ primary pool hostname: ["
                    + this.getPoolHostname(pool) + "]");
            LOG.info("------ primary this-site hostname: ["
                    + this.getThisSiteHostname(pool) + "]");
            LOG.info("------ all pool hostnames: ["
                    + this.getPoolHostnames(pool) + "]");
            LOG.info("------ all this-site hostnames: ["
                    + this.getThisSiteHostnames(pool) + "]");
            LOG.info("------ all other-site hostnames: ["
                    + this.getOtherSiteHostnames(pool) + "]");
            LOG.info("------ site cookie name: ["
                    + this.getSiteCookieName(pool) + "]");
            LOG.info("------ site cookie value: ["
                    + this.getSiteCookieValue(pool) + "]");
            LOG.info("------ site cookie domain: ["
                    + this.getSiteCookieDomain(pool) + "]");
            LOG.info("------ site cookie path: ["
                    + this.getSiteCookiePath(pool) + "]");
            LOG.info("------ staff-user IP addresses: ["
                    + this.getPrivilegedClientAddresses(pool) + "]");
            LOG.info("------ staff-user query key: ["
                    + this.getPrivilegedClientQueryKey(pool) + "]");
            LOG.info("------ closing grace period: ["
                    + this.getClosingGrace(pool) + "]");
            LOG.info("------ closed grace period: ["
                    + this.getClosedGrace(pool) + "]");
        }

    } // end method printConfiguration

    // ///////////////////////////////////////////////////////////
    // PRIVATE METHODS
    // ///////////////////////////////////////////////////////////
    private HashMap explode(Object properties) {

        HashMap pools = new HashMap();
        Enumeration names;
        String name, id, value;
        int i;

        // Explode the filter properties into a HashMap of pool IDs (trimmed),
        // each pointing to a HashMap of properties for that pool (names
        // pointing to values, all trimmed). Begin by enumerating across all the
        // init properties.

        names = this.getPropertyNames(properties);
        if (names == null)
            return pools;

        while (names.hasMoreElements()) {
            name = (String) names.nextElement();
            value = this.getProperty(properties, name);
            name = name.trim();
            value = value.trim();

            // Skip past properties which by definition do not contain pool
            // ID's.
            if (DEFAULT_POOL_PARAM.equals(name)
                    || HEALTHCHECK_ROOT_PARAM.equals(name)
                    || ADMITTED_CLIENT_SESSION_KEY_PARAM.equals(name)
                    || ADMITTED_CLIENT_QUERY_KEY_PARAM.equals(name)
                    || PRIV_CLIENT_SESSION_KEY_PARAM.equals(name)
                    || CLOSING_TIME_REQUEST_KEY_PARAM.equals(name))
                continue;

            // For properties which by definition may contain pool IDs, look for
            // a pool ID by splitting on "." - if no pool ID is found, assume
            // wildcard "*" as the pool ID. Then add the property to the
            // exploded set.
            i = name.indexOf(".");
            if (i == -1) {
                id = WILDCARD_POOL_ID;
            } else {
                id = name.substring(0, i).trim();
                if (i + 1 < name.length())
                    name = name.substring(i + 1).trim();
                else
                    name = "";
            }
            this.put(pools, id, name, value);
        }

        return pools;
    } // end method explode

    private void put(HashMap pools, String id, String name, String value) {

        // Add the name-value pair to the id-pool pair among the pools.
        // Fist get the pool by ID from the pools. (Since this is a private
        // method, we assume all params are already made proper by the caller:
        // ie, non-null and trimmed.)
        HashMap pool = (HashMap) pools.get(id);
        // If it did not exist, make it (empty) and add it to the pools.
        if (pool == null) {
            pool = new HashMap();
            pools.put(id, pool);
        }
        // Now put the name-value pair into the pool. Done.
        pool.put(name, value);

    } // end method put

    private String[] split(String value) {

        // Split the given value by comma into an array of non-blank values.
        // Trim and lowercase each value.
        String[] newFields;
        String[] fields = value.split(",");
        ArrayList values = new ArrayList();
        String field;
        for (int i = 0; i < fields.length; i++) {
            field = fields[i].trim().toLowerCase();
            if (!"".equals(field))
                values.add(field);
        }
        newFields = new String[values.size()];
        for (int i = 0; i < values.size(); i++)
            newFields[i] = (String) values.get(i);
        return newFields;

    } // end method split

    private HashSet toHashSet(String[] values) {

        HashSet set = new HashSet();
        for (int i = 0; i < values.length; i++)
            set.add(values[i]);
        return set;

    } // end method toHashSet

    private Object getFromPool(HashMap pool, String id) {

        Object value = null;
        if (id != null) {
            value = pool.get(id);
            if ((value == null) && !WILDCARD_POOL_ID.equals(id))
                value = pool.get(WILDCARD_POOL_ID);
        }
        return value;

    } // end method get

    private String normalize(String s) {

        if (s != null)
            s = s.trim();
        if ("".equals(s))
            s = null;
        return s;

    } // end method normalize

    private String getProperty(Object properties, String key) {

        Properties props;
        FilterConfig fconfig;
        String value = null;
        if (properties != null) {
            if (properties instanceof Properties) {
                props = (Properties) properties;
                value = props.getProperty(key);
            } else if (properties instanceof FilterConfig) {
                fconfig = (FilterConfig) properties;
                value = fconfig.getInitParameter(key);
            }
        }
        return value;

    } // end method getProperty

    private Enumeration getPropertyNames(Object properties) {

        Properties props;
        FilterConfig fconfig;
        Enumeration names = null;
        if (properties != null) {
            if (properties instanceof Properties) {
                props = (Properties) properties;
                names = props.propertyNames();
            } else if (properties instanceof FilterConfig) {
                fconfig = (FilterConfig) properties;
                names = fconfig.getInitParameterNames();
            }
        }
        return names;

    } // end method getPropertyNames
}