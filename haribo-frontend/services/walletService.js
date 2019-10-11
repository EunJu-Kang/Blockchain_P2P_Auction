var walletService = {
    findAddressById: function(id, callback){
        $.get(API_BASE_URL + "/api/wallets/of/" + id, function(data){
            callback(data['주소']);
        });
    },
    findById: function(id, callback){
        $.ajax({
            type: "GET",
            url : API_BASE_URL + "/api/wallets/of/" + id,
            data: id,
            dataType: "json",
            success: function(response){
                callback(response);
            },
            error: function(){
            }
        });
    },
    isValidPrivateKey: function(id, privateKey, callback){
        var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
        var account = web3.eth.accounts.privateKeyToAccount(privateKey);

        this.findById(id, function(data){
            var address = data['주소'];
            callback(account && account.address == address);
        });
    },
    registerWallet: function(userId, walletAddress, callback){
        var wallet = {
          "소유자id": userId,
          "주소": walletAddress
        }

        $.ajax({
            type: "POST",
            url : API_BASE_URL+"/api/wallets",
            data: JSON.stringify(wallet),
            headers: { 'Content-Type': 'application/json' },
            success: function(response){
                callback(response);
            }
        });
    },
    chargeEther: function(walletAddress, callback){
        $.ajax({
            type: "PUT",
            url: API_BASE_URL + "/api/wallets/" + walletAddress,
            data: JSON.stringify(wallet),
            headers: { 'Content-Type': 'application/json' },
            success: function(response){
                callback(response);
            }
        });
    }
}
