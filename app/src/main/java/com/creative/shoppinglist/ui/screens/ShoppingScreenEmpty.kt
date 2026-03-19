package com.creative.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingScreenEmpty(
    message: String,
    icon: ImageVector
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Empty list icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Text(
                text = "Please add items to the list.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShoppingScreenEmptyPreview() {
    ShoppingScreenEmpty(
        message = "No important shopping items found.",
        icon = Icons.AutoMirrored.Rounded.List
    )
}