-- ============================================================
--   Table: SPP_LOGIN_RULE                                       
-- ============================================================
PROMPT table SPP_LOGIN_RULE
CREATE TABLE SPP_LOGIN_RULE
(
    RULE_TYPE                           VARCHAR2 (20)                  NOT NULL
  , RULE_CLASSES                       VARCHAR2(500)                  NOT NULL
  , SITE_IDENTIFIER                  VARCHAR2 (20)                    NOT NULL
  , CONSTRAINT PK_SPP_LOGIN_RULE PRIMARY KEY (RULE_TYPE, SITE_IDENTIFIER)
)
/