package com.bcauction.domain.wrapper;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EthereumTransaction {

    private String txHash;
    private String Status;
    private String blockId;
    private String gasPrice;
    private String gas;
    private String input;
    private LocalDateTime timestamp;
    private String from;
    private String to;
    private BigInteger amount;
    private boolean accepted;

    public static EthereumTransaction getEthereumTransaction(final EthBlock.TransactionResult tx,
                                                              final BigInteger timestamp,
                                                              final boolean accepted){
        EthereumTransaction ethTx = new EthereumTransaction();
        if(tx instanceof EthBlock.TransactionHash){
            ethTx.txHash = ((EthBlock.TransactionHash) tx).get();
        } else if(tx instanceof EthBlock.TransactionObject){
            ethTx = EthereumTransaction.convertTransaction(((EthBlock.TransactionObject) tx).get());
        }
        ethTx.accepted = accepted;
        ethTx.timestamp = (timestamp != null) ? LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.longValue()), TimeZone.getDefault().toZoneId()) : null;

        return ethTx;
    }

	public static List<EthereumTransaction> getEthereumTransactionList(final List<EthBlock.TransactionResult> txs, final BigInteger timestamp, final boolean accepted){
        List<EthereumTransaction> txList = new ArrayList<>();

        for(EthBlock.TransactionResult t: txs) {
            EthereumTransaction etx = EthereumTransaction.getEthereumTransaction(t, timestamp, accepted);
            etx.setStatus("0x01");
            txList.add(etx);
        }
        return txList;
    }

    public static EthereumTransaction convertTransaction(Transaction transaction){
        EthereumTransaction tx = new EthereumTransaction();

        tx.txHash = transaction.getHash();
        tx.blockId = String.valueOf(transaction.getBlockNumber());
        tx.from = transaction.getFrom();
        tx.to = transaction.getTo();
        tx.gasPrice = transaction.getGasPriceRaw();
        tx.gas = transaction.getGasRaw();
        tx.amount = transaction.getValue();
        tx.input = transaction.getInput();

        return tx;
    }

	public static EthereumTransaction convertTransaction(final com.bcauction.domain.Transaction transaction)
    {
        EthereumTransaction tx = new EthereumTransaction();
        tx.txHash = transaction.getHash();
        tx.blockId = transaction.getBlockNumber();
        tx.from = transaction.getFrom();
        tx.to = transaction.getTo();
        tx.gasPrice = transaction.getGasPrice();
        tx.gas = transaction.getGas();
        if(transaction.getValue() != null)
            tx.amount = new BigInteger(transaction.getValue());
        return tx;
    }

	public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public boolean isAccepted() {
        return accepted;
    }
    
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGasPriceRaw() {
		return gasPrice;
	}

	public void setGasPriceRaw(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	public String getGasRaw() {
		return gas;
	}

	public void setGasRaw(String gas) {
		this.gas = gas;
	}

	public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
    
}
