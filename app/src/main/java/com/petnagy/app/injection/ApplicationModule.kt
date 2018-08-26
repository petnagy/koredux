package com.petnagy.app.injection

import com.petnagy.app.redux.AppReducer
import com.petnagy.app.redux.AppState
import com.petnagy.app.redux.LogMiddleware
import com.petnagy.koredux.Store
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class  ApplicationModule {

    @Provides
    @Singleton
    fun provideStore(): Store<AppState> = Store(reducer = AppReducer(), middlewareList = listOf(LogMiddleware()),
            initState = AppState(0))

}