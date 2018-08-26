package com.petnagy.app.injection

import com.petnagy.app.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DaggerActivityModule {

    @PerActivity
    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

}