package com.hp.it.spf.user.group.manager.example;

import com.hp.it.spf.user.group.stub.GroupResponse;
import com.hp.it.spf.user.group.stub.UserContext;
import com.hp.it.spf.user.group.stub.ArrayOfString;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplLocator;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImpl;
import com.hp.it.spf.user.group.stub.GetGroups;
import com.hp.it.spf.user.group.stub.GroupRequest;
import com.hp.it.spf.user.group.stub.ArrayOfUserContext;
import com.hp.it.spf.user.group.manager.IUserGroupRetriever;
import com.hp.it.spf.user.group.manager.UGSUserGroupRetriever;
import com.hp.it.spf.sso.portal.MockeryUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.jmock.Mockery;
import org.jmock.Expectations;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UGSCall
{
	public static void main(String[] args) throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("com.hp.it.spf").setLevel(Level.DEBUG);

		String ugsUrl = "https://hppwsstg.passport.hp.com/xfire-webservice-webapp/services/UGSRuntimeServiceXfireImpl";
		String siteName = "acme";
		if (args.length == 2) {
			ugsUrl = args[0];
			siteName = args[1];
		}

		UserProfile userProfile = new UserProfile() {{
			attribute("ProfileId").is("6cb23f1203fc672c1a31fd2649920524");
			attribute("createtimestamp").is("2009-03-10 02:59:20");
			attribute("PhoneContactPref").is("N");
			attribute("homeState").is("");
			attribute("homeCity").is("home_city");
			attribute("clang").is("CN-13");
			attribute("givenname").is("Ye");
			attribute("1").is("[Crazy]");
			attribute("Language").is("zh");
			attribute("firstName").is("Ye");
			attribute("SecurityLevel").is("0.0");
			attribute("FirstName").is("Ye");
			attribute("homeAddressLine2").is("home_address2");
			attribute("4").is("Shanghai");
			attribute("LastLoginDate").is("Tue Mar 17 18:13:48 CST 2009");
			attribute("homeTelephoneNumber").is("86(021)12345678");
			attribute("signInName").is("liuye_caf");
			attribute("hppprofileId").is("6cb23f1203fc672c1a31fd2649920524");
			attribute("contactPrefTelephone").is("N");
			attribute("homeTelephoneCountryCode").is("86");
			attribute("langcode").is("13");
			attribute("TeleCityCD").is("021");
			attribute("homeCounty").is("CN");
			attribute("contactPrefPost").is("N");
			attribute("homeAddressLine1").is("home_address1");
			attribute("homeTelephoneCityCode").is("021");
			attribute("hpclidnumber").is("6cb23f1203fc672c1a31fd2649920524");
			attribute("preferredlanguage").is("13");
			attribute("homeZipCodePostCode").is("021");
			attribute("PostalContactPref").is("N");
			attribute("hpclname").is("liuye_caf");
			attribute("lastname").is("Liu");
			attribute("sn").is("Liu");
			attribute("Email").is("liuye@hp.com");
			attribute("PhoneNumber").is("null");
			attribute("PhoneNumberExt").is("null");
			attribute("LastChangeDate").is("Tue Mar 10 03:03:18 CST 2009");
			attribute("Country").is("CN");
			attribute("LoginId").is("liuye_caf");
			attribute("contactPrefEmail").is("N");
			attribute("hpresidentcountrycode").is("CN");
			attribute("email").is("liuye@hp.com");
			attribute("Timezone").is("America/Los_Angeles");
			attribute("homeCountryCode").is("CN");
			attribute("modifytimestamp").is("2009-03-10 03:03:18");
			attribute("EmailContactPref").is("N");
			attribute("LastName").is("Liu");
			attribute("homePostalcode").is("021");

//			attribute("Country").is("PL;FR;US");
//			attribute("Title").is("Mr");
//			attribute("SecurityLevel").is("2");

		}};


//		UGSRuntimeServiceXfireImpl gms = new UGSRuntimeServiceXfireImplLocator();
//		UGSRuntimeServiceXfireImplHttpBindingStub binding =
//				(UGSRuntimeServiceXfireImplHttpBindingStub)gms.getUGSRuntimeServiceXfireImplHttpPort(new URL(ugsUrl));
//
//		UserContext[] userContextItems = userProfile.userContext();
//		GroupResponse response = binding.getGroups(new GetGroups(new GroupRequest(
//				"SPF_" + siteName, new ArrayOfUserContext(userContextItems)))).getOut();
//
//		System.out.println("Groups retrieved: " + returnedGroups(response));

		IUserGroupRetriever groupRetriever = new UGSUserGroupRetriever();
		HttpServletRequest request = createHttpServletRequest(siteName);
		System.out.println("Groups retrieved with retriever: " +
				groupRetriever.getGroups(userProfile.map(), request));
	}

	private static HttpServletRequest createHttpServletRequest(final String siteName)
	{
		Mockery context = new Mockery();
		final HttpServletRequest result = MockeryUtils.mockHttpServletRequest(context);
		context.checking(new Expectations() {{
			allowing(result).getPathInfo(); will(returnValue("/" + siteName));
		}});
		return result;
	}

	private static List<String> returnedGroups(GroupResponse response)
	{
		ArrayOfString list = response.getGroupList();
		if (list == null) {
			return Collections.emptyList();
		}
		else {
			return Arrays.asList(list.getString());
		}
	}

	private static class UserProfile {
		private Map<String, Object> mProfile = new TreeMap<String, Object>();

		public UserProfileAttribute attribute(String name) {
			return new UserProfileAttribute(name);
		}

		public Map<String, Object> map() {
			return mProfile;
		}

		public UserContext[] userContext() {
			UserContext[] result = new UserContext[mProfile.size()];
			int i = 0;
			for (Map.Entry<String, Object> item : mProfile.entrySet()) {
				result[i] = new UserContext(item.getKey(), String.valueOf(item.getValue()));
				i++;
			}
			return result;
		}

		private class UserProfileAttribute {
			private String mAttributeName;

			private UserProfileAttribute(String attributeName)
			{
				mAttributeName = attributeName;
			}

			private void is(Object value) {
				UserProfile.this.mProfile.put(mAttributeName, value);
			}
		}
	}
}
