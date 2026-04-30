@file:OptIn(ExperimentalMaterial3Api::class)

package com.creative.shoppinglist.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.creative.shoppinglist.ui.components.BottomNavBarItem
import com.creative.shoppinglist.ui.components.BottomSheetAddItem
import com.creative.shoppinglist.ui.components.FloatingActionButtonAddShoppingItem
import com.creative.shoppinglist.ui.components.TopAppBarShoppingItem
import com.creative.shoppinglist.ui.screens.ShoppingScreenEmpty
import com.creative.shoppinglist.ui.screens.ShoppingScreenImportant
import com.creative.shoppinglist.ui.screens.ShoppingScreenRegular
import com.creative.shoppinglist.ui.viewmodels.ShoppingViewModel
import com.creative.shoppinglist.util.GenerateToast
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Regular : Screen
    @Serializable
    data object Important : Screen
}

@Composable
fun ShoppingNavGraph() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<ShoppingViewModel>()

    val regularItems by viewModel.regularShoppingItems.collectAsStateWithLifecycle()
    val importantItems by viewModel.importantShoppingItems.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current

    val showFab = when {
        currentDestination?.hasRoute<Screen.Regular>() == true -> regularItems.isNotEmpty()
        currentDestination?.hasRoute<Screen.Important>() == true -> importantItems.isNotEmpty()
        else -> true
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (showFab) {
                FloatingActionButtonAddShoppingItem { showBottomSheet = true }
            }
        },
        topBar = { TopAppBarShoppingItem() },
        bottomBar = {
            NavigationBar {
                BottomNavBarItem(
                    navController = navController,
                    currentDestination = currentDestination,
                    route = Screen.Regular,
                    type = "Regular",
                    icon = Icons.AutoMirrored.Filled.List,
                    badgeCount = regularItems.size,
                )
                BottomNavBarItem(
                    navController = navController,
                    currentDestination = currentDestination,
                    route = Screen.Important,
                    type = "Important",
                    icon = Icons.Default.ErrorOutline,
                    badgeCount = importantItems.size
                )
            }
        }
    ) { innerPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                dragHandle = null
            ) {
                BottomSheetAddItem(
                    viewModel = viewModel,
                    onDismiss = { message ->
                        showBottomSheet = false
                        if (message.isNotBlank()) GenerateToast(context, message)
                    }
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Regular,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Regular> {
                if (regularItems.isEmpty()) {
                    ShoppingScreenEmpty(
                        message = "No shopping items found.",
                        icon = Icons.AutoMirrored.Rounded.List,
                        onActionClick = { showBottomSheet = true }
                    )
                } else {
                    ShoppingScreenRegular(
                        items = regularItems,
                        onDelete = viewModel::deleteShoppingItem,
                        onToggleDone = viewModel::toggleItemChecked
                    )
                }
            }
            composable<Screen.Important> {
                if (importantItems.isEmpty()) {
                    ShoppingScreenEmpty(
                        message = "No important items found.",
                        icon = Icons.Default.ErrorOutline,
                        onActionClick = { showBottomSheet = true }
                    )
                } else {
                    ShoppingScreenImportant(
                        items = importantItems,
                        onDelete = viewModel::deleteShoppingItem,
                        onToggleDone = viewModel::toggleItemChecked
                    )
                }
            }
        }
    }
}