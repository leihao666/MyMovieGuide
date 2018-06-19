package com.marklei.mymovieguide.moviedetail;

import com.marklei.mymovieguide.di.ActivityScoped;
import com.marklei.mymovieguide.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MovieDetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MovieDetailFragment movieDetailFragment();

    @ActivityScoped
    @Binds
    abstract MovieDetailContract.Presenter provideMovieDetailPresenter(MovieDetailPresenter movieDetailPresenter);
}
