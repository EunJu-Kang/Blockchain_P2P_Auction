package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Transaction;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.infrastructure.repository.factory.TransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepository implements ITransactionRepository {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transaction> 목록조회() {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM 트랜잭션 ");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                    new Object[]{}, (rs, rowNum) -> TransactionFactory.생성(rs));
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Transaction 조회(String hash) {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM 트랜잭션 WHERE hash=?");
        try {
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                    new Object[] { hash }, (rs, rowNum) -> TransactionFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public List<Transaction> 조회By주소(final String 주소)
    {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM 트랜잭션 WHERE from_hash=? OR to_hash=?");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                                           new Object[] { 주소, 주소 }, (rs, rowNum) -> TransactionFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public long 추가(Transaction 트랜잭션) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hash", 트랜잭션.getHash());
            paramMap.put("nonce", 트랜잭션.getNonce());
            paramMap.put("block_hash", 트랜잭션.getBlockHash());
            paramMap.put("block_number", 트랜잭션.getBlockNumber());
            paramMap.put("transaction_index", 트랜잭션.getTransactionIndex());
            paramMap.put("from_hash", 트랜잭션.getFrom());
            paramMap.put("to_hash", 트랜잭션.getTo());
            paramMap.put("value", 트랜잭션.getValue());
            paramMap.put("gas_price", 트랜잭션.getGasPrice());
            paramMap.put("gas", 트랜잭션.getGas());
            paramMap.put("input", 트랜잭션.getInput());
            paramMap.put("creates", 트랜잭션.getCreates());
            paramMap.put("public_key", 트랜잭션.getPublicKey());
            paramMap.put("raw", 트랜잭션.getRaw());
            paramMap.put("r", 트랜잭션.getR());
            paramMap.put("s", 트랜잭션.getS());
            paramMap.put("v", 트랜잭션.getV());
            paramMap.put("저장일시", LocalDateTime.now());

            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("트랜잭션")
                    .usingGeneratedKeyColumns("id");

            Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
            return newId.longValue();

        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
