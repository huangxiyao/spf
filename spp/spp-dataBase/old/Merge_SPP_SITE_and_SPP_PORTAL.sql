DROP TABLE SPP_SITE CASCADE CONSTRAINTS ; 

CREATE TABLE SPP_SITE ( 
  ID                       NUMBER        NOT NULL, 
  NAME                VARCHAR2 (100)  UNIQUE NOT NULL, 
  LANDING_PAGE        VARCHAR2 (256)  NOT NULL, 
  LOCALE_IN_URL       NUMBER (1)    NOT NULL, 
  HOME_PAGE           VARCHAR2 (256), 
  PORTLET_ID          VARCHAR2 (40), 
  HPAPPID             VARCHAR2 (50), 
  LOGOUT_PAGE         VARCHAR2 (256), 
  UPS_QUERY_ID        VARCHAR2 (40), 
  PROTOCOL            VARCHAR2 (5)  DEFAULT 'http' NOT NULL, 
  PERSIST_SIMULATION  NUMBER (1), 
  CONSTRAINT PK_SPP_SITE
  PRIMARY KEY ( ID ) 
) ; 

-- Verify the data to import depending of environment
INSERT INTO SPP_SITE(ID, NAME, LANDING_PAGE, LOCALE_IN_URL, HOME_PAGE, PORTLET_ID, HPAPPID, LOGOUT_PAGE, UPS_QUERY_ID, PROTOCOL, PERSIST_SIMULATION ) VALUES ( 
2, 'sppportal', 'publicsppportal/menuitem.47729479d29138ac0bc0c41085daa8f0', 0, '16ab47298ab4119878f8e96885daa8f0'
, 'ff42d0bc6e8a74a8ca66bfb385daa8f0', 'SPPPORTAL', NULL, 'sppqa_HPP_ALL', 'http', 1); 
INSERT INTO SPP_SITE (ID, NAME, LANDING_PAGE, LOCALE_IN_URL, HOME_PAGE, PORTLET_ID, HPAPPID, LOGOUT_PAGE, UPS_QUERY_ID, PROTOCOL, PERSIST_SIMULATION ) VALUES ( 
3, 'sppqa', 'publicsppqa/menuitem.eb64825d42071d5d279ac31085daa8f0', 0, 'cdd7a4c978685937bafcba1c85daa8f0'
, 'ff42d0bc6e8a74a8ca66bfb385daa8f0', 'SPPQA', NULL, 'sppqa_HPP_ALL', 'http', 1); 
INSERT INTO SPP_SITE (ID, NAME, LANDING_PAGE, LOCALE_IN_URL, HOME_PAGE, PORTLET_ID, HPAPPID, LOGOUT_PAGE, UPS_QUERY_ID, PROTOCOL, PERSIST_SIMULATION ) VALUES ( 
4, 'console', 'console/menuitem.604befba6e8d67446d191c4d85daa8f0', 0, NULL, 'ff42d0bc6e8a74a8ca66bfb385daa8f0'
, 'SPPQA', NULL, NULL, 'http', 1); 
INSERT INTO SPP_SITE (ID, NAME, LANDING_PAGE, LOCALE_IN_URL, HOME_PAGE, PORTLET_ID, HPAPPID, LOGOUT_PAGE, UPS_QUERY_ID, PROTOCOL, PERSIST_SIMULATION ) VALUES ( 
1, 'smartportal', 'smartportal/menuitem.a4ddecf9a0a5690fe3d2cd8686daa8f0', 0, '47c1218974f7b0f4d35d712b86daa8f0'
, 'ff42d0bc6e8a74a8ca66bfb385daa8f0', 'SESAME', NULL, 'sppqa_HPP_ALL', 'http', 1); 


ALTER TABLE SPP_ESERVICE ADD  CONSTRAINT SPP_ESERVICE_SITE_FK
 FOREIGN KEY (SITE_ID) 
  REFERENCES SPPDEV.SPP_SITE (ID) ;

ALTER TABLE SPP_GROUP ADD  CONSTRAINT SPP_GROUP_SITE_FK
 FOREIGN KEY (SITE_ID) 
  REFERENCES SPPDEV.SPP_SITE (ID) ;

ALTER TABLE SPP_RESOURCE_HISTORY ADD  CONSTRAINT SPP_RESOURCE_SITE_FK
 FOREIGN KEY (SITE_ID) 
  REFERENCES SPPDEV.SPP_SITE (ID) ;

ALTER TABLE SPP_STANDARD_PARAMETERSET ADD  CONSTRAINT SPP_STD_PARAMSET_SITE_FK
 FOREIGN KEY (SITE_ID) 
  REFERENCES SPPDEV.SPP_SITE (ID) ;

DROP TABLE SPP_PORTAL CASCADE CONSTRAINTS;

ALTER TABLE SPP_CUSTOM_ERROR ADD  CONSTRAINT SPP_CUSTOM_ERROR_PORTAL_FK
 FOREIGN KEY (PORTAL) 
  REFERENCES SPPDEV.SPP_SITE (NAME) ;
  
ALTER TABLE SPPDEV.SPP_WORKFLOW_ERROR ADD  CONSTRAINT SPP_WORKFLOW_ERROR_PORTAL_FK
 FOREIGN KEY (PORTAL) 
  REFERENCES SPP_SITE (NAME) ;