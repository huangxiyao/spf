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

/**
 * {@link com.hp.it.spf.user.profile.manager.IUserProfileRetriever} implementation which is able
 * to merge user profile maps retrieved by a set of delegates. The retrievers are invoked in order
 * in which they are specified in the <code>deleteClassNames</code> constructor parameter.
 */
public class CompoundUserProfileRetriever implements IUserProfileRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(CompoundUserProfileRetriever.class);
    
    private List<IUserProfileRetriever> retrievers = new ArrayList<IUserProfileRetriever>();

    /**
     * Parse all delegate classes from delegateClassNames parameter, then instantiated them
     * in sequence.
     * 
     * @param delegateClassNames fully qualified delegate class names seperated by comma
     * @throws ClassNotFoundException If a class specified in <code>delegateClassNames</code> parameter
	 * could not be found
     * @throws InstantiationException If a class specified in <code>delegateClassNames</code> parameter
	 * could not be instantiated
     * @throws IllegalAccessException If a class specified in <code>delegateClassNames</code> parameter
	 * is not public
     */
    @SuppressWarnings("unchecked")
    public CompoundUserProfileRetriever(String delegateClassNames)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
        StringTokenizer st = new StringTokenizer(delegateClassNames, ",");
        while (st.hasMoreTokens()) {
            String delegatedClassName = st.nextToken().trim();
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Instantiating profile retriever delegate class: " + delegatedClassName);
			}
            Class clazz = Class.forName(delegatedClassName);
            IUserProfileRetriever retriever = (IUserProfileRetriever)clazz.newInstance();
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
