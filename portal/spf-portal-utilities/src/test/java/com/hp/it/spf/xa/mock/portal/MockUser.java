/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.mock.portal;

import java.io.Serializable;
import java.util.Set;
import com.epicentric.authorization.AuthorizationException;
import com.epicentric.authorization.Principal;
import com.epicentric.authorization.PrincipalSet;
import com.epicentric.entity.ChildEntity;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;
import com.epicentric.entity.ParentEntity;
import com.epicentric.entity.UniquePropertyValueConflictException;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class MockUser implements ChildEntity, Principal, Serializable {
    private static final long serialVersionUID = 1L;
    private String uid = "epi:guest";

    public void addParent(ParentEntity arg0) throws EntityPersistenceException {
        // TODO Auto-generated method stub

    }

    public boolean canAddParent(ParentEntity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canRemoveParent(ParentEntity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public Set getParents(EntityType arg0, boolean arg1)
            throws EntityPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasParent(ParentEntity arg0, boolean arg1)
            throws EntityPersistenceException {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeParent(ParentEntity arg0)
            throws EntityPersistenceException {
        // TODO Auto-generated method stub

    }

    public void delete() throws EntityPersistenceException {
        // TODO Auto-generated method stub

    }

    public EntityType getEntityType() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getProperty(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUID() {
        return this.uid;
    }

    public String getUIDType() {
        // TODO Auto-generated method stub
        return null;
    }

    public void save() throws EntityPersistenceException {
        // TODO Auto-generated method stub

    }

    public void setProperty(String arg0, Object arg1)
            throws UniquePropertyValueConflictException,

            EntityPersistenceException {
        // TODO Auto-generated method stub

    }

    public PrincipalSet getAllChildPrincipals(boolean arg0)
            throws AuthorizationException {
        // TODO Auto-generated method stub
        return null;
    }

    public PrincipalSet getAllParentPrincipals(boolean arg0)
            throws AuthorizationException {
        // TODO Auto-generated method stub
        return null;
    }

    public PrincipalSet getChildPrincipals(boolean arg0)
            throws AuthorizationException {
        // TODO Auto-generated method stub
        return null;
    }

    public PrincipalSet getParentPrincipals(boolean arg0)
            throws AuthorizationException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isSystemPrincipal() {
        // TODO Auto-generated method stub
        return false;
    }

}
