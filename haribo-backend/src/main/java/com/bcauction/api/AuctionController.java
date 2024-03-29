package com.bcauction.api;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IAuctionService;
import com.bcauction.application.IWalletService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.AuctionInfo;
import com.bcauction.domain.Bid;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IDigitalWorkRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AuctionController {
	public static final Logger logger = LoggerFactory.getLogger(AuctionController.class);

	private IAuctionService auctionService;
	private IAuctionContractService auctionContractService;
	private IWalletService walletService;
	private IDigitalWorkRepository workRepository;

	@Autowired
	public AuctionController(IAuctionService auctionService, IAuctionContractService auctionContractService,
			IWalletService walletService, IDigitalWorkRepository workRepository) {
		Assert.notNull(auctionService, "auctionService 개체가 반드시 필요!");
		Assert.notNull(auctionContractService, "auctionContractService 개체가 반드시 필요!");

		this.auctionService = auctionService;
		this.auctionContractService = auctionContractService;
		this.walletService = walletService;
		this.workRepository = workRepository;
	}

	@RequestMapping(value = "/auctions", method = RequestMethod.POST)
	public Auction 생성(@RequestBody Auction auction) {
		Auction 경매 = auctionService.생성(auction);
		if (경매 == null)
			throw new ApplicationException("경매 정보를 입력할 수 없습니다!");

		return 경매;
	}

	@RequestMapping(value = "/auctions", method = RequestMethod.GET)
	public List<Auction> 목록조회() {
		List<Auction> 목록 = auctionService.경매목록조회();

		if (목록 == null || 목록.isEmpty())
			throw new EmptyListException("NO DATA");

		Collections.sort(목록, new Comparator<Auction>() {

			@Override
			public int compare(Auction o1, Auction o2) {
				String str1 = o1.get종료일시() + "";
				String str2 = o2.get종료일시() + "";

				return str2.compareTo(str1);
			}
		});

		return 목록;
	}

	@RequestMapping(value = "/auctions/explorer", method = RequestMethod.GET)
	public List<Auction> 전체목록조회() {
		List<Auction> 목록 = auctionService.목록조회();

		if (목록 == null || 목록.isEmpty())
			throw new EmptyListException("NO DATA");

		Collections.sort(목록, new Comparator<Auction>() {

			@Override
			public int compare(Auction o1, Auction o2) {
				String str1 = o1.get종료일시() + "";
				String str2 = o2.get종료일시() + "";

				return str2.compareTo(str1);
			}
		});

		return 목록;
	}

	@RequestMapping(value = "/auctions/{id}", method = RequestMethod.GET)
	public AuctionInfo 조회(@PathVariable long id) {
		Auction 경매 = this.auctionService.조회(id);
		if (경매 == null) {
			logger.error("NOT FOUND AUCTION: ", id);
			throw new NotFoundException(id + " 해당 경매를 찾을 수 없습니다.");
		}

		AuctionInfo 경매정보 = this.auctionContractService.경매정보조회(경매.get컨트랙트주소());
		if (경매정보 == null) {
			throw new NotFoundException(id + " 해당 경매 컨트랙트를 찾을 수 없습니다.");
		}
		경매정보.set경매컨트랙트주소(경매.get컨트랙트주소());
		경매정보.set작품id(경매.get경매작품id());
		경매정보.set최소금액(경매.get최저가());
		경매정보.set경매시작시간(경매.get시작일시());
		경매정보.set경매종료시간(경매.get종료일시());

		return 경매정보;
	}

	@RequestMapping(value = "/auctions/{aid}/by/{mid}", method = RequestMethod.DELETE)
	public Auction 경매취소(@PathVariable long aid, @PathVariable long mid) {
		return this.auctionService.경매취소(aid, mid);
	}

	@RequestMapping(value = "/auctions/{aid}/by/{mid}", method = RequestMethod.PUT)
	public Auction 경매종료(@PathVariable long aid, @PathVariable long mid) { // mid = 최고가 입찰자 id
		return this.auctionService.경매종료(aid, mid);
	}

	@RequestMapping(value = "/auctions/bid", method = RequestMethod.PUT)
	public Bid 입찰(@RequestBody Bid bid) {
		return auctionService.입찰(bid);
	}

	@RequestMapping(value = "/auctions/owner/{id}", method = RequestMethod.GET)
	public List<Auction> 사용자경매목록조회(@PathVariable int id) {
		// TODO
		List<Auction> auctions = auctionService.나의경매목록조회(id);
		return auctions;
	}

	@RequestMapping(value = "/auctions/search/{str}", method = RequestMethod.GET)
	public List<Auction> 경매검색조회(@PathVariable String str) {
		List<Auction> auctions = auctionService.경매검색조회(str);
		return auctions;
	}

	@RequestMapping(value = "/auctions/home/", method = RequestMethod.GET)
	public List<Auction> 메인경매조회() {
		List<Auction> auctions = this.목록조회();

		Collections.sort(auctions, new Comparator<Auction>() {

			@Override
			public int compare(Auction o1, Auction o2) {
				if (o2.getId() < o1.getId())
					return -1;
				else if (o2.getId() > o1.getId())
					return 1;
				else
					return 0;
			}
		});

		return auctions;
	}

	@RequestMapping(value = "/auctions/bidder/{bidder}", method = RequestMethod.GET)
	public String 최고가입찰자(@PathVariable String bidder) {
		String str = this.walletService.최고입찰자조회(bidder);
		return str;
	}

	@RequestMapping(value = "/auctions/filter/{num}", method = RequestMethod.GET)
	public List<Auction> 선택요소정렬(@PathVariable int num) {
		List<Auction> auctions = this.목록조회();

		Collections.sort(auctions, new Comparator<Auction>() {

			@Override
			public int compare(Auction o1, Auction o2) {
				String str1 = o1.get생성일시() + "";
				String str2 = o2.get생성일시() + "";
				String str3 = o1.get종료일시() + "";
				String str4 = o2.get종료일시() + "";

				if (num == 0) { // 시작시간 순
					return str1.compareTo(str2);
				} else if (num == 1) { // 종료시간 순
					return str3.compareTo(str4);
				} 
				else if (num == 2) { // 종료시간 순
					return str4.compareTo(str3);
				} else { // 가나다 순
					long art1 = o1.get경매작품id();
					long art2 = o2.get경매작품id();
					DigitalWork work1 = workRepository.조회(art1);
					DigitalWork work2 = workRepository.조회(art2);

					return work1.get이름().compareTo(work2.get이름());
				}
			}
		});

		return auctions;
	}
}
