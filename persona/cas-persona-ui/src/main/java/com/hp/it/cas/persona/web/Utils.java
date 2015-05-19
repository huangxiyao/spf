package com.hp.it.cas.persona.web;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.portlet.PortletRequest;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;

/**
 * A container class for miscellaneous utility methods for Persona UI
 * components.
 * 
 * @author <link href="ye.liu@hp.com">Liu Ye</link>
 * @version TBD
 */
public class Utils {
	/**
	 * The group name of Persona admin.
	 */
	private final static String PERSONA_ADMIN_GROUP = "ADMIN-PERSONA";
	
    /**
     * <p>
     * Returns a set ordered by the attribute name. If there is a <code>null</code> object in 
     * the set, <code>null</code> will be the first element.
     * </p>
     * 
     * @param set a user attribute set got from Persona API.
     * @return the set ordered by the name.
     * @throws NullPointerException if parameter set is not specified.
     */
    @SuppressWarnings("unchecked")
    public static Set<IUserAttribute> getAttributeSetOrderByName(Set<IUserAttribute> set) {
        Set<IUserAttribute> sortedSet = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                // null is at the first
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                IUserAttribute u1 = (IUserAttribute)o1;
                IUserAttribute u2 = (IUserAttribute)o2;
                // order by attribute name
                return u1.getUserAttributeName()
                         .compareTo(u2.getUserAttributeName());
            }
        });
        sortedSet.addAll(set);
        return sortedSet;
    }
    
    /**
     * <p>
     * Verify the current user is a Persona admin or not.
     * </p>
     * 
	 * @param request
	 *            The portlet request.
	 * @return True if current user is a Persona admin.
     */
    public static boolean isPersonaAdmin(PortletRequest request) {
        return request.isUserInRole(PERSONA_ADMIN_GROUP);
    }
}
