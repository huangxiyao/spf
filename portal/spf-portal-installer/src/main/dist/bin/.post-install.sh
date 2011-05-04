#!/bin/sh

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

echo "### TODO: uncomment setupPrimaryPortalNode.sh in .post-install.sh"
#${CASFW_HOME}/bin/setupPrimaryPortalNode.sh

# Print message about URL at which Portal runs
tomcat_portal_http_port="$(get_property_value "${CASFW_HOME}/etc/casfw.properties" "tomcat_portal_connector_http_port")"
echo
echo "Starting Tomcat with Vignette Portal at http://$(hostname):${tomcat_portal_http_port}/portal/"
echo "### TODO: uncomment 'tomcat-portal.sh start' in .post-install.sh"
#${CASFW_HOME}/bin/tomcat-portal.sh start



echo
echo "Please check ${CASFW_HOME}/README.txt for details of the components included"
echo "in this installation."