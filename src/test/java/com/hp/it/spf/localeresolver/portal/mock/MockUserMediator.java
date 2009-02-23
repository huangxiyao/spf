/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.mock;

import javax.servlet.http.HttpServletRequest;
import com.epicentric.entity.ChildEntity;
import com.hp.it.spf.localeresolver.portal.getter.IUserMediator;
import com.hp.it.spf.localeresolver.portal.mock.MockUser;

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
