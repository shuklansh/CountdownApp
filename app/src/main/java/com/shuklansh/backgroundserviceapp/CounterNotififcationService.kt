package com.shuklansh.backgroundserviceapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shuklansh.backgroundserviceapp.services.BackgroundService

class CounterNotififcationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val activityIntent = Intent(context, MainActivity::class.java)

//    val activityPendingIntent = PendingIntent.getService(
//        context,
//        1,
//        activityIntent,
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
//    )

//    val showCounterIntent = PendingIntent.getBroadcast(
//        context,
//        2,
//        Intent(context, BroadcastReciever::class.java),
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
//    )

    val stopService = PendingIntent.getActivity(
        context,
        3,
        activityIntent,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
    )
    fun Counter(time: String): String {
//
        val name = time.toInt()
        //val time = SimpleTimeZone.

        var s = name % 60
        var h = name / 60
        var m = h % 60
        h = h / 60
        var text = "%02d:%02d:%02d".format(h, m, s)
        return text

//    Text(text = time)
    }


    fun showNotifications(counter: Int) {
        val notification = NotificationCompat.Builder(
            context,
            COUNTER_CHANNEL
        )
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setContentTitle("Countdown Timer")
            .setContentText("${Counter(counter.toString())} remaining")
            .setContentIntent(stopService)
//            .addAction(
//                R.drawable.ic_baseline_timer_24,
//                "Open",
//                stopService
//            )
            .build()

        notificationManager.notify(
            3,
            notification
        )

    }

    companion object {
        const val COUNTER_CHANNEL = "counter_channel"
    }

}