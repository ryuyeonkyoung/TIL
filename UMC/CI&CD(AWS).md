# CI & CD (AWS)

## 배포(Deployment)
- **의미**: 개발한 서버 애플리케이션(WAS, Web Application Server)을 외부에서 접근 가능하도록 서버에 올리는 과정
- **온프레미스**: 직접 컴퓨터를 서버로 사용하여 배포하는 방식
- **클라우드 컴퓨팅**: AWS, Azure 등 클라우드 서비스에서 서버를 빌려 배포하는 방식

## VPC (Virtual Private Cloud)
- AWS 내 가상 네트워크 환경
- 기본 VPC(Default VPC)는 기본 설정 네트워크로 쉽게 EC2 생성 가능
- 실제 운영 환경에서는 보안과 네트워크 최적화를 위해 VPC 설정이 중요

## EC2 (Elastic Compute Cloud)
- AWS에서 제공하는 가상 서버 인스턴스
- 인스턴스 유형(t2.micro 등)과 OS 선택 가능 (Ubuntu, Amazon Linux 등)
- 프리티어: 신규 계정에 무료 제공되는 인스턴스 및 사용 시간

## GitHub Actions
- GitHub에서 제공하는 CI/CD 자동화 도구
- `.github/workflows` 폴더에 YAML 파일로 파이프라인 정의
- 특정 브랜치(push, PR 등) 이벤트에 맞춰 빌드, 테스트, 배포 자동 수행

## CI (Continuous Integration, 지속적 통합)
- 개발자가 코드 변경 시마다 자동으로 빌드, 테스트 등을 수행하여 코드 품질 유지
- 빌드 자동화, 테스트 자동화에 초점

## CD (Continuous Delivery / Deployment, 지속적 배포)
- CI 이후, 코드 변경이 자동으로 실제 서버에 배포되는 과정
- CD를 통해 배포 속도 및 안정성 향상

## 브랜치 전략 (Git Flow)
- 주요 브랜치: `feature/*`, `develop`, `release`, `main`(또는 master), `hotfix/*`
- 기능 개발은 `feature`에서, 통합은 `develop`에서 진행
- 배포 전 코드는 `release`나 `main` 브랜치에 병합
- 새 브랜치는 항상 `develop`에서 생성
- 작업 중 코드 변경은 `git stash`로 임시 보관 후 브랜치 전환

## Elastic Beanstalk
- AWS 인프라를 자동으로 **프로비저닝·배포·스케일링·모니터링**해 주는 완전관리형 애플리케이션 호스팅 서비스
- 개발자는 코드 번들(.zip, .war, 컨테이너 등)을 업로드하기만 하면 EB가 EC2·ELB·Auto Scaling·CloudWatch 등을 구성해 실행
- Go, Java, .NET, Node.js, PHP, Python, Ruby + **Docker 기반 사용자 지정 플랫폼** 지원

### 특징
- 콘솔·`eb` CLI·AWS CLI·API를 통한 손쉬운 환경 관리
- **무제한 확장**: Auto Scaling 정책 자동 생성 및 조정
- 추가 서비스 비용 없음 → **사용한 리소스**(EC2‧ELB‧RDS 등)만 과금

### 구조
- Application → Version → **Environment**(WebServer / Worker)
- Platform: 언어 런타임 + AMI + 관리 에이전트
- 구성 파일: `.ebextensions/`, `Procfile`, `Dockerrun.aws.json`
- 워크플로: 코드 업로드 → 환경 프로비저닝 → 배포/롤백 → 모니터링

### 장점
- 몇 분 만에 **CI/CD 파이프라인** 구축 가능
- 배포·용량 조정·헬스 체크 자동화로 **운영 오버헤드 감소**

### 사용 시 주의할 점
- 세부 네트워크·OS 레벨 설정 자유도가 제한될 수 있음
- 플랫폼 업그레이드 시 **환경 재생성**이나 다운타임 없는 롤링 업데이트 전략 필요

[Elastic Beanstalk 공식 문서](https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/Welcome.html)

---

## S3

### Amazon S3 (Simple Storage Service)
- AWS가 제공하는 객체 스토리지 서비스
- 파일을 버킷에 저장하고 URL·API·SDK로 읽고 쓰는 방식
- 핵심 가치: 무한 확장 · 높은 가용성 · 강한 보안

### 특징
- 버킷 단위로 관리 → 기본 비공개, 정책으로 세밀 제어
- 스토리지 클래스 다양 → Standard ~ Glacier Deep Archive까지 비용·지연 차등
- 읽기-후-쓰기 강한 일관성 제공(모든 Region)

### 구조
- Bucket: 최상위 컨테이너
- Object: 데이터 + 메타데이터, Key로 식별
- 옵션: Versioning, Lifecycle, Replication, Access Point 등

### 장점
- 저장한 만큼만 과금(프리티어 제공)
- Lifecycle·Intelligent-Tiering으로 자동 비용 최적화
- SDK·CLI 지원이 풍부해 개발‧운영 난도 낮음

### 단점 (사용 시 주의할 점)
- 동일 Key 동시 쓰기는 Last-Writer-Wins → 애플리케이션 잠금 필요
- 퍼블릭 접근 차단(Block Public Access) 해제 시 실수로 전체 공개 위험

[Amazon S3 공식 문서](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html)

## MIME Type
- 인터넷에서 **콘텐츠 형식**을 나타내는 표준 문자열(예: `text/html`, `image/png`)
- HTTP‧이메일‧S3 등에서 수신자가 **어떻게 처리·표시**할지 결정하는 메타데이터
- IANA가 공식 레지스트리를 관리하며, 애플리케이션·브라우저·서버가 공통으로 인식

### 특징
- `type/subtype` 2단계 계층 구조 + `; 파라미터`(선택)
- **대소문자 구분 없음**, 공백 금지, 127자 이하 권장
- `charset`, `boundary` 등 파라미터로 인코딩·멀티파트 정보 추가 가능

### 구조
- 기본: `<type>/<subtype>`
    - type: `text`, `image`, `application`, `audio`, `video`, `multipart`, …
    - subtype: 형식 세부 구분 (`plain`, `jpeg`, `json`, …)
- 예시
    - `text/plain; charset=UTF-8`
    - `application/json`
    - `multipart/form-data; boundary=----WebKitFormBoundary`

### 장점
- 클라이언트가 **자동 렌더링 또는 다운로드** 결정 → 사용자 경험 향상
- 서버–클라이언트 간 **콘텐츠 협상(Content Negotiation)** 가능
- 파일 확장자 대신 신뢰할 수 있는 형식 판별 기준 제공

### 사용 시 주의할 점
- 잘못 설정 시 브라우저 오동작(다운로드·XSS 위험) 발생
- 신규 포맷은 등록 지연 → `application/octet-stream`으로 처리되는 불편
- 일부 환경은 확장자·파일 헤더 우선 감지 → 헤더 값과 불일치 시 혼란

## MultipartFile
- `multipart/form-data` 요청의 **개별 파일 파트**를 추상화한 인터페이스
- 브라우저(또는 클라이언트)에서 업로드된 파일을 자바 객체로 제공
- 파일명·크기·MIME Type·스트림 등을 손쉽게 조회·저장 가능

### 특징
- HTTP 메시지 파싱은 Spring(MultipartResolver)이 담당, 개발자는 메서드 호출만으로 파일 사용
- 임시 디스크·메모리 저장 뒤 제공 → 대용량 업로드도 안정적 처리
- `@RequestParam`·`@RequestPart`·`ModelAttribute` 등과 함께 활용 가능

### 구조
- 주요 구현체
    - `StandardMultipartFile` (Servlet 3.0 `Part` 기반)
    - `CommonsMultipartFile` (Apache Commons FileUpload 기반, 선택)
- 핵심 메서드
    1. `String getName()` – 폼 필드 이름
    2. `String getOriginalFilename()` – 클라이언트 파일명
    3. `String getContentType()` – MIME Type (nullable)
    4. `boolean isEmpty()` – 파일 비어 있는지
    5. `long getSize()` – 바이트 크기
    6. `byte[] getBytes()` – 전체 바이트 배열
    7. `InputStream getInputStream()` – 스트림 읽기
    8. `void transferTo(File dest)` – 서버 로컬 파일로 저장 (예외 처리 필요)

### 예제 코드
```java
// 1) 컨트롤러 – JSON + 파일 동시 업로드
@PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ApiResponse<Long> createPost(
        @RequestPart("dto")   @Valid PostRequest dto,
        @RequestPart("image") MultipartFile image) throws IOException {

    Long id = postService.save(dto, image);
    return ApiResponse.ok(id);
}
```

[Spring MultipartFile 공식 문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html)
