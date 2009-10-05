package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.ChangePasswordRequestElement;
import com.hp.globalops.hppcbl.webservice.ChangePasswordResponseElement;

public class ChangePasswordTask extends Task {

    public static final String OPERATION_NAME="changePassword";
    private ChangePasswordRequestElement requestElement;

	public ChangePasswordTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new ChangePasswordResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String userId = (String) args.get("userId");
        String currentPassword = (String) args.get("currentPassword");
        String newPassword = (String) args.get("newPassword");
        String newPasswordConfirm = (String) args.get("newPasswordConfirm");

        requestElement = new ChangePasswordRequestElement();
        requestElement.setUserId(userId);
        requestElement.setCurrentPassword(currentPassword);
        requestElement.setNewPassword(newPassword);
        requestElement.setNewPasswordConfirm(newPasswordConfirm);
 	}

}
