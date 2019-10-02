package com.bcauction.api;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IEthereumService;
import com.bcauction.domain.Address;
import com.bcauction.domain.AuctionInfo;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/eth")
public class EthereumController {

	public static final Logger log = LoggerFactory.getLogger(EthereumController.class);

	private IEthereumService explorerService;
	private IAuctionContractService auctionContractService;

	@Autowired
	public EthereumController(IEthereumService explorerService, IAuctionContractService auctionContractService) {
		Assert.notNull(explorerService, "explorerService 개체가 반드시 필요!");
		Assert.notNull(auctionContractService, "auctionContractService 개체가 반드시 필요!");

		this.explorerService = explorerService;
		this.auctionContractService = auctionContractService;
	}

	@GetMapping("/blocks")
	public List<Block> 최근블록조회() {
		List<Block> 목록 = this.explorerService.최근블록조회();

		if (목록 == null || 목록.isEmpty())
			throw new EmptyListException("NO DATA");

		return 목록;
	}

	@GetMapping("/trans")
	public List<EthereumTransaction> 최근트랜잭션조회() {
		List<EthereumTransaction> 목록 = this.explorerService.최근트랜잭션조회();

		if (목록 == null || 목록.isEmpty())
			throw new EmptyListException("NO DATA");

		return 목록;
	}

	@GetMapping("/blocks/{id}")
	public Block 블록검색(@PathVariable String id) {
		Block 블록 = this.explorerService.블록검색(id);

		if (블록 == null)
			throw new NotFoundException(id + " 블록 정보를 찾을 수 없습니다.");

		return 블록;
	}

	@GetMapping("/trans/{id}")
	public EthereumTransaction 트랜잭션검색(@PathVariable String id) {
		EthereumTransaction 트랜잭션 = this.explorerService.트랜잭션검색(id);
		if (트랜잭션 == null)
			throw new NotFoundException(id + " 트랜잭션 정보를 찾을 수 없습니다.");
		System.out.println("1" + 트랜잭션.toString());
		return 트랜잭션;
	}

	@GetMapping("/address/{addr}")
	public Address 주소검색(@PathVariable String addr) {
		Address 주소 = this.explorerService.주소검색(addr);

		if (주소 == null)
			throw new NotFoundException(addr + " 주소 정보를 찾을 수 없습니다.");
		System.out.println("주소검색들어왔다" + 주소.toString());
		return 주소;
	}

	@GetMapping("/auctions")
	public List<AuctionInfo> 경매목록조회() {
		List<String> 경매목록 = this.auctionContractService.경매컨트랙트주소리스트();
		if (경매목록 == null || 경매목록.isEmpty())
			throw new EmptyListException("NO DATA");

		List<AuctionInfo> 경매정보목록 = new ArrayList<>();
        경매목록.forEach(경매 -> {
            AuctionInfo 경매정보 = this.auctionContractService.경매정보조회(경매.toString());
            경매정보목록.add(경매정보);
        });
		int cnt=1;
		for (String 경매 : 경매목록) {
			AuctionInfo 경매정보 = this.auctionContractService.경매정보조회(경매);
			if (경매정보 != null)
				경매정보목록.add(경매정보);
		}
		return 경매정보목록;
	}

	@GetMapping("/auctions/{addr}")
	public AuctionInfo 경매검색(@PathVariable String addr) {
		AuctionInfo 경매정보 = this.auctionContractService.경매정보조회(addr);
		if (경매정보 == null)
			throw new NotFoundException(addr + " 경매 정보를 찾을 수 없습니다.");

		return 경매정보;
	}
}
