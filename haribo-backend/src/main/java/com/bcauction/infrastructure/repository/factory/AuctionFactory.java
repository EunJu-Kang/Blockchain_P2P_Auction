package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Auction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionFactory
{
	public static Auction 생성(ResultSet rs) throws SQLException
	{
		if(rs == null) return null;
		Auction 경매 = new Auction();

		경매.setId(rs.getInt("id"));
		경매.set경매생성자id(rs.getLong("경매생성자id"));
		경매.set경매작품id(rs.getLong("경매작품id"));
		경매.set생성일시(rs.getTimestamp("생성일시").toLocalDateTime());
		경매.set시작일시(rs.getTimestamp("시작일시").toLocalDateTime());
		경매.set종료일시(rs.getTimestamp("종료일시").toLocalDateTime());
		경매.set상태(rs.getString("상태"));
		경매.set최저가(rs.getBigDecimal("최저가").toBigInteger());
		경매.set컨트랙트주소(rs.getString("컨트랙트주소"));

		return 경매;
	}
}
