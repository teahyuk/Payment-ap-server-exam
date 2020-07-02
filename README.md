# Rest API 기반 결제 시스템

- rest api 기반의 결제 시스템 ap 서비스 입니다.
- Spring boot 2 기반 입니다.
- build 툴로 maven 을  사용 하였 습니다.
    
## 요구사항 정리

- 트랜젝션 관리를 위한 StringData 관리
  - 3rdParty 로의 통신을 대신 한다고 생각하여 자체 테이블 1개만을 설계 함.
  - 테이블 명을 **CardCompany** 로 지었습니다.
- 데이터 저장 테이블은 Payment 와 Cancel 테이블 2개 설계
- 관계 테이블 입니다.
- 부분 취소 기능은 PaymentQuery 에서 Cancel의 합을 빼는 것으로 구현 하였습니다.
- 암호화 방식
  - AES256 (salted) 방식
  - 같은 key 라도 매번 암호화 된 문자가 다릅니다.
  - 다른 암호화된 문자라도 같은 key로 내부 메소드 이용하면 복호화 가능합니다.
- Uid 생성 방식
  - 중복 최소 화를 위하여 CardNumber + 현재 시간(millisecond) + random 값 으로 만듦니다.
  - 중복이 없는 생성 방식 이지만 방어 로직 으로 중복 시 재 생성 하게끔 수정 하였습니다.
    - CardCompany 에 uid 키로 체크하고 insert 하도록 코드 작성

## API

### 1. 결제 API

- (POST) /v1/payment

#### request

```json
{
    "cardNumber": "0321654879",
    "validity" : "0720",
    "cvc" : "096",
    "installment": 3,
    "amount": 42000,
    "vat": 50
}
```

#### response

```json
{
  "uid": "t/a1s3d_g4z6x5c7d8t9"
}
```

### 2. 결제 데이터 조회 API

- (GET) /v1/payment/{id}

#### response

```json
{
    "uid": "a1s2d3f4a5s6d7f8d9s0",
    "cardNumber": "******497***",
    "validity": "0720",
    "cvc": "000",
    "requestType": "PAYMENT",
    "amount": 2000,
    "vat": 4
}
```

### 3. 결제 취소 API

- (POST) /v1/payment/{id}/cancel

#### request

```json
{
    "amount": 4200,
    "vat": 0
}
```

#### response

```json
{
  "uid": "t/a1s3d_g4z6x5c7d8t9"
}
```

### 4. 결제 취소 데이터 조회 API

- (POST) /v1/payment/{id}/cancel/{cancelId}

#### response

```json
{
    "uid": "a1s2d3f4a5s6d7f8d9s0",
    "cardNumber": "******497***",
    "validity": "0720",
    "cvc": "000",
    "requestType": "CANCEL",
    "amount": 2000,
    "vat": 4
}
```

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

#### PAYMENT

|name|type|index|non null|비고|
|---|---|---|---|---|
|ID|long|pk|true|테이블 기본 키|
|UID|varchar(20)|unique index|true|TXid|
|CARD_INFO|varchar(20)|null|true|암호화된 카드정보|
|AMOUNT|integer|null|true|결제 요청 값|
|VAT|integer|null|true|부가세|

#### CANCEL

|name|type|index|non null|비고|
|---|---|---|---|---|
|ID|long|pk|true|테이블 기본 키|
|UID|varchar(20)|unique index|true|TXid|
|PAYMENT_ID|long|fk|true|결제 테이블 외래키|
|AMOUNT|integer|null|true|취소 요청 값|
|VAT|integer|null|true|부가세|

#### PaymentStatus

|name|type|index|non null|비고|
|---|---|---|---|---|
|ID|long|pk|true|테이블 기본 키|
|UID|varchar(20)|unique index|true|결제 정보 의 Txid|
|AMOUNT|integer|null|true|남은 결제 값|
|VAT|integer|null|true|남은 부가세 값|
  
### 통신구간 (String 데이터)

#### CardCompany

|name|type|index|non null|
|---|---|---|---|
|ID|long|pk|true|
|UID|varchar(20)|unique index|true|
|String|varchar(450)|null|true|
