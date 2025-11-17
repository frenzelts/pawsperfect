package com.frenzelts.dogguesser.presentation.quiz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.frenzelts.dogguesser.presentation.common.BaseViewController
import com.frenzelts.dogguesser.presentation.common.NavBar
import com.frenzelts.dogguesser.presentation.quiz.QuizUiState
import com.frenzelts.dogguesser.presentation.quiz.QuizViewController
import com.frenzelts.dogguesser.util.ViewControllerUtil

@Composable
fun QuizScreen() {
    val viewController = ViewControllerUtil.rememberViewController {
        QuizViewController()
    } ?: return
    val viewModel = viewController.viewModel ?: return
    val state = viewModel.quizState
    val nextButton by derivedStateOf {
        state is QuizUiState.Ready &&
                state.selectedOption != null
    }

    val haptic = LocalHapticFeedback.current
    LaunchedEffect(Unit) {
        viewController.events.collect { event ->
            when(event) {
                is BaseViewController.UiEvent.HapticSuccess -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                is BaseViewController.UiEvent.HapticError -> {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                else -> Unit
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        NavBar(
            title = "Guess the Breed",
            onBack = { viewController.sendEvent(BaseViewController.UiEvent.Navigate("back")) }
        )

        when (state) {
            is QuizUiState.Loading -> {
                CircularProgressIndicator(Modifier.padding(horizontal = 16.dp))
            }
            is QuizUiState.Ready -> {
                QuizContent(
                    question = state.question,
                    selectedOption = state.selectedOption,
                    onOptionClick = { viewController.onOptionSelected(it) },
                )
            }
            is QuizUiState.Error -> {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Something went wrong.")
                    Button(
                        onClick = { viewController.retry() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Next")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        QuizFooter(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            score = viewModel.score,
            streak = viewModel.streak,
            nextButton = nextButton,
            onNext = {
                viewController.onNext()
            }
        )
    }
}