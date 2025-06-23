package com.simuel.onebitllm.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simuel.onebitllm.domain.model.ErrorCode
import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.usecase.LoadModelUseCase
import com.simuel.onebitllm.ui.model.ModelSyncState
import com.simuel.onebitllm.ui.model.SplashEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadModelUseCase: LoadModelUseCase
) : ViewModel() {

    private val _effect = Channel<SplashEffect>(Channel.BUFFERED)
    val effect: Flow<SplashEffect> = _effect.receiveAsFlow()

    val state: StateFlow<ModelSyncState> = loadModelUseCase()
        .onEach { result ->
            when (result) {
                is OperationResult.Success -> {
                    if (result.data >= 1f) {
                        _effect.trySend(SplashEffect.NavigateToChatList)
                    }
                }
                is OperationResult.Failure ->
                    _effect.trySend(SplashEffect.ShowInitError(result.message))
            }
        }
        .map { result ->
            when (result) {
                is OperationResult.Success -> {
                    val percent = result.data * 100f
                    if (percent >= 100f) ModelSyncState.Completed else ModelSyncState.InProgress(percent)
                }
                is OperationResult.Failure -> ModelSyncState.Failed(result.errorCode)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ModelSyncState.Initial
        )
}
