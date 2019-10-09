package com.bcauction.application.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import com.bcauction.application.IEthereumService;
import com.bcauction.domain.Address;
import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;

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
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return blockList;
	}

	@Override
	public List<com.bcauction.domain.Transaction> 최근트랜잭션조회() {
		List<com.bcauction.domain.Transaction> ListTran = transactionRepository.목록조회();
		return ListTran;
	}

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

	@Override
	public EthereumTransaction 트랜잭션검색(String 트랜잭션Hash) {
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

	@Override
	public String 충전(final String 주소) {
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
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Address 주소검색(String 주소) {
		Address address = new Address();

		List<com.bcauction.domain.Transaction> ListTran = transactionRepository.조회By주소(주소);
		EthGetBalance ethGetBalance;
		EthGetTransactionCount txCount;
		try {
			ethGetBalance = web3j.ethGetBalance(주소, DefaultBlockParameterName.LATEST).sendAsync().get();
			txCount = web3j.ethGetTransactionCount(주소, DefaultBlockParameterName.LATEST).sendAsync().get();
			address.setBalance(ethGetBalance.getBalance());
			address.setTxCount(txCount.getTransactionCount());
			address.setTrans(ListTran);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return address;
	}

	public void changeTran(String BlockNumber) {
		Block block = 블록검색(BlockNumber);
		List<EthereumTransaction> ListTran = block.getTrans();
		com.bcauction.domain.Transaction transaction = new com.bcauction.domain.Transaction();

		if (ListTran.size() > 0) {
			for (int i = 0; i < ListTran.size(); i++) {
				EthereumTransaction tx = ListTran.get(i);
				transaction.setId(0);
				transaction.setHash(tx.getTxHash());
				transaction.setNonce(null);
				transaction.setBlockHash(null);
				transaction.setBlockNumber(tx.getBlockId());
				transaction.setTransactionIndex(null);
				transaction.setFrom(tx.getFrom());
				transaction.setTo(tx.getTo());
				transaction.setValue(null);
				transaction.setGasPrice(tx.getGasPriceRaw());
				transaction.setGas(tx.getGasRaw());
				transaction.setInput(tx.getInput());
				transaction.setCreates(null);
				transaction.setPublicKey(null);
				transaction.setRaw(null);
				transaction.setR(null);
				transaction.setS(null);
				transaction.setV(0);
				transaction.set저장일시(tx.getTimestamp());

				transactionRepository.추가(transaction);
			}
		}
	}
}
