package com.marklei.mymovieguide;

import com.marklei.mymovieguide.shared.data.MoviesRepository;
import com.marklei.mymovieguide.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class BaseApplication extends DaggerApplication {

    @Inject
    MoviesRepository moviesRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

}
