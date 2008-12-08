package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.AdminCreateUserRequestElement;
import com.hp.globalops.hppcbl.webservice.AdminCreateUserResponseElement;
import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;

public class AdminCreateUserTask extends Task {

    public static final String OPERATION_NAME="adminCreateUser";
    private AdminCreateUserRequestElement requestElement;

	public AdminCreateUserTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new AdminCreateUserResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        String userId = (String) args.get("userId");
        String groupName = (String) args.get("groupName");
        String roleName = (String) args.get("roleName");
        EmailTemplate[] emailTemplate = (EmailTemplate[]) args.get("emailTemplate");
        ProfileCore profileCore = (ProfileCore) args.get("profileCore");
        ProfileExtended profileExtended = (ProfileExtended) args.get("profileExtended");

        requestElement = new AdminCreateUserRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setUserId(userId);
        if(groupName != null)
       		requestElement.setGroupName(groupName);
        if(roleName != null)
       		requestElement.setRoleName(roleName);
        if(emailTemplate != null)
       		requestElement.setEmailTemplate(emailTemplate);
        requestElement.setProfileCore(profileCore);
        if(profileExtended != null)
       		requestElement.setProfileExtended(profileExtended);
	}

}
