PROMPT table WSRP_PREFERENCE   
create table WSRP_PREFERENCE (
        ID varchar2(100) not null,
        NAME varchar2(50) not null,
        READ_ONLY varchar2(5) not null,
        PORTLET varchar2(50) not null,
        primary key (ID)
    );
