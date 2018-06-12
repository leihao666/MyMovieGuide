package com.marklei.mymovieguide.movies;

import com.marklei.mymovieguide.BasePresenter;
import com.marklei.mymovieguide.BaseView;
import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.movies.sorting.SortType;

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
