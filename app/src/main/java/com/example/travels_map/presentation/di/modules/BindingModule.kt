package com.example.travels_map.presentation.di.modules

import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.data.mappers.ParseObjectToGroupMapper
import com.example.travels_map.data.mappers.ParseObjectToPlaceMapper
import com.example.travels_map.data.mappers.ParseObjectToReviewMapper
import com.example.travels_map.data.mappers.ParseObjectToUserMapper
import com.example.travels_map.data.mappers.PlaceToParsePlaceMapper
import com.example.travels_map.data.mappers.FeedbackDataToParseReviewMapper
import com.example.travels_map.data.mappers.ParseObjectToRouteMapper
import com.example.travels_map.data.mappers.ParsePlaceToFeedbackMapper
import com.example.travels_map.data.mappers.BuildingRouteDataToParseRouteMapper
import com.example.travels_map.data.mappers.UserRegistrationDataToParseUserMapper
import com.example.travels_map.data.repositories.DrivingRouteRepositoryImpl
import com.example.travels_map.data.repositories.FeedbackRepositoryImpl
import com.example.travels_map.data.repositories.GroupRepositoryImpl
import com.example.travels_map.data.repositories.PlaceRepositoryImpl
import com.example.travels_map.data.repositories.ReviewRepositoryImpl
import com.example.travels_map.data.repositories.UserRepositoryImpl
import com.example.travels_map.domain.entities.Feedback
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.entities.Review
import com.example.travels_map.domain.entities.Route
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.models.BuildingRouteData
import com.example.travels_map.domain.models.FeedbackData
import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import com.example.travels_map.domain.repositories.IFeedbackRepository
import com.example.travels_map.domain.repositories.IGroupRepository
import com.example.travels_map.domain.repositories.IPlaceRepository
import com.example.travels_map.domain.repositories.IReviewRepository
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseObject
import com.parse.ParseUser
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class BindingModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(repository: UserRepositoryImpl): IUserRepository

    @Binds
    @Singleton
    abstract fun bindGroupRepository(repository: GroupRepositoryImpl): IGroupRepository

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(repository: PlaceRepositoryImpl): IPlaceRepository

    @Binds
    @Singleton
    abstract fun bindFeedbackRepository(repository: FeedbackRepositoryImpl): IFeedbackRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(repository: ReviewRepositoryImpl): IReviewRepository

    @Binds
    @Singleton
    abstract fun bindDrivingRouteRepository(repository: DrivingRouteRepositoryImpl): IDrivingRouteRepository

    @Binds
    abstract fun bindUserRegistrationDataToParseUserMapper(mapper: UserRegistrationDataToParseUserMapper): IEntityMapper<UserRegistrationData, ParseUser>

    @Binds
    abstract fun bindParseObjectToGroupMapper(mapper: ParseObjectToGroupMapper): IEntityMapper<ParseObject, Group>

    @Binds
    abstract fun bindParseObjectToUserMapper(mapper: ParseObjectToUserMapper): IEntityMapper<ParseObject, User>

    @Binds
    abstract fun bindParseObjectToPlaceMapper(mapper: ParseObjectToPlaceMapper): IEntityMapper<ParseObject, Place>

    @Binds
    abstract fun bindPlaceToParsePlaceMapper(mapper: PlaceToParsePlaceMapper): IEntityMapper<Place, ParseObject>

    @Binds
    abstract fun bindParseObjectToReviewMapper(mapper: ParseObjectToReviewMapper): IEntityMapper<ParseObject, Review>

    @Binds
    abstract fun bindFeedbackDataToParseReviewMapper(mapper: FeedbackDataToParseReviewMapper): IEntityMapper<FeedbackData, ParseObject>

    @Binds
    abstract fun bindParseObjectToFeedbackMapper(mapper: ParsePlaceToFeedbackMapper): IEntityMapper<ParseObject, Feedback>

    @Binds
    abstract fun bindBuildingRouteDataToParseRouteMapper(mapper: BuildingRouteDataToParseRouteMapper): IEntityMapper<BuildingRouteData, ParseObject>

    @Binds
    abstract fun bindParseObjectToRouteMapper(mapper: ParseObjectToRouteMapper): IEntityMapper<ParseObject, Route>
}