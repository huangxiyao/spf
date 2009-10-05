package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;
import com.hp.globalops.hppcbl.webservice.RemoveUserFromGroupRequestElement;
import com.hp.globalops.hppcbl.webservice.RemoveUserFromGroupResponseElement;

public class RemoveUserFromGroupTask extends Task {

    public static final String OPERATION_NAME="removeUserFromGroup";
    private RemoveUserFromGroupRequestElement requestElement;

	public RemoveUserFromGroupTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new RemoveUserFromGroupResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        ProfileIdentity profileIdentity = (ProfileIdentity) args.get("profileIdentity");
        String groupName = (String) args.get("groupName");
        String roleName = (String) args.get("roleName");
        EmailTemplate[] emailTemplate = (EmailTemplate[]) args.get("emailTemplate");

        requestElement = new RemoveUserFromGroupRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setProfileIdentity(profileIdentity);
        requestElement.setGroupName(groupName);
        requestElement.setRoleName(roleName);
        if(emailTemplate != null)
        	requestElement.setEmailTemplate(emailTemplate);
	}

}
