package com.marklei.mymovieguide.movies;

import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.di.FragmentScoped;

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
