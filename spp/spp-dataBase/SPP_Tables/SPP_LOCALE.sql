-- ============================================================
--   Table: SPP_LOCALE                                       
-- ============================================================
PROMPT table SPP_LOCALE
create table SPP_LOCALE
(
    LANGUAGE_CODE                  VARCHAR2(10)		NOT NULL     ,
    COUNTRY_CODE                   VARCHAR2(10)			     ,
    SITE_IDENTIFIER                VARCHAR2(30)			     ,
    GUEST_USER			   VARCHAR2(30)		NOT NULL     ,
    PREFERRED_LANGUAGE_CODE	   VARCHAR2(10)		NOT NULL
)
/