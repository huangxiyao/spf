/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * The comments
 *
 * FILENAME: SurrogateSessionInfo.java
 * PACKAGE : com.hp.serviceportal.framework.mock
 * $Id: SurrogateSessionInfo.java,v 1.1 2007/05/29 08:23:01 marcd Exp $
 * $Log: SurrogateSessionInfo.java,v $
 * Revision 1.1  2007/05/29 08:23:01  marcd
 * add some testing classes
 *
 * Revision 1.1  2007/04/20 07:39:30  marcd
 * rename to follow the main directory and finish up a bit on the unit testing
 *
 */

package com.hp.it.spf.xa.mock.portal;

import com.epicentric.common.website.SessionInfo;

public class SurrogateSessionInfo extends SessionInfo {
    private static final long serialVersionUID = 1L;
    private SurrogateSite currentSite;

    public SurrogateSessionInfo() {
        super(null);
    }

    public SurrogateSite getCurrentSite() {
        return currentSite;
    }
    public void setCurrentSite(SurrogateSite currentSite) {
        this.currentSite = currentSite;
    }

}
