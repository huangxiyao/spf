#!/bin/bash

source .access_script.rc 2>/dev/null

read -r -d '' VITALS_MESSAGE <<end-of-message
############################## V  I  T  A  L  S ##################################
##   Server notes:
##      This HPUX server is hosting PRO SPF apache web server.
##   Application accounts:
##      sp
##   IMPORTANT COMMANDS:
##      pbrun /opt/webhost/dwheng/bin/start_apache -all
##      pbrun /opt/webhost/dwheng/bin/stop_apache -all
##      ps -efx | grep httpd
##   CONFIGURATIONS:
##      /opt/webhost/DWH-ATHP-ANON/apache/conf/httpd.conf
##      /opt/webhost/DWH-ATHP-AUTH/apache/conf/httpd.conf
##      /opt/webhost/DWH-HPP-ANON/apache/conf/httpd.conf
##      /opt/webhost/DWH-HPP-AUTH/apache/conf/httpd.conf
##      /opt/webhost/sp/DWH-COMMON/apache/conf/httpd.conf
##      /opt/webhost/sp/DWH-ATHP-ANON/apache/conf/httpd.conf
##      /opt/webhost/sp/DWH-ATHP-AUTH/apache/conf/httpd.conf
##      /opt/webhost/sp/DWH-HPP-ANON/apache/conf/httpd.conf
##      /opt/webhost/sp/DWH-HPP-AUTH/apache/conf/httpd.conf
##   LOG FILE LOCATIONS:
##      /opt/webhost/DWH-ATHP-ANON/apache/logs
##      /opt/webhost/DWH-ATHP-AUTH/apache/logs
##      /opt/webhost/DWH-HPP-ANON/apache/logs
##      /opt/webhost/DWH-HPP-AUTH/apache/logs
##################################################################################
''
end-of-message

if [[ -z "${run_access_script}" && $# -eq 0 ]]; then
    echo "${VITALS_MESSAGE}"
fi
eval "${run_access_script}" ssh -t g2u0870.austin.hp.com "$@"
