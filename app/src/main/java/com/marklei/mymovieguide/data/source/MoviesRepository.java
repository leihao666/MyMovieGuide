/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marklei.mymovieguide.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.Review;
import com.marklei.mymovieguide.data.Video;
import com.marklei.mymovieguide.movies.sorting.SortType;
import com.marklei.mymovieguide.movies.sorting.SortingOptionStore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 从数据源加载电影到缓存的具体实现
 */
@Singleton
public class MoviesRepository implements MoviesDataSource {

    private final MoviesDataSource mMoviesRemoteDataSource;

    private final MoviesDataSource mMoviesLocalDataSource;

    private SortingOptionStore sortingOptionStore;

    @Nullable
    private Map<String, Movie> mCachedPopularMovies;
    @Nullable
    private Map<String, Movie> mCachedHighestRatedMovies;
    @Nullable
    private Map<String, Movie> mCachedFavoritesMovies;

    private boolean mCachePopularIsDirty = false;
    private boolean mCacheHighestRatedIsDirty = false;
    private boolean mCacheFavoritesIsDirty = false;

    @Inject
    MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource,
                     @Local MoviesDataSource moviesLocalDataSource,
                     SortingOptionStore store) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesLocalDataSource = moviesLocalDataSource;
        sortingOptionStore = store;
    }

    @Override
    public Flowable<List<Movie>> fetchPopularMovies(int page) {
        if (mCachedPopularMovies != null && !mCachePopularIsDirty) {
            return Flowable.fromIterable(mCachedPopularMovies.values()).toList().toFlowable();
        } else if (mCachedPopularMovies == null) {
            mCachedPopularMovies = new LinkedHashMap<>();
        }

        Flowable<List<Movie>> remoteMovies = getAndSaveRemotePopularMovies(page);

        if (mCachePopularIsDirty) {
            return remoteMovies;
        } else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Movie>> localMovies = getAndCacheLocalPopularMovies();
            return Flowable.concat(localMovies, remoteMovies)
                    .filter(movies -> !movies.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Movie>> getAndSaveRemotePopularMovies(int page) {
        return mMoviesRemoteDataSource.fetchPopularMovies(page)
                .flatMap(movies -> Flowable.fromIterable(movies).doOnNext(movie -> {
                    mMoviesLocalDataSource.saveMovie(movie);
                    mCachedPopularMovies.put(movie.getId(), movie);
                }).toList().toFlowable())
                .doOnComplete(() -> mCachePopularIsDirty = false);
    }

    private Flowable<List<Movie>> getAndCacheLocalPopularMovies() {
        return mMoviesLocalDataSource.fetchPopularMovies(1)
                .flatMap(movies -> Flowable.fromIterable(movies)
                        .doOnNext(movie -> mCachedPopularMovies.put(movie.getId(), movie))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies(int page) {
        if (mCachedHighestRatedMovies != null && !mCacheHighestRatedIsDirty) {
            return Flowable.fromIterable(mCachedHighestRatedMovies.values()).toList().toFlowable();
        } else if (mCachedHighestRatedMovies == null) {
            mCachedHighestRatedMovies = new LinkedHashMap<>();
        }

        Flowable<List<Movie>> remoteMovies = getAndSaveRemoteHighestRatedMovies(page);

        if (mCacheHighestRatedIsDirty) {
            return remoteMovies;
        } else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Movie>> localMovies = getAndCacheLocalHighestRatedMovies();
            return Flowable.concat(localMovies, remoteMovies)
                    .filter(movies -> !movies.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Movie>> getAndSaveRemoteHighestRatedMovies(int page) {
        return mMoviesRemoteDataSource.fetchHighestRatedMovies(page)
                .flatMap(movies -> Flowable.fromIterable(movies).doOnNext(movie -> {
                    mMoviesLocalDataSource.saveMovie(movie);
                    mCachedHighestRatedMovies.put(movie.getId(), movie);
                }).toList().toFlowable())
                .doOnComplete(() -> mCacheHighestRatedIsDirty = false);
    }

    private Flowable<List<Movie>> getAndCacheLocalHighestRatedMovies() {
        return mMoviesLocalDataSource.fetchHighestRatedMovies(1)
                .flatMap(movies -> Flowable.fromIterable(movies)
                        .doOnNext(movie -> mCachedHighestRatedMovies.put(movie.getId(), movie))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<List<Movie>> fetchFavoritesMovies() {
        if (mCachedFavoritesMovies != null && !mCacheFavoritesIsDirty) {
            return Flowable.fromIterable(mCachedFavoritesMovies.values()).toList().toFlowable();
        } else {
            mCachedFavoritesMovies = new LinkedHashMap<>();
        }

        Flowable<List<Movie>> localMovies = getAndCacheLocalFavoritesMovies();

        if (mCacheFavoritesIsDirty) {
            return localMovies;
        } else {
            return Flowable.concat(Flowable.fromIterable(mCachedFavoritesMovies.values()).toList().toFlowable(), localMovies)
                    .filter(movies -> !movies.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Movie>> getAndCacheLocalFavoritesMovies() {
        return mMoviesLocalDataSource.fetchFavoritesMovies()
                .flatMap(movies -> Flowable.fromIterable(movies)
                        .doOnNext(movie -> mCachedFavoritesMovies.put(movie.getId(), movie))
                        .toList().toFlowable())
                .doOnComplete(() -> mCacheFavoritesIsDirty = false);
    }

    @Override
    public Flowable<List<Video>> getTrailers(String id) {
        return mMoviesRemoteDataSource.getTrailers(id);
    }

    @Override
    public Flowable<List<Review>> getReviews(String id) {
        return mMoviesRemoteDataSource.getReviews(id);
    }

    @Override
    public Flowable<Integer> getFavorite(String id) {
        return mMoviesLocalDataSource.getFavorite(id);
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        mMoviesRemoteDataSource.saveMovie(movie);
        mMoviesLocalDataSource.saveMovie(movie);
    }

    @Override
    public void updateMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        mMoviesRemoteDataSource.updateMovie(movie);
        mMoviesLocalDataSource.updateMovie(movie);
    }

    @Override
    public void refreshMovies() {
        mCachePopularIsDirty = true;
        mCacheHighestRatedIsDirty = true;
        mCacheFavoritesIsDirty = true;
    }

    public int getSelectedOption() {
        return sortingOptionStore.getSelectedOption();
    }

    public void setSortingOption(SortType sortType) {
        sortingOptionStore.setSelectedOption(sortType);
    }
}
