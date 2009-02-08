package com.epicentric.common.website;

import javax.servlet.http.HttpSession;

import com.epicentric.user.User;

public class SessionUtils {
    private static User mUser;

    public static User getCurrentUser(HttpSession session) {
        return mUser;
    }

    public static void setCurrentUser(HttpSession session, User user) {
        mUser = user;
    }
    
    public static void cleanup() {
        mUser = null;
    }
}
