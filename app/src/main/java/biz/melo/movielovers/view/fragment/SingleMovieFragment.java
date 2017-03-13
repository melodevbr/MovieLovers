package biz.melo.movielovers.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import biz.melo.movielovers.R;
import biz.melo.movielovers.databinding.FragmentSingleMovieViewBinding;
import biz.melo.movielovers.model.Movie;
import biz.melo.movielovers.presenter.BasePresenter;
import biz.melo.movielovers.presenter.ViewMoviePresenter;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleMovieFragment extends Fragment {

    private static String PRESENTER_MOVIE_BUNDLER = "biz.melo.movielovers.PRESENTER_MOVIE_BUNDLE";

    @BindView(R.id.poster_image_view)
    protected ImageView posterImageView;

    private BasePresenter presenter;
    private FragmentSingleMovieViewBinding binding;

    public SingleMovieFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_single_movie_view, container, false);

        ButterKnife.bind(this, binding.getRoot());


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            presenter = (BasePresenter) savedInstanceState.getSerializable(PRESENTER_MOVIE_BUNDLER);
        }

        Picasso.with(this.getContext())
                .load(presenter.getMovie().getPoster())
                .into(posterImageView);

        binding.setPresenter(presenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PRESENTER_MOVIE_BUNDLER, presenter);
    }

    public BasePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

}
