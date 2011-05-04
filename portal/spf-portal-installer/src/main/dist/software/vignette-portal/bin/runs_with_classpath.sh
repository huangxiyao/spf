#!/bin/sh

if [ $# = 0 ]
then
    echo "usage: $0 java_class [args]"
    exit 1
fi

if [ -f `dirname $0`/setenv.sh ]
then
 . `dirname $0`/setenv.sh
fi

VAP_INSTALL_DIR=..
VAP_WEBAPP_DIR=../portal

if [ "a$JAVA_HOME" = "a" ]
then
 JVM=`which java`
else
 JVM=$JAVA_HOME/bin/java
fi

if [ "a$MEM_ARGS" = "a" ]
then
 MEM_ARGS="-Xms256m -Xmx512m"
fi

if [[ "$(uname)" =~ "CYGWIN" ]]; then
    $JVM $MEM_ARGS -classpath "$(cygpath -pw "./util:$CLASSPATH")" -Dcom.vignette.portal.installdir.path="$(cygpath -aw $VAP_INSTALL_DIR)" -Dcom.vignette.portal.webappdir.path="$(cygpath -aw $VAP_WEBAPP_DIR)" PathLoader "$@"
else
    $JVM $MEM_ARGS -classpath "./util:$CLASSPATH" -Dcom.vignette.portal.installdir.path=$VAP_INSTALL_DIR -Dcom.vignette.portal.webappdir.path=$VAP_WEBAPP_DIR PathLoader "$@"
fi 

