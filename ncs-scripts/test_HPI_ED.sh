#!/bin/sh
# Program: test_HPI_ED.sh
# Author: Nilay Soneji
# Date: 2015/09/02
# Description: Shell script to test connectivity to HPI Enterprise Directory Servers
# Usage:
#   test_HPI_ED.sh - if no parameters are passed display usage
#   test_HPI_ED.sh all - tests all HPI ED environments - PRO, ITG and DEV
#   test_HPI_ED.sh pro - tests all HPI PRO ED environments
#   test_HPI_ED.sh itg - tests all HPI ITG ED environments
#   test_HPI_ED.sh dev - tests all HPI DEV ED environments


RUNDATE=`date +"%m-%d-%Y_%H-%M"`
HOSTNAME=`hostname`
LOGFILE="${HOSTNAME}_HPI_ED_${RUNDATE}.txt"
CURRENTDIRECTORY=`pwd`

BINARYEXISTS=0
if [ -f "portcheck.pl" ]
then
    BINARYEXISTS=1
    COMMAND="portcheck.pl"
else
  if [ -f  "/usr/local/bin/portcheck.pl" ]
  then
      BINARYEXISTS=1
      COMMAND="/usr/local/bin/portcheck.pl"
  fi
fi

if [ $BINARYEXISTS -eq 0 ]
then
  echo
  echo
  echo CRITICAL ERROR: portcheck.pl NOT found
  echo
  echo "1. On your local PC go to https://vg9w4029-ent302.houston.hp.com/teams/FireWalker/Shared%20Documents/Port%20Test%20Tool"
  echo "2. Download PortTestxxx.zip to your local PC"
  echo "3. Unzip PortTestxxx.zip"
  echo "4. Upload portcheck.pl to ${HOSTNAME} in ${CURRENTDIRECTORY} directory"
  echo
  echo
  exit 1
fi


# no argument passed - test only PRO HPI ED hostnames
# TARGETHOSTS is list of destination hostnames - separate with space
if [ -z "$1" ]
then
  echo "Usage:"
  echo "  test_HPI_ED.sh - if no parameters are passed display usage"
  echo "  test_HPI_ED.sh all - tests all HPI ED environments - PRO, ITG and DEV"
  echo "  test_HPI_ED.sh pro - tests all HPI PRO ED environments"
  echo "  test_HPI_ED.sh itg - tests all HPI ITG ED environments"
  echo "  test_HPI_ED.sh dev - tests all HPI DEV ED environments"
  echo
  exit 0
else
  if [ "$1" = "all" ]
  then
    TARGETHOSTS="hpi-pro-ods-ed-master.infra.hpicorp.net hpi-pro-ods-ed.infra.hpicorp.net hpi-itg-ods-ed-master.infra.hpicorp.net hpi-itg-ods-ed.infra.hpicorp.net hpi-dev-ods-ed-master.infra.hpicorp.net hpi-dev-ods-ed.infra.hpicorp.net"
  else
    if [ "$1" = "itg" ]
    then
      TARGETHOSTS="hpi-itg-ods-ed-master.infra.hpicorp.net hpi-itg-ods-ed.infra.hpicorp.net"
    else
      if [ "$1" = "dev" ]
      then
        TARGETHOSTS="hpi-dev-ods-ed-master.infra.hpicorp.net hpi-dev-ods-ed.infra.hpicorp.net"
      else
        TARGETHOSTS="hpi-pro-ods-ed-master.infra.hpicorp.net hpi-pro-ods-ed.infra.hpicorp.net"
      fi
    fi
  fi
fi

# TARGETPORTS is list of desination ports - separate with space
TARGETPORTS="389 636"

# loop all destination hostnames
for TARGETHOST in $TARGETHOSTS
do
# loop all desination ports
  for TARGETPORT in $TARGETPORTS
  do
    perl $COMMAND -t ${TARGETHOST} -p ${TARGETPORT} 2>&1 | tee -a ${LOGFILE}
  done
done

echo "Log Generated: ${LOGFILE}"
