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
        // Use the injected ViewModelProvider.Factory
        viewModel = ViewModelProvider(activity, viewModelFactory)[QuizViewModel::class.java]
    }

    fun onOptionSelected(option: QuizQuestion.Option) {
        val vm = viewModel ?: return

        val isCorrect = vm.submitAnswer(option)

        if (isCorrect) {
            sendEvent(UiEvent.HapticSuccess)
            sendEvent(UiEvent.Snackbar("Correct! ${option.breed.displayName}"))
        } else {
            val correctName = (vm.uiState.value as? QuizUiState.Ready)
                ?.question
                ?.options
                ?.firstOrNull { it.isCorrect }
                ?.breed
                ?.displayName

            sendEvent(UiEvent.HapticError)
            sendEvent(UiEvent.Snackbar("Wrong! Correct: $correctName"))
        }

        viewControllerScope.launch {
            delay(900)
            vm.loadQuestion()
        }
    }

    fun retry() {
        viewModel?.loadQuestion()
    }
}