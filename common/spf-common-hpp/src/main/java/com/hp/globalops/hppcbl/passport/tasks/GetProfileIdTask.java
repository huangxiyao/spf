package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetProfileIdRequestElement;
import com.hp.globalops.hppcbl.webservice.GetProfileIdResponseElement;

public class GetProfileIdTask extends Task {

    public static final String OPERATION_NAME="getProfileId";
    private GetProfileIdRequestElement requestElement;

    public GetProfileIdTask() {
        super();
    }

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetProfileIdResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String userId = (String) args.get("userId");

        requestElement = new GetProfileIdRequestElement();
        requestElement.setUserId(userId);

	}

}
