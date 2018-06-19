package com.marklei.mymovieguide.data.source.local;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.Review;
import com.marklei.mymovieguide.data.Video;
import com.marklei.mymovieguide.data.source.MoviesDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class MoviesLocalDataSource implements MoviesDataSource {

    private final MoviesDao mMoviesDao;

    @Inject
    MoviesLocalDataSource(@NonNull MoviesDao moviesDao) {
        mMoviesDao = moviesDao;
    }

    @Override
    public Flowable<List<Movie>> fetchPopularMovies(int page) {
        return Flowable.create(emitter -> emitter.onNext(mMoviesDao.getPopularMovies()), BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies(int page) {
        return Flowable.create(emitter -> emitter.onNext(mMoviesDao.getHighestRatedMovies()), BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<List<Movie>> fetchFavoritesMovies() {
        return Flowable.create(emitter -> emitter.onNext(mMoviesDao.getFavoritesMovies()), BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<List<Video>> getTrailers(String id) {
        return null;
    }

    @Override
    public Flowable<List<Review>> getReviews(String id) {
        return null;
    }

    @Override
    public Flowable<Integer> getFavorite(String id) {
        return Flowable.just(id).flatMap(string -> Flowable.just(mMoviesDao.isFavorite(string)));
    }

    @Override
    public void saveMovie(@NonNull final Movie movie) {
        checkNotNull(movie);
        mMoviesDao.insertMovie(movie);
    }

    @Override
    public void updateMovie(@NonNull final Movie movie) {
        checkNotNull(movie);
        mMoviesDao.updateMovie(movie);
    }

    @Override
    public void refreshMovies() {

    }
}
