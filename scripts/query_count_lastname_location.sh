#!/bin/sh

if [ $# -lt 3 ]; then
    echo "Not enough arguments!"
    echo "Proper usage: query_count_lastname_location.sh table_name lastname location"
    exit
fi

$USERAPP_HOME/scripts/setup-phoenix.sh $1

SQLTEMPLATE=$USERAPP_HOME/scripts/sql/query_count_lastname_location_template.sql
SQL=$USERAPP_HOME/scripts/sql/query_count_lastname_location.sql
sed "s/\@tablename/${1}/g;s/\@lastname/${2}/g;s/\@location/${3}/g" $SQLTEMPLATE > $SQL
$PHOENIX_HOME/bin/sqlline.py $ZOOKEEPER $SQL

