/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.SecurityException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.hp.it.spf.ac.healthcheck.background.BrokenHealthcheckConnectionException;
import com.hp.it.spf.ac.healthcheck.background.CannotCreateHealthcheckClientException;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckLogger;
import com.hp.it.spf.ac.healthcheck.background.TimedOutHealthcheckConnectionException;

/**
 * This is the base class for all HTTP-based healthcheck clients in the
 * healthcheck service. An HTTP-based healthcheck client is currently the only
 * kind of healthcheck client in the service. An HTTP-based healthcheck client
 * is any class which performs a healthcheck by connecting to an HTTP server at
 * a given IP and port, and then performing a particular HTTP request message
 * against it. The server IP and port are of the instance being monitored, and
 * the request message should be for a resource on the server which manipulates
 * critical functionality upon which health decisions should be based.
 * <p>
 * 
 * At the present time, there are two kinds of HTTP-based healthcheck clients,
 * which subclass this one. They are executed and evaluated by the
 * <code>HealthcheckDriver</code>.
 * <p>
 * 
 * <ul>
 * <li> <code>PortalPulseHealthcheckClient</code> performs a request for the
 * configured "portal pulse page" against the portal server IP and port
 * configured in the <code>environment.properties</code> file.
 * <p>
 * 
 * <blockquote> <b>Note:</b> Additional subclasses of HealthcheckClient can be
 * made as-needed and configured in <code>environment.properties</code> for
 * alternative use as a "portal pulse page" checker. This is why this class is
 * an abstract.
 * <p>
 * </blockquote>
 * 
 * <li> <code>OpenSignHealthcheckClient</code> performs an request for the
 * "open sign" against the open-sign server IP and port configured in the
 * <code>environment.properties</code> file.
 * <p>
 * </ul>
 * 
 * See JavaDocs for those classes for further information.
 * <p>
 * 
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */

public abstract class HealthcheckClient {

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    private int timeout = 0;

    private int max = 0;

    private int port = 0;

    private boolean manageCookies = false;

    private String host = null;

    private String url = "/";

    private Hashtable cookies = new Hashtable();

    protected Pattern pattern = null;

    private static String JAVA_DNS_TTL_PROPERTY = "networkaddress.cache.ttl";

    private static int BYTES_TO_READ = 8192;

    private static String COOKIE_REGEX = "^(?i:Cookie): ";

    private static String SET_COOKIE_REGEX = "^(?i:Set-Cookie): ";

    private static String SET_COOKIE_ATTRS_REGEX = ";.*$";

    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * Constructs a <code>HealthcheckClient</code> for requesting the given
     * URL from the given host at the given port number, using HTTP. After
     * construction, this <code>HealthcheckClient</code> instance may be used
     * to perform repeated arbitrary healthcheck transactions against that
     * server and port, using the <code>execute</code> method each time (see).
     * <p>
     * 
     * @param <code>String</code> aHost is the HTTP server host's domain name
     *            (eg, <i>host.network.hp.com</i>) or IP address in common
     *            string form (eg, <i>192.6.48.18</i>). If a domain name, note
     *            it must be resolvable from the system using a typical
     *            mechanism, such as <i>/etc/hosts</i>, HP's standard DNS
     *            hierarchy, or "specialty" DNS resolvers such as GSLB systems.
     *            The server host is a required field. If null or blank (empty)
     *            string are provided, then
     *            <code>CannotCreateHealthcheckClientException</code> is
     *            thrown. Note that if an unresolvable/unreachable name or IP
     *            address is provided, this will not be detected until you
     *            actually call the <code>execute</code> method (see). The
     *            given host can be a proxy server. In that case, specify the
     *            actual host in the URL parameter below.
     *            <p>
     * 
     * @param <code>int</code> aPort is the port number for HTTP service on the
     *            host (eg, <i>80</i>). It is a required field. If a
     *            non-positive integer is provided, then
     *            <code>CannotCreateHealthcheckClientException</code> is
     *            thrown. Note that if an uncommunicable port is provided, this
     *            will not be detected until you actually call the
     *            <code>execute</code> method (see). The given port can be for
     *            a proxy server. In that case, specify the actual port in the
     *            URL parameter below.
     *            <p>
     * 
     * @param <code>String</code> aURL is the URL to request from the given host
     *            at the given port. The request will use the GET method. For a
     *            direct connection, this should be a relative URL - including
     *            path and query string (if any). For a proxied connection, this
     *            should be an absolute URL - including scheme, actual host and
     *            port, path, and query string (if any). If null or blank
     *            (empty) string are provided, then <code>/</code> is used by
     *            default. Note that if an invalid URL is provided, this will
     *            not be detected until you actually call the
     *            <code>execute</code> method (see).
     *            <p>
     * 
     * @exception <code>CannotCreateHealthcheckClientException</code> if the host
     *                parameter is null or blank, or the port is a non-positive
     *                integer.
     *                <p>
     */
    public HealthcheckClient(String aHost, int aPort, String aURL)
            throws CannotCreateHealthcheckClientException {

        String mp = this.getClass().getName()
                + " constructor (String, int, String) | ";
        String m;

        HealthcheckLogger.log(this,
                "Entered constructor (String, int, String).");

        if ((aHost == null) || ("".equals(aHost = aHost.trim()))
                || (aPort <= 0)) {
            m = "Requires an HTTP server host (name or IP address) and positive port number.";
            HealthcheckLogger.log(this, m);
            throw new CannotCreateHealthcheckClientException(mp + m);
        }
        if ((aURL == null) || ("".equals(aURL = aURL.trim()))) {
            aURL = this.url;
        }

        this.host = aHost;
        this.port = aPort;
        this.url = aURL;

        m = "Finished constructor: host: [" + this.host + "], port: ["
                + this.port + "], url: [" + this.url + "].";
        HealthcheckLogger.log(this, m);

    } // end constructor

    /**
     * Returns the HTTP server host to which this <code>HealthcheckClient</code>
     * attempts to connect. The returned value is identical to what you provided
     * when constructing the <code>HealthcheckClient</code> (see), except that
     * any leading or trailing whitespace has been trimmed.
     * <p>
     * 
     * @return <code>String</code> is the HTTP server host.
     */
    public String getHost() {

        return (this.host);

    } // end method

    /**
     * Returns the port number of the HTTP server to which this
     * <code>HealthcheckClient</code> attempts to connect. The returned value
     * is identical to what you provided when constructing the
     * <code>HealthcheckClient</code> (see).
     * <p>
     * 
     * @return <code>int</code> is the HTTP server port.
     */
    public int getPort() {

        return (this.port);

    } // end method

    /**
     * Returns the URL which this <code>HealthcheckClient</code> requests. The
     * returned value is identical to what you provided when constructing the
     * <code>HealthcheckClient</code> (see) - except that if you provided a
     * blank/null value, then the default "/" is returned; and if you provided a
     * non-blank, non-null value, then any leading or trailing whitespace has
     * been trimmed.
     * <p>
     * 
     * @return <code>String</code> is the URL.
     */
    public String getUrl() {

        return (this.url);

    } // end method

    /**
     * Returns the current cookie management policy for the healthcheck client.
     * See the companion <code>setCookiePolicy</code> method for an
     * explanation.
     * <p>
     * 
     * @return <code>boolean</code> is <code>true</code> if cookie
     *         management is enabled, and <code>false</code> if disabled.
     */
    public boolean getCookiePolicy() {

        return (this.manageCookies);

    } // end method getCookiePolicy

    /**
     * Returns the timeout (in milliseconds) used when the
     * <code>HealthcheckClient</code> is executed. If no timeout has been
     * defined, then <i>0</i> will be returned.
     * <p>
     * 
     * @return <code>int</code> is the timeout (in milliseconds), or <i>-1</i>
     *         (meaning no timeout).
     */
    public int getTimeout() {

        return (this.timeout);

    } // end method

    /**
     * Sets the cookie management policy in the healthcheck client. A
     * <code>true</code> parameter enables cookie management, and a
     * <code>false</code> parameter disables it.
     * <p>
     * When enabled, any cookies found in the server's response to the
     * healthcheck client will be saved, and sent back to the server in
     * subsequent requests. This cookie management is primitive: all cookies in
     * the response are accepted, even if their domain or path do not match the
     * request. Similarly, subsequent set-cookies using the same cookie name
     * overwrite the previous value for that cookie, despite any differences in
     * domain or path scope.
     * <p>
     * When disabled, any cookies in the server's response are ignored.
     * <p>
     * 
     * @param <code>boolean</code> enables cookie management if <code>true</code>
     *            and disables it if <code>false</code>.
     */
    public void setCookiePolicy(boolean flag) {

        this.manageCookies = flag;

    } // end method setCookiePolicy

    /**
     * Sets a timeout (in milliseconds) to be used when the
     * <code>HealthcheckClient</code> is executed. The timeout must be a
     * positive integer; otherwise timeout is disabled.
     * <p>
     * 
     * By default, the <code>execute</code> method (see) performs without any
     * timeout - it will block for as long as the underlying network and server
     * permit, both when attempting to connect to the HTTP server, and when
     * attempting to read data back from the HTTP server.
     * <p>
     * 
     * With the <code>setTimeout</code> method, a timeout (in milliseconds)
     * can be applied to subsequent <code>execute</code> calls. The timeout is
     * applied to the <code>Socket.connect</code> and
     * <code>Socket.getInputStream.read</code> steps which are performed by
     * <code>execute</code>. If any such steps time-out, then
     * <code>execute</code> will throw a
     * <code>TimedOutHealthcheckConnectionException</code> back to the caller.
     * <p>
     * 
     * <b>Note:</b> Because the timeout is applied at <b>each step</b>
     * (connect and subsequent reads) within <code>execute</code>, setting
     * the timeout to X milliseconds does <b>not</b> necessarily constrain the
     * whole <code>execute</code> to X milliseconds. A haltingly-slow network,
     * for example, could drag the <code>execute</code> out over several times
     * that, so long as the connect and each read returned within the X-ms
     * threshold.
     * <p>
     * 
     * @param <code>int</code> aTimeout is the timeout (in milliseconds), or use
     *            <i>0</i> or less to disable timeout (the default).
     */
    public void setTimeout(int aTimeout) {

        if (aTimeout < 0)
            aTimeout = 0;
        this.timeout = aTimeout;

    } // end method

    /**
     * Returns the maximum response length (in bytes) that should be read by the
     * <code>HealthcheckClient</code> when executed. If no maximum has been
     * defined, then <i>0</i> will be returned.
     * <p>
     * 
     * @return <code>int</code> is the maximum response length to return (in
     *         bytes), or <i>0</i> (meaning unlimited).
     *         <p>
     */
    public int getMaxResponseBytes() {

        return (this.max);

    } // end method

    /**
     * Sets a maximum response length (in bytes) that should be read by the
     * <code>HealthcheckClient</code> when executed. If the server's response
     * is longer than the maximum response length, it will be truncated. The
     * maximum must be a positive integer; otherwise response length will be
     * unlimited.
     * <p>
     * 
     * @param <code>int</code> aMaximum is the maximum response length (in
     *            bytes), or use <i>0</i> or less to disable truncation (the
     *            default).
     */
    public void setMaxResponseBytes(int aMaximum) {

        if (aMaximum < 0)
            aMaximum = 0;
        this.max = aMaximum;

    } // end method

    /**
     * Returns the Java regular expression string that should be used in the
     * <code>evaluate</code> method to verify the healthcheck response was
     * successful. It will be returned exactly as it was provided in the
     * <code>setSuccessPattern</code> method (see) - with null being returned
     * by default.
     * <p>
     * 
     * @return <code>String</code> is the success pattern to search for, or
     *         null if none was provided.
     *         <p>
     */
    public String getSuccessPattern() {

        String aPattern = null;

        if (this.pattern != null)
            aPattern = this.pattern.pattern();
        return (aPattern);

    } // end method

    /**
     * Sets the Java regular expression string that should be used in the
     * <code>evaluate</code> method to verify the healthcheck response was
     * successful. If the given string is not a valid Java regular expression
     * then the standard <code>PatternSyntaxException</code> is thrown. Thus
     * when <code>evaluate</code> runs, you know that the <code>pattern</code>
     * attribute is either null or valid.
     * <p>
     * 
     * @param <code>String</code> aPattern is the Java regular expression string
     *            to use.
     */
    public void setSuccessPattern(String aPattern)
            throws PatternSyntaxException {

        this.pattern = Pattern.compile(aPattern);

    } // end method

    /**
     * Performs an HTTP transaction at the server and port for this
     * <code>HealthcheckClient</code>, requesting the URL for this
     * <code>HealthcheckClient</code>, and returning the server's response
     * message string.
     * <p>
     * 
     * <code>execute</code> begins by attempting to establish and conduct an
     * HTTP transaction on the server. The server host and port are taken from
     * the values configured with the <code>HealthcheckClient</code> when you
     * created it.
     * <p>
     * 
     * If the server host is not already an IP address, then it is resolved
     * without caching (thus a new hostname resolution occurs each time the
     * <code>execute</code> method is called). Should the
     * <code>HealthcheckClient</code> be unable to establish a connection with
     * the server and port, then a
     * <code>BrokenHealthcheckConnectionException</code> will be thrown. (If
     * the attempt to establish a connection times-out, then the exception
     * thrown will actually be a subclass of
     * <code>BrokenHealthcheckConnectionException</code>:
     * <code>TimedOutHealthcheckConnectionException</code>.)
     * <p>
     * 
     * When the HTTP connection is established, <code>execute</code> sends an
     * HTTP GET request for the URL configured with the
     * <code>HealthcheckClient</code> when you created it. The URL string is
     * assumed to fully conform to the basic requirements of the HTTP protocol -
     * no edit-checking is done against it by the <code>HealthcheckClient</code>
     * itself. Execution then blocks until receipt of a response stream; when it
     * is received, the <code>HealthcheckClient</code> drains it into a
     * response message string to be returned. (If a maximum response length has
     * been defined, then the response stream is only drained up to that point.)
     * <code>HealthcheckClient</code> then closes the connection. Any failure
     * in this I/O against the connection will result in a
     * <code>BrokenHealthcheckConnectionException</code>. (If the attempt to
     * read the response stream times-out, then the exception thrown will
     * actually be a subclass of
     * <code>BrokenHealthcheckConnectionException</code>:
     * <code>TimedOutHealthcheckConnectionException</code>.)
     * <p>
     * 
     * Lastly, the response message string is parsed for cookies (<i>Set-Cookie:</i>
     * headers). Any cookies found are stored inside the
     * <code>HealthcheckClient</code>, and are sent back to the server next
     * time. Thus a rudimentary form of cookie management is done by this
     * client.
     * <p>
     * 
     * Otherwise, no parsing of the response message string is done. Any HTTP or
     * application errors indicated in it, are left undetected. The caller will
     * presumably check for them itself (eg, using the <code>evaluate</code>
     * method). For example, if an HTTP error occurred on the server (eg <i>404</i>
     * or <i>500</i>) that does <b>not</b> cause <code>execute</code> to
     * throw any exception. Instead the response message is simply returned.
     * <p>
     * 
     * @return <code>String</code> is the response stream returned from the
     *         server, in serialized string form. For example,
     *         <code>HTTP/1.0 200 Document found\r\n...</code>. Note that the
     *         response stream may have been truncated by a previously-set
     *         maximum length (see <code>setMaxResponseBytes</code>). The
     *         response stream is not edit-checked or error-detected in any way.
     *         See above for information about set-cookie handling.
     *         <p>
     * 
     * @exception <code>TimedOutHealthcheckConnectionException</code> (a subclass
     *                of <code>BrokenHealthcheckConnectionException</code>)
     *                means that either the connect or read socket operations
     *                timed-out, per the timeout previously specified using the
     *                <code>setTimeout</code> method (see).
     *                <p>
     * 
     * @exception <code>BrokenHealthcheckConnectionException</code> may mean that
     *                a connection to the HTTP server and port (specified in the
     *                <code>HealthcheckClient</code> constructor) could not be
     *                established. Or it may mean that a subsequent I/O
     *                operation against the connection failed.
     *                <p>
     */
    public String execute() throws TimedOutHealthcheckConnectionException,
            BrokenHealthcheckConnectionException {

        InetAddress ipAddr;
        InetSocketAddress sockAddr;
        OutputStream requestMessageStream;
        InputStream responseMessageStream;
        Socket sock;

        String ttl;
        String request;
        String response;
        byte[] requestBuffer;
        byte[] responseBuffer;
        int actualLength;
        int howMuch;
        int i;
        boolean maxedOut;

        String connectToHost = this.host;
        int connectToPort = this.port;
        String connectToUrl = this.url;

        String pre = this.getClass().getName() + " execute () | ";
        String msg;

        HealthcheckLogger.log(this, "Entered execute().");

        // Construct request string.

        request = "GET " + connectToUrl + " HTTP/1.0\r\n";
        request += "User-Agent: healthchecker (" + this.getClass().getName()
                + ")\r\n";
        // Insert "Host: hp.com" to satisfy SiteMinder WebAgent, if it is in the
        // way (SiteMinder WebAgent insists on a Host: header). Note: for
        // healthcheck purposes, we assume the value of the Host header doesn't
        // matter).
        request += "Host: " + connectToHost + "\r\n";
        request += "\r\n";

        // Inject any previously-saved cookies as another preliminary step.

        if (this.manageCookies) {
            HealthcheckLogger.log(this, "Injecting cookies into request.");
            request = this.injectCookiesIntoRequest(request);
        }

        msg = "host: [" + connectToHost + "], " + "port: [" + connectToPort
                + "], " + "request: [" + request + "].";
        HealthcheckLogger.log(this, "Planning to perform this transaction: "
                + msg);

        // First lookup an InetAddress for the host. InetAddress.getByName works
        // whether the host is an actual hostname (eg "itrc.hp.com") or string
        // representation of an IP address (eg "192.6.165.27"). In case the DNS
        // server for the host is a dynamic-DNS system (eg Internet Redirector,
        // Foundry GSLB) we temporarily set the Java caching policy to never
        // cache.

        msg = "Resolving IP address for host: [" + connectToHost + "].";
        HealthcheckLogger.log(this, msg);
        try {
            ttl = Security.getProperty(JAVA_DNS_TTL_PROPERTY);
            Security.setProperty(JAVA_DNS_TTL_PROPERTY, "0");
            ipAddr = InetAddress.getByName(connectToHost);
            if (ttl != null)
                Security.setProperty(JAVA_DNS_TTL_PROPERTY, ttl);
        } catch (UnknownHostException e) {
            msg = "Could not resolve an IP address for the host.  UnknownHostException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        } catch (SecurityException e) {
            msg = "Could not temporarily disable DNS caching ("
                    + JAVA_DNS_TTL_PROPERTY
                    + " security property), or could not perform DNS lookup of the host.  This should never happen.  SecurityException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Next convert the InetAddress to the form needed to establish a
        // socket connection with timeout: an InetSocketAddress.

        msg = "Building socket address for host IP address: [" + ipAddr
                + "], port: [" + connectToPort + "].";
        HealthcheckLogger.log(this, msg);
        try {
            sockAddr = new InetSocketAddress(ipAddr, connectToPort);
        } catch (IllegalArgumentException e) {
            msg = "Could not make a socket address for this host and port number.  Maybe the port is out-of-range.  IllegalArgumentException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Next construct a new unbound socket with the timeout set.

        if (this.timeout == 0)
            msg = "Making the socket with no timeout.";
        else
            msg = "Making the socket and setting the timeout to: ["
                    + this.timeout + "] millis.";
        HealthcheckLogger.log(this, msg);
        try {
            sock = new Socket();
            sock.setSoTimeout(this.timeout);
        } catch (SocketException e) {
            msg = "Could not make a socket and/or set the timeout.  SocketException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Now bind the socket to the socket address and connect to the server
        // and port.

        msg = "Connecting socket to server at host IP address: [" + ipAddr
                + "], port: [" + connectToPort + "].";
        HealthcheckLogger.log(this, msg);
        try {
            sock.connect(sockAddr, this.timeout);
        } catch (SocketTimeoutException e) {
            try {
                sock.close();
            } catch (IOException ex) {
                ;
            }
            msg = "The connect to the server timed out and has been aborted.  SocketTimeoutException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new TimedOutHealthcheckConnectionException(pre + msg);
        } catch (IOException e) {
            try {
                sock.close();
            } catch (IOException ex) {
                ;
            }
            msg = "There was an I/O problem connecting to the server.  Maybe nothing is listening at that IP address and port.  IOException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        } catch (IllegalArgumentException e) {
            try {
                sock.close();
            } catch (IOException ex) {
                ;
            }
            msg = "The connect operation failed due to an illegal argument.  This should never happen.  IllegalArgumentException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Now send the HTTP request message text, as a byte array, to the
        // server.

        msg = "Writing HTTP request message text into the socket.";
        HealthcheckLogger.log(this, msg);
        try {
            requestBuffer = request.getBytes("UTF-8");
            requestMessageStream = sock.getOutputStream();
            requestMessageStream.write(requestBuffer);
            requestMessageStream.flush();
        } catch (IOException e) {
            try {
                sock.close();
            } catch (IOException ex) {
                ;
            }
            msg = "There was an I/O problem writing the HTTP request message text to the server.  IOException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Now turn around and start reading the response from the server, until
        // the server closes the response stream. This is a blocking read, with
        // a timeout (see Socket.setSoTimeout JavaDoc for description of
        // behavior) as configured by the caller through a previous
        // setTimeout call. It reads 8K at a time, accumulating it
        // into a data string up to the limit configured by the caller through
        // a previous setMaxResponseBytes call (0 for unlimited).

        maxedOut = false;
        actualLength = 0;
        response = "";
        responseBuffer = new byte[BYTES_TO_READ];
        msg = "Reading HTTP response message text from the socket.";
        HealthcheckLogger.log(this, msg);
        try {
            responseMessageStream = sock.getInputStream();
            while ((i = responseMessageStream.read(responseBuffer)) > 0) {
                if ((this.max > 0) && (actualLength + i > this.max))
                    maxedOut = true;
                howMuch = maxedOut ? this.max - actualLength : i;
                response += new String(responseBuffer, 0, howMuch, "UTF-8");
                actualLength += howMuch;
                if (maxedOut)
                    break;
            }
        } catch (SocketTimeoutException e) {
            try {
                sock.close();
            } catch (IOException ex) {
                ;
            }
            msg = "The read operation to the server timed out and has been aborted.  SocketTimeoutException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new TimedOutHealthcheckConnectionException(pre + msg);
        } catch (IOException e) {
            try {
                sock.close();
            } catch (IOException ex) {
                ;
            }
            msg = "There was an I/O problem reading the HTTP response message from the server.  IOException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Disconnect the server.

        HealthcheckLogger.log(this, "Closing the socket.");
        try {
            sock.close();
        } catch (IOException e) {
            msg = "There was an I/O problem closing the socket connected to the server.  IOException message: ["
                    + e.getMessage() + "].";
            HealthcheckLogger.log(this, msg);
            throw new BrokenHealthcheckConnectionException(pre + msg);
        }

        // Now parse the response string, looking for any Set-Cookie headers.
        // If we find any, save those cookies for next time.

        if (this.manageCookies) {
            HealthcheckLogger.log(this, "Sniffing response for cookies.");
            this.sniffCookiesFromResponse(response);
        }

        // Done.

        HealthcheckLogger.log(this,
                "Finished execute() and returning response: [" + response
                        + "].");
        return (response);

    } // end method

    // ///////////////////////////////////////////////////////////////////
    /* ABSTRACT METHODS */
    // ////////////////////////////////////////////////////////////////////
    /**
     * Abstract method reserved for evaluating the given HTTP response message
     * string to see if it passes the healthcheck or not. Typically the given
     * string would be the one returned by the <code>execute</code> method
     * (see), and the protected <code>pattern</code> attribute (as set
     * previously by <code>setSuccessPattern</code>, see) would be used to
     * match it. Typically if the match succeeded then true would be returned,
     * and otherwise false would be returned (and if the <code>pattern</code>
     * attribute was unset, false would be returned automatically).
     * <p>
     * 
     * This method is left abstract, though, so that alternative
     * <code>HealthcheckClient</code> subclasses can be created which evaluate
     * in different ways.
     * <p>
     */
    public abstract boolean evaluate(String response);

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
    private String injectCookiesIntoRequest(String request) {

        int i = 0;
        String line;
        Vector lines;
        Hashtable ck;
        boolean body = false;
        boolean found = false;
        boolean more = false;

        if (request == null)
            return request;

        if (this.cookies.isEmpty())
            return request;

        lines = new Vector(Arrays.asList(request.split("\n")));

        request = "";
        while (i < lines.size()) {
            line = (String) lines.get(i++);

            if (body) { // process body of request
                if (more)
                    request += "\n";
                request += line;
                more = true;
            } else { // process headers of request
                line = line.replaceAll("\r$", "");

                // line.matches() is not working for some reason in Java
                // 1.4.2.09
                // but line.replaceAll is working - use it instead
                // if (line.matches (this.COOKIE_REGEX)) {
                if (!line.equals(line.replaceAll(COOKIE_REGEX, ""))) {
                    line = line.replaceAll(COOKIE_REGEX, "");
                    ck = deserializeCookies(line);
                    ck.putAll(this.cookies);
                    line = "Cookie: " + serializeCookies(ck) + "\r\n";
                    request += line;
                    found = true;
                } else if (line.equals("")) {
                    if (!found) {
                        line = "Cookie: " + serializeCookies(this.cookies)
                                + "\r\n";
                        request += line;
                        found = true;
                    }
                    line = "\r\n";
                    request += line;
                    body = true;
                } else {
                    line += "\r\n";
                    request += line;
                }
            }
        }
        return request;

    } // end method

    private void sniffCookiesFromResponse(String response) {

        int i = 0;
        String line;
        Vector lines;
        Hashtable ck;

        if (response == null)
            return;

        lines = new Vector(Arrays.asList(response.split("\n")));

        while (i < lines.size()) {
            line = (String) lines.get(i++);
            line = line.replaceAll("\r$", "");

            // line.matches() is not working for some reason in Java 1.4.2.09
            // but line.replaceAll is working - use it instead
            // if (line.matches (this.SET_COOKIE_REGEX)) {
            if (!line.equals(line.replaceAll(SET_COOKIE_REGEX, ""))) {
                line = line.replaceAll(SET_COOKIE_REGEX, "");
                line = line.replaceAll(SET_COOKIE_ATTRS_REGEX, "");
                ck = deserializeCookies(line);
                this.cookies.putAll(ck);
            } else if (line.equals("")) {
                break;
            }
        }
        return;

    } // end method

    private String serializeCookies(Hashtable ck) {

        Enumeration e;
        String s = "";
        String k, v;

        if (ck != null) {
            e = ck.keys();
            try {
                do {
                    k = (String) e.nextElement();
                    v = (String) ck.get(k);
                    if (!s.equals(""))
                        s += ";";
                    s += k + "=" + v;
                } while (true);
            } catch (NoSuchElementException x) {
            }
            ;
        }

        return s;

    } // end method

    private Hashtable deserializeCookies(String s) {

        int i = 0;
        Hashtable t = new Hashtable();
        String[] e;

        if (s != null) {
            e = s.split("[=;]");
            while (i + 1 < e.length) {
                t.put(e[i], e[i + 1]);
                i += 2;
            }
        }

        return t;

    } // end method

} // end class
