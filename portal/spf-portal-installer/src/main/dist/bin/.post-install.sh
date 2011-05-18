#!/bin/sh

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

# Setup DB for portal
${CASFW_HOME}/bin/setupPrimaryPortalNode.sh

echo "Importing remaining component archives"
VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"
echo "Y" > ${CASFW_HOME}/var/accept_site_import.txt
pushd ${VIGNETTE_HOME}/bin
for carFile in $(ls ${CASFW_HOME}/software/*.car); do
    if ${using_cygwin}; then
        carFile="$(cygpath -am ${carFile})"
    fi
    echo "Importing ${carFile}"
    ./ImportExportTool.sh -import ${carFile} < ${CASFW_HOME}/var/accept_site_import.txt \
    1>${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.out \
    2>${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.err

    last_exit_code=$?
    if [ ${last_exit_code} -ne 0 ]; then
        echo "Importing file ${carFile} failed with code ${last_exit_code}."
        echo "Aborting."
        popd
        rm ${CASFW_HOME}/var/accept_site_import.txt
        exit ${last_exit_code}
    fi
done
popd
rm ${CASFW_HOME}/var/accept_site_import.txt

# Setup DB for persona
${CASFW_HOME}/bin/setupPersonaDatabase.sh


# Print message about URL at which Portal runs
tomcat_portal_http_port="$(get_property_value "${CASFW_HOME}/etc/casfw.properties" "tomcat_portal_connector_http_port")"
echo
echo "Starting Tomcat with Vignette Portal at http://$(hostname):${tomcat_portal_http_port}/portal/"
${CASFW_HOME}/bin/tomcat-portal.sh start



echo
echo "Please check ${CASFW_HOME}/README.txt for details of the components included"
echo "in this installation."