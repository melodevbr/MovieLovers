package biz.melo.movielovers.presenter;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;

import biz.melo.movielovers.model.Movie;
import biz.melo.movielovers.omdb.MovieSearch;
import biz.melo.movielovers.omdb.OmdbService;
import io.reactivex.observers.DisposableObserver;

/**
 * Add movie presenter.
 *
 * Created by hotaviano on 3/12/17.
 */

public class AddMoviePresenter extends BasePresenter {

    // timer used for typing management
    private Timer timer;

    public AddMoviePresenter() {}

    public AddMoviePresenter(Context context) {
        super(context);
    }

    /**
     * Cancel typing timer if it exists
     */
    public void cancelTypingTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Persist local movie (you can get it using <code>getMovie()</code>)
     */
    public void persistMovie() {
        if (getMovie() != null) {
            getMovie().save();
        }
    }

    /**
     * Query movie from IMDB Api
     *
     * @param id movie imdb id
     * @param beforeRequest it'll run before network operation
     * @param observer callback
     */
    public void queryMovieById(String id, final Runnable beforeRequest,
                               final DisposableObserver<Movie> observer) {

        // submit network operation
        super.submitQuery(OmdbService.getInstance().findMovieByOmdbId(id), beforeRequest, observer);
    }

    /**
     * Manage typing event, it's used to know when request the resource
     * to open the autocomplete popup.
     *
     * @param movieTitle movie title query
     * @param beforeRequest it'll be executed before network operation
     * @param observer callback
     */
    public void onTypeMovieTitle(final String movieTitle, final Runnable beforeRequest,
                                 final DisposableObserver<MovieSearch> observer) {

        if (timer != null) {
            Logger.d("Request cancelled by fast typing");
            timer.cancel();
            timer = null;
        }

        final BasePresenter presenter = this;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (movieTitle == null || movieTitle.length() < 5) {
                    Logger.i("Request cancelled by string length less than 5 chars");
                    return;
                }

                Logger.i("Requesting movies on OMDB API...");

                // submit network operation
                presenter.submitQuery(OmdbService.getInstance().searchMovies(movieTitle),
                        beforeRequest, observer);

                timer.cancel();
                timer = null;
            }
        }, 1000);

    }

}
