package com.example.offlinefirst.di;

import com.example.offlinefirst.domain.datasource.CommentDataSource;
import com.example.offlinefirst.domain.datasource.CommentDataSourceFactory;
import com.example.offlinefirst.domain.repository.BaseCommentRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = RepositoryModule.class)
public class DataSourceModule {

    @Singleton
    @Provides
    CommentDataSource provideCommentDataSource(BaseCommentRepository baseCommentRepository) {
        return new CommentDataSource(baseCommentRepository);
    }

    @Singleton
    @Provides
    CommentDataSourceFactory provideCommentDataSourceFactory(CommentDataSource commentDataSource) {
        return new CommentDataSourceFactory(commentDataSource);
    }
}
