package com.marklei.mymovieguide.movies;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.data.source.MoviesRepository;
import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.movies.sorting.SortType;

import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private MoviesContract.View view;

    private boolean mFirstLoad = true;

    @Inject
    MoviesPresenter(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    @Override
    public void loadMovies(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link MoviesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            if (isViewAttached()) {
                view.setLoadingIndicator(true);
            }
        }
        if (forceUpdate) {
            mMoviesRepository.refreshMovies();
        }

        mMoviesRepository.getMovies(new MoviesDataSource.LoadMoviesCallback() {

            @Override
            public void onMoviesLoaded(List<Movie> movies) {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void setFiltering(SortType sortType) {

    }

    @Override
    public void takeView(MoviesContract.View view) {
        this.view = view;
        loadMovies(false);
    }

    @Override
    public void dropView() {
        view = null;
    }
}
