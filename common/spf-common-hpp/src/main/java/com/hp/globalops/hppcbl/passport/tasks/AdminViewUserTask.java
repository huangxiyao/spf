package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.AdminViewUserRequestElement;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResponseElement;
import com.hp.globalops.hppcbl.webservice.UserLookupCriteria;

/**
 * Created by IntelliJ IDEA.
 * User: millerand
 * Date: Aug 23, 2004
 * Time: 11:06:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminViewUserTask extends Task {

    public static final String OPERATION_NAME="adminViewUser";
    private AdminViewUserRequestElement requestElement;

    public AdminViewUserTask() {
        super();
    }

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
		String strAdminSessionToken = (String)args.get("adminSessionToken");
		UserLookupCriteria lookupCriteria = (UserLookupCriteria)args.get("userLookupCriteria");

		requestElement = new AdminViewUserRequestElement();
		requestElement.setAdminSessionToken(strAdminSessionToken);

		requestElement.setUserLookupCriteria(lookupCriteria);
    }

    protected Object getRequestElement() throws TaskExecutionException {
        return requestElement;
    }

    protected Object getResponseElement() throws TaskExecutionException {
        return new AdminViewUserResponseElement();
    }

}
