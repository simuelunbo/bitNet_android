package com.simuel.onebitllm.data.datasource

import com.simuel.onebitllm.data.model.ModelLoadProgressDto
import com.simuel.onebitllm.data.model.OperationResultDto
import kotlinx.coroutines.flow.Flow

interface BitnetNativeDataSource {
    // 모델 로드 (진행 상황 percent로 나타내기 위해 Flow로 반환)
    fun loadModel(): Flow<ModelLoadProgressDto>
    
    // 시스템 프롬프트 설정
    fun setSystemPrompt(prompt: String): OperationResultDto
    
    // 사용자 프롬프트 설정
    fun setUserPrompt(prompt: String): OperationResultDto

    // 모델 로드 되었는지 확인
    fun isModelLoaded(): Boolean
    
    // 응답 토큰 생성
    fun generateResponseFlow(): Flow<String>
    
    // 리소스 해제
    fun release()
}
