-- ============================================================
--   Table: SPP_CONTACT_LANGUAGE                                       
-- ============================================================
PROMPT table SPP_CONTACT_LANGUAGE
CREATE TABLE SPP_CONTACT_LANGUAGE 
(
  LANGUAGECODE        VARCHAR2 (2)    NOT NULL,
  LANGUAGELABEL        VARCHAR2 (90)    NOT NULL,
  ISHPPCOMPLIANT    NUMBER        DEFAULT 1 NOT NULL 
)
/
