const shim = require('fabric-shim');


var Chaincode = class {
    /**
     * Chaincode Instantiation method.
     * @param {Object} stub
     * @return {SuccessResponse} shim.success() returns a standard response object with status code 200 and an optional payload.
     */
    async Init(stub){
        console.info('Instantiated completed');
        return shim.success();
    }

    /**
     * Chaincode Invoking method.
     * @param {Object} stub The chaincode object
     * @return {SuccessResponse} status code and optional payload
     */
    async Invoke(stub){

       /** Get method name and parameter from the chaincode arguments */
       let ret = stub.getFunctionAndParameters();
       let method = this[ret.fcn];

       /** Undefined method calling exception(but do not throw error) */
       if(!method){
           console.log('Method name [' + ret.fcn + '] is not defined');
           return shim.success();
       }

       /** Method call */
       try{
           let payload = await method(stub, ret.params);
           return shim.success(payload);

       }catch(err){
           console.log(err);
           return shim.error(err);
       }
    }


    /**
     * Regist digital asset information
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID, args[1]: owner
     */
    async registerAsset(stub, args){

        /** Inappropriate argument exception */
        if(args.length != 2){
            throw new Error('Incorrect number of arguments. Expecting 2, but received '+ args.length);
        }

        /** !!! Generate composite key !!! */
        let compositeKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Duplicated asset checking */
        let dupCheck = await stub.getState(compositeKey);

        var isExist = function(value){
            if(value == "" || value == null || value == undefined ||
            (value != null && typeof value =="object" && !Object.keys(value).length)){
                return true;
            }

            else{
                return false;
            }
        };

        if(isExist(dupCheck) != true){
            throw new Error('AssetID ' + compositeKey + 'is already registered.');
        }

        /** Consist asset information structure */
        var assetInfo = {
          assetID: args[0],
          owner: args[1],
          createdAt: 'FALSE',
          expiredAt: 'FALSE'
        };

        /** Put the asset information */
        await stub.putState(compositeKey, Buffer.from(JSON.stringify(assetInfo)));

    }

    /**
     * Confirm timestamp which is asset registeration time.
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID
     */
    async confirmTimestamp(stub, args){ //assetID에 대한 소유권 등록 시점 기록
        /** Inappropriate argument exception */
        if(args.length != 1){
            throw new Error('Incorrect number of arguments. Expecting assetID as an argument');
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of asset information */
        let asset = await stub.getState(searchKey);
        let assetInfo = JSON.parse(asset);

        /** Get transaction timestampe using 'stub' */
        let txTimestamp = stub.getTxTimestamp();

        /** Timestamp formatting 'YYYY-MM-DD HH:MM:SS' */
        let timestampString;
        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low;
        let dataTimeObj = new Date(tsSecValue*1000);

        timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) +'-'
                        +('0'+dataTimeObj.getDate()).slice(-2)+' '+ (dataTimeObj.getHours() + 9) +':'+('0'+dataTimeObj.getMinutes()).slice(-2)+ ':'+dataTimeObj.getSeconds();


        /** Modify asset's createAt field */
        assetInfo.createdAt = timestampString;

        /** Put the modified asset information */
        await stub.putState(searchKey, Buffer.from(JSON.stringify(assetInfo)));

    }

    /**
     * Get digital asset information.
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID
     * @return {string} The asset information of assetID
     */
    async query(stub, args){

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state */
        let asset = await stub.getState(searchKey);

        /** Return asset state */
        return asset;
    }


    /**
     * Get asset's history of state
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID
     * @return {string} The history of asset
     */
    async getAssetHistory(stub, args){

        /** Inappropriate argument exception */


        /** !!! Generate composite key !!! */


        /** Get the history of state */


        /** Copy the history to array and parse to string*/


        /** Return the history as string */

    }

    /**
     * Record expiration time of current owner's ownership
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID, args[1]: current owner
     */
    async expireAssetOwnership(stub, args){

        /** Inappropriate argument exception */


        /** !!! Generate composite key !!! */


        /** Get state of the asset */


        /** Check current owner of the asset
         *  We restrict the expiration operation to the current owner
         */


        /** Get transaction timestamp using 'stub' */



        /** Timestamp formatting 'YYY-MM-DD HH:MM:SS' */


        /** Update expiredAt field */


        /** Put state  */


    }

    /**
     * Update asset's ownership and its operation time
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID, args[1]: new owner
     */
    async updateAssetOwnership(stub, args){

        /** Inappropriate argument exception */


        /** !!! Generate composite key !!! */


        /** Get state of the asset */


        /** Get transaction timestamp using 'stub' */


        /** Timestamp formatting 'YYY-MM-DD HH:MM:SS' */


        /** Update asset information
         *  owner, createdAt, expiredAt
         *  expiredAt shuld be 'FALSE'
         */


        /** Put the udpated asset information */


    }

};

/** Start chaincode */
shim.start(new Chaincode());
