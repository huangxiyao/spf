/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.http;

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
