# JVM이란?
- Java Vertual Machine (자바 가상 머신)
- 스택 기반
- JAVA는 OS와 무관하게 동작하는 독립적인 언어이다. JVM이 JAVA와 OS 사이에서 중간다리 역할을 해준다.

# 장점
- 운영체제에 독립적 : Write Once, Run Anywhere(WORA)
- 가비지 콜렉터를 통해 자동 메모리 관리

# 단점
- 실행 속도가 느림 : 바이트 코드를 사용하므로 기계어 코드를 사용하는 애플리케이션보다 느릴 수 있다.
    -> JIT(Just-In-Time) 컴파일러, HotSpot VM, GraalVM
- 다중 상속이나 타입에 엄격, 제약이 많음

# JVM의 구조
1. Class Loader
2. Runtime data areas
3. Execution Engine
4. GC

## 1. Class Loader
클래스 파일을 로드하고 링크한다.
정확히는 Runtime Data Area의 메서드 영역으로 로드하고, 심볼릭 참조를 토대로 실제 메모리 주소로 링크한다.

## 2. Runtime data areas
자바의 메모리 공간. 런타임시 클래스 데이터와 같은 메타 데이터와 실제 데이터가 저장되는 곳이다.

## 3. Execution Engine
CLASS파일(바이트코드)를 실제 기계어(비트코드) 번역해 실행함.

## 4. GC (Garbage Collection. 가비지 컬렉션)
- 메모리를 관리한다
- 동적으로 할당되었던 메모리 중에서 필요없어진 메모리 영역을 회수한다.
- 힙 영역을 관리하기 위해 더이상 사용되지 않는 인스턴스(객체)를 관리한다.

# 과정 (명령어 JAVA)
0. JAVA 파일이 JVM 밖에서 COMPILER를 통해 CLASS파일이 된다. (명령어 JAVAC)
1. CALSS파일이 CLASS LOADER을 통과한다.
2. EXECUTION ENGINE에서 바이트코드인 CLASS파일을 비트코드인 기계어로 바꿔준다.
3. 데이터가 RUNTIME DATA AREAS로 이동한다.

# 참고자료
- https://doozi0316.tistory.com/entry/1%EC%A3%BC%EC%B0%A8-JVM%EC%9D%80-%EB%AC%B4%EC%97%87%EC%9D%B4%EB%A9%B0-%EC%9E%90%EB%B0%94-%EC%BD%94%EB%93%9C%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%8B%A4%ED%96%89%ED%95%98%EB%8A%94-%EA%B2%83%EC%9D%B8%EA%B0%80