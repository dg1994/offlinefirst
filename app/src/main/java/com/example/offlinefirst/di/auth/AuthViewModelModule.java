package com.example.offlinefirst.di.auth;

import androidx.lifecycle.ViewModel;

import com.example.offlinefirst.di.ViewModelKey;
import com.example.offlinefirst.viewmodel.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);
}