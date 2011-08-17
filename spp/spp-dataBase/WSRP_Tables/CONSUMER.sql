PROMPT table WSRP_CONSUMER
create table WSRP_CONSUMER (
        handle varchar2(50) not null,
        name varchar2(50) not null,
        agent varchar2(50) not null,
        method_get_supported varchar2(5) not null,
        primary key (handle)
    );