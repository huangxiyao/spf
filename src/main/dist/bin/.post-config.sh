#!/bin/sh

CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)"

. ${CASFW_HOME}/bin/.casfwrc

VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"
portal_db_driver_class="$(get_property_value "${CASFW_HOME}/etc/casfw.properties" "portal_db_driver_class")"

#if [[ ${portal_db_driver_class} =~ "derby" ]]; then
if echo ${portal_db_driver_class} | grep -q -e "derby" ; then
    # Apply the spf patch to fix a bug in Vignette 8.2
    echo "Applying SPF patch for Derby database"
    cp -R ${VIGNETTE_HOME}/spf-patch/* ${VIGNETTE_HOME}/portal/WEB-INF/classes/
else
    spf_patch_file_path="${VIGNETTE_HOME}/portal/WEB-INF/classes/com/epicentric/entity/datasource/internal/sql/SQLEntityQuery.class"
    if [ -f "${spf_patch_file_path}" ]; then
        echo "Cleaning SPF patch for non-Derby database"
        rm -f "${spf_patch_file_path}"
    fi
fi
