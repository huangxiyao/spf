package com.hp.spp.webservice.monitoring;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.hp.spp.config.ConfigException;
import com.hp.spp.profile.Constants;
import com.hp.spp.webservice.ugs.manager.UserGroupServiceFactory;

/**
 * Class to check that UserGroup WS are up.
 * Needs the creation of the defautl Group for site 'sppqa'.
 * 
 * @author Mathieu Vidal
 *
 */
public class UserGroupMonitoringServlet extends HttpServlet {

	private static Logger mLog = Logger.getLogger(UserGroupMonitoringServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (mLog.isDebugEnabled()) {
			mLog.debug("UserGroupMonitoringServlet called");
		}

		String responseMsg = "USER GROUP WEB SERVICE KO";

		try {
			Object[] groups = getGroups();
			if (groups != null && groups.length != 0) {
				responseMsg = "USER GROUP WEB SERVICE OK";
			}
		} catch (Exception e) {
			mLog.error("Error calling the UserGroup web service", e);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("UserGroupMonitoringServlet response message: " + responseMsg);
		}

		response.getWriter().print(responseMsg);
	}

	private Object[] getGroups() throws RemoteException, ServiceException, ConfigException, MalformedURLException {

		HashMap userProfile = getUserProfile();

		String siteName = (String) userProfile.get(Constants.MAP_SITE);
		return new UserGroupServiceFactory().newUserGroupService(siteName).getUserGroups(siteName, userProfile);
	}

	@SuppressWarnings("unchecked")
	public HashMap getUserProfile()	{

		HashMap profile = new HashMap();

		// With the site name, at least one group is returned
		profile.put(Constants.MAP_SITE, "sppqa");
		profile.put(Constants.MAP_SITE_ID, "sppqa");

		// but some (maybe not all) of these attributes need to be present too
		profile.put(Constants.MAP_COMPANY_NAME, "HP");
		profile.put(Constants.MAP_COMPANY_NUMBER, "1");
		profile.put(Constants.MAP_COUNTRY, "UK");
		profile.put(Constants.MAP_EMAIL, "mathieu.vidal@hp.com");
		profile.put(Constants.MAP_FIRSTNAME, "mathieu");
		profile.put(Constants.MAP_HOMEPAGE, "http://dummy.fr");
		profile.put(Constants.MAP_HPPID, "d4d4fb3c4d8e16b0c84f29113e96024b");
		profile.put(Constants.MAP_IS_SIMULATING, "false");
		profile.put(Constants.MAP_LANGUAGE, "en");
		profile.put(Constants.MAP_LASTLOGINDATE, "1148398836000");
		profile.put(Constants.MAP_LASTNAME, "vidal");
		profile.put(Constants.MAP_PORTALSESSIONID, "GzQVkWVx60v2pHLrSQTlfJFqBG62G9H0phgbtL2MLfRGFQSk8JB7!-144656560!1148391573629");
		profile.put(Constants.MAP_STATUS, "1");
		profile.put(Constants.MAP_UPDATEDDATE, "1147368007000");
		profile.put(Constants.MAP_USERGROUPS, "GENERIC_UK_IRELAND_PARTNERS;PROFILE_EASY_CATALOGUE;GENERIC_ALL_PARTNERS;PROFILE_PARTNER_ONE;PROFILE_HP_CAT;GENERIC_NO_PC_COMPETITOR");
		profile.put(Constants.MAP_USERNAME, "spp");
		profile.put("PhysAdLine1", "21 Grovelands Avenue");
		profile.put("PartnerNameHQEMEA", "");
		profile.put("PartnerProIdHQ", "1-1WV-40");
		profile.put("HpInternalUser", "T");
		profile.put("PartnerLegalName", "Destec Systems");
		profile.put("PartnerName", "DESTEC SYSTEMSSWINDON");
		profile.put("CountryCode", "GB");
		profile.put("Department", "Engineering/IT");
		profile.put("Seniority", "Board/President/Owner");
		profile.put("HPOrg", "HP United Kingdom");
		profile.put("PhysAdLine3", "");
		profile.put("PartnerProId", "1-1WV-40");
		profile.put("BlnPhaseOut", "F");
		profile.put("DateOfBirth", "");
		profile.put("PhysCity", "Swindon");
		profile.put("Accreditations", "");
		profile.put("UserRole", "0");
		profile.put("PhysPostalCode", "SN1 4ET");
		profile.put("ReceiveFax", "0");
		profile.put("PartnerTypes", "Sales Partner;Sales Partner");
		profile.put("UserFax", "+44 (0)1793 610739");
		profile.put("BlnPhaseIn", "F");
		profile.put("Programs", "EU-zz05-z026;EU-1006-I;EU-zz05-z085;80-UC06-REG;80-2001-ARP");
		profile.put("PartnerPhone", "+44 (0)1793 496217");
		profile.put("PartnerNameHQ", "DESTEC SYSTEMSSWINDON");
		profile.put("Contact_HPRole", "");
		profile.put("ReceiveMail", "0");
		profile.put("ReceiveSMS", "0");
		profile.put("CurrentStatus", "1");
		profile.put("PhysCountry", "GB");
		profile.put("ReceiveEmail", "0");
		profile.put("title", "Mr");
		profile.put("IsPartnerAdmin", "F");
		profile.put("CBN", "");
		profile.put("PartnerFax", "+44 (0)1793 610739");
		profile.put("PhysAdLine2", "Old Town");
		profile.put("PreferredLanguageCode", "IX");
		profile.put("CompanyNumber", "");
		profile.put("ReceiveAlert", "0");
		profile.put("ReceiveNewsLetter", "0");
		profile.put("ReceiveCall", "0");
		profile.put("IsHybrid", "F");
		profile.put("Tier", "2");
		profile.put("HierarchyType", "Headquarters");
		profile.put("PartnerSubTypes", "SSP;");
		profile.put("JobFunction", "");
		profile.put("UserPhone", "6681");
		profile.put("Campaigns", "");
		profile.put("PrimaryChannelSegment", "TDL");
		profile.put("ProgramName", "EU-Easysales");
		profile.put("ProgramRowId", "1-IIW5N");
		profile.put("TemplateName", "");
		profile.put("Title", "Mr");
		profile.put("SecurityLevel", "1");
		return profile;
	}

}
