package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberFactory {

    public static Member 생성(ResultSet rs) throws SQLException {
        if (rs == null) return null;
        Member 회원 = new Member();
        회원.setId(rs.getLong("id"));
        회원.set이름(rs.getString("이름"));
        회원.set이메일(rs.getString("이메일"));
        회원.set비밀번호(rs.getString("비밀번호"));
        회원.set등록일시(rs.getTimestamp("등록일시").toLocalDateTime());

        return 회원;
    }
}
