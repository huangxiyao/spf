package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetUserGroupsRequestElement;
import com.hp.globalops.hppcbl.webservice.GetUserGroupsResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;

public class GetUserGroupsTask extends Task {

    public static final String OPERATION_NAME="getUserGroups";
    private GetUserGroupsRequestElement requestElement;

	public GetUserGroupsTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetUserGroupsResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        ProfileIdentity profileIdentity = (ProfileIdentity) args.get("profileIdentity");

        requestElement = new GetUserGroupsRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setProfileIdentity(profileIdentity);
	}

}
