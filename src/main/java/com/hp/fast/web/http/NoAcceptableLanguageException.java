/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/http/NoAcceptableLanguageException.java,v 1.1 2007/04/03 10:05:09 marcd Exp $ */

package com.hp.fast.web.http;

import java.util.Collection;
import java.util.Collections;

/**
 * A mutually acceptable language could not be negotiated.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class NoAcceptableLanguageException extends NotAcceptableException {
    private static final long serialVersionUID = 9130195254146942908L;

    private final Collection acceptedLanguages;

    /**
     * Creates an exception.
     * 
     * @param message
     *            the reason for the exception.
     * @param acceptedLanguages
     *            acceptable languages
     */
    public NoAcceptableLanguageException(String message,
            Collection acceptedLanguages) {
        super(message);
        this.acceptedLanguages = Collections
                .unmodifiableCollection(acceptedLanguages);
    }

    /**
     * Returns the acceptable languages.
     * 
     * @return the acceptable languages.
     */
    public Collection getAcceptedValues() {
        return acceptedLanguages;
    }
}
