function createWeb3(){
    var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
    return web3;
}


function createFactoryContract(web3){
    var auctionContract = new web3.eth.Contract(AUCTION_FACTORY_CONTRACT_ABI, AUCTION_CONTRACT_ADDRESS);
    return auctionContract;
}

function createAuctionContract(web3, contractAddress){
    var auctionContract = new web3.eth.Contract(AUCTION_CONTRACT_ABI, contractAddress);
    return auctionContract;
}

 function createAuction(options, walletAddress, privateKey, onConfirm){
   var web3 = createWeb3();
   var contract = createFactoryContract(web3);
   var auctionContract = contract.methods.createAuction(options.workId, options.minValue, options.startTime/1000, options.endTime/1000);
   var encodedABI = auctionContract.encodeABI();
   var tx = {
     from: walletAddress,
     to: AUCTION_CONTRACT_ADDRESS,
     gas: 2000000,
     data: encodedABI
   }

   web3.eth.accounts.signTransaction(tx, privateKey).then(res => {
     web3.eth.sendSignedTransaction(res.rawTransaction).then(r => {
        contract.methods.allAuctions().call().then(response => {
          onConfirm(response);
        })
     })
   })
 }

 function auction_bid(options, onConfirm){
   var web3 = createWeb3();
   var contract = createAuctionContract(web3, options.contractAddress);
   var auctionContract = contract.methods.bid();
   var encodedABI = auctionContract.encodeABI();

   const etherValue = Web3.utils.toWei(options.amount, 'ether');
   var tx = {
     from: options.walletAddress,
     to: options.contractAddress,
     gas: 3000000,
     value:etherValue,
     data: encodedABI
   }

   web3.eth.accounts.signTransaction(tx, options.privateKey).then(res => {
     web3.eth.sendSignedTransaction(res.rawTransaction).then(r => {
       onConfirm(r)
     }).catch(error => {
      console.log(error);
    })
   })
 }

function auction_close(options, onConfirm){
  var web3 = createWeb3();
  var contract = createAuctionContract(web3, options.contractAddress);
  var auctionContract = contract.methods.endAuction();
  var encodedABI = auctionContract.encodeABI();

  var tx = {
    from: options.walletAddress,
    to: options.contractAddress,
    gas: 2000000,
    data: encodedABI
  }

  web3.eth.accounts.signTransaction(tx, options.privateKey).then(res => {
    web3.eth.sendSignedTransaction(res.rawTransaction).then(r => {
      onConfirm(r)
    }).catch(error => {
      console.log(error);
    })
  })
}

function auction_cancel(options, onConfirm){
  var web3 = createWeb3();
  var contract = createAuctionContract(web3, options.contractAddress);
  var auctionContract = contract.methods.cancelAuction();
  var encodedABI = auctionContract.encodeABI();

  var tx = {
    from: options.walletAddress,
    to: options.contractAddress,
    gas: 2000000,
    data: encodedABI
  }

  web3.eth.accounts.signTransaction(tx, options.privateKey).then(res => {
    web3.eth.sendSignedTransaction(res.rawTransaction).then(r => {
      onConfirm(r)
    }).catch(error => {
      console.log(error);
    })
  })
}

function auction_info(contractAddress, onConfirm){
  var web3 = createWeb3();
  var contract = createAuctionContract(web3, contractAddress);
  var highestbid = contract.methods.highestBid();
  var highestbidder = contract.methods.highestBidder();
  var endtime = contract.methods.ended();
  var auctionEndTime = contract.methods.auctionEndTime();

  highestbid.call().then(bid =>{
    highestbidder.call().then(bidder =>{
      endtime.call().then(endtime =>{
        auctionEndTime.call().then(auctionEndTime =>{
          onConfirm(bid, bidder, endtime, auctionEndTime)
        })
      })
    })
  }).catch(e =>{
    bid = null
    bidder = null
    endtime = null
    auctionEndTime = null
    onConfirm(bid, bidder, endtime, auctionEndTime)
  })
}
