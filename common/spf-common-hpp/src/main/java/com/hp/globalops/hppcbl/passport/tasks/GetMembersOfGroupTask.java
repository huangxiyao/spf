package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetGroupInfoResponseElement;
import com.hp.globalops.hppcbl.webservice.GetMembersOfGroupRequestElement;

public class GetMembersOfGroupTask extends Task {

    public static final String OPERATION_NAME="getMembersOfGroup";
    private GetMembersOfGroupRequestElement requestElement;

	public GetMembersOfGroupTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetGroupInfoResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        String groupName = (String) args.get("groupName");
        String roleName = (String) args.get("roleName");

        requestElement = new GetMembersOfGroupRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setGroupName(groupName);
        requestElement.setRoleName(roleName);
	}

}
