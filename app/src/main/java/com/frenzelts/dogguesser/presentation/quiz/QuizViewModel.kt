package com.frenzelts.dogguesser.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frenzelts.dogguesser.domain.usecase.GetQuizQuestion
import com.frenzelts.dogguesser.domain.model.QuizQuestion
//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor (
    private val getQuizQuestion: GetQuizQuestion
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var score = 0
    private var streak = 0

    init { loadQuestion() }

    fun loadQuestion() {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            try {
                val q = getQuizQuestion()
                _uiState.value = QuizUiState.Ready(
                    question = q,
                    score = score,
                    streak = streak,
                    lastAnswer = null
                )
            } catch (t: Throwable) {
                _uiState.value = QuizUiState.Error(t.message ?: "Unknown error")
            }
        }
    }

    /** Return boolean so controller can handle UI effects */
    fun submitAnswer(option: QuizQuestion.Option): Boolean {
        val current = _uiState.value as? QuizUiState.Ready ?: return false
        val isCorrect = option.isCorrect

        if (isCorrect) {
            streak += 1
            score += 10 + streak * 2
        } else {
            streak = 0
        }

        _uiState.value = current.copy(
            score = score,
            streak = streak,
            lastAnswer = option
        )

        return isCorrect
    }
}
