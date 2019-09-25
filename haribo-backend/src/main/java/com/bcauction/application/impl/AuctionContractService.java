package com.bcauction.application.impl;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IAuctionService;
import com.bcauction.domain.*;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.DomainException;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.domain.wrapper.AuctionContract;
import com.bcauction.domain.wrapper.AuctionFactoryContract;
import com.bcauction.domain.wrapper.AuctionFactoryContract.AuctionCreatedEventResponse;
import com.bcauction.infrastructure.repository.factory.WalletFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Event;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteRef;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * AuctionContractService 작성, 배포한 AuctionFactory.sol Auction.sol 스마트 컨트랙트를 이용한다.
 */
@Service
public class AuctionContractService implements IAuctionContractService {
	private static final Logger log = LoggerFactory.getLogger(AuctionContractService.class);

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
	private IAuctionService auctionService;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	
	@Autowired
	public AuctionContractService(IWalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	public AuctionContractService(IAuctionRepository auctionRepository) {
		this.auctionRepository = auctionRepository;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 경매 정보를 조회한다.
	 * 
	 * @param 컨트랙트주소
	 * @return AuctionInfo 
	 * 1. web3j API를 이용하여 해당 컨트랙트주소의 스마트 컨트랙트를 로드(load)한다.
	 * 2. info의 highestBidder의 정보를 가지고 최고입찰자 회원의 id를 찾아 
	 * 3. AuctionInfo의 인스턴스를 생성하여 반환한다.
	 */
	@Override
	public AuctionInfo 경매정보조회(final String 컨트랙트주소) {
		// TODO
		AuctionInfo auctionInfo = new AuctionInfo();
		BigInteger highestBid = BigInteger.ZERO;
		String highestBidder = null;
		Wallet wallet = new Wallet();
		Long highestBidderId = null;
		try {
			web3j = Web3j.build(new HttpService("http://54.180.162.22:8545"));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionFactoryContract = AuctionFactoryContract.load(AUCTION_FACTORY_CONTRACT, web3j, credentials, contractGasProvider);
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

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰가를 조회한다.
	 * 
	 * @param 컨트랙트주소
	 * @return BigInteger 현재최고가
	 */
	@Override
	public BigInteger 현재최고가(final String 컨트랙트주소) {
		// TODO
		BigInteger highestBid = BigInteger.ZERO;
		try {
			web3j = Web3j.build(new HttpService("http://54.180.162.22:8545"));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionFactoryContract = AuctionFactoryContract.load(AUCTION_FACTORY_CONTRACT, web3j, credentials, contractGasProvider);
			auctionContract = AuctionContract.load(컨트랙트주소, web3j, credentials, contractGasProvider);
			highestBid = auctionContract.highestBid().send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return highestBid;
	   }

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰 주소를 조회한다.
	 * 
	 * @param 컨트랙트주소
	 * @return String 최고 입찰한 이더리움 주소(EOA)
	 */
	@Override
	public String 현재최고입찰자주소(final String 컨트랙트주소) {
		// TODO
		String highestBidder = null;
		try {
			web3j = Web3j.build(new HttpService("http://54.180.162.22:8545"));
			credentials = WalletUtils.loadCredentials(PASSWORD, WALLET_RESOURCE);
			auctionContract = AuctionContract.load(컨트랙트주소, web3j, credentials, contractGasProvider);
			highestBidder = auctionContract.highestBidder().send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return highestBidder;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 생성된 모든 경매 컨트랙트의 주소 목록을 조회한다.
	 * 
	 * @return List<String> 경매 컨트랙트의 주소 리스트
	 */
	@Override
	public List<String> 경매컨트랙트주소리스트() {
		// TODO
		String str;
		List<String> list = new ArrayList<>();
		try {
			str = auctionFactoryContract.allAuctions().send().toString();
			list = new ArrayList<>();
			StringTokenizer tokens = new StringTokenizer(str, "[|, |]");
			for (int x = 1; tokens.hasMoreElements(); x++) {
				list.add(tokens.nextToken());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
