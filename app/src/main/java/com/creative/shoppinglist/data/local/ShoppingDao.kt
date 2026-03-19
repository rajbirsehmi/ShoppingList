package com.creative.shoppinglist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItemEntity)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItemEntity)

    @Query("UPDATE shopping_list SET isChecked = true WHERE id = :id")
    suspend fun markItemAsDone(id: Int)

    @Query("UPDATE shopping_list SET isChecked = false WHERE id = :id")
    suspend fun markItemAsUndone(id: Int)

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAllShoppingItems()

    @Query("SELECT * FROM shopping_list WHERE isImportant = FALSE ORDER BY ID ASC")
    fun getRegularShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_list WHERE isImportant = TRUE ORDER BY ID ASC")
    fun getImportantShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Query("UPDATE shopping_list SET isImportant = true WHERE ID = :id")
    suspend fun markItemAsImportant(id: Int)

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    suspend fun getShoppingItemById(id: Int): ShoppingItemEntity?

    @Query("UPDATE shopping_list SET isImportant = false WHERE ID = :id")
    suspend fun markItemAsNotImportant(id: Int)
}


