package com.frenzelts.dogguesser.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frenzelts.dogguesser.domain.usecase.GetQuizQuestion
import javax.inject.Inject

class QuizViewModelFactory @Inject constructor(
    private val getQuizQuestion: GetQuizQuestion
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizViewModel(getQuizQuestion) as T
    }
}