package com.epicentric.user;

import com.epicentric.entity.Entity;

public class UserProvider {

    public static User getUser(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User Entity is invalid.");
        }
        return new User(entity);
    }

}
