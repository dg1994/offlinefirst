package com.example.offlinefirst.di.auth;

import com.example.offlinefirst.db.auth.UserDao;
import com.example.offlinefirst.domain.repository.auth.AuthRepository;
import com.example.offlinefirst.network.auth.AuthService;
import com.example.offlinefirst.session.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AuthModule {

    @AuthScope
    @Provides
    static AuthService provideAuthService(FirebaseAuth firebaseAuth){
        return new AuthService(firebaseAuth);
    }

    @AuthScope
    @Provides
    static AuthRepository provideAuthRepository(
            SessionManager sessionManager,
            UserDao userDao,
            AuthService authService){
        return new AuthRepository(userDao, authService, sessionManager);
    }
}
