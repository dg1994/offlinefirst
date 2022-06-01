package com.example.offlinefirst.di.main;

import com.example.offlinefirst.ui.MessagesRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @MainScope
    @Provides
    static MessagesRecyclerAdapter provideMessagesRecyclerAdapter(){
        return new MessagesRecyclerAdapter();
    }
}
