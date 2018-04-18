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
    Map<String, Movie> mCachedTasks;

    boolean mCacheIsDirty = true;

    @Inject
    MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource,
                     @Local MoviesDataSource moviesLocalDataSource,
                     SortingOptionStore store) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesLocalDataSource = moviesLocalDataSource;
        sortingOptionStore = store;
    }

    @Override
    public Flowable<List<Movie>> getMovies() {
        // Respond immediately with cache if available and not dirty
        if (mCachedTasks != null && !mCacheIsDirty) {
            return Flowable.fromIterable(mCachedTasks.values()).toList().toFlowable();
        } else if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }

        Flowable<List<Movie>> remoteTasks = getAndSaveRemoteMovies();

        if (mCacheIsDirty) {
            return remoteTasks;
        } else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Movie>> localTasks = getAndCacheLocalMovies();
            return Flowable.concat(localTasks, remoteTasks)
                    .filter(tasks -> !tasks.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Movie>> getAndCacheLocalMovies() {
        return mMoviesLocalDataSource.getMovies()
                .flatMap(movies -> Flowable.fromIterable(movies)
                        .doOnNext(movie -> mCachedTasks.put(movie.getId(), movie))
                        .toList()
                        .toFlowable());
    }

    private Flowable<List<Movie>> getAndSaveRemoteMovies() {
        return mMoviesRemoteDataSource
                .getMovies()
                .flatMap(movies -> Flowable.fromIterable(movies).doOnNext(movie -> {
                    mMoviesLocalDataSource.saveMovie(movie);
                    mCachedTasks.put(movie.getId(), movie);
                }).toList().toFlowable())
                .doOnComplete(() -> mCacheIsDirty = false);
    }

    @Override
    public void saveMovie(@NonNull Movie task) {

    }

    @Override
    public void refreshMovies() {

    }

    public void setSortingOption(SortType sortType) {
        sortingOptionStore.setSelectedOption(sortType);
    }
}
