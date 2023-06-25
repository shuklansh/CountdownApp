package com.shuklansh.backgroundserviceapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import com.shuklansh.backgroundserviceapp.CounterNotififcationService
import com.shuklansh.backgroundserviceapp.R
import com.shuklansh.backgroundserviceapp.ViewModel
import com.shuklansh.backgroundserviceapp.composables.CounterWithService
import kotlinx.coroutines.*

class BackgroundService : Service() {

    lateinit var theydontknowmeson: MediaPlayer
    var count = 0
    var isRunning = false
    var job: Job? = null
    lateinit var service : CounterNotififcationService



    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        service = CounterNotififcationService(this)
        theydontknowmeson = MediaPlayer.create(this, R.raw.audio)
        theydontknowmeson.isLooping = true
        theydontknowmeson.setVolume(100F, 100F)
        Log.d("@@@", "SERVICE STARTED")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null) {
            count = intent.getIntExtra("valByUser", 0) * 60
        }



        isRunning = true
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isRunning) {

                val intent = Intent("counter_screen").apply {
                    putExtra("count", count)
                }

                delay(1000L)
                count--
                sendBroadcast(intent)
                service.showNotifications(count+1)

                if (count < 0) {
                    theydontknowmeson.start()
                    job?.cancel()
                    isRunning=false
                }
            }

        }

//        val CHANNEL_ID = "CountDownService"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            var channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_ID,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//
//            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
//            val notification = Notification.Builder(
//                this,
//                CHANNEL_ID
//            ).setContentText(" remaining : $count")
//                .setContentTitle("You have $count seconds left")
//                .setSmallIcon(R.drawable.ic_baseline_timer_24)
//
//            startForeground(1001,notification.build())
//
//        }





        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        count = 0
        theydontknowmeson.pause()
        theydontknowmeson.stop()
        theydontknowmeson.release()
        job?.cancel()
        isRunning = false
    }

}