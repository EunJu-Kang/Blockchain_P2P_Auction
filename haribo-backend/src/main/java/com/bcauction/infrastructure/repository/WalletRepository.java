package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.infrastructure.repository.factory.WalletFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WalletRepository implements IWalletRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Wallet> 목록조회()
	{
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM 지갑");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> WalletFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Wallet 조회(final long 소유자id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 지갑 WHERE 소유자id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { 소유자id }, (rs, rowNum) -> WalletFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Wallet 조회(final String 지갑주소)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 지갑 WHERE 주소=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { 지갑주소 }, (rs, rowNum) -> WalletFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 추가(final Wallet 지갑)
	{
		try{
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("소유자id", 지갑.get소유자id());
			paramMap.put("주소", 지갑.get주소());
			paramMap.put("잔액", 지갑.get잔액());
			paramMap.put("충전회수", 0);

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("지갑")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		}catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 잔액갱신(final String 지갑주소, final BigDecimal 잔액)
	{
		StringBuilder sbSql =  new StringBuilder("UPDATE 지갑 ");
		sbSql.append("SET 잔액=? ");
		sbSql.append("where 주소=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { 잔액, 지갑주소 });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 충전회수갱신(final String 지갑주소){
		StringBuilder sbSql =  new StringBuilder("UPDATE 지갑 SET 충전회수 = 충전회수 + 1 ");
		sbSql.append("WHERE 주소=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { 지갑주소 });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
