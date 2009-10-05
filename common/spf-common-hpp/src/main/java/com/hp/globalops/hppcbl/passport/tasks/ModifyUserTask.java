package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.ModifyUserRequestElement;
import com.hp.globalops.hppcbl.webservice.ModifyUserResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;

public class ModifyUserTask extends Task {

    public static final String OPERATION_NAME="modifyUser";
    private ModifyUserRequestElement requestElement;

    public ModifyUserTask() {
        super();
    }

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("SessionToken");
        ProfileCore profileCore = (ProfileCore) args.get("ProfileCore");
        ProfileExtended profileExtended = (ProfileExtended) args.get("ProfileExtended");

        requestElement = new ModifyUserRequestElement();
        requestElement.setSessionToken(sessionToken);
        if(profileCore != null)
        	requestElement.setProfileCore(profileCore);
        if(profileExtended != null)
        	requestElement.setProfileExtended(profileExtended);

        }

    protected Object getRequestElement() throws TaskExecutionException {
        return requestElement;
    }

    protected Object getResponseElement() throws TaskExecutionException {
        return new ModifyUserResponseElement();
    }

}
