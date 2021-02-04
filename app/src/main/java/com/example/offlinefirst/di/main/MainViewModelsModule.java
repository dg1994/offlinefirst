package com.example.offlinefirst.di.main;

import androidx.lifecycle.ViewModel;

import com.example.offlinefirst.di.ViewModelKey;
import com.example.offlinefirst.viewmodel.main.CommentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(CommentViewModel.class)
    public abstract ViewModel bindMainViewModel(CommentViewModel viewModel);
}
