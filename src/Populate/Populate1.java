package Populate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Created by yaolan on 5/17/17.
 */
public class Populate1 {

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

    //
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

    private void deleteTable(String table) throws SQLException{
        Statement deleteStament = conn.createStatement();
        System.out.println("Deleting previous tuples ...");
        deleteStament.executeUpdate("DELETE FROM " + table);
        deleteStament.close();
    }


    public void insertToDB(String fifeName) throws IOException {

        //initial the resultSet, connection and statement
        String tableName = transferFilename(fifeName);
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

            //get table's number of columns
            stmt = conn.createStatement();
            String countRecordsSQL = SQL_COUNT_RECORDS.replaceFirst(TABLE_REGEX, tableName);
            ResultSetMetaData tableMetaData = stmt.executeQuery(countRecordsSQL).getMetaData();
            int numOfColumns = tableMetaData.getColumnCount();
            for (int j = 0; j < numOfColumns - 1; j++) {
                template += "?,";
            }
            template += "?";

            BufferedReader reader = new BufferedReader(new FileReader(fifeName));

            try {

                //delete previous tuples...
                deleteTable(tableName);

                //replace table name and template in sql sentence
                String sql_insert = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
                sql_insert = sql_insert.replaceFirst(VALUES_REGEX, template);
                PreparedStatement preparedStatement = conn.prepareStatement(sql_insert);

                //print out start time
                System.out.println(LocalDateTime.now());
                if ((line = reader.readLine()) != null) {
                    System.out.println("Populating data into "+tableName+ "...");
                    //original idea of code in while loop comes from Xiaoxiao Shang
                    while ((line = reader.readLine()) != null && i<2) {
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
            } finally {
                try {
                    conn.commit();
                    conn.close();
                    System.out.println("Disconnect to Database");
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    System.out.println("There is a error in commit!");
                }
            }

            reader.close();
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

        Populate1 populate = new Populate1();

        if (args.length >0 ) {
            for (int i = 0; i < args.length; i++) {
                populate.insertToDB(args[i]);
            }

        } else {
            //populate.insertToDB(MOVIES);
            //populate.insertToDB(MOVIES, "MOVIE");
            //populate.insertToDB(MOVIE_ACTORS, "Movie_actors");
            //populate.insertToDB(MOVIE_COUNTRIES, "Movie_countries");
            //populate.insertToDB(MOVIE_DIRECTORS, "Movie_directors");
            //populate.insertToDB(MOVIE_GENRES, "Movie_genres");

           /* populate.insertToDB(MOVIE_LOCATIONS, "Movie_locations");
            populate.insertToDB(MOVIE_TAGS, "Movie_tags");
            populate.insertToDB(TAGS, "tags");
            populate.insertToDB(URER_RATEDMOVIES_TIMESTAMPS, "user_ratedmovies_timestamps");
            populate.insertToDB(USER_RATEDMOVIES, "user_ratedmovies");
            populate.insertToDB(USER_TAGGEDMOVIES_TIMESTAMPS, "user_taggedmovies_timestamps");
            populate.insertToDB(USER_TAGGEDMOVIES, "user_taggedmovies");*/

        }
        System.out.println(populate.transferFilename("Movie_ratted-user.dat"));
    }


}
