package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.ValidateSessionTokenRequestElement;
import com.hp.globalops.hppcbl.webservice.ValidateSessionTokenResponseElement;

public class ValidateSessionTokenTask extends Task {

    public static final String OPERATION_NAME="validateSessionToken";
    private ValidateSessionTokenRequestElement requestElement;

	public ValidateSessionTokenTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new ValidateSessionTokenResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("sessionToken");

        requestElement = new ValidateSessionTokenRequestElement();
        requestElement.setSessionToken(sessionToken);
	}

}
