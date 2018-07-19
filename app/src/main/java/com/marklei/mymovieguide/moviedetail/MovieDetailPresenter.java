package com.marklei.mymovieguide.moviedetail;

import android.support.annotation.NonNull;

import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.Review;
import com.marklei.mymovieguide.data.Video;
import com.marklei.mymovieguide.data.source.MoviesRepository;
import com.marklei.mymovieguide.di.ActivityScoped;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

@ActivityScoped
public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    @NonNull
    private final MoviesRepository mMoviesRepository;

    private MovieDetailContract.View view;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    @Inject
    MovieDetailPresenter(@NonNull MoviesRepository moviesRepository) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadDetails(Movie movie) {
        if (isViewAttached()) {
            view.showDetails(movie);
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void loadTrailers(Movie movie) {
        Disposable disposable = mMoviesRepository.getTrailers(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        this::onLoadTrailersSuccess,
                        // onError
                        t -> onLoadTrailersFailure());
        mCompositeDisposable.add(disposable);
    }

    private void onLoadTrailersSuccess(List<Video> videos) {
        if (isViewAttached()) {
            view.showTrailers(videos);
        }
    }

    private void onLoadTrailersFailure() {
        // Do nothing
    }

    @Override
    public void loadReviews(Movie movie) {
        Disposable disposable = mMoviesRepository.getReviews(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadReviewsSuccess);
        mCompositeDisposable.add(disposable);
    }

    private void onLoadReviewsSuccess(List<Review> reviews) {
        if (isViewAttached()) {
            view.showReviews(reviews);
        }
    }

    @Override
    public void loadFavorite(Movie movie) {
        Disposable disposable = mMoviesRepository.getFavorite(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        this::onLoadFavoriteSuccess,
                        // onError
                        t -> onLoadFavoriteFailure(t.getMessage()));
        mCompositeDisposable.add(disposable);
    }

    private void onLoadFavoriteSuccess(Integer isFavorite) {
        if (isViewAttached()) {
            view.showIsFavorited(isFavorite == 1);
        }
    }

    private void onLoadFavoriteFailure(String err) {
        // Do nothing
    }

    @Override
    public void switchFavorite(Movie movie) {
        if (isViewAttached()) {
            if (movie.getIsFavorite() == 1) {
                movie.setIsFavorite(0);
                view.showIsFavorited(false);
            } else {
                movie.setIsFavorite(1);
                view.showIsFavorited(true);
            }
            Disposable disposable = Flowable.just(movie).doOnNext(mMoviesRepository::updateMovie)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            mCompositeDisposable.add(disposable);
        }
    }

    @Override
    public void takeView(MovieDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
        mCompositeDisposable.clear();
    }
}
