import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by yaolan on 5/17/17.
 */
public class Populate {

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


    private void insert(String line, String tableName){
        String values = line.replaceAll(TAB_VALUE, "','");
        values = "'"+values+"'";
        String sql = SQL_INSERT.replaceFirst(TABLE_REGEX,tableName);
        sql = sql.replaceFirst(VALUES_REGEX,values);
        System.out.println(sql);
        Hw3.connectDB(sql);
    }

    public static void main(String[] agrs) throws IOException {

        Populate populate = new Populate();
        BufferedReader reader = new BufferedReader(new FileReader(MOVIES));

        String line = "";
        String keys = "";
        int i = 0;
        if ((line = reader.readLine())!=null ){
            keys = line.replaceAll(TAB_VALUE,",");
        }

        while ((line = reader.readLine()) != null && i<1) {
            populate.insert(line,"MOVIE");
            i++;
        }
        reader.close();
    }
}
