package com.bcauction.application;

import com.bcauction.domain.Wallet;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface IWalletService
{
	List<Wallet> 목록조회();
	Wallet 조회_ETH잔액동기화(String 지갑주소);
	Wallet 조회(long 소유자id);

	@Transactional
	Wallet 등록(Wallet 지갑);

	@Transactional
	Wallet 잔액갱신(String 지갑주소, BigDecimal 잔액);

	@Transactional
	Wallet 충전회수갱신(final String 지갑주소);

	@Transactional
	Wallet 충전(String 지갑주소);
}
