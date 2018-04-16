package com.marklei.mymovieguide.data.source.remote;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.network.TmdbWebService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoviesRemoteDataSource implements MoviesDataSource {

    private final TmdbWebService tmdbWebService;

    @Inject
    MoviesRemoteDataSource(TmdbWebService tmdbWebService) {
        this.tmdbWebService = tmdbWebService;
    }

    @Override
    public void getMovies(@NonNull LoadMoviesCallback callback) {

    }

    @Override
    public void refreshMovies() {

    }
}
