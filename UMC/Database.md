| Course_ID | Course_Name |
| --- | --- |
| 101 | Math |
| 102 | Science |
- 정의: 테이블의 유일한 식별자
---
- 특징: 유일성, 최소성
### 제 3 정규화 (3NF): 이형적 함수 종속(Transitive Dependency) 제거
- 원칙: 기본키 → b → c일 때, c 분리
## 외래키(Foreign Key)
#### 3NF 위반 테이블
- Student_ID → Dept_ID → Dept_Name의 이행적 종속 관계 존재
- 목적: 참조 무결성 보장
| Student_ID | Student_Name | Dept_ID | Dept_Name |
| --- | --- | --- | --- |
| 1 | Alice | 10 | Computer Science |
| 2 | Bob | 20 | Mathematics |
| **제약 조건** | **설명** |
#### 3NF 적용 후
(부서 정보를 별도 테이블로 분리하여 이행적 종속 제거)
| CASCADE | 부모 테이블의 값이 변경되면, 자식 테이블도 자동 변경 |
**학생 테이블:**
| RESTRICT | 부모 데이터가 참조 중이면 삭제/수정 불가 |
| Student_ID | Student_Name | Dept_ID |
| --- | --- | --- |
| 1 | Alice | 10 |
| 2 | Bob | 20 |
---
**부서 테이블:**
## ER 다이어그램
| Dept_ID | Dept_Name |
| --- | --- |
| 10 | Computer Science |
| 20 | Mathematics |
2. 관계 (Relationships): 개체들 간의 연관성을 나타내는 요소 (마름모)
---

### 보이스 코드 정규화 (BCNF, Boyce-Codd Normal Form)
- 정의: 3NF를 만족하면서, 결정자가 후보 키가 아닌 경우를 제거 (즉, 모든 결정자가 후보 키여야 함.)

#### BCNF 위반 테이블
- {Student_ID, Course_ID}가 기본 키지만, 교수는 과목에 의해 결정
- 강한 개체: 기본키만으로 고유 식별이 가능한 개체 (사각형)
| Student_ID | Course_ID | Professor |
| --- | --- | --- |
| 1 | 101 | Dr. Smith |
| 2 | 102 | Dr. Brown |
1. 개념적 ER 모델 (Conceptual ER Model): 엔티티와 관계만 정의하고, 속성 및 데이터 타입은 포함하지 않음.
#### BCNF 적용 후
(교수 정보를 별도 테이블로 분리하여 후보 키가 아닌 결정자 제거)
3. 물리적 ER 모델 (Physical ER Model): 테이블, 데이터타입, 제약 조건까지 포함됨
**학생-과목 테이블:**

| Student_ID | Course_ID |
| --- | --- |
| 1 | 101 |
| 2 | 102 |
4. 관계
**과목-교수 테이블:**

| Course_ID | Professor |
| --- | --- |
| 101 | Dr. Smith |
| 102 | Dr. Brown |
## 연관관계
---

### 제 4 정규화 (4NF): 다치 종속(Multivalued Dependency) 제거
- 원칙: 하나의 기본키가 둘 이상의 독립적인 다치 종속을 가질 때 분리
1. 일대일 관계 (예: 사용자 - 사용자 프로필)
#### 4NF 위반 테이블
- 한 학생이 여러 과목을 듣고, 여러 동아리에 소속될 수 있음
3. 다대다 관계 (예: 학생 - 강의, 해시태그)
| Student_ID | Subject | Club |
| --- | --- | --- |
| 1 | Math | Drama |
| 1 | Math | Music |
| 1 | Science | Drama |
| 1 | Science | Music |

- 과목과 동아리는 서로 독립적이므로 불필요한 데이터 중복 발생
- 정의: 두 개 이상의 컬럼(속성)으로 구성된 기본 키
#### 4NF 적용 후
- 특징: 복합키도 유일성과 최소성 만족해야 함
**학생-과목 테이블:**

| Student_ID | Subject |
| --- | --- |
| 1 | Math |
| 1 | Science |
| **구성 요소** | 하나의 컬럼 | 두 개 이상의 컬럼 | 단일 또는 복합 키 참조 가능 |
**학생-동아리 테이블:**
| **NULL 허용** | 불가 | 불가 | 가능 (옵션 설정에 따라) |
| Student_ID | Club |
| --- | --- |
| 1 | Drama |
| 1 | Music |

---
- Join이 많아질 경우 성능 저하
### 제 5 정규화 (5NF): 조인 종속(Join Dependency) 제거
- 원칙: 분해된 테이블을 조인할 때 데이터가 중복되거나 손실되는 경우 방지
- ORM 사용시 추가적인 설정 필요
#### 5NF 위반 테이블
- 강의가 교수, 과목, 시간표 각각에 종속되지만 이들이 독립적으로 관리될 경우

| Professor | Subject | Time |
| --- | --- | --- |
| Dr. A | Math | Mon 9AM |
| Dr. A | Physics | Wed 11AM |
| Dr. B | Math | Wed 11AM |
### 제 1 정규화 (1NF): 반복되는 그룹 제거
- 이 구조는 개별 관계는 유지되지만, 3개를 모두 조합했을 때만 유효한 수업이 존재해야 함

#### 5NF 적용 시
세 개의 테이블로 분해하고, 복원 가능한 경우만 유지
**교수-과목 테이블, 교수-시간 테이블, 과목-시간 테이블 등으로 나눠 관리**
| --- | --- | --- |
→ 보통은 정규화를 많이 진행해도 보이스 코드 정규화까지 진행하고, 이후 필요에 따라 반 정규화를 진행한다.
| 2 | Bob | English, History |
[정규화에 대한 자세한 설명](https://www.purestorage.com/kr/knowledge/what-is-data-normalization.html?utm_source=chatgpt.com)
#### 1NF 적용 후 (원자값 유지)
[정보처리 실기_데이터베이스06강_정규화](https://youtu.be/RXQ1kZ_JHqg?si=f0OPsoOWnJXSbqca)
| Student_ID | Name | Subject |
---

## 반 정규화(Denormalization)
- 정의: 정규화를 거친 데이터베이스 구조에서 성능 향상 및 관리 편의를 위해 일부러 정규화된 구조를 깨는 과정
- 목적: (읽기 최적화) 정규화를 진행하면서 JOIN이 많아지면 성능 저하 가능, 자주 조회하거나 읽기 위주인 경우, 쓰기 작업이 적은 경우 데이터 조회 속도를 높이기 위해 사용, 복잡한 쿼리 단순화
  - 반대로 데이터 무결성이 중요하거나, 쓰기 연산이 많거나, 데이터 변경이 자주 발생하는 경우에는 반정규화를 진행하지 않는 것이 좋다.

### 방법
1. 테이블 병합 (Merge Tables): 정규화된 두 개 이상의 테이블을 하나로 합쳐 JOIN을 최소화
2. 컬럼 추가 (Add Redundant Columns): 자주 JOIN되는 테이블의 데이터를 컬럼으로 추가
3. 중복 데이터 허용 (Store Derived Data): 계산 비용이 큰 데이터를 미리 계산하여 저장 (SUM, COUNT, AVG 등의 집계 데이터)
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

      | --- | --- |
  | 101 | Math |
  | 102 | Science |

  제 3 정규화 : 2NF를 만족하면서 이형적 함수 종속(Transitive Dependency) 제거

  → 기본키 → b → c일 때, c 분리

  3NF 위반 테이블 - Student_ID → Dept_ID → Dept_Name의 이행적 종속 관계 존재

  | Student_ID | Student_Name | Dept_ID | Dept_Name |
      | --- | --- | --- | --- |
  | 1 | Alice | 10 | Computer Science |
  | 2 | Bob | 20 | Mathematics |

  3NF 적용 후 (부서 정보를 별도 테이블로 분리하여 이행적 종속 제거)

  학생 테이블:

  | Student_ID | Student_Name | Dept_ID |
      | --- | --- | --- |
  | 1 | Alice | 10 |
  | 2 | Bob | 20 |

  부서 테이블:

  | Dept_ID | Dept_Name |
      | --- | --- |
  | 10 | Computer Science |
  | 20 | Mathematics |

  보이스 코드 정규화 (BCNF, Boyce-Codd Normal Form) : 3NF를 만족하면서, 결정자가 후보 키가 아닌 경우를 제거 (즉, 모든 결정자가 후보 키여야 함.)

  BCNF 위반 테이블 - {Student_ID, Course_ID}가 기본 키지만, 교수는 과목에 의해 결정

  | Student_ID | Course_ID | Professor |
      | --- | --- | --- |
  | 1 | 101 | Dr. Smith |
  | 2 | 102 | Dr. Brown |

  BCNF 적용 후 (교수 정보를 별도 테이블로 분리하여 후보 키가 아닌 결정자 제거)

  학생-과목 테이블:

  | Student_ID | Course_ID |
      | --- | --- |
  | 1 | 101 |
  | 2 | 102 |

  과목-교수 테이블:

  | Course_ID | Professor |
      | --- | --- |
  | 101 | Dr. Smith |
  | 102 | Dr. Brown |

  제 4 정규화 (4NF) : 다치 종속(Multivalued Dependency) 제거

  → **하나의 기본키가 둘 이상의 독립적인 다치 종속을 가질 때 분리**

  4NF 위반 테이블 – 한 학생이 여러 과목을 듣고, 여러 동아리에 소속될 수 있음

  | Student_ID | Subject | Club |
      | --- | --- | --- |
  | 1 | Math | Drama |
  | 1 | Math | Music |
  | 1 | Science | Drama |
  | 1 | Science | Music |

  → 과목과 동아리는 서로 독립적이므로 **불필요한 데이터 중복 발생**

  4NF 적용 후

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

  제 5 정규화 (5NF) : 조인 종속(Join Dependency) 제거

  → **분해된 테이블을 조인할 때 데이터가 중복되거나 손실되는 경우 방지**

  5NF 위반 테이블 – 강의가 교수, 과목, 시간표 각각에 종속되지만 이들이 독립적으로 관리될 경우

  | Professor | Subject | Time |
      | --- | --- | --- |
  | Dr. A | Math | Mon 9AM |
  | Dr. A | Physics | Wed 11AM |
  | Dr. B | Math | Wed 11AM |

  → 이 구조는 개별 관계는 유지되지만, 3개를 모두 조합했을 때만 유효한 수업이 존재해야 함

  5NF 적용 시: 세 개의 테이블로 분해하고, 복원 가능한 경우만 유지

  **교수-과목 테이블, 교수-시간 테이블, 과목-시간 테이블 등으로 나눠 관리**

  → 보통은 정규화를 많이 진행해도 보이스 코드 정규화까지 진행하고, 이후 필요에 따라 반 정규화를 진행한다.

  https://www.purestorage.com/kr/knowledge/what-is-data-normalization.html?utm_source=chatgpt.com

  [정보처리 실기_데이터베이스06강_정규화](https://youtu.be/RXQ1kZ_JHqg?si=f0OPsoOWnJXSbqca)

- 반 정규화
    - 정의 : 정규화를 거친 데이터베이스 구조에서 성능 향상 및 관리 편의를 위해 일부러 정규화된 구조를 깨는 과정
    - 목적 : (읽기 최적화) 정규화를 진행하면서 JOIN이 많아지면 성능 저하 가능, 자주 조회하거나 읽기 위주인 경우, 쓰기 작업이 적은 경우 데이터 조회 속도를 높이기 위해 사용, 복잡한 쿼리 단순화

  → 반대로 데이터 무결성이 중요하거나, 쓰기 연산이 많거나, 데이터 변경이 자주 발생하는 경우에는 반정규화를 진행하지 않는 것이 좋다.

  방법

    1. 테이블 병합 (Merge Tables) : 정규화된 두 개 이상의 테이블을 하나로 합쳐 JOIN을 최소화
    2. 컬럼 추가 (Add Redundant Columns) : 자주 JOIN되는 테이블의 데이터를 컬럼으로 추가
    3. 중복 데이터 허용 (Store Derived Data) : 계산 비용이 큰 데이터를 미리 계산하여 저장 (SUM, COUNT, AVG 등의 집계 데이터)