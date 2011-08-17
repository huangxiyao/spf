-- ============================================================
--   Table: SPP_WORKFLOW_ERROR                                       
-- ============================================================
PROMPT table SPP_WORKFLOW_ERROR
CREATE TABLE SPP_WORKFLOW_ERROR
(
    portal                           VARCHAR2 (100)                  NOT NULL
  , error_code                       NUMBER                          NOT NULL
  , target_url                       VARCHAR2 (256)                  NOT NULL
  , display_message                  NUMBER   (1)                    NOT NULL
  , CONSTRAINT PK_SPP_WORKFLOW_ERROR PRIMARY KEY (portal, error_code)
)
/
