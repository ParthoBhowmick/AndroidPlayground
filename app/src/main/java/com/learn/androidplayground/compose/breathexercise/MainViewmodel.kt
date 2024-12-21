package com.learn.androidplayground.compose.breathexercise

import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(MainScreenViewState())
    val viewState = _viewState.asStateFlow()

    private var timerJob: Job? = null

    private var timeRemaining = TOTAL_SESSION_TIME
    private lateinit var vibrator: Vibrator

    fun onActionButtonClick(vibrator: Vibrator) {
        this.vibrator = vibrator
        if (!_viewState.value.isTimerRunning) {
            startExercise()
        } else {
            pauseExercise()
        }
    }


    private fun startExercise() {
        _viewState.update {
            it.copy(
                isTimerRunning = true,
                buttonText = "Pause"
            )
        }
        timerJob = viewModelScope.launch {
            while (timeRemaining > 0) {
                delay(1000)
                handleStateTime()
                timeRemaining -= 1
            }
            _viewState.update {
                it.copy(
                    isTimerRunning = false,
                    isExerciseFinished = true
                )
            }
        }
        timerJob?.start()
    }

    private suspend fun handleStateTime() {
        if (_viewState.value.currentStateTimeRemaining == 0) {
            val pattern1 = longArrayOf(0, 300)
            when (_viewState.value.currentState) {
                BreathState.INHALE -> {
                    _viewState.update {
                        it.copy(
                            currentState = BreathState.HOLD,
                            currentStateTimeRemaining = HOLD_DURATION_SECOND,
                            currentStateTimeLimit = HOLD_DURATION_SECOND,
                            stateToShow = BreathState.HOLD.displayState
                        )
                    }

                }

                BreathState.HOLD -> {
                    _viewState.update {
                        it.copy(
                            currentState = BreathState.EXHALE,
                            currentStateTimeRemaining = EXHALE_DURATION_SECOND,
                            currentStateTimeLimit = EXHALE_DURATION_SECOND,
                            stateToShow = BreathState.EXHALE.displayState
                        )
                    }
                }

                BreathState.EXHALE -> {
                    _viewState.update {
                        it.copy(
                            currentState = BreathState.INHALE,
                            currentStateTimeRemaining = INHALE_DURATION_SECOND,
                            currentStateTimeLimit = INHALE_DURATION_SECOND,
                            stateToShow = BreathState.INHALE.displayState
                        )
                    }
                }
            }
            vibrator.vibrate(pattern1, -1)
            delay(PROPAGATION_DELAY)
        }
        _viewState.update {
            it.copy(
                currentStateTimeRemaining = _viewState.value.currentStateTimeRemaining - 1
            )
        }
    }

    private fun pauseExercise() {
        _viewState.update {
            it.copy(
                isTimerRunning = false,
                buttonText = "Resume"
            )
        }
        timerJob?.cancel()
    }

    fun onResumeState() {
        if (_viewState.value.isTimerRunning) {
            startExercise()
        }
    }

    fun onPauseState() {
        if (_viewState.value.isTimerRunning) {
            pauseExercise()
        }
    }

    data class MainScreenViewState(
        val currentState: BreathState = BreathState.INHALE,
        val stateToShow: String = BreathState.INHALE.displayState,
        val isTimerRunning: Boolean = false,
        val timeLeftInSecond: Long = -1L,
        val currentStateTimeRemaining: Int = INHALE_DURATION_SECOND,
        val currentStateTimeLimit: Int = INHALE_DURATION_SECOND,
        val buttonText: String = "Start",
        val isExerciseFinished: Boolean = false
    )

    enum class BreathState(val displayState: String) {
        INHALE("Breath In"),
        HOLD("Hold Breath"),
        EXHALE("Breath Out")
    }

    companion object {
        const val INHALE_DURATION_SECOND = 4
        const val HOLD_DURATION_SECOND = 4
        const val EXHALE_DURATION_SECOND = 8
        const val PROPAGATION_DELAY = 450L
        const val TOTAL_SESSION_TIME = 5 * 60
    }
}