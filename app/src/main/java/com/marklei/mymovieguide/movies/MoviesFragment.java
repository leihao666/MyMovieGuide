package com.marklei.mymovieguide.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marklei.mymovieguide.Constants;
import com.marklei.mymovieguide.R;
import com.marklei.mymovieguide.data.Movie;
import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.moviedetail.MovieDetailActivity;
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

    private boolean forceUpdate = false;
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
        moviesListing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    mPresenter.nextPage(forceUpdate);
                }
            }
        });
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
        mPresenter.firstPage(false);
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
        Snackbar.make(moviesListing, R.string.loading_movies, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        moviesListing.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMovieDetailsUi(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void showLoadingMovieError(String errorMessage) {
        Snackbar.make(moviesListing, errorMessage, Snackbar.LENGTH_INDEFINITE).show();
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
            forceUpdate = true;
            mPresenter.firstPage(true);
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
