package com.hp.spp.webservice.ups.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;

public class UPSTester {

	/**
	 * Class to test the call to UPS in ITG
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		System.out.println("start");
		String login = "sppuser";
		String password = "Qwerty.1!";
//		String login = "car_ppa@yahoo.com";
//		String password = "Qwerty.1!";
		
		String site = "smartportal";
		String region = "EMEA";
		// Staging
//		String upsUrl = "https://hppwsstg.passport.hp.com/upsweb/services/UPSWebServices";
		// Systest
		String upsUrl = "https://hppwssys.passport.hp.com/upsweb/services/UPSWebServices";
		// Production
//		String upsUrl = "https://hppws.passport.hp.com/upsweb/services/UPSWebServices";
		
		// retrieve profile Id
		PassportService ws = new PassportService();
		String profileId = null;
		try {
			profileId = (ws.getProfileId(login)).getProfileId();
		} catch (PassportServiceException e) {
			String error = "PassportServiceException for Login [" + login
					+ "]";
			System.out.println(error);
			List list = e.getFaults();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Fault fault = (Fault) iter.next();
				System.out.println(fault.getDescription());
			}
			throw new IllegalStateException(error);
		}
//		profileId = "766a9f016848e587d7d4b0822cc05edf";
		System.out.println("profileID : ["+profileId + "]");

		// retrieve session token
//		String sessionToken = null;
//		try {
//			sessionToken = (ws.login(login, password)).getSessionToken();
//		} catch (PassportServiceException e) {
//			String error = "PassportServiceException for Login [" + login
//					+ "] and Password [" + password + "]: " + e.getMessage();
//			System.out.println(error);
//			List list = e.getFaults();
//			for (Iterator iter = list.iterator(); iter.hasNext();) {
//				Fault fault = (Fault) iter.next();
//				System.out.println(fault.getDescription());
//			}
//			throw new IllegalStateException(error);
//		}
//		System.out.println("session token ["+sessionToken + "]");
//
		Map userProfile = new HashMap();
		System.out.println("call to UPS");
//		// GetData (HPP + Siebel)
//		String[] attributeNames = {"hppid", "SiteName", "loginType", "SessionToken", "reg"};
//		String[] attributeValues = {profileId, site, "Simulated", sessionToken, region};
//		userProfile = new HPPUserProfileServiceClient().getUserProfile(userProfile, "GetData",
//				attributeNames, attributeValues, login, true, upsUrl);

		// sppqa_HPP_ALL (HPP)
//		String[] attributeNames = {"ProfileId","SessionToken","SiteName"};
//		String[] attributeValues = {profileId, sessionToken, site};
//		userProfile = new HPPUserProfileServiceClient().getUserProfile(userProfile, "sppqa_HPP_ALL",
//		attributeNames, attributeValues, login, true, upsUrl);

		// retrieve admin session token
		
		String adminLogin = "sppgrpadmin"; // DEV
//		String adminLogin = "sppgrp"; // ITG, PRO
		String adminPassword = "E$kenl8P"; // DEV 
//		String adminPassword = "gR#45X!$"; // ITG 
//		 String adminPassword = "Xj#LBSbE"; // PRO
		
		String adminSessionToken = null;
		try {
			adminSessionToken = (ws.login(adminLogin, adminPassword)).getSessionToken();
		} catch (PassportServiceException e) {
			String error = "PassportServiceException for Login [" + adminLogin
			+ "] and Password [" + adminPassword + "]: " + e.getMessage();	
			System.out.println(error);
			List list = e.getFaults();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Fault fault = (Fault) iter.next();
				System.out.println(fault.getDescription());
			}
			throw new IllegalStateException(error);
		}
		System.out.println("admin session token : "+adminSessionToken);

		// Admin_HPP_ALL (HPP)
		String[] attributeNames = {"ProfileId","AdminSessionToken","SiteName"};
		String[] attributeValues = {profileId, adminSessionToken, site};
		userProfile = new HPPUserProfileServiceClient().getUserProfile(userProfile, "Admin_HPP_ALL",
		attributeNames, attributeValues, login, true, upsUrl);

		// Admin_ALL (HPP + Siebel)
//		String[] attributeNames = {"hppid", "SiteName", "loginType", "AdminSessionToken", "reg"};
//		String[] attributeValues = {profileId, site, "Simulated", adminSessionToken, region};
//		userProfile = new HPPUserProfileServiceClient().getUserProfile(userProfile, "Admin_ALL",
//		attributeNames, attributeValues, login, true, upsUrl);

		if (userProfile != null){
		System.out.println("contains of the map retrieved from UPS");
		for (Iterator iter = userProfile.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			System.out.println("[" + key + "] = [" + userProfile.get(key) + "]");
		}
		} else {
			System.out.println("error or map null retrieved from UPS");
		}
	}

}
