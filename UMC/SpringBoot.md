| `@Aspect` | AOP 클래스 정의 |
| `@Before` | 메서드 실행 전 |
| `@AfterReturning` | 정상 종료 후 실행 |
| `@AfterThrowing` | 예외 발생 시 실행 |
| `@Around` | 전/후 모두 감싸서 실행 |
| `@Pointcut` | advice 적용 위치 정의 |
# Spring Boot

## DI (Dependency Injection)
### 적용 예시
```java
@Component
public class UserRepository {
    public void save() {
        System.out.println("User saved!");
    }
}

@Service
public class UserService {
    // 생성자 주입
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void register() {
        // 회원 가입 로직
        userRepository.save();
    }
}
```

## AOP (Aspect-Oriented Programming)
- **관점 지향 프로그래밍**: 비즈니스 로직 외에 반복되는 **공통 관심사(Cross-Cutting Concern)**를 분리하여 모듈화

### 사용하는 이유
- **로직 중복 제거**
- 비즈니스 로직 가독성 향상
- 공통 기능을 한 곳에서 관리 → **유지보수성 향상**

### 장점
- 로직 재사용성 향상
- 핵심 로직 분리로 **SRP(단일 책임 원칙)** 실현
- 테스트/확장에 유리한 구조

### 사용처
- 공통 로깅 (로그 추적, 요청 응답 기록)
- 트랜잭션 관리 (주로 @Transactional 사용하지만 구조는 동일)
- 보안 체크 (@PreAuthorize 등과 결합 가능)
- 예외 로깅 및 처리 일괄화

### 특이점
- Spring AOP는 **런타임 프록시 방식만 지원**함 → **메서드 실행 전후에만 AOP 적용 가능**
- 내부 메서드, private 매서드는 AOP 적용 불가
- @Around를 사용하면 가장 강력하게 사용 가능

### 주의점
- **핵심 로직은 절대 AOP로 감싸지 말 것**

### 대표 어노테이션
| 어노테이션 | 설명 |
| --- | --- |

### 적용 예시
```java
@Aspect
@Component
public class LoggingAspect {
    // Pointcut: com.example.service 하위의 모든 메서드
    @Pointcut("execution(* com.example.service..*(..))")
    public void serviceMethods() {}
    // Advice: 메서드 실행 전
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[BEFORE] " + joinPoint.getSignature().toShortString());
    }
    // Advice: 메서드 실행 후 결과 반환 후
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("[AFTER] " + joinPoint.getSignature().toShortString() + " result=" + result);
    }
    // Advice: Around (가장 범용적으로 사용됨)
    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 실제 메서드 실행
        long end = System.currentTimeMillis();
        System.out.println("[AROUND] " + joinPoint.getSignature().toShortString() + " 실행 시간: " + (end - start) + "ms");
        return result;
    }
}
```

```java
@Service
public class MemberService {
    public String getMemberInfo(String memberId) {
        return "회원 정보: " + memberId;
    }
}
```

---

## 서블릿
- Java 웹 애플리케이션에서 HTTP 요청을 처리하는 **기본 실행 단위**
- 클라이언트 요청을 받아서, 데이터를 처리하고 응답을 반환하는 역할 수행

### 동작 흐름
1. 클라이언트가 HTTP 요청 전송
2. 서블릿 컨테이너(Tomcat 등)가 해당 요청을 서블릿 객체에 위임
3. 서블릿 클래스의 doGet() 또는 doPost() 메서드 실행
4. 처리 결과를 HTTP 응답으로 클라이언트에 전달

### 주의점
- 서블릿 기반 개발은 코드가 복잡해지고 유지보수 어려움 → Spring MVC가 이 단계를 추상화
- Spring의 DispatcherServlet이 서블릿 기반으로 동작

---

## 프레임워크와 API의 차이
| 구분 | 정의 | 흐름의 주도권 | 예시 |
| --- | --- | --- | --- |
| **API** | 기능을 사용하기 위한 명세 | 개발자가 주도 | List, Map, JDBC |
| **Framework** | 애플리케이션 구조와 실행 흐름을 제공 | 프레임워크가 주도 | Spring, Django |

### 추가 설명
- API는 **도구함**, 프레임워크는 **공사 설계도**
- API: `List.add()`, `JDBC.execute()` → 개발자가 호출
- 프레임워크: `@Controller`, `@Service` → Spring이 호출

#### 일상 예시
| 구분 | 설명 |
| --- | --- |
| API | 전자레인지에서 버튼 누르기 (내가 제어) |
| Framework | 요리 키트 배송받아 조리법 따라하기 (레시피가 제어 흐름 제공) |

[API vs 라이브러리/프레임워크 참고](https://velog.io/@bcl0206/API-vs-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-%ED%92%80%EB%A6%AC%EC%A7%80-%EC%95%8A%EB%8A%94-%EB%AF%B8%EC%8A%A4%ED%84%B0%EB%A6%AC%EC%97%90-%EA%B4%80%ED%95%98%EC%97%AC)
