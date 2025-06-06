package com.simuel.onebitllm.ui.splash

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.simuel.onebitllm.ui.model.SplashEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SplashRoute(
    onNavigateToChatList: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val job = lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collectLatest { effect ->
                    when (effect) {
                        SplashEffect.NavigateToChatList -> onNavigateToChatList()
                        is SplashEffect.ShowInitError -> {
                            android.widget.Toast.makeText(
                                context,
                                effect.message,
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                            (context as Activity).finish()
                        }
                    }
                }
            }
        }
        onDispose { job.cancel() }
    }
    SplashScreen(progressPercent = progress)
}
