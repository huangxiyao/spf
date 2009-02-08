package com.epicentric.user;

import java.util.Locale;
import java.util.Map;

import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;

public class UserManager {
    private static UserManager userManager = null;
    private static User user = null;
    private static User guestUser = null;
    private static String type = "default";

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public User getUser(String propertyID, Object propertyValue) throws EntityPersistenceException,
                                                                EntityNotFoundException {
        boolean returnUserTag = true;
        String[] str = propertyValue.toString().split("[-]");
        int len = str.length;
        if (type.equalsIgnoreCase("both") && len > 1) {
            returnUserTag = true;
        } else if (type.equalsIgnoreCase("lang") && len == 1) {
            returnUserTag = true;
        } else if (type.equalsIgnoreCase("en") && len == 1 && str[0].endsWith("_en")) {
            returnUserTag = true;
        } else if (type.equalsIgnoreCase("default")) {
            returnUserTag = true;
        } else {
            returnUserTag = false;
        }
        return returnUserTag ? user : null;
    }

    public static void createUser(User mUser, User gUser) {
        user = mUser;
        guestUser = gUser;
    }

    public User createUser(Map initialPropertySettings) throws UniquePropertyValueConflictException,
                                                       EntityPersistenceException {
        return user;

    }

    public User getGuestUser() throws EntityPersistenceException {
        return user;
    }

    public static void setReturnUserType(String returnType) {
        type = returnType;
    }

    public static void cleanup() {
        userManager = null;
        user = null;
        guestUser = null;
    }
}
