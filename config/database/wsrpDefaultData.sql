INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/suffix', 'generationQualifier'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/country', 'c'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/stateprov', 'st'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/online/email', 'mail'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/name', 'cn'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/family', 'sn'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/mobile/number', 'mobile'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/online/email', 'mail'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/prefix', 'personalTitle'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/online/uri', 'labeledURI'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/street', 'street'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/country', 'c'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/fax/number', 'fax'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/stateprov', 'st'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/postalcode', 'postalCode'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/online/uri', 'labeledURI'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/organization', 'o'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/city', '1'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/city', 'c'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'name/given', 'givenName'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/postal/name', 'cn'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/pager/number', 'pager'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'employerInfo/department', 'departmentNumber'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/postalcode', 'postalCode'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/mobile/number', 'mobile'); >

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'employerInfo/jobtitle', 'title'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/postal/street', 'street'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'homeInfo/telecom/telephone/number', 'homePhone'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/fax/number', 'fax'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/pager/number', 'pager'); 

INSERT INTO CONSUMER_DEF_USER_PROFILE_MAP(PORTAL_ID, PROFILE_KEY, PROFILE_VALUE) 
VALUES('portal1', 'businessInfo/telecom/telephone/number', 'telephoneNumber'); 

//We should insert default registration data also over here.
INSERT INTO CONSUMER_GLOBAL_DATA(PORTAL_ID, CONSUMER_ENABLE) VALUES('portal1', 1); 


INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.bdate', 'bdate');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.gender', 'gender');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.prefix', 'name/prefix');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.given', 'name/given');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.family', 'name/family');
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.middle', 'name/middle');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.name.nickName', 'name/nickName');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.employer', 'employerInfo/employer');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.department', 'employerInfo/department');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.jobtitle', 'employerInfo/jobtitle');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.name', 'homeInfo/postal/name');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.street', 'homeInfo/postal/street');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.city', 'homeInfo/postal/city');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.stateprov', 'homeInfo/postal/stateprov');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.postalcode', 'homeInfo/postal/postalcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.country', 'homeInfo/postal/country');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.postal.organization', 'homeInfo/postal/organization');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.intcode', 'homeInfo/telecom/telephone/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.loccode', 'homeInfo/telecom/telephone/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.number', 'homeInfo/telecom/telephone/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.ext', 'homeInfo/telecom/telephone/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.telephone.comment', 'homeInfo/telecom/telephone/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.intcode', 'homeInfo/telecom/fax/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.loccode', 'homeInfo/telecom/fax/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.number', 'homeInfo/telecom/fax/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.ext', 'homeInfo/telecom/fax/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.fax.comment', 'homeInfo/telecom/fax/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.intcode', 'homeInfo/telecom/mobile/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.loccode', 'homeInfo/telecom/mobile/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.number', 'homeInfo/telecom/mobile/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.ext', 'homeInfo/telecom/mobile/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.mobile.comment', 'homeInfo/telecom/mobile/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.intcode', 'homeInfo/telecom/pager/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.loccode', 'homeInfo/telecom/pager/loccode');
    
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.number', 'homeInfo/telecom/pager/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.ext', 'homeInfo/telecom/pager/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.telecom.pager.comment', 'homeInfo/telecom/pager/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.online.email', 'homeInfo/online/email');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.home-info.online.uri', 'homeInfo/online/uri');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.name', 'businessInfo/postal/name');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.street', 'businessInfo/postal/street');
    
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.city', 'businessInfo/postal/city');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.stateprov', 'businessInfo/postal/stateprov');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.postalcode', 'businessInfo/postal/postalcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.country', 'businessInfo/postal/country');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.postal.organization', 'businessInfo/postal/organization');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.intcode', 'businessInfo/telecom/telephone/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.loccode', 'businessInfo/telecom/telephone/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.number', 'businessInfo/telecom/telephone/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.ext', 'businessInfo/telecom/telephone/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.telephone.comment', 'businessInfo/telecom/telephone/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.intcode', 'businessInfo/telecom/fax/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.loccode', 'businessInfo/telecom/fax/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.number', 'businessInfo/telecom/fax/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.ext', 'businessInfo/telecom/fax/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.fax.comment', 'businessInfo/telecom/fax/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.intcode', 'businessInfo/telecom/mobile/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.loccode', 'businessInfo/telecom/mobile/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.number', 'businessInfo/telecom/mobile/number');
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.ext', 'businessInfo/telecom/mobile/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.mobile.comment', 'businessInfo/telecom/mobile/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.intcode', 'businessInfo/telecom/pager/intcode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.loccode', 'businessInfo/telecom/pager/loccode');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.number', 'businessInfo/telecom/pager/number');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.ext', 'businessInfo/telecom/pager/ext');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.telecom.pager.comment', 'businessInfo/telecom/pager/comment');
    
INSERT INTO  PRODUCER_GLOBAL_PROFILE_MAP
    (PORTALID, PORTLETATTR, WSRPATTR) VALUES 
    ('portal1', 'user.business-info.online.email', 'businessInfo/online/email');

INSERT INTO PRODUCER_GLOBAL_DATA (PORTALID, PRODUCERDISABLE) VALUES ('portal1', 0);
