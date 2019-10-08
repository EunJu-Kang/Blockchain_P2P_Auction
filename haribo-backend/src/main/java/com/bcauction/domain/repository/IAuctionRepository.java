package com.bcauction.domain.repository;

import com.bcauction.domain.Auction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAuctionRepository
{
	List<Auction> 목록조회();
	List<Auction> 나의경매목록조회(int id);
	Auction 조회(long id);
	Auction 조회(String 컨트랙트주소);
	List<Auction> 작품경매목록조회(long id);

	@Transactional
	long 생성(Auction 경매);

	@Transactional
	int 수정(Auction 경매);

	@Transactional
	int 삭제(long id);
}
