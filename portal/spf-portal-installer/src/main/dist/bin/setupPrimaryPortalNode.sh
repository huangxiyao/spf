#!/bin/bash

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

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

echo "Creating custom SPF columns"
alter_sql_path="${VIGNETTE_HOME}/config/spf_vap_alter_users.sql"
if [[ ${vignette_db_driver_class} =~ "derby" ]]; then
    alter_sql_path=${alter_sql_path}.derby
fi
if ${using_cygwin}; then
    alter_sql_path="$(cygpath -am ${alter_sql_path})"
fi

sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.SqlScriptRunner \
 "--driver=${vignette_db_driver_class}" \
 "--jdbcUrl=${vignette_db_url}" \
 "--username=${vignette_db_username}" \
 "--password=${vignette_db_password}" \
 "--scriptPath=${alter_sql_path}" \
 1>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
 2>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Creating custom SPF columns failed with code ${last_exit_code}."
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

echo "Adding anonymous users"
user_data_path="${VIGNETTE_HOME}/config/spf_anonymous_users.txt"
if ${using_cygwin}; then
    user_data_path="$(cygpath -am ${user_data_path})"
fi
sh ./runs_with_classpath.sh com.hp.it.spf.sso.portal.AnonUsersImport  "hpp_realm1" "${user_data_path}" \
 1>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Adding anonymous users failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi

echo "Updating administrator account ${vignette_admin_username} profile id"
update_sql_path="${VIGNETTE_HOME}/config/spf_vap_update_admin.sql"
if [[ ${vignette_db_driver_class} =~ "derby" ]]; then
    update_sql_path=${update_sql_path}.derby
fi
if ${using_cygwin}; then
    update_sql_path="$(cygpath -am ${update_sql_path})"
fi

sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.SqlScriptRunner \
 "--driver=${vignette_db_driver_class}" \
 "--jdbcUrl=${vignette_db_url}" \
 "--username=${vignette_db_username}" \
 "--password=${vignette_db_password}" \
 "--scriptPath=${update_sql_path}" \
 1>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Updating administrator account profile id failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi

popd

