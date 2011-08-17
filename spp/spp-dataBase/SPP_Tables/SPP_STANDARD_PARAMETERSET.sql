-- ============================================================
--   Table: SPP_STANDARD_PARAMETERSET                          
-- =============================================================

PROMPT table SPP_STANDARD_PARAMETERSET
create table SPP_STANDARD_PARAMETERSET
(
    ID                        INTEGER                not null,
    SITE_ID                   INTEGER                not null,
    NAME                      VARCHAR2(50)           null    ,
    CREATION_DATE             DATE                   null    ,
    LAST_MODIFICATION_DATE    DATE                   null    ,
    constraint PK_SPP_STANDARD_PARAMETERSET primary key (ID)
)
/