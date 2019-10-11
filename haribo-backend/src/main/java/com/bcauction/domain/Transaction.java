package com.bcauction.domain;

import org.springframework.util.Assert;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.time.LocalDateTime;

public class Transaction {
	private long id;
	private String hash;
	private String nonce;
	private String blockHash;
	private String blockNumber;
	private String transactionIndex;
	private String from;
	private String to;
	private String value;
	private String gasPrice;
	private String gas;
	private String input;
	private String creates;
	private String publicKey;
	private String raw;
	private String r;
	private String s;
	private int v;
	private LocalDateTime 저장일시;

	public Transaction() {
	}

	public Transaction(final EthBlock.TransactionResult txResult) {
		Assert.isTrue(txResult instanceof EthBlock.TransactionObject, "Wrong EthBlock.TransactionResult instance type");

		org.web3j.protocol.core.methods.response.Transaction tx = ((EthBlock.TransactionObject) txResult).get();
		this.hash = tx.getHash();
		this.nonce = String.valueOf(tx.getNonce());
		this.blockHash = tx.getBlockHash();
		this.blockNumber = String.valueOf(tx.getBlockNumber());
		this.transactionIndex = String.valueOf(tx.getTransactionIndex());
		this.from = tx.getFrom();
		this.to = tx.getTo();
		this.value = String.valueOf(tx.getValue());
		this.gasPrice = String.valueOf(tx.getGasPrice());
		this.gas = String.valueOf(tx.getGas());
		if (tx.getInput().length() < 300)
			this.input = tx.getInput();
		this.creates = tx.getCreates();
		this.publicKey = tx.getPublicKey();
		this.raw = tx.getRaw();
		this.r = tx.getR();
		this.s = tx.getS();
		this.v = (int) tx.getV();
		this.저장일시 = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public String getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getTransactionIndex() {
		return transactionIndex;
	}

	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	public String getGas() {
		return gas;
	}

	public void setGas(String gas) {
		this.gas = gas;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getCreates() {
		return creates;
	}

	public void setCreates(String creates) {
		this.creates = creates;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public LocalDateTime get저장일시() {
		return 저장일시;
	}

	public void set저장일시(LocalDateTime 저장일시) {
		this.저장일시 = 저장일시;
	}
}
