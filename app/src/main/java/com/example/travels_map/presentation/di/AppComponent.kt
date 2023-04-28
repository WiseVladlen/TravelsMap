package com.example.travels_map.presentation.di

import com.example.travels_map.presentation.activity.MainActivity
import com.example.travels_map.presentation.authentication.login.LoginFragment
import com.example.travels_map.presentation.authentication.registration.RegistrationFragment
import com.example.travels_map.presentation.di.modules.AppModule
import com.example.travels_map.presentation.main.group.GroupFragment
import com.example.travels_map.presentation.main.group.add_participant.AddParticipantFragment
import com.example.travels_map.presentation.main.group.create.CreateGroupFragment
import com.example.travels_map.presentation.main.group.join.JoinGroupFragment
import com.example.travels_map.presentation.main.group.select.SelectGroupFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: RegistrationFragment)

    fun inject(fragment: GroupFragment)

    fun inject(fragment: CreateGroupFragment)

    fun inject(fragment: SelectGroupFragment)

    fun inject(fragment: JoinGroupFragment)

    fun inject(fragment: AddParticipantFragment)
}