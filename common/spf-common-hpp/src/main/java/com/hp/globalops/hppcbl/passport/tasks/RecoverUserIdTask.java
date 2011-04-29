package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.RecoverUserIdRequestElement;
import com.hp.globalops.hppcbl.webservice.RecoverUserIdResponseElement;

public class RecoverUserIdTask extends Task {

    public static final String OPERATION_NAME="recoverUserId";
    private RecoverUserIdRequestElement requestElement;

    public RecoverUserIdTask() {
        super();
    }

    public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String emailAddress = (String) args.get("emailAddress");
        //String firstName = (String) args.get("firstName");
        //String lastName = (String) args.get("lastName");

        requestElement = new RecoverUserIdRequestElement();
        requestElement.setEmailAddress(emailAddress);
        //requestElement.setFirstName(firstName);
        //if(lastName != null)
        //	 requestElement.setLastName(lastName);

        }

    protected Object getRequestElement() throws TaskExecutionException {
        return requestElement;
    }

    protected Object getResponseElement() throws TaskExecutionException {
        return new RecoverUserIdResponseElement();
    }

}
