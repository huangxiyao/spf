
SPOOL SPP_InitData.log

PROMPT INSERTING into SPP_SITE
INSERT INTO SPP_SITE ( ID, NAME ) VALUES ( 
1, 'smartportal'); 
INSERT INTO SPP_SITE ( ID, NAME ) VALUES ( 
2, 'sppqa'); 

PROMPT INSERTING into SPP_CONFIG_ENTRY
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.IndexListView', 'jsp/SPP/search/IndexListView.jsp', 'S', 'Y', 'N', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.AgentPageView', 'jsp/SPP/search/AgentPageView.jsp', 'S', 'Y', 'N', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.ErrorPage', 'jsp/SPP/search/ErrorPage.jsp', 'S', 'Y', 'N', NULL); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.castorMappingFile', '/com/hp/spp/search/audience/service/AudiencingCastorMapping.xml', 'S', 'Y', 'N', 'name of file having castor mappings');
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.search.PortalEngineClassName', 'com.hp.spp.search.vap.VignettePortalEngine', 'S', 'Y', 'N', 'name of class implementing the PortalEngine');


INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.report.AudiencingReportHelper', 'jsp/SPP/report/AudiencingReportHelper.jsp', 'S', 'Y', 'N', NULL); 

INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.report.ErrorPage', 'jsp/SPP/report/ErrorPage.jsp', 'S', 'Y', 'N', NULL); 



INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ( 
'SPP.hpputils.DESKeyLocation', '/com/hp/spp/hpp/admin/keyfile', 'S', 'Y', 'N', 'this is the location of the Triple DES key'); 


PROMPT INSERTING into SPP_ESERVICE_PARAMETER
INSERT INTO SPP_ESERVICE_PARAMETER ( ID, STANDARD_PARAMETERSET_ID, ESERVICE_ID, NAME, EXPRESSION ) VALUES ( 
1, NULL, 1, 'siteName', 'SiteId'); 
INSERT INTO SPP_ESERVICE_PARAMETER ( ID, STANDARD_PARAMETERSET_ID, ESERVICE_ID, NAME, EXPRESSION ) VALUES ( 
2, NULL, 2, 'siteName', 'SiteId'); 

PROMPT INSERTING into WSRP_APPLICATION
insert into WSRP_APPLICATION (ID, DEFINITION_ID) VALUES ('1', 'spp-services-web');
 
PROMPT INSERTING into WSRP_PORTLET
insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) 
select '1.1', '1', 'spp-services-web.Groups', WSRP_APPLICATION.ID from WSRP_APPLICATION  where definition_Id = 'spp-services-web';

PROMPT INSERTING into WSRP_PORTLET
insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION)
select '1.13', '13', 'spp-services-web.EServicesMgt', WSRP_APPLICATION.ID from WSRP_APPLICATION where definition_Id = 'spp-services-web';

PROMPT INSERTING into WSRP_PORTLET
insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) 
select '1.4', '4', 'spp-services-web.HppReport', WSRP_APPLICATION.ID from WSRP_APPLICATION where definition_Id = 'spp-services-web';


PROMPT INSERTING into WSRP_PORTLET
insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) 
select '1.5', '5', 'spp-services-web.AgeingReport', WSRP_APPLICATION.ID from WSRP_APPLICATION where definition_Id = 'spp-services-web';


SPOOL OFF
