#!/bin/sh
CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

source ${CASFW_HOME}/bin/.casfwrc

# Create Tomcat instance
TOMCAT_HOME="$(cd $(ls -d ${CASFW_HOME}/software/apache-tomcat-6.* | tail -n1) && pwd -P)"

TOMCAT_PORTAL_HOME="${CASFW_HOME}/etc/tomcat-portal"
echo "Creating Tomcat instance for ${TOMCAT_HOME}"
echo "in ${TOMCAT_PORTAL_HOME}"

cp -R ${TOMCAT_HOME}/conf ${TOMCAT_PORTAL_HOME}

mkdir -p ${CASFW_HOME}/var/log/tomcat-portal
ln -sf ${CASFW_HOME}/var/log/tomcat-portal ${TOMCAT_PORTAL_HOME}/logs

if ${using_cygwin}; then
    mkdir ${TOMCAT_PORTAL_HOME}/temp
    mkdir ${TOMCAT_PORTAL_HOME}/webapps
    mkdir ${TOMCAT_PORTAL_HOME}/work
else
    mkdir -p ${CASFW_HOME}/var/tomcat-portal/temp
    mkdir -p ${CASFW_HOME}/var/tomcat-portal/webapps
    mkdir -p ${CASFW_HOME}/var/tomcat-portal/work

    # ${CASFW_HOME}/etc/tomcat-portal should already exist as we will copy conf files there
    ln -sf ${CASFW_HOME}/var/tomcat-portal/temp ${TOMCAT_PORTAL_HOME}/temp
    ln -sf ${CASFW_HOME}/var/tomcat-portal/webapps ${TOMCAT_PORTAL_HOME}/webapps
    ln -sf ${CASFW_HOME}/var/tomcat-portal/work ${TOMCAT_PORTAL_HOME}/work
fi 

# Portal config dir
VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"
ln -sf ${VIGNETTE_HOME}/config ${CASFW_HOME}/etc/vignette-portal

# Setup other /var directories 
mkdir -p ${CASFW_HOME}/var/log/vignette-portal
mkdir -p ${CASFW_HOME}/var/data

# Fix permissions
echo "Setting permissions"

# In general we want:
# - user to read+write+browse (i.e. execute for directories, and if execute for files was already there we are fine), 
# - group to read+browse, 
# - others to do nothing
chmod -R u+rwX,g=rX,o= ${CASFW_HOME}

# And now we explicitely set 'execute' permissions for files we know we need
chmod ug+x ${CASFW_HOME}/bin/*.sh
for app_dir in $(ls -d ${CASFW_HOME}/software/apache-tomcat-*); do
    chmod ug+x ${app_dir}/bin/*.sh
done 
for app_dir in $(ls -d ${CASFW_HOME}/software/vignette-portal-*); do
    chmod ug+x ${app_dir}/bin/*.sh
done 

# Update Java "cacerts" file with the one that we ship and which contains HP Certificate Authority
echo "Installing HP Certificate Authority"
for java_dir in $(ls -d ${CASFW_HOME}/software/oracle-java-1.5.* 2>/dev/null); do
    cp ${java_dir}/jre/lib/security/cacerts ${java_dir}/jre/lib/security/cacerts.ORIGINAL
    cp ${CASFW_HOME}/etc/security/java5_cacerts ${java_dir}/jre/lib/security/cacerts
done 


