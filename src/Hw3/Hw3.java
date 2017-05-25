package Hw3;

import javax.swing.*;
import java.sql.*;

/**
 * Created by yaolan on 5/18/17.
 */
public class Hw3 {

    //DB information
    static final String url = "jdbc:oracle:thin:@localhost:49161:xe";
    static final String user = "system";
    static final String password = "oracle";

    //GUI
    //main frame
    private JFrame mainFrame;


    //code modified from https://www.tutorialspoint.com/jdbc/jdbc-select-records.htm
    static public ResultSet connectDB(String sql) {

        //initial the resultSet, connection and statement
        ResultSet resultSet = null;
        Connection conn = null;
        Statement stmt = null;

        try {

            //connect to DB
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected database successfully...");
            conn.setAutoCommit(false);

            //executeQuery
            stmt = conn.createStatement();
            System.out.println(stmt.toString());
            //
            try {
                //stmt.executeUpdate(sql);
                resultSet = stmt.executeQuery(sql);
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException sqle) {
                    System.out.println("There is a error in rollback!");
                }
            } finally {
                try {
                    conn.commit();
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("There is a error in commit!");
                }
            }
            System.out.println("Created table in given database...");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return resultSet;
    }

    public  static  void  main(String[] args){


        String sql = "CREATE TABLE REGISTRATION " +
                "(id INTEGER not NULL, " +
                " first VARCHAR(255), " +
                " last VARCHAR(255), " +
                " age INTEGER, " +
                " PRIMARY KEY ( id ))";

        String sql2 = "Drop table Registration";

        ResultSet resultSet = Hw3.connectDB(sql);
        Hw3.connectDB(sql2);


        System.out.println();
    }
}