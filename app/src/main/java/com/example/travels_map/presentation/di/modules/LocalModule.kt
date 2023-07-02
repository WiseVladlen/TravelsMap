package com.example.travels_map.presentation.di.modules

import android.content.Context
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.data.managers.GroupManager
import com.example.travels_map.data.managers.UserManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    fun getCoroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun getUserManager(): UserManager {
        return UserManager()
    }

    @Provides
    @Singleton
    fun getGroupManager(): GroupManager {
        return GroupManager()
    }
}