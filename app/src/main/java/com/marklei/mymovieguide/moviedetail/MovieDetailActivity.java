package com.marklei.mymovieguide.moviedetail;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import android.view.MenuItem;

import com.marklei.mymovieguide.Constants;
import com.marklei.mymovieguide.R;
import com.marklei.mymovieguide.data.Movie;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MovieDetailActivity extends DaggerAppCompatActivity {

    @Inject
    MovieDetailFragment injectedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.MOVIE)) {
                Movie movie = extras.getParcelable(Constants.MOVIE);
                if (movie != null) {
                    MovieDetailFragment movieDetailsFragment = (MovieDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.movie_details_container);

                    if (movieDetailsFragment == null) {
                        movieDetailsFragment = injectedFragment;
                        Bundle args = new Bundle();
                        args.putParcelable(Constants.MOVIE, movie);
                        movieDetailsFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().add(R.id.movie_details_container, movieDetailsFragment).commit();
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
