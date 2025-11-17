package com.frenzelts.pawsperfect.presentation.quiz

import com.frenzelts.pawsperfect.domain.model.QuizQuestion

sealed interface QuizUiState {

    object Loading : QuizUiState

    data class Ready(
        val question: QuizQuestion,
        val selectedOption: QuizQuestion.Option? = null,
    ) : QuizUiState

    data class Error(val message: String) : QuizUiState

}