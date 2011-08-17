
-- ============================================================
--   Table: SPP_ESERVICE_PARAMETER                             
-- ============================================================
PROMPT table SPP_ESERVICE_PARAMETER
create table SPP_ESERVICE_PARAMETER
(
    ID                        INTEGER                not null,
    STANDARD_PARAMETERSET_ID  INTEGER                null    ,
    ESERVICE_ID               INTEGER                null    ,
    NAME                      VARCHAR2(50)           null    ,
    EXPRESSION                VARCHAR2(2000)          null    ,
    constraint PK_SPP_ESERVICE_PARAMETER primary key (ID)
)
/