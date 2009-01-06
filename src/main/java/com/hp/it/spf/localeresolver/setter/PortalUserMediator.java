/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: VignetteUserMediator.java
 * PACKAGE : com.hp.it.spf.localeresolver.setter
 * $Id: PortalUserMediator.java,v 1.3 2007/05/18 07:31:53 marcd Exp $
 * $Log: PortalUserMediator.java,v $
 * Revision 1.3  2007/05/18 07:31:53  marcd
 * interface
 *
 * Revision 1.2  2007/05/17 07:12:21  marcd
 * add javadoc
 *
 * Revision 1.1  2007/04/20 07:38:26  marcd
 * rename
 *
 * Revision 1.2  2007/04/17 01:41:32  marcd
 * add stuff for javadoc
 *
 *
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
