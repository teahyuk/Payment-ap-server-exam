# Rest API 기반 결제 시스템

- rest api 기반의 결제 시스템 ap 서비스 입니다.
- Spring boot 2 기반 입니다.
- build 툴로 maven 을  사용 하였 습니다.
- AP 서버로 써 MSA 에 대응 할 서비스 개발을 위해 state-less 하게 구현 하는 것이 목표 입니다.
  - 중복 결재 방지를 위해 작업 하는 부분은 옵션 입니다.
    1. 단순히 concurrentMap 으로 제어
    2. 클러스터링 을 위한 concurrentMap<->Redis 옵션화.
  - DB 는 property를 열어 3rd party DB 설정 가능하게끔 개발 합니다.
    
## 요구사항

- 실제 결재 요청은 3rd party service 로의 전달이 필요함.
- 결재 API 도 Post
- 결재 취소 API 도 Post 이다
- Amount 및 VAT 는 10억 (1,000,000,000) 이하 이다.
- 암/복호화 는 카드번호,유효기간,cvc 이다.
- 테이블 설계 는 추가 선택 요구사항인 부분 취소를 위해 결재 uid 와, Amount, Vat 만 설정 합니다.

## API

### 1. 결제 API

- (POST) /v1/payment

### 2. 결제 데이터 조회 API

- (GET) /v1/payment/{id}

### 3. 결제 취소 API

- (POST) /v1/payment/{id}/cancel

### 4. 결제 취소 데이터 조회 API

- (POST) /v1/payment/{id}/cancel/{cancelId}

## Domain

### VO

- CardNumber
  - 0으로 시작하는 번호가 있을 수 있다. (String 저장)
- Validity
  - 내부적으로 YearMonth를 사용하여 저장한다
  - isExpired()
    - 오늘 날짜 기준 만료일 계산
- CVC
  - 0으로 시작하는 번호가 있을 수 있다. (String 저장)
- Installment
  - 일시불(0), 할부 (2~12) int 로 제한 정함
- Amount
  - int 형 100 ~ 1,000,000,000 로 저장
  - createDefaultVat()
    - 자동 VAT 계산 this/11 
  - isValidVat
    - 현재 Vat 가 합당한 Vat 인지 확인
- Vat
  - int 형 100 ~ 1,000,000,000 로 저장

## Table

### 서비스 테이블

#### Payment

|name|type|index|non null|
|---|---|---|---|
|ID|long|pk|true|
|UID|varchar(20)|unique index|true|
|CARD_INFO|varchar(300)|null|true|
|AMOUNT|integer|null|true|
|VAT|integer|null|true|
|INSTALLMENT|integer|null|true|

  
#### Cancel

|name|type|index|non null|
|---|---|---|---|
|ID|long|pk|true|
|UID|varchar(20)|unique index|true|
|AMOUNT|integer|null|true|
|VAT|integer|null|true|

### 통신구간 (String 데이터)

#### StringData

|name|type|index|non null|
|---|---|---|---|
|ID|long|pk|true|
|UID|varchar(20)|unique index|true|
|String|varchar(450)|null|true|
