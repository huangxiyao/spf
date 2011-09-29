#!/bin/bash

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"

persona_db_driver_class="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_driver_class")"
persona_db_url="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_url")"
persona_db_username="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_username")"
persona_db_password="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_password")"

# First check if persona database already exists and then create it only if not
persona_database_setup_required=true

echo "Checking Persona database"
# Will use Vignette-provided tools to do that
pushd ${VIGNETTE_HOME}/bin

db_check_sql_path="${CASFW_HOME}/var/persona_db_check.sql"
echo "select count(*) from app;" > "${db_check_sql_path}"
if ${using_cygwin}; then
    db_check_sql_path="$(cygpath -am ${db_check_sql_path})"
fi

sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.SqlScriptRunner \
 "--driver=${persona_db_driver_class}" \
 "--jdbcUrl=${persona_db_url}" \
 "--username=${persona_db_username}" \
 "--password=${persona_db_password}" \
 "--scriptPath=${db_check_sql_path}" \
 2>&1 | grep -q -i "exception"

# If the database is not set up the previous command will find an "exception" message and return 0.
# Therefore if we get non-0 there was no exception so the database already exists
if [ $? -ne 0 ]; then
    persona_database_setup_required=false
fi
rm "${db_check_sql_path}"


if ${persona_database_setup_required}; then

    echo "Setting up Persona database ${persona_db_url}"
    # Will use Vignette-provided tools to do that

    alter_sql_path="${CASFW_HOME}/etc/persona/persona.ddl.sql"
    if ${using_cygwin}; then
        alter_sql_path="$(cygpath -am ${alter_sql_path})"
    fi

    echo "Creating Persona tables"
    sh ./runs_with_classpath.sh com.hp.it.spf.misc.portal.SqlScriptRunner \
     "--driver=${persona_db_driver_class}" \
     "--jdbcUrl=${persona_db_url}" \
     "--username=${persona_db_username}" \
     "--password=${persona_db_password}" \
     "--scriptPath=${alter_sql_path}" \
     1>${CASFW_HOME}/var/log/setupPersonaDatabase.out \
     2>${CASFW_HOME}/var/log/setupPersonaDatabase.err

    last_exit_code=$?
    if [ ${last_exit_code} -ne 0 ]; then
        echo "Creating Persona tables with code ${last_exit_code}."
        echo "Aborting."
        popd
        exit ${last_exit_code}
    fi

else
    echo "Persona database ${persona_db_url} already exists - skipping its setup"
fi

popd