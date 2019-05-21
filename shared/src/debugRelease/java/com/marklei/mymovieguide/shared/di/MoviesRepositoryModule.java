package com.marklei.mymovieguide.shared.di;

import android.app.Application;

import androidx.room.Room;

import com.marklei.mymovieguide.shared.data.Local;
import com.marklei.mymovieguide.shared.data.MoviesDataSource;
import com.marklei.mymovieguide.shared.data.Remote;
import com.marklei.mymovieguide.shared.data.local.MovieGuideDatabase;
import com.marklei.mymovieguide.shared.data.local.MoviesDao;
import com.marklei.mymovieguide.shared.data.local.MoviesLocalDataSource;
import com.marklei.mymovieguide.shared.data.remote.MoviesRemoteDataSource;
import com.marklei.mymovieguide.shared.data.remote.network.NetworkModule;

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
