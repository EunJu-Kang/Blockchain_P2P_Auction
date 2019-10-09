
const NUMBER_OF_CONTENTS_TO_SHOW = 10; 
const REFRESH_TIMES_OF_OVERVIEW = 1000; 
const REFRESH_TIMES_OF_BLOCKS = 5000; 
const REFRESH_TIMES_OF_TRANSACTIONS = 15000; 

const web3 = new Web3(BLOCKCHAIN_URL);

function fetchLatestBlock() {
  return web3.eth.getBlockNumber();
}

function parseQueryString() {
  var values = [],
    item;
  var sliced = window.location.href.slice(
    window.location.href.indexOf("?") + 1
  );
  sliced = sliced.split("&");

  for (var i = 0; i < sliced.length; i++) {
    item = sliced[i].split("=");
    values.push(item[0]);
    values[item[0]] = item[1];
  }
  return values;
}

function fetchBlocks(from, end, callback) {
  web3.eth.getBlock(from).then(function(block) {
    callback(block);

    var f = (from += 1);
    if (f <= end) {
      fetchBlocks(f, end, callback);
    }
  });
}

function timeSince(date) {
  var seconds = Math.floor((new Date() - date * 1000) / 1000);

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
}
