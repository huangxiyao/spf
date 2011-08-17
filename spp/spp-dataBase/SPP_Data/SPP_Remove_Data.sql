-- SPP Data
delete from SPP_GROUP;
delete from SPP_RESOURCE_HISTORY;
delete from SPP_ESERVICE_PARAMETER;
delete from SPP_ESERVICE;
delete from SPP_STANDARD_PARAMETERSET;
delete from SPP_CONTACT_FAULTS;
delete from SPP_CONTACT_LANGUAGE;
delete from SPP_CONTACT_TOPICS;
delete from SPP_WORKFLOW_ERROR;
delete from SPP_HPURLS;
delete from SPP_CUSTOM_ERROR;
delete from SPP_LOGIN_LABEL;
delete from SPP_SIMULATION_SESSION;
delete from SPP_ERROR_CODE;
delete from SPP_URL_ACCESS_RULE;
delete from SPP_USERSESSION;
delete from SPP_CONFIG_ENTRY;
delete from SPP_SITE;
delete from SPP_LOCALE;
delete from SPP_HEALTHCHECK_SERVER_INFO;

-- WSRP Data
delete from WSRP_PREFERENCE_VALUE;
delete from WSRP_PREFERENCE;
delete from WSRP_PORTLET;
delete from WSRP_CONSUMER_PORTLET;
delete from WSRP_CONSUMER;
delete from WSRP_APPLICATION;

-- VGN Data
delete from USERS where USER_NAME like ('sso_guest%');

commit;