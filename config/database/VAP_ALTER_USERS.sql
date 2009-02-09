/***************************************************************
* This SQL script will alter VAP USERS table to add additional
* fields that are supported by SPF
*  
* Date Crated: Feb/4/2009
* Date Last Modified: Feb/4/2009
* Created by : Liu, Ye
****************************************************************/

ALTER TABLE USERS ADD PROFILE_ID VARCHAR(64) NULL;
ALTER TABLE USERS ADD LAST_CHANGE_DATE DATE NULL;
ALTER TABLE USERS ADD LAST_LOGIN_DATE DATE NULL;
ALTER TABLE USERS ADD SP_TIMEZONE VARCHAR(64) NULL;

commit;

quit;


