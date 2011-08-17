-- ============================================================
--   Table: SPP_USERSESSION                                       
-- ============================================================
PROMPT table SPP_USERSESSION
CREATE TABLE SPP_USERSESSION 
( 
  HPPUSERID                 VARCHAR2 (200)  NOT NULL, 
  USERSESSION_CREATIONDATE  DATE          NOT NULL, 
  PORTAL_SESSIONID          VARCHAR2 (200)  NOT NULL
)
/
