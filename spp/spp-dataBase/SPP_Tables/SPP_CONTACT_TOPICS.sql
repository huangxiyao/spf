-- ============================================================
--   Table: SPP_CONTACT_TOPICS                                       
-- ============================================================
PROMPT table SPP_CONTACT_TOPICS
CREATE TABLE SPP_CONTACT_TOPICS 
( 
  LANGUAGECODE  VARCHAR2 (2)  NOT NULL, 
  TOPICCODE     NUMBER        NOT NULL, 
  TOPICLABEL    VARCHAR2 (4000)  NOT NULL, 
  USERID        VARCHAR2 (200)  NOT NULL )
/
