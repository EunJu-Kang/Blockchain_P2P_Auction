package com.bcauction.application.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IAuctionService;
import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.Bid;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.domain.repository.IBidRepository;

@Service
public class AuctionService implements IAuctionService {
	public static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

	private IAuctionContractService auctionContractService;
	private IFabricService fabricService;
	private IAuctionRepository auctionRepository;
	private IBidRepository bidRepository;
	private IAuctionService auctionService;
	private IDigitalWorkService digitalworkService;

	@Autowired
	public AuctionService(IAuctionContractService auctionContractService, IFabricService fabricService,
			IAuctionRepository auctionRepository, IBidRepository bidRepository,
			IDigitalWorkService digitalworkService) {
		this.auctionContractService = auctionContractService;
		this.fabricService = fabricService;
		this.auctionRepository = auctionRepository;
		this.bidRepository = bidRepository;
		this.digitalworkService = digitalworkService;
	}

	@Override
	public List<Auction> 경매목록조회() {
		return this.auctionRepository.목록조회();
	}

	@Override
	public List<Auction> 나의경매목록조회(int id) {
		return this.auctionRepository.나의경매목록조회(id);
	}

	@Override
	public Auction 조회(final long 경매id) {
		return this.auctionRepository.조회(경매id);
	}

	@Override
	public Auction 조회(final String 컨트랙트주소) {
		return this.auctionRepository.조회(컨트랙트주소);
	}

	@Override
	public Auction 생성(final Auction 경매) {
		if (경매.get시작일시() == null)
			return null;
		if (경매.get종료일시() == null)
			return null;
		if (경매.get경매생성자id() == 0)
			return null;
		if (경매.get경매작품id() == 0)
			return null;
		if (경매.get컨트랙트주소() == null)
			return null;
		if (경매.get최저가() == null)
			return null;

		경매.set생성일시(LocalDateTime.now());
		long id = this.auctionRepository.생성(경매);

		return this.auctionRepository.조회(id);
	}

	@Override
	public Bid 입찰(Bid 입찰) {
		long id = this.bidRepository.생성(입찰);
		return this.bidRepository.조회(id);
	}

	@Override
	public Bid 낙찰(final long 경매id, final long 낙찰자id, final BigInteger 입찰최고가) {
		int affected = this.bidRepository.수정(경매id, 낙찰자id, 입찰최고가);
		if (affected == 0)
			return null;

		return this.bidRepository.조회(경매id, 낙찰자id, 입찰최고가);
	}

	@Override
	public Auction 경매종료(final long 경매id, final long 회원id) {
		Auction 경매 = this.auctionRepository.조회(경매id);

		if (경매 == null) {
			throw new NotFoundException(경매id + " 해당 경매를 찾을 수 없습니다.");
		}
		경매.set상태("E");
		LocalDateTime currentDateTime = LocalDateTime.now();
		경매.set종료일시(currentDateTime);
		this.auctionRepository.수정(경매);

		Ownership os = fabricService.소유권이전(경매.get경매생성자id(), 회원id, 경매.get경매작품id());

		return 경매;
	}

	@Override
	public Auction 경매취소(final long 경매id, final long 회원id) {
		Auction 경매 = this.auctionRepository.조회(경매id);
		if (경매 == null) {
			logger.error("NOT FOUND AUCTION: ", 경매id);
			throw new NotFoundException(경매id + " 해당 경매를 찾을 수 없습니다.");
		}
		경매.set상태("C");
		LocalDateTime currentDateTime = LocalDateTime.now();
		경매.set종료일시(currentDateTime);
		this.auctionRepository.수정(경매);
		return 경매;
	}

	@Override
	public List<Auction> 경매검색조회(String str) {
		List<Auction> allAuction = this.경매목록조회();
		List<Auction> searched = new ArrayList<>();

		List<DigitalWork> allArtwork = this.digitalworkService.목록조회();
		List<DigitalWork> getArtwork = new ArrayList<>();

		for (int i = 0; i < allArtwork.size(); i++) {
			if (allArtwork.get(i).get설명().contains(str) || allArtwork.get(i).get이름().contains(str)) {
				getArtwork.add(allArtwork.get(i));
			}
		}

		for (int i = 0; i < getArtwork.size(); i++) {
			for (int j = 0; j < allAuction.size(); j++) {
				if (getArtwork.get(i).getId() == allAuction.get(j).get경매작품id()) {
					searched.add(allAuction.get(j));
				}
			}
		}

		return searched;
	}
}
