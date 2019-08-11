#!/bin/sh

if [ $# -lt 2 ]; then
    echo "Not enough arguments!"
    echo "Proper usage: query_count_location.sh table_name location"
    exit
fi

$USERAPP_HOME/scripts/setup-phoenix.sh $1

SQLTEMPLATE=$USERAPP_HOME/scripts/sql/query_count_location_template.sql
SQL=$USERAPP_HOME/scripts/sql/query_count_location.sql
sed "s/\@tablename/${1}/g;s/\@location/${2}/g" $SQLTEMPLATE > $SQL
$PHOENIX_HOME/bin/sqlline.py $ZOOKEEPER $SQL

