package com.marklei.mymovieguide.ui.movies;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.marklei.mymovieguide.R;
import com.marklei.mymovieguide.shared.util.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class MoviesActivity extends DaggerAppCompatActivity {

    @Inject
    Lazy<MoviesFragment> moviesFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
        if (moviesFragment == null) {
            // Get the fragment from dagger
            moviesFragment = moviesFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), moviesFragment, R.id.fragment_listing);
        }
    }

    private void setToolbar() {
        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.movie_guide);
            ab.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
