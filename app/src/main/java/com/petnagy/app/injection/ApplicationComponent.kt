package com.petnagy.app.injection

import com.petnagy.app.ProjectApplication
import com.petnagy.app.redux.AppState
import com.petnagy.koredux.Store
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (ApplicationModule::class), (DaggerActivityModule::class)])
interface ApplicationComponent : AndroidInjector<ProjectApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ProjectApplication>()

    fun exposeStore(): Store<AppState>
}