-- ============================================================
--   Table: SPP_HPPGROUP                                       
-- ============================================================
PROMPT table SPP_HPPGROUP

CREATE TABLE SPP_HPPGROUP
(
  HPPGROUP  VARCHAR2(100)                       NOT NULL,
  PORTAL    VARCHAR2(100)                       NOT NULL,
  constraint PK_SPP_HPPGROUP PRIMARY KEY (HPPGROUP, PORTAL)
)
/
