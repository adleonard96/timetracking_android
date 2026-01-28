import androidx.lifecycle.ViewModel
import com.home.timetracking.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.map

class TimeTrackerViewModel : ViewModel() {

    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions

    private val _currentSessionStart = MutableStateFlow<Long?>(null)
    val currentSessionStart: StateFlow<Long?> = _currentSessionStart

    fun startSession() {
        if (_currentSessionStart.value != null) return

        val start = System.currentTimeMillis()
        _currentSessionStart.value = start

        _sessions.update {
            it + Session(startTimeMillis = start, endTimeMillis = null)
        }
    }

    fun stopSession() {
        val start = _currentSessionStart.value ?: return

        _sessions.update { sessions ->
            sessions.map {
                if (it.startTimeMillis == start && it.endTimeMillis == null) {
                    it.copy(endTimeMillis = System.currentTimeMillis())
                } else it
            }
        }

        _currentSessionStart.value = null
    }

    fun totalTimeTodayMillis(): Long {
        return _sessions.value.sumOf { it.durationMillis }
    }
}
