


alter table WSRP_PORTLET
	add constraint FKApplication foreign key (APPLICATION)
	references WSRP_APPLICATION
/

alter table WSRP_PREFERENCE 
	add constraint FKPortlet foreign key (PORTLET)
	references WSRP_PORTLET
/
alter table WSRP_PREFERENCE_VALUE
	add constraint FKPreference foreign key (PREFERENCE)
	references WSRP_PREFERENCE
/
