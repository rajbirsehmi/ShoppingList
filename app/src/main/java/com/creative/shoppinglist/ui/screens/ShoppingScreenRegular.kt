package com.creative.shoppinglist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.creative.shoppinglist.domain.model.ShoppingItem
import com.creative.shoppinglist.ui.components.RegularShoppingItem
import com.creative.shoppinglist.ui.components.SwipeToDeleteContainer

@Composable
fun ShoppingScreenRegular(
    items: List<ShoppingItem>,
    onDelete: (ShoppingItem) -> Unit,
    onToggleDone: (ShoppingItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = items,
            key = { it.id ?: it.hashCode() }
        ) { item ->
            SwipeToDeleteContainer(
                onDelete = { onDelete(item) }
            ) {
                RegularShoppingItem(
                    item = item
                ) { onToggleDone(item) }
            }
        }
    }
}