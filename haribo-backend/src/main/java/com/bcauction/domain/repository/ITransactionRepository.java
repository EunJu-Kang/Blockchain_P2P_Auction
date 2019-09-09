package com.bcauction.domain.repository;

import com.bcauction.domain.Transaction;

import java.util.List;

public interface ITransactionRepository {
    List<Transaction> 목록조회();
    Transaction 조회(String hash);
    List<Transaction> 조회By주소(String 주소);
    long 추가(Transaction 트랜잭션);
}
