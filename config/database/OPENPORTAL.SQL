----------------------
--Producer DB Schema--
----------------------
CREATE TABLE PRODUCER (
	STATUS NUMBER(1), 
	PRODUCERKEY VARCHAR2(256) PRIMARY KEY, 
	NAMESPACE VARCHAR2(256), 
	INSTANCENAME VARCHAR2(256), 
	REQUIRESREGISTRATION NUMBER(1), 
	SUPPORTSINBANDREGISTRATION NUMBER(1),
	VERSION VARCHAR2(10),
	OFFEREDPORTLETS VARCHAR2(1024), 
	PRODUCERPROFILEMAP VARCHAR2(256), 
	REGISTRATIONPROPERTIES VARCHAR2(256), 
	REGISTRATIONVALIDATORCLASSNAME VARCHAR2(256), 
	PORTALID VARCHAR2(256) 
); 

CREATE TABLE CONSUMER_REGISTRATION ( 
	STATUS NUMBER(1), 
	CONSUMERNAME VARCHAR2(256) , 
	REGISTRATIONHANDLE VARCHAR2(256) PRIMARY KEY, 
	CONSUMERAGENT VARCHAR2(256), 
	METHODGETSUPPORTED NUMBER(1), 
	CONSUMERMODES VARCHAR2(256), 
	CONSUMERWINDOWSTATES VARCHAR2(256), 
	CONSUMERUSERSCOPES VARCHAR2(256), 
	CUSTOMUSERPROFILEDATA VARCHAR2(256), 
	REGISTRATIONPROPERTIES VARCHAR2(256),
	LIFETIMETERMINATIONTIME VARCHAR2(256),	
	PRODUCERKEY VARCHAR2(256)
); 

CREATE TABLE REGISTRATION_PROPERTY (
	PRODUCERKEY VARCHAR2(256), 
	LABELRESOURCENAME VARCHAR2(256), 
	LABELVALUE VARCHAR2(256), 
	LABELLANG VARCHAR2(256), 
	HINTRESOURCENAME VARCHAR2(256), 
	HINTVALUE VARCHAR2(256), 
	HINTLANG VARCHAR2(256), 
	NAME VARCHAR2(256) PRIMARY KEY, 
	TYPE VARCHAR2(256), 
	EXTENSION VARCHAR2(256)
);

CREATE TABLE PRODUCER_GLOBAL_PROFILE_MAP (

	PORTALID VARCHAR2(256) NOT NULL,
	PORTLETATTR VARCHAR2(256) NOT NULL,
	WSRPATTR VARCHAR2(256) NOT NULL,
	CONSTRAINT PROD_GLOBAL_PROFILE_PK PRIMARY KEY (PORTALID, PORTLETATTR)
);


CREATE TABLE PRODUCER_GLOBAL_DATA ( 

    PORTALID VARCHAR(256) NOT NULL, 
    PRODUCERDISABLE NUMBER(1) NOT NULL, 
    CONSTRAINT PRODUCER_GLOBAL_DATA_PK PRIMARY KEY (PORTALID) 
);


----------------------
--Consumer DB Schema--
----------------------
CREATE TABLE CONFIGURED_PRODUCERS ( 
	PORTAL_ID VARCHAR2(256) NOT NULL, 
	CONFIGURED_PRODUCER_ID VARCHAR2(256) NOT NULL, 
	CONFIGURED_PRODUCER_NAME VARCHAR2(256) NOT NULL, 
	NAMESPACE VARCHAR2(256), 
	PRODUCER_URL VARCHAR2(256) NOT NULL,
	PRODUCER_VERSION VARCHAR2(10) NOT NULL,
	PRODUCER_MARKUP_ENDPOINT VARCHAR2(256) NOT NULL, 
	PRODUCER_STATUS SMALLINT, 
	REGISTRATION_DATA VARCHAR2(4000), 
	REGISTRATION_CONTEXT VARCHAR2(4000), 
	SERVICE_DESCRIPTION CLOB, 
	USER_CATEGORY_MAPPING CLOB, 
	CUSTOM_USER_PROFILE CLOB, 
	SD_LASTMODIFIED NUMBER(20), 
	IDENTITY_PROPAGATION_TYPE VARCHAR2(256), 
	ENTITY_VERSION NUMBER(5), 
	CONSTRAINT CONFIG_PRODUCERS_PK PRIMARY KEY (PORTAL_ID, CONFIGURED_PRODUCER_ID)
); 

CREATE TABLE CONSUMER_GLOBAL_DATA ( 
	PORTAL_ID VARCHAR2(256) PRIMARY KEY, 
	CONSUMER_ENABLE NUMBER(1) NOT NULL, 
	DEFAULT_REGIS_DATA VARCHAR2(4000)
); 

CREATE TABLE CONSUMER_NAME ( 
	PORTAL_ID VARCHAR(256) NOT NULL, 
	NAMESPACE VARCHAR(256) NOT NULL, 
	CONSUMER_NAME VARCHAR(256) NOT NULL, 
	CONSTRAINT CONSUMER_NAME_PK PRIMARY KEY (PORTAL_ID, NAMESPACE) 
); 

CREATE TABLE CONSUMER_DEF_USER_PROFILE_MAP ( 
	PORTAL_ID VARCHAR2(256) NOT NULL, 
	PROFILE_KEY VARCHAR2(256) NOT NULL, 
	PROFILE_VALUE VARCHAR2(256) NOT NULL, 
	CONSTRAINT CONS_USER_PROFILE_PK PRIMARY KEY (PORTAL_ID, PROFILE_KEY) 
); 

-------------------------------
--Portlet Container DB Schema--
-------------------------------
CREATE TABLE PORTLET_WINDOW (ID NUMBER(19) NOT NULL, NAME VARCHAR2(255) UNIQUE NOT NULL, LANG VARCHAR2(255) NULL, PORTLETNAME VARCHAR2(255) NOT NULL, REMOTE VARCHAR2(255) NOT NULL, WIDTH VARCHAR2(255) NULL, TITLE VARCHAR2(255) NULL, VISIBLE VARCHAR2(255) NULL, ENTITYIDPREFIX VARCHAR2(255) NULL, PORTLETHANDLE VARCHAR2(255) NULL, CONSUMERID VARCHAR2(255) NULL, PRODUCERENTITYID VARCHAR2(255) NULL, PORTLETID VARCHAR2(255) NULL, PRIMARY KEY (ID))
CREATE TABLE PORTLET_DEPLOYMENT_DESCRIPTOR (DESCRIPTORNAME VARCHAR2(255) NOT NULL, CONTENT BLOB NULL, PRIMARY KEY (DESCRIPTORNAME))
CREATE TABLE PORTLET_APP (ID NUMBER(19) NOT NULL, NAME VARCHAR2(255) NOT NULL, DESCRIPTION VARCHAR2(255) NULL, PORTLETNAME VARCHAR2(255) UNIQUE NOT NULL, TITLE VARCHAR2(255) NULL, ARCHIVENAME VARCHAR2(255) NULL, ARCHIVETYPE VARCHAR2(255) NULL, SHORTTITLE VARCHAR2(255) NULL, TRANSPORTGUARANTEE VARCHAR2(255) NULL, PRIMARY KEY (ID))
CREATE TABLE PORTLET_APP_PROPERTIES (ID NUMBER(19) NOT NULL, PROPERTYNAME VARCHAR2(255) NOT NULL, SUBELEMENTNAME VARCHAR2(255) NOT NULL, SUBELEMENTVALUE VARCHAR2(255) NULL, PORTLET_APP_ID NUMBER(19) NOT NULL, PRIMARY KEY (ID))
CREATE TABLE PORTLET_USER_WINDOW_PREFERENCE (ID NUMBER(19) NOT NULL, TYPE VARCHAR2(255) NULL, PREFERENCENAME VARCHAR2(255) NULL, PREFERENCEVALUE VARCHAR2(2048) NULL, PORTLET_USER_WINDOW_ID NUMBER(19) NOT NULL, PRIMARY KEY (ID))
CREATE TABLE PORTLET_USER_WINDOW (ID NUMBER(19) NOT NULL, USERNAME VARCHAR2(255) NULL, PORTLETNAME VARCHAR2(255) NOT NULL, WINDOWNAME VARCHAR2(255) NOT NULL, PRIMARY KEY (ID))
ALTER TABLE PORTLET_APP_PROPERTIES ADD CONSTRAINT UNQ_PORTLET_APP_PROPERTIES_0 UNIQUE (PORTLET_APP_ID, PROPERTYNAME, SUBELEMENTNAME, SUBELEMENTVALUE)
ALTER TABLE PORTLET_USER_WINDOW_PREFERENCE ADD CONSTRAINT PORTLETUSER_WINDOW_PREFERENCE0 UNIQUE (TYPE, PREFERENCENAME, PORTLET_USER_WINDOW_ID)
ALTER TABLE PORTLET_USER_WINDOW ADD CONSTRAINT UNQ_PORTLET_USER_WINDOW_0 UNIQUE (WINDOWNAME, USERNAME)
ALTER TABLE PORTLET_APP_PROPERTIES ADD CONSTRAINT PRTLETAPPPROPERTIESPRTLETAPPID FOREIGN KEY (PORTLET_APP_ID) REFERENCES PORTLET_APP (ID)
ALTER TABLE PORTLET_USER_WINDOW_PREFERENCE ADD CONSTRAINT PRTLTSRWNDWPRFRNCEPRTLTSRWNDWD FOREIGN KEY (PORTLET_USER_WINDOW_ID) REFERENCES PORTLET_USER_WINDOW (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR2(50) NOT NULL, SEQ_COUNT NUMBER(38) NULL, PRIMARY KEY (SEQ_NAME))


----------------------------------
--Portlet Container Default Data--
----------------------------------
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletUserWindowID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletAppPropertiesID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletUserWindowPreferenceID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletAppID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletWindowID', 0)

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
VALUES('portal1', 'homeInfo/telecom/mobile/number', 'mobile'); 

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
VALUES('portal1', 'homeInfo/telecom/fax/number', 'fax'); 

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
VALUES('portal1', 'homeInfo/telecom/pager/number', 'pager'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'employerInfo/department', 'departmentNumber'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/postalcode', 'postalCode'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/mobile/number', 'mobile'); >

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'employerInfo/jobtitle', 'title'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/street', 'street'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/telephone/number', 'homePhone'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/fax/number', 'fax'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/pager/number', 'pager'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/telephone/number', 'telephoneNumber'); 

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
    ('portal1', 'user.home-info.telecom.telephone.number', 'homeInfo/telecom/telephone/number');
    
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
    ('portal1', 'user.home-info.telecom.fax.number', 'homeInfo/telecom/fax/number');
    
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
    ('portal1', 'user.home-info.telecom.mobile.number', 'homeInfo/telecom/mobile/number');
    
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
    ('portal1', 'user.home-info.telecom.pager.number', 'homeInfo/telecom/pager/number');
    
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
    ('portal1', 'user.business-info.telecom.telephone.number', 'businessInfo/telecom/telephone/number');
    
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
    ('portal1', 'user.business-info.telecom.fax.number', 'businessInfo/telecom/fax/number');
    
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
    ('portal1', 'user.business-info.telecom.mobile.number', 'businessInfo/telecom/mobile/number');
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
    ('portal1', 'user.business-info.telecom.pager.number', 'businessInfo/telecom/pager/number');
    
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


