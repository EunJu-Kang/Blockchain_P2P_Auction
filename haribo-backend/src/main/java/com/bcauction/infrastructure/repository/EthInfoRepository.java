package com.bcauction.infrastructure.repository;

import com.bcauction.domain.EthInfo;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IEthInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EthInfoRepository implements IEthInfoRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public EthInfo get(String ethUrl) {
        String sql =  "SELECT * FROM ETH_INFO WHERE net_url=?";
        try {
            return this.jdbcTemplate.queryForObject(sql,
                    new Object[] { ethUrl }, (rs, rowNum) -> new EthInfo(rs.getString("net_url"),
                                                                    rs.getString("latest_bno")) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public void put(String ethUrl, String blockNumber) {
        String sql =  "UPDATE ETH_INFO SET latest_bno=? WHERE net_url=?";
        try {
            this.jdbcTemplate.update(sql,
                    new Object[] { blockNumber, ethUrl });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
