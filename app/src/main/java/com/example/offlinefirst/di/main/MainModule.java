package com.example.offlinefirst.di.main;

import android.app.Application;

import com.example.offlinefirst.ui.CommentsRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @MainScope
    @Provides
    static CommentsRecyclerAdapter provideCommentsRecyclerAdapter(){
        return new CommentsRecyclerAdapter();
    }
}
