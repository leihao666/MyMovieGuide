package com.marklei.mymovieguide.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.marklei.mymovieguide.data.Movie;

/**
 * The Room Database that contains the Movie table.
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieGuideDatabase extends RoomDatabase {

    public abstract MoviesDao moviesDao();
}
