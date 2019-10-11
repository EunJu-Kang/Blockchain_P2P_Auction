package com.bcauction.domain;

import java.math.BigInteger;
import java.util.List;

public class Address {
	private String id;
	private BigInteger balance;
	private BigInteger txCount;
	private List<com.bcauction.domain.Transaction> trans;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}

	public List<com.bcauction.domain.Transaction> getTrans() {
		return trans;
	}

	public void setTrans(List<com.bcauction.domain.Transaction> trans) {
		this.trans = trans;
	}

	public BigInteger getTxCount() {
		return txCount;
	}

	public void setTxCount(final BigInteger txCount) {
		this.txCount = txCount;
	}
}
