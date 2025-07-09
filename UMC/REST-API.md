# REST-API

## alias 방식 vs termsId 방식의 장단점과 선택 기준

| 구분 | alias 방식 | termsId 방식 |
| --- | --- | --- |
| 장점 | 프론트 코드 작성이 직관적, 키워드 기반 | DB 정합성 보장, 확장성 있음 |
| 단점 | 서버에서 alias → id 매핑 필요 | 프론트에서 termsId를 미리 알아야 함 |
| 사용 기준 | 데이터가 고정일 때 | 데이터의 수정이 잦을 때 |

> **추가설명**: 기본적으로는 id 기반으로 저장하는게 성능면에서 유리함. 하지만 만약 다른 이유로 alias방식을 사용한다면, 그 때는 매핑을 redis에 저장하는 방법을 통해 좀 더 성능을 최적화할 수 있음.

---

## alias 기반으로 받은 데이터를 어떻게 DB에 저장할 수 있을까요?

1. 서버에서 alias와 db 컬럼을 연결해둔 map을 만든다
2. json의 alias를 db 속의 컬럼과 매핑한다.

---

## JSON 배열([])과 객체({})의 차이

### 예시 코드
```json
{
  "name": "김철수",
  "gender": "남",
  "birth": "1111/11/11",
  "address": [
    "00도 00시 00동",
    "000동 0000호"
  ],
  "foodPreferences": ["한식", "일식", "양식"],
  "agreements": {
    "agreeTerms": true,
    "agreePrivacy": true,
    "agreeMarketing": false
  }
}
```

- **foodPreferences** 값의 목록을 저장해야 하므로 배열([])로 표현하고, **agreements**는 각 항목마다 고유 키와 값이 있으므로 key-value 형태인 객체({})로 표현한다.
- java에서는 배열 또한 객체이지만, **json에서는 배열은 객체**가 아니다. (json에서 객체는 key-value 값들을 모은 object 밖에 없다)

### 이 json을 받는 DTO 예시
```java
public class SignupRequest {
    private String name;
    private String gender;
    private String birth;
    private List<String> address;
    private List<String> foodPreferences; // 음식 선호도: 리스트 구조 []
    private Map<String, Boolean> agreements; // 약관 동의: key-value 구조 {}
    // getter, setter 생략
}
```
