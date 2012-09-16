#!/bin/bash

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

# Setup DB for portal
${CASFW_HOME}/bin/setupPrimaryPortalNode.sh

# Setup DB for persona
${CASFW_HOME}/bin/setupPersonaDatabase.sh

# Create /opt/casfw/current folder if it doesn't exist
CASFW_HOME_CURRENT="${CASFW_HOME}/../current"     
if [[ ! -d ${CASFW_HOME_CURRENT} ]]; then
	mkdir -p ${CASFW_HOME_CURRENT}
fi
# Create the symbol link pointing to the current installer
ln -sf ${CASFW_HOME} ${CASFW_HOME_CURRENT}/portal
# Create init.d folder under ${CASFW_HOME}
mkdir -p ${CASFW_HOME}/init.d
# Create the symbol link pointing to the actual shell script
ln -sf ${CASFW_HOME}/bin/tomcat-portal.sh ${CASFW_HOME}/init.d/tomcat-portal.sh

#Fix permissions for all other files
echo "Setting other permissions"

# In general we want:
# - user to read+write+browse (i.e. execute for directories, and if execute for files was already there we are fine),
# - group to read+browse,
# - others to do nothing
# - everybody to have read+browse access to log directory
chmod -R u+rwX,g=rX,o= ${CASFW_HOME}
chmod a+rX ${CASFW_HOME}
chmod -R a+rX ${CASFW_HOME}/var
chmod -R a+rX ${CASFW_HOME}/var/log

# allow other installers using this one as base to provide their additional steps
if [ -r ${CASFW_HOME}/bin/.post-install_custom.sh ]; then
    source ${CASFW_HOME}/bin/.post-install_custom.sh
fi

# Print message about URL at which Portal runs
tomcat_portal_http_port="$(get_property_value "${CASFW_HOME}/etc/casfw.properties" "tomcat_portal_connector_http_port")"
echo
echo "Starting Tomcat with Vignette Portal at http://$(hostname):${tomcat_portal_http_port}/portal/"
${CASFW_HOME}/bin/tomcat-portal.sh start


#echo
#echo "Please check ${CASFW_HOME}/README.txt for details of the components included"
#echo "in this installation."