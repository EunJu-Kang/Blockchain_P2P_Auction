package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Member;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IMemberRepository;
import com.bcauction.infrastructure.repository.factory.MemberFactory;
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
public class MemberRepository implements IMemberRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Member> 목록조회() {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매회원 ");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                    new Object[]{}, (rs, rowNum) -> MemberFactory.생성(rs));
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Member 조회(long id) {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM 경매회원 WHERE id=?");
        try {
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                    new Object[] { id }, (rs, rowNum) -> MemberFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Member 조회(final String 이메일)
    {
        StringBuilder sbSql = new StringBuilder("SELECT * FROM 경매회원 WHERE 이메일=?");
        try{
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                                                    new Object[] { 이메일 }, (rs, rowNum) -> MemberFactory.생성(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }


    @Override
    public long 추가(Member 회원) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("이름", 회원.get이름());
            paramMap.put("이메일", 회원.get이메일());
            paramMap.put("등록일시", LocalDateTime.now());
            paramMap.put("비밀번호", 회원.get비밀번호());

            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("경매회원")
                    .usingGeneratedKeyColumns("id");

            Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
            return newId.longValue();

        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public int 수정(Member 회원) {
        StringBuilder sbSql =  new StringBuilder("UPDATE 경매회원 ");
        sbSql.append("SET 이름=?, 이메일=?, 비밀번호=? ");
        sbSql.append("WHERE id=?");
        try {
            return this.jdbcTemplate.update(sbSql.toString(),
                    new Object[] { 회원.get이름(), 회원.get이메일(), 회원.get비밀번호(), 회원.getId() });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public int 삭제(long id) {
        StringBuilder sbSql =  new StringBuilder("DELETE FROM 경매회원 ");
        sbSql.append("WHERE id=?");
        try {
            return this.jdbcTemplate.update(sbSql.toString(),
                    new Object[] { id });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
