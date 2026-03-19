package com.creative.shoppinglist.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.creative.shoppinglist.MainActivity

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Handle Dismiss Action
        if (intent.action == ACTION_DISMISS) {
            val id = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
            if (id != -1) {
                notificationManager.cancel(id)
            }
            return
        }

        // Check for POST_NOTIFICATIONS permission on API 33+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val itemName = intent.getStringExtra("ITEM_NAME") ?: "Shopping Item"
        val channelId = "shopping_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Shopping Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for shopping items"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Use the item name's hash as an ID so we don't spam multiple notifications for the same item
        val notificationId = itemName.hashCode()

        // Create the Dismiss intent
        val dismissIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_DISMISS
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create an intent for when the notification itself is clicked
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Shopping Reminder")
            .setContentText("Don't forget to buy: $itemName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setOngoing(true) // Standard way to make it sticky
            .setAutoCancel(false) // Don't dismiss when clicked
            .setContentIntent(contentPendingIntent)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Dismiss",
                dismissPendingIntent
            )
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val ACTION_DISMISS = "com.creative.shoppinglist.ACTION_DISMISS"
        const val EXTRA_NOTIFICATION_ID = "notification_id"
    }
}