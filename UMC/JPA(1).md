# JPA (1)

## Domain
- 소프트웨어가 해결하고자 하는 실제 세계의 주제나 문제를 의미한다
- 시스템에서 다루는 핵심 데이터와 비즈니스 규칙이 포함된다

### 역할
- 현실 세계의 개념을 프로그램 안에 객체로 표현한다
- 주로 Entity 클래스를 통해 데이터베이스 테이블과 매칭된다

### 장점
- 코드 구조가 명확해져 유지보수성이 높아진다
- 도메인 기반 테스트(단위 테스트)가 쉬워진다
- 복잡한 비즈니스 로직 변경에도 영향 범위를 최소화할 수 있다

### 단점
- 설계 초기에는 시간이 오래 걸릴 수 있다
- 과도하게 복잡한 도메인 모델은 오히려 유지보수를 어렵게 할 수 있다

---

## 양방향 매핑
- 두 엔티티가 서로를 참조하는 관계를 맺어 객체 간 탐색이 가능하도록 만드는 방법이다
- 연관된 객체를 양쪽 방향에서 조회하거나 조작할 수 있게 한다

### 역할
- 연관된 데이터를 함께 저장하거나 삭제할 때 cascade 설정을 적용할 수 있게 한다
- 객체 그래프 탐색이 쉬워져 코드 작성과 유지보수가 편해진다

### 장점
- 비즈니스 로직에서 객체 탐색이 자유롭다
- 연관된 데이터를 함께 다룰 때 코드가 깔끔해진다
- Cascade를 통해 편리하게 데이터 일괄 관리가 가능하다

### 단점
- 양방향 설정을 잘못하면 무한루프 문제(예: toString(), JSON 직렬화) 발생
- mappedBy를 잘못 설정하면 데이터가 이상하게 저장될 수 있다
- 관리 포인트가 늘어나므로 실수할 확률이 높아진다

---

## N + 1 문제
- 하나의 메인 쿼리로 데이터를 조회한 후, 각 결과마다 연관된 데이터를 조회하기 위해 추가 쿼리가 실행되는 현상이다
- 원래 가져오려던 엔티티 + 연관된 엔티티들을 다 가져오기 때문에 쿼리 횟수가 N+1번이 되어 성능이 급격히 저하될 수 있다

### 해결방법
1. Fetch Join
```java
@Query("SELECT m FROM Member m JOIN FETCH m.reviewList WHERE m.id = :id")
Member findMemberWithReviews(@Param("id") Long id);
```
2. EntityGraph 설정
```java
@EntityGraph(attributePaths = "reviewList")
@Query("SELECT m FROM Member m WHERE m.id = :id")
Member findMemberWithReviewsUsingEntityGraph(@Param("id") Long id);
```
3. Batch Size 조정
```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 100
```

### 세 해결방식 비교
| 방법 | 장점 | 단점 | 사용 추천 상황 |
| --- | --- | --- | --- |
| **Fetch Join** | - 매우 빠르고 확실하게 N+1 문제 해결<br>- 연관 엔티티를 즉시 함께 조회 | - 복잡한 join이 많아지면 성능 오히려 악화<br>- 다수의 컬렉션 Fetch Join 제한 (Hibernate 버전 문제) | - 특정 조회에서 반드시 연관 데이터를 같이 써야 할 때<br>- 단건 조회나 간단한 구조의 다건 조회 |
| **EntityGraph** | - 코드가 깔끔함 (JPQL 수정 없이 적용)<br>- fetch join과 유사한 효과 | - 복잡한 fetch 전략 제어가 어려움<br>- EntityGraph 설정에 실수가 있으면 오류 발생 | - 복잡하지 않은 fetch를 깔끔하게 설정하고 싶을 때<br>- 유지보수성을 고려할 때 |
| **Batch Size 설정** | - Lazy 로딩을 유지하면서도 쿼리 수 감소<br>- 코드 수정 없이 설정만으로 효과 | - 결국 여러 쿼리를 날리는 구조<br>- 1:N 관계에서 데이터가 너무 많으면 의미가 없음 | - 여러 엔티티를 List로 조회할 때<br>- 모든 케이스에 기본적으로 깔아두기 좋음 |

---

## 기타 Q&A

- **Enum은 글자수가 정해져 있음에도 @Column으로 글자수를 제한한 이유는?**
  - @Enumerate만 사용하면 JPA가 자동으로 enum 이름을 **varchar(255)** 컬럼에 저장하려고 함

- **1:N 단방향 매핑에서는 어느 쪽에 @ManyToOne을 적용해야 할까?**
  - 외래키를 가진 N 쪽(예: MemberPrefer, MemberAgree)에서 @ManyToOne을 적용해야 한다.

- **양방향 매핑을 사용할 때 반드시 설정해야 하는 속성은?**
  - @OneToMany(mappedBy="...")에서 mappedBy를 정확히 설정해야 한다. mappedBy는 연관관계의 주인을 가리킨다.

- **Member를 삭제할 때 연결된 MemberPrefer나 Review까지 같이 삭제하고 싶다면 어떤 옵션을 추가해야 할까?**
  - @OneToMany에 cascade = CascadeType.ALL과 orphanRemoval = true를 추가해야 한다.

- **Spring Data JPA에서 enum 타입 필드를 매핑할 때@Enumerated(EnumType.STRING)를 사용하는 이유**
  - enum의 순서(숫자)가 저장돼서 → 순서 변경 시 에러가 발생할 수 있다.


Q. 양방향 연관관계는 어느 상황에서 사용하는지. 혹시 남발하면 성능 등 추후 문제가 생길 수 있는지 (최대한 다 적용해야 하는건지, 최소한으로 적용해야 하는건지 궁금합니다.)
→ 유저 엔티티를 사용해서 조회해야 한다 → 양방향, 아니면 단방향 (예시 있음)

reviewlist처럼 조회할 필요가 있으면 양방향