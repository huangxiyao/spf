#!/bin/bash

source .access_script.rc 2>/dev/null

read -r -d '' VITALS_MESSAGE <<end-of-message
############################## V  I  T  A  L  S ##################################
##   Server notes:
##      This HPUX server is hosting ITFT SPF Vignette Portal.
##   Application accounts:
##      sp
##   IMPORTANT COMMANDS:
##      ps -efx | grep -i "Name=sp"
##   CONFIGURATIONS:
##      /opt/sasuapps/sp/vignette/portal742/config/properties.txt
##      /opt/sasuapps/sp/global_resources
##      /opt/sasuapps/sp/domain/sp/servers/SP1/resources
##      /opt/sasuapps/sp/domain/sp/servers/sp2/resources
##   LOG FILE LOCATIONS:
##      /opt/sasuapps/sp/vignette/portal742/logs
##      /opt/sasuapps/sp/domain/sp/servers/SP1/logs
##      /opt/sasuapps/sp/domain/sp/servers/sp2/logs
##################################################################################
''
end-of-message

if [[ -z "${run_access_script}" && $# -eq 0 ]]; then
    echo "${VITALS_MESSAGE}"
fi
eval "${run_access_script}" ssh -t g1u1681.austin.hp.com "$@"
