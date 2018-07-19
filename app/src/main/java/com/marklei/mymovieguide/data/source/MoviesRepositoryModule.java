package com.marklei.mymovieguide.data.source;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.marklei.mymovieguide.data.source.local.MovieGuideDatabase;
import com.marklei.mymovieguide.data.source.local.MoviesDao;
import com.marklei.mymovieguide.data.source.local.MoviesLocalDataSource;
import com.marklei.mymovieguide.data.source.remote.MoviesRemoteDataSource;
import com.marklei.mymovieguide.data.source.remote.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = NetworkModule.class)
abstract public class MoviesRepositoryModule {

    @Singleton
    @Binds
    @Local
    abstract MoviesDataSource provideMoviesLocalDataSource(MoviesLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract MoviesDataSource provideMoviesRemoteDataSource(MoviesRemoteDataSource dataSource);

    @Singleton
    @Provides
    static MovieGuideDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), MovieGuideDatabase.class, "Movies.db")
                .build();
    }

    @Singleton
    @Provides
    static MoviesDao provideMoviesDao(MovieGuideDatabase db) {
        return db.moviesDao();
    }

}
