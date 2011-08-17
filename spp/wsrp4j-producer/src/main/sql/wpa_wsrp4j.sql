
    drop table WSRP_APPLICATION cascade constraints;

    drop table WSRP_CONSUMER cascade constraints;

    drop table WSRP_CONSUMER_PORTLET cascade constraints;

    drop table WSRP_PORTLET cascade constraints;

    drop table WSRP_PREFERENCE cascade constraints;

    drop table WSRP_PREFERENCE_VALUE cascade constraints;

    create table WSRP_APPLICATION (
        ID varchar2(100) not null,
        DEFINITION_ID varchar2(100) not null,
        primary key (ID)
    );

    create table WSRP_CONSUMER (
        HANDLE varchar2(100) not null,
        NAME varchar2(100) not null,
        AGENT varchar2(100) not null,
        METHOD_GET_SUPPORTED varchar2(5) not null,
        primary key (HANDLE)
    );

    create table WSRP_CONSUMER_PORTLET (
        CONSUMER_HANDLE varchar2(100) not null,
        PORTLET_HANDLE varchar2(100) not null,
        primary key (CONSUMER_HANDLE, PORTLET_HANDLE)
    );

    create table WSRP_PORTLET (
        ID varchar2(100) not null,
        APPLICATION varchar2(100) not null,
        PORTLET_ID varchar2(100) not null,
        DEFINITION_ID varchar2(100) not null,
        PARENT_HANDLE varchar2(100),
        primary key (ID)
    );

    create table WSRP_PREFERENCE (
        ID varchar2(100) not null,
        PORTLET varchar2(100) not null,
        NAME varchar2(100) not null,
        READ_ONLY varchar2(5) not null,
        primary key (ID)
    );

    create table WSRP_PREFERENCE_VALUE (
        PREFERENCE varchar2(100) not null,
        PREFERENCE_IDX number(19,0) not null,
        VALUE varchar2(100),
        primary key (PREFERENCE, PREFERENCE_IDX)
    );

    alter table WSRP_PORTLET 
        add constraint FKApplication 
        foreign key (APPLICATION) 
        references WSRP_APPLICATION;

    alter table WSRP_PREFERENCE 
        add constraint FKPortlet 
        foreign key (PORTLET) 
        references WSRP_PORTLET;

    alter table WSRP_PREFERENCE_VALUE 
        add constraint FKPreference 
        foreign key (PREFERENCE) 
        references WSRP_PREFERENCE;


    insert into WSRP_APPLICATION (ID, DEFINITION_ID)
        VALUES ('0', 'wsrp4j-producer')

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION)
        VALUES ('0.1', '1', 'wsrp4j-producer.WSRP4JTestPortlet', '0')