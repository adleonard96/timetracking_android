package com.home.timetracking

import TimeTrackerViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


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
fun TimeTrackerScreen(viewModel: TimeTrackerViewModel = viewModel()) {
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionStart by viewModel.currentSessionStart.collectAsState()
    val currentSessionTime by viewModel.

    val totalTime = remember(sessions, currentSessionStart) {
        viewModel.totalTimeTodayMillis()
    }

    val currentTime = remember {  }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "Total today: ${formatDuration(totalTime)}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text (
            text = "Current time: ${formatDuration(millis = )}"
        )
        Button(
            onClick = {
                if (currentSessionStart == null) {
                    viewModel.startSession()
                } else {
                    viewModel.stopSession()
                }
            }
        ) {
            Text(if (currentSessionStart == null) "Start" else "Stop")
        }

        if (currentSessionStart != null) {
            Text(
                text = "Started at: ${formatTime(currentSessionStart!!)}"
            )
        }

    }
}
