package com.marklei.mymovieguide.ui.movies;

import com.marklei.mymovieguide.shared.di.ActivityScoped;
import com.marklei.mymovieguide.shared.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MoviesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MoviesFragment moviesFragment();

    @ActivityScoped
    @Binds
    abstract MoviesContract.Presenter provideMoviesPresenter(MoviesPresenter moviesPresenter);
}
