#!/bin/sh

if [ $# -lt 2 ]; then
    echo "Not enough arguments!"
    echo "Proper usage: process.sh input_folder table_name"
    exit
fi

spark-submit --class hu.sparklabs.RandomUserDataWriter $USERAPP_HOME/userdata/target/sparkprocessor-1.0-SNAPSHOT.jar $1 $2
