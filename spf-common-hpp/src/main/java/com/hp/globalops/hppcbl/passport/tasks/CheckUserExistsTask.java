package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.CheckUserExistsRequestElement;
import com.hp.globalops.hppcbl.webservice.CheckUserExistsResponseElement;

public class CheckUserExistsTask extends Task {

    public static final String OPERATION_NAME="checkUserExists";
    private CheckUserExistsRequestElement requestElement;

	public CheckUserExistsTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new CheckUserExistsResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String email = (String) args.get("email");
        String userId = (String) args.get("userId");

        requestElement = new CheckUserExistsRequestElement();
        if(email != null)
        	requestElement.setEmail(email) ;
        if(userId != null)
        	requestElement.setUserId(userId);
	}

}
