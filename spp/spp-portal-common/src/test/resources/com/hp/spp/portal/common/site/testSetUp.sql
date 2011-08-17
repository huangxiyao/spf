CREATE TABLE SPP_SITE
(
    id                               INTEGER                        NOT NULL
  , name                             VARCHAR (100)                  NOT NULL
  , landing_page                     VARCHAR (256)                  NOT NULL
  , locale_in_url                    INTEGER                        NOT NULL
  , home_page                        VARCHAR (256)
  , portlet_id                       VARCHAR (40)
  , hpappid                          VARCHAR (50)
  , logout_page                      VARCHAR (256)
  , ups_query_id                     VARCHAR (40)
  , protocol                         VARCHAR (5)                    DEFAULT 'http' NOT NULL
  , persist_simulation               INTEGER
  , site_identifier                  VARCHAR (50)
  , access_site                      INTEGER   
  , access_code                      VARCHAR (50)
  , stop_simulation_page             VARCHAR (256)
  ,   CONSTRAINT PK_SPP_SITE
  PRIMARY KEY ( ID )
);

CREATE TABLE SPP_SITE_SETTING
(
    SITE_ID		INTEGER
  , NAME		VARCHAR(100)
  , VALUE		VARCHAR(4000)
  , CONSTRAINT PK_SPP_SITE_SETTING PRIMARY KEY(SITE_ID,NAME)
  , CONSTRAINT FK_SPP_SITE_SETTING FOREIGN KEY(SITE_ID)REFERENCES SPP_SITE(ID)
);

INSERT INTO spp_site (ID,NAME,LANDING_PAGE,LOCALE_IN_URL,HOME_PAGE,PORTLET_ID,HPAPPID,LOGOUT_PAGE,UPS_QUERY_ID,PROTOCOL,PERSIST_SIMULATION,SITE_IDENTIFIER,ACCESS_SITE,ACCESS_CODE,STOP_SIMULATION_PAGE) VALUES (-10000,'DUMMY_SITE','TOBECHANGED','0','TOBECHANGED','TOBECHANGED','SESAME',NULL,'GetData','http','1','dummy','1',NULL,'TOBECHANGED');
INSERT INTO spp_site (ID,NAME,LANDING_PAGE,LOCALE_IN_URL,HOME_PAGE,PORTLET_ID,HPAPPID,LOGOUT_PAGE,UPS_QUERY_ID,PROTOCOL,PERSIST_SIMULATION,SITE_IDENTIFIER,ACCESS_SITE,ACCESS_CODE,STOP_SIMULATION_PAGE) VALUES (1,'TestSite','TOBECHANGED','0','TOBECHANGED','TOBECHANGED','SESAME',NULL,'GetData','http','1','TEST','1',NULL,'TOBECHANGED');

INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('AdminUPSQueryId','Admin_ALL',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('CacheFileterPublicPagesCacheExpiryPeriodInMins','60',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('ESMTimeoutInMilliseconds','40000',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UGSTimeoutInMilliseconds','60000',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UPSTimeoutInMilliseconds','60000',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('HPPAdminLoginId','defaultLoginID',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('HPPAdminPassword','gKoOBWmR3Y1IFOw4wbURRw==',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('SearchIndexPage','Search Index Page(http)',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('SearchPortletID','defaultPortletID',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('SiteDownMessage','The site is not available',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UPSUrl','defaultUPSUrl',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UseMockProfile','false',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UPSMonitoringServletLogin','defaultEmail',-10000);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UPSMonitoringServletPwd','defaultPwd',-10000);

INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('AdminUPSQueryId','Admin_ALL',1);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('CacheFileterPublicPagesCacheExpiryPeriodInMins','40',1);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('ESMTimeoutInMilliseconds','10000',1);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UGSTimeoutInMilliseconds','20000',1);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('UPSTimeoutInMilliseconds','30000',1);
INSERT INTO SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES ('HPPAdminLoginId','site_specific',1);

COMMIT;