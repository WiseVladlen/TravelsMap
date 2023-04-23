package com.example.travels_map.presentation.di

import com.example.travels_map.presentation.activity.MainActivity
import com.example.travels_map.presentation.authentication.login.LoginFragment
import com.example.travels_map.presentation.authentication.registration.RegistrationFragment
import com.example.travels_map.presentation.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: RegistrationFragment)
}