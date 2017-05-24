package Populate;

import java.io.IOException;

/**
 * Created by yaolan on 5/24/17.
 */
public class Main {
    public static void main(String[] args) throws IOException{
        Populate1 populate = new Populate1();

        if (args.length >0 ) {
                populate.insertToDB(args);
        } else {
           // populate.insertToDB(MOVIES);
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
    }
}
