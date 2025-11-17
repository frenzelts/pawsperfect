package com.frenzelts.dogguesser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import com.frenzelts.dogguesser.di.DIManager
import com.frenzelts.dogguesser.presentation.quiz.ui.QuizScreen
import com.frenzelts.dogguesser.presentation.theme.DogGuesserTheme
import com.frenzelts.dogguesser.util.LocalActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIManager.createAppComponent(application).inject(this)

        setContent {
            DogGuesserTheme {
                CompositionLocalProvider(
                    LocalActivity provides this
                ) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        QuizScreen()
                    }
                }
            }
        }
    }
}