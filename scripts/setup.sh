#!/bin/sh

#build java app
echo "Build UserData java app..."

cd $USERAPP_HOME/userdata
mvn clean package
cd $USERAPP_HOME

echo "Done..."


