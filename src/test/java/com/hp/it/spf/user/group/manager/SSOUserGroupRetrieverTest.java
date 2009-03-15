/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.UserManager;
import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.MockeryUtils;
import com.hp.it.spf.user.exception.UserGroupsException;
import com.hp.it.spf.user.group.stub.UserContext;

/**
 * This is the test class for SSOUserGroupRetriever class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.user.group.manager.UGSUserGroupRetriever
 */
public class SSOUserGroupRetrieverTest {
    Map<String, Object> userProfile = new HashMap<String, Object>();
    
    private static HttpServletRequest request;
    
    private static Mockery context;
    
    /**
    * Init mock objects.
    * 
    * @throws java.lang.Exception
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       context = MockeryUtils.createMockery();
       request = MockeryUtils.mockHttpServletRequestForANON(context);      
   }

   /**
    * Claenup the resource. Set static pararmters to null, so the object they
    * refer to can be GCed by JVM.
    * 
    * @throws Exception
    */
   @AfterClass
   public static void tearDownAfterClass() throws Exception {
       context = null;
       request = null;      
   }
    
    /**
     * Init siteName parameter and userProfile map for test
     * 
     * @throws Exception if setUp operation failed.
     */
    @Before
    public void setUp() throws Exception {        
        userProfile.put("Accreditations", "EU-02pp-Q530;EU-02pp-N342;EU-02pp-O138");
        userProfile.put("AgreementNos", "");
        userProfile.put("AgreementSubTypes", "");
        userProfile.put("AgreementTypes", "");
        userProfile.put("ApplicationStage", "Authorized");
        userProfile.put("BlnPhaseIn", "F");
        userProfile.put("BlnPhaseOut", "F");
        userProfile.put("Campaigns", "");
        userProfile.put("CompanyNumber", "40636315");
        userProfile.put("ConsentFlag", "Y");
        userProfile.put("ContactId", "");
        userProfile.put("Contact_HPRole", "");
        userProfile.put("Country", "FR");
        userProfile.put("CountryCode", "FR");
        userProfile.put("CurrentSiteUrl", "https://sppitg.austin.hp.com/portal/site/smartportal/");
        userProfile.put("DateOfBirth", "");
        userProfile.put("DefaultPartnerPreferredLanguageCode", "");
        userProfile.put("Department", "");
        userProfile.put("Email", "jxxzhj-pm80emea001@yahoo.com.cn");
        userProfile.put("EmailContactPreference", "U");
        userProfile.put("EmailFormatPreference", "");
        userProfile.put("ExternalIds", "");
        userProfile.put("FirstName", "c");
        userProfile.put("HPInternalUser", "F");
        userProfile.put("HPOrg", "HP France");
        userProfile.put("HPOrgs", "HP France;hellomoto");
        userProfile.put("HPPPreferedLanguageCode", "en");
        userProfile.put("HPPUserId", "f1b8d18d02289b8fbde546ec5ba6d6ff");
        userProfile.put("HPRole", "");
        userProfile.put("HPRoles", "");
        userProfile.put("HierarchyType", "Headquarters");
        userProfile.put("HomePageUrl", "https://sppitg.austin.hp.com/portal/site/smartportal/");
        userProfile.put("HostingBusinessGroup", "");
        userProfile.put("IsHybrid", "F");
        userProfile.put("IsPartnerAdmin", "T");
        userProfile.put("IsSimulating", "FALSE");
        userProfile.put("JobFunctions", "");
        userProfile.put("Job_Functions", "");
        userProfile.put("KeyReasons", "");
        userProfile.put("Language", "EN");
        userProfile.put("LastLoginDate", "1232680581865");
        userProfile.put("LastName", "jx - 001");
        userProfile.put("LoginId", "pm80emea001@yahoo.com.cn");
        userProfile.put("MiddleName", "");
        userProfile.put("NeverCall", "Y");
        userProfile.put("NeverEmail", "Y");
        userProfile.put("NeverFax", "Y");
        userProfile.put("NeverMail", "Y");
        userProfile.put("NeverSMS", "Y");
        userProfile.put("ParentPartnerId", "");
        userProfile.put("PartnerAgreements", "");
        userProfile.put("PartnerClass", "");
        userProfile.put("PartnerFax", "");
        userProfile.put("PartnerHierarchyType", "Headquarters");
        userProfile.put("PartnerLegalName", "Test_cjx_France_20080317");
        userProfile.put("PartnerManagement", "");
        userProfile.put("PartnerName", "Test_cjx_France_20080317");
        userProfile.put("PartnerNameHQ", "Test_cjx_France_20080317");
        userProfile.put("PartnerNameHQEMEA", "");
        userProfile.put("PartnerPhone", "");
        userProfile.put("PartnerPhysicalAddressCity", "London");
        userProfile.put("PartnerPhysicalAddressCountry", "GB");
        userProfile.put("PartnerPhysicalAddressLastUpdateDate", "54:11.0");
        userProfile.put("PartnerPhysicalAddressLine1", "UK");
        userProfile.put("PartnerPhysicalAddressLine2", "");
        userProfile.put("PartnerPhysicalAddressLine3", "");
        userProfile.put("PartnerPhysicalAddressPostalCode", "");
        userProfile.put("PartnerPhysicalAddressStateProvince", "");
        userProfile.put("PartnerProId", "1-3WDLK7");
        userProfile.put("PartnerProIdHQ", "1-3WDLK7");
        userProfile.put("PartnerSubTypes", "Supplies Connect");
        userProfile.put("PartnerTargetSegment", "");
        userProfile.put("PartnerTier", "");
        userProfile.put("PartnerTypes", "Sales Partner;Supplies Partner");
        userProfile.put("PartnershipContacts", "");
        userProfile.put("PersonId", "");
        userProfile.put("PhoneContactPreference", "U");
        userProfile.put("PhysAdLine1", "UK");
        userProfile.put("PhysAdLine2", "");
        userProfile.put("PhysAdLine3", "");
        userProfile.put("PhysCity", "London");
        userProfile.put("PhysCountry", "GB");
        userProfile.put("PhysPostalCode", "");
        userProfile.put("PortalPreferredLanguageCode", "EN");
        userProfile.put("PortalSessionId", "R0KgJ52HLG5HLN2fmNqZgy179pyWK6rYC1Z2jdnjzKzxbQZ7kvvL!94898267!-1095859323!1232680551802");
        userProfile.put("PostalContactPreference", "U");
        userProfile.put("PreferredLanguageCode", "EN");
        userProfile.put("PrimaryChannelSegment", "CRS");
        userProfile.put("PrimaryContactId", "");
        userProfile.put("PrimaryHPRoleId", "No Match Row Id");
        userProfile.put("PrimaryHPRoleName", "");
        userProfile.put("PrimaryPartnerType", "Sales Partner");
        userProfile.put("ProgramNames", "PPro Portal;EU-Easysales;EU-Smartquote");
        userProfile.put("Programs", "100000;EU-01pp-N784;EU-01pp-O925");
        userProfile.put("PurchasingType", "Indirect");
        userProfile.put("ReceiveCall", "N");
        userProfile.put("ReceiveEmail", "N");
        userProfile.put("ReceiveFax", "N");
        userProfile.put("ReceiveMail", "N");
        userProfile.put("ReceiveSMS", "N");
        userProfile.put("ResponsibilityNames", "HP Partner Portal Admin - EMEA");
        userProfile.put("SecurityAnswer", "");
        userProfile.put("SecurityLevel", "1");
        userProfile.put("SecurityQuestion", "");
        userProfile.put("SegmentStatus", "Existing");
        userProfile.put("Seniority", "");
        userProfile.put("SiebelHPPId", "f1b8d18d02289b8fbde546ec5ba6d6ff");
        userProfile.put("SiebelPreferredLanguageCode", "EN");
        userProfile.put("SiebelStatus", "Active");
        userProfile.put("SiebelUserEmailId", "jxxzhj-pm80emea001@yahoo.com.cn");
        userProfile.put("SiteIdentifier", "smartportal");
        userProfile.put("SiteName", "smartportal");
        userProfile.put("Sites", "");
        userProfile.put("Status", "1");
        userProfile.put("TemplateName", "EU-Profiling Form Complete FY07;EU-Profiling Form Complete FY05;EU-Transfer to CRM");
        userProfile.put("Tier", "2");
        userProfile.put("Title", "");
        userProfile.put("UserCampaigns", "");
        userProfile.put("UserCellphone", "");
        userProfile.put("UserCountryOfBusiness", "FR");
        userProfile.put("UserEmailId", "jxxzhj-pm80emea001@yahoo.com.cn");
        userProfile.put("UserFax", "");
        userProfile.put("UserGroups", "TEST_GROUP;LOCAL_SMARTPORTAL_ADMIN;PARTNER_EDUCATION_SETUP_REQUIRED;GENERIC_EXCLUDE_ALL_COMPETITORS;GENERIC_EXCLUDE_LASERJET_INKJET_COMPETITOR;PROFILE_SMARTQUOTE;SMARTPORTAL_ALLUSERS;GENERIC_EXCLUDE_MOBILITY_COMPETITOR;PROFILE_SQUOTE;GENERIC_PARTNER_PORTAL_ADMINISTRATOR;GENERIC_EXCLUDE_SUPPLIES_COMPETITOR;PROFILE_CSN_NON_ASP;GENERIC_EXCLUDE_ISS_COMPETITOR;GENERIC_EXCLUDE_LASER_PRINTER_COMPETITOR;PROFILE_SPP_SEARCH;PROFILE_PRODUCT_BROWSER_SUPPLIES;PROFILE_PRODUCT_BROWSER_STORAGE;PROFILE_CERTIFICATION_STATUS;GENERIC_EXCLUDE_PC_COMPETITOR;LOCAL_SMARTPORTAL_CONSOLE_BASIC;LOCAL_SMARTPORTAL_SHOWALL;PROFILE_NEW_SMARTQUOTE;PROFILE_EASY_SALES;LOCAL_SMARTPORTAL_CONSOLE;PROFILE_PRODUCT_BROWSER_RACK_AND_POWER;GENERIC_EXCLUDE_BCS_COMPETITOR;LOCAL_SMARTPORTAL_CONSOLE;GENERIC_EXCLUDE_NETWORKING_COMPETITOR;LOCAL_SMARTPORTAL_ADMIN;PROFILE_3P_SEARCH;GENERIC_EXCLUDE_SWD_COMPETITOR;PROFILE_PRODUCT_BROWSER_PC_MONITOR_PROCURVE_SERVERS;GENERIC_EXCLUDE_INKJET_COMPETITOR;PROFILE_GPC_SEARCH;PROFILE_PARTNER_ONE;GENERIC_ALL_PARTNERS;GENERIC_EXCLUDE_PSG_COMPETITOR;PROFILE_LITWEB");
        userProfile.put("UserId", "1-3WDLKG");
        userProfile.put("UserPhone", "");
        userProfile.put("UserRights", "LOCAL_SECURITY_Simulation;LOCAL_SECURITY_SmartQuote");
        userProfile.put("UserType", "");
    }

    /**
     * Test GetGroups method and check the return value.
     * 
     * @see UGSUserGroupRetriever#getGroups(String, Map)
     */
    @Test
    public void testGetGroups() {
        IUserGroupRetriever retriever = new UGSUserGroupRetriever();
        Set<String> groupSet;
        // request, userProfile both assigned.
        try {
            groupSet = retriever.getGroups(userProfile, request);
            assertNotNull("Result group set shouldn't be null.", groupSet);
        } catch (UserGroupsException ex) {
            //assertFalse(ex.getMessage(), true);
        }  
     
        // request is assigned, userProfile is null
        try {
            groupSet = retriever.getGroups(null, request);            
        } catch (UserGroupsException ex) {
            assertTrue(ex.getMessage(), true);
        }  
        
        // request is null, userProfile is assigned
        try {
            groupSet = retriever.getGroups(userProfile, null);            
        } catch (UserGroupsException ex) {
            assertTrue(ex.getMessage(), true);
        }  
        
        // request is null, userProfile is an empty map
        try {
            groupSet = retriever.getGroups(new HashMap<String, Object>(), null);            
        } catch (UserGroupsException ex) {
            assertTrue(ex.getMessage(), true);
        }  
        
        // both are null
        try {
            groupSet = retriever.getGroups(null, null);            
        } catch (UserGroupsException ex) {
            assertTrue(ex.getMessage(), true);
        }          
        
        // request is assigned, userProfile is an invaild map.
        try {
            Map<String, Object> profiles = new HashMap<String, Object>();
            profiles.put("test", "test");            
            groupSet = retriever.getGroups(profiles, request);
            // should be an empty set
            assertEquals(groupSet.size(), 0);
        } catch (UserGroupsException ex) {
            //assertFalse(ex.getMessage(), true);
        }
    }

	@Test
	public void testConvertToUserContext() {
		Map<String, Object> profile = new HashMap<String, Object>();

		// String attribute values
		profile.put("simpleAttribute", "value");
		profile.put("emptyAttribute", "");
		profile.put("nullAttribute", null);

		// List<String> attribute value
		profile.put("emptyListOfStringsAttribute", Collections.emptyList());
		profile.put("listOfStringsAttribute", Arrays.asList("item1", "item2", "item3"));

		// Map attribute value
		Map<String, String> mapAttribute = new HashMap<String, String>();
		mapAttribute.put("property1", "value1");
		mapAttribute.put("property2", "value2");
		profile.put("mapAttributeValue", mapAttribute);

		// Single element list of maps attribute
		Map<String, String> mapAttribute3 = new HashMap<String, String>();
		mapAttribute3.put("property5", "value5");
		mapAttribute3.put("property6", "value6");
		profile.put("singleElementListOfMapsAttribute", Arrays.asList(mapAttribute3));

		// List of maps attribute
		Map<String, String> mapAttribute1 = new HashMap<String, String>();
		mapAttribute1.put("property3", "value3.1");
		mapAttribute1.put("property4", "value4.1");
		Map<String, String> mapAttribute2 = new HashMap<String, String>();
		mapAttribute2.put("property3", "value3.2");
		mapAttribute2.put("property4", "value4.2");
		profile.put("listOfMapsAttribute", Arrays.asList(mapAttribute1, mapAttribute2));

		// Something else attribute
		Object somethingElseAttributeValue = new Object() {
			@Override
			public String toString()
			{
				return "something else";
			}
		};
		profile.put("somethingElseAttribute", somethingElseAttributeValue);

		// Call the method we're testing
		UserContext[] userContextItems = new UGSUserGroupRetriever().convertToUserContext(profile);

		// Convert result to map so we can easily access it using keys
		Map<String, String> userContextItemsMap = new HashMap<String, String>();
		for (UserContext item : userContextItems) {
			userContextItemsMap.put(item.getKey(), item.getValue());
		}

		// Simple attribute values
		assertEquals("simple attribute value",
				"value", userContextItemsMap.get("simpleAttribute"));
		assertEquals("empty attribute value",
				"", userContextItemsMap.get("emptyAttribute"));
		assertEquals("null attribute value",
				null, userContextItemsMap.get("nullAttribute"));

		// List<String> attribute value
		assertEquals("empty list of strings attribute value",
				null, userContextItemsMap.get("emptyListOfStringsAttribute"));
		assertEquals("list of strings attribute value",
				"item1;item2;item3", userContextItemsMap.get("listOfStringsAttribute"));

		// Map attribute value
		assertEquals("map attribute value for mapAttributeValue.property1",
				"value1", userContextItemsMap.get("mapAttributeValue.property1"));
		assertEquals("map attribute value for mapAttributeValue.property2",
				"value2", userContextItemsMap.get("mapAttributeValue.property2"));

		// Single element list of maps attribute
		assertEquals("single element list of maps attribute value for singleElementListOfMapsAttribute.property5",
				"value5", userContextItemsMap.get("singleElementListOfMapsAttribute.property5"));
		assertEquals("single element list of maps attribute value for singleElementListOfMapsAttribute.property6",
				"value6", userContextItemsMap.get("singleElementListOfMapsAttribute.property6"));

		// List of map attributes
		assertEquals("list of maps attribute value for istOfMapsAttribute.property3",
				"value3.1;value3.2", userContextItemsMap.get("listOfMapsAttribute.property3"));
		assertEquals("list of maps attribute value for listOfMapsAttribute.property4",
				"value4.1;value4.2", userContextItemsMap.get("listOfMapsAttribute.property4"));

		// something else attribute
		assertEquals("Not supported attribute value is saved as its string representation",
				somethingElseAttributeValue.toString(), userContextItemsMap.get("somethingElseAttribute"));

	}

}
