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

# Overlay spf-portal onto Vignette/portal
echo "Preparing SPF artifacts"

# first spf-portal-*.war - let's copy it over Vignette portal directory so Vignette bootstrap picks SPF classes too
spf_war="$(ls ${CASFW_HOME}/software/spf-portal-*.war | tail -n1)"
pushd ${VIGNETTE_HOME}/portal
# Let's make sure we can execute "jar" as the permissions are only fixed later
chmod u+x ${JAVA_HOME}/bin/jar
if ${using_cygwin}; then
    ${JAVA_HOME}/bin/jar xf "$(cygpath -aw "${spf_war}")"
else
    ${JAVA_HOME}/bin/jar xf "${spf_war}"
fi
popd

# Since spf-portal.war is not useful and we have its files already in Vignette let's remove it and 
# create a symbolic link to the target directory in Vignette
spf_war_basename="$(basename ${spf_war} .war)"
rm "${spf_war}"
ln -sf ${VIGNETTE_HOME}/portal ${CASFW_HOME}/software/${spf_war_basename}

# And now the CAR files so they get bootstrapped too
# Leave site cars in place because they depend on Vignette classes which are being imported
mv $(ls ${CASFW_HOME}/software/*.car | grep -v "\-site-") ${VIGNETTE_HOME}/system/bootstrap

# Setup other /var directories 
mkdir -p ${CASFW_HOME}/var/log/vignette-portal
mkdir -p ${CASFW_HOME}/var/data

# Fix permissions
echo "Setting permissions"

# In general we want:
# - user to read+write+browse (i.e. execute for directories, and if execute for files was already there we are fine), 
# - group to read+browse, 
# - others to do nothing
# - all to read+browse log files
chmod -R u+rwX,g=rX,o= ${CASFW_HOME}
chmod -R a+rX ${CASFW_HOME}/var/log

# And now we explicitely set 'execute' permissions for files we know we need
chmod ug+x ${CASFW_HOME}/bin/*.sh
for app_dir in $(ls -d ${CASFW_HOME}/software/oracle-java-* 2>/dev/null); do
    chmod u+x ${app_dir}/bin/*
    chmod u+x ${app_dir}/jre/bin/*
done 
for app_dir in $(ls -d ${CASFW_HOME}/software/apache-tomcat-*); do
    chmod u+x ${app_dir}/bin/*.sh
done 
for app_dir in $(ls -d ${CASFW_HOME}/software/vignette-portal-*); do
    chmod u+x ${app_dir}/bin/*.sh
done 


# Update Java "cacerts" file with the one that we ship and which contains HP Certificate Authority
echo "Installing HP Certificate Authority"
for java_dir in $(ls -d ${CASFW_HOME}/software/oracle-java-1.5.* 2>/dev/null); do
    cp ${java_dir}/jre/lib/security/cacerts ${java_dir}/jre/lib/security/cacerts.ORIGINAL
    cp ${CASFW_HOME}/etc/security/java5_cacerts ${java_dir}/jre/lib/security/cacerts
done 


