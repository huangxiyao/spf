/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/http/NoAcceptableMediaTypeException.java,v 1.1 2007/04/03 10:05:09 marcd Exp $ */

package com.hp.fast.web.http;

import java.util.Collection;
import java.util.Collections;

/**
 * A mutually acceptable media type could not be negotiated.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class NoAcceptableMediaTypeException extends NotAcceptableException {
    private static final long serialVersionUID = -578120839934577067L;

    private final Collection acceptedMediaTypes;

    /**
     * Creates an exception.
     * 
     * @param message
     *            the reason for the exception.
     * @param acceptedMediaTypes
     *            acceptable media types.
     */
    public NoAcceptableMediaTypeException(String message,
            Collection acceptedMediaTypes) {
        super(message);
        this.acceptedMediaTypes = Collections
                .unmodifiableCollection(acceptedMediaTypes);
    }

    /**
     * Returns the acceptable media types.
     * 
     * @return the acceptable media types.
     */
    public Collection getAcceptedValues() {
        return acceptedMediaTypes;
    }
}
