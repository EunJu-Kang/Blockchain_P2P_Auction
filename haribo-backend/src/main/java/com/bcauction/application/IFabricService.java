package com.bcauction.application;

import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.Ownership;

import java.util.List;

public interface IFabricService {
	Ownership 소유권등록(long owner, long 작품id);
	Ownership 소유권이전(long from, long to, long 작품);
	Ownership 소유권소멸(long 소유자, long 작품);

	List<Ownership> 소유자별조회(long id);
	List<FabricAsset> 작품이력조회(long id);
}
