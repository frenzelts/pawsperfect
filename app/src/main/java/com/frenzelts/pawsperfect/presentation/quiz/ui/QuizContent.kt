package com.frenzelts.pawsperfect.presentation.quiz.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.frenzelts.pawsperfect.domain.model.QuizQuestion
import com.frenzelts.pawsperfect.presentation.quiz.QuizViewController

@Composable
fun QuizContent(
    modifier: Modifier = Modifier,
    question: QuizQuestion,
    selectedOption: QuizQuestion.Option?,
    lives: Int,
    layoutMode: OptionLayoutMode,
    viewController: QuizViewController,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = rememberAsyncImagePainter(question.imageUrl),
                contentDescription = "Dog image",
                contentScale = ContentScale.Fit
            )

            LivesIndicator(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                lives = lives
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(
            targetState = layoutMode,
            animationSpec = tween(durationMillis = 300)
        ) { mode ->
            when (mode) {
                OptionLayoutMode.LIST -> {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        question.options.forEach { option ->
                            OptionItem(
                                option = option,
                                isSelected = selectedOption == option,
                                showResult = selectedOption != null,
                                onClick = { if (selectedOption == null)
                                    viewController.onOptionSelected(option)
                                }
                            )
                        }
                    }
                }

                OptionLayoutMode.GRID -> {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {

                        val totalHeight = maxHeight
                        val rows = 2
                        val cellHeight = totalHeight / rows - 12.dp

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(question.options) { option ->
                                OptionItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(cellHeight),
                                    option = option,
                                    isSelected = selectedOption == option,
                                    showResult = selectedOption != null,
                                    onClick = { if (selectedOption == null)
                                        viewController.onOptionSelected(option)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    modifier: Modifier = Modifier,
    option: QuizQuestion.Option,
    isSelected: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val (targetBg, targetBorder) = when {
        !showResult -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.outline

        isSelected && option.isCorrect ->
            Color(0xFF4CAF50) to Color(0xFF1B621D)  // correct

        isSelected && !option.isCorrect ->
            Color(0xFFF44336) to Color(0xFFB22217)  // wrong

        !isSelected && option.isCorrect ->
            Color(0xFFC5E1A5) to Color(0xFF7CB342)  // expected correct

        else ->
            MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.outline
    }

    val textColor = when {
        isSelected && option.isCorrect -> Color.White   // correct
        isSelected && !option.isCorrect -> Color.White  // wrong
        !isSelected && option.isCorrect -> Color.Black  // expected correct
        else -> MaterialTheme.colorScheme.onSurface
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetBg,
        animationSpec = tween(300),
        label = "background"
    )
    val borderColor by animateColorAsState(
        targetValue = targetBorder,
        animationSpec = tween(300),
        label = "background"
    )
    val borderStroke = BorderStroke(1.dp, borderColor)

    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !showResult) { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                text = option.breed.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = fontWeight,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
