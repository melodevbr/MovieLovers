package biz.melo.movielovers.view.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import biz.melo.movielovers.R;
import biz.melo.movielovers.model.Movie;
import biz.melo.movielovers.presenter.BasePresenter;
import biz.melo.movielovers.view.ViewMovieActivity;

/**
 * List movies adapter
 *
 * Created by hotaviano on 3/12/17.
 */

public class ListMoviesAdapter extends RecyclerView.Adapter<ListMoviesAdapter.ViewHolder> {

    private final BasePresenter presenter;

    public ListMoviesAdapter(BasePresenter presenter) {
        this.presenter = presenter;
        presenter.fetchAllMovies(); // get movies data
    }

    /**
     * List view holder, it'll manage item attributes and touch event.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final BasePresenter presenter;

        private View      view;
        public  ImageView posterImage;


        public ViewHolder(View view, BasePresenter presenter) {
            super(view);
            this.presenter = presenter;
            this.view = view;
            this.posterImage = (ImageView) view.findViewById(R.id.movie_image_list_item);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie movie = presenter.getMovies().get(getAdapterPosition());
            Intent intent = new Intent(this.view.getContext(), ViewMovieActivity.class);
            intent.putExtra(ViewMovieActivity.MOVIE_BUNDLE, movie.getImdbId());
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_item, parent, false);

        ViewHolder holder = new ViewHolder(view, presenter);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = presenter.getMovies().get(position);

        Picasso.with(holder.view.getContext()).load(movie.getPoster()).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return presenter.getMovies().size();
    }

}
