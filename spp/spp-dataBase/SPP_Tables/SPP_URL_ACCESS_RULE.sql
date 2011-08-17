-- ============================================================
--   Table: SPP_URL_ACCESS_RULE
-- ============================================================
PROMPT table SPP_URL_ACCESS_RULE
create table SPP_URL_ACCESS_RULE
(
    ID         	INTEGER NOT NULL,
    RULE_SET   	VARCHAR2(25) NOT NULL,
    ORD        	INTEGER NOT NULL,
    URL_PATTERN	VARCHAR2(255) NOT NULL,
    ACCESS_TYPE	VARCHAR2(25) NOT NULL,
    DATA       	VARCHAR2(1024) NULL, 
    constraint PK_URL_ACCESS_RULE primary key (ID)
)
/

