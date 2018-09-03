package com.petnagy.java.example.injection;

import com.petnagy.java.example.redux.AppReducer;
import com.petnagy.java.example.redux.AppState;
import com.petnagy.java.example.redux.LogMiddleware;
import com.petnagy.koredux.Store;

import java.util.Collections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public Store<AppState> provideStore() {
        return new Store<>(new AppReducer(), Collections.singletonList(new LogMiddleware()), new AppState(0));
    }

}
