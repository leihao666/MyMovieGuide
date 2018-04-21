package com.marklei.mymovieguide.data.source.local;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class MoviesLocalDataSource implements MoviesDataSource {

    @Inject
    MoviesLocalDataSource() {
    }

    @Override
    public Flowable<List<Movie>> fetchPopularMovies() {
        return null;
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies() {
        return null;
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
