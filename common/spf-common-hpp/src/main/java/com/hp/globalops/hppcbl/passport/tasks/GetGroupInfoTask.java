package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetGroupInfoRequestElement;
import com.hp.globalops.hppcbl.webservice.GetGroupInfoResponseElement;

public class GetGroupInfoTask extends Task {

    public static final String OPERATION_NAME="getGroupInfo";
    private GetGroupInfoRequestElement requestElement;

	public GetGroupInfoTask() {
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

        requestElement = new GetGroupInfoRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setGroupName(groupName);
        requestElement.setRoleName(roleName);
	}

}
