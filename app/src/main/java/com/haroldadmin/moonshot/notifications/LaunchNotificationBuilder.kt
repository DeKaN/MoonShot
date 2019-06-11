package com.haroldadmin.moonshot.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.haroldadmin.moonshot.MainActivity
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.utils.GlideApp

object LaunchNotificationBuilder {
    fun create(context: Context): Notification {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val preferences = context.getSharedPreferences(MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)
        val launchName = preferences.getString(KEY_LAUNCH_NAME, "A launch")
        val launchSite = preferences.getString(KEY_LAUNCH_SITE, "a launch site")
        val launchDate = preferences.getString(KEY_LAUNCH_DATE, "soon")
        val missionPatch = preferences.getString(KEY_LAUNCH_MISSION_PATCH, "")

        val largeIcon: Bitmap = if (missionPatch.isNullOrBlank()) {
            GlideApp.with(context)
                .load(R.drawable.ic_round_rocket_small)
                .submit()
                .get()
                .toBitmap()
        } else {
            GlideApp.with(context)
                .asBitmap()
                .load(missionPatch)
                .submit()
                .get()
        }

        createChannel(context)

        return NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(largeIcon)
            .setContentTitle("$launchName Launch")
            .setContentText("Launches at $launchSite, $launchDate")
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun createChannel(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "SpaceX Launch notifications",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(channel)
        }
    }
}