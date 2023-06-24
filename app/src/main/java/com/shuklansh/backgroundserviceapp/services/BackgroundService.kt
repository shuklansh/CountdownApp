package com.shuklansh.backgroundserviceapp.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.shuklansh.backgroundserviceapp.R
import com.shuklansh.backgroundserviceapp.ViewModel
import com.shuklansh.backgroundserviceapp.composables.CounterWithService
import kotlinx.coroutines.*

class BackgroundService : Service() {

    lateinit var theydontknowmeson : MediaPlayer
    var count = 89
    var isRunning = false
    var job : Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        theydontknowmeson= MediaPlayer.create(this, R.raw.audio)
        theydontknowmeson.isLooping = true
        theydontknowmeson.setVolume(100F, 100F)
        Log.d("@@@","SERVICE STARTED")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent!=null){
            count = intent.getIntExtra("valByUser",10) * 60
        }

        isRunning = true
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isRunning){


                val intent = Intent("counter_screen").apply {
                    putExtra("count",count)
                }
                delay(1000L)
                count--
                sendBroadcast(intent)
                if(count <0){
                    theydontknowmeson.start()
                    job?.cancel()
                }
            }

        }


        return START_STICKY
    }



    override fun onDestroy() {
        super.onDestroy()
        theydontknowmeson.pause()
        theydontknowmeson.stop()
        theydontknowmeson.release()
        job?.cancel()
        isRunning = false
    }


}