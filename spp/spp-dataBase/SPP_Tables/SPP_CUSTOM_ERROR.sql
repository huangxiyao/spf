-- ============================================================
--   Table: SPP_CUSTOM_ERROR                                       
-- ============================================================
PROMPT table SPP_CUSTOM_ERROR
CREATE TABLE SPP_CUSTOM_ERROR
(
    portal                           VARCHAR2 (100)                  NOT NULL
  , error_code                       NUMBER                          NOT NULL
  , locale                           VARCHAR2 (5)                    NOT NULL
  , custom_error_message             VARCHAR2 (256)                  NOT NULL
  , CONSTRAINT PK_SPP_CUSTOM_ERROR PRIMARY KEY (portal, error_code, locale)
)
/
