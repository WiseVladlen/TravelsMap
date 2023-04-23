package com.example.travels_map.presentation.di.modules

import android.content.Context
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.data.managers.UserSessionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Provides
    @Singleton
    fun getContext(): Context {
        return TravelsMapApplication.INSTANCE
    }

    @Provides
    @Singleton
    fun getUserSessionManager(): UserSessionManager {
        return UserSessionManager()
    }
}