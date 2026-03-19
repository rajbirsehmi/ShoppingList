package com.creative.shoppinglist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.creative.shoppinglist.util.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val isChecked: Boolean,
    val isImportant: Boolean,
    val isRemindable: Boolean,
    val remindMeOn: Long
)
