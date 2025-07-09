# Exception (예외)

## Java의 Exception 종류

| 분류 | 대표 예시 | 특징/설명 |
| --- | --- | --- |
| Error | OutOfMemoryError, StackOverflowError | 시스템 오류, 직접 처리 불가 |
| Checked Exception | IOException, SQLException, ... | 컴파일러 체크, 반드시 처리 필요 |
| Unchecked Exception | NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ... | 런타임 오류, 선택적 처리 |

---

### 1. Throwable
- 모든 예외와 에러의 최상위 클래스
- 하위로 Error와 Exception이 있음

### 2. Error
- JVM이나 하드웨어 등 시스템적 문제 (예: OutOfMemoryError, StackOverflowError)
- 개발자가 직접 처리하지 않음

### 3. Exception

#### (1) Checked Exception (일반 예외)
- 컴파일 타임에 반드시 처리(try-catch 또는 throws)
- 대표 예시:
    - IOException (입출력 오류)
    - SQLException (DB 오류)
    - ClassNotFoundException (클래스 못 찾음)
    - FileNotFoundException (파일 없음)

```java
public void readFile() throws IOException {
    // 파일 읽기 코드
}
```

#### (2) Unchecked Exception (런타임/실행 예외)
- 컴파일러가 체크하지 않음 (런타임에 발생)
- 주로 프로그래밍 실수, 논리 오류
- RuntimeException 및 그 하위 클래스
- 대표 예시:
    - NullPointerException (null 참조)
    - ArrayIndexOutOfBoundsException (배열 인덱스 초과)
    - IllegalArgumentException (잘못된 인자)
    - ArithmeticException (0으로 나누기 등)
    - NumberFormatException (문자열 → 숫자 변환 실패)

```java
int[] arr = new int[3];
int x = arr[5]; // ArrayIndexOutOfBoundsException
```

---

## 예외 처리 방법

1. try-catch-finally
2. throws 키워드로 상위 메서드에 예외 위임
3. throw 키워드로 직접 예외 발생

### 1) try-catch (finally)
```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("0으로 나눌 수 없습니다: " + e.getMessage());
} finally {
    System.out.println("항상 실행됨");
}
```

### 2) throws 키워드
```java
public void readFile(String path) throws IOException {
    FileReader reader = new FileReader(path);
    // ...
}
```

### 3) throw 키워드 (예외 강제 발생)
```java
public void withdraw(int amount) {
    if (amount < 0) {
        throw new IllegalArgumentException("출금 금액은 0 이상이어야 합니다.");
    }
    // ...
}
```

---

## @Valid
- 자바 표준(Bean Validation, JSR-303/JSR-380)의 유효성 검증 어노테이션
- 컨트롤러 메서드의 파라미터, 필드, 메서드 등 다양한 위치에 사용 가능

### 장점

- 코드에 반복적인 if문 없이 간단하게 유효성 검증 가능
- 객체의 중첩 구조까지 자동 검증

### 동작 방식

- @Valid가 붙은 객체는 스프링의 ArgumentResolver가 파라미터 바인딩 시점에 유효성 검증을 수행
- 검증 실패 시 MethodArgumentNotValidException이 발생 → 이를 통해 에러 메시지 생성
- 객체의 **전체 그래프**(즉, 내부에 또 다른 객체가 있으면 그 내부 객체까지) 재귀적으로 검증

### 사용 예시

```java
@PostMapping("/user")
public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto userDto) {
    // userDto의 각 필드에 선언된 제약조건(@NotNull 등)을 자동으로 검증
}
```

### @Valid vs @Validated

| **구분** | **@Valid** | **@Validated** |
| --- | --- | --- |
| 제공 | 자바 표준(Jakarta EE, JSR-303) | 스프링 프레임워크 |
| 그룹 검증 | 지원하지 않음 | 지원함 |
| 적용 계층 | 주로 컨트롤러(파라미터 바인딩) | 컨트롤러, 서비스, 레포지토리 등 스프링 빈 전체 |
| 동작 방식 | 표준 Bean Validation | AOP 기반, 스프링 빈 전체 |
| 예외 | MethodArgumentNotValidException | ConstraintViolationException 등 |

- **@Valid**: JSR-303 표준, 기본적인 전체 객체 검증에 사용.
- **@Validated**: Spring에서 제공, **그룹 검증**(validation group)이나 AOP 기반의 계층별(컨트롤러, 서비스 등) 검증에 사용.
- @Valid는 컨트롤러에서 주로 쓰이고, @Validated는 서비스 등 다른 계층에서도 활용할 수 있음.

  → 단순한 파라미터 검증에는 `@Valid`, 다양한 계층에서 유효성 검증이 필요할 때는 `@Validated` 사용

### Validation 어노테이션

- **`@NotNull`**: null만 허용하지 않음 (""이나 " "은 허용)
- **`@NotEmpty`**: null과 "" 모두 허용하지 않음 (" "은 허용)
- **`@NotBlank`**: null, "", " " 모두 허용하지 않음 (공백도 불가)
- **`@Size(min=, max=)`**: 문자열, 컬렉션, 배열 크기 제한
- **`@Email`**: 이메일 형식 체크
- **`@Pattern`**: 정규식 패턴 체크
- **`@Min`**, **`@Max`**: 숫자 범위 제한

```java
public class UserSignupRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    *// getter/setter 생략*
}
```

---

퀴즈
1. @ApiResponse와 @ApiResponses의 차이점
2. Swagger 문서를 특정 환경(예: 로컬/개발)에서만 활성화하려면 어떻게 설정해야 하는지
3. Swagger의 주요 목적과 사용 가능한 어노테이션들
4. 커스텀 어노테이션으로 AOP를 적용하는 방법
5. 커스텀 어노테이션을 만드는 이유와 예시
6. @Valid와 @Validated의 차이점

- 정답

  1번:

    - `@ApiResponse`: 단일 HTTP 응답 코드 설명
    - `@ApiResponses`: 여러 `@ApiResponse`를 묶어서 사용

  2번:

    - `springdoc.profiles`로 프로필 지정
    - `@Profile("dev")`로 컨트롤러 제한

  3번:

  API 명세 자동 생성 및 테스트 UI 제공.

    - `@OpenAPIDefinition`: 전역 설정

    - `@Operation`: API 엔드포인트 설명

    - `@Schema`: DTO 필드 설명

  4번:

  `@Around("@annotation(커스텀어노테이션)")`으로 포인트컷 지정
  5번:

    - 중복 코드 제거 (예: `@UserAuth`로 권한 체크 통합)
    - 메타데이터 추가 (예: `@DeprecatedAPI`로 사용 중단 API 표시)

  6번:

    - `@Valid`: JSR-303 표준 (컨트롤러에서 주로 사용)
    - `@Validated`: 스프링 확장 (그룹 검증, 서비스 계층 사용)