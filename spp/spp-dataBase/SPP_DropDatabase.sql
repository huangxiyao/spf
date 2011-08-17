-- WSRP Tables
drop table WSRP_APPLICATION cascade constraints;
drop table WSRP_CONSUMER cascade constraints;
drop table WSRP_CONSUMER_PORTLET cascade constraints;
drop table WSRP_PORTLET cascade constraints;
drop table WSRP_PREFERENCE cascade constraints;
drop table WSRP_PREFERENCE_VALUE cascade constraints;

-- SPP Tables
drop table SPP_SITE cascade constraints;
drop table SPP_GROUP cascade constraints;
drop table SPP_STANDARD_PARAMETERSET cascade constraints;    
drop table SPP_ESERVICE cascade constraints;
drop table SPP_ESERVICE_PARAMETER cascade constraints;
drop table SPP_RESOURCE_HISTORY cascade constraints;
drop table SPP_CONFIG_ENTRY cascade constraints;
drop table SPP_URL_ACCESS_RULE cascade constraints;
drop table SPP_ERROR_CODE cascade constraints;
drop table SPP_WORKFLOW_ERROR cascade constraints;
drop table SPP_CUSTOM_ERROR cascade constraints;
drop table SPP_HPURLS cascade constraints;
drop table SPP_LOGIN_LABEL cascade constraints;
drop table SPP_USERSESSION cascade constraints;
drop table SPP_CONTACT_TOPICS cascade constraints;
drop table SPP_CONTACT_FAULTS cascade constraints;
drop table SPP_CONTACT_LANGUAGE cascade constraints;
drop table SPP_SIMULATION_SESSION cascade constraints;
drop table SPP_LOCALE cascade constraints;
drop table SPP_WSRP_DISABLED_SERVER cascade constraints;
drop table SPP_HEALTHCHECK_SERVER_INFO cascade constraints;

-- Sequences
drop sequence SPP_SITE_ID_SEQ;
drop sequence SPP_GROUP_ID_SEQ;
drop sequence SPP_ESERVICE_PARAMETER_ID_SEQ;
drop sequence SPP_STANDARD_PARAMETERSET_SEQ;
drop sequence SPP_ESERVICE_ID_SEQ;
drop sequence SPP_RESOURCE_HISTORY_ID_SEQ;
