package com.hp.spp.webservice.ugs.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserGroupManager;
import com.epicentric.user.UserManager;
import com.hp.spp.profile.Constants;
import com.hp.spp.profile.ProfileHelper;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;
import com.hp.spp.common.util.DiagnosticContext;
import com.hp.spp.config.ConfigException;
import com.hp.runtime.ugs.asl.service.InvalidGroupRequestException;
import com.hp.runtime.ugs.asl.service.UGSSystemException;
import com.hp.runtime.ugs.asl.service.NoRulesOrGroupsForSiteException;

import javax.xml.rpc.ServiceException;

/**
 * Class which takes care of the management of the user groups
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class SPPUserGroupManager {

	private static final Logger mLog = Logger.getLogger(SPPUserGroupManager.class);

	/**
	 * compute the groups of the user and put them in the profile.
	 * @param userProfile
	 * @return the user profile with its groups
	 */
	public Set computeGroups(Map userProfile) {

		try {
			TimeRecorder.getThreadInstance().recordStart(Operation.COMPUTE_GROUPS_INC_UGS);
			String site = (String) userProfile.get(Constants.MAP_SITE);

			// we retrieve the groups from UGS (need to insure that the map contains only String as
			// value
			Map userProfileUGS = (new ProfileHelper()).toLegacyProfile(userProfile);
			Set ugs_UserGroup = getVignetteGroups(site, (HashMap) userProfileUGS);

			// we recompute the user groups by comparison with the set in portal
			Set computed_userGroups = computeUserGroup(userProfile, ugs_UserGroup);

			// we put the groups in the profile
			StringBuffer userGroupList = new StringBuffer();
			for (Iterator iter = computed_userGroups.iterator(); iter.hasNext();) {
				UserGroup element = (UserGroup) iter.next();

				if (userGroupList.length() > 0) {
					userGroupList.append(';');
				}
				userGroupList.append(element.getProperty("title").toString());
			}
			if (userGroupList.length() > 0) {
				userProfile.put(Constants.MAP_USERGROUPS, userGroupList.toString());
			}

			TimeRecorder.getThreadInstance().recordEnd(Operation.COMPUTE_GROUPS_INC_UGS);
			return ugs_UserGroup;
		}
		catch (RuntimeException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.COMPUTE_GROUPS_INC_UGS, e);
			throw e;
		}
	}

	/**
	 * Compute the groups of the user
	 * <p>
	 * 
	 * @param userProfile the user profile
	 * @param ugs_UserGroup the set of groups from ugs
	 * @return the set of groups of the user
	 */
	public Set computeUserGroup(Map userProfile,
								Set ugs_UserGroup) {
		String hppId = (String) userProfile.get(Constants.MAP_HPPID);

		Set final_portal_userGroups = null;
		try {
			// retrieve user
			User currentUser = UserManager.getInstance().getUser(Constants.VGN_HPPID,
					hppId);
			if (mLog.isDebugEnabled())
				mLog.debug("displayName of currentUser :" + currentUser.getDisplayName());

			// groups in portal for the user
			EntityType userGroupType = UserGroupManager.getInstance().getUserGroupEntityType();
			Set portal_userGroups = currentUser.getParents(userGroupType, false);

			// Equality of 2 sets
			if (portal_userGroups.equals(ugs_UserGroup)) {
				if (mLog.isDebugEnabled())
					mLog.debug("groups from portal and ugs are equals");
				return portal_userGroups;
			}

			// remove and add groups in the portal + get again the groups
			removeGroups(ugs_UserGroup, portal_userGroups, currentUser);
			addGroups(ugs_UserGroup, portal_userGroups, currentUser);
			final_portal_userGroups = currentUser.getParents(userGroupType, false);
		} catch (EntityNotFoundException e) {
			String error = "EntityNotFoundException during retrieval of user " + hppId
					+ " . " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error);
		} catch (EntityPersistenceException e) {
			String error = "EntityPersistenceException during remove or add groups of user "
					+ hppId + " . " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error);
		}

		return final_portal_userGroups;
	}

	/*
	 * remove the groups in portal that are not in UGS. Some groups are not removed because
	 * they are internal to Vignette.
	 */
	private void removeGroups(Set ugs_UserGroup, Set portal_userGroups, User currentUser)
			throws EntityPersistenceException {

		// Compute and remove groups
		Set userGroupToRemove = new HashSet(portal_userGroups);
		userGroupToRemove.removeAll(ugs_UserGroup);
		if (!userGroupToRemove.isEmpty()) {
			Iterator it = userGroupToRemove.iterator();
			// process list of group to remove user from each
			while (it.hasNext()) {
				UserGroup group = (UserGroup) it.next();
				String groupName = (String) group.getProperty("title");

				// Do not remove LOCAL groups
				if (!groupName.startsWith("LOCAL_"))
					group.removeChild(currentUser);
			}
		}
	}

	/*
	 * add the new groups present in UGS.
	 */
	private void addGroups(Set ugs_UserGroup, Set portal_userGroups, User currentUser)
			throws EntityPersistenceException {

		// Compute and add groups
		Set userGroupToAdd = new HashSet(ugs_UserGroup);
		userGroupToAdd.removeAll(portal_userGroups);
		if (!userGroupToAdd.isEmpty()) {
			Iterator it = userGroupToAdd.iterator();
			while (it.hasNext()) {
				UserGroup group = (UserGroup) it.next();
				group.addChild(currentUser);
			}
		}
	}

	/**
	 * Retrievs the group names from the {@link UserGroupService} implementation provided by
	 * {@link UserGroupServiceFactory} for the given <tt>siteName</tt> and the user described by
	 * the provided <tt>userProfile</tt>
	 *
	 * @param siteName name of the site for which groups are retrieved
	 * @param userProfile profile of the user for whom the groups are retrieved
	 * @return an array of group names or <tt>null</tt> in an error or a non RuntimeException occurs.
	 */
	public String[] getGroupsFromUserGroupService(String siteName, HashMap userProfile) {
		// Test conditions
		if (siteName == null) {
			mLog.error("site name passed to SPPUserGroupWSManager is null");
			return null;
		}
		if (userProfile == null) {
			mLog.error("user profile passed to SPPUserGroupWSManager is null");
			return null;
		}

		if (userProfile.get(Constants.MAP_SITE_ID) == null) {
			mLog .error("site id contained in the user profile passed to SPPUserGroupWSManager is null");
			return null;
		}

		String[] ugsGroups = null;

		try {
			UserGroupServiceFactory factory = new UserGroupServiceFactory();
			UserGroupService client = factory.newUserGroupService(siteName);

			TimeRecorder.getThreadInstance().recordStart(Operation.UGS_CALL);
			ugsGroups = client.getUserGroups(siteName, userProfile);
			TimeRecorder.getThreadInstance().recordEnd(Operation.UGS_CALL);

		} catch (UGSSystemException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS SystemException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (InvalidGroupRequestException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS InvalidGroupRequestException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (NoRulesOrGroupsForSiteException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS NoRulesOrGroupsForSiteException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (RemoteException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS RemoteException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (ServiceException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS ServiceException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (MalformedURLException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS MalformedURLException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (ConfigException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			DiagnosticContext.getThreadInstance().add("UGS ConfigException", e.getMessage());
			mLog.error(e, e);
			return null;
		} catch (RuntimeException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UGS_CALL, e);
			throw e;
		}

		return ugsGroups;
	}

	/**
	 * Returns a set of user groups present in Vignette Portal for the given <tt>siteName</tt>
	 * and the user described by <tt>userProfile</tt>. This method first retrieves the group names from
	 * {@link UserGroupService} and then returns only the Vignette groups having these names. No error
	 * is generated when the group with given name cannot be found in Vignette.
	 *
	 * @param siteName name of the site for which the groups are retrieved. Note that this site is
	 * only used to call UserGroupService but not to retrieve groups from Vignette Portal. In other words
	 * any relationship that may exist in Vignette between a site and groups is ignored.
	 * @param userProfile profile of the user for which the groups are retrieved.
	 * @return set of user groups the given user is a member of.
	 */
	public Set<UserGroup> getVignetteGroups(String siteName, HashMap userProfile) {
		Set<UserGroup> vignetteGroups = null;

		String[] groups = getGroupsFromUserGroupService(siteName, userProfile);
		if (groups != null) {
			vignetteGroups = retrieveVignetteGroups(groups);
		}
		else {
			throw new IllegalStateException("list of groups retrieved from ugs is null");
		}
		return vignetteGroups;
	}

	/**
	 * Retrieves from Vignette Portal the set of UserGroup object whose names are present in the given
	 * <tt>groups</tt> array. No error occurs if the group with the given name cannot be found -
	 * it is simply not present on the result list.
	 *
	 * @param groups list of group names to look for in Vignette Portal
	 * @return a set of groups present in Vignette Portal for the given set of group names
	 */
	private Set<UserGroup> retrieveVignetteGroups(String[] groups) {
		Set<UserGroup> vignetteGroups = null;

		com.epicentric.user.UserGroupManager userGroupManager =
				com.epicentric.user.UserGroupManager .getInstance();

		// init ugs_Group
		vignetteGroups = new HashSet<UserGroup>();
		for (int i = 0; i < groups.length; i++) {
			UserGroup p = null;
			try {
				p = (UserGroup) userGroupManager.getUserGroups("title", groups[i]).next();
			} catch (EntityPersistenceException e) {
				mLog.warn(e, e);
				p = null;
			} catch (NullPointerException e) {
				mLog.warn("Group [" + groups[i] +
						"] is registered in UGS but not in Vignette. It is not added to the groups of the user.");
				p = null;
			}
			if (p != null) {
				vignetteGroups.add(p);
			}
		}

		return vignetteGroups;
	}

}
