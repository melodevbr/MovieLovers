package biz.melo.movielovers.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import biz.melo.movielovers.R;
import biz.melo.movielovers.presenter.ListMoviesPresenter;
import biz.melo.movielovers.view.adapter.ListMoviesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListMoviesActivity extends AppCompatActivity {

    @BindView(R.id.movies_recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.empty_movies_list)
    protected TextView textView;

    private ListMoviesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        setTitle(getString(R.string.list_movies_title));

        presenter = new ListMoviesPresenter(this);

        ButterKnife.bind(this);

        ListMoviesAdapter adapter = new ListMoviesAdapter(presenter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if (adapter.getItemCount() == 0) {
            textView.setVisibility(TextView.VISIBLE);
        }
    }


    @OnClick(R.id.add_movie_float_button)
    public void onAddMovieClick() {
        Intent intent = new Intent(this, AddMovieActivity.class);
        startActivity(intent);
    }

}
