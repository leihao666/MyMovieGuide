package com.marklei.mymovieguide.ui.movies;

import com.marklei.mymovieguide.shared.BasePresenter;
import com.marklei.mymovieguide.shared.BaseView;
import com.marklei.mymovieguide.shared.data.sorting.SortType;
import com.marklei.mymovieguide.shared.model.Movie;

import java.util.List;

public interface MoviesContract {

    interface View extends BaseView {

        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showMovieDetailsUi(Movie movie);

        void showLoadingMovieError(String errorMessage);

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter<View> {

        void firstPage(boolean forceUpdate);

        void nextPage(boolean forceUpdate);

        void setFiltering(SortType sortType);
    }

}
