package com.frenzelts.dogguesser.presentation.quiz

import com.frenzelts.dogguesser.domain.model.QuizQuestion

sealed interface QuizUiState {

    object Loading : QuizUiState

    data class Ready(
        val question: QuizQuestion,
        val score: Int,
        val streak: Int,
        val lastAnswer: QuizQuestion.Option?
    ) : QuizUiState

    data class Error(val message: String) : QuizUiState
}
