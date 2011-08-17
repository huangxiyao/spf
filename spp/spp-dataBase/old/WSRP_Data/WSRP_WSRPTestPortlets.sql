insert into WSRP_APPLICATION (ID, DEFINITION_ID) VALUES ('1', 'spp-services-web');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values
       ('1.2', '2', 'spp-services-web.SlowPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.3', '3', 'spp-services-web.AdminConfigurablePortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.4', '4', 'spp-services-web.CachedPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.5', '5', 'spp-services-web.DownloadPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.6', '6', 'spp-services-web.UrlPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.7', '7', 'spp-services-web.IPCSenderPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.8', '8', 'spp-services-web.IPCReceiverPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.9', '9', 'spp-services-web.UrlParameterPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.10', '10', 'spp-services-web.ContentDisplayPortletRemote', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.11', '11', 'spp-services-web.ContentListPortletRemote', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.12', '12', 'spp-services-web.HelloPortlet', '1');

    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.13', '13', 'spp-services-web.ModeMappingPortlet', '1');
	
    insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values 
       ('1.14', '14', 'spp-services-web.EServicesMgt', '1');
	
	commit;