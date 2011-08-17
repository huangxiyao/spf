-- ============================================================
--   Table: SPP_CONTACT_FAULTS                                       
-- ============================================================
PROMPT table SPP_CONTACT_FAULTS
CREATE TABLE SPP_CONTACT_FAULTS 
(
  LANGUAGECODE    VARCHAR2 (2)    NOT NULL,
  RULENUMBER    NUMBER   (4)    NOT NULL,
  CODE        VARCHAR2 (30)    NOT NULL,
  FIELDNAME    VARCHAR2 (50)    NOT NULL,
  DESCRIPTION    VARCHAR2 (4000)    NOT NULL 
)
/
