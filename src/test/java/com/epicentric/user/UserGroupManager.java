package com.epicentric.user;

import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;

public class UserGroupManager {
    public static UserGroupManager getInstance() {
        return new UserGroupManager();
    }

    public EntityType getUserGroupEntityType() {
        return null;
    }

    public UserGroupQueryResults getUserGroups(String propertyID,
                                               Object propertyValue) throws EntityPersistenceException {
        return null;
    }
}
