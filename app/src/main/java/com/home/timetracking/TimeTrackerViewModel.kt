import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.home.timetracking.Session
import com.home.timetracking.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.collections.map

class TimeTrackerViewModel : ViewModel() {

    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    private val json = Json { ignoreUnknownKeys = true }
    val sessions: StateFlow<List<Session>> = _sessions

    private val _currentSessionStart = MutableStateFlow<Long?>(null)
    val currentSessionStart: StateFlow<Long?> = _currentSessionStart

    private val _currentSessionLength = MutableStateFlow<Long?>(null)

    val currentSessionTime: StateFlow<Long?> = _currentSessionLength
    fun startSession() {
        if (_currentSessionStart.value != null) return
        val start = System.currentTimeMillis()
        _currentSessionStart.value = start
        currentTimeMillis()

        _sessions.update {
            it + Session(startTimeMillis = start, endTimeMillis = null, synced = false)
        }
    }

    fun removeSession(start: Long, stop: Long) {
        _sessions.update { it.filterNot { it -> it.startTimeMillis == start && it.endTimeMillis == stop } }
    }
    fun clearSessions() {
        _sessions.update {
            emptyList()
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
        _currentSessionLength.value = null
    }

    fun totalTimeTodayMillis(): Long {
        return _sessions.value.sumOf { it.durationMillis }
    }

    fun currentTimeMillis() {
        if (_currentSessionStart.value == null) {
            return
        }

        _currentSessionLength.value =  System.currentTimeMillis() - _currentSessionStart.value!!
    }

    fun currTimeMillis(): Long {
        if (_currentSessionLength.value == null) {
            return 0
        }
        return _currentSessionLength.value!!
    }

    suspend fun saveList(context: Context) {
        val key = stringPreferencesKey("sessions")
        context.dataStore.edit {
            prefs -> prefs[key] = json.encodeToString(_sessions.value)
        }
    }

    suspend fun removeSessions(context: Context) {
        val key = stringPreferencesKey("sessions")
        context.dataStore.edit {
                prefs -> prefs[key] = json.encodeToString(_sessions.value)
        }
    }
}
