// // 웹서버 API URL을 지정합니다.
// API_BASE_URL = "http://13.125.178.26:8089";

API_BASE_URL = "http://localhost:8080";
// 배포한 옥션 컨트랙트 주소를 지정합니다.
AUCTION_CONTRACT_ADDRESS = "0x1C7c7a7a093D98088eEeCe660e267f74B96B2cEE";
// 이더리움 블록체인 네트워크의 URL을 설정합니다.
BLOCKCHAIN_URL = "http://54.180.162.22:8545";
// AuctionFactory.sol의 ABI를 설정합니다.
AUCTION_FACTORY_CONTRACT_ABI = [
   {
      "constant": true,
      "inputs": [],
      "name": "allAuctions",
      "outputs": [
         {
            "name": "",
            "type": "address[]"
         }
      ],
      "payable": false,
      "stateMutability": "view",
      "type": "function"
   },
   {
      "constant": false,
      "inputs": [
         {
            "name": "workId",
            "type": "uint256"
         },
         {
            "name": "minValue",
            "type": "uint256"
         },
         {
            "name": "startTime",
            "type": "uint256"
         },
         {
            "name": "endTime",
            "type": "uint256"
         }
      ],
      "name": "createAuction",
      "outputs": [
         {
            "name": "",
            "type": "address"
         }
      ],
      "payable": false,
      "stateMutability": "nonpayable",
      "type": "function"
   },
   {
      "constant": true,
      "inputs": [
         {
            "name": "",
            "type": "uint256"
         }
      ],
      "name": "auctions",
      "outputs": [
         {
            "name": "",
            "type": "address"
         }
      ],
      "payable": false,
      "stateMutability": "view",
      "type": "function"
   },
   {
      "constant": true,
      "inputs": [],
      "name": "owner",
      "outputs": [
         {
            "name": "",
            "type": "address"
         }
      ],
      "payable": false,
      "stateMutability": "view",
      "type": "function"
   },
   {
      "constant": false,
      "inputs": [
         {
            "name": "newOwner",
            "type": "address"
         }
      ],
      "name": "transferOwnership",
      "outputs": [],
      "payable": false,
      "stateMutability": "nonpayable",
      "type": "function"
   },
   {
      "inputs": [],
      "payable": false,
      "stateMutability": "nonpayable",
      "type": "constructor"
   },
   {
      "anonymous": false,
      "inputs": [
         {
            "indexed": false,
            "name": "auctionContract",
            "type": "address"
         },
         {
            "indexed": false,
            "name": "owner",
            "type": "address"
         },
         {
            "indexed": false,
            "name": "numAuctions",
            "type": "uint256"
         },
         {
            "indexed": false,
            "name": "allAuctions",
            "type": "address[]"
         }
      ],
      "name": "AuctionCreated",
      "type": "event"
   },
   {
      "anonymous": false,
      "inputs": [
         {
            "indexed": false,
            "name": "auctionContract",
            "type": "address"
         },
         {
            "indexed": false,
            "name": "owner",
            "type": "address"
         },
         {
            "indexed": false,
            "name": "workId",
            "type": "uint256"
         },
         {
            "indexed": false,
            "name": "minValue",
            "type": "uint256"
         },
         {
            "indexed": false,
            "name": "startTime",
            "type": "uint256"
         },
         {
            "indexed": false,
            "name": "endTime",
            "type": "uint256"
         }
      ],
      "name": "NewAuction",
      "type": "event"
   },
   {
      "anonymous": false,
      "inputs": [
         {
            "indexed": true,
            "name": "previousOwner",
            "type": "address"
         },
         {
            "indexed": true,
            "name": "newOwner",
            "type": "address"
         }
      ],
      "name": "OwnershipTransferred",
      "type": "event"
   }
]
// Auction.sol의 ABI를 설정합니다.
AUCTION_CONTRACT_ABI =[
	{
		"constant": true,
		"inputs": [],
		"name": "ended",
		"outputs": [
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [],
		"name": "bid",
		"outputs": [],
		"payable": true,
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [],
		"name": "withdraw",
		"outputs": [
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "auctionEndTime",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "_address",
				"type": "address"
			}
		],
		"name": "getPendingReturnsBy",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "digitalWorkId",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "owner",
		"outputs": [
			{
				"name": "",
				"type": "address"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [],
		"name": "cancelAuction",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "highestBidder",
		"outputs": [
			{
				"name": "",
				"type": "address"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "minValue",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "getTimeLeft",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "highestBid",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "getAuctionInfo",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			},
			{
				"name": "",
				"type": "uint256"
			},
			{
				"name": "",
				"type": "uint256"
			},
			{
				"name": "",
				"type": "uint256"
			},
			{
				"name": "",
				"type": "address"
			},
			{
				"name": "",
				"type": "uint256"
			},
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "auctionStartTime",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [],
		"name": "endAuction",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"name": "_owner",
				"type": "address"
			},
			{
				"name": "workId",
				"type": "uint256"
			},
			{
				"name": "minimum",
				"type": "uint256"
			},
			{
				"name": "startTime",
				"type": "uint256"
			},
			{
				"name": "endTime",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": false,
				"name": "bidder",
				"type": "address"
			},
			{
				"indexed": false,
				"name": "amount",
				"type": "uint256"
			}
		],
		"name": "HighestBidIncereased",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": false,
				"name": "winner",
				"type": "address"
			},
			{
				"indexed": false,
				"name": "amount",
				"type": "uint256"
			}
		],
		"name": "AuctionEnded",
		"type": "event"
	}
]
