package com.bcauction.domain.repository;

import com.bcauction.domain.DigitalWork;

import java.util.List;

public interface IDigitalWorkRepository
{
	List<DigitalWork> 목록조회();
	DigitalWork 조회(long id);
	DigitalWork 조회(final long 회원id, final String 이름);
	long 추가(DigitalWork 작품);
	int 수정(DigitalWork 작품);
	int 삭제(long id);

	List<DigitalWork> 사용자작품목록조회(long id);
}
