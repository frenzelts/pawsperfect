package com.frenzelts.pawsperfect.presentation.quiz.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.frenzelts.pawsperfect.presentation.common.BaseViewController
import com.frenzelts.pawsperfect.presentation.common.NavBar
import com.frenzelts.pawsperfect.presentation.quiz.QuizUiState
import com.frenzelts.pawsperfect.presentation.quiz.QuizViewController
import com.frenzelts.pawsperfect.util.ViewControllerUtil

@Composable
fun QuizScreen(onBack: () -> Unit) {
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
            }
        }
    }

    Scaffold(
        topBar = {
            NavBar(
                title = "Guess the Breed",
                onBack = { onBack() },
                action = {
                    val icon = if (viewModel.layoutMode == OptionLayoutMode.LIST)
                        Icons.AutoMirrored.Default.List
                    else
                        Icons.Default.AccountBox

                    IconButton(onClick = { viewController.toggleLayoutMode() }) {
                        Icon(imageVector = icon, contentDescription = "Change View")
                    }
                }
            )
        },
        bottomBar = {
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
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val shakeOffset = remember { Animatable(0f) }
            LaunchedEffect(viewModel.lives) {
                if (viewModel.lives < 3) {
                    shakeOffset.animateTo(
                        targetValue = 20f,
                        animationSpec = tween(80)
                    )
                    shakeOffset.animateTo(
                        targetValue = -20f,
                        animationSpec = tween(80)
                    )
                    shakeOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(60)
                    )
                }
            }

            when (state) {
                is QuizUiState.Loading ->
                    CircularProgressIndicator(Modifier.padding(horizontal = 16.dp))
                is QuizUiState.Ready ->
                    QuizContent(
                        modifier = Modifier.offset(x = shakeOffset.value.dp),
                        question = state.question,
                        selectedOption = state.selectedOption,
                        lives = viewModel.lives,
                        layoutMode = viewModel.layoutMode,
                        viewController = viewController,
                    )
                is QuizUiState.Error ->
                    ErrorState { viewController.reload() }
            }
        }
    }

    if (viewModel.isGameOver) {
        GameOverDialog(
            onRestart = {
                viewModel.resetGame()
            },
            onExit = {
                viewModel.isGameOver = false
                onBack()
            }
        )
    }

    BackHandler {
        onBack()
    }
}