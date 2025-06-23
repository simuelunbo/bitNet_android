package com.simuel.onebitllm.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.simuel.onebitllm.ui.model.ModelSyncState

@Composable
fun SplashScreen(state: ModelSyncState) {
    val percent = when (state) {
        is ModelSyncState.InProgress -> state.percentComplete
        ModelSyncState.Completed -> 100f
        else -> 0f
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "BitNet",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.wrapContentWidth()
            )
            Text(
                text = "${percent.toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(state = ModelSyncState.InProgress(50f))
}
