/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/http/NoAcceptableCharacterSetException.java,v 1.1 2007/04/03 10:05:09 marcd Exp $ */

package com.hp.fast.web.http;

import java.util.Collection;
import java.util.Collections;

/**
 * A mutually acceptable character set could not be negotiated.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class NoAcceptableCharacterSetException extends NotAcceptableException {
    private static final long serialVersionUID = 1025514882649493375L;

    private final Collection acceptedCharacterSets;

    /**
     * Creates an exception.
     * 
     * @param message
     *            the reason for the exception.
     * @param acceptedCharacterSets
     *            acceptable character sets.
     */
    public NoAcceptableCharacterSetException(String message,
            Collection acceptedCharacterSets) {
        super(message);
        this.acceptedCharacterSets = Collections
                .unmodifiableCollection(acceptedCharacterSets);
    }

    /**
     * Returns the acceptable character sets.
     * 
     * @return the acceptable character sets.
     */
    public Collection getAcceptedValues() {
        return acceptedCharacterSets;
    }
}
