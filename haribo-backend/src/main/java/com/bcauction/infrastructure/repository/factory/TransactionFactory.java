package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionFactory {

    public static Transaction 생성(ResultSet rs) throws SQLException {
        if (rs == null) return null;
        Transaction 트랜잭션 = new Transaction();

        트랜잭션.setId(rs.getLong("id"));
        트랜잭션.setHash(rs.getString("hash"));
        트랜잭션.setNonce(rs.getString("nonce"));
        트랜잭션.setBlockHash(rs.getString("block_hash"));
        트랜잭션.setBlockNumber(rs.getString("block_number"));
        트랜잭션.setTransactionIndex(rs.getString("transaction_index"));
        트랜잭션.setFrom(rs.getString("from_hash"));
        트랜잭션.setTo(rs.getString("to_hash"));
        트랜잭션.setValue(rs.getString("value"));
        트랜잭션.setGasPrice(rs.getString("gas_price"));
        트랜잭션.setGas(rs.getString("gas"));
        트랜잭션.setInput(rs.getString("input"));
        트랜잭션.setCreates(rs.getString("creates"));
        트랜잭션.setPublicKey(rs.getString("public_key"));
        트랜잭션.setRaw(rs.getString("raw"));
        트랜잭션.setR(rs.getString("r"));
        트랜잭션.setS(rs.getString("s"));
        트랜잭션.setV(rs.getInt("v"));
        트랜잭션.set저장일시(rs.getTimestamp("저장일시").toLocalDateTime());

        return 트랜잭션;
    }
}
