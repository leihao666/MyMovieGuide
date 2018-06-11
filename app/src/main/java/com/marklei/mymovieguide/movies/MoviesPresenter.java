package com.marklei.mymovieguide.movies;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.data.source.MoviesRepository;
import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.movies.sorting.SortType;
import com.marklei.mymovieguide.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

@ActivityScoped
public class MoviesPresenter implements MoviesContract.Presenter {

    @NonNull
    private final MoviesRepository mMoviesRepository;

    private MoviesContract.View view;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private boolean mFirstLoad = true;
    private int currentPage = 1;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @Inject
    MoviesPresenter(@NonNull MoviesRepository moviesRepository, @NonNull BaseSchedulerProvider schedulerProvider) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadMovies(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadMovies(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link MoviesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadMovies(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            if (isViewAttached()) {
                view.setLoadingIndicator(true);
            }
        }
        if (forceUpdate) {
            mMoviesRepository.refreshMovies();
        }

        mCompositeDisposable.clear();
        Flowable<List<Movie>> listFlowable;
        int selectedOption = mMoviesRepository.getSelectedOption();
        if (selectedOption == SortType.MOST_POPULAR.getValue()) {
            listFlowable = mMoviesRepository.fetchPopularMovies(currentPage);
        } else if (selectedOption == SortType.HIGHEST_RATED.getValue()) {
            listFlowable = mMoviesRepository.fetchHighestRatedMovies();
        } else {
            listFlowable = mMoviesRepository.fetchFavoritesMovies();
        }
        Disposable disposable = listFlowable
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        // onNext
                        movies -> view.showMovies(movies),
                        // onError
                        throwable -> view.showLoadingMovieError(throwable.getMessage()));
        mCompositeDisposable.add(disposable);
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void setFiltering(SortType sortType) {
        mMoviesRepository.setSortingOption(sortType);
    }

    @Override
    public void takeView(MoviesContract.View view) {
        this.view = view;
        loadMovies(false, true);
    }

    @Override
    public void dropView() {
        view = null;
    }
}
