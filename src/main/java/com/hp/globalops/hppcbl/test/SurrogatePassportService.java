package com.hp.globalops.hppcbl.test;

import java.util.HashMap;
import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.tasks.AddUserToGroupTask;
import com.hp.globalops.hppcbl.passport.tasks.AdminCreateUserTask;
import com.hp.globalops.hppcbl.passport.tasks.AdminModifyUserTask;
import com.hp.globalops.hppcbl.passport.tasks.ChangeUserIdTask;
import com.hp.globalops.hppcbl.passport.tasks.CheckAuthorityTask;
import com.hp.globalops.hppcbl.passport.tasks.CreateUserTask;
import com.hp.globalops.hppcbl.passport.tasks.GetGroupInfoTask;
import com.hp.globalops.hppcbl.passport.tasks.GetMembersOfGroupTask;
import com.hp.globalops.hppcbl.passport.tasks.GetProfileIdTask;
import com.hp.globalops.hppcbl.passport.tasks.GetRMCookieDataTask;
import com.hp.globalops.hppcbl.passport.tasks.GetRMDataTask;
import com.hp.globalops.hppcbl.passport.tasks.GetSecurityQuestionTask;
import com.hp.globalops.hppcbl.passport.tasks.GetUserCoreTask;
import com.hp.globalops.hppcbl.passport.tasks.GetUserGroupsTask;
import com.hp.globalops.hppcbl.passport.tasks.GetUserIdListTask;
import com.hp.globalops.hppcbl.passport.tasks.GetUserIdTask;
import com.hp.globalops.hppcbl.passport.tasks.GetUserTask;
import com.hp.globalops.hppcbl.passport.tasks.IsMemberOfGroupTask;
import com.hp.globalops.hppcbl.passport.tasks.MigrateUserTask;
import com.hp.globalops.hppcbl.passport.tasks.RemoveUserFromGroupTask;
import com.hp.globalops.hppcbl.passport.tasks.SendEmailTask;
import com.hp.globalops.hppcbl.passport.tasks.TaskExecutionException;
import com.hp.globalops.hppcbl.passport.tasks.ValidateSessionTokenTask;
import com.hp.globalops.hppcbl.webservice.AddUserToGroupResponseElement;
import com.hp.globalops.hppcbl.webservice.AdminCreateUserResponseElement;
import com.hp.globalops.hppcbl.webservice.AdminModifyUserResponseElement;
import com.hp.globalops.hppcbl.webservice.AdminResetPasswordResponseElement;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResponseElement;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice;
import com.hp.globalops.hppcbl.webservice.ChangePasswordResponseElement;
import com.hp.globalops.hppcbl.webservice.ChangeUserIdResponseElement;
import com.hp.globalops.hppcbl.webservice.CheckAuthorityResponseElement;
import com.hp.globalops.hppcbl.webservice.CheckUserExistsResponseElement;
import com.hp.globalops.hppcbl.webservice.CreateUserResponseElement;
import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.GetGUIDExpirationResponseElement;
import com.hp.globalops.hppcbl.webservice.GetGroupInfoResponseElement;
import com.hp.globalops.hppcbl.webservice.GetMembersOfGroupResponseElement;
import com.hp.globalops.hppcbl.webservice.GetProfileIdResponseElement;
import com.hp.globalops.hppcbl.webservice.GetRMCookieDataResponseElement;
import com.hp.globalops.hppcbl.webservice.GetRMDataResponseElement;
import com.hp.globalops.hppcbl.webservice.GetSecurityQuestionResponseElement;
import com.hp.globalops.hppcbl.webservice.GetUserCoreResponseElement;
import com.hp.globalops.hppcbl.webservice.GetUserGroupsResponseElement;
import com.hp.globalops.hppcbl.webservice.GetUserIdListResponseElement;
import com.hp.globalops.hppcbl.webservice.GetUserIdResponseElement;
import com.hp.globalops.hppcbl.webservice.GetUserResponseElement;
import com.hp.globalops.hppcbl.webservice.IsMemberOfGroupResponseElement;
import com.hp.globalops.hppcbl.webservice.LoginResponseElement;
import com.hp.globalops.hppcbl.webservice.MigrateUserResponseElement;
import com.hp.globalops.hppcbl.webservice.ModifyUserResponseElement;
import com.hp.globalops.hppcbl.webservice.PrivateData;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileCredentials;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;
import com.hp.globalops.hppcbl.webservice.ProfilePrivate;
import com.hp.globalops.hppcbl.webservice.RecoverUserIdResponseElement;
import com.hp.globalops.hppcbl.webservice.RemoveUserFromGroupResponseElement;
import com.hp.globalops.hppcbl.webservice.ResetPasswordResponseElement;
import com.hp.globalops.hppcbl.webservice.SendEmailResponseElement;
import com.hp.globalops.hppcbl.webservice.SystemData;
import com.hp.globalops.hppcbl.webservice.UpdateCredentialsResponseElement;
import com.hp.globalops.hppcbl.webservice.ValidatePinResponseElement;
import com.hp.globalops.hppcbl.webservice.ValidateSessionTokenResponseElement;

/**
 * Created by IntelliJ IDEA. User: millerand Date: Aug 23, 2004 Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurrogatePassportService extends PassportService {

	private String mSystemLangCode = "";

	public CheckUserExistsResponseElement checkUserExists(String userId, String email) throws PassportServiceException {
		CheckUserExistsResponseElement response = new CheckUserExistsResponseElement();
		response.setMatchedProfileId("default matched profile id");
		response.setProfileIdByUserId("default profile id by user id");
		return response;
	}

	public CreateUserResponseElement createUser(String userId, ProfileCore profileCore, ProfileExtended profileExtended, ProfileCredentials profileCredentials) throws PassportServiceException {
		CreateUserTask createUserTask = new CreateUserTask();
		CreateUserResponseElement response = null;

		try {
			HashMap props = new HashMap();
			props.put("userId", userId);
			props.put("ProfileCore", profileCore);
			props.put("ProfileCredentials", profileCredentials);
			props.put("ProfileExtended", profileExtended);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				createUserTask.setSystemLangCode(mSystemLangCode);
			createUserTask.init(props);
			response = (CreateUserResponseElement) createUserTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(createUserTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(createUserTask.getFaults());
		}
		return response;
	}

	public GetProfileIdResponseElement getProfileId(String userId) throws PassportServiceException {
		GetProfileIdTask getProfileIdTask = new GetProfileIdTask();
		GetProfileIdResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("userId", userId);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getProfileIdTask.setSystemLangCode(mSystemLangCode);
			getProfileIdTask.init(props);
			response = (GetProfileIdResponseElement) getProfileIdTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getProfileIdTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getProfileIdTask.getFaults());
		}
		return response;
	}

	public GetRMCookieDataResponseElement getRMCookieData(String profileId) throws PassportServiceException {
		GetRMCookieDataTask getRMCookieDataTask = new GetRMCookieDataTask();
		GetRMCookieDataResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("profileId", profileId);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getRMCookieDataTask.setSystemLangCode(mSystemLangCode);
			getRMCookieDataTask.init(props);
			response = (GetRMCookieDataResponseElement) getRMCookieDataTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getRMCookieDataTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getRMCookieDataTask.getFaults());
		}
		return response;
	}

	public GetRMDataResponseElement getRMData(String rMdataField) throws PassportServiceException {
		GetRMDataTask getRMDataTask = new GetRMDataTask();
		GetRMDataResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("rMdataField", rMdataField);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getRMDataTask.setSystemLangCode(mSystemLangCode);
			getRMDataTask.init(props);
			response = (GetRMDataResponseElement) getRMDataTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getRMDataTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getRMDataTask.getFaults());
		}
		return response;
	}

	public GetUserResponseElement getUser(String sessionToken) throws PassportServiceException {
		GetUserTask getUserTask = new GetUserTask();
		GetUserResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("sessionToken", sessionToken);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getUserTask.setSystemLangCode(mSystemLangCode);
			getUserTask.init(props);
			response = (GetUserResponseElement) getUserTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getUserTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getUserTask.getFaults());
		}
		return response;
	}

	public GetUserCoreResponseElement getUserCore(String sessionToken) throws PassportServiceException {
		GetUserCoreTask getUserCoreTask = new GetUserCoreTask();
		GetUserCoreResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("sessionToken", sessionToken);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getUserCoreTask.setSystemLangCode(mSystemLangCode);
			getUserCoreTask.init(props);
			response = (GetUserCoreResponseElement) getUserCoreTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getUserCoreTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getUserCoreTask.getFaults());
		}
		return response;
	}

	public GetUserIdResponseElement getUserId(ProfileIdentity profileIdentity) throws PassportServiceException {
		GetUserIdTask getUserIdTask = new GetUserIdTask();
		GetUserIdResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("ProfileIdentity", profileIdentity);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getUserIdTask.setSystemLangCode(mSystemLangCode);
			getUserIdTask.init(props);
			response = (GetUserIdResponseElement) getUserIdTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getUserIdTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getUserIdTask.getFaults());
		}
		return response;
	}

	public GetUserIdListResponseElement getUserIdList(String[] profileId, String[] applicationRefId) throws PassportServiceException {
		GetUserIdListTask getUserIdListTask = new GetUserIdListTask();
		GetUserIdListResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("ProfileId", profileId);
			props.put("ApplicationRefId", applicationRefId);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getUserIdListTask.setSystemLangCode(mSystemLangCode);
			getUserIdListTask.init(props);
			response = (GetUserIdListResponseElement) getUserIdListTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getUserIdListTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getUserIdListTask.getFaults());
		}
		return response;
	}

	public MigrateUserResponseElement migrateUser(String sessionToken, String applicationRefId, ProfileCredentials profileCredentials, ProfileCore profileCore, ProfileExtended profileExtended) throws PassportServiceException {
		MigrateUserTask migrateUserTask = new MigrateUserTask();
		MigrateUserResponseElement response = null;

		try {
			HashMap props = new HashMap();
			props.put("SessionToken", sessionToken);
			props.put("ApplicationRefId", applicationRefId);
			props.put("ProfileCredentials", profileCredentials);
			props.put("ProfileCore", profileCore);
			props.put("ProfileExtended", profileExtended);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				migrateUserTask.setSystemLangCode(mSystemLangCode);
			migrateUserTask.init(props);
			response = (MigrateUserResponseElement) migrateUserTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(migrateUserTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(migrateUserTask.getFaults());
		}
		return response;
	}

	public ModifyUserResponseElement modifyUser(String sessionToken, ProfileCore profileCore, ProfileExtended profileExtended) throws PassportServiceException {
		ModifyUserResponseElement response = null;
		return response;
	}

	public ChangePasswordResponseElement changePassword(String userId, String currentPassword, String newPassword, String newPasswordConfirm) throws PassportServiceException {
		ChangePasswordResponseElement response = new ChangePasswordResponseElement();
		return response;
	}

	public ChangeUserIdResponseElement changeUserId(String sessionToken, String newUserId, String currentPassword) throws PassportServiceException {
		ChangeUserIdTask changeUserIdTask = new ChangeUserIdTask();
		ChangeUserIdResponseElement response = null;

		try {
			HashMap props = new HashMap();
			props.put("sessionToken", sessionToken);
			props.put("newUserId", newUserId);
			props.put("currentPassword", currentPassword);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				changeUserIdTask.setSystemLangCode(mSystemLangCode);
			changeUserIdTask.init(props);
			response = (ChangeUserIdResponseElement) changeUserIdTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(changeUserIdTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(changeUserIdTask.getFaults());
		}
		return response;
	}

	public GetGUIDExpirationResponseElement getGUIDExpiration(String guid) throws PassportServiceException {
		GetGUIDExpirationResponseElement response = new GetGUIDExpirationResponseElement();
		response.setMinsRemaining("15");
		response.setProfileId("default profile id");
		return response;
	}

	public GetSecurityQuestionResponseElement getSecurityQuestion(String guid) throws PassportServiceException {
		GetSecurityQuestionTask getSecurityQuestionTask = new GetSecurityQuestionTask();
		GetSecurityQuestionResponseElement response = null;

		try {
			HashMap props = new HashMap();
			props.put("guid", guid);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getSecurityQuestionTask.setSystemLangCode(mSystemLangCode);
			getSecurityQuestionTask.init(props);
			response = (GetSecurityQuestionResponseElement) getSecurityQuestionTask.invoke();

		} catch (TaskExecutionException e) {
			// displayFaults(getSecurityQuestionTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getSecurityQuestionTask.getFaults());
		}
		return response;
	}

	//public RecoverUserIdResponseElement recoverUserId(String emailAddress, String firstName, String lastName) throws PassportServiceException {
	public RecoverUserIdResponseElement recoverUserId(String emailAddress) throws PassportServiceException {	
		RecoverUserIdResponseElement response = new RecoverUserIdResponseElement();
		ProfileIdentity id = new ProfileIdentity();
		id.setProfileId("default profile id");
		id.setUserId("excellent dude");
		response.setProfileIdentity(id);
		return response;
	}

	public ResetPasswordResponseElement resetPassword(String userId, String email, EmailTemplate[] emailTemplate) throws PassportServiceException {
		ResetPasswordResponseElement response = new ResetPasswordResponseElement();
		response.setProfileId("default profile id");
		return response;
	}

	public UpdateCredentialsResponseElement updateCredentials(String guid, String newPassword, String newPasswordConfirm, String securityAnswer, String securityQuestion, String userId) throws PassportServiceException {
		UpdateCredentialsResponseElement response = new UpdateCredentialsResponseElement();
		return response;
	}

	public LoginResponseElement login(String userId, String password) throws PassportServiceException {
		LoginResponseElement response = new LoginResponseElement();
		response.setSessionToken("default session token");
		return response;
	}

	public AdminCreateUserResponseElement adminCreateUser(String adminSessionToken, String userId, String groupName, String roleName, EmailTemplate[] emailTemplate, ProfileCore profileCore, ProfileExtended profileExtended) throws PassportServiceException {
		AdminCreateUserTask adminCreateUserTask = new AdminCreateUserTask();
		AdminCreateUserResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("userId", userId);
			props.put("groupName", groupName);
			props.put("roleName", roleName);
			props.put("emailTemplate", emailTemplate);
			props.put("profileCore", profileCore);
			props.put("profileExtended", profileExtended);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				adminCreateUserTask.setSystemLangCode(mSystemLangCode);
			adminCreateUserTask.init(props);
			response = (AdminCreateUserResponseElement) adminCreateUserTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(adminCreateUserTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(adminCreateUserTask.getFaults());
		}
		return response;
	}

	public AdminModifyUserResponseElement adminModifyUser(String adminSessionToken, String profileId, EmailTemplate emailTemplate, ProfileCore profileCore, ProfileExtended profileExtended) throws PassportServiceException {
		AdminModifyUserTask adminModifyUserTask = new AdminModifyUserTask();
		AdminModifyUserResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("profileId", profileId);
			props.put("emailTemplate", emailTemplate);
			props.put("profileCore", profileCore);
			props.put("profileExtended", profileExtended);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				adminModifyUserTask.setSystemLangCode(mSystemLangCode);
			adminModifyUserTask.init(props);
			response = (AdminModifyUserResponseElement) adminModifyUserTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(adminModifyUserTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(adminModifyUserTask.getFaults());
		}
		return response;
	}

	public AdminResetPasswordResponseElement adminResetPassword(String adminSessionToken, String profileId, EmailTemplate[] emailTemplate) throws PassportServiceException {
		AdminResetPasswordResponseElement response = new AdminResetPasswordResponseElement();
		return response;
	}

	public AdminViewUserResponseElement adminViewUser(String adminSessionToken, String searchValue, String searchCriteria) throws PassportServiceException {
		AdminViewUserResponseElement response = new AdminViewUserResponseElement();
		response.setIsEditable(true);
		
		ProfileIdentity id = new ProfileIdentity();
		id.setProfileId("default profile id");
		id.setUserId("excellent dude");
		response.setProfileIdentity(id);
		
		AdminViewUserResultTypeChoice choice = new AdminViewUserResultTypeChoice();
		PrivateData data = new PrivateData();
		ProfilePrivate profilePrivate = new ProfilePrivate();		
		profilePrivate.setContactPrefEmail("U");					//hpp required
		profilePrivate.setContactPrefPost("2517 金科路");
		profilePrivate.setContactPrefTelephone("2777");
		profilePrivate.setEmail("excellent@hp.com");				//hpp required
		profilePrivate.setFirstName("Excellent");
		profilePrivate.setLastName("Excellent");					//hpp required
		profilePrivate.setLangCode("12");							//hpp required
		profilePrivate.setResidentCountryCode("CN");				//hpp required
		profilePrivate.setSecurityAnswer("42");						//hpp required
		profilePrivate.setSecurityLevel("1");
		profilePrivate.setSecurityQuestion("Whats the meaning of life?");	//hpp required
		profilePrivate.setSegmentName("006");
		profilePrivate.setTitle("Your Highness");
		data.setProfilePrivate(profilePrivate);
		
		ProfileExtended extended = new ProfileExtended();
		extended.setBusAddressLine1("2517金科路");
		extended.setBusBuildingName("A栋");
		extended.setBusCity("上海");
		extended.setBusCompanyName("Hewlett Packard");
		extended.setBusCountryCode("CN");
		extended.setBusDistrict("张江区");
		extended.setBusPostalCode("201203");
		data.setProfileExtended(extended);
		
		SystemData sysData = new SystemData();
		sysData.setBrandedFlag("Y");
		sysData.setProfileCreationDate("01/01/2007");
		sysData.setProfileLastUpdatedDate("01/01/2007");
		sysData.setLastSuccessfulLoginDate("01/01/2007");
		sysData.setLastAttemptedLoginDate("01/01/2007");
		data.setSystemData(sysData);
		
		choice.setPrivateData(data);
		response.setAdminViewUserResultTypeChoice(choice);
		return response;
	}

	public SendEmailResponseElement sendEmail(String userId, EmailTemplate[] emailTemplate) throws PassportServiceException {
		SendEmailTask sendEmailTask = new SendEmailTask();
		SendEmailResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("userId", userId);
			props.put("emailTemplate", emailTemplate);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				sendEmailTask.setSystemLangCode(mSystemLangCode);
			sendEmailTask.init(props);
			response = (SendEmailResponseElement) sendEmailTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(sendEmailTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(sendEmailTask.getFaults());
		}
		return response;
	}

	public ValidatePinResponseElement validatePin(String guid, String newPassword, String newPasswordConfirm, String securityAnswer, String securityQuestion, String pin) throws PassportServiceException {
		ValidatePinResponseElement response = new ValidatePinResponseElement();
		return response;
	}

	public AddUserToGroupResponseElement addUserToGroup(String adminSessionToken, ProfileIdentity profileIdentity, String groupName, String roleName, EmailTemplate[] emailTemplate) throws PassportServiceException {
		AddUserToGroupTask addUserToGroupTask = new AddUserToGroupTask();
		AddUserToGroupResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("profileIdentity", profileIdentity);
			props.put("groupName", groupName);
			props.put("roleName", roleName);
			props.put("emailTemplate", emailTemplate);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				addUserToGroupTask.setSystemLangCode(mSystemLangCode);
			addUserToGroupTask.init(props);
			response = (AddUserToGroupResponseElement) addUserToGroupTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(addUserToGroupTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(addUserToGroupTask.getFaults());
		}
		return response;
	}

	public GetGroupInfoResponseElement getGroupInfo(String adminSessionToken, String groupName, String roleName) throws PassportServiceException {
		GetGroupInfoTask getGroupInfoTask = new GetGroupInfoTask();
		GetGroupInfoResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("groupName", groupName);
			props.put("roleName", roleName);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getGroupInfoTask.setSystemLangCode(mSystemLangCode);
			getGroupInfoTask.init(props);
			response = (GetGroupInfoResponseElement) getGroupInfoTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(getGroupInfoTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getGroupInfoTask.getFaults());
		}
		return response;
	}

	public GetMembersOfGroupResponseElement getMembersOfGroup(String adminSessionToken, String groupName, String roleName) throws PassportServiceException {
		GetMembersOfGroupTask getMembersOfGroupTask = new GetMembersOfGroupTask();
		GetMembersOfGroupResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("groupName", groupName);
			props.put("roleName", roleName);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getMembersOfGroupTask.setSystemLangCode(mSystemLangCode);
			getMembersOfGroupTask.init(props);
			response = (GetMembersOfGroupResponseElement) getMembersOfGroupTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(getMembersOfGroupTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getMembersOfGroupTask.getFaults());
		}
		return response;
	}

	public GetUserGroupsResponseElement getUserGroups(String adminSessionToken, ProfileIdentity profileIdentity) throws PassportServiceException {
		GetUserGroupsTask getUserGroupsTask = new GetUserGroupsTask();
		GetUserGroupsResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("profileIdentity", profileIdentity);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				getUserGroupsTask.setSystemLangCode(mSystemLangCode);
			getUserGroupsTask.init(props);
			response = (GetUserGroupsResponseElement) getUserGroupsTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(getUserGroupsTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(getUserGroupsTask.getFaults());
		}
		return response;
	}

	public IsMemberOfGroupResponseElement isMemberOfGroup(String sessionToken, ProfileIdentity profileIdentity, String groupName, String roleName) throws PassportServiceException {
		IsMemberOfGroupTask isMemberOfGroupTask = new IsMemberOfGroupTask();
		IsMemberOfGroupResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("sessionToken", sessionToken);
			props.put("profileIdentity", profileIdentity);
			props.put("groupName", groupName);
			props.put("roleName", roleName);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				isMemberOfGroupTask.setSystemLangCode(mSystemLangCode);
			isMemberOfGroupTask.init(props);
			response = (IsMemberOfGroupResponseElement) isMemberOfGroupTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(isMemberOfGroupTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(isMemberOfGroupTask.getFaults());
		}
		return response;
	}

	public RemoveUserFromGroupResponseElement removeUserFromGroup(String adminSessionToken, ProfileIdentity profileIdentity, String groupName, String roleName, EmailTemplate[] emailTemplate) throws PassportServiceException {
		RemoveUserFromGroupTask removeUserFromGroupTask = new RemoveUserFromGroupTask();
		RemoveUserFromGroupResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("profileIdentity", profileIdentity);
			props.put("groupName", groupName);
			props.put("roleName", roleName);
			props.put("emailTemplate", emailTemplate);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				removeUserFromGroupTask.setSystemLangCode(mSystemLangCode);
			removeUserFromGroupTask.init(props);
			response = (RemoveUserFromGroupResponseElement) removeUserFromGroupTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(removeUserFromGroupTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(removeUserFromGroupTask.getFaults());
		}
		return response;
	}

	public CheckAuthorityResponseElement checkAuthority(String adminSessionToken, String userId) throws PassportServiceException {
		CheckAuthorityTask checkAuthorityTask = new CheckAuthorityTask();
		CheckAuthorityResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("adminSessionToken", adminSessionToken);
			props.put("userId", userId);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				checkAuthorityTask.setSystemLangCode(mSystemLangCode);
			checkAuthorityTask.init(props);
			response = (CheckAuthorityResponseElement) checkAuthorityTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(checkAuthorityTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(checkAuthorityTask.getFaults());
		}
		return response;
	}

	public ValidateSessionTokenResponseElement validateSessionToken(String guid, String newPassword, String newPasswordConfirm, String securityAnswer, String securityQuestion, String pin) throws PassportServiceException {
		ValidateSessionTokenTask validateSessionTokenTask = new ValidateSessionTokenTask();
		ValidateSessionTokenResponseElement response = null;
		try {
			HashMap props = new HashMap();
			props.put("guid", guid);
			props.put("newPassword", newPassword);
			props.put("newPasswordConfirm", newPasswordConfirm);
			props.put("securityAnswer", securityAnswer);
			props.put("securityQuestion", securityQuestion);
			props.put("pin", pin);
			if (mSystemLangCode != null && !mSystemLangCode.equals(""))
				validateSessionTokenTask.setSystemLangCode(mSystemLangCode);
			validateSessionTokenTask.init(props);
			response = (ValidateSessionTokenResponseElement) validateSessionTokenTask.invoke();
		} catch (TaskExecutionException e) {
			// displayFaults(validateSessionTokenTask.getFaults());
			if (!"Got Faults".equalsIgnoreCase(e.getMessage()))
				throw new PassportServiceException(e.getMessage());
			else
				throw new PassportServiceException(validateSessionTokenTask.getFaults());
		}
		return response;
	}

	public String getSystemLangCode() {
		return mSystemLangCode;
	}

	public void setSystemLangCode(String systemLangCode) {
		mSystemLangCode = systemLangCode;
	}
}
