package com.example.travels_map.presentation.di.modules

import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.data.mappers.ParseObjectToGroupMapper
import com.example.travels_map.data.mappers.ParseObjectToUserMapper
import com.example.travels_map.data.mappers.UserRegistrationDataToParseUserMapper
import com.example.travels_map.data.repositories.GroupRepositoryImpl
import com.example.travels_map.data.repositories.UserRepositoryImpl
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IGroupRepository
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseObject
import com.parse.ParseUser
import dagger.Binds
import dagger.Module

@Module
abstract class BindingModule {

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): IUserRepository

    @Binds
    abstract fun bindGroupRepository(repository: GroupRepositoryImpl): IGroupRepository

    @Binds
    abstract fun bindUserRegistrationDataToParseUserMapper(mapper: UserRegistrationDataToParseUserMapper): IEntityMapper<UserRegistrationData, ParseUser>

    @Binds
    abstract fun bindParseObjectToGroupMapper(mapper: ParseObjectToGroupMapper): IEntityMapper<ParseObject, Group>

    @Binds
    abstract fun bindParseObjectToUserMapper(mapper: ParseObjectToUserMapper): IEntityMapper<ParseObject, User>
}