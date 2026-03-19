package com.creative.shoppinglist.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.creative.shoppinglist.domain.model.ShoppingItem
import com.creative.shoppinglist.notifications.scheduleNotification
import com.creative.shoppinglist.ui.viewmodels.ShoppingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import com.creative.shoppinglist.util.GenerateToast

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
fun TopAppBarShoppingItem() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Shopping List", style = MaterialTheme.typography.headlineSmall
            )
        })
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAddItem(
    viewMode: ShoppingViewModel = hiltViewModel(),
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

    var selectedDateTimeText by remember { mutableStateOf("Set reminder time") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Item", style = MaterialTheme.typography.titleLarge)

        ItemNameInput(
            itemName = itemName,
            errorText = "Item name cannot be empty",
            label = "Item Name",
            isNameEmpty = isNameEmpty,
            onValueChange = {
                itemName = it
                if (it.isNotBlank()) isNameEmpty = false
                if (it.isBlank()) isNameEmpty = true
            },
            onDone = {
                focusManager.clearFocus()
            })

        ToggleSettingRow(
            message = "Mark as Important?",
            isChecked = isImportant,
            onCheckedChange = {
                isImportant = it
                if (!it) isRemindable = false
            }
        )

        if (isImportant) {
            ToggleSettingRow(
                message = "Mark as Remindable?",
                isChecked = isRemindable,
                onCheckedChange = {
                    isRemindable = it
                    if (it) showDatePicker = true
                }
            )
        }

        if (isRemindable) {
            ReminderTextButton(
                message = selectedDateTimeText,
                onClick = { showDatePicker = true }
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = @androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM) {
                itemName = itemName.trim()
                remindMeOn = System.currentTimeMillis()
                if (itemName.isEmpty()) {
                    isNameEmpty = true
                    return@Button
                }
                val newItem = ShoppingItem(
                    id = null,
                    name = itemName,
                    isImportant = isImportant,
                    remindMeOn = if (isRemindable) remindMeOn else 0L,
                    isRemindable = isRemindable,
                    isChecked = false
                )
                var message = ""
                if (isRemindable) {
                    try {
                        scheduleNotification(
                            context = context,
                            timeInMillis = remindMeOn,
                            itemName = itemName
                        )
                        message = " And Notification is Set"
                    } catch (ex: SecurityException) {
                        ex.printStackTrace()
                    }
                }
                viewMode.insertShoppingItem(
                    newItem
                )
                itemName = ""
                isNameEmpty = false
                isImportant = false
                isRemindable = false
                remindMeOn = 0L
                selectedDateTimeText = ""
                onDismiss(message)
            }) {
            Text(text = "Add Item")
        }

        if (showDatePicker) {
            MyDatePickerDialog(
                labelNext = "Next",
                labelCancel = "Cancel",
                datePickerState,
                onDismissButton = {
                    showDatePicker = false
                    isRemindable = false
                },
                onConfirmButton = {
                    showDatePicker = false
                    showTimePicker = true
                },
                onPickerDismiss = {
                    showDatePicker = false
                    showTimePicker = false
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
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
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
            val cal = Calendar.getInstance()
            datePickerState.selectedDateMillis?.let { cal.timeInMillis = it }
            cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            cal.set(Calendar.MINUTE, timePickerState.minute)

            val formatter = SimpleDateFormat("MMM dd, HH:mm")
            val formattedDate = formatter.format(cal.time)
            onConfirm(formattedDate, cal.timeInMillis)
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
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
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
    routesString: String,
    type: String,
    icon: ImageVector
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { it.route == routesString } == true,
        onClick = {
            navController.navigate(routesString) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = type
            )
        },
        label = {
            Text(text = "$type Items")
        }
    )
}

@Composable
fun RegularShoppingItem(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (item.isChecked) {
            // "Disabled" look: Lower alpha or a muted surface color
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "itemBackgroundColor"
    )

    val contentAlpha = if (item.isChecked) 0.5f else 1.0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = item.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha)
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
    }
}

@Composable
fun ImportantShoppingItem(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (item.isChecked) {
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.errorContainer
        },
        label = "importantBackgroundColor"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.padding(start = 8.dp),
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Important Item",
            tint = if (item.isChecked) MaterialTheme.colorScheme.error.copy(alpha = 0.4f) else MaterialTheme.colorScheme.error
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = if (item.isChecked) 0.5f else 1f)
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (item.isRemindable) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Reminder",
            )
        }
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
    }
}

//@Composable
//@Preview(showBackground = true)
//fun RegularShoppingItemPreview() {
//    val dummyItems = (1..10).toList()
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Use the 'items' function provided by LazyColumn
//        items(dummyItems.size) { index ->
//            RegularShoppingItem(
//                itemName = "Item Number ${dummyItems[index]}",
//                onCheckedChange = {}
//            )
//        }
//    }
//}
//
//@Composable
//@Preview(showBackground = true)
//fun ImportantShoppingItemPreview() {
//    val dummyItems = (1..10).toList()
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Use the 'items' function provided by LazyColumn
//        items(dummyItems.size) { index ->
//            ImportantShoppingItem(
//                itemName = "Item Number ${dummyItems[index]}",
//                onCheckedChange = {}
//            )
//        }
//    }
//}
