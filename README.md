# 이미지 저작권 P2P 경매 사이트

윈도우 XP의 공식 배경화면으로 유명한 언덕사진을 찍은 사진작가는 세계에서 2번째로 많은 저작권료를 받고 있습니다. 핸드폰으로 사진작가 못지 않은 퀄리티를 내는 요즘, 찍은 사진들의 저작권을 쉽게 사고팔면 어떨까? 하는 생각을 바탕으로 저작권을 쉽고 안전하게 거래 할 수 있는 사이트를 개발하였습니다.
<br>
<br>
>클릭하면 YouTube로 넘어갑니다.
>
[![예제](http://img.youtube.com/vi/p6_8xIwxEEc/0.jpg)](https://youtu.be/p6_8xIwxEEc?t=0s) 

### 블록체인 
> 신뢰를 만드는 기술
>
블록체인은 이전 블록에 대한 해시를 다음 블록에 기입해 계속 이어지도록 체인을 만드는 기술이다. 이전 블록에서 거래 내역을 수정하거나 조작하면 일단 해당 해시가 변경된다. 그렇기 때문에 조작한 내역을 숨기려면 이전블록 뿐만 아니라 이전블록 앞의 연결된 모든 블록들을 변경해야만 한다. 따라서 악의를 가지고 데이터를 마음대로 바꾸는 것은 불가능하다. 이러한 블록체인의 특징을 이용해서 이미지에 대한 저작권을 블록체인에 기록하여 신뢰성을 가진 정보를 제공할 수 있다.

### 블록체인 활용
#### 1. Public BlockChain (이더리움)
<u>`경매기능`</u>은 이더리움 `스마트 컨트랙트`를 통해 직접 진행됩니다. 이때 이더리움 네트워크 상에 작성한 스마트 컨트랙트가 사용되며 `이더리움 네트워크`의 통화인 이더(Eth)가 매개가 됩니다.<br>

* Remix에서 Solidity 언어로 작성한 [smartContract].sol 파일을 컴파일한다.
* ABI, binaryCode, address를 가지고 wrapper class로 변환 하면 java로 사용 가능
* 얻은 ABI 및 address로 haribo-frontend/components/constants.js에서 초기화
* 스마트컨트랙트를 wrapper class로 변환한 파일을 가지고 Spring boot에서 사용
* [wrapper class 변환](https://web3j.readthedocs.io/en/latest/command_line_tools.html) (변환 시 web3j 파일이 필요하다면 docs/web3j에 포함)
* [wrapper class 사용시 이해](https://medium.com/day34/klaytn-caver-java-3-%EC%8A%A4%EB%A7%88%ED%8A%B8-%EC%BB%A8%ED%8A%B8%EB%9E%99%ED%8A%B8-%EB%B0%B0%ED%8F%AC%EC%99%80-%EC%8B%A4%ED%96%89-19630316d36)

##### [> 스마트컨트랙트 확인하기](https://lab.ssafy.com/EunJu/blockchin_final/blob/master/docs/contract/smartcontract/AuctionFactory.sol)


#### 2. Private BlockChain (Hyperledger Fabric)
이미지의 <u>`저작권`</u>은 프라이빗 혹은 허가형 블록체인의 대표인 `하이퍼레저 패브릭`에 기록하는 기능을 지닙니다. 시스템을 통한 작품 등록, 삭제, 경매 완료 등 소유권에 변경이 발생하는 이벤트가 있을 때 [체인코드](https://miiingo.tistory.com/105?category=644184)를 호출하여 소유권 이력을 기록하게 합니다. 
* [체인코드를 Fabric-sdk-java를 이용해서 채널 연동 및 사용법 (1)](https://medium.com/@lkolisko/hyperledger-fabric-sdk-java-basics-tutorial-a67b2b898410)
* [체인코드를 Fabric-sdk-java를 이용해서 채널 연동 및 사용법 (2)](https://cyberx.tistory.com/191)
* haribo-frontend/components/auctionFactory.js

##### [> 체인코드 확인하기](https://lab.ssafy.com/EunJu/blockchin_final/blob/master/docs/contract/chaincode/chaincode.md)


### 네트워크 구성
<img width="600px" src="./docs/assets/network.PNG"/>

> - haribo-backend/src/main/resources/application.properties 에 환경설정 및 변수들이 초기화 되어있다.
> - haribo-frontend/component/constants.js 에서 URL 수정
> - docs/SETTING 에 Front-End, Back-End, DB, Ethereum, Hyperledger Fabric 설정 관련 파일 포함.


### 개발환경


| 구분 | 기술 | 버전 | 요약 |
|---|:---:|---:|---:|
| Blockchain | Ethereum | Geth 1.8.27-stable | public blockchain |
|  | Hyperledger Fabric | | private blockchain | |
| Chaincode | Node.js | 8.x | |
| Smart Contract | Solidity | 0.4.22 이상 0.6.0 이하  | |
| Frontend-Blockchain | web3.js | 0.2x.x |Ethereum Javascript API |
| Backend-Blockchain | web3.j | web3j-spring-boot-starter 1.6.0, core 4.2.0 | Ethereum Java API |
| | Fabric-sdk-java | 1.4.0 | Fabric Java API |
| Frontend | Vue.js |  |  |
|  | Javascript |  |  |
| Backend | Java | 1.8 | OOP Language |
|  | Spring boot | 5.0 + | Java Framework |
|  | MySQL | 6.0.0 | RDBMS |
|  | maven | 4.0.0 | Java Project Build Tool |
| server | AWS |  | ubuntu |

### [API Reference for Developers(Swagger)](https://lab.ssafy.com/EunJu/blockchin_final/tree/master/docs/assets/api/BlockChain)
#### 1. Auction
<img width="700px" src="./docs/assets/api/BlockChain/Auction/Auction.png"/>

#### 2. Digital_work
<img width="700px" src="./docs/assets/api/BlockChain/Digital_work/Digital_work.png"/>

#### 3. Ethereum
<img width="700px" src="./docs/assets/api/BlockChain/Ethereum/Ethereum.png"/>

#### 4. Member
<img width="700px" src="./docs/assets/api/BlockChain/Member/Member.png"/>

#### 5. Wallet
<img width="700px" src="./docs/assets/api/BlockChain/Wallet/Wallet.png"/>