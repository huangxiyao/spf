package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.AdminResetPasswordRequestElement;
import com.hp.globalops.hppcbl.webservice.AdminResetPasswordResponseElement;
import com.hp.globalops.hppcbl.webservice.EmailTemplate;

public class AdminResetPasswordTask extends Task {

    public static final String OPERATION_NAME="adminResetPassword";
    private AdminResetPasswordRequestElement requestElement;

	public AdminResetPasswordTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new AdminResetPasswordResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String adminSessionToken = (String) args.get("adminSessionToken");
        String profileId = (String) args.get("profileId");
        String url = (String)args.get("url");
        System.out.println("In init method, url = " + url);
        EmailTemplate[] emailTemplate = (EmailTemplate[]) args.get("emailTemplate");

        requestElement = new AdminResetPasswordRequestElement();
        requestElement.setAdminSessionToken(adminSessionToken);
        requestElement.setProfileId(profileId);
        requestElement.setEmailTemplate(emailTemplate);
        requestElement.setUrl(url);
	}

}
