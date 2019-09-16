pragma solidity ^0.4.24;

/**
 * @title Ownable
 * @dev The Ownable contract has an owner address, and provides basic authorization control
 * functions, this simplifies the implementation of "user permissions".
 */
contract Ownable {
  address public owner;

  event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

  /**
   * @dev The Ownable constructor sets the original `owner` of the contract to the sender
   * account.
   */
  constructor() public {
    owner = msg.sender;
  }


  /**
   * @dev Throws if called by any account other than the owner.
   */
  modifier onlyOwner() {
    require(msg.sender == owner);
    _;
  }


  /**
   * @dev Allows the current owner to transfer control of the contract to a newOwner.
   * @param newOwner The address to transfer ownership to.
   */
  function transferOwnership(address newOwner) public onlyOwner {
    require(newOwner != address(0));
    emit OwnershipTransferred(owner, newOwner);
    owner = newOwner;
  }

}

contract AuctionFactory is Ownable {

    address[] public auctions;

    event AuctionCreated(address auctionContract, address owner, uint numAuctions, address[] allAuctions);
    event NewAuction(address auctionContract, address owner, uint workId, uint minValue, uint startTime, uint endTime);

    constructor() public {
    }

    function createAuction(uint workId, uint minValue,  uint startTime, uint endTime) public returns (address){
        Auction newAuction = new Auction(msg.sender, workId, minValue, startTime, endTime);
        auctions.push(newAuction);
        emit NewAuction(newAuction, msg.sender, workId, minValue, startTime, endTime);
        return newAuction;
        //AuctionCreated(newAuction, msg.sender, auctions.length, auctions);
    }

    function allAuctions() public constant returns (address[]) {
        return auctions;
    }
}

contract Auction {

  // 생성자에 의해 정해지는 값
  address public owner;
  uint public auctionStartTime;
  uint public auctionEndTime;
  uint public minValue;
  uint public digitalWorkId;

  // 현재 최고 입찰 상태
  address public highestBidder;
  uint public highestBid;

  mapping(address => uint) pendingReturns;
  address[] bidders;

  bool public ended;

  event HighestBidIncereased(address bidder, uint amount);
  event AuctionEnded(address winner, uint amount);

  constructor(address _owner, uint workId, uint minimum, uint startTime, uint endTime) public {
      require(minimum > 0);
      digitalWorkId = workId;
      minValue = minimum * 1 ether;
      owner = _owner;
      auctionStartTime = startTime;
      auctionEndTime = endTime;
  }

  function bid() public payable
  onlyNotOwner
  onlyBeforeEnd
  onlyHigherThanMinValue
  onlyHigherThanCurrentHighest{

      if(highestBid > 0) {
        pendingReturns[highestBidder] += highestBid;
      }

      highestBid = msg.value;
      highestBidder = msg.sender;
      bidders.push(msg.sender);

      emit HighestBidIncereased(msg.sender, msg.value);
  }

  function getTimeLeft() public view returns (uint) {
      return (auctionEndTime - now);
  }

  function getPendingReturnsBy(address _address) view public returns (uint){
      return pendingReturns[_address];
  }

  function getAuctionInfo() view public returns (uint, uint, uint, uint, address, uint, bool){
      return (auctionStartTime, auctionEndTime, minValue, digitalWorkId, highestBidder, highestBid, ended);
  }

  function withdraw() public onlyBidder returns (bool) {

      uint amount = pendingReturns[msg.sender];

      if(!msg.sender.send(amount)){
          pendingReturns[msg.sender] = amount;
          return false;
      }
      pendingReturns[msg.sender] = 0;
      return true;
  }

  function endAuction() public
  onlyOwner
  onlyBeforeEnd{
    _end();
    _refund(false);
    owner.transfer(highestBid);

    emit AuctionEnded(highestBidder, highestBid);
  }

  function cancelAuction() public
  onlyOwner
  onlyBeforeEnd{
    _end();
    _refund(true);
  }

  function _end() internal{
      ended = true;
  }

  function _refund(bool isCancelled) internal {
      uint returnToHighestBidder;
      if(isCancelled){
        returnToHighestBidder = pendingReturns[highestBidder] + highestBid;
      } else {
        returnToHighestBidder = pendingReturns[highestBidder];
      }

      for(uint i = 0; i < bidders.length; i++){
        address bidder = bidders[i];
        if(bidder != highestBidder){
            if(!bidder.send(pendingReturns[bidder])){
                revert();
            }
        } else {
            if(!bidder.send(returnToHighestBidder)){
                revert();
            }
        }
      }
  }

  modifier onlyOwner {
    require(msg.sender == owner);
    _;
  }

  modifier onlyNotOwner {
    require(msg.sender != owner);
    _;
  }

  modifier onlyAfterStart {
    require(now >= auctionStartTime);
    _;
  }

  modifier onlyBeforeEnd {
    require(now < auctionEndTime && !ended);
    _;
  }

  modifier onlyHigherThanMinValue{
     require(msg.value >= minValue);
    _;
  }

  modifier onlyHigherThanCurrentHighest {
    require(msg.value > highestBid);
    _;
  }

  modifier onlyBidder {
    require(pendingReturns[msg.sender] != 0);
    _;
  }

}
