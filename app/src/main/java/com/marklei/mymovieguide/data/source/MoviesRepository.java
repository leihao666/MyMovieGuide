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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.movies.sorting.SortType;
import com.marklei.mymovieguide.movies.sorting.SortingOptionStore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

/**
 * 从数据源加载电影到缓存的具体实现
 */
@Singleton
public class MoviesRepository implements MoviesDataSource {

    private final MoviesDataSource mMoviesRemoteDataSource;

    private final MoviesDataSource mMoviesLocalDataSource;

    private SortingOptionStore sortingOptionStore;

    @Nullable
    Map<String, Movie> mCachedPopularMovies;
    @Nullable
    Map<String, Movie> mCachedHighestRatedMovies;
    @Nullable
    Map<String, Movie> mCachedFavoritesMovies;

    boolean mCachePopularIsDirty = false;
    boolean mCacheHighestRatedIsDirty = false;
    boolean mCacheFavoritesIsDirty = false;

    @Inject
    MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource,
                     @Local MoviesDataSource moviesLocalDataSource,
                     SortingOptionStore store) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesLocalDataSource = moviesLocalDataSource;
        sortingOptionStore = store;
    }

    @Override
    public Flowable<List<Movie>> fetchPopularMovies() {
        if (mCachedPopularMovies != null && !mCachePopularIsDirty) {
            return Flowable.fromIterable(mCachedPopularMovies.values()).toList().toFlowable();
        } else if (mCachedPopularMovies == null) {
            mCachedPopularMovies = new LinkedHashMap<>();
        }

        Flowable<List<Movie>> remoteMovies = getAndSaveRemotePopularMovies();

        if (mCachePopularIsDirty) {
            return remoteMovies;
        } else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Movie>> localTasks = getAndCacheLocalPopularMovies();
            return Flowable.concat(localTasks, remoteMovies)
                    .filter(tasks -> !tasks.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Movie>> getAndSaveRemotePopularMovies() {
        return mMoviesRemoteDataSource
                .fetchPopularMovies()
                .flatMap(movies -> Flowable.fromIterable(movies).doOnNext(movie -> {
                    mMoviesLocalDataSource.saveMovie(movie);
                    mCachedPopularMovies.put(movie.getId(), movie);
                }).toList().toFlowable())
                .doOnComplete(() -> mCachePopularIsDirty = false);
    }

    private Flowable<List<Movie>> getAndCacheLocalPopularMovies() {
        return mMoviesLocalDataSource.fetchPopularMovies()
                .flatMap(movies -> Flowable.fromIterable(movies)
                        .doOnNext(movie -> mCachedPopularMovies.put(movie.getId(), movie))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<List<Movie>> fetchHighestRatedMovies() {
        if (mCachedHighestRatedMovies != null && !mCacheHighestRatedIsDirty) {
            return Flowable.fromIterable(mCachedHighestRatedMovies.values()).toList().toFlowable();
        } else if (mCachedHighestRatedMovies == null) {
            mCachedHighestRatedMovies = new LinkedHashMap<>();
        }

        Flowable<List<Movie>> remoteMovies = getAndSaveRemoteHighestRatedMovies();

        if (mCacheHighestRatedIsDirty) {
            return remoteMovies;
        } else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Movie>> localTasks = getAndCacheLocalHighestRatedMovies();
            return Flowable.concat(localTasks, remoteMovies)
                    .filter(tasks -> !tasks.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Movie>> getAndSaveRemoteHighestRatedMovies() {
        return mMoviesRemoteDataSource
                .fetchHighestRatedMovies()
                .flatMap(movies -> Flowable.fromIterable(movies).doOnNext(movie -> {
                    mMoviesLocalDataSource.saveMovie(movie);
                    mCachedHighestRatedMovies.put(movie.getId(), movie);
                }).toList().toFlowable())
                .doOnComplete(() -> mCacheHighestRatedIsDirty = false);
    }

    private Flowable<List<Movie>> getAndCacheLocalHighestRatedMovies() {
        return mMoviesLocalDataSource.fetchHighestRatedMovies()
                .flatMap(movies -> Flowable.fromIterable(movies)
                        .doOnNext(movie -> mCachedHighestRatedMovies.put(movie.getId(), movie))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<List<Movie>> fetchFavoritesMovies() {
        return null;
    }

    @Override
    public void saveMovie(@NonNull Movie task) {

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
