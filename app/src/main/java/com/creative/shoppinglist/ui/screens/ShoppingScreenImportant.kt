package com.creative.shoppinglist.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.creative.shoppinglist.ui.components.ImportantShoppingItem
import com.creative.shoppinglist.ui.viewmodels.ShoppingViewModel
import com.creative.shoppinglist.util.toDomain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreenImportant(
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val importantItems by viewModel.importantShoppingItems.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = importantItems,
            key = { it.id ?: it.hashCode() }
        ) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.deleteShoppingItem(item.toDomain())
                        true
                    } else false
                }
            )
            SwipeToDismissBox(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                            else -> Color.Transparent
                        }, label = "backgroundColor"
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color, shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        val iconAlpha = if (dismissState.progress > 0.1f) 1f else 0f
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = Color.White.copy(alpha = iconAlpha)
                        )
                    }
                }
            ) {
                ImportantShoppingItem(
                    item = item.toDomain(),
                    onCheckedChange = { isChecked ->
                        if (item.toDomain().isChecked) viewModel.markItemAsUndone(item.id!!)
                        else viewModel.markItemAsDone(item.id!!)
                    }
                )
            }
        }
    }
}