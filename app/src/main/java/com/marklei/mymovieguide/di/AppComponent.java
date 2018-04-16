package com.marklei.mymovieguide.di;

import android.app.Application;

import com.marklei.mymovieguide.BaseApplication;
import com.marklei.mymovieguide.data.source.MoviesRepositoryModule;
import com.marklei.mymovieguide.network.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ActivityBindingModule.class,
        MoviesRepositoryModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
