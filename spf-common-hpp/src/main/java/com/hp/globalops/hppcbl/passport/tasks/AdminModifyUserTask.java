package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.AdminModifyUserRequestElement;
import com.hp.globalops.hppcbl.webservice.AdminModifyUserResponseElement;
import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;

public class AdminModifyUserTask extends Task {

    public static final String OPERATION_NAME="adminModifyUser";
    private AdminModifyUserRequestElement requestElement;

	public AdminModifyUserTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new AdminModifyUserResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        String profileId = (String) args.get("profileId");
        EmailTemplate emailTemplate = (EmailTemplate) args.get("emailTemplate");
        ProfileCore profileCore = (ProfileCore) args.get("profileCore");
        ProfileExtended profileExtended = (ProfileExtended) args.get("profileExtended");

        requestElement = new AdminModifyUserRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setProfileId(profileId);
        if(emailTemplate != null)
       		requestElement.setEmailTemplate(emailTemplate);
        requestElement.setProfileCore(profileCore);
        if(profileExtended != null)
       		requestElement.setProfileExtended(profileExtended);
	}

}
