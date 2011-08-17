alter table SPP_GROUP 
    add constraint SPP_GROUP_SITE_FK foreign key (SITE_ID) 
       references SPP_SITE (ID)
/

alter table SPP_RESOURCE_HISTORY
    add constraint SPP_RESOURCE_SITE_FK foreign key  (SITE_ID)
       references SPP_SITE (ID)
/

alter table SPP_ESERVICE
    add constraint SPP_ESERVICE_SITE_FK foreign key  (SITE_ID)
       references SPP_SITE (ID)
/

alter table SPP_ESERVICE
    add constraint SPP_ESERVICE_PARAMETERSET_FK foreign key  (STANDARDPARAMETERSET_ID)
       references SPP_STANDARD_PARAMETERSET (ID)
/

alter table SPP_STANDARD_PARAMETERSET
    add constraint SPP_STD_PARAMSET_SITE_FK foreign key  (SITE_ID)
       references SPP_SITE (ID)
/

alter table SPP_ESERVICE_PARAMETER
    add constraint SPP_PARAMETER_PARAMETERSET_FK foreign key  (STANDARD_PARAMETERSET_ID)
       references SPP_STANDARD_PARAMETERSET (ID)
/

alter table SPP_ESERVICE_PARAMETER
    add constraint SPP_PARAMETER_ESERVICE_FK foreign key  (ESERVICE_ID)
       references SPP_ESERVICE (ID)
/


ALTER TABLE spp_custom_error ADD CONSTRAINT spp_custom_error_code_fk FOREIGN KEY
(
    error_code
)
 REFERENCES spp_error_code(
    error_code
)
/

ALTER TABLE spp_custom_error ADD CONSTRAINT spp_custom_error_portal_fk FOREIGN KEY
(
    portal
)
 REFERENCES spp_site(
    name
)
/

ALTER TABLE spp_workflow_error ADD CONSTRAINT spp_workflow_error_code_fk FOREIGN KEY
(
    error_code
)
 REFERENCES spp_error_code(
    error_code
)
/

ALTER TABLE spp_workflow_error ADD CONSTRAINT spp_workflow_error_portal_fk FOREIGN KEY
(
    portal
)
 REFERENCES spp_site(
    name
)
/
