
-- SPP_SITE
INSERT INTO SPP_SITE ( ID, NAME ) VALUES ( SPP_SITE_ID_SEQ.nextval, 'sppqa');


--SPP_PORTAL
INSERT INTO SPP_PORTAL ( NAME, LANDING_PAGE, LOCALE_IN_URL, PORTLET_ID, HPAPPID, UPS_QUERY_ID, HOME_PAGE, LOGOUT_PAGE, PROTOCOL, PERSIST_SIMULATION ) VALUES ( 
'console', 'console/menuitem.a175504b5ac626836523754fe8039e01', 0, 'd27b4a058d6b9623055a97c2e8039e01'
, 'SPPQA', 'sppqa_HPP_ALL', NULL, NULL, 'http', NULL); 
INSERT INTO SPP_PORTAL ( NAME, LANDING_PAGE, LOCALE_IN_URL, PORTLET_ID, HPAPPID, UPS_QUERY_ID, HOME_PAGE, LOGOUT_PAGE, PROTOCOL, PERSIST_SIMULATION ) VALUES ( 
'sppqa', 'publicsppqa/menuitem.30828e5bfbf5aff1055a97c2e8039e01', 0, 'd27b4a058d6b9623055a97c2e8039e01'
, 'SPPQA', 'sppqa_HPP_ALL', 'deccf91c235e4555055a97c2e8039e01', NULL, 'http', NULL); 



INSERT INTO SPP_ESERVICE ( ID, SITE_ID, STANDARDPARAMETERSET_ID, NAME, METHOD, PRODUCTION_URL, TEST_URL, CREATION_DATE, LAST_MODIFICATION_DATE, IS_NEW_WINDOW, SECURITY_MODE ) VALUES ( 
SPP_ESERVICE_ID_SEQ.nextval, SPP_SITE_ID_SEQ.currval, NULL, 'Contact HP', 'GET', 'http://welcome.hp.com/country/us/en/contact_us.html'
, 'http://welcome.hp.com/country/us/en/contact_us.html',  TO_Date( '10/25/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
,  TO_Date( '10/25/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, 0); 

INSERT INTO SPP_ESERVICE ( ID, SITE_ID, STANDARDPARAMETERSET_ID, NAME, METHOD, PRODUCTION_URL, TEST_URL, CREATION_DATE, LAST_MODIFICATION_DATE, IS_NEW_WINDOW, SECURITY_MODE ) VALUES ( 
SPP_ESERVICE_ID_SEQ.nextval, SPP_SITE_ID_SEQ.currval, NULL, 'ForgotPassword', 'POST', 'http://sppitg.houston.hp.com/portal/jsp/SPP/dummyForgotPassword.html'
, 'http://sppitg.houston.hp.com/portal/jsp/SPP/dummyForgotPassword.html',  TO_Date( '09/26/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')

,  TO_Date( '09/26/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, 0); 
INSERT INTO SPP_ESERVICE ( ID, SITE_ID, STANDARDPARAMETERSET_ID, NAME, METHOD, PRODUCTION_URL, TEST_URL, CREATION_DATE, LAST_MODIFICATION_DATE, IS_NEW_WINDOW, SECURITY_MODE ) VALUES ( 
SPP_ESERVICE_ID_SEQ.nextval, SPP_SITE_ID_SEQ.currval, NULL, 'EditProfile', 'POST', 'https://hpptest2.external.hp.com/hpp/modify.do?s_lang=en&hpappid=test&smredirect=https%3A//ppdevatl.external.hp.com/hpp/test.do'
, 'https://hpptest2.external.hp.com/hpp/modify.do?s_lang=en&hpappid=test&smredirect=https%3A//ppdevatl.external.hp.com/hpp/test.do'
,  TO_Date( '09/26/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '09/26/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 0); 

INSERT INTO SPP_ESERVICE ( ID, SITE_ID, STANDARDPARAMETERSET_ID, NAME, METHOD, PRODUCTION_URL, TEST_URL, CREATION_DATE, LAST_MODIFICATION_DATE, IS_NEW_WINDOW, SECURITY_MODE ) VALUES ( 
SPP_ESERVICE_ID_SEQ.nextval, SPP_SITE_ID_SEQ.currval, NULL, 'Register', 'POST', 'https://hpptest2.external.hp.com/hpp/newuser.do', 'https://hpptest2.external.hp.com/hpp/newuser.do'
,  TO_Date( '09/26/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '09/26/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 0); 



INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 0); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 2); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 3); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50001); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50003); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50004); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50006); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50007); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50008); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50009); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50010); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50011); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50013); 
INSERT INTO SPP_ERROR_CODE ( ERROR_CODE ) VALUES ( 50014); 



INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 0, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 2, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 3, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50001, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50003, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50004, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50006, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50007, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50008, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50009, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50010, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50011, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50013, 'LANDING_PAGE', 1); 
INSERT INTO SPP_WORKFLOW_ERROR ( PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE ) VALUES ( 
'sppqa', 50014, 'LANDING_PAGE', 1); 









INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.IndexListView', 'jsp/SPP/search/IndexListView.jsp', 'S', 'Y', 'N', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.AgentPageView', 'jsp/SPP/search/AgentPageView.jsp', 'S', 'Y', 'N', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.ErrorPage', 'jsp/SPP/search/ErrorPage.jsp', 'S', 'Y', 'N', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.castorMappingFile', '/com/hp/spp/search/audience/service/AudiencingCastorMapping.xml'
, 'S', 'Y', 'N', 'name of file having castor mappings'); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.PortalEngineClassName', 'com.hp.spp.search.vap.VignettePortalEngine', 'S'
, 'Y', 'N', 'name of class implementing the PortalEngine'); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.ugs.url', 'http://sppdev2.gre.hp.com/spp-services-web/services/UserGroupManager'
, 'S', 'Y', 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.ups.url', 'http://sppdev2.gre.hp.com/upsweb/services/UPSWebServices', 'S', 'Y'
, 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.eservice.url', 'http://sppdev2.gre.hp.com/spp-services-web/services/EServiceManager'
, 'S', 'Y', 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.sppqa_HPP_ALL.AttributeNames', 'UserId;ProfileId;SessionToken;SiteName', 'S'
, 'Y', 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.admin.login', 'sppgrpadmin', 'S', 'Y', 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.admin.ups.query.id', 'Admin_HPP_ALL', 'S', 'Y', 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.sppportal.HPPAdminPassword', 'T2SfR3qLkG+AOF9onm+mEQ==', 'S', 'Y', 'Y', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.Admin_HPP_ALL.AttributeNames', 'UserId;ProfileId;AdminSessionToken;SiteName'
, 'S', 'Y', 'Y', NULL); 


-- GUEST USER
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927638, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f1', 'sso_guest_user_bg'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_bg', 'm', 'BG', 'bg@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927639, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f2', 'sso_guest_user_cs'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_cs', 'm', 'CZ', 'cs@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927640, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f3', 'sso_guest_user_da'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_da', 'm', 'DK', 'da@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927641, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f4', 'sso_guest_user_de'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_de', 'm', 'DE', 'de@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927642, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f5', 'sso_guest_user_el'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_el', 'm', 'GR', 'el@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-682376413, 'epi:user.standard;698496d719858aa24610829a85daa8f0', 'sso_guest_user_en'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_en', 'm', 'US', 'en@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 02:18:39 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927643, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f6', 'sso_guest_user_es'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_es', 'm', 'ES', 'es@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927644, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f7', 'sso_guest_user_fi'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_fi', 'm', 'FI', 'fi@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
1223349377, 'epi:user.standard;36cdff06b63cf4b24610829a85daa8f0', 'sso_guest_user_fr'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_fr', 'm', 'FR', 'fr@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 02:19:22 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927645, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f8', 'sso_guest_user_hr'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_hr', 'm', 'HR', 'hr@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927646, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa8f9', 'sso_guest_user_hu'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_hu', 'm', 'HU', 'hu@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927647, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f0', 'sso_guest_user_it'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_it', 'm', 'IT', 'it@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927648, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f1', 'sso_guest_user_nl'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_nl', 'm', 'NL', 'nl@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927649, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f2', 'sso_guest_user_no'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_no', 'm', 'NO', 'no@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927650, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f3', 'sso_guest_user_pl'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_pl', 'm', 'PL', 'pl@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927651, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f4', 'sso_guest_user_pt'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_pt', 'm', 'PT', 'pt@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927652, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f5', 'sso_guest_user_ro'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_ro', 'm', 'RO', 'ro@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927653, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f6', 'sso_guest_user_ru'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_ru', 'm', 'RU', 'ru@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927654, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f7', 'sso_guest_user_sk'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_sk', 'm', 'SK', 'sk@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927655, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f8', 'sso_guest_user_sl'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_sl', 'm', 'SI', 'sl@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927656, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9f9', 'sso_guest_user_sv'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_sv', 'm', 'SE', 'sv@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 
INSERT INTO USERS ( USER_ID, UNIQUE_ID, USER_NAME, DOMAIN, FIRST_NAME, LAST_NAME, GENDER
, COUNTRY, EMAIL_ADDRESS, TIMEZONE, CREATED_DATE, ENTITY_TYPE ) VALUES ( 
-210927657, 'epi:user.standard;5dd05fb46dc8870a36a2be0a85daa9fa', 'sso_guest_user_tr'
, 'PRMSSPP Web Server Realm', 'Guest', 'SSO_GUEST_USER_tr', 'm', 'TR', 'tr@sso_guest_user.com'
, 0,  TO_Date( '08/31/2006 11:47:43 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'epi:user.standard'
); 

--SPP_LOGIN_LABEL
--  USE JSP IN PORTAL WEB


--SPP_CONTACT_TOPICS

PROMPT INSERTING into WSRP_APPLICATION
insert into WSRP_APPLICATION (ID, DEFINITION_ID) VALUES ('1', 'spp-services-web');
 
PROMPT INSERTING into WSRP_PORTLET
insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) 
select '1.1', '1', 'spp-services-web.Groups', WSRP_APPLICATION.ID from WSRP_APPLICATION  where definition_Id = 'spp-services-web';

PROMPT INSERTING into WSRP_PORTLET
insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION)
select '1.13', '13', 'spp-services-web.EServicesMgt', WSRP_APPLICATION.ID from WSRP_APPLICATION where definition_Id = 'spp-services-web';
