package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.ChangeUserIdRequestElement;
import com.hp.globalops.hppcbl.webservice.ChangeUserIdResponseElement;

public class ChangeUserIdTask extends Task {

    public static final String OPERATION_NAME="changeUserId";
    private ChangeUserIdRequestElement requestElement;

	public ChangeUserIdTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new ChangeUserIdResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("sessionToken");
        String newUserId = (String) args.get("newUserId");
        String currentPassword = (String) args.get("currentPassword");

        requestElement = new ChangeUserIdRequestElement();
        requestElement.setSessionToken(sessionToken);
        requestElement.setNewUserId(newUserId);
        requestElement.setCurrentPassword(currentPassword);
	}

}
