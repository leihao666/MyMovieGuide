package com.marklei.mymovieguide.movies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marklei.mymovieguide.R;
import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.movies.sorting.SortType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class MoviesFragment extends DaggerFragment implements MoviesContract.View {

    @Inject
    MoviesContract.Presenter mPresenter;

    @BindView(R.id.movies_listing)
    RecyclerView moviesListing;

    private RecyclerView.Adapter adapter;
    private List<Movie> movies = new ArrayList<>(20);
    private Unbinder unbinder;

    @Inject
    public MoviesFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 开启OptionsMenu
        setHasOptionsMenu(true);
        // 保存Fragment状态
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initLayoutReferences();
        return rootView;
    }

    private void initLayoutReferences() {
        moviesListing.setHasFixedSize(true);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 2;
        } else {
            columns = getResources().getInteger(R.integer.no_of_columns);
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);

        moviesListing.setLayoutManager(layoutManager);
        adapter = new MoviesListingAdapter(movies, this);
        moviesListing.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.takeView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMovies(List<Movie> movies) {

    }

    @Override
    public void showMovieDetailsUi(Movie movie) {

    }

    @Override
    public void showLoadingMovieError(String errorMessage) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.action_sort));
        popup.getMenuInflater().inflate(R.menu.menu_sorting_movies, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.most_popular:
                    mPresenter.setFiltering(SortType.MOST_POPULAR);
                    break;
                case R.id.highest_rated:
                    mPresenter.setFiltering(SortType.HIGHEST_RATED);
                    break;
                case R.id.favorites:
                    mPresenter.setFiltering(SortType.FAVORITES);
                    break;
            }
            mPresenter.loadMovies(false);
            return true;
        });
        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.dropView();
        unbinder.unbind();
    }
}
