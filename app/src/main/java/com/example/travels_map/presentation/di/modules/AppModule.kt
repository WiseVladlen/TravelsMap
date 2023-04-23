package com.example.travels_map.presentation.di.modules

import dagger.Module

@Module(includes = [BindingModule::class, LocalModule::class])
class AppModule