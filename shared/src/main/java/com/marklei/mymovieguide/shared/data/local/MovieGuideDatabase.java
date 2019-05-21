package com.marklei.mymovieguide.shared.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.marklei.mymovieguide.shared.model.Movie;

/**
 * The Room Database that contains the Movie table.
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieGuideDatabase extends RoomDatabase {

    public abstract MoviesDao moviesDao();
}
