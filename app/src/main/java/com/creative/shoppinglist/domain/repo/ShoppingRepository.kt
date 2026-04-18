package com.creative.shoppinglist.domain.repo

import com.creative.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun getRegularShoppingItems(): Flow<List<ShoppingItem>>

    suspend fun markItemAsDone(id: Int)

    suspend fun markItemAsUndone(id: Int)

    fun getImportantShoppingItems(): Flow<List<ShoppingItem>>

    suspend fun getShoppingItemById(id: Int): ShoppingItem?

    suspend fun deleteAllShoppingItems()

    suspend fun markItemAsImportant(id: Int)

    suspend fun markAsNotImportant(id: Int)
}
