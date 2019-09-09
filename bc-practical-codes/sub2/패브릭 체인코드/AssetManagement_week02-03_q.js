const shim = require('fabric-shim');


var Chaincode = class {

    /**
     * Chaincode Instantiation method.
     * @param {Object} stub
     * @return {SuccessResponse} shim.success() returns a standard response object with status code 200 and an optional payload.
     */
    async Init(stub){
      return shim.success();
    }


    /**
     * Chaincode Invoking method.
     * @param {Object} stub The chaincode object
     * @return {SuccessResponse} status code and optional payload
     */
    async Invoke(stub){ //호출

       /** Get method name and parameter from the chaincode arguments */
       

       /** Undefined method calling exception(but do not throw error) */


       /** Method call */

    }


    /**
     * Regist digital asset information
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID, args[1]: owner
     */
     //2-1. 소유권 등록 함수
    async registerAsset(stub, args){

        /** Inappropriate argument exception */


        /** !!! Generate composite key !!! */


        /** Duplicated asset checking */


        /** Consist asset information structure */


        /** Put the asset information */


    }


    /**
     * Confirm timestamp which is asset registeration time.
     * @param {Object} stub
     * @param {string[]} args args[0]: assetID
     */
     //2-2. 타임스탬프 확인 함수
    async confirmTimestamp(stub, args){
        /** Inappropriate argument exception */


        /** !!! Generate composite key !!! */


        /** Get state of asset information and parse into JSON object*/


        /** Get transaction timestampe using 'stub' */


        /** Timestamp formatting 'YYYY-MM-DD HH:MM:SS' */


        /** Modify asset's createAt field */


        /** Put the modified asset information */


    }

    /**
     * Get digital asset information.
     * @param {Object} stub
     * @param {string[]} args
     * @return {string} The asset information of assetID
     */
     //2-3. 조회 함수
    async query(stub, args){

        /** !!! Generate composite key !!! */


        /** Get state */


        /** Return asset state */

    }



    async getAssetHistory(stub, args){

    }

    async expireAssetOwnership(stub, args){

    }

    async updateAssetOwnership(stub, args){

    }

};

shim.start(new Chaincode());
