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

/// @title AuctionFactory
contract AuctionFactory is Ownable {

    // 생성된 모든 auction 리스트 
    address[] public auctions;

    event AuctionCreated(address auctionContract, address owner, uint numAuctions, address[] allAuctions);
    event NewAuction(address auctionContract, address owner, uint workId, uint minValue, uint startTime, uint endTime);

    constructor() public {
    }

    /**
     * @dev 해당 state를 가지는 새로운 Auction을 생성합니다. 
     * @param workId 작품의 Id를 나타냅니다.
     * @param minValue 경매시 입찰할 수 있는 최고가를 말합니다.
     * @param startTime 경매 시작 시각으로 timestamp 형태로 받습니다.
     * @param endTime 경매 종료 시각으로 timestamp 형태로 받습니다.
     */
    function createAuction(uint workId, uint minValue, uint startTime, uint endTime) public returns (address){
      // todo 내용을 완성 합니다. 
    }

    /**
     * @dev 다음과 같이 함수를 추가해도 좋습니다. 
     */
    function allAuctions() public constant returns (address[]) {
        return auctions;
    }
}

/// @title Auction
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

  //**
   * @dev AuctionFactory의 createAuction함수에서 호출하는 생성자입니다.
   * 경매에서 고려해야하는 제한사항을 고려하여 상태변수를 초기화합니다. 
   */
  constructor(address _owner, uint workId, uint minimum, uint startTime, uint endTime) public {
    // todo 내용을 완성 합니다.  
  }

  //**
   * @dev 입찰을 위한 함수입니다. 
   */
  function bid() public onlyNotOwner payable {
    // todo 내용을 완성 합니다. 
  }

  //**
   * @dev 환불을 위한 함수입니다. 
   * 환불은 입찰 당사자가 해당 함수를 호출함으로써 가능합니다.
   */
  function withdraw() public returns (bool) {
    // todo 내용을 완성 합니다.  
  }

  //**
   * @dev 경매 종료를 위한 함수입니다.
   * 경매 생성자만이 경매를 종료시킬 수 있습니다.
   * 현재까지의 입찰 중 최고가를 선택하여 경매를 종료합니다. 
   */
  function endAuction() public onlyOwner {
    // todo 내용을 완성 합니다. 
  }

  //**
   * @dev 경매 취소를 위한 함수입니다. 
   * 경매 생성자만이 경매를 취소할 수 있습니다.
   * 모든 입찰에 대해 환불을 수행하고 경매를 종료합니다.  
   */
  function cancelAuction() public onlyOwner {
    // todo 내용을 완성 합니다. 
  }

  //**
   * @dev 이와 같이 추가 함수를 구현해보아도 좋습니다.  
   */
  function getPendingReturnsBy(address _address) view public returns (uint){
      return pendingReturns[_address];
  }

  //**
   * @dev 이와 같이 추가 modifier를 구현해보아도 좋습니다.  
   */
  modifier onlyNotOwner {
    require(msg.sender != owner);
    _;
  }

}
