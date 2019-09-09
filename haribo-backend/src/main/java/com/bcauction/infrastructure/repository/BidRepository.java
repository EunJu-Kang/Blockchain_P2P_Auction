package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Bid;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IBidRepository;
import com.bcauction.infrastructure.repository.factory.BidFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BidRepository implements IBidRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Bid> 목록조회() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매입찰");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> BidFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid 조회(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매입찰 WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid 조회(final Bid 입찰)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매입찰 WHERE 경매참여자id=? AND 경매id=? AND 입찰일시=? AND 입찰금액=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {
										입찰.get경매참여자id(),
										입찰.get경매id(),
										입찰.get입찰일시(),
										입찰.get입찰금액() },
								(rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid 조회(final long 경매id, final long 낙찰자id, final BigInteger 최고가) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매입찰 WHERE 경매참여자id=? AND 경매id=? AND 입찰금액=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {낙찰자id, 경매id, 최고가 }, (rs, rowNum) -> BidFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 생성(final Bid 입찰) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("경매참여자id", 입찰.get경매참여자id());
			paramMap.put("경매id", 입찰.get경매id());
			paramMap.put("입찰일시", 입찰.get입찰일시());
			paramMap.put("입찰금액", 입찰.get입찰금액());
			paramMap.put("낙찰여부", 입찰.get낙찰여부());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("경매입찰")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final Bid 입찰){
		StringBuilder sbSql =  new StringBuilder("UPDATE 경매입찰 ");
		sbSql.append("SET 낙찰여부=? ");
		sbSql.append("WHERE 경매참여자id=? AND 경매id=? AND 입찰금액=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
							new Object[] {
									입찰.get낙찰여부(),
									입찰.get경매참여자id(),
									입찰.get경매id(),
									입찰.get입찰금액()
							});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final long 경매id, final long 낙찰자id, final BigInteger 입찰최고가) {
		StringBuilder sbSql =  new StringBuilder("UPDATE 경매입찰 ");
		sbSql.append("SET 낙찰여부=? ");
		sbSql.append("WHERE 경매id=? AND 경매참여자id=? AND 입찰금액=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "Y", 경매id, 낙찰자id, 입찰최고가 });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 삭제(final long id) {
		StringBuilder sbSql =  new StringBuilder("DELETE FROM 경매입찰 WHERE id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(), new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}


}
