package com.marklei.mymovieguide.data.source.remote;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.movies.sorting.SortType;
import com.marklei.mymovieguide.movies.sorting.SortingOptionStore;
import com.marklei.mymovieguide.network.TmdbWebService;
import com.marklei.mymovieguide.network.wrapper.MoviesWraper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class MoviesRemoteDataSource implements MoviesDataSource {

    private final TmdbWebService tmdbWebService;
    private SortingOptionStore sortingOptionStore;

    @Inject
    MoviesRemoteDataSource(TmdbWebService tmdbWebService, SortingOptionStore store) {
        this.tmdbWebService = tmdbWebService;
        sortingOptionStore = store;
    }

    @Override
    public Flowable<List<Movie>> getMovies() {
        int selectedOption = sortingOptionStore.getSelectedOption();
        if (selectedOption == SortType.MOST_POPULAR.getValue()) {
            return tmdbWebService.popularMovies().map(MoviesWraper::getMovieList);
        } else if (selectedOption == SortType.HIGHEST_RATED.getValue()) {
            return tmdbWebService.highestRatedMovies().map(MoviesWraper::getMovieList);
        } else {
            // getFavorites没有网络接口
            return null;
        }
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

    }

    @Override
    public void refreshMovies() {

    }
}
