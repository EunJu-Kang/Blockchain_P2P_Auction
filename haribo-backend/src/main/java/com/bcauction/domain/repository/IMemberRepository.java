package com.bcauction.domain.repository;

import com.bcauction.domain.Member;

import java.util.List;

public interface IMemberRepository {
    List<Member> 목록조회();
    Member 조회(long id);
    Member 조회(String 이메일);
    long 추가(Member 회원);
    int 수정(Member 회원);
    int 삭제(long id);
}
