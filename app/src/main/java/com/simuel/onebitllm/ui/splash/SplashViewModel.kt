package com.simuel.onebitllm.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.usecase.LoadModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadModelUseCase: LoadModelUseCase
) : ViewModel() {

    private val _events = Channel<Unit>(Channel.BUFFERED)
    val events: Flow<Unit> = _events.receiveAsFlow()

    val progress: StateFlow<Float> = loadModelUseCase().map { result ->
            when (result) {
                is OperationResult.Success -> result.data * 100f
                is OperationResult.Failure -> 0f
            }
        }.onEach { percent ->
            if (percent >= 100f) _events.trySend(Unit)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0f
        )
}