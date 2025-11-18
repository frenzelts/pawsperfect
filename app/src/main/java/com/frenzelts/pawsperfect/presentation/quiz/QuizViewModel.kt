package com.frenzelts.pawsperfect.presentation.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frenzelts.pawsperfect.data.local.ScoreDataStore
import com.frenzelts.pawsperfect.domain.usecase.GetQuizQuestion
import com.frenzelts.pawsperfect.domain.model.QuizQuestion
import com.frenzelts.pawsperfect.presentation.quiz.ui.OptionLayoutMode
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor (
    private val getQuizQuestion: GetQuizQuestion,
    private val scoreStore: ScoreDataStore
) : ViewModel() {

    var quizState by mutableStateOf<QuizUiState>(QuizUiState.Loading)
        private set

    var score by mutableIntStateOf(0)

    var streak by mutableIntStateOf(0)

    var highestStreak by mutableIntStateOf(0)

    var lives by mutableIntStateOf(3)

    var layoutMode by mutableStateOf<OptionLayoutMode>(OptionLayoutMode.GRID)

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
            score += (10 + streak * 2)
            streak += 1
            if (streak > highestStreak) {
                highestStreak = streak
            }
        } else {
            lives --
            if (lives <= 0) {
                onGameOver()
            } else {
                streak = 0
            }
        }

        return isCorrect
    }

    private fun onGameOver() {
        isGameOver = true
        saveHighestScore()
    }

    fun resetGame() {
        score = 0
        streak = 0
        highestStreak = 0
        lives = 3
        isGameOver = false
        loadQuestion()
    }

    fun saveHighestScore() {
        viewModelScope.launch {
            scoreStore.saveHighestScore(score)
            scoreStore.saveHighestStreak(highestStreak)
        }
    }

    fun toggleLayoutMode() {
        layoutMode = when (layoutMode) {
            OptionLayoutMode.LIST -> OptionLayoutMode.GRID
            OptionLayoutMode.GRID -> OptionLayoutMode.LIST
        }
    }
}
