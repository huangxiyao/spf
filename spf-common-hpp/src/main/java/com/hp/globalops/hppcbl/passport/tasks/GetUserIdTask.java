package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetUserIdRequestElement;
import com.hp.globalops.hppcbl.webservice.GetUserIdResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;

public class GetUserIdTask extends Task {

    public static final String OPERATION_NAME="getUserId";
    private GetUserIdRequestElement requestElement;

	public GetUserIdTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetUserIdResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        ProfileIdentity profileIdentity = (ProfileIdentity) args.get("ProfileIdentity");

        requestElement = new GetUserIdRequestElement();
        requestElement.setProfileId(profileIdentity.getProfileId());

	}

}
