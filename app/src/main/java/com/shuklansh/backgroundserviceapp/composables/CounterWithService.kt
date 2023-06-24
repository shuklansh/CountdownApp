package com.shuklansh.backgroundserviceapp.composables

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuklansh.backgroundserviceapp.ViewModel
import com.shuklansh.backgroundserviceapp.services.BackgroundService

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CounterWithService() {


    var count by remember {
        mutableStateOf(0)
    }

    var keybaordEnable by remember{
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


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        TextField(value = query, onValueChange = {
            query = it
        },
            enabled = keybaordEnable,
            keyboardActions = KeyboardActions(
                onGo = {
                    var limit = query.toInt()
                    var i = Intent(context, BackgroundService::class.java)
                    i.putExtra("valByUser", limit)
                    context.startService(i)
                    focuManager.clearFocus()
                    keybo?.hide()
                    keybaordEnable = false


                },
                onDone = {
                    var limit = query.toInt()
                    var i = Intent(context, BackgroundService::class.java)
                    i.putExtra("valByUser", limit)
                    context.startService(i)
                    focuManager.clearFocus()
                    keybo?.hide()
                    keybaordEnable = false
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

            Button(onClick = {
                var limit = query.toInt()
                var i = Intent(context, BackgroundService::class.java)
                i.putExtra("valByUser", limit)
                context.startService(i)
                focuManager.clearFocus()
                keybo?.hide()
                keybaordEnable = false
            }) {
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



fun Counter(time: String) : String  {
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