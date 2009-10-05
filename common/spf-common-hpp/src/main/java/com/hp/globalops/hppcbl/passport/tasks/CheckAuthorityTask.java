package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.CheckAuthorityRequestElement;
import com.hp.globalops.hppcbl.webservice.CheckAuthorityResponseElement;

public class CheckAuthorityTask extends Task {

    public static final String OPERATION_NAME="checkAuthority";
    private CheckAuthorityRequestElement requestElement;

	public CheckAuthorityTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new CheckAuthorityResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        String userId = (String) args.get("userId");

        requestElement = new CheckAuthorityRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken) ;
        requestElement.setUserId(userId);
	}

}
