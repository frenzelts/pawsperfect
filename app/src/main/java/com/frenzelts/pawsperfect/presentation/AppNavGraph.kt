package com.frenzelts.pawsperfect.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frenzelts.pawsperfect.presentation.home.HomeScreen
import com.frenzelts.pawsperfect.presentation.quiz.ui.QuizScreen

const val NAVIGATION_HOME = "home"
const val NAVIGATION_QUIZ = "quiz"

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NAVIGATION_HOME
    ) {
        composable(NAVIGATION_HOME) {
            HomeScreen(
                onStartClicked = {
                    navController.navigate("quiz")
                }
            )
        }

        composable(NAVIGATION_QUIZ) {
            QuizScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
