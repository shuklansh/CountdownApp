package com.shuklansh.backgroundserviceapp.composables

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
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
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.common.reflect.Reflection.getPackageName
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

    val url = "android.resource://" + context.packageName + "/${R.raw.goggins}"

    var muted by remember {
        mutableStateOf(false)
    }
    var mutedVol by remember {
        mutableStateOf(0f)
    }


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(url))
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                playWhenReady = playWhenReady
                volume = mutedVol
                prepare()
                play()
            }
        }

        if (count == 0) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                AndroidView(factory = { context ->
                    PlayerView(context).apply {

                        player = exoPlayer
                    }

                },
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    )
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(),
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
                            painter = painterResource(id = if(muted){R.drawable.ic_baseline_volume_off_24}else{R.drawable.ic_baseline_volume_up_24},),
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