PROMPT ***************************************************************************
PROMPT Vignette OBJECTS
PROMPT ***************************************************************************

create table DBRDFORUM (
 FORUMUID varchar2(192) not null,
 MODULEUID varchar2(192) not null,
 TITLE varchar2(765) null,
 DESCRIPTION varchar2(765) null,
 CREATIONDATE decimal(16) null,
 MOSTRECENTPOSTDATE decimal(16) null,
 TOPICCOUNT decimal(22) default 0 null,
 MESSAGECOUNT decimal(22) default 0 null,
 constraint DBRDFORUM_PK primary key(FORUMUID)
)
/

create table DBRDUSERSNAPSHOT (
 USERID varchar2(192) not null,
 DISPLAYNAME varchar2(765) null,
 EMAILADDRESS varchar2(765) null,
 constraint DBRDUSERSNAPSHOT_PK primary key(USERID)
)
/

create table DBRDTOPIC (
 TOPICUID varchar2(192) not null,
 FORUMUID varchar2(192) null,
 MESSAGEUID varchar2(192) null,
 RECURSIVEREPLYCOUNT decimal(22) default 0 null,
 MOSTRECENTPOSTDATE decimal(16) null,
 USERSNAPSHOTUID varchar2(192) null,
 SUBJECT varchar2(765) null,
 constraint DBRDTOPIC_PK primary key(TOPICUID)
)
/

create table DBRDTOPICLASTVISITED (
 TOPICUID varchar2(192) not null,
 USERID varchar2(192) not null,
 LASTVISITEDDATE decimal(16) null,
 constraint DBRDTOPICLASTVISITED_PK primary key(TOPICUID,USERID)
)
/

create table DBRDMESSAGE (
 MESSAGEUID varchar2(192) not null,
 TOPICUID varchar2(192) null,
 USERSNAPSHOTUID varchar2(192) null,
 SUBJECT varchar2(765) null,
 ANONYMOUS decimal(22) default 0 not null,
 HIDEEMAIL decimal(22) default 0 not null,
 POSTEDDATE decimal(16) not null,
 BODY clob null,
 constraint DBRDMESSAGE_PK primary key(MESSAGEUID)
)
/

create table DBRDMESSAGEHIERARCHY (
 MESSAGEHIERARCHYUID varchar2(192) not null,
 PARENTMESSAGEUID varchar2(192) null,
 CHILDMESSAGEUID varchar2(192) null,
 constraint DBRDMESSAGEHIERARCHY_PK primary key(MESSAGEHIERARCHYUID)
)
/

create table DBRDDOCUMENTLINK (
 DOCUMENTLINKUID varchar2(192) not null,
 MESSAGEUID varchar2(192) null,
 MIDDLEWAREPATH varchar2(765) null,
 constraint DBRDDOCUMENTLINK_PK primary key(DOCUMENTLINKUID)
)
/

create table DBRDCAPABILITY (
 CAPABILITYCODEUID varchar2(192) not null,
 CAPABILITYCODEDESCRIPTION varchar2(765) null,
 constraint DBRDCAPABILITY_PK primary key(CAPABILITYCODEUID)
)
/

create table DBRDFORUMCAPABILITY (
 FORUMCAPABILITYUID varchar2(192) not null,
 FORUMUID varchar2(192) null,
 CAPABILITYCODEUID varchar2(192) not null,
 constraint DBRDFORUMCAPABILITY_PK primary key(FORUMCAPABILITYUID)
)
/

create table BROADCASTS (
 ID decimal(22) not null,
 TYPE varchar2(765) not null,
 SENDER_ID varchar2(240) not null,
 INT_ARG decimal(22) null,
 STRING_ARG varchar2(765) null,
 JAVA_ARG long raw null,
 constraint BROADCASTS_PK primary key(ID)
)
/

create sequence BROADCASTS_seq
/

create trigger BROADCASTS_init before insert on BROADCASTS
  for each row
    begin
      select BROADCASTS_seq.nextval into :new.ID from dual;
    end;
/

create table DOCUMENTCONTENTS (
 DOCUMENT_UID varchar2(192) not null,
 DOCUMENT_TYPE varchar2(288) null,
 DOCUMENT_SIZE decimal(22) null,
 CONTENTS blob null,
 constraint DOCCONTENTS_PK primary key(DOCUMENT_UID)
)
/

create table DOCUMENTPROPERTIES (
 DOCUMENT_UID varchar2(192) not null,
 NAME varchar2(765) not null,
 PROPERTY_TYPE decimal(22) null,
 VAL varchar2(765) null,
 LONGVAL blob null,
 INTVAL decimal(22) null,
 DATEVAL date null,
 constraint DOCPROPS_PK primary key(DOCUMENT_UID,NAME)
)
/

create index DOCPROPS_DATEVAL on DOCUMENTPROPERTIES(DATEVAL)
/

create table DOCUMENTS (
 UNIQUEID varchar2(192) not null,
 FOLDER_UID varchar2(192) not null,
 NAME varchar2(765) not null,
 CREATED date not null,
 MODIFIED date null,
 EXPIRES date null,
 CHECK_EXPIRATION char(3) default 0 not null,
 constraint DOCUMENTS_PK primary key(UNIQUEID)
)
/

create unique index DOCS_FOLDERNAME on DOCUMENTS(FOLDER_UID,NAME)
/

create index DOCS_EXPIRES on DOCUMENTS(EXPIRES)
/

create table FOLDERPROPERTIES (
 FOLDER_UID varchar2(192) not null,
 NAME varchar2(765) not null,
 PROPERTY_TYPE decimal(22) null,
 VAL varchar2(765) null,
 LONGVAL blob null,
 INTVAL decimal(22) null,
 DATEVAL date null,
 constraint FOLDERPROPS_PK primary key(FOLDER_UID,NAME)
)
/

create table FOLDERS (
 UNIQUEID varchar2(192) not null,
 PARENT_UID varchar2(192) not null,
 PATH_PREFIX varchar2(765) not null,
 PATH_SIZE decimal(22) not null,
 PATH blob not null,
 PATH_DIGEST varchar2(96) not null,
 constraint FOLDERS_PK primary key(UNIQUEID)
)
/

create index FOLDERS_PAR_UID on FOLDERS(PARENT_UID)
/

create index FOLDERS_PATHPREFIX on FOLDERS(PATH_PREFIX)
/

create unique index FOLDERS_PATHDIGEST on FOLDERS(PATH_DIGEST)
/

create table CHANGESET (
 OBJECT_UID varchar2(192) not null,
 SESSION_ID varchar2(192) not null,
 OLD_PATH_SIZE decimal(22) not null,
 OLD_PATH blob not null,
 OLD_NAME varchar2(765) null,
 EXPIRES date null,
 constraint CHANGESET_PK primary key(OBJECT_UID)
)
/

create index CHANGESET_SESSION on CHANGESET(SESSION_ID)
/

create table PAGES (
 ID varchar2(192) not null,
 TITLE varchar2(450) not null,
 DESCRIPTION varchar2(765) null,
 CREATORID varchar2(300) null,
 CREATEDDATE date not null,
 constraint PAGES_PK primary key(ID)
)
/

create table PAGELAYOUTS (
 PAGEID varchar2(192) not null,
 VERTICALLYORIENTED char(3) null,
 USERELATIVEWIDTHS char(3) null,
 USERELATIVELAYOUTWIDTH char(3) null,
 DEFAULTCELLWIDTH decimal(22) null,
 LASTUPDATETIME date null,
 constraint PAGELAYOUTS_PK primary key(PAGEID)
)
/

create table PLDIVIDERS (
 PAGEID varchar2(192) not null,
 DIVIDERINDEX decimal(22) not null,
 WIDTH decimal(22) null,
 constraint PLDIVIDERS_PK primary key(PAGEID,DIVIDERINDEX)
)
/

create table PLCELLS (
 PAGEID varchar2(192) not null,
 DIVIDERINDEX decimal(22) not null,
 CELLINDEX decimal(22) not null,
 ID varchar2(192) not null,
 NAME varchar2(300) null,
 WIDTH decimal(22) null,
 MAXMODULEWIDTH decimal(22) null,
 constraint PLCELLS_PK primary key(PAGEID,DIVIDERINDEX,CELLINDEX)
)
/

create unique index PLCELLS_ID on PLCELLS(ID)
/

create table PAGEMODULESETS (
 PAGEID varchar2(192) not null,
 PMSID varchar2(192) not null,
 LASTMODULEUPDATE date null,
 LASTLAYOUTUPDATE date null,
 PAGEDISABLED char(3) null,
 PAGELOCKED char(3) null,
 UPDATEUNLOCKEDMODULES char(3) null,
 constraint PAGEMODULESETS_PK primary key(PAGEID,PMSID)
)
/

create table PMSCELLS (
 PAGEID varchar2(192) not null,
 PMSID varchar2(192) not null,
 DIVIDERINDEX decimal(22) not null,
 CELLINDEX decimal(22) not null,
 LOCKED char(3) null,
 constraint PMSCELLS_PK primary key(PAGEID,PMSID,DIVIDERINDEX,CELLINDEX)
)
/

create table PMSMODULEENTRIES (
 PAGEID varchar2(192) not null,
 PMSID varchar2(192) not null,
 DIVIDERINDEX decimal(22) not null,
 CELLINDEX decimal(22) not null,
 MODULEID decimal(22) not null,
 ORDERING decimal(22) not null,
 LOCKED char(3) null,
 PORTLETUID varchar2(192) not null,
 constraint PMSMENTRIES_PK primary key(PAGEID,PMSID,DIVIDERINDEX,CELLINDEX,ORDERING,PORTLETUID)
)
/

create table USERMODULESETS (
 PAGEID varchar2(192) not null,
 USERID varchar2(192) not null,
 LASTMODULEUPDATE date null,
 LASTLAYOUTUPDATE date null,
 constraint USERMODULESETS_PK primary key(PAGEID,USERID)
)
/

create table UMSMODULEENTRIES (
 PAGEID varchar2(192) not null,
 USERID varchar2(192) not null,
 DIVIDERINDEX decimal(22) not null,
 CELLINDEX decimal(22) not null,
 MODULEID decimal(22) not null,
 ORDERING decimal(22) not null,
 MINIMIZED char(3) null,
 PORTLETUID varchar2(192) not null,
 constraint UMSMENTRIES_PK primary key(PAGEID,USERID,DIVIDERINDEX,CELLINDEX,ORDERING,PORTLETUID)
)
/

create table PORTALBEANS (
 ID decimal(22) not null,
 ANOTHER_ID varchar2(180) not null,
 CONTENTS blob not null,
 STRING_ID varchar2(180) not null,
 TITLE varchar2(765) null,
 DESCRIPTION varchar2(765) null,
 DESCRIPTOR_ID varchar2(765) null,
 CATEGORY varchar2(765) null,
 LAST_UPDATE date null,
 LOCK_SERIAL varchar2(42) null,
 CREATED_DATE date null,
 constraint PORTALBEANS_PK primary key(ID)
)
/

create unique index PB_STRING_ID on PORTALBEANS(STRING_ID)
/

create unique index PB_ANOTHER_ID on PORTALBEANS(ANOTHER_ID)
/

create index PB_TITLE_DESC on PORTALBEANS(TITLE,DESCRIPTION)
/

create index PB_CATEGORY on PORTALBEANS(CATEGORY)
/

create index PB_CREATED_DATE on PORTALBEANS(CREATED_DATE)
/

create index PB_DESCRIPTOR_ID on PORTALBEANS(DESCRIPTOR_ID)
/

create table PORTALBEANATTRIBUTES (
 UNIQUE_ID varchar2(180) not null,
 NAME varchar2(765) not null,
 VALUE varchar2(765) null,
 constraint PBATTRIBUTES_PK primary key(UNIQUE_ID,NAME)
)
/

create table TASK (
 TASKID decimal(22) not null,
 SCHEDULERNAME varchar2(150) null,
 LAUNCHSTYLE decimal(22) not null,
 RESULT char(3) default 0 not null,
 SUCCESSCOUNT decimal(22) default 0 not null,
 FAILCOUNT decimal(22) default 0 not null,
 LASTRUNDATE date null,
 NEXTRUNDATE date null,
 HASEXPIRED char(3) default 0 not null,
 DELETEWHENEXPIRED char(3) default 1 not null,
 ANINTERVAL decimal(22) default 0 not null,
 INTERVALUNITS decimal(22) default 0 not null,
 NUMREPETITIONS decimal(22) default 0 not null,
 STARTDATE date null,
 ENDDATE date null,
 DAYSOFWEEK varchar2(75) null,
 DAYSOFMONTH varchar2(300) null,
 MONTHS varchar2(105) null,
 DISPLAYNAME varchar2(150) null,
 constraint TASK_PK primary key(TASKID)
)
/

create table SUBTASK (
 TASKID decimal(22) default 0 not null,
 SUBTASKID decimal(22) default 0 not null,
 LAUNCHCLASS varchar2(360) null,
 LAUNCHMETHOD varchar2(360) null,
 EXTERNALPROG varchar2(750) null,
 ABENDCONTINUE char(3) not null,
 DISPLAYNAME varchar2(150) null,
 SUCCESSCOUNT decimal(22) default 0 not null,
 FAILCOUNT decimal(22) default 0 not null,
 constraint SUBTASK_PK primary key(TASKID,SUBTASKID)
)
/

create table PARAMETER (
 TASKID decimal(22) default 0 not null,
 SUBTASKID decimal(22) default 0 not null,
 SEQNUM decimal(22) default 0 not null,
 PARAMTYPE decimal(22) default 0 not null,
 PARAMVALUE varchar2(750) not null,
 constraint PARAMETER_PK primary key(TASKID,SUBTASKID,SEQNUM)
)
/

create table TEMPLATEARCHIVES (
 TEMPLATEID varchar2(240) not null,
 DATEUPDATED date not null,
 ZIPFILE blob null,
 constraint TEMPLARCHIVES_PK primary key(TEMPLATEID)
)
/

create table TEMPLATES (
 ID varchar2(765) not null,
 TITLE varchar2(765) null,
 DESCRIPTION varchar2(765) null,
 SYNTAXTABLE varchar2(765) null,
 TEMPLATEHEADER varchar2(765) null,
 DATA_HASH blob null,
 OWNER decimal(22) not null,
 CREATED_ON date not null,
 TEMPLATETYPE varchar2(765) null,
 FRIENDLYID varchar2(765) null,
 ALLOWGUESTACCESS char(3) default 0 null,
 constraint TEMPLATES_PK primary key(ID)
)
/

create table STYLEARCHIVES (
 TEMPLATEID varchar2(240) not null,
 STYLEID varchar2(240) not null,
 DATEUPDATED date not null,
 ZIPFILE blob null,
 constraint STYLEARCHIVES_PK primary key(STYLEID)
)
/

create table STYLES (
 ID varchar2(240) not null,
 TEMPLATEID varchar2(240) not null,
 TITLE varchar2(765) null,
 DESCRIPTION varchar2(765) null,
 PRIMARYFILENAME varchar2(765) null,
 RELATIVEPATH varchar2(765) null,
 DATA_HASH blob null,
 OWNER decimal(22) not null,
 CREATED_ON date not null,
 FRIENDLYID varchar2(765) null,
 PROCESSINGTYPE decimal(22) default 0 not null,
 constraint STYLES_PK primary key(ID)
)
/

create table PORTALACTIONS (
 ID varchar2(192) not null,
 TEMPLATESTYLEID varchar2(240) not null,
 ACTIONSGROUP varchar2(192) not null,
 CLASSNAME varchar2(765) null,
 TYPE decimal(22) default 0 null,
 SEQNUM decimal(22) default 0 null,
 constraint PORTALACTIONS_PK primary key(ID,TEMPLATESTYLEID,ACTIONSGROUP)
)
/

create table GROUPRELATIONSHIP (
 CHILD_ID decimal(22) null,
 PARENT_ID decimal(22) null,
 CHILD_UID varchar2(192) null,
 PARENT_UID varchar2(192) null
)
/

create index GROUPREL_SK on GROUPRELATIONSHIP(CHILD_ID,PARENT_ID)
/

create index GROUPREL_PK on GROUPRELATIONSHIP(CHILD_UID,PARENT_UID)
/

create table USERGROUPMEMBERSHIP (
 USER_ID decimal(22) null,
 GROUP_ID decimal(22) null,
 USER_UID varchar2(192) null,
 GROUP_UID varchar2(192) null
)
/

create index USERGROUPMEM_SK on USERGROUPMEMBERSHIP(USER_ID,GROUP_ID)
/

create index USERGROUPMEM_PK on USERGROUPMEMBERSHIP(USER_UID,GROUP_UID)
/

create table USERGROUPS (
 ID decimal(22) not null,
 NAME varchar2(765) not null,
 DESCRIPTION varchar2(765) not null,
 PERMISSIONS blob null,
 OWNER decimal(22) null,
 CREATED_DATE date not null,
 UNIQUE_ID varchar2(192) not null,
 constraint USERGROUPS_PK primary key(ID)
)
/

create unique index USERGROUPS_UNIQUE_ID on USERGROUPS(UNIQUE_ID)
/

create index USERGROUPS_NAME on USERGROUPS(NAME)
/

create index USERGROUPS_DATE on USERGROUPS(CREATED_DATE)
/

create table USERKEYS (
 ID decimal(22) not null,
 UNIQUE_ID varchar2(96) not null,
 USER_ID decimal(22) not null,
 TYPE decimal(22) not null,
 EXPIRES date not null,
 CHECK_EXPIRATION char(3) default 0 not null,
 constraint USERKEYS_PK primary key(ID)
)
/

create sequence USERKEYS_seq
/

create trigger USERKEYS_init before insert on USERKEYS
  for each row
    begin
      select USERKEYS_seq.nextval into :new.ID from dual;
    end;
/

create table USERS (
 USER_ID decimal(22) not null,
 UNIQUE_ID varchar2(192) not null,
 USER_NAME varchar2(192) null,
 PASSWORD varchar2(96) null,
 DOMAIN varchar2(384) null,
 ALT_DOMAIN varchar2(384) null,
 FIRST_NAME varchar2(96) null,
 MIDDLE_NAME varchar2(96) null,
 LAST_NAME varchar2(96) null,
 LANGUAGE varchar2(96) null,
 GENDER varchar2(48) null,
 DOB varchar2(45) null,
 ADDRESS1 varchar2(192) null,
 ADDRESS2 varchar2(192) null,
 CITY varchar2(96) null,
 STATE varchar2(96) null,
 ZIP varchar2(96) null,
 COUNTRY varchar2(96) null,
 EMPLOYER varchar2(96) null,
 EMAIL_ADDRESS varchar2(750) null,
 DAY_PHONE varchar2(96) null,
 NIGHT_PHONE varchar2(96) null,
 MOBILE_PHONE varchar2(96) null,
 FAX varchar2(96) null,
 TIMEZONE decimal(22) null,
 USERTYPE decimal(22) null,
 OWNER decimal(22) null,
 CREATED_DATE date null,
 PRIMARY_GROUP decimal(22) null,
 ENTITY_TYPE varchar2(96) null,
 constraint USERS_PK primary key(USER_ID)
)
/

create unique index USERS_UNIQUE_ID on USERS(UNIQUE_ID)
/

create unique index USERS_USER_NAME on USERS(USER_NAME)
/

create index USERS_LAST_NAME on USERS(LAST_NAME)
/

create index USERS_CREATED_DATE on USERS(CREATED_DATE)
/

create index USERS_EMAIL on USERS(EMAIL_ADDRESS)
/

create table SYSTEMUSERGROUPS (
 ID decimal(22) not null,
 UNIQUE_ID varchar2(192) not null,
 NAME varchar2(765) not null,
 DESCRIPTION varchar2(765) not null,
 CREATED_DATE date not null,
 ENTITY_TYPE varchar2(96) null,
 constraint SYSTEMUG_PK primary key(ID)
)
/

create unique index SYSTEMUG_UNIQUE_ID on SYSTEMUSERGROUPS(UNIQUE_ID)
/

create index SYSTEMUG_NAME on SYSTEMUSERGROUPS(NAME)
/

create index SYSTEMUGS_DATE on SYSTEMUSERGROUPS(CREATED_DATE)
/

create table SYSTEMUSERGROUPMEMBERSHIP (
 USER_ID decimal(22) null,
 GROUP_ID decimal(22) null,
 USER_UID varchar2(192) null,
 GROUP_UID varchar2(192) null
)
/

create index SYSTEMUGM_SK on SYSTEMUSERGROUPMEMBERSHIP(USER_ID,GROUP_ID)
/

create index SYSTEMUGM_PK on SYSTEMUSERGROUPMEMBERSHIP(USER_UID,GROUP_UID)
/

create table SYSTEMUSERGROUPRELATIONSHIP (
 CHILD_ID decimal(22) null,
 PARENT_ID decimal(22) null,
 CHILD_UID varchar2(192) null,
 PARENT_UID varchar2(192) null
)
/

create index SYSTEMUGR_SK on SYSTEMUSERGROUPRELATIONSHIP(CHILD_ID,PARENT_ID)
/

create index SYSTEMUGR_PK on SYSTEMUSERGROUPRELATIONSHIP(CHILD_UID,PARENT_UID)
/

create table AUXILIARYUSERINFO (
 LOGIN varchar2(384) not null,
 LAST_LOGIN date null,
 constraint AUXILIARYUI_PK primary key(LOGIN)
)
/

create table METRICSLOCK (
 REPORTID decimal(22) not null,
 STATUS varchar2(96) null,
 constraint METRICSLOCK_PK primary key(REPORTID)
)
/

create table METRICSLOG (
 REPORTID decimal(22) not null,
 NAMESPACE varchar2(384) not null,
 KEY1ATTRIBUTE varchar2(192) not null,
 KEY1VALUE varchar2(192) not null,
 KEY2ATTRIBUTE varchar2(192) not null,
 KEY2VALUE varchar2(192) not null,
 KEY3ATTRIBUTE varchar2(192) not null,
 KEY3VALUE varchar2(192) not null,
 LASTUPDATE date null,
 ATTRIBUTE varchar2(192) not null,
 NUMBERVALUE decimal(22) null,
 CHARVALUE varchar2(765) null,
 DATEVALUE date null,
 constraint METRICSLOG_PK primary key(REPORTID,NAMESPACE,KEY1ATTRIBUTE,KEY1VALUE,KEY2ATTRIBUTE,KEY2VALUE,KEY3ATTRIBUTE,KEY3VALUE,ATTRIBUTE)
)
/

create table METRICS (
 REPORT_ID decimal(22) not null,
 CLUSTER_ID varchar2(765) null,
 REPORT_DATE date null,
 REPORT_PERIOD decimal(22) null,
 DB varchar2(765) null,
 DB_VER varchar2(765) null,
 AUTH_METHOD varchar2(765) null,
 REGISTERED_USERS decimal(22) null,
 UNIQUE_USERS decimal(22) null,
 PAGES decimal(22) null,
 SITES decimal(22) null,
 USERGROUPS decimal(22) null,
 PARENT_GROUPS decimal(22) null,
 CHILD_GROUPS decimal(22) null,
 MAX_CONCUR_USERS decimal(22) null,
 MIN_CONCUR_USERS decimal(22) null,
 FRONT_PAGE_VIEWS decimal(22) null,
 TOTAL_PAGE_VIEWS decimal(22) null,
 constraint METRICS_PK primary key(REPORT_ID)
)
/

create table INSTALLATIONUSAGE (
 REPORT_ID decimal(22) not null,
 SERIAL_NUMBER varchar2(96) not null,
 VERSION varchar2(765) null,
 INSTALL_DATE date null,
 UPGRADE_DATE date null,
 LICENSE_STATUS varchar2(765) null,
 OS varchar2(765) null,
 OS_VER varchar2(765) null,
 SERVLET_ENGINE varchar2(765) null,
 JVM varchar2(765) null,
 JVM_VER varchar2(765) null,
 JDBC varchar2(765) null,
 JDBC_VER varchar2(765) null,
 constraint INSTUSAGE_PK primary key(REPORT_ID,SERIAL_NUMBER)
)
/

create table SERVERUSAGE (
 REPORT_ID decimal(22) not null,
 SERIAL_NUMBER varchar2(96) not null,
 SERVER_ID varchar2(192) not null,
 JVM_TOTAL_MEM decimal(22) null,
 JVM_FREE_MEM decimal(22) null,
 CONCUR_USERS decimal(22) null,
 MAX_CONCUR_USERS decimal(22) null,
 MIN_CONCUR_USERS decimal(22) null,
 FRONT_PAGE_VIEWS decimal(22) null,
 TOTAL_PAGE_VIEWS decimal(22) null,
 constraint SERVERUSAGE_PK primary key(REPORT_ID,SERIAL_NUMBER,SERVER_ID)
)
/

create table DOMAINUSAGE (
 REPORT_ID decimal(22) not null,
 DOMAIN varchar2(384) not null,
 FRONT_PAGE_VIEWS decimal(22) null,
 TOTAL_PAGE_VIEWS decimal(22) null,
 constraint DOMAINUSAGE_PK primary key(REPORT_ID,DOMAIN)
)
/

create table MODULEUSAGE (
 REPORT_ID decimal(22) not null,
 DOMAIN varchar2(384) not null,
 MODULE_ID varchar2(384) not null,
 MODULE_NAME varchar2(384) not null,
 REQUIRED decimal(22) null,
 DEFAULT_LAYOUT decimal(22) null,
 USERS decimal(22) null,
 VIEWS decimal(22) null,
 constraint MODULEUSAGE_PK primary key(REPORT_ID,DOMAIN,MODULE_ID,MODULE_NAME)
)
/

create table WEBSERVICEUSAGE (
 REPORT_ID decimal(22) not null,
 DOMAIN varchar2(384) not null,
 SERVICE varchar2(384) not null,
 SUB_LEVEL varchar2(96) not null,
 DEFAULT_LEVEL varchar2(96) null,
 VIEWS decimal(22) null,
 constraint WSUSAGE_PK primary key(REPORT_ID,DOMAIN,SERVICE,SUB_LEVEL)
)
/

create table USERGROUPCONFIG (
 REPORT_ID decimal(22) not null,
 GROUP_ID varchar2(384) not null,
 GROUP_NAME varchar2(765) null,
 USERS decimal(22) null,
 constraint UGCONFIG_PK primary key(REPORT_ID,GROUP_ID)
)
/

create table TEMPLATECONFIG (
 REPORT_ID decimal(22) not null,
 TEMPLATE_ID varchar2(384) not null,
 constraint TEMPLATECONFIG_PK primary key(REPORT_ID,TEMPLATE_ID)
)
/

create table STYLECONFIG (
 REPORT_ID decimal(22) not null,
 TEMPLATE_ID varchar2(384) not null,
 STYLE_ID varchar2(384) not null,
 constraint STYLECONFIG_PK primary key(REPORT_ID,TEMPLATE_ID,STYLE_ID)
)
/

create table PORTLETUSAGE (
 REPORT_ID decimal(22) not null,
 PORTLETUID varchar2(384) not null,
 TITLE varchar2(765) not null,
 VIEWS decimal(22) null,
 constraint PORTLETUSAGE_PK primary key(REPORT_ID,PORTLETUID)
)
/

create table SETTINGS (
 UNIQUEID varchar2(192) not null,
 TITLE varchar2(765) null,
 DESCRIPTION varchar2(765) null,
 constraint SETTINGS_PK primary key(UNIQUEID)
)
/

create table SETTINGSCONTENTS (
 UNIQUEID varchar2(192) not null,
 SETTINGS_KEY varchar2(765) not null,
 VALUE1_DICTIONARY varchar2(192) not null,
 VALUE2_SMALL varchar2(765) null,
 VALUE2_LARGE blob null,
 constraint SETTINGSCONT_PK primary key(UNIQUEID,SETTINGS_KEY)
)
/

create table SETTINGSDICTIONARY (
 UNIQUEID varchar2(192) not null,
 VALUE_SMALL varchar2(765) null,
 VALUE_LARGE blob null,
 constraint SETTINGSDICT_PK primary key(UNIQUEID)
)
/

create table CATEGORIES (
 UNIQUEID varchar2(192) not null,
 TITLE varchar2(765) not null,
 PARENTUID varchar2(192) null,
 CATEGORY_TYPE varchar2(765) not null,
 DESCRIPTION varchar2(765) null,
 constraint CATEGORIES_PK primary key(UNIQUEID)
)
/

create table CATEGORYMAP (
 CATEGORY varchar2(192) not null,
 CATEGORIZABLE varchar2(192) not null,
 TYPE varchar2(765) null,
 constraint CATEGORYMAP_PK primary key(CATEGORY,CATEGORIZABLE)
)
/

create table SITES (
 UNIQUEID varchar2(192) not null,
 MAIN_REPOSITORY varchar2(192) not null,
 SHARED_REPOSITORY varchar2(192) not null,
 MENU varchar2(192) not null,
 REQUIRED_MENU varchar2(192) null,
 REQUIRED_MENU_POSITION decimal(22) not null,
 ADMIN_GROUP varchar2(192) not null,
 NEW_USERS_GROUP varchar2(192) not null,
 SELF_REG_GROUP varchar2(192) not null,
 SETTINGS varchar2(192) not null,
 TITLE varchar2(765) null,
 DNS_NAME varchar2(765) not null,
 ZOMBIE char(3) default 0 not null,
 DESCRIPTION varchar2(765) null,
 EXCLUSIVE_MENU_MODE char(3) default 0 not null,
 GROUP_PRECEDENCE varchar2(192) not null,
 TYPE varchar2(192) not null,
 CREATED_DATE date not null,
 constraint SITES_PK primary key(UNIQUEID)
)
/

create unique index SITES_DNS_NAME on SITES(DNS_NAME)
/

create table REPOSITORIES (
 UNIQUEID varchar2(192) not null,
 TITLE varchar2(765) null,
 constraint REPOSITORIES_PK primary key(UNIQUEID)
)
/

create table REPOSITORYELEMREF (
 REPOSITORY_ELEMENT varchar2(192) not null,
 SOURCE_REPOSITORY varchar2(192) not null,
 TARGET_REPOSITORY varchar2(192) not null,
 constraint REPELEMREF_PK primary key(REPOSITORY_ELEMENT,SOURCE_REPOSITORY,TARGET_REPOSITORY)
)
/

create table TEAMSPACEROLES (
 TEAMSPACE_UID varchar2(192) not null,
 TEAMSPACE_ROLE varchar2(192) not null,
 GROUP_UID varchar2(192) not null,
 constraint TEAMSPACEROLES_PK primary key(TEAMSPACE_UID,TEAMSPACE_ROLE)
)
/

create table MENUITEM (
 ID varchar2(192) not null,
 STRING_ID varchar2(765) not null,
 TYPE varchar2(765) not null,
 SETTINGS_ID varchar2(192) not null,
 constraint MENUITEM_PK primary key(ID)
)
/

create table MENUITEMASSOCIATION (
 MENU_ID varchar2(192) not null,
 MENU_ITEM_ID varchar2(192) not null,
 MENU_ITEM_POSITION decimal(22) not null,
 MENU_ITEM_TYPE varchar2(765) not null,
 constraint MENUITEMASSN_PK primary key(MENU_ID,MENU_ITEM_ID)
)
/

create table ASSOCIATIONGROUP (
 ASSOCIATION_GROUP_ID varchar2(192) not null,
 TYPE varchar2(765) not null,
 UNIQUE_ID varchar2(765) not null,
 constraint ASSNGROUP_PK primary key(ASSOCIATION_GROUP_ID,TYPE)
)
/

create table ASSOCIATION (
 ASSOCIATION_ID varchar2(192) not null,
 ASSOCIATION_GROUP_ID varchar2(192) not null,
 TYPE varchar2(765) not null,
 ASSOCIATE_ID varchar2(765) null,
 constraint ASSOCIATION_PK primary key(ASSOCIATION_ID,TYPE)
)
/

create table COMPONENTARCHIVES (
 ARCHIVEID varchar2(192) not null,
 LASTMODIFIED date null,
 EVERBEENDEPLOYED char(3) default 0 not null,
 ACTIVITYCUE decimal(22) null,
 CUEDATE date null,
 ACTION decimal(22) default 0 null,
 COMPONENTID varchar2(765) not null,
 COMPONENTTYPE varchar2(384) not null,
 MAJORVERSION decimal(22) not null,
 MINORVERSION decimal(22) not null,
 BUILD varchar2(384) null,
 DESCRIPTOROBJECT blob null,
 constraint ARCHIVE_PK primary key(ARCHIVEID)
)
/

create unique index ARCHIVE_UK on COMPONENTARCHIVES(COMPONENTID,COMPONENTTYPE,MAJORVERSION,MINORVERSION)
/

create table COMPONENTFILES (
 ARCHIVEID varchar2(192) not null,
 NAME varchar2(765) not null,
 TYPE decimal(22) null,
 LASTMODIFIED date null,
 FILEDATE date null,
 FILESIZE decimal(22) null,
 EVERBEENDEPLOYED char(3) default 0 not null,
 CONTENT blob null,
 ACTION decimal(22) default 0 null,
 constraint FILE_PK primary key(ARCHIVEID,NAME)
)
/

create table DEPLOYMENTRECORDS (
 ARCHIVEID varchar2(192) not null,
 SERVERID varchar2(192) not null,
 LASTDEPLOYMENTDATE date null,
 constraint DEPLOYMENT_PK primary key(ARCHIVEID,SERVERID)
)
/

create table REGISTEREDLOCALES (
 LOCALE_ID varchar2(192) not null,
 LANGUAGE_CODE char(6) not null,
 COUNTRY_CODE char(6) null,
 VARIANT varchar2(150) null,
 constraint REGLOCALES_PK_ primary key(LOCALE_ID)
)
/

create unique index REGLOCALES_IX_1 on REGISTEREDLOCALES(LANGUAGE_CODE,COUNTRY_CODE,VARIANT)
/

create table LOCALEASSOCIATION (
 OBJECT_ID varchar2(192) not null,
 LOCALE_ID varchar2(192) not null,
 DEFAULT_LOCALE char(3) default 0 not null,
 constraint LOCALEASSN_PK primary key(OBJECT_ID,LOCALE_ID)
)
/

create table AUTOLOGIN (
 USER_NAME varchar2(192) not null,
 REALM_ID varchar2(384) not null,
 UNIQUE_ID varchar2(192) not null,
 LAST_LOGIN_TIME date not null,
 constraint AUTOLOGIN_PK primary key(UNIQUE_ID)
)
/

create unique index AUTOLOGIN_UR on AUTOLOGIN(USER_NAME,REALM_ID)
/

create table ACCOUNTS (
 USER_NAME varchar2(192) not null,
 PASSWORD varchar2(192) null,
 constraint ACCOUNTS_PK primary key(USER_NAME)
)
/

create table PASSWORD_CONTROLS (
 USER_NAME varchar2(192) not null,
 EXPIRATION date null,
 constraint PASSWORD_CONTROLS_PK primary key(USER_NAME)
)
/

create table AUTHORIZATIONMANAGEMENT (
 PERMISSIONABLE varchar2(192) not null,
 GRANTEE varchar2(192) not null,
 PERMISSION varchar2(765) not null,
 CONTEXT varchar2(192) not null,
 constraint AUTHORIZATION_PK primary key(PERMISSIONABLE,GRANTEE,PERMISSION,CONTEXT)
)
/

create index AUTHORIZATION_CL on AUTHORIZATIONMANAGEMENT(PERMISSIONABLE,CONTEXT)
/

create table PERMISSION (
 OBJECT_ID varchar2(384) not null,
 OBJECT_TYPE varchar2(384) not null,
 PERMISSION_FIELD varchar2(384) null,
 GROUP_ID varchar2(192) not null,
 constraint PERMISSION_PK primary key(OBJECT_ID,OBJECT_TYPE,GROUP_ID)
)
/

create table CACHEDOBJECTS (
 REGION_ID varchar2(192) not null,
 ITEM_ID varchar2(192) not null,
 ITEM_VALUE blob null,
 LAST_MODIFIED_TIME date not null,
 EXPIRATION_TIME date not null,
 constraint CACHEDOBJECTS_PK primary key(REGION_ID,ITEM_ID)
)
/

create table VERSION (
 VERSION_ID varchar2(60) not null,
 LAST_UPDATE date null,
 constraint VERSION_PK primary key(VERSION_ID)
)
/

create table SEGMENTS (
 GUID varchar2(96) not null,
 FRIENDLY_ID varchar2(96) not null,
 TITLE varchar2(765) not null,
 DESCRIPTION varchar2(765) null,
 CREATED_DATE date not null,
 CRITERIA blob not null,
 constraint SEGMENTS_PK primary key(GUID)
)
/

create unique index SEGMENTS_UI on SEGMENTS(FRIENDLY_ID)
/

create table TASKQUEUE (
 UNIQUEID varchar2(96) not null,
 SCHEDULE_UID varchar2(96) not null,
 ACTIVATION_DATE date not null,
 DEACTIVATION_DATE date null,
 OWNER_UID varchar2(96) null,
 OWNERSHIP_DATE date null,
 CONTENTS blob not null,
 BROADCAST char(3) not null,
 OVERLAP_STYLE decimal(22) default 3 not null,
 TOTAL_FAILURE_COUNT decimal(22) default 0 not null,
 constraint TQ_PK primary key(UNIQUEID)
)
/

create index TQ_SCHEDULEUID on TASKQUEUE(SCHEDULE_UID)
/

create index TQ_ACTDATE on TASKQUEUE(ACTIVATION_DATE)
/

create index TQ_DEACTDATE on TASKQUEUE(DEACTIVATION_DATE)
/

create index TQ_SPEEDLOOKUP on TASKQUEUE(ACTIVATION_DATE,DEACTIVATION_DATE,OWNER_UID,TOTAL_FAILURE_COUNT)
/

create table TASKSCHEDULE (
 UNIQUEID varchar2(96) not null,
 TASK_IDENTIFIER varchar2(765) not null,
 ACTIVATION_DATE date not null,
 DEACTIVATION_DATE date null,
 NEXT_RUN_DATE date not null,
 REPEAT_PERIOD decimal(22) not null,
 OWNER_UID varchar2(96) null,
 CONTENTS blob not null,
 BROADCAST char(3) not null,
 OVERLAP_STYLE decimal(22) default 3 not null,
 constraint TS_PK primary key(UNIQUEID)
)
/

create unique index TS_TASKID on TASKSCHEDULE(TASK_IDENTIFIER)
/

create index TS_NEXTRUNDATE on TASKSCHEDULE(NEXT_RUN_DATE)
/

create index TS_OWNERTASK on TASKSCHEDULE(OWNER_UID)
/

create table TASKFAILURERECORDS (
 TASK_UID varchar2(96) not null,
 NODE_UID varchar2(96) not null,
 ATTEMPT_DATE date not null,
 constraint TFR_PK primary key(TASK_UID,NODE_UID)
)
/

create index TFR_ATTEMPTDATE on TASKFAILURERECORDS(ATTEMPT_DATE)
/

create index TFR_SPEEDLOOKUP on TASKFAILURERECORDS(NODE_UID,ATTEMPT_DATE)
/

create table LOCKS (
 RESOURCE_UID varchar2(192) not null,
 OWNER_UID varchar2(192) not null,
 LOCKED_DATE date not null,
 constraint LOCK_PK primary key(RESOURCE_UID)
)
/

create index LOCK_OWNERUID on LOCKS(OWNER_UID)
/

create table UIDREGISTRY (
 UNIQUEID varchar2(192) not null,
 UIDTYPE varchar2(765) not null,
 IS_SYSTEM char(3) default 0 not null,
 IS_VISIBLE char(3) default 1 not null,
 constraint UID_PK primary key(UNIQUEID)
)
/

create table ALIASES (
 SITE varchar2(192) not null,
 TYPE varchar2(765) not null,
 ALIAS varchar2(765) not null,
 UNIQUEID varchar2(192) not null,
 constraint ALIAS_PK primary key(SITE,TYPE,ALIAS)
)
/

create unique index UNIQUE_SK on ALIASES(UNIQUEID,SITE)
/

create table ADMINCOMPONENTS (
 COMPONENTID varchar2(192) not null,
 DEFAULTTITLE varchar2(450) not null,
 TITLEKEY varchar2(192) null,
 DEFAULTDESCRIPTION varchar2(765) null,
 DESCRIPTIONKEY varchar2(192) null,
 TYPE varchar2(765) null,
 RESOURCEBUNDLEID varchar2(192) null,
 DEFAULTDISPLAYID varchar2(192) not null,
 ENABLEDBYDEFAULT char(3) not null,
 PERMDEFAULTNAME varchar2(300) null,
 PERMNAMEKEY varchar2(192) null,
 PERMNAMEWEIGHT decimal(5, 5) null,
 constraint ADMINCOMP_PK primary key(COMPONENTID)
)
/

create table ADMINCOMPONENTRESOURCES (
 COMPONENTID varchar2(192) not null,
 RESOURCETYPE varchar2(96) not null,
 RESOURCEKEY varchar2(192) not null,
 RESOURCEVALUE varchar2(765) not null,
 constraint ADMINCOMPRES_PK primary key(COMPONENTID,RESOURCETYPE,RESOURCEKEY)
)
/

create table ADMINCOMPONENTBREADCRUMBS (
 COMPONENTID varchar2(192) not null,
 DEFAULTTITLE varchar2(450) not null,
 TITLEKEY varchar2(192) null,
 LINKCOMPONENTID varchar2(192) null,
 LINKDISPLAYID varchar2(192) null,
 SEQ decimal(22) not null,
 constraint ADMINCOMPCRUMBS_PK primary key(COMPONENTID,DEFAULTTITLE)
)
/

create table ADMINCONSOLEMENUITEMS (
 ID varchar2(192) not null,
 PARENTID varchar2(192) null,
 COMPONENTID varchar2(192) null,
 DEFAULTTITLE varchar2(300) not null,
 BUNDLEID varchar2(192) null,
 TITLEKEY varchar2(300) null,
 INTERNALCOMPONENTID varchar2(192) null,
 INTERNALDISPLAYID varchar2(192) null,
 EXTERNALURL varchar2(765) null,
 SEQ decimal(22) default -1 not null,
 VIEWABLEBYSERVERADMINS char(3) not null,
 VIEWABLEBYDELSERVERADMINS char(3) not null,
 VIEWABLEBYSITEADMINS char(3) not null,
 VIEWABLEBYDELSITEADMINS char(3) not null,
 VIEWABLEBYTEAMSPACEADMINS char(3) not null,
 WINDOWNAME varchar2(150) null,
 FRAMENAME varchar2(150) null,
 WIDTH decimal(22) null,
 HEIGHT decimal(22) null,
 CHROMESPEC varchar2(300) null,
 ISDETACHED char(3) null,
 constraint UID_CM primary key(ID)
)
/

create table ADMINCONSOLEMENUITEMORDERING (
 ID varchar2(192) not null,
 PREF_TYPE char(3) not null,
 SEQ decimal(22) default -1 not null,
 SIBLING_ID varchar2(192) not null,
 constraint ACMI_ORDR_PK primary key(ID,PREF_TYPE,SEQ,SIBLING_ID)
)
/

create table PORTLETCATEGORIES (
 PORTLET_CATEGORY_UID varchar2(192) not null,
 FRIENDLY_ID varchar2(384) not null,
 SITE_UID varchar2(192) not null,
 TITLE varchar2(240) not null,
 DESCRIPTION varchar2(765) null,
 CREATED_DATE date not null,
 DELETABLE char(3) not null,
 constraint PC_PK primary key(PORTLET_CATEGORY_UID)
)
/

create unique index PC_TITLESITE on PORTLETCATEGORIES(TITLE,SITE_UID)
/

create unique index PC_FID on PORTLETCATEGORIES(FRIENDLY_ID)
/

create table PORTLETCATEGORIES_PORTLETS (
 PORTLET_CATEGORY_UID varchar2(192) not null,
 PORTLET_UID varchar2(192) not null
)
/

create unique index PC_PORTLETS on PORTLETCATEGORIES_PORTLETS(PORTLET_CATEGORY_UID,PORTLET_UID)
/

create table PORTLETCATEGORIES_AVBLE (
 PAGE_UID varchar2(192) not null,
 SITE_UID varchar2(192) not null,
 PC_CONTEXT_UID varchar2(192) not null
)
/

create unique index AC on PORTLETCATEGORIES_AVBLE(PAGE_UID,SITE_UID,PC_CONTEXT_UID)
/

create table PORTLETCATEGORIES_AVBLE_BULK (
 PAGE_UID varchar2(192) not null,
 SITE_UID varchar2(192) not null,
 SITE_CONTEXT_UID varchar2(192) not null
)
/

create unique index AC_BULK on PORTLETCATEGORIES_AVBLE_BULK(PAGE_UID,SITE_UID,SITE_CONTEXT_UID)
/

create table PORTLETS (
 PORTLET_UID varchar2(192) not null,
 PORTLET_TYPE_UID varchar2(192) not null,
 SERVICE_PROVIDER_ID varchar2(192) not null,
 FRIENDLY_ID varchar2(384) not null,
 TITLE varchar2(765) not null,
 DESCRIPTION varchar2(765) null,
 CREATED_DATE date not null,
 PORTLET_SPECIES_NAME varchar2(96) not null,
 WIDTH decimal(22) not null,
 TIMEOUT decimal(22) not null,
 CHROME_DISPLAYED char(3) not null,
 PUBLISHED char(3) not null,
 SELECTABLE char(3) not null,
 KIND varchar2(96) not null,
 constraint PORTLETS_PK primary key(PORTLET_UID)
)
/

create unique index PORTLETS_SPID on PORTLETS(SERVICE_PROVIDER_ID)
/

create unique index PORTLETS_FID on PORTLETS(FRIENDLY_ID)
/

create table PORTLETTYPES (
 PORTLET_TYPE_UID varchar2(192) not null,
 TITLE varchar2(240) not null,
 DESCRIPTION varchar2(765) null,
 KEYWORDS varchar2(765) null,
 CREATED_DATE date not null,
 SERVICE_PROVIDER_ID varchar2(192) not null,
 SERVICE_PROVIDER_NAME varchar2(96) not null,
 constraint TYPES_PK primary key(PORTLET_TYPE_UID)
)
/

create unique index TYPES_SPID on PORTLETTYPES(SERVICE_PROVIDER_ID)
/

create table JSRPORTLETAPPLICATIONS (
 JSR_PORTLET_APPLICATION_UID varchar2(192) not null,
 CONTEXT_ROOT varchar2(600) not null,
 TITLE varchar2(765) not null,
 DESCRIPTION varchar2(765) null,
 CREATED_DATE date not null,
 constraint APPL_UID primary key(JSR_PORTLET_APPLICATION_UID)
)
/

create unique index APPL_CR on JSRPORTLETAPPLICATIONS(CONTEXT_ROOT)
/

create table JSRPORTLETAPPLICATIONS_TYPES (
 JSR_PORTLET_APPLICATION_UID varchar2(192) not null,
 JSR_PORTLET_TYPE_UID varchar2(192) not null
)
/

create unique index APPL_TYPES on JSRPORTLETAPPLICATIONS_TYPES(JSR_PORTLET_APPLICATION_UID,JSR_PORTLET_TYPE_UID)
/

create table JSRPORTLETAPPLICATIONS_ROLES (
 JSR_PORTLET_APPLICATION_UID varchar2(192) not null,
 ROLE_NAME varchar2(384) not null,
 ROLE_DESCRIPTION varchar2(765) null
)
/

create unique index APPL_ROLES on JSRPORTLETAPPLICATIONS_ROLES(JSR_PORTLET_APPLICATION_UID,ROLE_NAME)
/

create table JSRPORTLETTYPES (
 JSR_PORTLET_TYPE_UID varchar2(192) not null,
 NAME varchar2(765) not null,
 SECURE char(3) not null,
 ORPHAN char(3) not null,
 constraint TYPEUID primary key(JSR_PORTLET_TYPE_UID)
)
/

create table JSRPORTLETTYPES_ROLES (
 JSR_PORTLET_TYPE_UID varchar2(192) not null,
 ROLE_NAME varchar2(384) not null
)
/

create unique index TYPEUID_ROLES on JSRPORTLETTYPES_ROLES(JSR_PORTLET_TYPE_UID,ROLE_NAME)
/

create table JSRPORTLETTYPES_LOCALES (
 JSR_PORTLET_TYPE_UID varchar2(192) not null,
 LOCALE_DISPLAY_NAME varchar2(384) not null
)
/

create unique index TYPEUID_LOCALES on JSRPORTLETTYPES_LOCALES(JSR_PORTLET_TYPE_UID,LOCALE_DISPLAY_NAME)
/

create table JSRPORTLETS (
 JSR_PORTLET_UID varchar2(192) not null,
 JSR_PORTLET_TYPE_UID varchar2(192) not null,
 constraint PORTLETUID primary key(JSR_PORTLET_UID)
)
/

create table JSRPREFERENCES (
 PREFERENCE_UID varchar2(192) not null,
 PREFERENCE_PARENT_UID varchar2(192) null,
 PREFERENCE_KEY varchar2(765) null,
 PREFERENCE_VALUE blob null,
 PREFERENCE_ORDER decimal(22) not null,
 constraint PRIMARYINDEX primary key(PREFERENCE_UID,PREFERENCE_ORDER)
)
/

create index PREFERENCEUID on JSRPREFERENCES(PREFERENCE_UID)
/

create table JSRRESOURCEPREFERENCES (
 RESOURCE_UID varchar2(192) not null,
 PREFERENCE_UID varchar2(192) not null,
 constraint RESOURCEUID primary key(RESOURCE_UID)
)
/

create unique index RESOURCE_PREF on JSRRESOURCEPREFERENCES(RESOURCE_UID,PREFERENCE_UID)
/

create table JSRENTITYPREFERENCES (
 USER_UID varchar2(192) not null,
 JSR_PORTLET_UID varchar2(192) not null,
 PREFERENCE_UID varchar2(192) not null,
 constraint USERPORTLET primary key(USER_UID,JSR_PORTLET_UID)
)
/

create table WSRPPORTLETAPPLICATIONS (
 WSRP_PORTLET_APPLICATION_UID varchar2(192) not null,
 TITLE varchar2(765) not null,
 REGISTRATION_HANDLE varchar2(765) null,
 PRODUCER_TYPE decimal(22) not null,
 DESCRIPTION varchar2(765) null,
 FRIENDLYID varchar2(765) null,
 COOKIE_INIT varchar2(192) null,
 CREATED_DATE date not null,
 PRODUCER_DATA blob not null,
 constraint WSRPAPPL_UID primary key(WSRP_PORTLET_APPLICATION_UID)
)
/

create table WSRPREGISTRATIONPROPERTIES (
 UNIQUEID varchar2(192) not null,
 WSRP_PORTLET_APPLICATION_UID varchar2(192) not null,
 ID varchar2(765) not null,
 QNAME varchar2(765) not null,
 TITLE varchar2(765) null,
 HINT varchar2(765) null,
 VALUE clob not null,
 SORT_INDEX decimal(22) not null,
 constraint REG_PROPERTY_PK primary key(UNIQUEID)
)
/

create table WSRPPORTLETTYPES (
 WSRP_PORTLET_TYPE_UID varchar2(192) not null,
 WSRP_PORTLET_APPLICATION_UID varchar2(192) not null,
 WSRP_PORTLET_HANDLE varchar2(765) not null,
 WSRP_PARENT_HANDLE varchar2(765) null,
 NAME varchar2(765) not null,
 SECURE char(3) not null,
 VISIBLE char(3) not null,
 CREATED_DATE date not null,
 constraint WSRPTYPE_UID primary key(WSRP_PORTLET_TYPE_UID)
)
/

create table WSRPPORTLETS (
 WSRP_PORTLET_UID varchar2(192) not null,
 WSRP_PORTLET_TYPE_UID varchar2(192) not null,
 WSRP_PORTLET_HANDLE varchar2(765) not null,
 WSRP_PORTLET_STATE blob null,
 VERSION decimal(22) default 0 not null,
 constraint WSRPPORTLETUID primary key(WSRP_PORTLET_UID)
)
/

create table WSRPPORTLET_USERSTATE (
 WSRP_PORTLET_UID varchar2(192) not null,
 USER_UID varchar2(192) not null,
 WSRP_PORTLET_HANDLE varchar2(765) null,
 WSRP_PORTLET_STATE blob null
)
/

alter table dbrdDocumentLink
  add constraint Document_to_Message_Parent_FK
    foreign key (MessageUID)
    references dbrdMessage (MessageUID)
/

alter table dbrdForumCapability
  add constraint ForumCap_to_Cap_FK
    foreign key (CapabilityCodeUID)
    references dbrdCapability (CapabilityCodeUID)
/

alter table dbrdForumCapability
  add constraint ForumCap_to_Forum_FK
    foreign key (ForumUID)
    references dbrdForum (ForumUID)
/

alter table dbrdMessageHierarchy
  add constraint Hierarchy_to_Message_Child_FK
    foreign key (ChildMessageUID)
    references dbrdMessage (MessageUID)
/

alter table dbrdMessageHierarchy
  add constraint Hierarchy_to_Message_Parent_FK
    foreign key (ParentMessageUID)
    references dbrdMessage (MessageUID)
/

alter table dbrdMessage
  add constraint Message_to_Topic_FK
    foreign key (TopicUID)
    references dbrdTopic (TopicUID)
/

alter table dbrdMessage
  add constraint Message_to_User_FK
    foreign key (UserSnapshotUID)
    references dbrdUserSnapshot (UserID)
/

alter table WsrpPortlet_UserState
  add constraint PortletState_to_Portlet_FK
    foreign key (wsrp_portlet_uid)
    references WsrpPortlets (wsrp_portlet_uid)
/

alter table WsrpPortlets
  add constraint Portlets_to_Type_FK
    foreign key (wsrp_portlet_type_uid)
    references WsrpPortletTypes (wsrp_portlet_type_uid)
/

alter table WsrpRegistrationProperties
  add constraint Reg_Property_to_Appl_FK
    foreign key (wsrp_portlet_application_uid)
    references WsrpPortletApplications (wsrp_portlet_application_uid)
/

alter table dbrdTopicLastVisited
  add constraint TopicVisit_to_Topic_FK
    foreign key (TopicUID)
    references dbrdTopic (TopicUID)
/

alter table dbrdTopic
  add constraint Topic_to_Forum_FK
    foreign key (ForumUID)
    references dbrdForum (ForumUID)
/

alter table dbrdTopic
  add constraint Topic_to_Message_FK
    foreign key (MessageUID)
    references dbrdMessage (MessageUID)
/

alter table dbrdTopic
  add constraint Topic_to_User_FK
    foreign key (UserSnapshotUID)
    references dbrdUserSnapshot (UserID)
/

alter table WsrpPortletTypes
  add constraint Type_to_Appl_FK
    foreign key (wsrp_portlet_application_uid)
    references WsrpPortletApplications (wsrp_portlet_application_uid)
/

alter table Aliases
  add constraint UIDtoUIDReg_FK
    foreign key (UniqueID)
    references UIDRegistry (uniqueid)
    on delete cascade
/
