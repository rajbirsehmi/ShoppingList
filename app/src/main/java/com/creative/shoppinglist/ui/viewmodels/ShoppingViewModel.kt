package com.creative.shoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.shoppinglist.domain.model.ShoppingItem
import com.creative.shoppinglist.domain.repo.ShoppingRepository
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

    val regularShoppingItems: StateFlow<List<ShoppingItem>> =
        repository
            .getRegularShoppingItems()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(500),
                emptyList()
            )

    val importantShoppingItems: StateFlow<List<ShoppingItem>> =
        repository
            .getImportantShoppingItems()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(500),
                emptyList()
            )

    fun insertShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.insertShoppingItem(shoppingItem)
        }
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(shoppingItem)
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