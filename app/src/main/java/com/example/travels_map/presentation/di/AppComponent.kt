package com.example.travels_map.presentation.di

import com.example.travels_map.presentation.activity.MainActivity
import com.example.travels_map.presentation.authentication.login.LoginFragment
import com.example.travels_map.presentation.authentication.registration.RegistrationFragment
import com.example.travels_map.presentation.di.modules.AppModule
import com.example.travels_map.presentation.main.account.AccountFragment
import com.example.travels_map.presentation.main.account.change_password.ChangePasswordFragment
import com.example.travels_map.presentation.main.account.edit_profile.EditProfileFragment
import com.example.travels_map.presentation.main.explore.ExploreFragment
import com.example.travels_map.presentation.main.explore.manage_place.ManagePlaceFragment
import com.example.travels_map.presentation.main.explore.create_route.CreateRouteFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.PlaceInfoBottomSheetFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.create_review.CreateReviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.OverviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.ReviewsFragment
import com.example.travels_map.presentation.main.explore.route_configuration.RouteConfigurationFragment
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

    fun inject(fragment: AccountFragment)

    fun inject(fragment: EditProfileFragment)

    fun inject(fragment: ChangePasswordFragment)

    fun inject(fragment: ExploreFragment)

    fun inject(fragment: PlaceInfoBottomSheetFragment)

    fun inject(fragment: RouteConfigurationFragment)

    fun inject(fragment: CreateRouteFragment)

    fun inject(fragment: OverviewFragment)

    fun inject(fragment: ReviewsFragment)

    fun inject(fragment: ManagePlaceFragment)

    fun inject(fragment: CreateReviewFragment)
}