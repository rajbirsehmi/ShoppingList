package com.creative.shoppinglist.data.repo

import com.creative.shoppinglist.data.local.ShoppingDao
import com.creative.shoppinglist.data.local.ShoppingItemEntity
import com.creative.shoppinglist.domain.repo.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingDao: ShoppingDao
): ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItemEntity) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItemEntity) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun getRegularShoppingItems(): Flow<List<ShoppingItemEntity>> =
        shoppingDao.getRegularShoppingItems()

    override suspend fun markItemAsDone(id: Int) {
        shoppingDao.markItemAsDone(id)
    }

    override suspend fun markItemAsUndone(id: Int) {
        shoppingDao.markItemAsUndone(id)
    }

    override fun getImportantShoppingItems(): Flow<List<ShoppingItemEntity>> =
        shoppingDao.getImportantShoppingItems()

    override suspend fun getShoppingItemById(id: Int): ShoppingItemEntity? =
        shoppingDao.getShoppingItemById(id)

    override suspend fun deleteAllShoppingItems() {
        shoppingDao.deleteAllShoppingItems()
    }

    override suspend fun markItemAsImportant(id: Int) {
        shoppingDao.markItemAsImportant(id)
    }

    override suspend fun markAsNotImportant(id: Int) {
        shoppingDao.markItemAsNotImportant(id)
    }
}