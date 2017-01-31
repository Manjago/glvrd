#!/bin/sh

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

LOGDIR="glvrdlogs"

nohup $JAVA_HOME/bin/java -jar glvrd-${project.version}.jar glvrd.config 1>$LOGDIR/out 2>$LOGDIR/err & echo $!>PID;

