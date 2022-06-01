package com.example.offlinefirst.di;

import com.example.offlinefirst.domain.datasource.MessageDataSource;
import com.example.offlinefirst.domain.datasource.MessageDataSourceFactory;
import com.example.offlinefirst.domain.repository.BaseMessageRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = RepositoryModule.class)
public class DataSourceModule {

    @Singleton
    @Provides
    MessageDataSource provideMessageDataSource(BaseMessageRepository baseMessageRepository) {
        return new MessageDataSource(baseMessageRepository);
    }

    @Singleton
    @Provides
    MessageDataSourceFactory provideMessageDataSourceFactory(MessageDataSource messageDataSource) {
        return new MessageDataSourceFactory(messageDataSource);
    }
}
