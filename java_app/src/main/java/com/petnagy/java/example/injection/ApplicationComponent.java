package com.petnagy.java.example.injection;

import com.petnagy.java.example.ProjectApplication;
import com.petnagy.java.example.redux.AppState;
import com.petnagy.koredux.Store;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ApplicationModule.class, DaggerActivityModule.class})
public interface ApplicationComponent extends AndroidInjector<ProjectApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<ProjectApplication> {
    }

    Store<AppState> exposeStore();
}
