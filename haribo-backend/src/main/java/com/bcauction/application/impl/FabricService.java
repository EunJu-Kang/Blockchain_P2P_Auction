package com.bcauction.application.impl;

import com.bcauction.application.IFabricCCService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.domain.repository.IOwnershipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FabricService implements IFabricService {
	private static final Logger logger = LoggerFactory.getLogger(FabricService.class);

	@Autowired
	private IFabricCCService fabricCCService;
	private IOwnershipRepository ownershipRepository;
	private IDigitalWorkRepository digitalWorkRepository;

	@Autowired
	public FabricService(IOwnershipRepository ownershipRepository, IDigitalWorkRepository digitalWorkRepository,
			IFabricCCService fabricCCService) {
		this.ownershipRepository = ownershipRepository;
		this.digitalWorkRepository = digitalWorkRepository;
		this.fabricCCService = fabricCCService;
	}

	@Override
	public Ownership 소유권등록(final long 소유자, final long 작품id) {
		FabricAsset asset = this.fabricCCService.registerOwnership(소유자, 작품id);
		if (asset == null)
			return null;

		Ownership 소유권 = new Ownership();
		소유권.set소유자id(소유자);
		소유권.set작품id(작품id);
		소유권.set소유시작일자(asset.getCreatedAt());

		long result = this.ownershipRepository.생성(소유권);
		if (result == 0)
			return null;

		Ownership 조회된소유권 = this.ownershipRepository.조회(소유자, 작품id);
		return 조회된소유권;
	}

	@Override
	public Ownership 소유권이전(final long from, final long to, final long 작품id) {
		List<FabricAsset> assets = this.fabricCCService.transferOwnership(from, to, 작품id);
		if (assets == null)
			return null;

		Ownership 소멸소유권 = this.ownershipRepository.조회(from, 작품id);
		if (소멸소유권 == null)
			return null;

		소멸소유권.set소유종료일자(assets.get(0).getExpiredAt());
		long result = this.ownershipRepository.수정(소멸소유권);
		if (result == 0)
			return null;

		DigitalWork 작품정보 = this.digitalWorkRepository.조회(작품id);
		if (작품정보.get회원id() != from)
			return null;

		작품정보.set회원id(to);
		result = this.digitalWorkRepository.수정(작품정보);
		if (result == 0)
			return null;

		Ownership 새소유권 = new Ownership();
		새소유권.set소유자id(to);
		새소유권.set작품id(작품id);
		새소유권.set소유시작일자(assets.get(1).getCreatedAt());

		result = this.ownershipRepository.생성(새소유권);
		if (result == 0)
			return null;

		Ownership os = this.ownershipRepository.조회(to, 작품id);
		return os;
	}

	@Override
	public Ownership 소유권소멸(final long 소유자id, final long 작품id) {
		FabricAsset asset = this.fabricCCService.expireOwnership(작품id, 소유자id);
		if (asset == null)
			return null;

		Ownership 소멸소유권 = this.ownershipRepository.조회(소유자id, 작품id);
		if (소멸소유권 == null)
			return null;

		소멸소유권.set소유종료일자(asset.getExpiredAt());
		long result = this.ownershipRepository.수정(소멸소유권);
		if (result == 0)
			return null;

		return 소멸소유권;
	}

	@Override
	public List<FabricAsset> 작품이력조회(final long id) {
		List<FabricAsset> history = this.fabricCCService.queryHistory(id);
		if (history == null)
			return null;

		for (int i = 0; i < history.size(); i++) {
			if (history.get(i).getCreatedAt() == null || history.get(i).getExpiredAt() != null) {
				history.remove(i);
				i--;
			}
		}

		return history;
	}

	@Override
	public List<Ownership> 소유자별조회(final long id) {
		List<Ownership> 소유자별조회 = this.ownershipRepository.소유자별목록조회(id);
		if (소유자별조회 == null)
			return null;

		return 소유자별조회;
	}
}
