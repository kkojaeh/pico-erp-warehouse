# 창고 관리

## 내용

### 기능

- 입고
- 출고
- 운송
- 창고
- 폐기

### 장소

아래로 순차적으로 상세한 장소를 의미하며 하위 장소는 상위 장소에 귀속된다.

- Site : 물리적으로 분리된 공간(다른 건물을 의미)
- Zone: 지역
- Rack: 랙
- Bay: 파레트 하나의 면적으로 랙을 나눈 구역
- Level: 하나의 Bay 기준의 층수

창고의 위치 기준은 위의 Level 이 된다
외부에서는 직접적으로 Level 을 참조하지 않고 Location 을 참조하여 관리된다.


## Release

```
./gradlew release -Prelease.useAutomaticVersion=true
```

## DDL 생성

### 명령어
```
./gradlew generateSchema
```

### 출력 위치
```
build/generated-schema/create.sql
```

## IntelliJ Setting

* Settings
  * Build, Execution, Deployment > Build Tools > Gradle > Runner
    * Delegate IDE build/run actions to Gradle 활성화
