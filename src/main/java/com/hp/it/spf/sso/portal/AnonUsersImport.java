package com.hp.it.spf.sso.portal;

import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserGroupManager;
import com.epicentric.user.UserGroupQueryResults;
import com.vignette.portal.log.LogWrapper;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Tool to load anonymous users. This program will do the following:
 * <ul>
 * <li>create non existing users</li>
 * <li>update existing users</li>
 * <li>create LOCAL_PROFILE_LANG_{langugage} and LOCAL_PROFILE_COUNTRY_{country} groups if they
 * don't exist already</li>
 * <li>add users to LOCAL_PROFILE_LANG_{langugage} and LOCAL_PROFILE_COUNTRY_{country} groups</li>
 * </ul>
 * <p>
 * In order to use this class its JAR file must be copied to Portal WAR directory and then the class
 * must be executed as follows:
 * <pre>
 * run_with_classpath com.hp.it.spf.sso.portal.AnonUsersImport {realm} {data file path}
 * </pre>
 * run_with_classpath(.bat or .sh) is Vignette tool present in VAP bin directory which allows to run a class
 * with the classpath containing all the libraries present in the portal WAR.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AnonUsersImport {

	private static final LogWrapper LOG = new LogWrapper(
			AnonUsersImport.class, AnonUsersImport.class.getName());

	/**
	 * Vignette user properties which cannot be modified once the user has been created.
	 */
	private static final Set<String> READ_ONLY_VAP_PROPERTIES =
			new TreeSet<String>(Arrays.asList(
					AuthenticationConsts.PROPERTY_DOMAIN_ID,
					AuthenticationConsts.PROPERTY_USER_NAME_ID
			));


	/**
	 * Performs the import process.
	 * @param args 2-element array with the first element being the portal realm to which
	 * the users should be attached and the 2nd element is a path to the data file.
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			printUsageAndExit();
		}

		final String realm = args[0];
		final String inputFilePath = args[1];
		try {
			new AnonUsersImport().run(realm, inputFilePath);
		}
		catch (FileNotFoundException ex) {
			LOG.error("Input file not found: " + inputFilePath);
		}
	}


	private static void printUsageAndExit() {
		System.out.printf("%nUsage runs_with_classpath %s <realm> <input_file_name>%n%n", AnonUsersImport.class.getName());
		System.exit(0);
	}


	private void run(String realm, String inputFilePath) throws FileNotFoundException {
		final InputFile inputFile = new InputFile(inputFilePath);

		LOG.info("Starting anonymous users import process from file " + inputFilePath);
		int counter = 0;
		while (inputFile.hasNext()) {
			try {
				AnonymousUserData userData = inputFile.next();
				User user = saveUser(userData, realm);
				saveUserGroups(user, userData.getLanguage(), userData.getCountry());
				counter++;
			}
			catch (IllegalArgumentException e) {
				LOG.error("Error persisting user: " + e.getMessage());
			}
			catch (UniquePropertyValueConflictException e) {
				LOG.error("Error writing Vignette user", e);
			}
			catch (EntityPersistenceException e) {
				LOG.error("Error reading/writing Vignette user", e);
			}
		}
		LOG.info("Anonymous users import process complete! Successfully created/updated users: " + counter);
	}


	private User saveUser(AnonymousUserData userData, String realm) throws EntityPersistenceException, UniquePropertyValueConflictException {
		Map<String, Object> userProperties = userData.toVignetteUserProperties();
		setRealm(userProperties, realm);
		User user;
		try {
			user = UserManager.getInstance().getUser(
					AuthenticationConsts.PROPERTY_USER_NAME_ID,
					userProperties.get(AuthenticationConsts.PROPERTY_USER_NAME_ID));

			LOG.info("Updating user: " + userProperties);
			for (Map.Entry<String, Object> userProperty : userProperties.entrySet()) {
				if (!READ_ONLY_VAP_PROPERTIES.contains(userProperty.getKey())) {
					user.setProperty(userProperty.getKey(), userProperty.getValue());
				}
			}

			user.save();
			LOG.info("User updated: " + userData.getUsername());
		}
		catch (EntityNotFoundException ex) {
			LOG.info("Creating user: " + userProperties);
			user = UserManager.getInstance().createUser(userProperties);
			LOG.info("User created: " + userData.getUsername());
		}
		return user;
	}


	private void setRealm(Map<String, Object> userProperties, String realm)
	{
		userProperties.put(AuthenticationConsts.PROPERTY_DOMAIN_ID, realm);
	}


	@SuppressWarnings("unchecked")
	private void saveUserGroups(User user, String language, String country) throws EntityPersistenceException, UniquePropertyValueConflictException
	{
		Set<String> userGroupNames =
				AuthenticatorHelper.getUserGroupTitleSet(AuthenticatorHelper.getUserGroupSet(user));
		LOG.info("User is currently part of the following groups: " + userGroupNames);

		setLanguageGroup(user, userGroupNames, language);
		setCountryGroup(user, userGroupNames, country);
		user.save();
	}


	private void setCountryGroup(User user, Set<String> userGroupNames, String country) throws EntityPersistenceException, UniquePropertyValueConflictException
	{
		if (country == null) {
			return;
		}

		final String countryGroupName = "LOCAL_PORTAL_COUNTRY_" + country.toUpperCase();
		if (!userGroupNames.contains(countryGroupName)) {
			user.addParent(getGroup(countryGroupName));
			LOG.info("User added to country group: " + countryGroupName);
		}
	}


	private void setLanguageGroup(User user, Set<String> userGroupNames, String language) throws EntityPersistenceException, UniquePropertyValueConflictException
	{
		if (language == null) {
			return;
		}

		final String languageGroupName = "LOCAL_PORTAL_LANG_" + language.toUpperCase();
		if (!userGroupNames.contains(languageGroupName)) {
			user.addParent(getGroup(languageGroupName));
			LOG.info("User added to language group: " + languageGroupName);
		}
	}


	private UserGroup getGroup(String groupName) throws EntityPersistenceException, UniquePropertyValueConflictException
	{
		UserGroupQueryResults userGroups =
				UserGroupManager.getInstance().getUserGroups(
						AuthenticationConsts.GROUP_TITLE, groupName);
		if (userGroups.hasNext()) {
			UserGroup userGroup = (UserGroup) userGroups.next();
			if (userGroups.hasNext()) {
				LOG.warning("More than one group with the following name exists: " + groupName +
						". Will use the first one returned: uid=" + userGroup.getProperty("uid"));
			}
			return userGroup;
		}
		else {
			Map<String, String> groupProperties = new HashMap<String, String>();
			groupProperties.put(AuthenticationConsts.GROUP_TITLE, groupName);
			UserGroup userGroup = UserGroupManager.getInstance().createUserGroup(groupProperties);
			LOG.info("User group created: " + groupName);
			return userGroup;
		}
	}


}
