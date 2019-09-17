// // 웹서버 API URL을 지정합니다.
// API_BASE_URL = "http://13.125.178.26:8089";

API_BASE_URL = "http://localhost:8080";
// 배포한 옥션 컨트랙트 주소를 지정합니다.
AUCTION_CONTRACT_ADDRESS = "0xa320375bba452e1b951e1459d1b612cc2b267462";
// 이더리움 블록체인 네트워크의 URL을 설정합니다.
BLOCKCHAIN_URL = "http://54.180.162.22:8545";
// AuctionFactory.sol의 ABI를 설정합니다.
AUCTION_FACTORY_CONTRACT_ABI = [{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"name":"previousOwner","type":"address"},{"indexed":true,"name":"newOwner","type":"address"}],"name":"OwnershipTransferred","type":"event"}];
// Auction.sol의 ABI를 설정합니다.
AUCTION_CONTRACT_ABI = [{"constant":true,"inputs":[],"name":"owner","outputs":[{"internalType":"address","name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"internalType":"address","name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"previousOwner","type":"address"},{"indexed":true,"internalType":"address","name":"newOwner","type":"address"}],"name":"OwnershipTransferred","type":"event"}];
