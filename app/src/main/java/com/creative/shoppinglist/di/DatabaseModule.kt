package com.creative.shoppinglist.di

import android.app.Application
import androidx.room.Room
import com.creative.shoppinglist.data.local.AppDatabase
import com.creative.shoppinglist.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideShoppingDao(database: AppDatabase) = database.shoppingDao()
}