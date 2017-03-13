package biz.melo.movielovers.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import biz.melo.movielovers.R;
import biz.melo.movielovers.model.Movie;
import biz.melo.movielovers.presenter.ViewMoviePresenter;
import biz.melo.movielovers.view.fragment.SingleMovieFragment;

public class ViewMovieActivity extends AppCompatActivity {

    public static String MOVIE_BUNDLE = "biz.melo.movielovers.MOVIE_BUNDLE";

    private ViewMoviePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        presenter = new ViewMoviePresenter(this);

        String imdbId = getIntent().getStringExtra(MOVIE_BUNDLE);

        if (imdbId == null) {
            finish();
        }

        presenter.fetchMovieById(imdbId);

        SingleMovieFragment fragment = new SingleMovieFragment();
        fragment.setPresenter(presenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.view_movie_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_movie_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ViewMovieActivity context = this;

        switch (item.getItemId()) {
            case R.id.remove_movie_option:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.remove_movie_message).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            presenter.deleteMovie();
                        } finally {

                        }

                        Intent intent = new Intent(context, ListMoviesActivity.class);
                        context.startActivity(intent);
                        context.finish();
                    }
                }).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setPresenter(ViewMoviePresenter presenter) {
        this.presenter = presenter;
    }

    public ViewMoviePresenter getPresenter() {
        return presenter;
    }
}
