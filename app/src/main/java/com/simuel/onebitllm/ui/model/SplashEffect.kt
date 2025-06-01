package com.simuel.onebitllm.ui.model

sealed interface SplashEffect {
    data object NavigateToChatList : SplashEffect
    data class ShowInitError(val message: String) : SplashEffect
}
