package com.marklei.mymovieguide.data.source;

import com.marklei.mymovieguide.data.source.local.MoviesLocalDataSource;
import com.marklei.mymovieguide.data.source.remote.MoviesRemoteDataSource;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract public class MoviesRepositoryModule {

    @Singleton
    @Binds
    @Local
    abstract MoviesDataSource provideMoviesLocalDataSource(MoviesLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract MoviesDataSource provideMoviesRemoteDataSource(MoviesRemoteDataSource dataSource);
}
