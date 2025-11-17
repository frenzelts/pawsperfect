package com.frenzelts.dogguesser.presentation.quiz

import androidx.lifecycle.ViewModelProvider
import com.frenzelts.dogguesser.di.DIManager
import com.frenzelts.dogguesser.domain.model.QuizQuestion
import com.frenzelts.dogguesser.presentation.common.BaseViewController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewController : BaseViewController<QuizViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun injectComponent() {
        DIManager.getAppComponent().inject(this)
    }

    override fun createViewModel(factory: ViewModelProvider.Factory) {
        viewModel = ViewModelProvider(activity, viewModelFactory)[QuizViewModel::class.java]
    }

    fun onOptionSelected(option: QuizQuestion.Option) {
        viewModel?.submitAnswer(option)
    }

    fun onNext() {
        viewModel?.loadQuestion()
    }

    fun retry() {
        viewModel?.loadQuestion()
    }
}