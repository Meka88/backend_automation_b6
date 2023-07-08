package database;

import java.sql.*;

public class DatabaseConnection {
    public static void main(String[] args) throws SQLException {

        /**
         * In order to connect to database, we need the URL, username, and password
         * 1. provide DB name, username, password
         * 2. connection
         * 3. statement
         * 4. result set
         */
        String url = "jdbc:oracle:thin:@techglobal.cup7q3kvh5as.us-east-2.rds.amazonaws.com:1521/TGDEVQA";
        String username = "TECHGLOBALQA";
        String password = "TechGlobal123$!";
        String query = "SELECT * FROM employees";

        // create a connection to the database with parameters we stored
        Connection connection = DriverManager.getConnection(url, username, password);

        // statement keeps the connection between DB and Automation framework to send queries
        Statement statement = connection.createStatement();

        // ResultSet is sending the query to the DB and get the result
        ResultSet resultSet = statement.executeQuery(query);

        // ResultSetMetaData gives meta info about the table(number of columns, column name etc)
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        System.out.println("Number of columns: " + resultSetMetaData.getColumnCount());
        System.out.println("Column name: " + resultSetMetaData.getColumnName(8));

        while(resultSet.next()){
            System.out.println(resultSet.getString("LAST_NAME"));
            System.out.println(resultSet.getString(8));
        }

    }
}
