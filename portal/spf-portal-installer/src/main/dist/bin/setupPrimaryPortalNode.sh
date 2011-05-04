#!/bin/sh

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

echo "Setting up Vignette Portal"

VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"

vignette_db_driver_class="$(get_property_value "${VIGNETTE_HOME}/config/properties.txt" "default.db.driver")"
vignette_db_url="$(get_property_value "${VIGNETTE_HOME}/config/properties.txt" "default.db.url")"
vignette_db_username="$(get_property_value "${VIGNETTE_HOME}/config/properties.txt" "default.db.user")"
vignette_db_password="$(get_property_value "${VIGNETTE_HOME}/config/properties.txt" "default.db.password")"

vignette_admin_username="$(get_property_value "${CASFW_HOME}/etc/casfw.properties" "portal_admin_username")"
vignette_admin_password="$(get_property_value "${CASFW_HOME}/etc/casfw.properties" "portal_admin_password")"

echo "Setting up Vignette Portal database ${vignette_db_url}"
# Will use Vignette-provided tools to do that
pushd ${VIGNETTE_HOME}/bin

echo "Creating portal tables"
sh ./runs_with_classpath.sh com.vignette.tableinstaller.tools.TableInstaller \
 "--driver=${vignette_db_driver_class}" \
 "--jdbc-url=${vignette_db_url}" \
 "--user=${vignette_db_username}" \
 "--password=${vignette_db_password}" \
 create ../system/dbSetup/tables.xml \
 1>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
 2>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Table creation failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi


echo "Creating administrator account ${vignette_admin_username}"
sh ./create_first_admin_account.sh ${vignette_admin_username} ${vignette_admin_password} \
 1>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Administration account creation failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi

 
echo "Bootstraping portal application"
sh ./war_tool.sh portal.war \
 1>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Bootstraping portal application failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi

popd

echo "#### TODO: add anonymous users import #####"

