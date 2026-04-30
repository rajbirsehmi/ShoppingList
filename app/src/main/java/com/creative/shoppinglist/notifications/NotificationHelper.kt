package com.creative.shoppinglist.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun scheduleNotification(context: Context, timeInMillis: Long, itemName: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("ITEM_NAME", itemName)
        action = "com.creative.shoppinglist.ACTION_REMINDER"
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        timeInMillis.hashCode(),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    } catch (ex: SecurityException) {
        ex.printStackTrace()
    }
}