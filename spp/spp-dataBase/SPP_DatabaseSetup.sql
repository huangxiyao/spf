SPOOL SPP_DatabaseSetup.log

Prompt *********************************
Prompt  CREATION OF TABLES
Prompt *********************************

@./SPP_Tables/SPP_SITE.sql
@./SPP_Tables/SPP_GROUP.sql
@./SPP_Tables/SPP_STANDARD_PARAMETERSET.sql
@./SPP_Tables/SPP_ESERVICE.sql
@./SPP_Tables/SPP_ESERVICE_PARAMETER.sql
@./SPP_Tables/SPP_RESOURCE_HISTORY.sql
@./SPP_Tables/SPP_CONFIG_ENTRY.sql
@./SPP_Tables/SPP_URL_ACCESS_RULE.sql
@./SPP_Tables/SPP_ERROR_CODE.sql
@./SPP_Tables/SPP_WORKFLOW_ERROR.sql
@./SPP_Tables/SPP_CUSTOM_ERROR.sql
@./SPP_Tables/SPP_HPURLS.sql
@./SPP_Tables/SPP_LOGIN_LABEL.sql
@./SPP_Tables/SPP_LOGIN_RULE.sql
@./SPP_Tables/SPP_USERSESSION.sql
@./SPP_Tables/SPP_CONTACT_TOPICS.sql
@./SPP_Tables/SPP_CONTACT_FAULTS.sql
@./SPP_Tables/SPP_CONTACT_LANGUAGE.sql
@./SPP_Tables/SPP_SIMULATION_SESSIONS.sql
@./SPP_Tables/SPP_HPPGROUP.sql
@./SPP_Tables/SPP_LOCALE.sql
@./SPP_Tables/SPP_SITE_SETTING.sql
@./SPP_Tables/SPP_WSRP_DISABLED_SERVER.sql
@./SPP_Tables/SPP_HEALTHCHECK_SERVER_INFO.sql

@./WSRP_Tables/APPLICATION.sql
@./WSRP_Tables/CONSUMER.sql
@./WSRP_Tables/CONSUMER_PORTLET.sql
@./WSRP_Tables/PORTLET.sql
@./WSRP_Tables/PREFERENCE.sql
@./WSRP_Tables/PREFERENCE_VALUE.sql



Prompt *********************************
Prompt  CREATION OF CONSTRAINT
Prompt *********************************

@./SPP_Tables/DB_CONSTRAINT.sql
@./WSRP_Tables/DB_CONSTRAINT.sql


Prompt *********************************
Prompt  CREATION OF SEQUENCE
Prompt *********************************

@./SPP_Tables/DB_SEQUENCE.sql

Prompt *********************************
Prompt  CREATION OF VIGNETTE Objects
Prompt *********************************

@./VGN_Tables/VGN_modify_USERS.sql



SPOOL OFF;
 

    

    

  
