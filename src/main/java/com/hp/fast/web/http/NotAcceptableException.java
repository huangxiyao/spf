/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/http/NotAcceptableException.java,v 1.1 2007/04/03 10:05:09 marcd Exp $ */

package com.hp.fast.web.http;

import javax.servlet.ServletException;

/**
 * A mutually acceptable content representation could not be negotiated.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class NotAcceptableException extends ServletException {
    private static final long serialVersionUID = -5085096373300726468L;

    /**
     * Creates an exception.
     * 
     * @param message
     *            the reason for the exception.
     */
    public NotAcceptableException(String message) {
        super(message);
    }
}
