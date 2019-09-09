package com.bcauction.domain.repository;

import com.bcauction.domain.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface IWalletRepository
{
	List<Wallet> 목록조회();
	Wallet 조회(long id);
	Wallet 조회(String 지갑주소);
	
	long 추가(Wallet 지갑);
	int 잔액갱신(String 지갑주소, BigDecimal 잔액);
	int 충전회수갱신(String 지갑주소);
}
