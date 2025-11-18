package com.frenzelts.pawsperfect.presentation.quiz.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frenzelts.pawsperfect.presentation.common.BaseViewController
import com.frenzelts.pawsperfect.presentation.common.NavBar
import com.frenzelts.pawsperfect.presentation.quiz.QuizUiState
import com.frenzelts.pawsperfect.presentation.quiz.QuizViewController
import com.frenzelts.pawsperfect.R
import com.frenzelts.pawsperfect.util.ViewControllerUtil

@Composable
fun QuizScreen(onBack: () -> Unit) {
    val viewController = ViewControllerUtil.rememberViewController {
        QuizViewController()
    } ?: return
    val viewModel = viewController.viewModel ?: return
    val state = viewModel.quizState
    val nextButton = state is QuizUiState.Ready && state.selectedOption != null
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
                is BaseViewController.UiEvent.BackNavigate -> {
                    viewModel.saveHighestScore()
                    onBack.invoke()
                    viewModel.resetGame()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            NavBar(
                modifier = Modifier.statusBarsPadding(),
                title = "Guess the Breed",
                onBack = { viewController.sendEvent(BaseViewController.UiEvent.BackNavigate) },
                action = {
                    val icon = if (viewModel.layoutMode == OptionLayoutMode.LIST)
                        painterResource(R.drawable.ic_list)
                    else
                        painterResource(R.drawable.ic_grid)

                    IconButton(onClick = { viewController.toggleLayoutMode() }) {
                        Icon(painter = icon, contentDescription = "Change View")
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
                viewController.sendEvent(BaseViewController.UiEvent.BackNavigate)
            }
        )
    }

    BackHandler {
        viewController.sendEvent(BaseViewController.UiEvent.BackNavigate)
    }
}