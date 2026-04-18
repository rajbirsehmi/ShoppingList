package com.creative.shoppinglist.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.creative.shoppinglist.domain.model.ShoppingItem
import com.creative.shoppinglist.notifications.scheduleNotification
import com.creative.shoppinglist.ui.navigation.Screen
import com.creative.shoppinglist.ui.viewmodels.ShoppingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun FloatingActionButtonAddShoppingItem(
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.padding(16.dp), onClick = {
            onClick()
        }) {
        Icon(Icons.Default.Add, contentDescription = "Add Item")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarShoppingItem(scrollBehavior: TopAppBarScrollBehavior? = null) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Shopping List",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    )
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAddItem(
    viewModel: ShoppingViewModel = hiltViewModel(),
    onDismiss: (String) -> Unit
) {

    var itemName by remember { mutableStateOf("") }
    var isNameEmpty by remember { mutableStateOf(false) }
    var isImportant by remember { mutableStateOf(false) }
    var isRemindable by remember { mutableStateOf(false) }
    var remindMeOn by remember { mutableLongStateOf(0) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    var selectedDateTimeText by remember { mutableStateOf("Not set") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Handle bar for visual cue (usually added by ModalBottomSheet, but safe to add if custom)
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "New Item",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { onDismiss("") }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ItemNameInput(
            itemName = itemName,
            errorText = "Please enter a name",
            label = "What do you need to buy?",
            isNameEmpty = isNameEmpty,
            onValueChange = {
                itemName = it
                if (it.isNotBlank()) isNameEmpty = false
            },
            onDone = {
                focusManager.clearFocus()
            })

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Settings",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        ToggleSettingRow(
            message = "Mark as Important",
            icon = Icons.Default.PriorityHigh,
            isChecked = isImportant,
            onCheckedChange = {
                isImportant = it
                if (!it) isRemindable = false
            }
        )

        if (isImportant) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            ToggleSettingRow(
                message = "Set Reminder",
                icon = Icons.Outlined.Notifications,
                isChecked = isRemindable,
                onCheckedChange = {
                    isRemindable = it
                    if (it) showDatePicker = true
                }
            )
            
            if (isRemindable) {
                Surface(
                    onClick = { showDatePicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 4.dp, bottom = 8.dp),
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (remindMeOn > 0) selectedDateTimeText else "Tap to set time",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (remindMeOn > 0) MaterialTheme.colorScheme.onSurface 
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            onClick = @androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM) {
                val trimmedName = itemName.trim()
                if (trimmedName.isEmpty()) {
                    isNameEmpty = true
                    return@Button
                }
                
                val newItem = ShoppingItem(
                    id = null,
                    name = trimmedName,
                    isImportant = isImportant,
                    remindMeOn = if (isRemindable) remindMeOn else 0L,
                    isRemindable = isRemindable,
                    isChecked = false
                )
                
                var message = ""
                if (isRemindable && remindMeOn > 0) {
                    try {
                        scheduleNotification(
                            context = context,
                            timeInMillis = remindMeOn,
                            itemName = trimmedName
                        )
                        message = "Reminder set!"
                    } catch (ex: SecurityException) {
                        ex.printStackTrace()
                    }
                }
                
                viewModel.insertShoppingItem(newItem)
                onDismiss(message)
            }) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add to List", style = MaterialTheme.typography.titleMedium)
        }

        if (showDatePicker) {
            MyDatePickerDialog(
                labelNext = "Next",
                labelCancel = "Cancel",
                datePickerState,
                onDismissButton = {
                    showDatePicker = false
                    if (remindMeOn == 0L) isRemindable = false
                },
                onConfirmButton = {
                    showDatePicker = false
                    showTimePicker = true
                },
                onPickerDismiss = {
                    showDatePicker = false
                }
            )
        }

        if (showTimePicker) {
            MyTimePickerDialog(
                timePickerState = timePickerState,
                datePickerState = datePickerState,
                onDismissRequest = { showTimePicker = false },
                onConfirm = { formatterResult, timeInMillis ->
                    remindMeOn = timeInMillis
                    selectedDateTimeText = formatterResult
                    showTimePicker = false
                }
            )
        }
    }
}

@Composable
fun ItemNameInput(
    itemName: String,
    errorText: String,
    label: String,
    isNameEmpty: Boolean,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        maxLines = 1,
        value = itemName,
        label = { Text(label) },
        onValueChange = {
            onValueChange(it)
        },
        isError = isNameEmpty,
        supportingText = {
            if (isNameEmpty) {
                Text(
                    text = errorText, color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            })
    )

}

@Composable
fun ToggleSettingRow(
    message: String,
    icon: ImageVector? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        onClick = { onCheckedChange(!isChecked) },
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun ReminderTextButton(
    message: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = "Reminder: $message",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePickerDialog(
    timePickerState: TimePickerState,
    datePickerState: DatePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: (String, Long) -> Unit
) {
    TimePickerDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        onConfirm = {
            // datePickerState.selectedDateMillis is UTC at midnight
            val selectedDateUtc = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
            
            // Extract YMD from UTC to avoid timezone shift
            val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = selectedDateUtc
            }
            
            // Create local calendar with those YMD + selected hour/minute
            val localCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, utcCal.get(Calendar.YEAR))
                set(Calendar.MONTH, utcCal.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, utcCal.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                set(Calendar.MINUTE, timePickerState.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            val formattedDate = formatter.format(localCal.time)
            onConfirm(formattedDate, localCal.timeInMillis)
        }) {
        TimePicker(state = timePickerState)
    }
}

@Composable
fun MyDatePickerDialog(
    labelNext: String,
    labelCancel: String,
    datePickerState: DatePickerState,
    onDismissButton: () -> Unit,
    onConfirmButton: () -> Unit,
    onPickerDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = {
            onPickerDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmButton()
            }) {
                Text(labelNext)
            }
        }, dismissButton = {
            TextButton(onClick = {
                onDismissButton()
            }) {
                Text(labelCancel)
            }
        }) {
        DatePicker(state = datePickerState)
    }
}

@Composable
@Preview(
    showBackground = true,
    device = "id:pixel_9_pro",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)

fun FloatingActionButtonAddShoppingItemPreview() {
    BottomSheetAddItem(
        onDismiss = {}
    )
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit, onConfirm: () -> Unit, content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = "Select Time",
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}

@Composable
fun RowScope.BottomNavBarItem(
    navController: NavHostController,
    currentDestination: NavDestination?,
    route: Screen,
    type: String,
    icon: ImageVector,
    badgeCount: Int = 0
) {
    val selected = currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true

    NavigationBarItem(
        selected = selected,
        onClick = {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge {
                            Text(text = badgeCount.toString())
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = type
                )
            }
        },
        label = {
            Text(
                text = type,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    )
}

@Composable
fun RegularShoppingItem(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit
) {
    val contentAlpha by animateFloatAsState(
        targetValue = if (item.isChecked) 0.5f else 1.0f,
        label = "contentAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!item.isChecked) },
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha)
                    )
                )
            },
            trailingContent = {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = {
                        onCheckedChange(it)
                    }
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun ImportantShoppingItem(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (item.isChecked) {
            MaterialTheme.colorScheme.surfaceContainerHigh
        } else {
            lerp(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.errorContainer,
                0.15f
            )
        },
        label = "containerColor"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!item.isChecked) },
        color = containerColor,
        shape = MaterialTheme.shapes.medium,
        border = if (!item.isChecked) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        ) else null,
        tonalElevation = 2.dp
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = "Important Item",
                    tint = if (item.isChecked) MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.error
                )
            },
            headlineContent = {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                )
            },
            supportingContent = if (item.isRemindable) {
                {
                    val sdf =
                        remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
                    val dateString = sdf.format(Date(item.remindMeOn))
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else null,
            trailingContent = {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = {
                        onCheckedChange(it)
                    }
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingItemsPreview() {
    val regularItem = ShoppingItem(
        id = 1,
        name = "Milk",
        isChecked = false,
        isImportant = false,
        isRemindable = false,
        remindMeOn = 0L
    )
    val checkedItem = ShoppingItem(
        id = 2,
        name = "Bread",
        isChecked = true,
        isImportant = false,
        isRemindable = false,
        remindMeOn = 0L
    )
    val importantItem = ShoppingItem(
        id = 3,
        name = "Fresh Watermelon",
        isChecked = false,
        isImportant = true,
        isRemindable = true,
        remindMeOn = System.currentTimeMillis()
    )

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        RegularShoppingItem(item = regularItem, onCheckedChange = {})
        RegularShoppingItem(item = checkedItem, onCheckedChange = {})
        ImportantShoppingItem(item = importantItem, onCheckedChange = {})
    }
}