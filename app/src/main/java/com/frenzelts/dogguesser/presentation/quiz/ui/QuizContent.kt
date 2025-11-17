package com.frenzelts.dogguesser.presentation.quiz.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.frenzelts.dogguesser.domain.model.QuizQuestion

@Composable
fun QuizContent(
    question: QuizQuestion,
    selectedOption: QuizQuestion.Option?,
    onOptionClick: (QuizQuestion.Option) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        Image(
            painter = rememberAsyncImagePainter(question.imageUrl),
            contentDescription = "Dog image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    Spacer(Modifier.height(16.dp))

    question.options.forEach { option ->
        OptionItem(
            modifier = Modifier.padding(horizontal = 16.dp),
            option = option,
            isSelected = selectedOption == option,
            showResult = selectedOption != null,
            onClick = { if (selectedOption == null) onOptionClick(option) }
        )
        Spacer(Modifier.height(12.dp))
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
    val targetColor = when {
        !showResult -> MaterialTheme.colorScheme.surface
        isSelected && option.isCorrect -> Color(0xFF4CAF50) // correct answer
        isSelected && !option.isCorrect -> Color(0xFFF44336) // incorrect answer
//        !isSelected && option.isCorrect -> Color(0xFF4CAF50) // expected correct answer
        else -> MaterialTheme.colorScheme.surface
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(300),
        label = ""
    )

    val borderStroke = if (!showResult) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    } else {
        BorderStroke(1.dp, Color.Transparent)
    }

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
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = option.breed.displayName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (showResult && isSelected) {
                Icon(
                    imageVector = if (option.isCorrect) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
