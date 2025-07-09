# JPA (2)

## 지연로딩과 즉시로딩의 차이
- 연관된 엔티티를 **언제 조회할지 결정하는 전략**

| 구분 | Lazy (지연로딩) | Eager (즉시로딩) |
| --- | --- | --- |
| 조회 시점 | 실제 사용 시 쿼리 실행 | 엔티티 조회 시 즉시 함께 로딩 |
| 성능 | 초기에는 빠름 (불필요한 쿼리 방지) | 초기 로딩 비용 증가 |
| 위험 | N+1 문제 발생 가능 | 필요 없는 연관 객체까지 모두 로딩 |
| 기본값 | `@OneToMany` 등 대부분 Lazy | `@ManyToOne`은 Eager |

- 대부분의 연관 관계는 Lazy로 설정하고, **실제로 필요한 경우에만 Fetch Join/EntityGraph로 즉시로딩을 제어**하는 것이 좋다.

---

## Fetch Join
- 연관 엔티티를 함께 조회하기 위해 **JOIN FETCH 구문을 JPQL에 명시**
- N+1 문제 해결에 가장 직관적이고 강력한 방법

```java
@Query("SELECT m FROM Member m JOIN FETCH m.team")
List<Member> findAllWithTeam();
```

### 장점
- 가장 빠르고 명확하게 Lazy 로딩 문제 해결
- 1번의 쿼리로 모든 데이터 가져올 수 있음

### 단점
- **여러 컬렉션과 함께 사용하면 안됨 (JPA 제약)**
- **복잡한 쿼리에선 성능 이슈 발생 가능**

---

## @EntityGraph
- JPQL을 수정하지 않고도 **연관 엔티티를 함께 조회**하는 설정
- Fetch Join과 유사한 효과를 제공하지만 **더 선언적이고 깔끔**

```java
@EntityGraph(attributePaths = {"team"})
List<Member> findAll();  // team도 함께 조회됨
```

### 장점
- JPQL을 건드리지 않아 **유지보수가 쉬움**
- 상황별로 다양한 조회 전략 설정 가능

### 단점
- 복잡한 fetch 전략 구현엔 한계
- 설정 실수 시 오류 발생

---

## JPQL
**JPQL (Java Persistence Query Language)**
- SQL과 유사하지만, 테이블이 아닌 **엔티티 객체**를 대상으로 쿼리 작성하는 JPA의 쿼리 언어

### 사용 방식
- 메서드 네이밍 전략: `findByNameAndStatus(...)`
- `@Query` 어노테이션 사용: 직접 쿼리 작성

### 특징
- 객체 지향 쿼리이므로, 엔티티 필드를 기준으로 작성
- SQL보다 타입 안정성이 낮고, 복잡한 동적 쿼리 작성에는 부적합 → QueryDSL

---

## QueryDSL
- 타입 안전성을 보장하는 코드 기반 쿼리 빌더 (실무에서 선호도 높음)

### 장점
- 컴파일 시점 오류 검출 가능
- 메서드 체이닝으로 가독성 좋음
- 복잡한 **동적 쿼리 작성**에 최적화 (BooleanBuilder 등)

### 단점
- Q 클래스 생성 설정 필요
- 설정이 다소 복잡

### 사용 방식
- `JPAQueryFactory`를 Bean 등록 후 의존성 주입
- `Q엔티티` 객체 기반으로 쿼리 작성
- 조건문 조합은 `BooleanBuilder`로 동적 생성

---

## 퀴즈

1. 즉시 로딩(EAGER)은 왜 실무에서 지양되는지?
2. 영속성 컨텍스트의 Dirty Checking은 언제 동작하는가?
3. 다음 조건을 만족하는 복잡한 검색 쿼리를 만들고 싶을 때 추천되는 방법은?
   (조건: name이 있을 수도 없을 수도 있고, 점수가 4.0 이상인 경우만 포함)
4. 여러 조건(name, status, createdAt 등)이 모두 null일 수도 있는 상황에서 QueryDSL을 쓰려고 한다. 이때 BooleanBuilder로만 구현하면 코드가 너무 길어지는데, 실무에서는 어떻게 개선할 수 있을지?

---

## 정답

A1. 연관된 모든 데이터를 한 번에 가져와서 불필요한 쿼리가 실행되고, 특히 **N+1 문제**가 발생하기 쉬움.

A2. **트랜잭션 커밋 시점**에 엔티티의 변경 사항을 감지해 자동으로 UPDATE 쿼리를 실행함.

findAll(),

A3. **QueryDSL + BooleanBuilder로 동적 조건을 조합하여 구현.**

A4. BooleanBuilder는 좋지만, if문이 많아질수록 유지보수가 어려움. → BooleanExpression 헬퍼 함수 분리 패턴, where() 다중 파라미터 활용 등으로 개선 가능

+) @Null, 에러 처리를 앞에서 해도 됨.
