#!/usr/bin/env bash

#JAVA_HOME="/opt/jdk1.8.0_121"

if [ -e "PID" ]
 then
 PID=`cat PID`
fi

if [ -n "$PID" ]
 then
   echo bot already running
   exit
fi

LOGDIR="logs"

nohup $JAVA_HOME/bin/java -Xmx100m -Dlogback.configurationFile=logback.xml -jar glvrd-${project.version}.jar -config production.config 1>$LOGDIR/out 2>$LOGDIR/err & echo $!>PID;

