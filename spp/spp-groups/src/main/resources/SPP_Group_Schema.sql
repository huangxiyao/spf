drop table SPP_GROUP cascade constraints;
drop table SPP_SITE cascade constraints;
drop sequence SPP_GROUP_ID_SEQ;
drop sequence SPP_SITE_ID_SEQ;
create table SPP_GROUP (ID number(19,0) not null, NAME varchar2(100), RULES blob, CREATIONDATE date, MODIFICATIONDATE date, SITE_ID number(19,0), primary key (ID));
create table SPP_SITE (ID number(19,0) not null, name varchar2(100), primary key (ID));
alter table SPP_GROUP add constraint SPP_GROUP_SITE_FK foreign key (SITE_ID) references SPP_SITE;
create sequence SPP_GROUP_ID_SEQ;
create sequence SPP_SITE_ID_SEQ;
