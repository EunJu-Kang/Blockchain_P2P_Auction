package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Ownership;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OwnershipFactory
{
	public static Ownership 생성(ResultSet rs) throws SQLException
	{
		if(rs == null) return null;
		Ownership 소유권 = new Ownership();

		소유권.setId(rs.getLong("id"));
		소유권.set소유자id(rs.getLong("소유자id"));
		소유권.set작품id(rs.getLong("작품id"));
		if(rs.getString("소유시작일자") != null)
			소유권.set소유시작일자(LocalDateTime.parse(rs.getString("소유시작일자")));
		if(rs.getString("소유종료일자") != null)
			소유권.set소유종료일자(LocalDateTime.parse(rs.getString("소유종료일자")));

		return 소유권;
	}
}
