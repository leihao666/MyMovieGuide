package com.marklei.mymovieguide.ui.moviedetail;

import com.marklei.mymovieguide.shared.di.ActivityScoped;
import com.marklei.mymovieguide.shared.di.FragmentScoped;

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
