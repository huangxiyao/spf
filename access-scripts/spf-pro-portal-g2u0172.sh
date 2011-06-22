#!/bin/bash

source .access_script.rc 2>/dev/null

read -r -d '' VITALS_MESSAGE <<end-of-message
############################## V  I  T  A  L  S ##################################
##   Server notes:
##      This HPUX server is hosting PRO SPF Vignette Portal.
##   Application accounts:
##      sp
##   IMPORTANT COMMANDS:
##      ps -efx | grep spf
##   CONFIGURATIONS:
##      /opt/sasuapps/sp/vignetteSPFB/portal742/config/properties.txt
##      /opt/sasuapps/sp/global_resources
##      /opt/sasuapps/sp/domain/spfB/servers/spfB7/resources
##      /opt/sasuapps/sp/domain/spfB/servers/spfB8/resources
##   LOG FILE LOCATIONS:
##      /opt/sasuapps/sp/vignetteSPFB/portal742/logs
##      /opt/sasuapps/sp/domain/spfB/servers/spfB7/logs
##      /opt/sasuapps/sp/domain/spfB/servers/spfB8/logs
##################################################################################
''
end-of-message

if [[ -z "${run_access_script}" && -z "${RUN_COMMAND_ONLY}" ]]; then
    echo "${VITALS_MESSAGE}"
fi
eval "${run_access_script}" ssh -t g2u0172.austin.hp.com "$@"
