#!/bin/bash

# This file is deployed into {casfw_home}/bin so let's walk up
# the directory hierarchy to get to CASFW root directory
CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)"

umask u=rwx,g=rx,o=rx

source ${CASFW_HOME}/bin/.casfwrc

vignette_home="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"
trust_store_path=${CASFW_HOME}/etc/security/java6_cacerts
vignette_log_dir=${CASFW_HOME}/var/log/vignette-portal

if ${using_cygwin}; then
    trust_store_path="$(cygpath -am ${trust_store_path})"
    vignette_home="$(cygpath -am ${vignette_home})"
    vignette_log_dir="$(cygpath -am ${vignette_log_dir})"
fi

default_catalina_opts="-Djava.awt.headless=true -Dcom.vignette.portal.installdir.path=${vignette_home} -Djavax.net.ssl.trustStore=${trust_store_path} -Dlog_dir=${vignette_log_dir}"
default_memory_opts="-Xms512m -Xmx1024m -XX:MaxPermSize=512m"

# Find the latest tomcat 6
export CATALINA_HOME="$(cd $(ls -d ${CASFW_HOME}/software/apache-tomcat-6.* | tail -n1) && pwd -P)"
export CATALINA_BASE=${CASFW_HOME}/etc/tomcat-portal
if [ -z "${PORTAL_CATALINA_OPTS}" ]; then
    export CATALINA_OPTS="${default_catalina_opts} ${default_memory_opts}"
else
    export CATALINA_OPTS="${default_catalina_opts} ${PORTAL_CATALINA_OPTS}"
fi
export CATALINA_PID=${CASFW_HOME}/var/tomcat-portal.pid
export DISPLAY=

${CATALINA_HOME}/bin/catalina.sh $*
