package com.frenzelts.dogguesser.presentation.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.frenzelts.dogguesser.domain.model.QuizQuestion
import com.frenzelts.dogguesser.presentation.common.BaseViewController
import com.frenzelts.dogguesser.util.ViewControllerUtil
import com.frenzelts.dogguesser.util.ViewControllerUtil.rememberViewController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    val viewController = rememberViewController { QuizViewController() }
        ?: return
    val viewModel = viewController.viewModel ?: return

    val uiState by viewModel.uiState.collectAsState()

    // For VC → UI events
    val snackbarHostState = remember { SnackbarHostState() }
    val haptics = LocalHapticFeedback.current
    val coroutine = rememberCoroutineScope()

    // Listen to ViewController UiEvents
    LaunchedEffect(Unit) {
        viewController.events.collect { event ->
            when (event) {

                is BaseViewController.UiEvent.Snackbar ->
                    snackbarHostState.showSnackbar(event.message)

                BaseViewController.UiEvent.HapticSuccess ->
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                BaseViewController.UiEvent.HapticError ->
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                is BaseViewController.UiEvent.Navigate -> {
                    // handle navigation however your NavGraph is set up
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Dog Guesser") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is QuizUiState.Loading -> LoadingContent()

                is QuizUiState.Error -> {
                    val msg = (uiState as QuizUiState.Error).message
                    ErrorContent(message = msg, onRetry = viewController::retry)
                }

                is QuizUiState.Ready -> {
                    val ready = uiState as QuizUiState.Ready
                    QuizContent(
                        question = ready.question,
                        score = ready.score,
                        streak = ready.streak,
                        lastAnswer = ready.lastAnswer,
                        onOptionClick = { viewController.onOptionSelected(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    question: QuizQuestion,
    score: Int,
    streak: Int,
    lastAnswer: QuizQuestion.Option?,
    onOptionClick: (QuizQuestion.Option) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Dog Image
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            AsyncImage(
                model = question.imageUrl,
                contentDescription = "Quiz dog image",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Which breed is this?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        // Options list
        Column(Modifier.fillMaxWidth()) {
            question.options.forEach { option ->
                OptionCard(
                    label = option.breed.displayName,
                    enabled = lastAnswer == null, // Disable after selecting
                    onClick = { onOptionClick(option) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Score + Streak footer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: $score")
            Text("🔥 Streak: $streak")
        }
    }
}

@Composable
private fun OptionCard(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(label)
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}