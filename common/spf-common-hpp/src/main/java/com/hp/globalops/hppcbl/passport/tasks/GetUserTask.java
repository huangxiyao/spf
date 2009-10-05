package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetUserRequestElement;
import com.hp.globalops.hppcbl.webservice.GetUserResponseElement;

/**
 * Created by IntelliJ IDEA.
 * User: millerand
 * Date: Aug 23, 2004
 * Time: 11:06:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class GetUserTask extends Task {

    public static final String OPERATION_NAME="getUser";
    private GetUserRequestElement requestElement;

    public GetUserTask() {
        super();
    }

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("sessionToken");
        requestElement = new GetUserRequestElement();
        requestElement.setSessionToken(sessionToken);
        }

    protected Object getRequestElement() throws TaskExecutionException {
        return requestElement;
    }

    protected Object getResponseElement() throws TaskExecutionException {
        return new GetUserResponseElement();
    }

}
