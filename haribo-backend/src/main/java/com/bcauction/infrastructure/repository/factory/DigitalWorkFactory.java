package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.DigitalWork;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DigitalWorkFactory
{
	public static DigitalWork 생성(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		DigitalWork 작품 = new DigitalWork();
		작품.setId(rs.getLong("id"));
		작품.set회원id(rs.getLong("회원id"));
		작품.set이름(rs.getString("이름"));
		작품.set설명(rs.getString("설명"));
		작품.set상태(rs.getString("상태"));
		작품.set공개여부(rs.getString("공개여부"));

		return 작품;
	}
}
