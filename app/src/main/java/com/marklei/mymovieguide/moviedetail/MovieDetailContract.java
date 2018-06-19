package com.marklei.mymovieguide.moviedetail;

import com.marklei.mymovieguide.BasePresenter;
import com.marklei.mymovieguide.BaseView;
import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.data.Review;
import com.marklei.mymovieguide.data.Video;

import java.util.List;

public interface MovieDetailContract {

    interface View extends BaseView {

        void showDetails(Movie movie);

        void showTrailers(List<Video> trailers);

        void showReviews(List<Review> reviews);

        void showIsFavorited(boolean isFavorited);

        boolean isActive();
    }

    interface Presenter extends BasePresenter<MovieDetailContract.View> {

        void loadDetails(Movie movie);

        void loadTrailers(Movie movie);

        void loadReviews(Movie movie);

        void loadFavorite(Movie movie);

        void switchFavorite(Movie movie);
    }

}
