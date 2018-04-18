package com.marklei.mymovieguide.data.source.local;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.movies.sorting.SortType;
import com.marklei.mymovieguide.movies.sorting.SortingOptionStore;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class MoviesLocalDataSource implements MoviesDataSource {

    private SortingOptionStore sortingOptionStore;

    @Inject
    public MoviesLocalDataSource(SortingOptionStore store) {
        sortingOptionStore = store;
    }

    @Override
    public Flowable<List<Movie>> getMovies() {
        //TODO 查询数据库
        int selectedOption = sortingOptionStore.getSelectedOption();
        if (selectedOption == SortType.MOST_POPULAR.getValue()) {

        } else if (selectedOption == SortType.HIGHEST_RATED.getValue()) {

        } else {

        }
        return null;
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

    }

    @Override
    public void refreshMovies() {

    }
}
