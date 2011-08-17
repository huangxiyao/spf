PROMPT table WSRP_PORTLET
create table WSRP_PORTLET (
        ID varchar2(50) not null,
        PORTLET_ID varchar2(50) not null,
        DEFINITION_ID varchar2(50) not null,
        APPLICATION varchar2(50) not null,
        PARENT_HANDLE varchar2(50),
        primary key (ID)
    );
