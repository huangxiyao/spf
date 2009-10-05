package com.hp.it.spf.user.profile.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.exception.UserProfileException;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

public class CompoundUserProfileRetriever implements IUserProfileRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(CompoundUserProfileRetriever.class);
    
    private List<IUserProfileRetriever> retrievers = new ArrayList<IUserProfileRetriever>();

    /**
     * Parse all delegate classes from delegateClassNames parameter, then instantiated them
     * in sequence.
     * 
     * @param delegateClassNames delegated class names seperated by comma
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public CompoundUserProfileRetriever(String delegateClassNames) throws ClassNotFoundException,
                                                                InstantiationException,
                                                                IllegalAccessException {
        StringTokenizer st = new StringTokenizer(delegateClassNames, ",");
        while (st.hasMoreTokens()) {
            String delegatedClassName = st.nextToken().trim();
            Class clazz = Class.forName(delegatedClassName);
            IUserProfileRetriever retriever = (IUserProfileRetriever)clazz.newInstance();
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Retrieved delegated class: " + delegatedClassName);
            }
            retrievers.add(retriever);
        }
    }

    /**
     * Retrieve user profile by specified UserProfileRetrievers in list in sequence.
     * 
     * @param userIdentifier user identifier
     * @param request portal request
     * @return user profile map or an empty map
     * @throws UserProfileException if retrieving user profile error occurs
     */
    public Map<String, Object> getUserProfile(String userIdentifier,
                                              HttpServletRequest request) throws UserProfileException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (IUserProfileRetriever retriever : retrievers) {
            map.putAll(retriever.getUserProfile(userIdentifier, request));
        }
        return map;
    }
}
