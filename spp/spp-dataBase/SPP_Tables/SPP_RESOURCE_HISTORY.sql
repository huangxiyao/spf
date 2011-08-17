-- ============================================================
--   Table: SPP_RESOURCE_HISTORY                               
-- ============================================================
PROMPT table SPP_RESOURCE_HISTORY
create table SPP_RESOURCE_HISTORY
(
    ID                 INTEGER                not null,
    SITE_ID            INTEGER                null    ,
    OWNER              VARCHAR2(100)          null    ,
    MODIFICATION_COMMENT            VARCHAR2(500)          null    ,
    MODIFICATION_TYPE  INTEGER                not null,
    CREATION_DATE      DATE                   not null,
    DATA_CHANGE	       BLOB,
    BACKUP_FILE	       BLOB,	
    constraint PK_SPP_RESOURCE_HISTORY primary key (ID)
)
/