package com.creative.shoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.shoppinglist.data.local.ShoppingItemEntity
import com.creative.shoppinglist.domain.model.ShoppingItem
import com.creative.shoppinglist.domain.repo.ShoppingRepository
import com.creative.shoppinglist.util.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val regularShoppingItems: StateFlow<List<ShoppingItemEntity>> =
        repository
            .getRegularShoppingItems()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val importantShoppingItems: StateFlow<List<ShoppingItemEntity>> =
        repository
            .getImportantShoppingItems()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun insertShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.insertShoppingItem(shoppingItem.toEntity())
        }
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(shoppingItem.toEntity())
        }
    }

    fun markItemAsDone(id: Int) {
        viewModelScope.launch {
            repository.markItemAsDone(id)
        }
    }

    fun markItemAsUndone(id: Int) {
        viewModelScope.launch {
            repository.markItemAsUndone(id)
        }
    }

    fun deleteAllShoppingItems() {
        viewModelScope.launch {
            repository.deleteAllShoppingItems()
        }
    }

    fun markItemAsImportant(id: Int) {
        viewModelScope.launch {
            repository.markItemAsImportant(id)
        }
    }
}