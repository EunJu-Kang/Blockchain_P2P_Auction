var etheriumService = {
  recentBlock : function(callback){
    $.get(API_BASE_URL + "/api/eth/blocks/", function(data){
      callback(data)
    })
  },
  recentTrans : function(callback){
    $.get(API_BASE_URL + "/api/eth/trans/", function(data){
      callback(data)
    })
  },
  timeSince : function(date) {
    var seconds = Math.floor((new Date() - Date.parse(date)) / 1000);
    var interval = Math.floor(seconds / 31536000);
    if (interval > 1) {
      return interval + " years ago";
    }
    interval = Math.floor(seconds / 2592000);
    if (interval > 1) {
      return interval + " months ago";
    }
    interval = Math.floor(seconds / 86400);
    if (interval > 1) {
      return interval + " days ago";
    }
    interval = Math.floor(seconds / 3600);
    if (interval > 1) {
      return interval + " hours ago";
    }
    interval = Math.floor(seconds / 60);
    if (interval > 1) {
      return interval + " minutes ago";
    }
    return Math.floor(seconds) + " seconds ago";
  },
  findBlockById : function(id, callback){
    $.get(API_BASE_URL + "/api/eth/blocks/" + id, function(data){
      callback(data)
    })
  },
  findTranById : function(id, callback){
    $.get(API_BASE_URL + "/api/eth/trans/" + id, function(data){
      console.log(data)
      callback(data)
    })
  },

  findTranByAddress :  function(address, callback){
    $.get(API_BASE_URL + "/api/eth/tranaddress/" +address, function(data){
      console.log(data);
      callback(data)
    })
  },

  findAuction : function(callback){
    $.get(API_BASE_URL + "/api/eth/auctions", function(data){
      callback(data)
    })
  }
}
