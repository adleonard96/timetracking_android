package com.home.timetracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchViewModel : ViewModel(){
    var running by mutableStateOf(false)
    var elapsedTime by mutableLongStateOf(0L)

    private var startTime = 0L

    fun start() {
        startTime = System.currentTimeMillis() - elapsedTime
        running = true

        viewModelScope.launch {
            while (running) {
                elapsedTime = System.currentTimeMillis() - startTime
                delay(50)
            }
        }
    }

    fun stop() {
        running = false
    }

    fun reset() {
        running = false
        elapsedTime = 0L
    }
}