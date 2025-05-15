package com.simuel.onebitllm.data.bitnet

import android.content.Context
import android.util.Log
import com.simuel.onebitllm.BitnetNative
import com.simuel.onebitllm.data.model.BitnetOperationResult
import com.simuel.onebitllm.data.model.ModelLoadProgress
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitnetNativeDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitnetNativeDataSource {

    private val TAG = "BitnetNativeDataSource"

    override fun loadModel(): Flow<ModelLoadProgress> = flow {
        emit(ModelLoadProgress(phase = ModelLoadProgress.LoadPhase.INITIALIZATION))

        try {
            // 1단계: 모델 파일 준비 (Assets에서 복사)
            emit(ModelLoadProgress(phase = ModelLoadProgress.LoadPhase.PREPARING))
            val modelFile = prepareModelFile { bytesLoaded, totalBytes ->
                emit(
                    ModelLoadProgress(
                        bytesLoaded,
                        totalBytes,
                        ModelLoadProgress.LoadPhase.PREPARING
                    )
                )
            }

            // 2단계: 모델 로딩
            emit(ModelLoadProgress(phase = ModelLoadProgress.LoadPhase.LOADING))
            val threads = Runtime.getRuntime().availableProcessors().coerceAtMost(4)

            // 모델 로드 진행률은 알기 어려워서 50% 고정값 사용
            emit(ModelLoadProgress(50, 100, ModelLoadProgress.LoadPhase.LOADING))

            val modelLoaded = BitnetNative.initModel(modelFile.absolutePath, threads)

            // 3단계: 최종 완료
            if (modelLoaded) {
                emit(ModelLoadProgress(100, 100, ModelLoadProgress.LoadPhase.FINALIZING))
                Log.d(TAG, "Model loaded successfully")
            } else {
                throw RuntimeException("모델 초기화 실패")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    override fun setSystemPrompt(prompt: String): BitnetOperationResult {
        if (!isModelLoaded()) {
            return BitnetOperationResult.Error(IllegalStateException("모델이 로드되지 않았습니다"))
        }

        return try {
            val success = BitnetNative.setSystemPrompt(prompt)
            if (success) {
                BitnetOperationResult.Success
            } else {
                BitnetOperationResult.Error(RuntimeException("시스템 프롬프트 설정에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting system prompt", e)
            e.printStackTrace()
            BitnetOperationResult.Error(e)
        }
    }

    override fun setUserPrompt(prompt: String): BitnetOperationResult {
        if (!isModelLoaded()) {
            return BitnetOperationResult.Error(IllegalStateException("모델이 로드되지 않았습니다"))
        }

        return try {
            val success = BitnetNative.setUserPrompt(prompt)
            if (success) {
                BitnetOperationResult.Success
            } else {
                BitnetOperationResult.Error(RuntimeException("사용자 입력 설정 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting user prompt", e)
            e.printStackTrace()
            BitnetOperationResult.Error(e)
        }
    }

    override fun isModelLoaded(): Boolean {
        return BitnetNative.isModelLoaded()
    }

    override fun generateResponseFlow(): Flow<String> = flow {
        if (!isModelLoaded()) {
            emit("[모델이 로드되지 않았습니다]")
            return@flow
        }

        try {
            var isGenerating = true

            while (isGenerating) {
                val token = BitnetNative.generateNextToken()

                // 특수 토큰 처리 (종료 조건 등)
                if (token.isEmpty() || token == "<EOS>" || token == "</s>") {
                    isGenerating = false
                    continue
                }
                emit(token)
                delay(10)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating response", e)
            emit("[대화 생성 중 오류가 발생했습니다: ${e.message}]")
        }
    }.flowOn(Dispatchers.IO)

    override fun release() {
        try {
            if (isModelLoaded()) {
                BitnetNative.freeModel()
                Log.d(TAG, "Model resources released")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing model", e)
        }
    }

    // 진행률 update 포함한 모델 파일 준비
    private suspend fun prepareModelFile(
        progressUpdate: suspend (bytesLoaded: Long, totalBytes: Long) -> Unit
    ): File {
        val assetFileName = "models/ggml-model-i2_s.gguf"
        val modelFile = File(context.filesDir, "ggml-model-i2_s.gguf")

        // 파일이 이미 존재 하면 다시 copy 하지 않음
        if (modelFile.exists()) {
            progressUpdate(1, 1) // 100% 완료 표시
            return modelFile
        }

        // assets 에서 모델 파일 복사 (진행률 update 포함)
        context.assets.open(assetFileName).use { input ->
            val fileSize = input.available().toLong()
            val buffer = ByteArray(16 * 1024) // 16KB 버퍼
            var bytesRead: Int
            var totalRead = 0L

            FileOutputStream(modelFile).use { output ->
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    totalRead += bytesRead
                    progressUpdate(totalRead, fileSize)
                }
                output.flush()
            }
        }

        return modelFile
    }
}