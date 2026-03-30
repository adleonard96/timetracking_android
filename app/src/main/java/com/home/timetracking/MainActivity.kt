package com.home.timetracking

import TimeTrackerViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TimeTrackerScreen()
            }
        }

    }

}

@Composable
fun TimeTrackerScreen(
    viewModel: TimeTrackerViewModel = viewModel(),
    stopWatchViewModel: StopwatchViewModel = viewModel(),
) {
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionStart by viewModel.currentSessionStart.collectAsState()
    val elapsedTime = stopWatchViewModel.elapsedTime
    val gradientBrush = Brush.linearGradient(listOf(Color.Black, Color.Red))

    val totalTime = remember(sessions, currentSessionStart) {
        viewModel.totalTimeTodayMillis()
    }

    val currentTime = remember {
        viewModel.currTimeMillis()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .fillMaxSize()
            .padding(top = 25.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
//            text = "Total today: ${formatDuration(totalTime)}",
            text = "Hours today: %.5f".format(elapsedTime / 3_600_000.0),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Row() {
            Button(
                onClick = {
                    if (currentSessionStart == null) {
                        viewModel.startSession()
                        stopWatchViewModel.start()
                    } else {
                        viewModel.stopSession()
                        stopWatchViewModel.stop()
                    }
                }
            ) {
                Text(if (currentSessionStart == null) "Start" else "Stop")
            }
            Button(
                onClick = {
                    viewModel.clearSessions()
                    stopWatchViewModel.reset()
                }, modifier = Modifier.padding(start = 4.dp)
            ) {
                Text("Clear")
            }
            val context = LocalContext.current
            Button(

                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val successful = withContext(Dispatchers.IO) {
                            syncSessions(context, viewModel.sessions.value)
                        }
                        if (successful) {
                            viewModel.clearSessions()
                            stopWatchViewModel.reset()
                            Toast.makeText(context, "Sync was successful!", Toast.LENGTH_LONG).show()
                        }
                    }
                }, modifier = Modifier.padding(start = 4.dp)
            ) {
                Text("Sync")
            }
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            sessions.reversed().forEach { session ->
                val shape = RoundedCornerShape(9.dp)
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(width = 2.dp, Color.Black, shape = RoundedCornerShape(9.dp))
                        .background(Color.White, shape = shape)
                        .padding(8.dp)
                ) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val timeFormat = SimpleDateFormat("HH:mm:ss")
                    val startDate = Date(session.startTimeMillis)
                    val stopped = session.endTimeMillis != null
                    Text(text = "Date: ${sdf.format(startDate)}")

                    session.endTimeMillis?.let { endMillis ->
                        val endDate = Date(endMillis)
                        Text(text = "Stop: ${timeFormat.format(endDate)}")
                    }
                    Text(text = "Start: ${timeFormat.format(startDate)}")

                    if (session.endTimeMillis != null) {
                        Button(
                            onClick = {
                                val endMillis = session.endTimeMillis!!

                                viewModel.removeSession(
                                    session.startTimeMillis,
                                    endMillis
                                )

                                stopWatchViewModel.deduct(endMillis - session.startTimeMillis)
                            }
                        ) {
                            Text(text = "Delete Session")
                        }
                    }
                }
            }
        }
    }
}
