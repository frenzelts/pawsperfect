package com.frenzelts.dogguesser.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Brand colors
val BluePrimary = Color(0xFF4F7BFF)
val BlueSecondary = Color(0xFF7FA4FF)
val BlueTertiary = Color(0xFFB7CCFF)

val DarkBluePrimary = Color(0xFF2F4FBF)
val DarkBlueSecondary = Color(0xFF4A67CC)
val DarkBlueTertiary = Color(0xFF7388E0)

// Light theme
val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueTertiary,
    onPrimaryContainer = Color.Black,

    secondary = BlueSecondary,
    onSecondary = Color.White,

    background = Color(0xFFF9F9FB),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color.Black
)

// Dark theme
val DarkColors = darkColorScheme(
    primary = DarkBluePrimary,
    onPrimary = Color.White,
    primaryContainer = DarkBlueTertiary,
    onPrimaryContainer = Color.White,

    secondary = DarkBlueSecondary,
    onSecondary = Color.White,

    background = Color(0xFF101114),
    onBackground = Color(0xFFE3E3E8),
    surface = Color(0xFF1C1D21),
    onSurface = Color.White
)
