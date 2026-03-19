package com.creative.shoppinglist.domain.repo

import com.creative.shoppinglist.data.local.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItemEntity)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItemEntity)

    fun getRegularShoppingItems(): Flow<List<ShoppingItemEntity>>

    suspend fun markItemAsDone(id: Int)

    suspend fun markItemAsUndone(id: Int)

    fun getImportantShoppingItems(): Flow<List<ShoppingItemEntity>>

    suspend fun getShoppingItemById(id: Int): ShoppingItemEntity?

    suspend fun deleteAllShoppingItems()

    suspend fun markItemAsImportant(id: Int)

    suspend fun markAsNotImportant(id: Int)
}
