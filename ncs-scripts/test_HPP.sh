#!/bin/sh
# Program: test_HPP.sh
# Author: Nilay Soneji
# Date: 2015/09/02
# Description: Shell script to test connectivity to HP Passport Servers

RUNDATE=`date +"%m-%d-%Y_%H-%M"`
HOSTNAME=`hostname`
LOGFILE="${HOSTNAME}_HPP_${RUNDATE}.txt"

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
TARGETHOSTS="16.193.48.131 16.193.48.132 16.210.154.142 16.210.154.143 16.193.20.65 16.193.20.66 16.193.20.62 16.193.20.63 16.193.20.64 16.236.64.211 16.236.64.212 16.236.64.213 16.238.20.178 16.210.6.91 16.210.6.92 16.210.168.249 16.210.168.250 16.234.34.33 16.234.34.34 16.234.34.35 16.234.105.114 16.236.80.194 16.236.80.195 16.193.24.146 16.193.57.21 16.193.56.103 16.193.56.104 16.210.120.141 16.210.120.140 16.234.34.36 16.234.99.213"
# TARGETPORTS is list of desination ports - separate with space
TARGETPORTS="44001 44002 44003"

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