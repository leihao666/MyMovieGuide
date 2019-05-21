package com.marklei.mymovieguide.shared.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.marklei.mymovieguide.shared.model.Movie;

import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface MoviesDao {

    /**
     * Select all movies from the movies table.
     * 本地数据没有分页
     *
     * @return all movies.
     */
//    @Query("SELECT * FROM MOVIES ORDER BY popularity DESC LIMIT 20 OFFSET 20*(:page - 1)")
    @Query("SELECT * FROM MOVIES ORDER BY popularity DESC")
    List<Movie> getPopularMovies();

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM MOVIES ORDER BY vote_average DESC")
    List<Movie> getHighestRatedMovies();

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM MOVIES WHERE is_favorite = 1")
    List<Movie> getFavoritesMovies();

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

    @Query("SELECT is_favorite FROM MOVIES WHERE entryid = :id")
    int isFavorite(String id);
}
