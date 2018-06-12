package com.marklei.mymovieguide.data.source.remote;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.network.TmdbWebService;
import com.marklei.mymovieguide.network.wrapper.MoviesWraper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class MoviesRemoteDataSource implements MoviesDataSource {

    private final TmdbWebService tmdbWebService;

    @Inject
    MoviesRemoteDataSource(TmdbWebService tmdbWebService) {
        this.tmdbWebService = tmdbWebService;
    }

    @Override
    public Flowable<List<Movie>> fetchPopularMovies(int page) {
        return tmdbWebService.popularMovies(page).map(MoviesWraper::getMovieList);
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies(int page) {
        return tmdbWebService.highestRatedMovies(page).map(MoviesWraper::getMovieList);
    }

    @Override
    public Flowable<List<Movie>> fetchFavoritesMovies() {
        return null;
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

    }

    @Override
    public void refreshMovies() {

    }
}
