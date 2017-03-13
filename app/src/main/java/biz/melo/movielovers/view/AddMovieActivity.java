package biz.melo.movielovers.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import biz.melo.movielovers.R;
import biz.melo.movielovers.model.Movie;
import biz.melo.movielovers.omdb.MovieSearch;
import biz.melo.movielovers.presenter.AddMoviePresenter;
import biz.melo.movielovers.view.fragment.SingleMovieFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import io.reactivex.observers.DisposableObserver;

public class AddMovieActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static String MOVIE_BUNDLER = "biz.melo.movielovers.MOVIE";

    @BindView(R.id.movies_autocomplete_text_view)
    protected AutoCompleteTextView movieTitle;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    private AddMoviePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new AddMoviePresenter(this);

        setContentView(R.layout.activity_add_movie);

        ButterKnife.bind(this);

        movieTitle.setOnItemClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIE_BUNDLER, presenter.getMovie());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            presenter.setMovie((Movie) savedInstanceState.getSerializable(MOVIE_BUNDLER));
            presenter.cancelTypingTimer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_movie_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_movie_option:
                if (presenter.getMovie() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.empty_movie_message).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.create().show();
                }

                else {
                    presenter.persistMovie();
                    Intent intent = new Intent(this, ListMoviesActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnEditorAction(value = R.id.movies_autocomplete_text_view)
    public boolean onEditorAction(int actionId) {

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(movieTitle.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            return true;
        }

        return false;
    }

    @OnTextChanged(value = R.id.movies_autocomplete_text_view)
    public void onMovieTitleChanged(CharSequence sequence) {

        if (sequence != null && presenter.getMovie() != null
                && sequence.toString().equals(presenter.getMovie().getTitle())) {
            Logger.i("The text changed is the same");
            return;
        }

        final Context context = this;

        presenter.onTypeMovieTitle(movieTitle.getText().toString(), new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        }, new DisposableObserver<MovieSearch>() {
            @Override
            public void onNext(MovieSearch values) {

                if (values.getSearch() == null) {
                    return;
                }

                presenter.setMovies(values.getSearch());
                presenter.removeAlreadySaved();


                List<String> stringValues = new ArrayList<String>();
                for (Movie movie: presenter.getMovies()) {
                    stringValues.add(movie.getTitle());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line, stringValues.toArray(new String[0]));

                movieTitle.setAdapter(adapter);
                movieTitle.showDropDown();
            }

            @Override
            public void onError(Throwable e) {
                // NOTIFY NETWORK ERROR TO USER
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Logger.e(e, "Error quering movies to autocomplete");
            }

            @Override
            public void onComplete() {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });

    }

    // AutoComplete item selection
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.cancelTypingTimer();

        presenter.setMovie(presenter.getMovies().get(position));

        Logger.i("Movie %s selected from autocomplete", presenter.getMovie().getTitle());

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(movieTitle.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);


        presenter.queryMovieById(presenter.getMovie().getImdbId(), new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        }, new DisposableObserver<Movie>() {
            @Override
            public void onNext(Movie value) {
                presenter.setMovie(value);

                SingleMovieFragment fragment = new SingleMovieFragment();
                fragment.setPresenter(presenter);
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_content_fragment,
                        fragment).commit();
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Logger.e(e, "Error trying get movie by id from IMDB Api");
            }

            @Override
            public void onComplete() {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });

    }

    public AddMoviePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(AddMoviePresenter presenter) {
        this.presenter = presenter;
    }

}
