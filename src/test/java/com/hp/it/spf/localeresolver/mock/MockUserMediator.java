/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: MockUserMediator.java
 * PACKAGE : com.hp.it.spf.localeresolver.filter
 * $Id: MockUserMediator.java,v 1.4 2007/05/29 08:23:49 marcd Exp $
 * $Log: MockUserMediator.java,v $
 * Revision 1.4  2007/05/29 08:23:49  marcd
 * modified mock location
 *
 * Revision 1.3  2007/04/20 07:39:30  marcd
 * rename to follow the main directory and finish up a bit on the unit testing
 *
 * Revision 1.2  2007/04/17 01:52:02  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.mock;

import javax.servlet.http.HttpServletRequest;
import com.epicentric.entity.ChildEntity;
import com.hp.it.spf.localeresolver.setter.IUserMediator;
import com.hp.it.spf.xa.common.portal.mock.MockUser;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class MockUserMediator implements IUserMediator {
    private ChildEntity currentUser = new MockUser();
    private boolean guestUser;

    public void setCurrentUser(ChildEntity currentUser) {
        this.currentUser = currentUser;
    }
    public ChildEntity getCurrentUser(HttpServletRequest request) {
        return this.currentUser;
    }

    public boolean isGuestUser(ChildEntity user) {
        return guestUser;
    }
    public void setGuestUser(boolean guestUser) {
        this.guestUser = guestUser;
    }

}
