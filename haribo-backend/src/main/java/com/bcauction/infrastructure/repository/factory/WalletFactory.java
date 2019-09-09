package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletFactory
{
	public static Wallet 생성(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Wallet 지갑 = new Wallet();
		지갑.setId(rs.getLong("id"));
		지갑.set소유자id(rs.getLong("소유자id"));
		지갑.set주소(rs.getString("주소"));
		지갑.set잔액(rs.getBigDecimal("잔액"));
		지갑.set충전회수(rs.getInt("충전회수"));

		return 지갑;
	}
}
