// domain/model/ErrorCode.kt
package com.simuel.onebitllm.domain.model

enum class ErrorCode {
    MODEL_NOT_LOADED,       // 모델이 로드되지 않은 경우
    INVALID_PROMPT,         // 프롬프트가 유효하지 않은 경우
    PROCESSING_ERROR,       // 모델 처리 중 오류
    FILE_ACCESS_ERROR,      // 파일 접근/읽기/쓰기 오류
    MEMORY_ERROR,           // 메모리 부족 등의 리소스 관련 오류
    UNKNOWN_ERROR           // 분류되지 않은 기타 오류
}
