PROMPT table WSRP_CONSUMER_PORTLET
create table WSRP_CONSUMER_PORTLET (
        consumer_handle varchar2(50) not null,
        portlet_handle varchar2(50) not null,
        primary key (consumer_handle, portlet_handle)
    );