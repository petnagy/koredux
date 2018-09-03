package com.petnagy.java.example.injection;

import com.petnagy.java.example.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface DaggerActivityModule {

    @PerActivity
    @ContributesAndroidInjector
    MainActivity contributeMaineActivity();

}
