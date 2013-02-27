#!/bin/sh
CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)" 

#Let's make sure that all our files are writable by us
chmod -R u+rwX ${CASFW_HOME}

. ${CASFW_HOME}/bin/.casfwrc

# Portal config dir
VIGNETTE_HOME="$(cd $(ls -d ${CASFW_HOME}/software/vignette-portal-* | tail -n1) && pwd -P)"
ln -sf ${VIGNETTE_HOME}/config ${CASFW_HOME}/etc/vignette-portal

# Overlay spf-portal onto Vignette/portal
echo "Preparing SPF artifacts"

# first spf-portal-*.war - let's copy it over Vignette portal directory so Vignette bootstrap picks SPF classes too
spf_war="$(ls ${CASFW_HOME}/software/spf-portal-*.war | tail -n1)"

cd ${VIGNETTE_HOME}/portal

"${JAVA_HOME}/bin/jar" xf "${spf_war}"

# Since spf-portal.war is not useful and we have its files already in Vignette let's remove it and 
# create a symbolic link to the target directory in Vignette
spf_war_basename="$(basename "$(basename "${spf_war}" .war)" -novgn)"
rm "${spf_war}"
ln -sf ${VIGNETTE_HOME}/portal ${CASFW_HOME}/software/${spf_war_basename}

# And now the CAR files so they get bootstrapped too
# Leave site cars in place because they depend on Vignette classes which are being imported
# Leave also other cars in place because we will re-import them ton ensure that site cars do not overwrite
#  the components with older versions
cp $(ls ${CASFW_HOME}/software/*.car | grep -v "\-site-") ${VIGNETTE_HOME}/system/bootstrap

# Setup other /var directories 
mkdir -p ${CASFW_HOME}/var/log/vignette-portal
mkdir -p ${CASFW_HOME}/var/log/spf-html-viewer
mkdir -p ${CASFW_HOME}/var/log/spf-healthcheck
mkdir -p ${CASFW_HOME}/var/data

#Fix permissions for the executables we know about
echo "Setting 'execute' permissions"

# And now we explicitely set 'execute' permissions for files we know we need
chmod ug+x ${CASFW_HOME}/bin/*.sh
for app_dir in $(ls -d ${CASFW_HOME}/software/vignette-portal-*); do
    chmod u+x ${app_dir}/bin/*.sh
done 

# allow other installers using this one as base to provide their additional steps
if [ -r ${CASFW_HOME}/bin/.pre-install_custom.sh ]; then
    . ${CASFW_HOME}/bin/.pre-install_custom.sh
fi
