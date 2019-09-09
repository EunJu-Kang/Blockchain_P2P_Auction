function usage {

    echo "How to use channel-ops shell script"
    echo "./channel-ops.sh [ create | join | updateanchor | list ]"

}

if [ -z $1 ];
then 
    usage
    exit 0
else
    OPERATION=$1

    if [ "list" != "$OPERATION" ] && [ -z $2 ]; 
    then
        echo "Please provide channel id"
        exit 0
    else
        CHANNEL_ID=$2
    fi

fi

# !!! Please change to your orderer address
ORDERER_ADDRESS="10.0.0.3:7050"

# !!! Please check your configuration path
# Modify these configuration when switch to other organization. 
export CORE_PEER_LOCALMSPID="copyrightAssociateOrgMSP"
export CORE_PEER_TLS_ROOTCERT=/home/copy-org/dev/crypto-config/peerOrganizations/copyrightOrg/peers/peer0.copyrightAssociateOrg/tls/ca-cert.pem
export CORE_PEER_MSPCONFIGPATH=/home/copy-org/dev/crypto-config/peerOrganizations/copyrightOrg/users/Admin@copyrightOrg/msp
export CORE_PEER_ADDRESS=10.0.0.3:7051

case $OPERATION in
    "create")
        echo "OPERATION: CREATE"

        ./fabric-samples/bin/peer channel create -o $ORDERER_ADDRESS -c $CHANNEL_ID -f $CHANNEL_ID.tx --outputBlock ~/dev/$CHANNEL_ID-genesis.block
    ;;

    "join")
        echo "OPERATION: JOIN"
        ./fabric-samples/bin/peer channel fetch config -b "./$CHANNEL_ID-genesis.block" -o $ORDERER_ADDRESS -c $CHANNEL_ID
        ./fabric-samples/bin/peer channel join -o $ORDERER_ADDRESS -b "./$CHANNEL_ID-genesis.block"
    ;;

    "updateanchor")
        echo "OPERATION: UPDATE ANCHOR"
        if [ -z $3 ];
        then
            echo "Please provide anchor peer update tx file"
            exit 0
        else
            ANCHOR_TX=$3
        fi

        ./fabric-samples/bin/peer channel join -o $ORDERER_ADDRESS -c $CHANNEL_ID -f $ANCHOR_TX
    ;;

    "list")
        echo "OPERATION: LIST"
        ./fabric-samples/bin/peer channel list
    ;;

    *)
        echo "Undefiend operation"
        usage
    ;;

esac