package biz.melo.movielovers.presenter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import biz.melo.movielovers.model.Movie;
import biz.melo.movielovers.omdb.OmdbService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * BasePresenter
 *
 * Created by hotaviano on 3/13/17.
 */

public abstract class BasePresenter extends BaseObservable implements Serializable {

    private Context context;

    private Movie movie;
    private List<Movie> movies;

    public BasePresenter() {}

    public BasePresenter(Context context) {
        this.context = context;
    }

    public void fetchAllMovies() {
        movies = Movie.findAll();
    }

    public void removeAlreadySaved() {
        final List<Movie> myMovies = Movie.findAll();

        for (Movie movie: myMovies) {
            movies.remove(movie);
        }
    }

    public void fetchMovieById(String imdbId) {
        movie = Movie.findByImdbId(imdbId).get(0);
    }

    public void deleteMovie() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Movie movie = realm.copyToRealmOrUpdate(getMovie());
        movie.deleteFromRealm();
        realm.commitTransaction();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Utility to execute requests
     *
     * @param observable data observable
     * @param beforeRequest it'll be executed before network operation
     * @param observer callback
     * @param <T>
     */
    protected <T> void submitQuery(Observable<T> observable, final Runnable beforeRequest,
                                   final DisposableObserver<T> observer) {

        Handler handler = new Handler(context.getMainLooper());
        handler.post(beforeRequest);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
