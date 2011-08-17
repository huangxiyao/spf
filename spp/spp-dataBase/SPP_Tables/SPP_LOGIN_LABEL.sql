-- ============================================================
--   Table: SPP_LOGIN_LABEL                                       
-- ============================================================
PROMPT table SPP_LOGIN_LABEL
create table SPP_LOGIN_LABEL
(
    LOCALE                        VARCHAR2(5)               ,
    LABEL                          VARCHAR2(50)             not null,
    MESSAGE                        VARCHAR2(1024)             not null,
    constraint SPP_LOGIN_LABEL primary key (LOCALE, LABEL, MESSAGE)
)
/
