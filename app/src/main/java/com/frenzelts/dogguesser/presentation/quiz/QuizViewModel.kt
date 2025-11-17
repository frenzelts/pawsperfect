package com.frenzelts.dogguesser.presentation.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frenzelts.dogguesser.domain.usecase.GetQuizQuestion
import com.frenzelts.dogguesser.domain.model.QuizQuestion
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor (
    private val getQuizQuestion: GetQuizQuestion
) : ViewModel() {

    var quizState by mutableStateOf<QuizUiState>(QuizUiState.Loading)
    var score by mutableStateOf<Int>(0)
    var streak by mutableStateOf<Int>(0)

    init { loadQuestion() }

    fun loadQuestion() {
        viewModelScope.launch {
            quizState = QuizUiState.Loading
            try {
                val question = getQuizQuestion()
                quizState = QuizUiState.Ready(question = question)
            } catch (t: Throwable) {
                quizState = QuizUiState.Error(t.message ?: "Unknown error")
            }
        }
    }

    fun submitAnswer(option: QuizQuestion.Option) {
        val currentState = quizState as? QuizUiState.Ready ?: return
        quizState = currentState.copy(
            selectedOption = option
        )

        val isCorrect = option.isCorrect
        if (isCorrect) {
            streak += 1
            score += (10 + streak * 2)
        } else {
            streak = 0
        }
    }
}
