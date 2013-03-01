#!/bin/sh

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

. ${CASFW_HOME}/bin/.casfwrc

# Setup DB for portal
${CASFW_HOME}/bin/setupPrimaryPortalNode.sh

# Setup DB for persona
${CASFW_HOME}/bin/setupPersonaDatabase.sh

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
    . ${CASFW_HOME}/bin/.post-install_custom.sh
fi

# Print message about URL at which Portal runs
echo
echo "Please configure WebLogic with Vignette Portal as post installation."
