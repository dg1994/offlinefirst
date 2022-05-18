package com.example.offlinefirst.di.auth;

import com.example.offlinefirst.db.auth.AccountPropertiesDao;
import com.example.offlinefirst.db.auth.AuthTokenDao;
import com.example.offlinefirst.domain.repository.auth.AuthRepository;
import com.example.offlinefirst.network.auth.AuthApiService;
import com.example.offlinefirst.session.SessionManager;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AuthModule {

    @AuthScope
    @Provides
    static AuthApiService provideAuthApiService(Retrofit retrofit){
        return retrofit.create(AuthApiService.class);
    }

    @AuthScope
    @Provides
    static AuthRepository provideAuthRepository(
            SessionManager sessionManager,
            AuthTokenDao authTokenDao,
            AccountPropertiesDao accountPropertiesDao,
            AuthApiService authApiService){
        return new AuthRepository(authTokenDao, accountPropertiesDao, authApiService, sessionManager);
    }
}
