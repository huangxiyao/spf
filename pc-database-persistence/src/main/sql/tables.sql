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
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletUserWindowID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletAppPropertiesID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletUserWindowPreferenceID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletAppID', 0)
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('PortletWindowID', 0)
