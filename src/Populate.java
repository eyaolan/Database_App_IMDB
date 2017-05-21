
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Created by yaolan on 5/17/17.
 */
public class Populate {

    private static final String url = "jdbc:oracle:thin:@localhost:49161:xe";
    private static final String user = "system";
    private static final String password = "oracle";

    //path of data file
    private static final String MOVIES = "/Users/yaolan/Documents/movieDB/movies.dat";
    private static final String MOVIE_ACTORS = "/Users/yaolan/Documents/movieDB/movie_actors.dat";
    private static final String MOVIE_COUNTRIES = "/Users/yaolan/Documents/movieDB/movie_countries.dat";
    private static final String MOVIE_DIRECTORS = "/Users/yaolan/Documents/movieDB/movie_directors.dat";
    private static final String MOVIE_GENRES = "/Users/yaolan/Documents/movieDB/movie_genres.dat";
    private static final String MOVIE_LOCATIONS = "/Users/yaolan/Documents/movieDB/movie_locations.dat";
    private static final String MOVIE_TAGS = "/Users/yaolan/Documents/movieDB/movie_tags.dat";
    private static final String URER_RATEDMOVIES_TIMESTAMPS = "/Users/yaolan/Documents/movieDB/user_ratedmovies-timestamps.dat";
    private static final String USER_RATEDMOVIES = "/Users/yaolan/Documents/movieDB/user_ratedmovies.dat";
    private static final String USER_TAGGEDMOVIES_TIMESTAMPS = "/Users/yaolan/Documents/movieDB/user_taggedmovies-timestamps.dat";
    private static final String USER_TAGGEDMOVIES = "/Users/yaolan/Documents/movieDB/user_taggedmovies.dat";

    //
    private static final String SQL_INSERT = "INSERT INTO ${table} VALUES (${values})";
    private static final String SQL_COUNT_RECORDS = "SELECT * FROM ${table}";
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";
    private static final String TAB_VALUE = (Character.toString((char) 9));


    private String transferValues(String line) {
        String values = line.replaceAll("'", "''");
        values = values.replaceAll("\\$", "\\\\\\$");
        values = values.replaceAll(TAB_VALUE, "','");
        values = "'" + values + "'";
        return values.replaceAll("\\\\N", "");
    }

    public void insertToDB(String filePath, String tableName) throws IOException {

        //initial the resultSet, connection and statement
        ResultSet resultSet = null;
        Connection conn = null;
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
            System.out.println(numOfColumns);
            for (int j = 0; j < numOfColumns - 1; j++) {
                template += "?,";
            }
            template += "?";

            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            try {

                String sql_insert = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
                sql_insert = sql_insert.replaceFirst(VALUES_REGEX, template);


                PreparedStatement preparedStatement = conn.prepareStatement(sql_insert);
                System.out.println(LocalDateTime.now());
                System.out.println(sql_insert);
                if ((line = reader.readLine()) != null) {

                    //original idea of code in while loop comes from Xiaoxiao Shang
                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split(TAB_VALUE);
                       if(i == 1) {
                           for (int a = 0; a < values.length; a++) {
                               System.out.println(values[a]);
                           }
                       }
                        for (int h = 0; h < numOfColumns; h++) {
                            if (h >= values.length) {
                                preparedStatement.setString(h + 1, null);
                            } else if (tableMetaData.getColumnType(h + 1) == 2 && values[h].equals("\\N")) {
                                preparedStatement.setString(h + 1, null);
                            } else {
                                preparedStatement.setString(h + 1, values[h]);
                            }
                        }
                        preparedStatement.executeUpdate();
                        /*String values = transferValues(line);
                        String sql = sql_insert.replaceFirst(VALUES_REGEX, values);
                        stmt.executeQuery(sql);*/
                       // System.out.println(i);
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
    }


    public static void main(String[] agrs) throws IOException {

        Populate populate = new Populate();
        //populate.insertToDB(MOVIES, "MOVIE");
        //populate.insertToDB(MOVIE_ACTORS, "Movie_actors");
        populate.insertToDB(MOVIE_COUNTRIES, "Movie_countries");
        populate.insertToDB(MOVIE_DIRECTORS, "Movie_directors");
        populate.insertToDB(MOVIE_GENRES, "Movie_genres");


    }


}
