package com.example.offlinefirst.di;

import com.example.offlinefirst.MainActivity;
import com.example.offlinefirst.di.main.MainModule;
import com.example.offlinefirst.di.main.MainScope;
import com.example.offlinefirst.di.main.MainViewModelsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {
    @MainScope
    @ContributesAndroidInjector(
            modules = {MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();
}
