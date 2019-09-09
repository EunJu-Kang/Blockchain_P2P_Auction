package com.bcauction.application.impl;

import com.bcauction.application.IEthereumService;
import com.bcauction.domain.*;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;
import com.bcauction.infrastructure.repository.WalletRepository;

import ch.qos.logback.core.net.SyslogOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EthereumService implements IEthereumService {

	private static final Logger log = LoggerFactory.getLogger(EthereumService.class);

	public static final BigInteger GAS_PRICE = BigInteger.valueOf(1L);
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21_000L);

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;
	@Value("${eth.encrypted.password}")
	private String PASSWORD;
	@Value("${eth.admin.wallet.filename}")
	private String ADMIN_WALLET_FILE;

	private ITransactionRepository transactionRepository;
	private IWalletRepository walletRepository;
	@Autowired
	private Web3j web3j;
	@Autowired
	private Admin admin = Admin.build(new HttpService());

	@Autowired
	public EthereumService(ITransactionRepository transactionRepository, IWalletRepository walletRepository) {
		this.transactionRepository = transactionRepository;
		this.walletRepository = walletRepository;
	}

	private EthBlock.Block 최근블록(final boolean fullFetched) {
		try {
			EthBlock latestBlockResponse;
			latestBlockResponse = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, fullFetched).sendAsync()
					.get();
			return latestBlockResponse.getBlock();
		} catch (ExecutionException | InterruptedException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * 최근 블록 조회 예) 최근 20개의 블록 조회
	 *
	 * @return List<Block>
	 */
	@Override
	public List<Block> 최근블록조회() {
		BigInteger blocknum = this.최근블록(true).getNumber();
		List<Block> blockList = new ArrayList<Block>();
		for (long i = 0; i < 10; i++) {
			DefaultBlockParameter defaultBlockParameter = DefaultBlockParameter
					.valueOf(blocknum.add(BigInteger.valueOf((-i))));
			EthBlock block = null;
			try {
				block = web3j.ethGetBlockByNumber(defaultBlockParameter, false).sendAsync().get();
				blockList.add(Block.fromOriginalBlock(block.getBlock()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return blockList;
	}

	/**
	 * 최근 생성된 블록에 포함된 트랜잭션 조회 이더리움 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 *
	 * @return List<EthereumTransaction>
	 */
	@Override
	public List<EthereumTransaction> 최근트랜잭션조회() {
		List<EthereumTransaction> listTransaction = new ArrayList<>();
		EthBlock.Block block = this.최근블록(true);
		listTransaction = EthereumTransaction.getEthereumTransactionList(block.getTransactions(), block.getTimestamp(),
				true);
		return listTransaction;
	}

	/**
	 * 특정 블록 검색 조회한 블록을 Block으로 변환해야 한다.
	 *
	 * @param 블록No
	 * @return Block
	 */
	@Override
	public Block 블록검색(String 블록No) {
		BigInteger blocknum = new BigInteger(블록No);
		DefaultBlockParameter defaultBlockParameter;
		EthBlock latestBlockResponse = null;

		Block Selectblock = null;

		defaultBlockParameter = DefaultBlockParameter.valueOf(blocknum);
		try {
			latestBlockResponse = web3j.ethGetBlockByNumber(defaultBlockParameter, true).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		Selectblock = Block.fromOriginalBlock(latestBlockResponse.getBlock());

		return Selectblock;
	}

	/**
	 * 특정 hash 값을 갖는 트랜잭션 검색 조회한 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 *
	 * @param 트랜잭션Hash
	 * @return EthereumTransaction
	 */
	@Override
	public EthereumTransaction 트랜잭션검색(String 트랜잭션Hash) {
		// TODO
		EthereumTransaction selectTranstion = null;
		EthTransaction ethTransaction = null;

		try {
			ethTransaction = web3j.ethGetTransactionByHash(트랜잭션Hash).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		selectTranstion = EthereumTransaction.convertTransaction(ethTransaction.getResult());
		return selectTranstion;
	}

	/**
	 * 이더리움으로부터 해당 주소의 잔액을 조회하고 동기화한 트랜잭션 테이블로부터 Address 정보의 trans 필드를 완성하여 정보를
	 * 반환한다.
	 *
	 * @param 주소
	 * @return Address
	 */

	/**
	 * [주소]로 시스템에서 정한 양 만큼 이더를 송금한다. 이더를 송금하는 트랜잭션을 생성, 전송한 후 결과인 String형의 트랜잭션 hash
	 * 값을 반환한다.
	 *
	 * @param 주소
	 * @return String 생성된 트랜잭션의 hash 반환 (참고, TransactionReceipt)
	 */
	@Override
	public String 충전(final String 주소) // 특정 주소로 테스트 특정 양(5Eth) 만큼 충전해준다.
	{

		PersonalUnlockAccount personalUnlockAccount = null;
		String transactionHash = null;
		try {
			personalUnlockAccount = admin.personalUnlockAccount(ADMIN_ADDRESS, PASSWORD).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (personalUnlockAccount.accountUnlocked()) {
			BigInteger value = Convert.toWei("5.0", Convert.Unit.ETHER).toBigInteger();
			Transaction Transaction = org.web3j.protocol.core.methods.request.Transaction
					.createEtherTransaction(ADMIN_ADDRESS, null, GAS_PRICE, GAS_LIMIT, 주소, value);
			EthSendTransaction transactionResponse;
			try {
				transactionResponse = web3j.ethSendTransaction(Transaction).sendAsync().get();
				transactionHash = transactionResponse.getTransactionHash();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return transactionHash;
	}

	@Override
	public void 잔액동기화(Wallet wallet) {
		try {
			EthGetBalance ethGetBalance = web3j.ethGetBalance(wallet.get주소(), DefaultBlockParameterName.LATEST)
					.sendAsync().get();
			BigInteger wei = ethGetBalance.getBalance();
			java.math.BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
			if (tokenValue != wallet.get잔액()) {
				wallet.set잔액(tokenValue);
				walletRepository.잔액갱신(wallet.get주소(), tokenValue);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Address 주소검색(String 주소) {
		// TODO Auto-generated method stub
		return null;
	}

}
