package com.shuklansh.backgroundserviceapp.composables

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.util.SparseArray
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.maxrave.kotlinyoutubeextractor.*
import com.maxrave.kotlinyoutubeextractor.State
import com.shuklansh.backgroundserviceapp.R
import com.shuklansh.backgroundserviceapp.services.BackgroundService
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
@Composable
fun CounterWithService() {

    var count by remember {
        mutableStateOf(0)
    }

    var keybaordEnable by remember {
        mutableStateOf(true)
    }

    var query by remember {
        mutableStateOf("")
    }

    var context = LocalContext.current
    var intentService = remember {
        Intent(context, BackgroundService::class.java)
    }

    val broadcast = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                count = intent?.getIntExtra("count", 10) ?: 10
            }

        }
    }

    val focuManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        val intentfilter = IntentFilter("counter_screen")
        context.registerReceiver(broadcast, intentfilter)
    }

    val keybo = LocalSoftwareKeyboardController.current

//    val url = "android.resource://" + context.packageName + "/${R.raw.goggins}"


    var muted by remember {
        mutableStateOf(false)
    }
    var mutedVol by remember {
        mutableStateOf(0f)
    }

    val playbackpos: Long = 0
    val currentWindow = 0
    val pwhenr = true

//    var streamUrl : String? by remember { mutableStateOf(null) }
    var urlofvidAvailable by remember { mutableStateOf(false) }



    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val youtubeLink = "http://youtube.com/watch?v=BM-Yf-DXhMM"

        val exoPlayer = remember { ExoPlayer.Builder(context).build() }

//        val exoPlayer = remember { ExoPlayer.Builder(context).build().apply {
//
//            val dataSourceFactory : DataSource.Factory = DefaultDataSourceFactory(
//                context, Util.getUserAgent(context, context.packageName)
//            )
//            val mediaItem = MediaItem.fromUri("https://rr8---sn-ci5gup-qxay.googlevideo.com/videoplayback?expire=1687986665&ei=iU2cZP3uKsq94t4PxrS_wA4&ip=103.121.205.48&id=o-AE35EaFX_BmIz0Gz8Utcx8LJ9cBZ8GEd_2AK9y7r7hGI&itag=22&source=youtube&requiressl=yes&spc=qEK7B4AmwmgxGzoA__ZT4DxW7_gosfs6VLcJLT9mmw&vprv=1&svpuc=1&mime=video%2Fmp4&ns=f_BYU8UlEbMc51BwHxqcQoEN&cnr=14&ratebypass=yes&dur=68.893&lmt=1673526261383648&fexp=24007246,24363391&c=WEB&txp=5311224&n=SfhuXLfW8SVEtQ&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Cns%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIhAO0yNAy7BMRRjX3wgnsJivmcTuDc7Ar-k36ms0UhRpiBAiBLTKmcjwMxihYJuTSWM1w7T5Wj6OlKniWTk1EaukzktA%3D%3D&title=They%20Don%27t%20Know%20Me%20Son%20%7C%20David%20Goggins&rm=sn-gxo0cgv5qc5oq-3o0e7z,sn-qxaed7z&req_id=a51fa216023ea3ee&redirect_counter=2&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=nf&mip=171.76.13.68&mm=29&mn=sn-ci5gup-qxay&ms=rdu&mt=1687976019&mv=m&mvi=8&pcm2cms=yes&pl=20&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pcm2cms,pl&lsig=AG3C_xAwRgIhANUPLQqyv553gRoUED48MBNQ90C9ULARuoxwcWttv4gZAiEA65gPKLRpLtW51_4FJX_dMIU6WpLZzs8IujzEKJnibko%3D")
//            val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//            this.setMediaSource(source)
//            this.prepare()
//
//
//        } }

        var videoId = "BM-Yf-DXhMM"
        val yt = YTExtractor(con = context, CACHING = true, LOGGING = true)
        var ytFiles: SparseArray<YtFile>? = null
        var videoMeta: VideoMeta? by remember{ mutableStateOf(null) }

        LaunchedEffect(key1 = true) {
            yt.extract(videoId)
            if (yt.state == State.SUCCESS) {
                ytFiles = yt.getYTFiles()
                videoMeta = yt.getVideoMeta()
                var ytFile = ytFiles?.get(17)
                val audioYtFiles =
                    ytFiles?.getAudioOnly()?.bestQuality()
                val videoYtFiles  =
                    ytFiles?.getVideoOnly()?.bestQuality()
                var streamUrl = ytFile?.url


                if (videoYtFiles != null ) {

                    val mediaItem = MediaItem.fromUri(videoYtFiles.url!!)
                    Log.d("besturl", videoYtFiles.url!!)
                    Log.d("streamurl", streamUrl.toString())
                    //            val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    urlofvidAvailable = true
                } else {
                    Log.d("%%%%", "null streamurl")
                }
            }
        }



        if (count == 0) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                if (urlofvidAvailable != false ) {
                    Column(Modifier.fillMaxWidth().height(286.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                        AndroidView(
                            factory = { context ->
                                PlayerView(context).apply {
                                    exoPlayer.playWhenReady = true
                                    player = exoPlayer


                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(244.dp)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Text(text = videoMeta!!.title.toString())
                    }
                } else {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(286.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                        )

                    }

                }
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    IconButton(
                        onClick = {
                            if (muted) {
                                mutedVol = 1f
                                exoPlayer.volume = mutedVol
                                muted = false
                            } else {
                                mutedVol = 0f
                                exoPlayer.volume = mutedVol
                                muted = true
                            }

                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (muted) {
                                    R.drawable.ic_baseline_volume_off_24
                                } else {
                                    R.drawable.ic_baseline_volume_up_24
                                }
                            ),
                            contentDescription = "audio mute toggle"
                        )
                    }

                }

            }
        } else {
            Text(text = "Keep pushing")
            exoPlayer.volume = 0f
            exoPlayer.pause()
        }

        Spacer(Modifier.height(12.dp))


        TextField(value = query, onValueChange = {
            query = it
        },
            enabled = keybaordEnable,
            colors = TextFieldDefaults.textFieldColors(
                //backgroundColor = Color(204, 204, 204, 255),
                cursorColor = Color.Black,
                //textColor = Color.Black,
                //disabledLabelColor = Color(66, 66, 66, 255),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            label = { Text(text = "Enter countdown time in minutes") },
            keyboardActions = KeyboardActions(
                onGo = {
                    if (!query.isBlank()) {
                        var limit = query.toInt()
                        var i = Intent(context, BackgroundService::class.java)
                        i.putExtra("valByUser", limit)
                        context.startService(i)
                        focuManager.clearFocus()
                        keybo?.hide()
                        keybaordEnable = false
                    } else {
                        Toast.makeText(
                            context,
                            "Please Enter a valid time limit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onDone = {
                    if (!query.isBlank()) {
                        var limit = query.toInt()
                        var i = Intent(context, BackgroundService::class.java)
                        i.putExtra("valByUser", limit)
                        context.startService(i)
                        focuManager.clearFocus()
                        keybo?.hide()
                        keybaordEnable = false
                    } else {
                        Toast.makeText(
                            context,
                            "Please Enter a valid time limit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1,
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (count == 0) {
                "Over"
            } else {
                Counter(time = "$count")
            }, style = TextStyle(
                fontSize = 30.sp, fontWeight = FontWeight.W400
            )
        )

        Spacer(Modifier.height(12.dp))

        Row() {
            Button(onClick =
            {
                if (!query.isBlank()) {
                    var limit = query.toInt()
                    var i = Intent(context, BackgroundService::class.java)
                    i.putExtra("valByUser", limit)
                    context.startService(i)
                    focuManager.clearFocus()
                    keybo?.hide()
                    keybaordEnable = false
                } else {
                    Toast.makeText(
                        context,
                        "Please Enter a valid time limit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            ) {
                Text("Start Service")
            }

            Spacer(Modifier.width(12.dp))

            Button(onClick = {
                context.stopService(intentService)
                keybaordEnable = true
            }) {
                Text("Stop Service")
            }

        }


    }

}


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