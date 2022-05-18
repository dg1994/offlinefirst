package com.example.offlinefirst.di.auth;

import com.example.offlinefirst.ui.auth.LauncherFragment;
import com.example.offlinefirst.ui.auth.LoginFragment;
import com.example.offlinefirst.ui.auth.RegisterFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract LauncherFragment contributeLauncherFragment();

    @ContributesAndroidInjector()
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector()
    abstract RegisterFragment contributeRegisterFragment();
}
