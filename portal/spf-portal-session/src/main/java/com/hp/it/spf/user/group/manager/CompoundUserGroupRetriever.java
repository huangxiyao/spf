package com.hp.it.spf.user.group.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.exception.UserGroupsException;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

public class CompoundUserGroupRetriever implements IUserGroupRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(CompoundUserGroupRetriever.class);
    
    private List<IUserGroupRetriever> retrievers = new ArrayList<IUserGroupRetriever>();

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
    public CompoundUserGroupRetriever(String delegateClassNames) throws ClassNotFoundException,
                                                                InstantiationException,
                                                                IllegalAccessException {
        StringTokenizer st = new StringTokenizer(delegateClassNames, ",");
        while (st.hasMoreTokens()) {
            String delegatedClassName = st.nextToken().trim();
            Class clazz = Class.forName(delegatedClassName);
            IUserGroupRetriever retriever = (IUserGroupRetriever)clazz.newInstance();
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Retrieved delegated class: " + delegatedClassName);
            }
            retrievers.add(retriever);
        }
    }

    /**
     * Retrieve user groups by specified UserGroupRetrievers in list in sequence.
     * 
     * @param userProfile user profile map
     * @param request HttpServletRequest 
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     * @throws UserGroupsException if any exception occurs, an
     *             UserGroupsException will be thrown.
     */
    public Set<String> getGroups(Map<String, Object> userProfile,
                                 HttpServletRequest request) throws UserGroupsException {
        Set<String> set = new HashSet<String>();
        for (IUserGroupRetriever retriever : retrievers) {
            set.addAll(retriever.getGroups(userProfile, request));
        }
        return set;
    }
}
