package com.greencodemoscow.redbook.app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.greencodemoscow.redbook.core.ResourceHolder
import com.greencodemoscow.redbook.core.ResourceHolderImpl
import com.greencodemoscow.redbook.core.data.db.RedBookDao
import com.greencodemoscow.redbook.core.data.db.RedBookDatabase
import com.greencodemoscow.redbook.redBook.data.api.RedBookApi
import retrofit2.create
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModel {
    private const val BASE_URL = "http://127.0.0.1:8000/"
    private const val DB_NAME = "redbook_database"

    @Provides
    @Singleton
    fun provideRedBookApi(): RedBookApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RedBookApi::class.java)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideResourceHolder(@ApplicationContext context: Context): ResourceHolder {
        return ResourceHolderImpl(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RedBookDatabase {
        return Room.databaseBuilder(
            context,
            RedBookDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideRedBookDao(database: RedBookDatabase) = database.redBookDao()

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }
}