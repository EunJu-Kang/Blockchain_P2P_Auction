package com.bcauction.application.impl;

import com.bcauction.application.IEthereumService;
import com.bcauction.application.IWalletService;
import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService implements IWalletService {
	private static final Logger log = LoggerFactory.getLogger(WalletService.class);

	private IWalletRepository walletRepository;
	private IEthereumService ethereumService;

	@Autowired
	public WalletService(IWalletRepository walletRepository, IEthereumService ethereumService) {
		this.walletRepository = walletRepository;
		this.ethereumService = ethereumService;
	}

	@Override
	public List<Wallet> 목록조회() {
		return this.walletRepository.목록조회();
	}

	@Override
	public Wallet 조회_ETH잔액동기화(final String 지갑주소) {
		Wallet wallet = walletRepository.조회(지갑주소);
		if (wallet == null)
			throw new NotFoundException(지갑주소 + " 해당 주소 지갑을 찾을 수 없습니다.");
		else {
			this.ethereumService.잔액동기화(wallet);
		}

		return wallet;
	}

	@Override
	public Wallet 조회(final long id) {
		Wallet wallet = this.walletRepository.조회(id);
		if (wallet == null)
			return null;

		return 조회_ETH잔액동기화(wallet.get주소());
	}

	@Override
	public Wallet 등록(final Wallet 지갑) {
		long id = this.walletRepository.추가(지갑);
		return this.walletRepository.조회(id);
	}

	@Override
	public Wallet 잔액갱신(final String 지갑주소, final BigDecimal 잔액) {
		int affected = this.walletRepository.잔액갱신(지갑주소, 잔액);
		if (affected == 0)
			throw new ApplicationException("잔액갱신 처리가 반영되지 않았습니다.");

		return this.walletRepository.조회(지갑주소);
	}

	@Override
	public Wallet 충전회수갱신(final String 지갑주소) {
		int affected = this.walletRepository.충전회수갱신(지갑주소);
		if (affected == 0)
			throw new ApplicationException("충전회수갱신 처리가 반영되지 않았습니다.");

		return this.walletRepository.조회(지갑주소);
	}

	@Override
	public Wallet 충전(String 지갑주소) {
		Wallet wallet = this.조회_ETH잔액동기화(지갑주소);
		if (wallet == null || !wallet.충전가능()) {
			throw new ApplicationException("[1] 충전할 수 없습니다!");
		}

		try {
			String txHash = this.ethereumService.충전(지갑주소);
			if (txHash == null || txHash.equals(""))
				throw new ApplicationException("충전회수갱신 트랜잭션을 보낼 수 없습니다!");

			this.충전회수갱신(지갑주소);
			return this.조회_ETH잔액동기화(지갑주소);
		} catch (Exception e) {
			throw new ApplicationException("[2] 충전할 수 없습니다!");
		}
	}
}
