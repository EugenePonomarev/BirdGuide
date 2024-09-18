package com.greencodemoscow.redbook.app.di

import com.greencodemoscow.redbook.app.domain.repositories.RedBookRepository
import com.greencodemoscow.redbook.core.data.repository.RedBookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(redBookRepositoryImpl: RedBookRepositoryImpl): RedBookRepository
}