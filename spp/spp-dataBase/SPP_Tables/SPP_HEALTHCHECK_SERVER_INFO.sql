-- ============================================================
--   Table: SPP_HEALTHCHECK_SERVER_INFO                                     
-- ============================================================
PROMPT table SPP_HEALTHCHECK_SERVER_INFO
create table SPP_HEALTHCHECK_SERVER_INFO
(
	server_name varchar2(100) not null,
	application_name varchar2(50) not null,
	server_type varchar2(50) not null,
	site_name varchar2(50) not null,
	out_of_rotation_flag char default 'N' not null,
	constraint spp_healthcheck_server_info_pk primary key (server_name)
)
/