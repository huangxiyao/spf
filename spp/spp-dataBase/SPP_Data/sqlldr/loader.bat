@ECHO OFF
echo.
echo Don't forget to remove the :: in front of the line 
echo of the environment you want to update.
echo.

REM -----------------------------------------
REM     DEV Environment
REM -----------------------------------------
::sqlldr userid=sppdev_user/sppdev_user@SPPD control=SPP_CONTACT_TOPICS.ctl log=SPP_CONTACT_TOPICS.log
::sqlldr userid=sppdev_user/sppdev_user@SPPD control=SPP_CONTACT_LANGUAGE.ctl log=SPP_CONTACT_LANGUAGE.log
::sqlldr userid=sppdev_user/sppdev_user@SPPD control=SPP_CONTACT_FAULTS.ctl log=SPP_CONTACT_FAULTS.log

REM -----------------------------------------
REM     FT Environment
REM -----------------------------------------
::sqlldr userid=spp_user/spp_user@SPPD control=SPP_CONTACT_TOPICS.ctl log=SPP_CONTACT_TOPICS.log
::sqlldr userid=spp_user/spp_user@SPPD control=SPP_CONTACT_LANGUAGE.ctl log=SPP_CONTACT_LANGUAGE.log
::sqlldr userid=spp_user/spp_user@SPPD control=SPP_CONTACT_FAULTS.ctl log=SPP_CONTACT_FAULTS.log

REM -----------------------------------------
REM     ITG Environment
REM -----------------------------------------
::sqlldr userid=spp_user/spp_user@SPPI control=SPP_CONTACT_TOPICS.ctl log=SPP_CONTACT_TOPICS.log
::sqlldr userid=spp_user/spp_user@SPPI control=SPP_CONTACT_LANGUAGE.ctl log=SPP_CONTACT_LANGUAGE.log
::sqlldr userid=spp_user/spp_user@SPPI control=SPP_CONTACT_FAULTS.ctl log=SPP_CONTACT_FAULTS.log

REM -----------------------------------------
REM     PRO Environment
REM -----------------------------------------
::sqlldr userid=spp_user/spp_user@SPPP control=SPP_CONTACT_TOPICS.ctl log=SPP_CONTACT_TOPICS.log
::sqlldr userid=spp_user/spp_user@SPPP control=SPP_CONTACT_LANGUAGE.ctl log=SPP_CONTACT_LANGUAGE.log
::sqlldr userid=spp_user/spp_user@SPPP control=SPP_CONTACT_FAULTS.ctl log=SPP_CONTACT_FAULTS.log

