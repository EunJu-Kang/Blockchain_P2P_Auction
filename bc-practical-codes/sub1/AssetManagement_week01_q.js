/**
 * Hyperledger Fabric chaincode interface
 * fabric-shim(shim) class provides the service to register the chaincode with the specific peer, and
 * listen for incoming requests from the peer to dispatch to the chaincode in order to process 
 * transaction proposals or execute queries.
 */
const shim = require('fabric-shim');

var Chaincode = class {

    /**
     * Chaincode Instantiation method.
     * @param {Object} stub 
     * @return {SuccessResponse} shim.success() returns a standard response object with status code 200 and an optional payload.
     */
    async Init(stub){

    }

    /**
     * Chaincode Invoking method.
     * @param {Object} stub The chaincode object
     */
    async Invoke(stub){

        /** Get method name and parameter from the chaincode arguments */


        /** Undefined method calling exception(but do not throw error) */


        /** Method call */

    
    }

    
    /**
     * Regist digital asset information
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: owners
     */
    async registerAsset(stub, args){

        /** Inappropriate argument exception */
       
        
        /** Duplicated asset checking */
       

        /** Consist asset information structure */
        

        /** Put the asset information */
        

    }

    /**
     * Get digital asset information.
     * @param {Object} stub 
     * @param {string[]} args 
     * @return {string} The asset information of assetID
     */
    async query(stub, args){
        
      /** Get state */
      

      /** Return asset state */
      
    }

};  

/** Start chaincode */
shim.start(new Chaincode());