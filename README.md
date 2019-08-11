Userapp
=======

Environment setup
-----------------
Please set the following environment variables:
1. `USERAPP_HOME` - main directory of this repository
2. `PHOENIX_HOME` - the Phoenix installation directory
3. `ZOOKEEPER` - Zookeeper root node

Prerequisites
-------------
Apache Spark 2.4.x (the scripts are using `spark-submit` to execute Spark jobs)
Java JDK 1.8
Before first use please run the `scripts/setup.sh` script to build the Maven Java project.

Data generator
-----------------
`scripts/generate.sh <size> <output folder>` - generate user data [firstname, lastname, location, birthday] in CSV format with the given script using the following parameter set:
- [size] the size of the dataset you would like to create
- [output folder] the location of the folder on DFS

Data processor
-----------------
`scripts/process.sh <input folder> <table name>` - process user data [firstname, lastname, location, birthday] from an input forlder with the given script using the following parameter set:
- [input folder] the location of the folder on DFS
- [table name] the HBase table name where the aggregated data should be put after processing them

Query scripts
-----------------
Before using the query scripts please run `scripts/setup-phoenix.sh` to prepare the Phoenix table based on the Hbase table.

`query_count_location.sh <table name> <location>` runs a Phoenix sql query against the processed dataset
- [table name] the HBase table processed data is put
- [location] filter parameter on the dataset

`query_count_firstname_location.sh <table name> <firstname> <location>` runs a Phoenix sql query against the processed dataset
- [table name] the HBase table processed data is put
- [firstname] filter parameter on the dataset
- [location] filter parameter on the dataset

`query_count_lastname_location.sh <table name> <lastname> <location>` runs a Phoenix sql query against the processed dataset
- [table name] the HBase table processed data is put
- [lastname] filter parameter on the dataset
- [location] filter parameter on the dataset

