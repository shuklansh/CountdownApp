package com.shuklansh.backgroundserviceapp.composables

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YouTubeUriExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.RepeatMode
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.shuklansh.backgroundserviceapp.R
import com.shuklansh.backgroundserviceapp.services.BackgroundService


@OptIn(ExperimentalComposeUiApi::class)
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


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val youtubeLink = "http://youtube.com/watch?v=BM-Yf-DXhMM"

        val exoPlayer = remember { ExoPlayer.Builder(context).build().apply {

            val dataSourceFactory : DataSource.Factory = DefaultDataSourceFactory(
                context, Util.getUserAgent(context, context.packageName)
            )
            val mediaItem = MediaItem.fromUri("https://rr6---sn-ci5gup-a3vs.googlevideo.com/videoplayback?expire=1687791290&ei=WlKZZKlPzonzBPiqq-gG&ip=170.247.220.71&id=o-AN_x8OxgR7IqBAMrhQiVZfNhoTdnN0DsaDVsZ6mYzZ2m&itag=22&source=youtube&requiressl=yes&spc=qEK7ByHj7zCUbake9MVzSOEJlUnodBUCPOu5W6_weg&vprv=1&svpuc=1&mime=video%2Fmp4&ns=rwyPhGyDs9eMKD-Cd-VT4wUN&cnr=14&ratebypass=yes&dur=68.893&lmt=1673526261383648&fexp=24007246,24350018,24362685&beids=24350018&c=WEB&txp=5311224&n=z8s38jUJWNjoKQ&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Cns%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRgIhAOOlwduii75J6IdUM1eEn5LoYLMQ1ubpLOTIRGVSDyTdAiEA8esTJaOktCVe-XBgycZ4kJ457CKWn4K61GPVSivMKfw%3D&title=They%20Don%27t%20Know%20Me%20Son%20%7C%20David%20Goggins&redirect_counter=1&rm=sn-ab5es77z&req_id=846e0b535594a3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=nf&mip=110.227.113.170&mm=31&mn=sn-ci5gup-a3vs&ms=au&mt=1687784967&mv=m&mvi=6&pl=22&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRAIgPkkPWHus7G9VG09iwbgEzCp4MLq3WteYS2n5EW5oBrACIByvziGCFnCBn2OZaDeh8_HzxvGXplg57_HAg3qP1BjL")
            val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            this.setMediaSource(source)
            this.prepare()


        } }
//
//        val exoPlayer = remember {
//            ExoPlayer.Builder(context).build().apply {
//                setMediaItem(MediaItem.fromUri(url))
//                repeatMode = ExoPlayer.REPEAT_MODE_ALL
//                playWhenReady = playWhenReady
//                volume = mutedVol
//                prepare()
//                play()
//            }
//        }

//        val exoPlayer = remember { ExoPlayer.Builder(context).build().apply{
//            repeatMode = ExoPlayer.REPEAT_MODE_ALL
//            playWhenReady = pwhenr
//            volume = mutedVol
//            prepare()
//            play()
//        }}

        //exoPlayer = ExoPlayer.Builder(context).build()


//        val youtubeLink = "https://youtu.be/BM-Yf-DXhMM"


//        object : YouTubeExtractor(context) {
//            override fun onExtractionComplete(
//                ytFiles: SparseArray<YtFile>?,
//                videoMeta: VideoMeta?
//            ) {
//                if (ytFiles != null) {
//                    val itag = 137
//                    val audioTag = 140
//                    val videoUrl = ytFiles[itag].url
//                    val audioUrl = ytFiles[audioTag].url
//
//                    val audioSource: MediaSource = ProgressiveMediaSource.Factory(
//                        DefaultHttpDataSource.Factory()
//                    ).createMediaSource(MediaItem.fromUri(audioUrl))
//
//                    val videoSource: MediaSource = ProgressiveMediaSource.Factory(
//                        DefaultHttpDataSource.Factory()
//                    ).createMediaSource(MediaItem.fromUri(videoUrl))
//
//
//                    exoPlayer.setMediaSource(
//                        MergingMediaSource(
//                            true, videoSource, audioSource
//                        ), true
//                    )
//
////                        setMediaItem(MediaItem.fromUri(youtubeLink))
//
//
//
//
//
//                }
//            }
//        }.extract(youtubeLink, false, true)

//
//        object : YouTubeExtractor(context) {
//            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>, vMeta: VideoMeta) {
//                if (ytFiles != null) {
//                    val itag = 22
//                    val downloadUrl = ytFiles[itag].url
//
//                }
//            }
//        }.extract(youtubeLink, false,true)


        if (count == 0) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            (player as ExoPlayer).playWhenReady = true
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
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