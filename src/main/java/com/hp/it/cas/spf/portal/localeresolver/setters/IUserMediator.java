/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: UserMediator.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.setters
 * $Id: IUserMediator.java,v 1.1 2007/05/18 07:31:53 marcd Exp $
 * $Log: IUserMediator.java,v $
 * Revision 1.1  2007/05/18 07:31:53  marcd
 * interface
 *
 * Revision 1.3  2007/05/17 07:12:21  marcd
 * add javadoc
 *
 * Revision 1.2  2007/04/17 01:41:32  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.setters;

import javax.servlet.http.HttpServletRequest;
import com.epicentric.entity.ChildEntity;

/**
 * The mediator class is responsible for providing users and
 * information about those users.
 * This class exists mainly to wrap some utility classes, and
 * also the 'final' implementation of the vignette User class
 * so we can do a bit of unit testing.
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public interface IUserMediator {
    /**
     * Pulls the current user entity out of the session
     * @param request the http servlet request
     * @return vignette generalized model class
     */
    ChildEntity getCurrentUser(HttpServletRequest request);
    
    /**
     * Determines whether the given entity is a guest user
     * @param user any object which implements the child entity interface
     * @return true if the entity is a guest user
     */
    boolean isGuestUser(ChildEntity user);
}
