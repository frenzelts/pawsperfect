package com.frenzelts.pawsperfect.presentation.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizFooter(
    modifier: Modifier = Modifier,
    score: Int,
    streak: Int,
    nextButton: Boolean,
    onNext: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Score: $score")

        Text("🔥 Streak: $streak")

        Button(onClick = onNext, enabled = nextButton) {
            Text("Next")
        }
    }
}