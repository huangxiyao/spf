-- ============================================================
--   Table: SPP_SITE                                       
-- ============================================================
PROMPT table SPP_SITE
CREATE TABLE SPP_SITE
(
    id                               NUMBER                          NOT NULL
  , name                             VARCHAR2 (100)                  UNIQUE NOT NULL
  , landing_page                     VARCHAR2 (256)                  NOT NULL
  , locale_in_url                    NUMBER   (1)                    NOT NULL
  , home_page                        VARCHAR2 (256)                  
  , portlet_id                       VARCHAR2 (40)                   
  , hpappid                          VARCHAR2 (50)                   
  , logout_page                      VARCHAR2 (256)                  
  , ups_query_id                     VARCHAR2 (40)                   
  , protocol                         VARCHAR2 (5)                    DEFAULT 'http' NOT NULL
  , persist_simulation               NUMBER   (1)                    
  , site_identifier                  VARCHAR2 (50)                   
  , access_site                      NUMBER   (1)                    
  , access_code                      VARCHAR2 (50) 
  , stop_simulation_page             VARCHAR2 (256)                  
  ,   CONSTRAINT PK_SPP_SITE
  PRIMARY KEY ( ID )                   
)
/
