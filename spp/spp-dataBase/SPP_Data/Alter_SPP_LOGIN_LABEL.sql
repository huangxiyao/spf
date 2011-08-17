--Alter Script for SPP_LOGIN_LABEL


alter table spp_login_label drop constraint SPP_LOGIN_LABEL
alter table spp_login_label add  (SITE_NAME VARCHAR2(100))

COMMIT;