package com.frenzelts.pawsperfect.presentation.common.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Brand colors
val PurplePrimary = Color(0xFF6F16F6)
val YellowSecondary = Color(0xFFFDD014)
val MaroonTertiary = Color(0xFF9F0D03)

// Complementary tones (container colors)
val PurpleContainer = Color(0xFF9A52FF)
val YellowContainer = Color(0xFFFFE47A)
val MaroonContainer = Color(0xFFCC3928)

// Light theme
val LightColors = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = Color.White,

    secondary = YellowSecondary,
    onSecondary = Color.Black,
    secondaryContainer = YellowContainer,
    onSecondaryContainer = Color.Black,

    tertiary = MaroonTertiary,
    onTertiary = Color.White,
    tertiaryContainer = MaroonContainer,
    onTertiaryContainer = Color.White,

    background = Color(0xFFF9F9FB),
    onBackground = Color(0xFF1A1C1E),

    surface = Color.White,
    onSurface = Color.Black,
)

// Dark theme
val DarkColors = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = Color.White,

    secondary = YellowSecondary,
    onSecondary = Color.Black,
    secondaryContainer = YellowContainer,
    onSecondaryContainer = Color.Black,

    tertiary = MaroonTertiary,
    onTertiary = Color.White,
    tertiaryContainer = MaroonContainer,
    onTertiaryContainer = Color.White,

    background = Color(0xFF101114),
    onBackground = Color(0xFFE3E3E8),

    surface = Color(0xFF1C1D21),
    onSurface = Color.White
)