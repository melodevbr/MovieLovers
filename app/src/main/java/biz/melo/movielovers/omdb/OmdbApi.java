package biz.melo.movielovers.omdb;

import java.util.List;

import biz.melo.movielovers.model.Movie;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit OMDB API Spec
 *
 * Created by hotaviano on 3/11/17.
 */

public interface OmdbApi {

    static String ENDPOINT = "http://www.omdbapi.com";

    @GET("/")
    public Observable<MovieSearch> searchMovies(@Query("s") String search);

    @GET("/")
    public Observable<Movie> findMovieByOmdbId(@Query("i") String id);

}
