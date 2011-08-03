#!/bin/bash

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

# Setup DB for portal
${CASFW_HOME}/bin/setupPrimaryPortalNode.sh

VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"
echo "Y" > ${CASFW_HOME}/var/accept_site_import.txt
pushd ${VIGNETTE_HOME}/bin

function import_car_files {
    local current_time=

    for car_file in "$@"; do
        if ${using_cygwin}; then
            car_file="$(cygpath -am ${car_file})"
        fi
        echo "Importing ${car_file}"

        current_time="$(date '+%Y-%m-%d %H:%M:%S')"

        echo "==== ${current_time} - Importing ${car_file} ====" >> ${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.out
        echo "==== ${current_time} - Importing ${car_file} ====" >> ${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.err

        ./ImportExportTool.sh -import ${car_file} < ${CASFW_HOME}/var/accept_site_import.txt \
        1>>${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.out \
        2>>${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.err

        last_exit_code=$?
        if [ ${last_exit_code} -ne 0 ]; then
            echo "Importing file ${car_file} failed with code ${last_exit_code}."
            echo "Aborting."
            popd
            rm ${CASFW_HOME}/var/accept_site_import.txt
            exit ${last_exit_code}
        fi
    done
}

remote_portlet_servers="${VIGNETTE_HOME}/config/spf_remote_portlet_servers.xml"
if ${using_cygwin}; then
    remote_portlet_servers="$(cygpath -am ${remote_portlet_servers})"
fi

echo "Registering remote portlet servers defined in ${remote_portlet_servers}"
sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.RegisterRemotePortletServers \
 ${remote_portlet_servers} \
1>>${CASFW_HOME}/var/log/vignette-portal/registerRemotePortletServers.out \
2>>${CASFW_HOME}/var/log/vignette-portal/registerRemotePortletServers.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Registering remote portlet servers failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi

portlet_applications="${VIGNETTE_HOME}/config/spf_local_portlet_applications.xml"
if ${using_cygwin}; then
    portlet_applications="$(cygpath -am ${portlet_applications})"
fi
echo "Registering portlet applications defined in ${portlet_applications}"
sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.RegisterPortletApplications \
 ${portlet_applications} \
1>>${CASFW_HOME}/var/log/vignette-portal/registerPortletApplications.out \
2>>${CASFW_HOME}/var/log/vignette-portal/registerPortletApplications.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Registering portlet applications failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi

echo "Importing portal sites"
import_car_files $(ls ${CASFW_HOME}/software/*.car | grep "\-site-")

# We imported those already during bootstrap, but let's reimport them to ensure that if the site CARs
#  came with older components the newer ones packaged in these CARs will be used.
echo "Importing remaining component archives"
import_car_files $(ls ${CASFW_HOME}/software/*.car | grep -v "\-site-")

popd
rm ${CASFW_HOME}/var/accept_site_import.txt

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