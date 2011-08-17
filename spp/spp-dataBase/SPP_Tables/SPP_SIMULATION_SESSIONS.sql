-- ============================================================
--   Table: SPP_SIMULATION_SESSION                                       
-- ============================================================
PROMPT table SPP_SIMULATION_SESSION
CREATE TABLE SPP_SIMULATION_SESSION 
( 
  SITE  VARCHAR(100)        NOT NULL, 
  HPPID_SIMULATOR  VARCHAR(50)        NOT NULL, 
  HPPID_SIMULATED  VARCHAR(50)        NOT NULL,
  CONSTRAINT PK_SPP_SIMULATION_SESSION
  PRIMARY KEY (SITE, HPPID_SIMULATOR, HPPID_SIMULATED) 
)
/
