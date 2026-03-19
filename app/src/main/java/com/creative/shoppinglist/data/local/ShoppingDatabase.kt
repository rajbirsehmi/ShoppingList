package com.creative.shoppinglist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingDao(): ShoppingDao
}