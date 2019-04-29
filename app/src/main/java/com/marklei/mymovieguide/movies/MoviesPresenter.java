package com.marklei.mymovieguide.movies;

import androidx.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.source.MoviesDataSource;
import com.marklei.mymovieguide.data.source.MoviesRepository;
import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.movies.sorting.SortType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

@ActivityScoped
public class MoviesPresenter implements MoviesContract.Presenter {

    @NonNull
    private final MoviesRepository mMoviesRepository;

    private MoviesContract.View view;

    private int currentPage = 1;
    private List<Movie> loadedMovies = new ArrayList<>();

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @Inject
    MoviesPresenter(@NonNull MoviesRepository moviesRepository) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void firstPage(boolean forceUpdate) {
        currentPage = 1;
        loadedMovies.clear();
        loadMovies(forceUpdate, true);
    }

    @Override
    public void nextPage(boolean forceUpdate) {
        if (forceUpdate && isPaginationSupported()) {
            currentPage++;
            loadMovies(true, true);
        }
    }

    private boolean isPaginationSupported() {
        return mMoviesRepository.getSelectedOption() != SortType.FAVORITES.getValue();
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link MoviesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadMovies(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            if (view != null && view.isActive()) {
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
            listFlowable = mMoviesRepository.fetchHighestRatedMovies(currentPage);
        } else {
            listFlowable = mMoviesRepository.fetchFavoritesMovies();
        }
        Disposable disposable = listFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        this::onMovieFetchSuccess,
                        // onError
                        this::onMovieFetchFailed);
        mCompositeDisposable.add(disposable);
    }

    private void onMovieFetchSuccess(List<Movie> movies) {
        if (isPaginationSupported()) {
            loadedMovies.addAll(movies);
        } else {
            loadedMovies = new ArrayList<>(movies);
        }
        if (view != null && view.isActive()) {
            view.showMovies(loadedMovies);
        }
    }

    private void onMovieFetchFailed(Throwable e) {
        view.showLoadingMovieError(e.getMessage());
    }

    @Override
    public void setFiltering(SortType sortType) {
        mMoviesRepository.setSortingOption(sortType);
    }

    @Override
    public void takeView(MoviesContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}
