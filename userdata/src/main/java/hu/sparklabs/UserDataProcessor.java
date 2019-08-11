package hu.sparklabs;

import hu.sparklabs.model.UserData;
import hu.sparklabs.model.UserDataStat;
import hu.sparklabs.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Aggregates the userdata located in the input folder and put into the given HBase table

 * To run: spark-submit --class hu.sparklabs.UserDataProcessor
 * sparkprocessor-1.0-SNAPSHOT.jar <input folder> <output HBase table name>
 */
public final class UserDataProcessor {

    private static String COLUMN_FAMILY = "PCF";
    private static Logger LOGGER = LoggerFactory.getLogger(UserData.class);

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaSparkPi")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        JavaRDD<String> lines = jsc.textFile(args[0], 1);

        JavaRDD<UserData> triplets =
                lines.map(line ->
                        new UserData(Objects.requireNonNull(StringUtils.splitAndTake3(line))));

        JavaPairRDD<UserData, Long> counts = triplets
                .mapToPair(userData -> new Tuple2<>(userData, 1L))
                .reduceByKey(Long::sum);

        List<UserDataStat> result =
                counts.collect().stream()
                        .map(r -> new UserDataStat(r._1, r._2))
                        .collect(Collectors.toList());

        System.out.println(String.format("UserData resultset is %d rows!", result.size()));

        spark.stop();

        try {
            saveDataToHBase(result, args[1]);
            LOGGER.info(String.format("%d number of records saved to %s.", result.size(), args[1]));
        } catch (IOException ex) {
            LOGGER.error("Error at closing connection to HBase!", ex);
        }

    }

    private static Connection createConnection() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        return ConnectionFactory.createConnection(configuration);
    }


    private static void saveDataToHBase(
            final List<UserDataStat> userDataStatList,
            final String tableName) throws IOException {

        Connection connection = null;

        try {
            connection = createConnection();
            createHBaseTable(tableName, connection);
            putDataToHBaseTable(tableName, userDataStatList, connection);
        } catch (IOException ex) {
            LOGGER.error("Some error occurred at saving data to Hbase!", ex);
        } finally {
            if (connection != null) connection.close();
        }

    }

    private static void createHBaseTable(final String tableName, final Connection connection) throws IOException {

        try {
            Admin admin = connection.getAdmin();

            TableName hBaseTableName = TableName.valueOf(tableName);

            if (admin.tableExists(hBaseTableName)) {
                admin.disableTable(hBaseTableName);
                admin.deleteTable(hBaseTableName);
            }

            HTableDescriptor ht = new HTableDescriptor(TableName.valueOf(tableName));
            ht.addFamily(new HColumnDescriptor(COLUMN_FAMILY));
            admin.createTable(ht);

        } catch (IOException ex) {
            LOGGER.error("Error occurred at creating Hbase table", ex);
            throw ex;
        }

    }

    private static void putDataToHBaseTable(final String tableName, final List<UserDataStat> userDataStatList, final Connection connection) throws IOException {

        Table table = null;

        try {
            table = connection.getTable(TableName.valueOf(tableName));

            final List<Put> list = new ArrayList<>();

            IntStream.range(0, userDataStatList.size())
                    .forEach(i -> {
                        Put put = new Put(Bytes.toBytes(String.valueOf(i)));
                        put.addColumn(
                                Bytes.toBytes(COLUMN_FAMILY),
                                Bytes.toBytes("FIRSTNAME"),
                                Bytes.toBytes(userDataStatList.get(i).getUserData().getFirstName()));
                        put.addColumn(
                                Bytes.toBytes(COLUMN_FAMILY),
                                Bytes.toBytes("LASTNAME"),
                                Bytes.toBytes(userDataStatList.get(i).getUserData().getLastName()));
                        put.addColumn(
                                Bytes.toBytes(COLUMN_FAMILY),
                                Bytes.toBytes("LOCATION"),
                                Bytes.toBytes(userDataStatList.get(i).getUserData().getLocation()));
                        put.addColumn(
                                Bytes.toBytes(COLUMN_FAMILY),
                                Bytes.toBytes("COUNT"),
                                Bytes.toBytes(userDataStatList.get(i).getCount()));
                        list.add(put);
                        i++;
                    });

            table.put(list);
            table.close();
        } catch (IOException ex) {
            LOGGER.error("Error occurred at putting data into Hbase table", ex);
            if (table != null) table.close();
            throw ex;
        }

    }

}
