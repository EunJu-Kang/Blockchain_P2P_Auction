package com.bcauction.application.impl;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.domain.*;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.domain.wrapper.AuctionContract;
import com.bcauction.domain.wrapper.AuctionFactoryContract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class AuctionContractService implements IAuctionContractService {
	private static final Logger log = LoggerFactory.getLogger(AuctionContractService.class);
	private String ad = "http://54.180.162.22:3336";

	@Value("${eth.auction.factory.contract}")
	private String AUCTION_FACTORY_CONTRACT;

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;

	@Value("${eth.admin.wallet.filename}")
	private String WALLET_RESOURCE;

	@Value("${eth.encrypted.password}")
	private String PASSWORD;

	private AuctionFactoryContract auctionFactoryContract;
	private AuctionContract auctionContract;
	private ContractGasProvider contractGasProvider = new DefaultGasProvider();
	private Credentials credentials;

	@Autowired
	private Web3j web3j;

	private IWalletRepository walletRepository;
	private IAuctionRepository auctionRepository;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public AuctionContractService(IWalletRepository walletRepository, IAuctionRepository auctionRepository) {
		this.walletRepository = walletRepository;
		this.auctionRepository = auctionRepository;
	}

	@Override
	public AuctionInfo 경매정보조회(final String 컨트랙트주소) {
		AuctionInfo auctionInfo = new AuctionInfo();
		BigInteger highestBid = BigInteger.ZERO;
		String highestBidder = null;
		Wallet wallet = new Wallet();
		Long highestBidderId = null;
		try {
			web3j = Web3j.build(new HttpService(ad));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionFactoryContract = AuctionFactoryContract.load(AUCTION_FACTORY_CONTRACT, web3j, credentials,
					contractGasProvider);
			auctionContract = AuctionContract.load(컨트랙트주소, web3j, credentials, contractGasProvider);

			highestBid = this.현재최고가(컨트랙트주소);
			highestBidder = this.현재최고입찰자주소(컨트랙트주소);
			wallet = this.walletRepository.조회(highestBidder);
			if(wallet == null) {
				highestBidderId = (long) 0;
			} else {
				highestBidderId = wallet.get소유자id();
			}
	
			auctionInfo.set최고입찰액(highestBid);
			auctionInfo.set최고입찰자id(highestBidderId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return auctionInfo;
	}

	@Override
	public BigInteger 현재최고가(final String 컨트랙트주소) {
		BigInteger highestBid = BigInteger.ZERO;
		try {
			web3j = Web3j.build(new HttpService(ad));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionFactoryContract = AuctionFactoryContract.load(AUCTION_FACTORY_CONTRACT, web3j, credentials,
					contractGasProvider);
			auctionContract = AuctionContract.load(컨트랙트주소, web3j, credentials, contractGasProvider);
			highestBid = auctionContract.highestBid().send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return highestBid;
	}

	@Override
	public String 현재최고입찰자주소(final String 컨트랙트주소) {
		String highestBidder = null;
		try {
			web3j = Web3j.build(new HttpService(ad));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionContract = AuctionContract.load(컨트랙트주소, web3j, credentials, contractGasProvider);
			highestBidder = auctionContract.highestBidder().send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return highestBidder;
	}

	@Override
	public List<String> 경매컨트랙트주소리스트() {
		String str;
		List<String> list = new ArrayList<>();
		try {
			web3j = Web3j.build(new HttpService(ad));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionFactoryContract = AuctionFactoryContract.load(AUCTION_FACTORY_CONTRACT, web3j, credentials,
					contractGasProvider);
			str = auctionFactoryContract.allAuctions().send().toString();
			list = new ArrayList<>();
			StringTokenizer tokens = new StringTokenizer(str, "[|, |]");
			
			for (int x = 1; tokens.hasMoreElements(); x++) {
				list.add(tokens.nextToken());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
