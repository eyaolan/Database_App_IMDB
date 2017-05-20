import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

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
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";
    private static final String TAB_VALUE = (Character.toString((char) 9));


    public void insertToDB(String filePath, String tableName) throws IOException {

        //initial the resultSet, connection and statement
        ResultSet resultSet = null;
        Connection conn = null;
        Statement stmt = null;

        try {

            //connect to DB
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected database successfully...");

            //executeQuery
            stmt = conn.createStatement();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line = "";
            String keys = "";
            int i = 0;
            if ((line = reader.readLine()) != null) {
                keys = line.replaceAll(TAB_VALUE, ",");
                //System.out.print(keys);
            }

            try {
                String sql_insert = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
                while ((line = reader.readLine()) != null) {
                    String values = line.replaceAll("'", "''");
                    values = values.replaceAll(TAB_VALUE, "','");
                    values = "'" + values + "'";
                    values = values.replaceAll("\\\\N", "");

                    String sql = sql_insert.replaceFirst(VALUES_REGEX, values);
                    //stmt.executeUpdate(sql);
                    if (i == 134) {
                        System.out.println(sql);
                    }
                    stmt.executeQuery(sql);
                    System.out.println(i + " ");
                    i++;
                }
                System.out.println();
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
        populate.insertToDB(MOVIES, "MOVIE");


    }


}
