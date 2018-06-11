package com.marklei.mymovieguide.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marklei.mymovieguide.data.Movie;

import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface MoviesDao {

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM MOVIES ORDER BY popularity DESC LIMIT 20 OFFSET 20*(:page - 1)")
    List<Movie> getPopularMovies(int page);

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    /**
     * Update a movie.
     *
     * @param movie movie to be updated
     * @return the number of movies updated. This should always be 1.
     */
    @Update
    int updateMovie(Movie movie);
}
