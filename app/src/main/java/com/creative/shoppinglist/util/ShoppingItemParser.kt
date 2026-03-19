package com.creative.shoppinglist.util

import com.creative.shoppinglist.data.local.ShoppingItemEntity
import com.creative.shoppinglist.domain.model.ShoppingItem

fun ShoppingItemEntity.toDomain(): ShoppingItem = ShoppingItem(
    id = id,
    name = name,
    isChecked = isChecked,
    isImportant = isImportant,
    isRemindable = isRemindable,
    remindMeOn = remindMeOn
)

fun ShoppingItem.toEntity(): ShoppingItemEntity = ShoppingItemEntity(
    id = id,
    name = name,
    isChecked = isChecked,
    isImportant = isImportant,
    isRemindable = isRemindable,
    remindMeOn = remindMeOn
)