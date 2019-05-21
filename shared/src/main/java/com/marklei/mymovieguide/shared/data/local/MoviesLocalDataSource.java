package com.marklei.mymovieguide.shared.data.local;

import androidx.annotation.NonNull;

import com.marklei.mymovieguide.shared.data.MoviesDataSource;
import com.marklei.mymovieguide.shared.model.Movie;
import com.marklei.mymovieguide.shared.model.Review;
import com.marklei.mymovieguide.shared.model.Video;

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
        return Flowable.create(emitter -> {
            emitter.onNext(mMoviesDao.getPopularMovies());
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies(int page) {
//        return Flowable.just(page).flatMap(i -> Flowable.just(mMoviesDao.getHighestRatedMovies()));
        return Flowable.create(emitter -> {
            emitter.onNext(mMoviesDao.getHighestRatedMovies());
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<List<Movie>> fetchFavoritesMovies() {
        return Flowable.create(emitter -> {
            emitter.onNext(mMoviesDao.getFavoritesMovies());
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
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
