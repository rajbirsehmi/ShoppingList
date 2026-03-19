package com.creative.shoppinglist.domain.model

data class ShoppingItem(
    val id: Int?,
    val name: String,
    val isChecked: Boolean,
    val isImportant: Boolean,
    val isRemindable: Boolean,
    val remindMeOn: Long
)
