package com.hp.it.spf.xa.common.portal.surrogate;

import java.util.Collection;

import com.epicentric.authorization.AuthorizationSpace;
import com.epicentric.authorization.AuthorizationSpaceException;
import com.epicentric.authorization.Permissionable;
import com.epicentric.common.Describable;
import com.epicentric.repository.RecursivelyShareableRepositoryElement;
import com.epicentric.template.Style;

public class SurrogateStyle extends Style implements Describable, Cloneable,
        RecursivelyShareableRepositoryElement, Permissionable {

    public int getOwnerID() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setOwnerID(int arg0) {
        // TODO Auto-generated method stub

    }

    public Collection getChildShareableRepositoryElements() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUIDType() {
        // TODO Auto-generated method stub
        return null;
    }

    public AuthorizationSpace getAuthorizationSpace(String arg0)
            throws AuthorizationSpaceException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasEndUserPermissions() {
        // TODO Auto-generated method stub
        return false;
    }

}
