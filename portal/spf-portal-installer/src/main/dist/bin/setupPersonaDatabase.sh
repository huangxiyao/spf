#!/bin/sh

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"

persona_db_driver_class="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_driver_class")"
persona_db_url="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_url")"
persona_db_username="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_username")"
persona_db_password="$(get_property_value "${CASFW_HOME}/etc/persona/persona_database.properties" "persona_db_password")"


echo "Setting up Persona database ${persona_db_url}"
# Will use Vignette-provided tools to do that
pushd ${VIGNETTE_HOME}/bin

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

