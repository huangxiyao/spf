INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (1, 'portal', 100, '/portal/console/*', 'ldap', NULL);
INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (2, 'portal', 200, '/portal/site/*/template.STARTSIMULATION*', 'ldap', NULL);
INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (3, 'portal', 300, '/portal/jsp/SPP/cacheAdmin.jsp', 'ldap', NULL);
INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (4, 'portal', 400, '/portal/jsp/SPP/log4jAdmin.jsp', 'ldapacl', 'slawomir.zachcial@hp.com, philipp.chifflet@hp.com, remi.barbarin@hp.com, philip.bretherton@hp.com, adrien.geymond@hp.com, girish.keshavamurthy@hp.com, cyril.micoud@hp.com, hari.rao@hp.com, akash.srivastava@hp.com, mathieu.vidal@hp.com');
INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (5, 'wsrp', 100, '/spp-services-web/jsp/cacheAdmin.jsp', 'ldap', NULL);
INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (6, 'wsrp', 200, '/spp-services-web/jsp/log4jAdmin.jsp', 'ldapacl', 'slawomir.zachcial@hp.com, philipp.chifflet@hp.com, remi.barbarin@hp.com, philip.bretherton@hp.com, adrien.geymond@hp.com, girish.keshavamurthy@hp.com, cyril.micoud@hp.com, hari.rao@hp.com, akash.srivastava@hp.com, mathieu.vidal@hp.com');
INSERT INTO SPP_URL_ACCESS_RULE ( ID, RULE_SET, ORD, URL_PATTERN, ACCESS_TYPE, DATA ) VALUES (7, 'portal', 50, '/portal/site/*', 'simldap', NULL);

INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ('portal.ManagedServer.port.https', '50053', 'I', 'N', 'N', 'HTTPS port number of the WebLogic managed server running Vignette'); 
INSERT INTO SPP_CONFIG_ENTRY ( NAME, VALUE, TYPE, REQUIRED, READ_ONLY, DESCRIPTION ) VALUES ('wsrp.ManagedServer.port.https', '20053', 'I', 'N', 'N', 'HTTPS port number of the WebLogic managed server running WSRP'); 


COMMIT;
