-- ============================================================
--   Table: SPP_SITE_SETTING                                       
-- ============================================================
PROMPT table SPP_SITE_SETTING
CREATE TABLE SPP_SITE_SETTING
(
    SITE_ID		NUMBER
  , NAME		VARCHAR2(100)
  , VALUE		VARCHAR2(4000)
  , CONSTRAINT PK_SPP_SITE_SETTING PRIMARY KEY(SITE_ID,NAME)
  , CONSTRAINT FK_SPP_SITE_SETTING FOREIGN KEY(SITE_ID)REFERENCES SPP_SITE(ID)
)
/