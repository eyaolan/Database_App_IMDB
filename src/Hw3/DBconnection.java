package Hw3;

import java.sql.*;

/**
 * Created by yaolan on 5/25/17.
 */
public class DBconnection {
    //DB information
    static final String url = "jdbc:oracle:thin:@localhost:49161:xe";
    static final String user = "system";
    static final String password = "oracle";


    //return connection that connect to Oracle database
    public static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    //close connection
    public static void closeDB(Connection conn) {
        try {
            conn.commit();
            conn.close();
            System.out.println("Disconnect to Database");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("There is a error in commit!");
        }
    }

    public static ResultSet executeSQL(Connection conn, String sql) throws SQLException{
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    //code modified from https://www.tutorialspoint.com/jdbc/jdbc-select-records.htm
    //test the connectDB and closeDB methods
    public static ResultSet connectDB(String sql) {

        //initial the resultSet, connection and statement
        ResultSet resultSet = null;
        Connection conn = null;
        Statement stmt = null;

        try {

            //connect to DB
            conn = connectDB();

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
            }
            try {

                while (resultSet.next()) {
                    System.out.println(resultSet.getInt(1)+" "+resultSet.getString(2));
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            closeDB(conn);
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

    public static void main(String[] args) {
        /*String sql = "CREATE TABLE REGISTRATION " +
                "(id INTEGER not NULL, " +
                " first VARCHAR(255), " +
                " last VARCHAR(255), " +
                " age INTEGER, " +
                " PRIMARY KEY ( id ))";
*/
        // String sql2 = "Drop table Registration";

        String sql = "SELECT * FROM MOVIES WHERE id<10";
        connectDB(sql);
        System.out.println();

    }
}

