# Database

## 기본키(Primary Key)
- 정의: 테이블의 유일한 식별자
- 목적: 개체 무결성 보장
- 특징: 유일성, 최소성

## 외래키(Foreign Key)
- 정의: 다른 테이블의 기본키를 참조하는 속성으로, 테이블간의 관계를 정의하는 키
- 목적: 참조 무결성 보장
- 특징: 중복 가능, null값을 가질 수 있다.

### 외래키 제약조건
| **제약 조건** | **설명** |
| --- | --- |
| CASCADE | 부모 테이블의 값이 변경되면, 자식 테이블도 자동 변경 |
| SET NULL | 부모 데이터 삭제 시 자식 테이블의 값은 NULL 처리 |
| RESTRICT | 부모 데이터가 참조 중이면 삭제/수정 불가 |
| NO ACTION | RESTRICT와 유사하나, 지연된 무결성 검사 |

[MariaDB Foreign Key 공식 문서](https://mariadb.com/kb/ko/foreign-keys/?utm_source=chatgpt.com)

---

## ER 다이어그램
- 데이터베이스 내 개체간의 관계를 시각적으로 표현하는 다이어그램

### ER 다이어그램 주요 요소
1. 개체(Entity): 데이터베이스에서 저장할 수 있는 대상 (사각형)
2. 관계 (Relationships): 개체들 간의 연관성을 나타내는 요소 (마름모)
3. 속성 (Attributes): 개체가 가지는 정보 및 특성 (타원형)

#### 강한 개체 vs 약한 개체
- 강한 개체: 기본키만으로 고유 식별이 가능한 개체 (사각형)
- 약한 개체: 외래 키를 통해서만 식별 가능한 개체 (이중 사각형)
  - 약한 개체는 반드시 소유 개체의 기본 키를 외래 키로 가짐

### ER 다이어그램의 유형
1. 개념적 ER 모델 (Conceptual ER Model): 엔티티와 관계만 정의하고, 속성 및 데이터 타입은 포함하지 않음.
2. 논리적 ER 모델 (Logical ER Model): 속성, 기본키, 외래키까지 포함
3. 물리적 ER 모델 (Physical ER Model): 테이블, 데이터타입, 제약 조건까지 포함됨
  - 밑으로 갈수록 포함되는 게 더 많음.

#### 그리는 순서
1. 엔티티
2. 속성
3. 기본키
4. 관계
5. 외래키

[IBM ERD 설명](https://www.ibm.com/think/topics/entity-relationship-diagram)

---

## 연관관계
- 정의: 개체들간의 관계

### 연관관계의 유형
1. 일대일 관계 (예: 사용자 - 사용자 프로필)
2. 일대다 관계 (예: 회원 - 주문)
3. 다대다 관계 (예: 학생 - 강의, 해시태그)

### 연관관계의 방향성
- 단방향 (Unidirectional): 한 객체에서만 관계를 참조 가능
- 양방향 (Bidirectional): 양쪽 개체에서 서로를 참조 가능

---

## 복합 키(Composite Key)
- 정의: 두 개 이상의 컬럼(속성)으로 구성된 기본 키
- 목적: 유일성 보장
- 특징: 복합키도 유일성과 최소성 만족해야 함
- 활용: 업무적으로 단일 식별자 도입이 부적절한 경우, 다대다 관계에서 중간 테이블

### 기본키 vs 복합키 vs 외래키
| **구분** | **단일 키(Single Primary Key)** | **복합 키(Composite Primary Key)** | **외래 키(Foreign Key)** |
| --- | --- | --- | --- |
| **역할** | 테이블의 유일 식별자 | 여러 컬럼 조합으로 유일 식별 | 다른 테이블의 기본키 참조 |
| **구성 요소** | 하나의 컬럼 | 두 개 이상의 컬럼 | 단일 또는 복합 키 참조 가능 |
| **유일성 보장** | 단일 컬럼으로 유일 | 컬럼 조합으로 유일 | 유일성 보장 X (중복 가능) |
| **NULL 허용** | 불가 | 불가 | 가능 (옵션 설정에 따라) |
| **사용 예시** | 회원 ID, 주문 번호 | 회원-역할 중간 테이블 (M:N) | 주문 테이블의 member_id 참조 |
| **JOIN 성능** | 단순 조인으로 빠름 | 조인 조건 복잡 → 성능 고려 | 외래키에 인덱스 없으면 느려질 수 있음 |
| **제약 조건** | PRIMARY KEY | PRIMARY KEY(col1, col2) | FOREIGN KEY(col) REFERENCES 대상테이블(기본키) |
| **JPA 매핑** | @Id | @EmbeddedId 또는 @IdClass | @ManyToOne + @JoinColumn |

#### 복합 키의 단점
- Join이 많아질 경우 성능 저하
- 인덱스 설정이 복잡할 수 있음
- ORM 사용시 추가적인 설정 필요
  - 따라서, 테이블 규모가 커지면 복합 키 대신 별도의 ID를 생성하는 방법도 고려

---

## 정규화(Normalization)
- 목적: 데이터 중복 없애기, 무결성 유지, 이상(Anomaly) 방지

### 제 1 정규화 (1NF): 반복되는 그룹 제거
- 원칙: 모든 속성이 원자값(Atomic Value)을 가져야 함.

#### 1NF 위반 테이블
| Student_ID | Name | Subjects |
| --- | --- | --- |
| 1 | Alice | Math, Science |
| 2 | Bob | English, History |

#### 1NF 적용 후 (원자값 유지)
| Student_ID | Name | Subject |
| --- | --- | --- |
| 1 | Alice | Math |
| 1 | Alice | Science |
| 2 | Bob | English |
| 2 | Bob | History |

---

### 제 2 정규화 (2NF): 부분적 종속(Partial Dependency) 제거
- 원칙: 기본키가 복합키일 때, 기본키의 일부에만 종속되는 컬럼 분리

#### 2NF 위반 테이블
| Student_ID | Course_ID | Student_Name | Course_Name |
| --- | --- | --- | --- |
| 1 | 101 | Alice | Math |
| 2 | 102 | Bob | Science |

#### 2NF 적용 후
**학생 테이블:**
| Student_ID | Student_Name |
| --- | --- |
| 1 | Alice |
| 2 | Bob |
**과목 테이블:**
| Course_ID | Course_Name |
| --- | --- |
| 101 | Math |
| 102 | Science |

---

### 제 3 정규화 (3NF): 이행적 함수 종속(Transitive Dependency) 제거
- 원칙: 기본키 → b → c일 때, c 분리

#### 3NF 위반 테이블
- Student_ID → Dept_ID → Dept_Name의 이행적 종속 관계 존재
| Student_ID | Student_Name | Dept_ID | Dept_Name |
| --- | --- | --- | --- |
| 1 | Alice | 10 | Computer Science |
| 2 | Bob | 20 | Mathematics |

#### 3NF 적용 후
**학생 테이블:**
| Student_ID | Student_Name | Dept_ID |
| --- | --- | --- |
| 1 | Alice | 10 |
| 2 | Bob | 20 |
**부서 테이블:**
| Dept_ID | Dept_Name |
| --- | --- |
| 10 | Computer Science |
| 20 | Mathematics |

---

### 보이스 코드 정규화 (BCNF, Boyce-Codd Normal Form)
- 정의: 3NF를 만족하면서, 결정자가 후보 키가 아닌 경우를 제거 (즉, 모든 결정자가 후보 키여야 함.)

#### BCNF 위반 테이블
- {Student_ID, Course_ID}가 기본 키지만, 교수는 과목에 의해 결정
| Student_ID | Course_ID | Professor |
| --- | --- | --- |
| 1 | 101 | Dr. Smith |
| 2 | 102 | Dr. Brown |

#### BCNF 적용 후
**학생-과목 테이블:**
| Student_ID | Course_ID |
| --- | --- |
| 1 | 101 |
| 2 | 102 |
**과목-교수 테이블:**
| Course_ID | Professor |
| --- | --- |
| 101 | Dr. Smith |
| 102 | Dr. Brown |

---

### 제 4 정규화 (4NF): 다치 종속(Multivalued Dependency) 제거
- 원칙: 하나의 기본키가 둘 이상의 독립적인 다치 종속을 가질 때 분리

#### 4NF 위반 테이블
- 한 학생이 여러 과목을 듣고, 여러 동아리에 소속될 수 있음
| Student_ID | Subject | Club |
| --- | --- | --- |
| 1 | Math | Drama |
| 1 | Math | Music |
| 1 | Science | Drama |
| 1 | Science | Music |
- 과목과 동아리는 서로 독립적이므로 불필요한 데이터 중복 발생

#### 4NF 적용 후
**학생-과목 테이블:**
| Student_ID | Subject |
| --- | --- |
| 1 | Math |
| 1 | Science |
**학생-동아리 테이블:**
| Student_ID | Club |
| --- | --- |
| 1 | Drama |
| 1 | Music |

---

### 제 5 정규화 (5NF): 조인 종속(Join Dependency) 제거
- 원칙: 분해된 테이블을 조인할 때 데이터가 중복되거나 손실되는 경우 방지

#### 5NF 위반 테이블
- 강의가 교수, 과목, 시간표 각각에 종속되지만 이들이 독립적으로 관리될 경우
| Professor | Subject | Time |
| --- | --- | --- |
| Dr. A | Math | Mon 9AM |
| Dr. A | Physics | Wed 11AM |
| Dr. B | Math | Wed 11AM |
- 이 구조는 개별 관계는 유지되지만, 3개를 모두 조합했을 때만 유효한 수업이 존재해야 함

#### 5NF 적용 시
세 개의 테이블로 분해하고, 복원 가능한 경우만 유지
**교수-과목 테이블, 교수-시간 테이블, 과목-시간 테이블 등으로 나눠 관리**

→ 보통은 정규화를 많이 진행해도 보이스 코드 정규화까지 진행하고, 이후 필요에 따라 반 정규화를 진행한다.

[정규화에 대한 자세한 설명](https://www.purestorage.com/kr/knowledge/what-is-data-normalization.html?utm_source=chatgpt.com)
[정보처리 실기_데이터베이스06강_정규화](https://youtu.be/RXQ1kZ_JHqg?si=f0OPsoOWnJXSbqca)

---

## 반 정규화(Denormalization)
- 정의: 정규화를 거친 데이터베이스 구조에서 성능 향상 및 관리 편의를 위해 일부러 정규화된 구조를 깨는 과정
- 목적: (읽기 최적화) 정규화를 진행하면서 JOIN이 많아지면 성능 저하 가능, 자주 조회하거나 읽기 위주인 경우, 쓰기 작업이 적은 경우 데이터 조회 속도를 높이기 위해 사용, 복잡한 쿼리 단순화
  - 반대로 데이터 무결성이 중요하거나, 쓰기 연산이 많거나, 데이터 변경이 자주 발생하는 경우에는 반정규화를 진행하지 않는 것이 좋다.

### 방법
1. 테이블 병합 (Merge Tables): 정규화된 두 개 이상의 테이블을 하나로 합쳐 JOIN을 최소화
2. 컬럼 추가 (Add Redundant Columns): 자주 JOIN되는 테이블의 데이터를 컬럼으로 추가
3. 중복 데이터 허용 (Store Derived Data): 계산 비용이 큰 데이터를 미리 계산하여 저장 (SUM, COUNT, AVG 등의 집계 데이터)
