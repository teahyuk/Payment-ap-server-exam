# Rest API 기반 결제 시스템

- rest api 기반의 결제 시스템 ap 서비스 입니다.
- Spring boot 2 기반 입니다.
- build 툴로 maven 을  사용 하였 습니다.
- AP 서버로 써 MSA 에 대응 할 서비스 개발을 위해 state-less 하게 구현 하는 것이 목표 입니다.
  - 중복 결재 방지를 위해 작업 하는 부분은 옵션 입니다.
    1. 단순히 concurrentMap 으로 제어
    2. 클러스터링 을 위한 concurrentMap<->Redis 옵션화

## API

### 1. 결제 API

- (POST) /v1/payment

### 2. 데이터 조회 API

- (GET) /v1/payment/{id}

### 3. 결제 취소 API

- (DELETE) /v1/payment/{id}

## Domain

- CardNumber
- Validity
- CVC
- Installment
- Amount
- Vat
- Payment

## Table

- 테이블은 부분취소 기능 추가를 위해 결재 완료한 내역에 대해서만 관리하는 테이블을 만듭니다.

### 결재내역

- uuid
- Amount
- Vat
