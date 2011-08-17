-- ============================================================
--   Table: SPP_GROUP                                       
-- ============================================================
PROMPT table SPP_GROUP
create table SPP_GROUP
(
	ID INTEGER not null,
	NAME varchar2(100),
	RULES blob,
	CREATIONDATE date,
	MODIFICATIONDATE date,
	SITE_ID INTEGER,
	constraint PK_SPP_GROUP primary key (ID)
)
/
