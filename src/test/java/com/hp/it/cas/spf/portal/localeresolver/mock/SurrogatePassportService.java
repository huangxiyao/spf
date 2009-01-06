/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: SurrogatePassportService.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.filter
 * $Id: SurrogatePassportService.java,v 1.2 2007/04/17 01:52:02 marcd Exp $
 * $Log: SurrogatePassportService.java,v $
 * Revision 1.2  2007/04/17 01:52:02  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.mock;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.webservice.GetUserCoreResponseElement;
import com.hp.globalops.hppcbl.webservice.ModifyUserResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class SurrogatePassportService extends PassportService {
    private GetUserCoreResponseElement userCoreResp;
    private ModifyUserResponseElement userResp = new ModifyUserResponseElement();
    private boolean gotUserCore;
    private boolean modifiedUser;

    public boolean isGotUserCore() {
        return gotUserCore;
    }

    public void setGotUserCore(boolean gotUserCore) {
        this.gotUserCore = gotUserCore;
    }

    public boolean isModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(boolean modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public GetUserCoreResponseElement getUserCoreResp() {
        return userCoreResp;
    }

    public void setUserCoreResp(GetUserCoreResponseElement userCoreResp) {
        this.userCoreResp = userCoreResp;
    }

    public ModifyUserResponseElement getUserResp() {
        return userResp;
    }

    public void setUserResp(ModifyUserResponseElement userResp) {
        this.userResp = userResp;
    }

    public GetUserCoreResponseElement getUserCore(String sessionToken)
            throws PassportServiceException {
        gotUserCore = true;
        GetUserCoreResponseElement resp = new GetUserCoreResponseElement();
        resp.setProfileCore(new ProfileCore());
        return resp;
    }

    public ModifyUserResponseElement modifyUser(String sessionToken,
            ProfileCore profileCore, ProfileExtended profileExtended)
            throws PassportServiceException {
        modifiedUser = true;
        return this.userResp;
    }
}
