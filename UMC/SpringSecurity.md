# Spring Security

Java 애플리케이션에 인증 및 인가(Authorization)를 제공하는 데 중점을 둔 프레임워크.

---

## 주요 특징
- FilterChain: Spring MVC와 분리되어 동작
- Bean으로 설정 가능 (XML 설정 불필요)

---

## 주요 기능
1. 인증 및 인가에 대한 포괄적이고 확장 가능한 지원
2. 세션 고정, 클릭재킹, CSRF 등 다양한 공격에 대한 보호
3. 서블릿 API 통합
4. Spring Web MVC와의 통합(선택적)

---

## Spring Security의 구조
- Authentication Filter: 인증 처리를 시작하는 필터. 사용자 요청에서 인증정보를 추출하고 Authentication 객체를 생성, 이후 AuthenticationManager에 인증 처리를 위임
- UsernamePasswordAuthenticationFilter: 폼 로그인 요청을 처리하는 필터
- AuthenticationManager: 인증처리를 총괄하는 인터페이스, 실제 인증 로직은 Provider에 위임
- AuthenticationProvider: 실제 인증 로직을 구현하는 인터페이스
- UserDetailsService: username을 기준으로 DB 등에서 사용자 정보를 조회
- SecurityContext: 인증정보(Authentication 객체) 저장

---

## 인증 및 인가 지원
- 다양한 인증 방식, 권한 제어, 커스텀 필터 추가 등 유연한 보안 정책 적용 가능

---

## Spring Security가 제공하는 보안 기능

### 1) 세션 고정(Session Fixation) 방지
로그인 성공 후 새로운 세션 ID를 발급하고 기존 세션을 무효화
```java
http
  .sessionManagement()
    .sessionFixation().migrateSession();
```

### 2) 클릭재킹(Clickjacking) 방지
HTTP 응답 헤더에 X-Frame-Options를 자동으로 추가하여 iframe 삽입을 제한
```java
http
  .headers()
    .frameOptions().sameOrigin();
```

### 3) CSRF(Cross-Site Request Forgery) 방지
CSRF 토큰을 쿠키로 발급하여, 요청 시 검증
```java
http
  .csrf()
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
```

---

## 서블릿 API 통합
- SecurityFilterChain, HttpServletRequest/Response, HttpSession 등과 직접 연동하여 세션 정보, 헤더, 인증 정보 관리

---

## Spring Web MVC와의 통합
- `@Controller`, `@RestController`, `@RequestMapping` 등 기존 Spring MVC 어노테이션과 결합하여 권한 제어 가능
- 메서드 보안: `@EnableGlobalMethodSecurity(prePostEnabled = true)` 활성화 후, `@PreAuthorize`, `@PostAuthorize` 어노테이션 사용
```java
@PreAuthorize("hasRole('ADMIN')")
public String adminOnlyEndpoint() { ... }
@PostAuthorize("returnObject.owner == authentication.name")
public Resource getResource() { ... }
```
- URL 보안: `WebSecurityConfigurerAdapter`를 통해 URL 패턴별로 접근 권한 설정
```java
http
  .authorizeRequests()
    .antMatchers("/admin/**").hasRole("ADMIN")
    .antMatchers("/user/**").authenticated()
    .anyRequest().permitAll();
```
- View: Thymeleaf, JSP 등에서 Spring Security 태그 라이브러리로 사용자 권한에 따라 화면 요소 제어
```java
<sec:authorize access="hasRole('ADMIN')">
  <!-- 관리자 전용 메뉴 -->
</sec:authorize>
```

---

## 인증(Authentication)과 인가(Authorization)
- 인증: 누구신지 (인증 정보를 추출 → 인증 객체 생성 → 인증 매니저에 인증 위임 → 인증 성공 시 SecurityContext에 인증 정보 저장)
- 인가: 여기 들어와도 되는지 (인증된 사용자의 권한 정보를 확인 → 요청한 리소스에 대한 접근 권한 체크 → 권한이 있으면 요청 처리, 없으면 예외 발생)

---

## 세션과 토큰
| **구분** | **세션(Session)** | **토큰(Token)** |
| --- | --- | --- |
| 상태 관리 | 서버 측에서 관리 | 클라이언트 측에서 관리 |
| 저장 위치 | 서버 메모리 | 클라이언트(로컬/세션스토리지 등) |
| 인증 방식 | 세션 ID로 서버에서 사용자 조회 | 토큰 자체로 인증 정보 포함 |
| 확장성 | 낮음 (서버 동기화 필요) | 높음 (분산 서버, 모바일 등 용이) |
| 보안성 | 상대적으로 높음 | 토큰 탈취 시 위험, 관리 필요 |
| 사용 예시 | 로그인, 금융 등 보안 중요한 서비스 | API, 모바일, 마이크로서비스 등 |

- 쿠키: 인증 정보 클라이언트가 관리
- 세션: 인증 정보 서버가 관리
- 토큰: 인증 정보 클라이언트가 관리

---

## 액세스 토큰(Access Token)과 리프레시 토큰(Refresh Token)
- 액세스 토큰: 탈취당했을 때 피해가 크다. 짧게 설정. 통신이 빈번하기에 탈취 가능성이 가장 높지만, 유효 기간을 짧게 설정했기에 피해가 비교적 적게 된다.
- 리프레시 토큰: 탈취당했을 때 피해가 비교적 적다. 길게 설정.

### JWT 예시
```json
{
  "iss": "https://YOUR_DOMAIN/", // 공개는 겹치면 안됨
  "sub": "auth0|123456",
  "aud": [
    "my-api-identifier",
    "https://YOUR_DOMAIN/userinfo"
  ],
  "azp": "YOUR_CLIENT_ID",
  "exp": 1489179954,
  "iat": 1489143954,
  "scope": "openid profile email address phone read:appointments"
}
```

### 서버-클라이언트 통신 과정
1. Access Token과 Refresh Token을 서버에서 받으면 클라이언트가 이를 로컬에 저장한다. 통신을 할 때는 헤더에 Access Token을 넣으면 된다.
2. Access Token의 유효기간이 만료되면, 서버는 `401(Unauthorized)` 에러코드로 응답한다.
3. 그럴 때 Access Token 대신 Refresh Token을 넣어 보낸다.
4. 그러면 서버는 로그인을 시켜주면서 Access Token을 새로 발급시켜준다.
5. Refresh Token이 만료되었을 때는 로그아웃 시킨다.

- 만료될 때마다
- Access Token이 만료될 때 같이 갱신 → 추천

---

## 참고 자료
- https://velog.io/@hope0206/Spring-Security-%EA%B5%AC%EC%A1%B0-%ED%9D%90%EB%A6%84-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%97%AD%ED%95%A0-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0
- https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-filters-review
- https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html
- https://sabarada.tistory.com/240
- https://velog.io/@ddangle/Session%EC%84%B8%EC%85%98%EA%B3%BC-Token%ED%86%A0%ED%81%B0%EC%9D%98-%EC%B0%A8%EC%9D%B4%EB%8A%94
- https://ksh-coding.tistory.com/113
- https://velog.io/@chuu1019/Access-Token%EA%B3%BC-Refresh-Token%EC%9D%B4%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B4%EA%B3%A0-%EC%99%9C-%ED%95%84%EC%9A%94%ED%95%A0%EA%B9%8C
- https://www.youtube.com/watch?v=LowJMwa7LCU
