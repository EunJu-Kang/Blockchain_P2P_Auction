# 실행순서: configtxgen 파일생성 --> docker-compose 구동 및 확인 --> channel create --> channel join --> create anchor peer update tx --> update anchor peer --> chaincode install --> chaincode instantiate

# configtxgen 파일생성
# configtxgen --> 채널 구성이나, 업데이트되는(피어가 채널에 참여라던지, 연결정보 변경한다던지) 시에 필요한 정보 파일(~.tx 이나 ~.block 등) 생성
./configtxgen -profile AuctionOrdererGenesis -outputBlock orderer-genesis.block -channelID ordererchannel001
./configtxgen -profile AuctionChannel -outputCreateChannelTx auctionchannel001.tx -channelID auctionchannel001
# 생성된 파일을 이동시킴(auctionchannel001.tx만 이동하며, orderer-genesis.block은 이동시키지 않음)
# 이후에 노드파일이 사용함
mv auctionchannel001.tx ./channel-artifacts


# 4개의 도커 컴포즈 파일을 순서대로 구동시킨다(zookeeper-compose.yaml -> kafka-compose.yaml -> orderer-compose.yaml -> peer-compose.yaml)
# zookeeper 실행
sudo docker-compose -f zookeeper-compose.yaml up -d

# check zookeeper000
nc -z 0.0.0.0 2181
if [ "$?" != 0 ]; then
    echo 'zookeeper000 run fail. Please check docker log.'
else
    echo 'zookeeper000 is active.'
fi

# check zookeeper001
nc -z 0.0.0.0 2182
if [ "$?" != 0 ]; then
    echo 'zookeeper001 run fail. Please check docker log.'
else
    echo 'zookeeper001 is active.'
fi

# check zookeeper002
nc -z 0.0.0.0 2183
if [ "$?" != 0 ]; then
    echo 'zookeeper002 run fail. Please check docker log.'
else
    echo 'zookeeper002 is active.'
fi

# kafka 실행 (kafka는 "docker logs kafka000" 명령어로 로그 확인 필요)
# kafka 로그 상에서 [KafkaServer id=1] started 라는 로그 출력 시 정상 동작(id 값은 kafka별로 상이)
sudo docker-compose -f kafka-compose.yaml up -d

# orderer 실행
sudo docker-compose -f orderer-compose.yaml up

# check orderer001
nc -z 0.0.0.0 8050
if [ "$?" != 0 ]; then
    echo 'orderer001 run fail. Please check docker log.'
else
    echo 'orderer001 is active.'
fi

# check orderer002
nc -z 0.0.0.0 7060
if [ "$?" != 0 ]; then
    echo 'orderer002 run fail. Please check docker log.'
else
    echo 'orderer002 is active.'
fi

# check orderer003
nc -z 0.0.0.0 7070
if [ "$?" != 0 ]; then
    echo 'orderer003 run fail. Please check docker log.'
else
    echo 'orderer003 is active.'
fi


# peer 실행
sudo docker-compose -f peer-compose.yaml up -d

# check peer0.copyrightOrg
nc -z 0.0.0.0 7071
if [ "$?" != 0 ]; then
    echo 'peer0.copyrightOrg run fail. Please check docker log.'
else
    echo 'peer0.copyrightOrg is active.'
fi

# check peer0.digitalAuthorOrg
nc -z 0.0.0.0 8051
if [ "$?" != 0 ]; then
    echo 'peer0.digitalAuthorOrg run fail. Please check docker log.'
else
    echo 'peer0.digitalAuthorOrg is active.'
fi


# channel create
# 피어들끼리 통신할 채널 생성
# peer channel create -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/fabric/production/auctionchannel001.tx --outputBlock /var/hyperledger/production/auctionchannel001-genesis.block
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel create -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/production/auctionchannel001.tx --outputBlock /var/hyperledger/production/auctionchannel001-genesis.block

# channel join
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel fetch config /var/hyperledger/production/auctionchannel001-genesis.block -o orderer001:7050 -c auctionchannel001
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel join -o orderer001:7050 -b /var/hyperledger/production/auctionchannel001-genesis.block
docker exec -e "CORE_PEER_LOCALMSPIP=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer channel fetch config /var/hyperledger/production/auctionchannel001-genesis.block -o orderer001:7050 -c auctionchannel001
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer channel join -o orderer001:7050 -b /var/hyperledger/production/auctionchannel001-genesis.block


# create anchor peer update tx
./configtxgen -profile AuctionChannel -outputAnchorPeersUpdate copyrightOrgAnchor.tx -channelID auctionchannel001 -asOrg copyrightAssociateOrgMSP
./configtxgen -profile AuctionChannel -outputAnchorPeersUpdate digitalAuthorOrgAnchor.tx -channelID auctionchannel001 -asOrg digitalAuthorOrgMSP

mv copyrightOrgAnchor.tx ./channel-artifacts
mv digitalAuthorOrgAnchor.tx ./channel-artifacts

# update anchor peer
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel update -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/production/copyrightOrgAnchor.tx
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer channel update -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/production/digitalAuthorOrgAnchor.tx


# ===========
# 체인코드
# ===========
# nodejs chaincode install
# 구동 확인을 위해 스켈레톤 체인코드가 아닌 완성본을 설치
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer chaincode install -n asset -v 0.1 -l node -p /var/hyperledger/chaincode/node
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode install -n asset -v 0.1 -l node -p /var/hyperledger/chaincode/node

# nodejs chaincode instantiate
# 체인코드 도커 컨테이너를 생성 및 구동
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode instantiate -o orderer001:7050 -C auctionchannel001 -n asset -v 0.1 -c '{"Args":["init"]}' -P "OR('copyrightAssociateOrgMSP.member','digitalAuthorOrgMSP.member')"

# nodejs chaincode invoke
# 생성된 체인코드 동작 테스트
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode invoke -o orderer001:7050 -C auctionchannel001 -n asset -c '{"Args":["registerAsset","testasset","testowner"]}'

# nodejs chaincode query
# 생성된 체인코드에 쿼리를 날려 invoke 시 등록한 정보가 반환되면 정상동작
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode query -o orderer001:7050 -C auctionchannel001 -n asset -c '{"Args":["query","0"]}'

docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode invoke -o orderer001:7050 -C auctionchannel001 -n asset -c '{"Args":["confirmTimestamp","209"]}'
# nodejs chaincode package
# 체인코드 수정 후 재배포를 위한 패키지 생성 명령어
# -v 플래그에 버전을 기록
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode package /var/hyperledger/chaincode/ccpack.out -n asset -v 0.2 -l node -p /var/hyperledger/chaincode/node

# nodejs chaincode package install
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer chaincode install /var/hyperledger/chaincode/ccpack.out -p /var/hyperledger/chaincode/node
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode install /var/hyperledger/chaincode/ccpack.out -p /var/hyperledger/chaincode/node

# nodejs chaincode upgrade
# 체인코드 수정 후 설치된 버전으로 업그레이드를 수행하는 명령어
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode upgrade -o orderer001:7050 -C auctionchannel001 -n asset -v 0.4 -c '{"Args":["init"]}' -P "OR('copyrightAssociateOrgMSP.member','digitalAuthorOrgMSP.member')"
