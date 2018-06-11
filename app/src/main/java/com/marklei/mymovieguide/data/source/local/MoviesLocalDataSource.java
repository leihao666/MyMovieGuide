package com.marklei.mymovieguide.data.source.local;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

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
        return Flowable.just(page).flatMap((Function<Integer, Flowable<List<Movie>>>) integer -> Flowable.just(mMoviesDao.getPopularMovies(integer)));
//        return Flowable.just(mMoviesDao.getPopularMovies(page));
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies() {
        return Flowable.create(emitter -> emitter.onNext(mMoviesDao.getHighestRatedMovies()), BackpressureStrategy.BUFFER);
//        return Flowable.just().flatMap((Function<Integer, Flowable<List<Movie>>>) integer -> Flowable.just(mMoviesDao.getHighestRatedMovies()));
    }

    @Override
    public Flowable<List<Movie>> fetchFavoritesMovies() {
        return null;
    }

    @Override
    public void saveMovie(@NonNull final Movie movie) {
        checkNotNull(movie);
        mMoviesDao.insertMovie(movie);
    }

    @Override
    public void refreshMovies() {

    }
}
