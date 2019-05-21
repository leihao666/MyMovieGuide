package com.marklei.mymovieguide.ui.moviedetail;

import com.marklei.mymovieguide.shared.BasePresenter;
import com.marklei.mymovieguide.shared.BaseView;
import com.marklei.mymovieguide.shared.model.Movie;
import com.marklei.mymovieguide.shared.model.Review;
import com.marklei.mymovieguide.shared.model.Video;

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
