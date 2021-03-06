----------------------
--Producer DB Schema--
----------------------
CREATE TABLE PRODUCER (
	STATUS NUMERIC(1), 
	PRODUCERKEY VARCHAR(256) PRIMARY KEY, 
	NAMESPACE VARCHAR(256), 
	INSTANCENAME VARCHAR(256), 
	REQUIRESREGISTRATION NUMERIC(1), 
	SUPPORTSINBANDREGISTRATION NUMERIC(1),
	VERSION VARCHAR(10),
	OFFEREDPORTLETS VARCHAR(4000), 
	PRODUCERPROFILEMAP VARCHAR(256), 
	REGISTRATIONPROPERTIES VARCHAR(256), 
	REGISTRATIONVALIDATORCLASSNAME VARCHAR(256), 
	PORTALID VARCHAR(256) 
); 

CREATE TABLE CONSUMER_REGISTRATION ( 
	STATUS NUMERIC(1), 
	CONSUMERNAME VARCHAR(256) , 
	REGISTRATIONHANDLE VARCHAR(256) PRIMARY KEY, 
	CONSUMERAGENT VARCHAR(256), 
	METHODGETSUPPORTED NUMERIC(1), 
	CONSUMERMODES VARCHAR(256), 
	CONSUMERWINDOWSTATES VARCHAR(256), 
	CONSUMERUSERSCOPES VARCHAR(256), 
	CUSTOMUSERPROFILEDATA VARCHAR(256), 
	REGISTRATIONPROPERTIES VARCHAR(256),
	LIFETIMETERMINATIONTIME VARCHAR(256),	
	PRODUCERKEY VARCHAR(256)
); 

CREATE TABLE REGISTRATION_PROPERTY (
	PRODUCERKEY VARCHAR(256), 
	LABELRESOURCENAME VARCHAR(256), 
	LABELVALUE VARCHAR(256), 
	LABELLANG VARCHAR(256), 
	HINTRESOURCENAME VARCHAR(256), 
	HINTVALUE VARCHAR(256), 
	HINTLANG VARCHAR(256), 
	NAME VARCHAR(256) PRIMARY KEY, 
	TYPE VARCHAR(256), 
	EXTENSION VARCHAR(256)
);

CREATE TABLE PRODUCER_GLOBAL_PROFILE_MAP (

	PORTALID VARCHAR(256) NOT NULL,
	PORTLETATTR VARCHAR(256) NOT NULL,
	WSRPATTR VARCHAR(256) NOT NULL,
	CONSTRAINT PROD_GLOBAL_PROFILE_PK PRIMARY KEY (PORTALID, PORTLETATTR)
);


CREATE TABLE PRODUCER_GLOBAL_DATA ( 

    PORTALID VARCHAR(256) NOT NULL, 
    PRODUCERDISABLE NUMERIC(1) NOT NULL, 
    CONSTRAINT PRODUCER_GLOBAL_DATA_PK PRIMARY KEY (PORTALID) 
);


----------------------
--Consumer DB Schema--
----------------------
CREATE TABLE CONFIGURED_PRODUCERS ( 
	PORTAL_ID VARCHAR(256) NOT NULL, 
	CONFIGURED_PRODUCER_ID VARCHAR(256) NOT NULL, 
	CONFIGURED_PRODUCER_NAME VARCHAR(256) NOT NULL, 
	NAMESPACE VARCHAR(256), 
	PRODUCER_URL VARCHAR(256) NOT NULL,
	PRODUCER_VERSION VARCHAR(10) NOT NULL,
	PRODUCER_MARKUP_ENDPOINT VARCHAR(256) NOT NULL, 
	PRODUCER_STATUS SMALLINT, 
	REGISTRATION_DATA VARCHAR(4000), 
	REGISTRATION_CONTEXT VARCHAR(4000), 
	SERVICE_DESCRIPTION TEXT, 
	USER_CATEGORY_MAPPING TEXT, 
	CUSTOM_USER_PROFILE TEXT, 
	SD_LASTMODIFIED NUMERIC(20), 
	IDENTITY_PROPAGATION_TYPE VARCHAR(256), 
	ENTITY_VERSION NUMERIC(5), 
	CONSTRAINT CONFIG_PRODUCERS_PK PRIMARY KEY (PORTAL_ID, CONFIGURED_PRODUCER_ID)
); 

CREATE TABLE CONSUMER_GLOBAL_DATA ( 
	PORTAL_ID VARCHAR(256) PRIMARY KEY, 
	CONSUMER_ENABLE NUMERIC(1) NOT NULL, 
	DEFAULT_REGIS_DATA VARCHAR(4000)
); 

CREATE TABLE CONSUMER_NAME ( 
	PORTAL_ID VARCHAR(256) NOT NULL, 
	NAMESPACE VARCHAR(256) NOT NULL, 
	CONSUMER_NAME VARCHAR(256) NOT NULL, 
	CONSTRAINT CONSUMER_NAME_PK PRIMARY KEY (PORTAL_ID, NAMESPACE) 
); 

CREATE TABLE CONSUMER_DEF_USER_PROFILE_MAP ( 
	PORTAL_ID VARCHAR(256) NOT NULL, 
	PROFILE_KEY VARCHAR(256) NOT NULL, 
	PROFILE_VALUE VARCHAR(256) NOT NULL, 
	CONSTRAINT CONS_USER_PROFILE_PK PRIMARY KEY (PORTAL_ID, PROFILE_KEY) 
); 

-------------------------------
--Portlet Container DB Schema--
-------------------------------
CREATE TABLE PORTLET_WINDOW (ID NUMERIC(19) NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, LANG VARCHAR(255) NULL, PORTLETNAME VARCHAR(255) NOT NULL, REMOTE VARCHAR(255) NOT NULL, WIDTH VARCHAR(255) NULL, TITLE VARCHAR(255) NULL, VISIBLE VARCHAR(255) NULL, ENTITYIDPREFIX VARCHAR(255) NULL, PORTLETHANDLE VARCHAR(255) NULL, CONSUMERID VARCHAR(255) NULL, PRODUCERENTITYID VARCHAR(255) NULL, PORTLETID VARCHAR(255) NULL, PRIMARY KEY (ID));
CREATE TABLE PORTLET_DEPLOYMENT_DESCRIPTOR (DESCRIPTORNAME VARCHAR(255) NOT NULL, CONTENT BLOB NULL, PRIMARY KEY (DESCRIPTORNAME));
CREATE TABLE PORTLET_APP (ID NUMERIC(19) NOT NULL, NAME VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(255) NULL, PORTLETNAME VARCHAR(255) UNIQUE NOT NULL, TITLE VARCHAR(255) NULL, ARCHIVENAME VARCHAR(255) NULL, ARCHIVETYPE VARCHAR(255) NULL, SHORTTITLE VARCHAR(255) NULL, TRANSPORTGUARANTEE VARCHAR(255) NULL, PRIMARY KEY (ID));
CREATE TABLE PORTLET_APP_PROPERTIES (ID NUMERIC(19) NOT NULL, PROPERTYNAME VARCHAR(255) NOT NULL, SUBELEMENTNAME VARCHAR(255) NOT NULL, SUBELEMENTVALUE VARCHAR(255) NULL, PORTLET_APP_ID NUMERIC(19) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE PORTLET_USER_WINDOW_PREFERENCE (ID NUMERIC(19) NOT NULL, TYPE VARCHAR(255) NULL, PREFERENCENAME VARCHAR(255) NULL, PREFERENCEVALUE VARCHAR(2048) NULL, PORTLET_USER_WINDOW_ID NUMERIC(19) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE PORTLET_USER_WINDOW (ID NUMERIC(19) NOT NULL, USERNAME VARCHAR(255) NULL, PORTLETNAME VARCHAR(255) NOT NULL, WINDOWNAME VARCHAR(255) NOT NULL, PRIMARY KEY (ID));
ALTER TABLE PORTLET_APP_PROPERTIES ADD CONSTRAINT UNQ_PORTLET_APP_PROPERTIES_0 UNIQUE (PORTLET_APP_ID, PROPERTYNAME, SUBELEMENTNAME, SUBELEMENTVALUE);
ALTER TABLE PORTLET_USER_WINDOW_PREFERENCE ADD CONSTRAINT PORTLETUSER_WINDOW_PREFERENCE0 UNIQUE (TYPE, PREFERENCENAME, PORTLET_USER_WINDOW_ID);
ALTER TABLE PORTLET_USER_WINDOW ADD CONSTRAINT UNQ_PORTLET_USER_WINDOW_0 UNIQUE (WINDOWNAME, USERNAME);
ALTER TABLE PORTLET_APP_PROPERTIES ADD CONSTRAINT PRTLETAPPPROPERTIESPRTLETAPPID FOREIGN KEY (PORTLET_APP_ID) REFERENCES PORTLET_APP (ID);
ALTER TABLE PORTLET_USER_WINDOW_PREFERENCE ADD CONSTRAINT PRTLTSRWNDWPRFRNCEPRTLTSRWNDWD FOREIGN KEY (PORTLET_USER_WINDOW_ID) REFERENCES PORTLET_USER_WINDOW (ID);
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMERIC(38) NULL, PRIMARY KEY (SEQ_NAME));


----------------------------------
--Portlet Container Default Data--
----------------------------------
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletUserWindowID', 0);
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletAppPropertiesID', 0);
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletUserWindowPreferenceID', 0);
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletAppID', 0);
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletWindowID', 0);

----------------------------------
--WSRP Default Data---------------
----------------------------------
INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/suffix', 'generationQualifier'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/country', 'c'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/stateprov', 'st'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/online/email', 'mail'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/name', 'cn'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/family', 'sn'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/mobile/NUMERIC', 'mobile'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/online/email', 'mail'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/prefix', 'personalTitle'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/online/uri', 'labeledURI'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/street', 'street'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/country', 'c'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/fax/NUMERIC', 'fax'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/stateprov', 'st'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/postalcode', 'postalCode'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/online/uri', 'labeledURI'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/organization', 'o'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/city', '1'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/city', 'c'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/given', 'givenName'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/name', 'cn'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/pager/NUMERIC', 'pager'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'employerInfo/department', 'departmentNUMERIC'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/postalcode', 'postalCode'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/mobile/NUMERIC', 'mobile');

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'employerInfo/jobtitle', 'title'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/street', 'street'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/telephone/NUMERIC', 'homePhone'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/fax/NUMERIC', 'fax'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/pager/NUMERIC', 'pager'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/telephone/NUMERIC', 'telephoneNUMERIC'); 

INSERT INTO CONSUMER_GLOBAL_DATA(PORTAL_ID, CONSUMER_ENABLE) VALUES('portal1', 1); 

INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.bdate', 'bdate');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.gender', 'gender');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.prefix', 'name/prefix');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.given', 'name/given');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.family', 'name/family');
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.middle', 'name/middle');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.nickName', 'name/nickName');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.employer', 'employerInfo/employer');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.department', 'employerInfo/department');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.jobtitle', 'employerInfo/jobtitle');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.name', 'homeInfo/postal/name');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.street', 'homeInfo/postal/street');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.city', 'homeInfo/postal/city');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.stateprov', 'homeInfo/postal/stateprov');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.postalcode', 'homeInfo/postal/postalcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.country', 'homeInfo/postal/country');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.organization', 'homeInfo/postal/organization');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.intcode', 'homeInfo/telecom/telephone/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.loccode', 'homeInfo/telecom/telephone/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.NUMERIC', 'homeInfo/telecom/telephone/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.ext', 'homeInfo/telecom/telephone/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.comment', 'homeInfo/telecom/telephone/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.intcode', 'homeInfo/telecom/fax/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.loccode', 'homeInfo/telecom/fax/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.NUMERIC', 'homeInfo/telecom/fax/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.ext', 'homeInfo/telecom/fax/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.comment', 'homeInfo/telecom/fax/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.intcode', 'homeInfo/telecom/mobile/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.loccode', 'homeInfo/telecom/mobile/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.NUMERIC', 'homeInfo/telecom/mobile/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.ext', 'homeInfo/telecom/mobile/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.comment', 'homeInfo/telecom/mobile/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.intcode', 'homeInfo/telecom/pager/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.loccode', 'homeInfo/telecom/pager/loccode');
    
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.NUMERIC', 'homeInfo/telecom/pager/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.ext', 'homeInfo/telecom/pager/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.comment', 'homeInfo/telecom/pager/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.online.email', 'homeInfo/online/email');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.online.uri', 'homeInfo/online/uri');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.name', 'businessInfo/postal/name');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.street', 'businessInfo/postal/street');
    
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.city', 'businessInfo/postal/city');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.stateprov', 'businessInfo/postal/stateprov');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.postalcode', 'businessInfo/postal/postalcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.country', 'businessInfo/postal/country');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.organization', 'businessInfo/postal/organization');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.intcode', 'businessInfo/telecom/telephone/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.loccode', 'businessInfo/telecom/telephone/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.NUMERIC', 'businessInfo/telecom/telephone/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.ext', 'businessInfo/telecom/telephone/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.comment', 'businessInfo/telecom/telephone/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.intcode', 'businessInfo/telecom/fax/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.loccode', 'businessInfo/telecom/fax/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.NUMERIC', 'businessInfo/telecom/fax/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.ext', 'businessInfo/telecom/fax/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.comment', 'businessInfo/telecom/fax/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.intcode', 'businessInfo/telecom/mobile/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.loccode', 'businessInfo/telecom/mobile/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.NUMERIC', 'businessInfo/telecom/mobile/NUMERIC');
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.ext', 'businessInfo/telecom/mobile/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.comment', 'businessInfo/telecom/mobile/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.intcode', 'businessInfo/telecom/pager/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.loccode', 'businessInfo/telecom/pager/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.NUMERIC', 'businessInfo/telecom/pager/NUMERIC');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.ext', 'businessInfo/telecom/pager/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.comment', 'businessInfo/telecom/pager/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.online.email', 'businessInfo/online/email');

INSERT INTO PRODUCER_GLOBAL_DATA (PORTALID, PRODUCERDISABLE) VALUES ('portal1', 0);


