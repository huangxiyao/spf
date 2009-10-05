package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.CreateUserRequestElement;
import com.hp.globalops.hppcbl.webservice.CreateUserResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileCredentials;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;

public class CreateUserTask extends Task {

    public static final String OPERATION_NAME="createUser";
    private CreateUserRequestElement requestElement;

    public CreateUserTask() {
        super();
    }

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String userId = (String) args.get("userId");
        ProfileCore profileCore = (ProfileCore) args.get("ProfileCore");
        ProfileExtended profileExtended = (ProfileExtended) args.get("ProfileExtended");
        ProfileCredentials profileCredentials = (ProfileCredentials) args.get("ProfileCredentials");

        requestElement = new CreateUserRequestElement();
        requestElement.setUserId(userId);
        requestElement.setProfileCore(profileCore);
        requestElement.setProfileCredentials(profileCredentials);
        if(profileExtended != null)
        	requestElement.setProfileExtended(profileExtended);
    }

    protected Object getRequestElement() throws TaskExecutionException {
        return requestElement;
    }

    protected Object getResponseElement() throws TaskExecutionException {
        return new CreateUserResponseElement();
    }

}
