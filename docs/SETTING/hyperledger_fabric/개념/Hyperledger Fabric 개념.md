# Hyperledger Fabric

## 01. Fabric 구조

 

#### 비트, 이더와 같은 점과 다른점

- 비트 , 이더

  - 많이 쓰는 퍼블릭 블록체인
  - 프로그램 하나가 모든걸 처리한다
  - 블록생성자 = 스마트 컨트랙트
  - 개인키 기반

- ##### Fabric

  - 역할을 나누어서 처리
  - 블록생성자 != 스마트 컨트랙트
  - Orderer는 블록생성만
  - Peer는 시뮬레이팅, 블록체인 보관
  - 인증서 기반으로 상호작용
    - Fabric ca라는 중간 관리자가 있음
  - 하나의 키 값을 한 블록에서 여러 번 바꿀 수가 없다(일반적으로 생각하는 방식으로)

<br>

#### 구성요소

1. ##### Peer

   - 블록체인보관
   - 트랜잭션을 시뮬레이팅하는 역할

2. ##### Orderer

   - 블록을 생성
   - 트랜잭션을 모아서 블록을 생성만 한다(시뮬레이팅 하지 않음)

3. ##### App(유저)

   - 인증서, 지갑을 가지고 거래를 만들고 Peer와  Orderer와 상호작용

- 위처럼 Peer와 Orderer가 나누어져 있기 때문에 일반적인 스마트컨트랙트와 로직이 달라짐

![2](C:\Users\multicampus\Desktop\바탕화면\fabric\2.png)

- 실제로 Orderer는 Ledger를 가지고 있지만 이해를 쉽게 하기 위해 없다고 가정

##### 트랜잭션 플로우

- App(유저)이 거래를 만들어 Peer에 보내면(proposals) 블록에 들어가는게 아니라 시뮬레이팅을 하게 됨
  - 이 때 거래를 승인하게되면 시뮬레이팅을 하게되고
  - ok사인을 App(유저)에게 보냄
  - 이 ok사인이 패브릭에서의 합의 방식
    - 합의방식 : `BFT`
    - 3명중 2명이 투표를 하면 됨
  - 위처럼 App이 peer들에게 허락을 받는걸 `endorsement policies`라고 한다
- 합의를 통해 검증을 밭은 트랜잭션은 Orderer에게 블록에 넣어달라고 함(proposal package)
- Orderer는 거래들을 쭈욱 모아서 peer들에게 허락을 받았는지만 검증, 스마트컨트랙트가 어떤 내용으로 실행되었는지, 어떤 내용이 있는지는 검증하지 않는다
- 검증이 끝난 블록은 Peer들에게 전달(Block 전파)
- Peer는 블록을 받아서 블록체인에 연결하고 자신의 world state를 업데이트하게 된다
- 비트, 이더에서는 하나의 노드가 거래를 모으고, 블록도 만들고 모든 일을 다하는데
- Fabric은 Peer와 Orderer로 나뉘어져 일하게 됨

<br>

#### Ledger

1. 블록체인
   - 블록과 거래가 들어있는
2. World state(Peer)
   - 자신의 최신 상태들을 보관
   - Peer만 보관한다
3. Peer만 들고 있음(활용)
   - Orderer가 들고있는 Ledger는 배포용

<br>

- 패브릭 아키텍처 플로우

![1](C:\Users\multicampus\Desktop\바탕화면\fabric\1.png)

- 플로우 다이어그램으로 살펴보기

![3](C:\Users\multicampus\Desktop\바탕화면\fabric\3.png)





## 02. Fabric Read&Write set

- 패브릭에서 체인코드가 어떤 식으로 읽고 쓰는지를 알아보자
- 체인코드 작성하고, 체인코드 구조를 설계하기 위해
- TPS가 높은 체인코드를 설계하기 위해

<br>

#### Fabric

- 체인코드가 값을 쓰는 방식

  - KVS(Key-Value store)
  - Level Db, couch DB

- 체인코드 안에서 사용

  - ```
    - a: 100
    - b: hello
    ```

  - 한 블록 내에서 하나의 키 값을 여러번 읽어서 바꿔 쓸 수 없다

  > Further, if the transaction writes a value multiple times for a key, only the last written value is retained. Also, if a transaction reads a value for a key, the value in the committed state is returned even if the transaction has updated the value for the key before issuing the read. In another words, `Read-your-writes semantics are not supported.`

##### Read set, Write set

- 한 번 write한 키에 대해서는 Read를 하면 실패한다.

```
World state: (k1,1,v1), (k2,1,v2), (k3,1,v3), (k4,1,v4), (k5,1,v5)

# 아래의 트랜잭션들은 모두 한 블록에 들어감
T1 -> Write(k1, v1'), Write(k2, v2')
T2 -> Read(k1), Write(k3, v3')
T3 -> Write(k2, v2'')
T4 -> Write(k2, v2'''), read(k2)
T5 -> Write(k6, v6'), read(k5)
```

1. `T1`은 k1에다가 v1'이라는 값을쓰고, k2에 v2'라는 값을 씀, 간단히 쓰는 작업
   - `T1`트랜잭션은 성공
2. `T2`는 k1은 읽고, k3에 v3라는 값을 씀
   - 하지만 이미 k1은 위에서 Write를 했기 때문에 `T2`트랜잭션은 실패
3. `T3` 는 k2에 v2''를 씀
   - `T3`은 읽지 않고 k2를 v2'' 쓰기 때문에 성공
4. `T4` 는 k2에 v2'''를 쓰고 k2를 읽음
   - k2는 이미 위에서 사용했기 때문에 읽을수 없다. 실패
5. `T5` 는 k6에 v6'를 쓰고, k5를 읽음
   - 위에서 k5를 사용하지 않았기 때문에 성공



#### Fabric - high through put network

- 태깅을 사용해서 

```
- A : 100(이전 블록)
# 이름이 A1, A2, A3로 다르기 떄문에 가능
- A1 : +100
- A2 : -50
- A3 : +100
- A : 250
```







- [hyperledger-fabric.readthedocs](https://hyperledger-fabric.readthedocs.io/){:}



## 03. Fabric Study Guide

##### hyperledger-fabric readthedoc

- Tutorials- BYFN(Building Your First Network)
- Operation Guide - 네트워크 설정, 노드 운영 설명

##### Fabric-samples

- github 예제
- 다 보면 좋음
- first-network - balance transfer - hight-throughput - unit test

<br>

#### Hyperledger Tools

- hyperledger project
  - explorer
  - cello
  - caliper
  - composer
- kubernetes



## 04. Fabric 네트워크 세팅

 ![1](C:\Users\multicampus\Desktop\바탕화면\fabric\4\1.png)

```
A = App(유저)
L = Ledger
S = ChainCode
CA = CA
CC = Channel configuration
NC = Network Config
R = Organization
C = Channel
```

- 패브릭에서의 권한 관리
  - 인증서를 기반으로
  - `X.509`를 만들고 관리하고 삭제하는 것들을`CA`라는 component로 제공
- Fabric CA(Certificate Authority)를 통해서 MSP(Membership Service Provider)
- Channel 
  - Peer들이 Channel에 가입할 수 있다.
  - 같은 Channel안에 있는 Peer들은 해당 Channel에 있는 스마트컨트랙트, 블록체인 데이터를 공유할 수 있다.
  - 이를 위해서는 같은 스마트컨트랙트가 있어야 한다.
- Organization
  - identity를 관리하는 하나의 group
  - 네트워크를 구성할 때 organizaion 단위로

<br>

#### 네트워크 구성

1. 크립토 관련 작업 
   - Fabric-ca
   - cryptogen
     - 크립토관련 인증서, 프라이빗 키 등등을 생성해주는 실행파일을 fabric에서 제공
2. Orderer 띄움
3. Peer를 띄움
4. 네트워크에서 Kafka, zookeeper를 띄워줌
5. Channel을 생성
   - Channel에 Peer를 조인시킴
     - 이를 가능하게 하는것은 크립토 관련 작업을 위에서 해주었기 때문
6. 스마트컨트랙트를 만들고 배포(Chaincode)
7. Ledger 생성
8. App(유저) 생성
   - 자신의 인증서를 사전에 발급받고, 해당 peer를 통해 소통할 수 있게 됨



## 05. Fabric Identity

<br>

#### Identity

- PKI(공개키 기반, Public Key Infrastructure) 를 기반으로 하는 인증서 기반 시스템
- orderer, peer, app들은 각각 자신의 개인키, 공개키, 인증서들이 있어서 각각 검증, 서명

<br>

#### PKI(공개키 기반, Public Key Infrastructure)

- 공개키 암호화
  - 개인키 + 공개키
  - 개인키
    - 소유자가 가지고 비밀로 보관해야하는 키
    - 데이터에 서명
    - 공개키로 암호화한 데이터를 복호화 할 수 있다.
  - 공개키 
    - 다른 사람에게 알려주는 용도
    - 개인키로 암호화한 데이터를 복호화 할 수 있다.

<br>

##### 전자서명

- 데이터가 위/변조 되지 않았다 라는 것을 PKI 시스템에서 알려주기 위한 용도

```
1. 원본의 digest(원본 해쉬)를 만든다
2. 개인키로 digest를 서명한 후에 원본에 붙인다
3. 붙인걸 상대방에게 넘겨준다
4. 상대방은 개인키로 암호화 되어있기 때문에 원본의 digest를 만든다
5. 그 후 암호화 된 digest(넘겨받은)를 자신의 공개키로 복호화를 한다
6. 복호화한 digest와 넘겨받은 원본의 digest를 비교해서 검증한다
```

<br>

##### X.509 (인증서 포맷)

- 공개키 데이터를 포함하고 있다.
- 발행자가 누구인지, 내가 누구인지, 도메인이 누구인지가 포함된
- root ca에 대한 정보

<br>

#### Fabric 인증서

- 인증서 위치 확인
- crypto-config
  - cryptogen을 이용해서 yaml파일을 수정해 crypto관련 material들을 만들게 된다
- cert.pem파일 내용을 복사에 `Certificate decoder`로 확인 가능

<br>

#### MSP(Membership Service Provider)

- 패브릭내에서 노드/유저/채널들간의 인증,권한을 관리해주는 기본적인 개념 / 서비스
- X.509 인증서를 기반으로한 PKI시스템으로 구현이 되어 있다.
-  노드(peer, orderer, user) : directory(msp)안에 있는 인증서
- 채널 / 네트워크 : 다른 여러가지 방법을 통한 멤버쉽 관리

<br>

#### Block Structure

![1](C:\Users\multicampus\Desktop\바탕화면\fabric\5\1.png)

- ##### block header // transaction // block metadata



## 06. Fabric Channel

- 패브릭이 private 블록체인으로써 public 블록체인과 어떻게 다른가를 가장 잘 설명해주는 개념
- 특정한 노드들끼리의 private함을 유지해준다

<br>

#### Channel

- 패브릭 네트워크 멤버들 사이의 서브넷(하위 네트워크)
  - 특정 멤버들(피어, 유저, organization) 끼리만 블록체인과 스마트 컨트랙트릉 공유할 수 있다.
  - 다른 채널의 데이터는 접근 할 수 없다.
    - PeerA가 채널 가, 나 둘 다 가입되어 있으면 PeerA는 둘 다 접근이 가능
    - 멀티채널에 peer가 등록되어 있으면 둘다 접근이 가능하다는 소리
- MSP, identity에 의해서 식별될 수 있다.
- 채널이 시작될 때 : 채널별로 블록체인이 따로 존재한다
  - 채널을 만들 때 genesis block에 채널의 정보가 있는 트랜잭션으로 시작한다.
- 채널에서 피어들이 블록을 공유할 때
  - 리더 피어 
    - Orderer와 연결해서 블록을 가져오는 피어(가장먼저)
    - 리더피어가 나머지 피어로 전달, 일괄적으로 피어들이 같은 블락을 가지게 되고 각자 validating하게된다
  - 앵커링 피어
    - 네트워크 관련 정보를 공유한다. 
- 같은 채널안의 체인코드들은 서로 이름, 버전들을 알고 있으면 호출이 가능
  - 서로 읽고 쓰기가 가능
- 서로 다른 채널의 체인코드들은 서로 읽을수는 있지만 값을 변화시킬 수는 없다
  - Ledger별로 블록이 따로 생성되기 때문에 쓰기를 했을 때에는 블록 자체의 단위가 애매해지기 때문에 쓰기를 막아놓은 것처럼 보인다

 <br>![1](C:\Users\multicampus\Desktop\바탕화면\fabric\1.png)

- 피어가 자기 가입된 채널에 속한 블록을 차례대로 쌓는 모습 





## 07. Fabric Gossip Protocol

- 노드 / 피어들이 서로 블록을 어떻게 주고 받느냐에 대한 내용
- 비트, 이더같은 public은 mining을 하면서 정해진 규칙을 검증
- Fabric은 비슷하지만 private이고,  identity기능이 있기 때문에 자기들의 만들어 놓은 인증서를 기반으로 소통을 한다

<br>

#### Peer의 역할 종류

- 커미터
  - 모든 peer는 커미터
  - 블록체인(레져)를 가지고 있고, excute, validate 하는 역할
- 앵커
  - 자신이 속한 organization의 네트워크 정보를 가지고 있고 이것을 전달해주는 역할
  - 한 organization에 여러 앵커를 가질수도 있지만, default로 하나의 앵커 peer는 설정하게 되어있다.
  - 다른 organization, 다른 채널에서 fabric에 접글 했을 때 앵커피어에 접근을 하게되면 해당 organization이나 이 앵커피어가 가지고 있는 다른 네트워크에 대한 정보들도 전달받을 수 있다. 
  - 네트워크 정보의 관점에서 정보를 가지고 있다.
- 리더
  - Orderer로부터 블록을 받아온다.
  - 리더피어 자신이 특정 시점에 받아올 수도 있다.
  - 피어들 줄 한 피어가 리더가 되는데, 네트워크 admin이 지정해줄 수도있고, 자기들끼리 선택할 수도 있다.

<br>

#### 피어

- (cli를 이용해서)채널을 만들 때 Orderer옵션을 통해 알리게 된다.
- 앵커 피어
  - organization의 앵커피어는 누구다 라고 앵커피어 트랜잭션을 통해서 알리게 된다. 

- 부트스트랩 피어를 설정

