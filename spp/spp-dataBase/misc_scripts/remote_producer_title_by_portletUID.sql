---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Displays the name of the Remote Producer using the portlet uid
-- Improperly working Portlet uid can be found from the error logs / VAP console
-- This can be used to find out the remote producer, 
-- to disable it using wsrpAdmin.jsp page of WSRP shield feature
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Run the query with the portlet UID in trouble
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
select title from wsrpportletapplications where wsrp_portlet_application_uid = 
( select wsrp_portlet_application_uid from wsrpportlettypes where wsrp_portlet_type_uid = 
( select wsrp_portlet_type_uid from wsrpportlets where wsrp_portlet_uid = 
( select service_provider_id from portlets where portlet_uid like '8228ad955e4513d56e46a5107743ce01' ) ) ) 
