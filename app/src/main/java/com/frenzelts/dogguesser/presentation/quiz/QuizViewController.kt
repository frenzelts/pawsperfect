package com.frenzelts.dogguesser.presentation.quiz

import androidx.lifecycle.ViewModelProvider
import com.frenzelts.dogguesser.di.DIManager
import com.frenzelts.dogguesser.domain.model.QuizQuestion
import com.frenzelts.dogguesser.presentation.common.BaseViewController
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
        val isCorrect = viewModel?.submitAnswer(option) ?: return

        if (isCorrect) {
            sendEvent(UiEvent.HapticSuccess)
        } else {
            sendEvent(UiEvent.HapticError)
        }
    }

    fun toggleViewMode() {
        val vm = viewModel ?: return
        vm.toggleViewMode()
    }

    fun onNext() {
        val viewModel = viewModel ?: return
        viewModel.loadQuestion()
    }

    fun reload() {
        val viewModel = viewModel ?: return
        viewModel.loadQuestion()
    }
}