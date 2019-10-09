var auctionService = {
    findAll: function(callback){
        $.get(API_BASE_URL + '/api/auctions', function(data){
            callback(data);
        });
    },
    explorerAuction: function(callback){
        $.get(API_BASE_URL + '/api/auctions/explorer', function(data){
            callback(data);
        });
    },
    highestBidder: function(bidder, callback) {
      $.get(API_BASE_URL + '/api/auctions/bidder/'+bidder, function(data){
          callback(data);
      });
    },
    homeAuction: function(callback) {
      $.get(API_BASE_URL + '/api/auctions/home/', function(data){
          callback(data);
      });
    },
    searchAuction: function(str, callback){
      $.get(API_BASE_URL + '/api/auctions/search/' + str, function(data){
          callback(data);
      });
    },
    findAllByUser: function(userId, callback){
      $.get(API_BASE_URL + '/api/auctions/owner/' + userId, function(data){
        callback(data);
      });
    },
    register: function(data, callback){
        $.ajax({
            type: "POST",
            url: API_BASE_URL + "/api/auctions",
            data: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json' },
            success: callback
        });
    },
    findById: function(id, callback){
        $.get(API_BASE_URL + "/api/auctions/" + id, callback);
    },
    saveBid: function(bidder, auctionId, bidPrice, callback){
        var data = {
            "경매참여자id": bidder,
            "경매id": auctionId,
            "입찰금액": bidPrice,
            "입찰일시": new Date()
        }
        $.ajax({
            type: "PUT",
            url: API_BASE_URL + "/api/auctions/bid",
            data: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json' },
            success: callback
        })
    },
    cancel: function(auctionId, bidderId, callback, whenError){
        $.ajax({
            type: "DELETE",
            url: API_BASE_URL + "/api/auctions/" + auctionId + "/by/" + bidderId,
            success: callback,
            error: whenError
        });
    },
    close: function(auctionId, bidderId, callback, whenError){
        $.ajax({
            type: "PUT",
            url: API_BASE_URL + "/api/auctions/" + auctionId + "/by/" + bidderId,
            success: callback,
            error: whenError
        });
    }
}
