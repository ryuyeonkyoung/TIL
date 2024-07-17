## 컬렉션 프레임워크 (JCF. Java Collectino Framework)
- 다수의 데이터를 쉽고 효과적으로 관리할 수 있는 표준화된 방법을 제공하는 클래스의 집합
- 재사용성이 높다.
- 인터페이스, 구현 클래스, 유틸리티 클래스로 나눠진다.

## 컬렉션 프레임워크: 인터페이스와 구현 클래스
- Collection : List, Set, Queue
  - List : ArrayList, LinkedList, Vector
    - ArrayList : 배열기반. 삽입/삭제 O(n), 인덱스 O(1)
    - LinkedList : 노드기반. 삽입/삭제 O(1), 인덱스 O(n)
- Map (구조상의 차이로 인해 별도로 정의)
  - HashMap : 무순서. 정렬 불가능
  - TreeMap : 순서. 정렬 가능. Red-Black Tree기반
  - Stack

### Set vs List vs Map
- Set : 중복 허용x
- List : 중복 허용o
- Map : 키는 중복 허용x, 값은 중복 허용o

### Queue vs Stack
- Queue : FIFO
- Stack : LIFO

### HashMap/HashSet에서 해시 충돌 해결방법
- Chaining : 같은 버킷에 LinkedList 연결
- Open Addressing : 다른 빈 슬롯에 데이터 삽입

### Iterator vs for-each
- Iterator : 요소제거, 커서 위치 변경
- for-each : 단순히 요소를 반복할 때

## 참고자료
- https://dev-wnstjd.tistory.com/m/491