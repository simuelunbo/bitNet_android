# onebitLLM

원비트LLM은 Microsoft BitNet 모델을 안드로이드 기기에서 실행하기 위한 데모 애플리케이션입니다. Jetpack Compose UI와 Room 데이터베이스를 사용하여 대화 기록을 저장하고, 네이티브 라이브러리를 통해 모델을 로드합니다.

## 프로젝트 구조
- `app/` - 메인 안드로이드 모듈
    - `ui/` - Compose 화면 (Splash → 채팅방 목록 → 채팅 화면)
    - `domain/` - UseCase 및 Repository 인터페이스
    - `data/` - Room DB, DataStore, 네이티브 연동 코드
    - `di/` - Hilt 모듈 정의
- `BitnetNative` - JNI를 통해 호출되는 비트넷 네이티브 라이브러리

## 시작하기
### 1. 저장소 클론
```bash
git clone <repository-url>
cd bitNet_android
```

### 2. 모델 다운로드
아래 명령으로 Hugging Face에서 BitNet 모델을 내려받습니다.
```bash
huggingface-cli download microsoft/BitNet-b1.58-2B-4T-gguf \
  --local-dir models/BitNet-b1.58-2B-4T
```
다운로드한 폴더에서 `.gguf` 파일을 `app/src/main/assets/models/ggml-model-i2_s.gguf` 위치로 복사(또는 이름을 변경)합니다. 앱 실행 시 해당 자산을 내부 저장소로 복사하여 모델을 로드합니다.

### 3. llama.cpp 연동
저장소에는 `app/src/main/cpp/bitnet/3rdparty/llama.cpp` 경로에 llama.cpp가 서브모듈로 포함되어 있습니다. `LLAMA_PATH` 변수를 따로 지정하지 않으면 이 기본 경로가 자동으로 사용됩니다. 다른 경로의 llama.cpp를 사용하고 싶다면 `LLAMA_PATH` 값을 지정하세요.```bash
cmake -DLLAMA_PATH=/path/to/llama.cpp ...
```

### 4. 빌드 및 실행
Android Studio에서 열어 실행하거나 아래 명령으로 빌드할 수 있습니다.
```bash
./gradlew assembleDebug
```

## 참고
- 첫 실행 시 모델 복사 및 초기화로 인해 시간이 소요될 수 있습니다.
- Splash 화면의 진행률이 100%가 되면 채팅 목록 화면으로 이동합니다.
