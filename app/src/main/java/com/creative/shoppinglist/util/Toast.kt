package com.creative.shoppinglist.util

import android.content.Context
import android.widget.Toast

fun GenerateToast(
    context: Context,
    text: String
) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}