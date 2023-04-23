package com.example.travels_map.presentation.di.modules

import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.data.mappers.UserRegistrationDataToParseUserMapper
import com.example.travels_map.data.repositories.UserRepositoryImpl
import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseUser
import dagger.Binds
import dagger.Module

@Module
abstract class BindingModule {

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): IUserRepository

    @Binds
    abstract fun bindUserRegistrationDataToParseUserMapper(mapper: UserRegistrationDataToParseUserMapper): IEntityMapper<UserRegistrationData, ParseUser>
}