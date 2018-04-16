package com.marklei.mymovieguide.data.source.local;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.source.MoviesDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoviesLocalDataSource implements MoviesDataSource {

    @Inject
    public MoviesLocalDataSource() {
    }

    @Override
    public void getMovies(@NonNull LoadMoviesCallback callback) {

    }

    @Override
    public void refreshMovies() {

    }
}
