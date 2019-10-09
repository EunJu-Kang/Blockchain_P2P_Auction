package com.bcauction.application.impl;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import com.bcauction.domain.repository.IEthInfoRepository;
import com.bcauction.domain.repository.ITransactionRepository;

@Service
public class EthBlockListeningService
{
	private static final Logger log = LoggerFactory.getLogger(EthBlockListeningService.class);

	private BigInteger latestBlockHeight = BigInteger.valueOf(0);

	private Web3j web3j;
	private IEthInfoRepository ethInfoRepository;
	private ITransactionRepository transactionRepository;
	private EthereumService etherumService;

	@Value("${spring.web3j.client-address}")
	private String ethUrl;

	@Autowired
	public EthBlockListeningService(Web3j web3j,
			IEthInfoRepository ethInfoRepository,
			ITransactionRepository transactionRepository,
			EthereumService ethereumService)
	{
		this.web3j = web3j;
		this.ethInfoRepository = ethInfoRepository;
		this.transactionRepository = transactionRepository;
		this.etherumService = ethereumService;
	}

	@PostConstruct
	public void listen()
	{
		System.out.println("listen start");
		Subscription subscription = (Subscription)web3j.replayPastAndFutureBlocksFlowable(
				(DefaultBlockParameterName.LATEST), false)
				.subscribe(block -> {
					BigInteger blockNumber= block.getBlock().getNumber();
					this.ethInfoRepository.put(ethUrl, blockNumber.toString());
					etherumService.changeTran(blockNumber.toString());
				});
		log.info("New Block Subscribed Here");
	}
}