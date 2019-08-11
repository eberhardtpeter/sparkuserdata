#!/bin/sh

if [ $# -lt 3 ]; then
    echo "Not enough arguments!"
    echo "Proper usage: query_count_firstname_location.sh table_name firstname location"
    exit
fi

$USERAPP_HOME/scripts/setup-phoenix.sh $1

SQLTEMPLATE=$USERAPP_HOME/scripts/sql/query_count_firstname_location_template.sql
SQL=$USERAPP_HOME/scripts/sql/query_count_firstname_location.sql
sed "s/\@tablename/${1}/g;s/\@firstname/${2}/g;s/\@location/${3}/g" $SQLTEMPLATE > $SQL
$PHOENIX_HOME/bin/sqlline.py $ZOOKEEPER $SQL

