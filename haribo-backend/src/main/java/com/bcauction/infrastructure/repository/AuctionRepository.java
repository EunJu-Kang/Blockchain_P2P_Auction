package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Auction;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.infrastructure.repository.factory.AuctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuctionRepository implements IAuctionRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Auction> 목록조회()
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매 WHERE 상태=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{ "V" }, (rs, rowNum) -> AuctionFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction 조회(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매 WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { id }, (rs, rowNum) -> AuctionFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction 조회(final String 컨트랙트주소)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매 WHERE 컨트랙트주소=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { 컨트랙트주소 }, (rs, rowNum) -> AuctionFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 생성(final Auction 경매) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("경매생성자id", 경매.get경매생성자id());
			paramMap.put("경매작품id", 경매.get경매작품id());
			paramMap.put("생성일시", 경매.get생성일시());
			paramMap.put("상태", 경매.get상태());
			paramMap.put("시작일시", 경매.get시작일시());
			paramMap.put("종료일시", 경매.get종료일시());
			paramMap.put("최저가", 경매.get최저가());
			paramMap.put("컨트랙트주소", 경매.get컨트랙트주소());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("경매")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final Auction 경매)
	{
		StringBuilder sbSql =  new StringBuilder("UPDATE 경매 ");
		sbSql.append("SET 상태=? AND 종료일시=? ");
		sbSql.append("where id=? AND 경매생성자id=? AND 경매작품id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] {
					                           경매.get상태(),
					                           경매.get종료일시(),
					                           경매.getId(),
					                           경매.get경매생성자id(),
					                           경매.get경매작품id()
			                                });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 삭제(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("DELETE FROM 경매 WHERE id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
