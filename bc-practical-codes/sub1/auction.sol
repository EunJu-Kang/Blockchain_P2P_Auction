pragma solidity ^0.5.2;

import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/ownership/Ownable.sol";

/// @title 경매
contract Auction is Ownable{

  address payable public beneficiary;
  uint public auctionEndTime;
  uint public minValue;

  // 현재 최고 입찰 상태
  address payable public highestBidder;
  uint public highestBid;

  mapping(address => uint) pendingReturns;
  address payable[] bidders;

  bool ended;

  event HighestBidIncereased(address bidder, uint amount);
  event AuctionEnded(address winner, uint amount);

  /// @notice 경매 생성
  /// @param minimum 경매품의 최소 가격
  /// @param hoursAfter 경매 진행 기간, 시간 단위
  /// @dev 생성자에서 경매의 상태 변수 beneficiary, auctionEndTime, minValue이 정해짐.
  constructor(uint minimum, uint hoursAfter) public payable{
    // todo 내용을 완성 합니다. 
    beneficiary = msg.sender;
    minValue = minimum;
    auctionEndTime =now+ hoursAfter;
  }

  /// @dev 이더를 지불하여 경매에 참가하기 위해 payable 함수로 작성
  /// 파라메터 필요하지 않음.
  /// 최고 가격(현재 가격보다 높은 값)을 제시하지 못하면 경매에 참여할 수 없음.
  function bid() public payable {
    // todo 내용을 완성 합니다. 
    require(now <= auctionEndTime);
    require(msg.value > highestBid);
    
    if(highestBid != 0){
        pendingReturns[highestBidder] += highestBid;
    }
    bidders.push(msg.sender);
    
    highestBidder = msg.sender;
    highestBid = msg.value;
    emit HighestBidIncereased(msg.sender, msg.value);
  }

  /// @dev 경매 종료까지 남은 시간을 초(in seconds)로 반환
  function getTimeLeft() public view returns (uint) {
      return (auctionEndTime - now);
  }

  /// @dev 특정 주소가 경매에 참여하여 환불받을 이더량
  /// @param _address 경매 참가자의 주소
  /// @return 경매에 참여한 참가자가 환불 받지 못한 이더
  function getPendingReturnsBy(address _address) view public returns (uint){
      return pendingReturns[_address];
  }

  /// @dev 출금 요청, 경매에 참여한 주소가 호출할 수 있음.
  /// 파라메터 필요하지 않음.
  /// @return bool 출금 성공 여부
  function withdraw() public returns (bool) {
    // todo 내용을 완성 합니다. 
     uint amount = pendingReturns[msg.sender];
        if (amount > 0) {
            pendingReturns[msg.sender] = 0;
            if (!msg.sender.send(amount)) {
                pendingReturns[msg.sender] = amount;
                return false;
            }
        }
        return true;
  }

  /// @dev 경매 생성자에 의해 경매 금액을 모두 반환하며 경매를 끝냄.
  /// 현재 최고가로 낙찰함.
  function endAuction() public onlyOwner {
    // todo 내용을 완성 합니다. 
    require(now <= auctionEndTime); // auction did not yet end
    require(!ended); // this function has already been called

    ended = true;
    for(uint i=0; i<bidders.length; i++ ){
        bidders[i].transfer(pendingReturns[bidders[i]]);
    }
    emit AuctionEnded(highestBidder, highestBid);
    
    beneficiary.transfer(highestBid);
  }

  /// @dev 경매 생성자에 의해 경매를 취소함.
  /// 현재 최고 경매가 제시자에게도 환불
  function cancelAuction() public onlyOwner{
    // todo 내용을 완성 합니다. 
     ended = true;
     for(uint i=0; i<bidders.length; i++ ){
        bidders[i].transfer(pendingReturns[bidders[i]]);
     }
     highestBidder.transfer(highestBid);
  }
}
