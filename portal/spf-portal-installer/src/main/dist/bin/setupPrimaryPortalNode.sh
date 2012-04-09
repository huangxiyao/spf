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

# First check if portal database already exists and then create it only if not
vignette_database_setup_required=true

echo "Checking Vignette Portal database"
# Will use Vignette-provided tools to do that
pushd ${VIGNETTE_HOME}/bin

db_check_sql_path="${CASFW_HOME}/var/vap_db_check.sql"
echo "select count(*) from users;" > "${db_check_sql_path}"
if ${using_cygwin}; then
    db_check_sql_path="$(cygpath -am ${db_check_sql_path})"
fi

sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.SqlScriptRunner \
 "--driver=${vignette_db_driver_class}" \
 "--jdbcUrl=${vignette_db_url}" \
 "--username=${vignette_db_username}" \
 "--password=${vignette_db_password}" \
 "--scriptPath=${db_check_sql_path}" \
 2>&1 | grep -q -i "exception"

# If the database is not set up the previous command will find an "exception" message and return 0.
# Therefore if we get non-0 there was no exception so the database already exists
if [ $? -ne 0 ]; then
    vignette_database_setup_required=false
fi
rm "${db_check_sql_path}"


if ${vignette_database_setup_required}; then
    ##### Setup portal database #####

    echo "Setting up Vignette Portal database ${vignette_db_url}"

    echo "Creating portal database objects"
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


    echo "Applying SPF database extensions"
    alter_sql_path="${VIGNETTE_HOME}/config/spf_vap_alter_users.sql"
    if [[ ${vignette_db_driver_class} =~ "derby" ]]; then
        alter_sql_path="${alter_sql_path}.derby"
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
     1>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
     2>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

    last_exit_code=$?
    if [ ${last_exit_code} -ne 0 ]; then
        echo "Creating custom SPF columns failed with code ${last_exit_code}."
        echo "Aborting."
        popd
        exit ${last_exit_code}
    fi


    ##### Setup admin account #####

    echo "Creating administrator account '${vignette_admin_username}'"
    #sh ./create_first_admin_account.sh ${vignette_admin_username} ${vignette_admin_password} \
    # 1>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.out \
    # 2>>${CASFW_HOME}/var/log/vignette-portal/setupPrimaryPortalNode.err

    sh ./runs_with_classpath.sh com.hp.it.spf.sso.portal.AdminUserTool \
     ${vignette_admin_username} hpp_realm1 \
     1>>${CASFW_HOME}/var/log/vignette-portal/AdminUserTool.out \
     2>>${CASFW_HOME}/var/log/vignette-portal/AdminUserTool.err

    last_exit_code=$?
    if [ ${last_exit_code} -ne 0 ]; then
        echo "Administration account creation failed with code ${last_exit_code}."
        echo "Aborting."
        popd
        exit ${last_exit_code}
    fi


    ##### Bootstrap portal #####

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


    ##### Set admin account's profile ID #####
    # This needs to be done only after bootstrap has run as create_first_admin_account does not update DB
    # but only create VAP/config/elxo.dat file.

    echo "Setting administrator account's profile ID"
    update_sql_path="${VIGNETTE_HOME}/config/spf_vap_update_admin.sql"
    if [[ ${vignette_db_driver_class} =~ "derby" ]]; then
        update_sql_path="${update_sql_path}.derby"
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
else
    echo "Vignette Portal database ${vignette_db_url} already exists - skipping its setup"
fi

##### Register remote portlet servers #####

remote_portlet_servers_path="${VIGNETTE_HOME}/config/spf_remote_portlet_servers.xml"
if ${using_cygwin}; then
    remote_portlet_servers_path="$(cygpath -am ${remote_portlet_servers_path})"
fi

echo "Registering remote portlet servers defined in ${remote_portlet_servers_path}"
sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.RegisterRemotePortletServers \
 "${remote_portlet_servers_path}" \
 1>>${CASFW_HOME}/var/log/vignette-portal/RegisterRemotePortletServers.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/RegisterRemotePortletServers.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Registering remote portlet servers failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi


##### Register local portlet applications #####

portlet_applications_path="${VIGNETTE_HOME}/config/spf_local_portlet_applications.xml"
if ${using_cygwin}; then
    portlet_applications_path="$(cygpath -am ${portlet_applications_path})"
fi

echo "Registering portlet applications defined in ${portlet_applications_path}"
sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.RegisterPortletApplications \
 "${portlet_applications_path}" \
 1>>${CASFW_HOME}/var/log/vignette-portal/RegisterPortletApplications.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/RegisterPortletApplications.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Registering portlet applications failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi


##### Register portal-supported languages/locales #####

supported_languages_path="${CASFW_HOME}/etc/resources_global/site_locale_support.properties"
if ${using_cygwin}; then
    supported_languages_path="$(cygpath -am ${supported_languages_path})"
fi

echo "Adding supported languages defined in ${supported_languages_path}"
sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.RegisterSupportedLanguages \
 "${supported_languages_path}" \
 1>>${CASFW_HOME}/var/log/vignette-portal/RegisterSupportLanguages.out \
 2>>${CASFW_HOME}/var/log/vignette-portal/RegisterSupportLanguages.err

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    echo "Registering support languages failed with code ${last_exit_code}."
    echo "Aborting."
    popd
    exit ${last_exit_code}
fi


##### Import CAR files #####

echo "Y" > ${CASFW_HOME}/var/accept_components_import.txt

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

        ./ImportExportTool.sh -import ${car_file} < ${CASFW_HOME}/var/accept_components_import.txt \
        1>>${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.out \
        2>>${CASFW_HOME}/var/log/vignette-portal/ImportExportTool.err

        last_exit_code=$?
        if [ ${last_exit_code} -ne 0 ]; then
            echo "Importing file ${car_file} failed with code ${last_exit_code}."
            echo "Aborting."
            popd
            rm ${CASFW_HOME}/var/accept_components_import.txt
            exit ${last_exit_code}
        fi
    done
}

echo "Importing portal sites"
import_car_files $(ls ${CASFW_HOME}/software/*.car | grep "\-site-")

# We imported those already during bootstrap, but let's reimport them to ensure that if the site CARs
#  came with older components the newer ones packaged in these CARs will be used.
echo "Importing remaining component archives"
import_car_files $(ls ${CASFW_HOME}/software/*.car | grep -v "\-site-")

rm ${CASFW_HOME}/var/accept_components_import.txt

##### Add anonymous users #####

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

popd

