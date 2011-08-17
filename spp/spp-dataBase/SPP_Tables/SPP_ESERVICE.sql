
-- ============================================================
--   Table: SPP_ESERVICE                                       
-- ============================================================
PROMPT table SPP_ESERVICE
create table SPP_ESERVICE
(
    ID                        INTEGER                not null,
    SITE_ID                   INTEGER                null    ,
    STANDARDPARAMETERSET_ID   INTEGER                null    ,
    NAME                      VARCHAR2(50)           not null,
    METHOD                    VARCHAR2(5)            null    ,
    PRODUCTION_URL            VARCHAR2(500)          null    ,
    TEST_URL                  VARCHAR2(500)          null    ,
    CREATION_DATE             DATE                   null    ,
    LAST_MODIFICATION_DATE    DATE                   null    ,
    IS_NEW_WINDOW	      INT		     null    ,
    SECURITY_MODE	      INT		     null    ,
    SIMULATION_MODE	      INT		     null    ,
    WINDOW_PARAMETERS	      VARCHAR2(500)	     null    ,
    CHAR_ENCODING	      VARCHAR2(50)	     null    ,
    constraint PK_SPP_ESERVICE primary key (ID)
)
/