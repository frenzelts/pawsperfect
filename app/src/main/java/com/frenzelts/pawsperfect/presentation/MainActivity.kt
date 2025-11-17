package com.frenzelts.pawsperfect.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.frenzelts.pawsperfect.di.DIManager
import com.frenzelts.pawsperfect.presentation.common.theme.PawsPerfectTheme
import com.frenzelts.pawsperfect.presentation.common.LocalActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIManager.createAppComponent(application).inject(this)

        setContent {
            PawsPerfectTheme {
                CompositionLocalProvider(
                    LocalActivity provides this
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}