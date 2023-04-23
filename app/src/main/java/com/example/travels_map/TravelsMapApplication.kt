package com.example.travels_map

import android.app.Application
import com.example.travels_map.presentation.di.AppComponent
import com.example.travels_map.presentation.di.DaggerAppComponent
import com.parse.Parse

class TravelsMapApplication : Application() {

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.create()
        INSTANCE = this

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .server(BuildConfig.BACK4APP_SERVER_URL)
                .applicationId(BuildConfig.BACK4APP_APP_ID)
                .clientKey(BuildConfig.BACK4APP_CLIENT_KEY)
                .build()
        )
    }

    companion object {
        lateinit var INSTANCE: TravelsMapApplication
            private set
    }
}