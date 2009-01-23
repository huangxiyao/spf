package com.hp.it.spf.user.group.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hp.it.spf.user.exception.UserGroupsException;

public class SSOUserGroupRetrieverTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetGroups() throws UserGroupsException{
        String siteName = "smartportal";
        Map<String, String> userProfile = new HashMap<String, String>();
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
        IUserGroupRetriever retriever =new SSOUserGroupRetriever();
        Set<String> groupSet = retriever.getGroups(siteName, userProfile);  
        for(String group : groupSet) {
            System.out.println(group);
        }        
    }

}
