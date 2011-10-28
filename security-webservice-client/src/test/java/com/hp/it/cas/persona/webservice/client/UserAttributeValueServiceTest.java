package com.hp.it.cas.persona.webservice.client;

import java.net.URL;
import java.util.Set;

import org.junit.Test;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;

public class UserAttributeValueServiceTest {

    private static final String USER_NAME = "APP-111319";
    private static final String PASSWORD = "123qwe!@#QWE";
    private static final String  WEB_SERVICE_URL = "https://sasuft1cl2.austin.hp.com:21978/cas-persona-webservice";
    private static final IUserIdentifier userIdentifier = new UserIdentifier(EUserIdentifierType.EMPLOYEE_SIMPLIFIED_EMAIL_ADDRESS, "quintin.may@hp.com");
    private static final IUserAttributeValueService SERVICE;
    static {
        try {
            SERVICE = new UserAttributeValueService(new URL(WEB_SERVICE_URL), USER_NAME, PASSWORD);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    @Test
    public void foo() {
        Set<IUserAttributeValue> values = SERVICE.findUserAttributeValues(userIdentifier);
        System.out.println(values);
    }
}
