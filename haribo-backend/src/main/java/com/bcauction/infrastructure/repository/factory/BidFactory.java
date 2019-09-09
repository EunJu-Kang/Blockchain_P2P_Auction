package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Bid;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BidFactory
{
	public static Bid 생성(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Bid 입찰 = new Bid();
		입찰.setId(rs.getLong("id"));
		입찰.set경매id(rs.getLong("경매id"));
		입찰.set경매참여자id(rs.getLong("경매참여자id"));
		입찰.set입찰일시(rs.getTimestamp("입찰일시").toLocalDateTime());
		입찰.set입찰금액(rs.getBigDecimal("입찰금액"));
		입찰.set낙찰여부(rs.getString("낙찰여부"));

		return 입찰;
	}
}
