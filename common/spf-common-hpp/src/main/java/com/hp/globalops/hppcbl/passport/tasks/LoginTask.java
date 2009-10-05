package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.LoginRequestElement;
import com.hp.globalops.hppcbl.webservice.LoginResponseElement;

/**
 * Created by IntelliJ IDEA.
 * User: millerand
 * Date: Aug 23, 2004
 * Time: 11:06:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginTask extends Task {

    public static final String OPERATION_NAME="login";
    private LoginRequestElement requestElement;

    public LoginTask() {
        super();
    }

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String userId = (String) args.get("userId");
        String password = (String) args.get("password");
        requestElement = new LoginRequestElement();
        requestElement.setUserId(userId);
        requestElement.setPassword(password);
    }

    protected Object getRequestElement() throws TaskExecutionException {
        return requestElement;
    }

    protected Object getResponseElement() throws TaskExecutionException {
        return new LoginResponseElement();
    }

}
