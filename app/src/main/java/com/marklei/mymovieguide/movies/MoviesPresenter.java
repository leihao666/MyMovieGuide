package com.marklei.mymovieguide.movies;

import com.marklei.mymovieguide.data.source.MoviesRepository;
import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.movies.sorting.SortType;

import javax.inject.Inject;

@ActivityScoped
public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private MoviesContract.View mView;

    @Inject
    MoviesPresenter(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    @Override
    public void loadMovies(boolean forceUpdate) {

    }

    @Override
    public void setFiltering(SortType sortType) {

    }

    @Override
    public void takeView(MoviesContract.View view) {
        mView = view;
        loadMovies(false);
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
