package com.marklei.mymovieguide.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.marklei.mymovieguide.data.Movie;

import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface MoviesDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM MOVIES")
    List<Movie> getPopularMovies();

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);
}
