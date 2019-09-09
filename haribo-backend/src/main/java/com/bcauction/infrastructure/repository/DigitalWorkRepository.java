package com.bcauction.infrastructure.repository;

import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.infrastructure.repository.factory.DigitalWorkFactory;
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
public class DigitalWorkRepository implements IDigitalWorkRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<DigitalWork> 목록조회() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 작품 WHERE 공개여부=? AND 상태=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{"Y", "Y"}, (rs, rowNum) -> DigitalWorkFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}


	@Override
	public List<DigitalWork> 사용자작품목록조회(final long 회원id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 작품 WHERE 상태=? AND 회원id=? ");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{"Y", 회원id}, (rs, rowNum) -> DigitalWorkFactory.생성(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public DigitalWork 조회(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 작품 WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> DigitalWorkFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public DigitalWork 조회(final long 회원id, final String 이름) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM 작품 WHERE 회원id=? AND 이름=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { 회원id, 이름 }, (rs, rowNum) -> DigitalWorkFactory.생성(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long 추가(final DigitalWork 작품) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("이름", 작품.get이름());
			paramMap.put("설명", 작품.get설명());
			paramMap.put("공개여부", 작품.get공개여부());
			paramMap.put("상태", 작품.get상태());
			paramMap.put("회원id", 작품.get회원id());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("작품")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 수정(final DigitalWork 작품) {
		StringBuilder sbSql =  new StringBuilder("UPDATE 작품 ");
		sbSql.append("SET 이름=?, 설명=?, 공개여부=?, 상태=?, 회원id=? ");
		sbSql.append("where id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] {
										작품.get이름(),
										작품.get설명(),
										작품.get공개여부(),
										작품.get상태(),
										작품.get회원id(),
										작품.getId()
			                                });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int 삭제(final long id) { // 상태를 N으로 업데이트
		StringBuilder sbSql =  new StringBuilder("UPDATE 작품 ");
		sbSql.append("SET 상태=?, 공개여부=? ");
		sbSql.append("where id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "N", "N", id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}

	}

}
