#!/bin/bash

source .access_script.rc 2>/dev/null

read -r -d '' VITALS_MESSAGE <<end-of-message
############################## V  I  T  A  L  S ##################################
##   Server notes:
##      This HPUX server is hosting ITFT SPF OpenPortal Producer and SPF Persona.
##   Application accounts:
##      sp
##   IMPORTANT COMMANDS:
##      ps -efx | grep "Name=sp"
##   CONFIGURATIONS:
##      /opt/sasuapps/sp/openportal753/config
##   LOG FILE LOCATIONS:
##      /opt/sasuapps/sp/openportal753/portlet-container/logs
##      /opt/sasuapps/sp/domain/sp/servers/sp1/logs
##################################################################################
''
end-of-message

if [[ -z "${run_access_script}" && $# -eq 0 ]]; then
    echo "${VITALS_MESSAGE}"
fi
eval "${run_access_script}" ssh -t g1u0753c.austin.hp.com "$@"
