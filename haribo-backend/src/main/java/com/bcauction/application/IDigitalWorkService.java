package com.bcauction.application;

import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IDigitalWorkService
{
	List<DigitalWork> 작품검색조회(String str);
	List<DigitalWork> 목록조회();
	List<DigitalWork> 사용자작품목록조회(long id);
	DigitalWork 조회(long id);
	DigitalWork 조회(String name);

	@Transactional
	DigitalWork 작품등록(DigitalWork 작품);

	@Transactional
	DigitalWork 작품정보수정(DigitalWork 작품);

	@Transactional
	DigitalWork 작품삭제(long id);
}
