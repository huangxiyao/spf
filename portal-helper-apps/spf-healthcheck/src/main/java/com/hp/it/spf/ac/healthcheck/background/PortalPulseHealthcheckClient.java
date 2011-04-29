/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import java.lang.String;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import com.hp.it.spf.ac.healthcheck.background.CannotCreateHealthcheckClientException;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckClient;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckLogger;

/**
 * This class tests a portal server to determine whether the portal pulse page
 * is working or not. It expects to be provided with the portal server host (or
 * IP address), port, URL, and success pattern for the portal pulse page. Its
 * <code>execute</code> method defers to the parent class. Its
 * <code>evaluate</code> method looks for the pattern in the response message
 * string.
 * <p>
 * 
 * Typically this class is executed only by the <code>HealthcheckDriver</code> -
 * see.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class PortalPulseHealthcheckClient extends HealthcheckClient {

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>PortalPulseHealthcheckClient</code> for the
     * given server hostname (or IP address), port number, and URL. This method
     * is a pass-thru to the superclass constructor - see.
     * <p>
     */
    public PortalPulseHealthcheckClient(String aHost, int aPort, String aUrl)
            throws CannotCreateHealthcheckClientException {

        super(aHost, aPort, aUrl);

    } // end constructor

    /**
     * Evaluates the given response string (assumed to be an HTTP response
     * message from the portal pulse page healthcheck), returning true if the
     * portal pulse page worked, and false otherwise.
     * <p>
     * 
     * The success pattern attribute in the superclass is assumed to carry the
     * pattern (ie Java regular expression) which indicates a success portal
     * pulse page. Specifically, if the pattern matches any <b>part</b> (ie
     * substring) of any <b>line</b> (delimited by newline and optional
     * carriage-return characters) in the response, then true is returned.
     * Otherwise false is returned. If the pattern is unset, or the given
     * response string is null, false is also returned in these cases.
     * <p>
     * 
     * <blockquote> Note the pattern automatically looks for a substring match.
     * So your pattern string does not need to begin and end with ".*" to get a
     * substring-match effect - this will happen automatically. (To match a
     * whole line, beginning of line, or end of line, you can still use the "^"
     * and "$" metacharacters in the pattern string, as usual.) Matching across
     * line boundaries is not supported by this class. </blockquote>
     * <p>
     * 
     * @param <code>String</code> is the HTTP response message string - typically
     *            you pass this exactly as it is returned by the
     *            <code>execute</code> method.
     */
    public boolean evaluate(String response) {

        Matcher matcher;
        Pattern subPattern;
        String[] lines;
        String line, m;
        int i = 0;

        HealthcheckLogger.log(this, "Entered evaluate().");

        // If null response - then portal pulse is definitely not working.
        if (response == null) {
            m = "Detected null response.  Finished evaluate() and returning false.";
            HealthcheckLogger.log(this, m);
            return false;
        }

        // If null pattern - then portal pulse is not evaluatable.
        if (this.pattern == null) {
            m = "Detected null pattern.  Finished evaluate() and returning false.";
            HealthcheckLogger.log(this, m);
            return false;
        }

        // Treat the portal pulse success pattern as a pattern for a substring
        // within a line. First, recompile the pattern, ensuring it has ".*"
        // before and after it (this will make it to be treated as a substring
        // pattern). This should never cause a PatternSyntaxException.

        subPattern = this.pattern;
        try {
            subPattern = Pattern.compile("^.*" + subPattern.pattern() + ".*$");
        } catch (PatternSyntaxException e) {
            ;
        }

        m = "Matching pattern [" + this.pattern.pattern()
                + "] to each line in given response.";
        HealthcheckLogger.log(this, m);

        // Next, split the response into lines and attempt to match each line at
        // a time, until successful or all lines have been exhausted. Assume
        // that newline '\n' delimits lines, and lines may (or may not) have
        // trailing carriage-return '\r' characters.
        lines = response.split("\n");
        while (i < lines.length) {
            line = lines[i++];
            line = line.replaceAll("\r$", "");
            matcher = subPattern.matcher(line);
            if (matcher.matches()) {
                m = "Found match on portal pulse success pattern.  Finished evaluate() and returning true.";
                HealthcheckLogger.log(this, m);
                return true;
            }
        }
        m = "No match found for portal pulse success pattern.  Finished evaluate() and returning true.";
        HealthcheckLogger.log(this, m);
        return false;

    } // end method

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
} // end class
