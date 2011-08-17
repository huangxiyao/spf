------------------------------------------------------------------------------
-- WARNING : THESE TABLES ARE NO MORE PART OF SPP DATABASES
-- THIS SCRIPT IS ONLY KEPT FOR HISTORICAL REASON
-- IT MUST NOT BE PLAYED ON DATABASE WHERE SPP OR VIGNETTE TABLES ARE HOSTED
------------------------------------------------------------------------------

CREATE TABLE ups_entity (
id NUMBER(38),
name VARCHAR2(50),
entity_type NUMBER(3),
CONSTRAINT id_entity_pk PRIMARY KEY (id)
)
/

CREATE SEQUENCE seq_ups_entity
INCREMENT BY 1
START WITH 1
NOMAXVALUE
NOCYCLE
NOCACHE
/

--
-- CREATING CONFIG TABLE AND SEQUENCE
--
CREATE TABLE ups_config (
id NUMBER(38),
entity_id NUMBER(38),
creation_date DATE,
version_number NUMBER(38),
config CLOB,
admin_comments CLOB,
CONSTRAINT id_config_pk PRIMARY KEY (id),
CONSTRAINT id_entity_fk FOREIGN KEY (entity_id) REFERENCES ups_entity (id)
)
/

CREATE SEQUENCE seq_ups_config
INCREMENT BY 1
START WITH 1
NOMAXVALUE
NOCYCLE
NOCACHE
/
