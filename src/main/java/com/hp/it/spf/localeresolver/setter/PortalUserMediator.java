/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.ChildEntity;
import com.epicentric.user.User;

/**
 * Default {@link IUserMediator} implementaion of the user mediatory which
 * provides access to user information.
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalUserMediator implements IUserMediator {

    /**
     * @param request the http servlet request
     * @return a vignette user object
     */
    public ChildEntity getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return SessionUtils.getCurrentUser(request.getSession(false));
    }

    /**
     * @param user a vignette user object
     * @return true for guest users, false for standard users
     */
    public boolean isGuestUser(ChildEntity user) {
        return ((User) user).isGuestUser();
    }

}
