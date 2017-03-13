package biz.melo.movielovers.omdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import biz.melo.movielovers.model.Movie;

/**
 * Created by hotaviano on 3/12/17.
 */

public class MovieSearch {

    @SerializedName("Search")
    private List<Movie> search;

    public List<Movie> getSearch() {
        return search;
    }

    public void setSearch(List<Movie> search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return "MovieSearch{" +
                "search=" + search +
                '}';
    }
}
