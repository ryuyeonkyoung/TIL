# Paging (Spring Data JPA)

## Page vs Slice

| **기능** | **Page** | **Slice** |
| --- | --- | --- |
| 전체 데이터 개수 | O | X |
| 다음 페이지 확인 | O | O |
| COUNT 쿼리 실행 | O | X |
| 성능 | 상대적 저하 | 우수 |

---

## Page
- 전체 데이터 개수와 전체 페이지 수를 제공(getTotalElements(), getTotalPages())
- 내부적으로 COUNT(*) 쿼리를 추가 실행

```java
public interface Page<T> extends Slice<T> {
    long getTotalElements();
    int getTotalPages();
}
```

---

## Slice
- 다음 페이지 존재 여부만 확인(hasNext())
- LIMIT + 1 방식으로 추가 쿼리 없이 다음 페이지 확인

```java
public interface Slice<T> {
    int getNumber();  // 현재 페이지 번호 (0-based)
    boolean hasNext();
    List<T> getContent();
}
```

---

## 내부 구조
- Page는 Slice를 상속받는다.

### Slice의 주요 필드
- `List<T> content`: 현재 페이지의 데이터 목록
- `Pageable pageable`: 현재 페이지의 페이징 정보(페이지 번호, 크기, 정렬 등)
- `boolean hasNext`: 다음 페이지가 존재하는지 여부

### 주요 메서드
- `getContent()`: 데이터 목록 반환
- `getNumber()`: 현재 페이지 번호
- `getSize()`: 한 페이지당 데이터 개수
- `hasNext()`: 다음 페이지 존재 여부
- `isFirst()` / `isLast()`: 첫/마지막 페이지 여부
- `getPageable()`, `nextPageable()`, `previousPageable()`
- `<U> Slice<U> map(Function<? super T, ? extends U> converter)`: 데이터 변환

### 특징
- 전체 데이터 개수, 전체 페이지 수 정보를 **가지지 않음**
- 다음 페이지가 있는지(`hasNext`)만 판단
- **COUNT 쿼리 없이** LIMIT+1 방식으로 다음 페이지 존재 여부만 확인

---

## Page의 추가 필드/메서드
- `long total`: 전체 데이터 개수
- `getTotalElements()`: 전체 데이터 개수 반환
- `getTotalPages()`: 전체 페이지 수 반환

### 특징
- 전체 데이터 개수, 전체 페이지 수 정보를 **추가로 제공**
- 데이터를 조회하는 쿼리 외에, **COUNT(*) 쿼리가 추가로 실행**됨
- 전체 페이지 네비게이션, 전체 개수 표시가 필요한 UI에 적합

---

## 사용 방법

### 1. Repository 계층
```java
Page<Member> findByAge(int age, Pageable pageable);
Slice<Member> findSliceByAge(int age, Pageable pageable);
```

### 2. Controller 계층
```java
@GetMapping("/members")
public Page<Member> getMembers(
    @PageableDefault(size=20, sort="name", direction=Sort.Direction.ASC) Pageable pageable
) {
    return memberService.findMembers(pageable);
}
```

`@PageableDefault`: 기본 페이지 설정

`PageRequest.of(page, size)`: 페이지 번호(0-based)와 크기 지정

---

## 관련 어노테이션 및 설정

### `@PageableDefault`

```java
@PageableDefault
public List<User> findByLastName(
    @RequestParam String lastName,
    @PageableDefault(size=100, sort="id", direction = DESC) Pageable pageable
) { ... }
```

### spring.data.web.pageable

```yaml
spring:
  data:
    web:
      pageable:
        default-page-size: 20
        max-page-size: 200

```

# 객체 그래프 탐색

→ 엔티티 객체의 참조(연관관계)를 따라가며 필요한 데이터를 가져오는 것

| **접근 방식** | **객체 그래프 탐색** | **명시적 조인(fetch)** |
| --- | --- | --- |
| 로딩 전략 | **LAZY**(프록시) / **EAGER**(JOIN) | 항상 즉시 로딩 (JOIN FETCH) |
| N+1 문제 | 리스크 존재 | JOIN FETCH 또는 서브쿼리로 해결 |
| 코드 가독성 | 간단한 엔티티 참조 (member.getTeam()) | JPQL 문장에 조인 절이 명시되어 다소 장황 |
| 성능 | 조회 시점에 따라 쿼리 수 증가 가능 | 최적화된 단일 쿼리로 성능 안정화 |

---

## 객체 그래프 탐색
- 연관 엔티티를 객체 참조로 탐색(member.getTeam().getOrders())
- **LAZY**: 실제 사용 시점에 SQL 실행
- **EAGER**: 조회 시점에 JOIN 실행

```java
Member member = em.find(Member.class, 1L);
Team team = member.getTeam();
List<Order> orders = member.getOrders();
```

---

## 언제 사용할까?
- 비즈니스 로직 단계에서 엔티티 연결이 직관적으로 필요할 때
- 단순 조회 API가 아닌, 도메인 객체 자체를 조작·검증하는 서비스 로직 내

---

## JPQL 경로 표현식
- 단일 값 연관 경로

    ```sql
    select m.team from Member m
    ```

- 컬렉션 값 연관 경로
  select m.orders from Member m

```sql
select m.team from Member m
```

---

## 적용
1. 모든 연관관계 **LAZY** 설정
2. `@EntityGraph(attributePaths = {"orders"})` 활용
3. `hibernate.default_batch_fetch_size=100` 설정
4. DTO 직접 조회로 OSIV 문제 방지

---

# Q&A

- Page와 Slice의 가장 큰 구조적 차이와, 각각을 선택하는 기준은 무엇인가요?

  Page는 전체 데이터 개수와 페이지 수를 제공하기 위해 COUNT 쿼리를 추가로 실행한다. Slice는 다음 페이지 존재 여부만 확인(LIMIT+1)하고 COUNT 쿼리를 실행하지 않아 대용량 데이터에서 성능이 좋다.

    - 전체 개수/페이지 수가 필요한 전통적인 UI(게시판 등)는 Page, 무한 스크롤/더보기 UI는 Slice가 적합하다.
- Page의 COUNT 쿼리가 너무 느릴 때, Slice를 사용하지 않고 Spring Data JPA에서 이를 최적화할 수 있는 방법은? (힌트: @Query 활용)

  @Query의 countQuery 속성에 별도의 단순 COUNT 쿼리를 작성한다.

    ```java
    @Query(value = "SELECT m FROM Member m",
           countQuery = "SELECT COUNT(m.id) FROM Member m")
    Page<Member> findMembersWithCustomCount(Pageable pageable);
    ```

- 객체 그래프 탐색 시 LazyInitializationException이 발생하는 대표적인 상황과, 이를 방지하는 방법은?

  상황:

    - 트랜잭션이 끝난 후 LAZY 연관 객체에 접근할 때 발생한다.

  방지:

    - 트랜잭션 내에서 모든 연관 객체를 사용
    - fetch join, @EntityGraph, DTO 변환 쿼리 등으로 미리 로딩
    - Open Session In View 사용(권장X)