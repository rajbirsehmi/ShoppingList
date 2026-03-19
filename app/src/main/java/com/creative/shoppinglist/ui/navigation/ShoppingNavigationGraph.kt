@file:OptIn(ExperimentalMaterial3Api::class)

package com.creative.shoppinglist.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import kotlinx.coroutines.launch

object Routes {
    const val REGULAR = "regular"
    const val IMPORTANT = "important"
}

@Composable
fun ShoppingNavGraph() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<ShoppingViewModel>()

    val regularItems by viewModel.regularShoppingItems.collectAsState(initial = emptyList())
    val importantItems by viewModel.importantShoppingItems.collectAsState(initial = emptyList())

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButtonAddShoppingItem(
                onClick = {
                    showBottomSheet = true
                }
            )
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        topBar = {
            TopAppBarShoppingItem()
        },
        bottomBar = {
            NavigationBar {
                BottomNavBarItem(
                    navController = navController,
                    currentDestination = currentDestination,
                    routesString = Routes.REGULAR,
                    type = "Regular",
                    icon = Icons.Default.List
                )
                BottomNavBarItem(
                    navController = navController,
                    currentDestination = currentDestination,
                    routesString = Routes.IMPORTANT,
                    type = "Important",
                    icon = Icons.Default.ErrorOutline
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
                    viewMode = viewModel,
                    onDismiss = { message ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                GenerateToast(context, "Item Added$message")
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
        NavHost(
            navController = navController,
            startDestination = Routes.REGULAR,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.REGULAR) {
                if (regularItems.isEmpty()) {
                    ShoppingScreenEmpty(
                        message = "No shopping items found.",
                        icon = Icons.AutoMirrored.Rounded.List
                    )
                } else {
                    ShoppingScreenRegular(
                        viewModel = viewModel
                    )
                }
            }
            composable(Routes.IMPORTANT) {
                if (importantItems.isEmpty()) {
                    ShoppingScreenEmpty(
                        message = "No important shopping items found.",
                        icon = Icons.Default.ErrorOutline
                    )
                } else {
                    ShoppingScreenImportant(
                        viewModel
                    )
                }
            }
        }
    }
}