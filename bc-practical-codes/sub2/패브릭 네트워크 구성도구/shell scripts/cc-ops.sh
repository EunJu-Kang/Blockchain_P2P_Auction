function helper {
    echo "How to use cc-ops shell script"
    echo "[install]"
    echo "./cc-ops.sh install [chaincode name] [chaincode version]"
    echo ""
    echo "[pckginstall]"
    echo "./cc-ops.sh install [chaincode package]"
    echo ""
    echo "[instantiate]"
    echo "./cc-ops.sh instantiate [chaincode name] [channel id] [chaincode version] [chaincode args]"
    echo ""
    echo "[query]"
    echo "./cc-ops.sh query [chaincode name] [channel id] [chaincode args]"
    echo ""
    echo "[package]"
    echo "./cc-ops.sh package [chaincode name] [chaincode version]"
    echo ""
    echo "[upgrade]"
    echo "./cc-ops.sh upgrade [chaincode name] [channel id] [chaincode version] [chaincode args]"
    echo ""
    echo "[installed | instantiated]"
    echo "./cc-ops.sh installed [channel id]"
    echo "./cc-ops.sh instantiated [channel id]"
    echo ""
}


if [ -z $1 ];
then
    helper
    exit 0
else
    OPERATION=$1

    if [ "installed" != "$OPERATION" ] && [ "instantiated"  != "$OPERATION" ];
    then
        if [ -z $2 ];
        then
            echo "Please provide chaincode name"
            exit 0
        else
            CHAINCODE_NAME=$2
        fi
    fi
fi

# Default orderer address
ORDERER_ADDRESS="10.0.0.3:7050"

# copy-org
export CORE_PEER_LOCALMSPID="copyrightAssociateOrgMSP"
export CORE_PEER_MSPCONFIGPATH=/home/copy-org/dev/crypto-config/peerOrganizations/copyrightOrg/users/Admin@copyrightOrg/msp
export CORE_PEER_ROOTCERT_FILE=/home/copy-org/dev/crypto-config/peerOrganizations/copyrightOrg/peers/peer0.copyrightAssociateOrg/tls/ca-cert.pem
export CORE_PEER_ADDRESS="10.0.0.3:7051"

case $OPERATION in
    "install")
        echo "OPERATION: INSTALL"
        if [ -z $3 ];
        then   
            echo "Please provide chaincode version"
            exit 0
        else 
            CHAINCODE_VERSION=$3
        fi

        ./fabric-samples/bin/peer chaincode install -n $CHAINCODE_NAME -v $CHAINCODE_VERSION -l node -p src/chaincode/nodejs/
    ;;

    "pckginstall")
        echo "OPERATION: PACKAGE INSTALL"
        if [ -z $3 ];
        then
            echo "Please provide chaincode package name"
            exit 0
        else
            CHAINCODE_PKGNAME=$3
        fi

        ./fabric-samples/bin/peer chaincode install $CHAINCODE_PKGNAME
    ;;

    "instantiate")
        echo "OPERATION: INSTANTIATE"
        if [ -z $3 ];
        then
            echo "Please provide channel name"
            exit 0
        else
            CHANNEL_ID=$3
        fi

        if [ -z $4 ];
        then 
            echo "Please provide chaincode version"
            exit 0
        else
            CHAINCODE_VERSION=$4
        fi

        if [ -z $5 ];
        then
            echo "Please provide chaincode arguments"
            exit 0
        else
            CHAINCODE_ARGS=$5
        fi
        
        ./fabric-samples/bin/peer chaincode instantiate -o $ORDERER_ADDRESS -n $CHAINCODE_NAME -v $CHAINCODE_VERSION -c $CHAINCODE_ARGS -P "OR('copyrightAssociateOrgMSP.member','digitalAuthorOrgMSP.member')"

    ;;

    "query")
        echo "OPERATION: QUERY"
        if [ -z $3 ];
        then
            echo "Please provide channel id"
            exit 0
        else
            CHANNEL_ID=$3
        fi

        if [ -z $4 ];
        then
            exit 0
        else   
            CHAINCODE_ARGS=$4
        fi

        ./fabric-samples/bin/peer chaincode query -C $CHANNEL_ID -n $CHAINCODE_NAME -c $CHAINCODE_ARGS
        
    ;;

    "invoke")
        echo "OPERATION: INVOKE"
        if [ -z $3 ];
        then
            echo "Please provide channel id"
            exit 0
        else
            CHANNEL_ID=$3
        fi

        if [ -z $4 ];
        then
            echo "Please provide chaincode arguments"
            exit 0
        else   
            CHAINCODE_ARGS=$4
        fi

        ./fabric-samples/bin/peer chaincode invoke -o $ORDERER_ADDRESS -C $CHANNEL_ID -n $CHAINCODE_NAME -c $CHAINCODE_ARGS
    ;;

    "package")
        echo "OPERATION: PACKAGE"
        if [ -z $3 ];
        then
            echo "Please provide chaincode version"
            exit 0
        else
            CHAINCODE_VERSION=$3
        fi
        ./fabric-samples/bin/peer chaincode package ccpack.out -n $CHAINCODE_NAME -l node -p src/chaincode/nodejs/ -v $CHAINCODE_VERSION
        
    ;;

    "upgrade")
        echo "OPERATION: UPGRADE"
        if [ -z $3 ];
        then
            echo "Please provide channel id"
            exit 0
        else
            CHANNEL_ID=$3
        fi

        if [ -z $4 ];
        then 
            echo "Please provide chaincode version"
            exit 0
        else
            CHAINCODE_VERSION=$4
        fi

        if [ -z $5 ];
        then
            echo "Please provide chaincode arguments"
            exit 0
        else
            CHAINCODE_ARGS=$5
        fi

        ./fabric-samples/bin/peer chaincode upgrade -o $ORDERER_ADDRESS -C $CHANNEL_ID -n $CHAINCODE_NAME -v $CHAINCODE_VERSION -c $CHAINCODE_ARGS -P "OR('copyrightAssociateOrgMSP.member','digitalAuthorOrgMSP.member')"         
    ;;

    "installed")
        echo "OPERATION: INSTALLED(list)"
        if [ -z $2 ];
        then
            echo "Please provide channel id"
            exit 0
        else
            CHANNEL_ID=$2
        fi

        ./fabric-samples/bin/peer chaincode list --installed -C $CHANNEL_ID
    ;;

    "instantiated")
        echo "OPERATION: INSTANTIATED(list)"
        if [ -z $2 ];
        then
            echo "Please provide channel id"
            exit 0
        else
            CHANNEL_ID=$2
        fi

        ./fabric-samples/bin/peer chaincode list --instantiated -C $CHANNEL_ID
    ;;

    "help")
        helper
    ;;

    *) 
        echo "Undefined operation"
        helper     
    ;;

esac
