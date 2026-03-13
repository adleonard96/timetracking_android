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
import kotlinx.coroutines.delay


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
fun TimeTrackerScreen(viewModel: TimeTrackerViewModel = viewModel(), stopWatchViewModel: StopwatchViewModel = viewModel()) {
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionStart by viewModel.currentSessionStart.collectAsState()
    val currentSessionTime by viewModel.currentSessionTime.collectAsState()
    val elapsedTime = stopWatchViewModel.elapsedTime

    val totalTime = remember(sessions, currentSessionStart) {
        viewModel.totalTimeTodayMillis()
    }

    val currentTime = remember {
        viewModel.currTimeMillis()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
//            text = "Total today: ${formatDuration(totalTime)}",
            text = "Time: ${elapsedTime / 1000}.${(elapsedTime % 1000)/100}",
            style = MaterialTheme.typography.headlineMedium
        )
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

        sessions.forEach { session ->
            Text(text = "${session.startTimeMillis}")
        }
    }
}
