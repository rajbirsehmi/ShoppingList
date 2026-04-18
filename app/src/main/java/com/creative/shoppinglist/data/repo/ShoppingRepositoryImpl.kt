package com.creative.shoppinglist.data.repo

import com.creative.shoppinglist.data.local.ShoppingDao
import com.creative.shoppinglist.domain.model.ShoppingItem
import com.creative.shoppinglist.domain.repo.ShoppingRepository
import com.creative.shoppinglist.util.toDomain
import com.creative.shoppinglist.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingDao: ShoppingDao
): ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem.toEntity())
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem.toEntity())
    }

    override fun getRegularShoppingItems(): Flow<List<ShoppingItem>> =
        shoppingDao.getRegularShoppingItems().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun markItemAsDone(id: Int) {
        shoppingDao.markItemAsDone(id)
    }

    override suspend fun markItemAsUndone(id: Int) {
        shoppingDao.markItemAsUndone(id)
    }

    override fun getImportantShoppingItems(): Flow<List<ShoppingItem>> =
        shoppingDao.getImportantShoppingItems().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getShoppingItemById(id: Int): ShoppingItem? =
        shoppingDao.getShoppingItemById(id)?.toDomain()

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