package com.hp.globalops.hppcbl.test;

import java.util.ArrayList;
import java.util.Iterator;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.globalops.hppcbl.webservice.*;

public class ClientJUnitTest extends BaseJUnitTestCase {
/*
    public void testRecoverUserId() throws Exception {
        try {

            // ws.setSystemLangCode(langCode);

            // call web service to get the user id

            RecoverUserIdResponseElement wsResponse = ws
                    .recoverUserId("qi-jun.he@hp.com");
            ProfileIdentity pf = wsResponse.getProfileIdentity();
            String userId = pf.getUserId();
            System.out.println("userId:" + userId);
            assertTrue(userId != null);
        } catch (PassportServiceException psEx) {

            ArrayList faults = psEx.getFaults();
            // loop through the hpp faults obj, if any ftype=2 found then go to
            // system error
            for (Iterator it = faults.iterator(); it.hasNext();) {
                Fault fault = (Fault)it.next();
                System.out.println(fault.toString());
            }
            assertTrue(false);
        }

    }*/

    public void testChangePassword() throws Exception {
        try {

            // ws.setSystemLangCode(langCode);

            // call web service to get the user id

            ChangePasswordResponseElement wsResponse = ws.changePassword(
                    "liuye", "1qaz2WSX", "1qaz2WSX", "1qaz2WSX");
            String pfId = wsResponse.getProfileId();

            System.out.println("profile Id:" + pfId);
            assertTrue(pfId != null);
        } catch (PassportServiceException psEx) {

            ArrayList faults = psEx.getFaults();
            // loop through the hpp faults obj, if any ftype=2 found then go to
            // system error
            for (Iterator it = faults.iterator(); it.hasNext();) {
                Fault fault = (Fault)it.next();
                System.out.println(fault.toString());
            }
            assertTrue(false);
        }

    }

}
