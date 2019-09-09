package com.bcauction.application;

import com.bcauction.domain.Auction;
import com.bcauction.domain.Bid;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface IAuctionService
{
	List<Auction> 경매목록조회();
	Auction 조회(long 경매id);
	Auction 조회(String 컨트랙트주소);

	@Transactional
	Auction 생성(Auction 경매);

	@Transactional
	Bid 입찰(Bid bid);

	@Transactional
	Bid 낙찰(long 경매id,long 회원id, final BigInteger 입찰최고가);

	@Transactional
	Auction 경매종료(long 경매id, long 회원id); // 현재 최고가에서 끝내기, 소유권 이전

	@Transactional
	Auction 경매취소(long 경매id, long 회원id); // 환불 후 옥션 끝내기
}
