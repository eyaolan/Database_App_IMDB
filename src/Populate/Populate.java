package Populate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Created by yaolan on 5/17/17.
 * populate data into table, the parameter is the list of filename of data:
 * movies.dat tags.dat movie_actors.dat movie_countries.dat movie_directors.dat movie_genres.dat movie_locations.dat movie_tags.dat user_ratedmovies.dat user_ratedmovies-timestamps.dat user_taggedmovies.dat user_taggedmovies-timestamps.dat
 */
public class Populate {

    //connection setting
    private static final String url = "jdbc:oracle:thin:@localhost:49161:xe";
    private static final String user = "system";
    private static final String password = "oracle";
    private Connection conn = null;

    //path of data file
    private static final String MOVIES = "movies.dat";
    private static final String MOVIE_ACTORS = "movie_actors.dat";
    private static final String MOVIE_COUNTRIES = "movie_countries.dat";
    private static final String MOVIE_DIRECTORS = "movie_directors.dat";
    private static final String MOVIE_GENRES = "movie_genres.dat";
    private static final String MOVIE_LOCATIONS = "movie_locations.dat";
    private static final String MOVIE_TAGS = "movie_tags.dat";
    private static final String TAGS = "tags.dat";
    private static final String URER_RATEDMOVIES_TIMESTAMPS = "user_ratedmovies-timestamps.dat";
    private static final String USER_RATEDMOVIES = "user_ratedmovies.dat";
    private static final String USER_TAGGEDMOVIES_TIMESTAMPS = "user_taggedmovies-timestamps.dat";
    private static final String USER_TAGGEDMOVIES = "user_taggedmovies.dat";

    //sql
    private static final String SQL_INSERT = "INSERT INTO ${table} VALUES (${values})";
    private static final String SQL_COUNT_RECORDS = "SELECT * FROM ${table}";
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";
    private static final String TAB_VALUE = (Character.toString((char) 9));


    private String transferFilename(String fileName) {
        String[] name = fileName.split("\\.");
        String tableName = name[0].replaceAll("-","_");
        return  tableName;
    }

    private void deleteTable(String[] tables) throws SQLException{
        for(int i = tables.length-1; i>=0 ;i--) {
            String table = transferFilename(tables[i]);

            Statement deleteStatement = conn.createStatement();
            System.out.println("Deleting previous tuples in " + table + " ...");
            deleteStatement.executeUpdate("DELETE FROM " + table);
            deleteStatement.close();
        }
    }

    private void insertTuples(String[] tables, Connection conn){

        //delete previous tuples...
        try {
            deleteTable(tables);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }

        for(String fileName: tables) {
            //initial the resultSet, connection and statement
            String tableName = transferFilename(fileName);
            String line = "";
            int i = 1;
            String template = "";
            try {
                //get table's number of columns
                Statement stmt = conn.createStatement();
                String countRecordsSQL = SQL_COUNT_RECORDS.replaceFirst(TABLE_REGEX, tableName);
                ResultSetMetaData tableMetaData = stmt.executeQuery(countRecordsSQL).getMetaData();
                int numOfColumns = tableMetaData.getColumnCount();
                for (int j = 0; j < numOfColumns - 1; j++) {
                    template += "?,";
                }
                template += "?";

                BufferedReader reader = new BufferedReader(new FileReader(fileName));

                //print out start time
                System.out.println(LocalDateTime.now());

                //replace table name and template in sql sentence
                String sql_insert = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
                sql_insert = sql_insert.replaceFirst(VALUES_REGEX, template);
                PreparedStatement preparedStatement = conn.prepareStatement(sql_insert);


                if ((line = reader.readLine()) != null  ) {
                    System.out.println("Populating data into "+tableName+ "...");
                    //original idea of code in while loop comes from Xiaoxiao Shang
                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split(TAB_VALUE);
                        for (int h = 0; h < numOfColumns; h++) {
                            if (h >= values.length) {
                                preparedStatement.setString(h + 1, null);
                            } else if (tableMetaData.getColumnType(h + 1) == 2 && values[h].equals("\\N")) {
                                preparedStatement.setString(h + 1, null);
                            } else {
                                preparedStatement.setString(h + 1, values[h]);
                            }
                        }

                        try {
                            preparedStatement.executeUpdate();
                        }catch (SQLException sqle){
                            sqle.printStackTrace();
                        }
                        i++;
                    }
                }
                reader.close();
                System.out.println(LocalDateTime.now());
            } catch (Exception e) {
                try {
                    conn.rollback();
                    e.printStackTrace();
                    System.out.println("Execution fails. Rollback!");
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    System.out.println("There is a error in rollback!");
                }
            }
        }
    }

    private void closeConn(Connection conn){
        try {
            conn.commit();
            conn.close();
            System.out.println("Disconnect to Database");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("There is a error in commit!");
        }
    }

    public void insertToDB(String[] tables) throws IOException {

        //initial the resultSet, connection and statement

        ResultSet resultSet = null;

        Statement stmt = null;
        String line = "";
        int i = 1;
        String template = "";

        try {

            //connect to DB
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected database successfully...");

            //insert data into tables
            insertTuples(tables,conn);

            closeConn(conn);

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
        System.out.println();
        System.out.println();
    }


    public static void main(String[] args) throws IOException {

        Populate populate = new Populate();
        if (args.length <0 ) {
                populate.insertToDB(args);
        } else {
            String[] tables = {MOVIES,TAGS,MOVIE_ACTORS,MOVIE_COUNTRIES,MOVIE_DIRECTORS,MOVIE_GENRES,MOVIE_LOCATIONS,MOVIE_TAGS,USER_RATEDMOVIES,URER_RATEDMOVIES_TIMESTAMPS,USER_TAGGEDMOVIES,USER_TAGGEDMOVIES_TIMESTAMPS};
            populate.insertToDB(tables);
        }
    }
}
