package com.simuel.onebitllm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simuel.onebitllm.ui.navigation.BitnetNavGraph
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnebitLLMTheme {
                BitnetNavGraph()
            }
        }
    }
}
