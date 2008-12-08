package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetUserCoreRequestElement;
import com.hp.globalops.hppcbl.webservice.GetUserCoreResponseElement;

public class GetUserCoreTask extends Task {

    public static final String OPERATION_NAME="getUserCore";
    private GetUserCoreRequestElement requestElement;

	public GetUserCoreTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetUserCoreResponseElement() ;
	}

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("sessionToken");
        requestElement = new GetUserCoreRequestElement();
        requestElement.setSessionToken(sessionToken);
    }

}
