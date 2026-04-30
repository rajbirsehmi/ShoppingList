package com.creative.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.creative.shoppinglist.data.local.AppDatabase
import com.creative.shoppinglist.data.local.ShoppingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideShoppingDao(db: AppDatabase): ShoppingDao {
        return db.shoppingDao()
    }
}
