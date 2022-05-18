package com.example.offlinefirst.di;

import com.example.offlinefirst.MainActivity;
import com.example.offlinefirst.di.auth.AuthFragmentBuildersModule;
import com.example.offlinefirst.di.auth.AuthModule;
import com.example.offlinefirst.di.auth.AuthScope;
import com.example.offlinefirst.di.auth.AuthViewModelModule;
import com.example.offlinefirst.di.main.MainModule;
import com.example.offlinefirst.di.main.MainScope;
import com.example.offlinefirst.di.main.MainViewModelsModule;
import com.example.offlinefirst.ui.auth.AuthActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {
    @MainScope
    @ContributesAndroidInjector(
            modules = {MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthModule.class, AuthFragmentBuildersModule.class, AuthViewModelModule.class}
    )
    abstract AuthActivity contributeAuthActivity();
}
