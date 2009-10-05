package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.IsMemberOfGroupRequestElement;
import com.hp.globalops.hppcbl.webservice.IsMemberOfGroupResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;

public class IsMemberOfGroupTask extends Task {

    public static final String OPERATION_NAME="isMemberOfGroup";
    private IsMemberOfGroupRequestElement requestElement;

	public IsMemberOfGroupTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new IsMemberOfGroupResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("sessionToken");
        ProfileIdentity profileIdentity = (ProfileIdentity) args.get("profileIdentity");
        String groupName = (String) args.get("groupName");
        String roleName = (String) args.get("roleName");

        requestElement = new IsMemberOfGroupRequestElement();
        requestElement.setSessionToken(sessionToken);
        requestElement.setProfileIdentity(profileIdentity);
        requestElement.setGroupName(groupName);
        requestElement.setRoleName(roleName);
	}

}
