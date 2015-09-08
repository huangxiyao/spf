#!/bin/sh
# Program: test_HPI_PowerBroker.sh
# Author: Nilay Soneji
# Date: 2015/09/02
# Description: Shell script to test connectivity to HPI PowerBroker Servers

RUNDATE=`date +"%m-%d-%Y_%H-%M"`
HOSTNAME=`hostname`
LOGFILE="${HOSTNAME}_PB_${RUNDATE}.txt"

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


# TARGETHOSTS is list of destination hostnames - separate with space
TARGETHOSTS="g1t6279.austin.hpicorp.net g2t4639.austin.hpicorp.net g4t7957.houston.hpicorp.net g9t6153.houston.hpicorp.net"
# TARGETPORTS is list of desination ports - separate with space
TARGETPORTS="12345 12346 22"

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