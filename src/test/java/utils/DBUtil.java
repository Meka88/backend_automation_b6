package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    private static String url = ConfigReader.getProperty("DBUrl");
    private static String username = ConfigReader.getProperty("DBUsername");
    private static String password = ConfigReader.getProperty("DBPassword");

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static Connection createDBConnection(){
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection is successful");
        } catch (SQLException e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        }
        return connection;
    }

    public static void executeQuery(String query){
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getColumnNames(String query){
        List<String> columnNames = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames.add(columnName);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnNames;
    }

    public static List<List<Object>> getQueryResultList(String query){
        executeQuery(query);
        List<List<Object>> rowList = new ArrayList<>();
        // we need to find number of columns to terminate our loop at the limit of table
        ResultSetMetaData resultSetMetaData;
        try {
            resultSetMetaData = resultSet.getMetaData();
            while(resultSet.next()){
                // create empty row list
                List<Object> row = new ArrayList<>();

                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    // store each column in the list
                    row.add(resultSet.getObject(i));
                }
                // storing it in list of list
                rowList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    public static Object getCellValue(String query){
        // if we need one value from one query we use this method
        return getQueryResultList(query).get(0).get(0);
    }
}
