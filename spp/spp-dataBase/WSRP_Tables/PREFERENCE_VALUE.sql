PROMPT table WSRP_PREFERENCE_VALUE
create table WSRP_PREFERENCE_VALUE (
        PREFERENCE varchar2(100) not null,
        PREFERENCE_IDX number(10,0) not null,
        VALUE varchar2(50),
        primary key (PREFERENCE, PREFERENCE_IDX)
    );
