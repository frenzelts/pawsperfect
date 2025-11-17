package com.frenzelts.dogguesser.presentation.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frenzelts.dogguesser.domain.usecase.GetQuizQuestion
import com.frenzelts.dogguesser.domain.model.QuizQuestion
import com.frenzelts.dogguesser.presentation.quiz.ui.OptionLayoutMode
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor (
    private val getQuizQuestion: GetQuizQuestion
) : ViewModel() {

    var quizState by mutableStateOf<QuizUiState>(QuizUiState.Loading)
        private set

    var score by mutableStateOf<Int>(0)
        private set

    var streak by mutableStateOf<Int>(0)
        private set

    var lives by mutableIntStateOf(3)
        private set

    var layoutMode by mutableStateOf<OptionLayoutMode>(OptionLayoutMode.GRID)
        private set

    var isGameOver by mutableStateOf<Boolean>(false)

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

    fun submitAnswer(option: QuizQuestion.Option): Boolean {
        val currentState = quizState as? QuizUiState.Ready ?: return false
        quizState = currentState.copy(
            selectedOption = option
        )

        val isCorrect = option.isCorrect
        if (isCorrect) {
            streak += 1
            score += (10 + streak * 2)
        } else {
            lives --
            if (lives <= 0) {
                isGameOver = true
            } else {
                streak = 0
            }
        }

        return isCorrect
    }

    fun resetGame() {
        score = 0
        streak = 0
        lives = 3
        loadQuestion()
    }

    fun toggleViewMode() {
        layoutMode = when (layoutMode) {
            OptionLayoutMode.LIST -> OptionLayoutMode.GRID
            OptionLayoutMode.GRID -> OptionLayoutMode.LIST
        }
    }
}
