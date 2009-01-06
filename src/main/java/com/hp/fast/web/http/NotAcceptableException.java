/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

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
