/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import com.hp.it.spf.ac.healthcheck.background.CannotCreateHealthcheckClientException;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckClient;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckLogger;

/**
 * This class tests an open-sign server to determine whether the open-sign is up
 * or not. It expects to be provided with the open-sign server host (or IP
 * address), port, and URL. Its <code>execute</code> method defers to the
 * parent class, but sets the superclass success pattern attribute to the
 * pattern indicating any found document: ^HTTP/1.. 200. Its
 * <code>evaluate</code> method then looks for the pattern in the response
 * message string.
 * <p>
 * 
 * Typically this class is executed only by the <code>HealthcheckDriver</code> -
 * see.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class OpenSignHealthcheckClient extends HealthcheckClient {

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////

    private static String OPEN_SIGN_PATTERN = "^HTTP/1\\.. 200 .*$";

    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>OpenSignHealthcheckClient</code> for the given
     * server hostname or IP address, port number, and URL. This method just
     * passes-thru to the superclass constructor - see.
     * <p>
     */
    public OpenSignHealthcheckClient(String aHost, int aPort, String aURL)
            throws CannotCreateHealthcheckClientException {

        super(aHost, aPort, aURL);

    } // end constructor

    /**
     * Checks for the ITRC online indicator (by sending a <code>/online</code>
     * HTTP request) to evaluate whether the site is open or not. Returns true
     * if the online indicator is found, and false otherwise.
     * <p>
     * 
     * To check for the online indicator, a <code>/online</code> HTTP request
     * is made using the HTTP <code>GET</code> method. If an HTTP 200 response
     * is received, then the online indicator is considered to be found, and
     * true is returned. Otherwise false is returned.
     * <p>
     * 
     * Note: Like all <code>HealthcheckClient</code> subclasses, this one
     * harvests and reuses cookies set by the server. Thus only a single HTTP
     * session on the server will be used no matter how many times you call the
     * <code>OnlineHealthcheckClient</code> <code>evaluate</code> method.
     * <p>
     * 
     * Note: Like all <code>HealthcheckClient</code> subclasses, this one uses
     * the superclass <code>execute</code> method to perform the HTTP
     * interaction. Any <code>BrokenHealthcheckConnectionException</code>
     * thrown from the superclass <code>execute</code> will be caught here and
     * result in a false return.
     * <p>
     */
    public boolean evaluate(String response) {

        Matcher matcher;
        String[] lines;
        String line, m;
        
        HealthcheckLogger.log(this, "Entered evaluate().");

        // If null response - then open sign is definitely not working.
        if (response == null) {
            m = "Detected null response.  Finished evaluate() and returning false.";
            HealthcheckLogger.log(this, m);
            return false;
        }

        // Set the HTTP 200 OK response pattern - this should never fail.
        try {
            this.setSuccessPattern(OPEN_SIGN_PATTERN);
        } catch (PatternSyntaxException e) {
            ;
        }

        // If null pattern - then portal pulse is not evaluatable.
        if (this.pattern == null) {
            m = "Detected null pattern.  Finished evaluate() and returning false.";
            HealthcheckLogger.log(this, m);
            return false;
        }

        m = "Matching pattern [" + this.pattern.pattern()
                + "] to first line in given response.";
        HealthcheckLogger.log(this, m);

        // Next, split the response into lines and attempt to match just the
        // first line. Assume that newline '\n' delimits lines, and lines may
        // (or may not) have trailing carriage-return '\r' characters.
        lines = response.split("\n");
        if (lines.length >= 1) {
            line = lines[0];
            line = line.replaceAll("\r$", "");
            matcher = this.pattern.matcher(line);
            if (matcher.matches()) {
                m = "Found match on open sign success pattern.  Finished evaluate() and returning true.";
                HealthcheckLogger.log(this, m);
                return true;
            }
        }
        m = "No match found for open sign success pattern.  Finished evaluate() and returning false.";
        HealthcheckLogger.log(this, m);
        return false;

    } // end method

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
} // end class
