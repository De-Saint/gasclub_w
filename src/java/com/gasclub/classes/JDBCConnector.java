/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Saint
 */
public class JDBCConnector {
     private static final String JDBC_LOADER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/gasclubdb";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "dee20mene";
    
//     private static final String JDBC_LOADER = "com.mysql.jdbc.Driver";
//    private static final String URL = "jdbc:mysql://localhost:3306/fdhghdhg_gasclubdb";
//    private static final String LOGIN = "fdhghdhg_GasUser";
//    private static final String PASSWORD = "@Gasclub202";

    private Connection connection;


    public JDBCConnector() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_LOADER);
        connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }



}
