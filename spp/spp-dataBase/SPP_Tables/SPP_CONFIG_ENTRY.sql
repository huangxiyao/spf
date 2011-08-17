-- ============================================================
--   Table: SPP_CONFIG_ENTRY                                       
-- ============================================================
PROMPT table SPP_CONFIG_ENTRY
create table SPP_CONFIG_ENTRY
(
	name varchar2(100) not null,
	value varchar2(4000) not null,
	type char default 'S' not null,
	required char default 'Y' not null,
	read_only char default 'N' not null,
	description varchar2(300),
	constraint spp_config_entry_pk primary key (name)
)
/
