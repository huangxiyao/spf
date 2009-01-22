/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.http;

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
