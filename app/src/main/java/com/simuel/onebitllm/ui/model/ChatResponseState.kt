package com.simuel.onebitllm.ui.model

import com.simuel.onebitllm.domain.model.ChatMessage

sealed class ChatResponseState {
    /** 초기 상태 */
    data object Initial : ChatResponseState()

    /** 응답 생성 시작 */
    data object Started : ChatResponseState()

    /** 생성 완료 */
    data class Completed(val message: ChatMessage) : ChatResponseState()

    /** 오류 발생 */
    data class Failed(val error: Throwable) : ChatResponseState()
}
