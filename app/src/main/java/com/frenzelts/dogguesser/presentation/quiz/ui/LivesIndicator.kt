package com.frenzelts.dogguesser.presentation.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LivesIndicator(modifier: Modifier = Modifier, lives: Int) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val isActive = index < lives
            val color = if (isActive) Color.Red else Color.LightGray.copy(alpha = 0.5f)

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = color
            )
        }
    }
}