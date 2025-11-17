package com.frenzelts.dogguesser.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frenzelts.dogguesser.constant.APP_NAME
import com.frenzelts.dogguesser.util.ViewControllerUtil.rememberViewController

@Composable
fun HomeScreen(onStartClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val viewController = rememberViewController {
            HomeViewController()
        } ?: return
        val viewModel = viewController.viewModel ?: return

        Text(
            text = APP_NAME,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Highest Score: ${viewModel.highestScore.value}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Highest Streak: ${viewModel.highestStreak.value}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onStartClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start Game", fontSize = 20.sp)
        }
    }
}
