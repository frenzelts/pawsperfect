package com.frenzelts.pawsperfect.presentation.quiz

import androidx.lifecycle.ViewModelProvider
import com.frenzelts.pawsperfect.data.local.ScoreDataStore
import com.frenzelts.pawsperfect.di.DIManager
import com.frenzelts.pawsperfect.domain.model.QuizQuestion
import com.frenzelts.pawsperfect.presentation.common.BaseViewController
import javax.inject.Inject

class QuizViewController : BaseViewController<QuizViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var scoreStore: ScoreDataStore

    override fun injectComponent() {
        DIManager.createAppComponent(activity.application).inject(this)
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

    fun toggleLayoutMode() {
        viewModel?.toggleLayoutMode()
    }

    fun onNext() {
        val viewModel = viewModel ?: return
        viewModel.loadQuestion()
    }

    fun reload() {
        val viewModel = viewModel ?: return
        viewModel.loadQuestion()
    }

    override fun onScreenStopped() {
        viewModel?.saveHighestScore()
    }
}