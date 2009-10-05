package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.MigrateUserRequestElement;
import com.hp.globalops.hppcbl.webservice.MigrateUserResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileCredentials;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;

public class MigrateUserTask extends Task {

    public static final String OPERATION_NAME="migrateUser";
    private MigrateUserRequestElement requestElement;

	public MigrateUserTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new MigrateUserResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String sessionToken = (String) args.get("SessionToken");
        String applicationRefId = (String) args.get("ApplicationRefId");
        ProfileCredentials profileCredentials = (ProfileCredentials) args.get("ProfileCredentials");
        ProfileCore profileCore = (ProfileCore) args.get("ProfileCore");
        ProfileExtended profileExtended = (ProfileExtended) args.get("ProfileExtended");

        requestElement = new MigrateUserRequestElement();
        requestElement.setSessionToken(sessionToken);
        requestElement.setApplicationRefId(applicationRefId);
        requestElement.setProfileCredentials(profileCredentials);
        requestElement.setProfileCore(profileCore);
        if(profileExtended != null)
        	requestElement.setProfileExtended(profileExtended);

	}

}
