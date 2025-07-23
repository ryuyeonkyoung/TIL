# 이해를 돕기 위한 예시 코드
class LLM:
    def __init__(self, model_weights, context_window=4096):
        self.weights = model_weights
        self.context_window = context_window

    def tokenize(self, text):
        # 1. 입력 인코딩 단계: 긴 입력은 context_window만큼만 사용 (맥락 손실 발생)
        tokens = tokenizer(text)
        if len(tokens) > self.context_window:
            tokens = tokens[-self.context_window:]  # 앞부분 맥락 손실
        return tokens

    def forward(self, tokens):
        # 2. Self-Attention: 입력 내 패턴만 학습, 외부/암묵적 맥락 반영 불가
        for layer in self.weights:
            tokens = self.self_attention(tokens, layer)
        return tokens

    def self_attention(self, tokens, layer):
        # 3. Attention: 입력 내 관계만 파악, 배경지식/상황 맥락 한계
        # (실제 구현은 복잡함)
        return tokens

    def generate(self, prompt):
        tokens = self.tokenize(prompt)
        output_tokens = self.forward(tokens)
        # 4. 명시적 목표(프롬프트)에만 최적화, 문제 재정의 불가
        return detokenize(output_tokens)