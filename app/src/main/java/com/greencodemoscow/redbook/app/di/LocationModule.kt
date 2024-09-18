package com.greencodemoscow.redbook.app.di

import com.greencodemoscow.redbook.app.data.DefaultLocationTracker
import com.greencodemoscow.redbook.app.domain.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationTracker): LocationTracker
}