#!/bin/sh

if [ $# -lt 1 ]; then
    echo "Not enough arguments!"
    echo "Proper usage: setup-phoenix.sh table_name"
    exit
fi

SQLTEMPLATE=$USERAPP_HOME/scripts/sql/setup_phoenix_template.sql
SQL=$USERAPP_HOME/scripts/sql/setup_phoenix.sql
sed "s/\@tablename/${1}/g" $SQLTEMPLATE > $SQL
$PHOENIX_HOME/bin/sqlline.py $ZOOKEEPER $SQL

