package com.bcauction.application.impl;

import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DigitalWorkService implements IDigitalWorkService {
	public static final Logger logger = LoggerFactory.getLogger(DigitalWorkService.class);

	private IDigitalWorkRepository digitalWorkRepository;
	private IFabricService fabricService;

	@Autowired
	public DigitalWorkService(IFabricService fabricService, IDigitalWorkRepository digitalWorkRepository) {
		this.fabricService = fabricService;
		this.digitalWorkRepository = digitalWorkRepository;
	}

	@Override
	public List<DigitalWork> 목록조회() {
		return this.digitalWorkRepository.목록조회();
	}

	@Override
	public List<DigitalWork> 사용자작품목록조회(final long id) {
		return this.digitalWorkRepository.사용자작품목록조회(id);
	}

	@Override
	public DigitalWork 조회(final long id) {
		return this.digitalWorkRepository.조회(id);
	}

	/**
	 * 작품 등록 시 작품 정보를 저장하고 패브릭에 작품 소유권을 등록한다.
	 * 
	 * @param 작품
	 * @return DigitalWork
	 */
	@Override
	public DigitalWork 작품등록(final DigitalWork 작품) {
		// TODO

		long 작품id = this.digitalWorkRepository.추가(작품);
		Ownership 소유권 = this.fabricService.소유권등록(작품.get회원id(), 작품id);

		return 작품;
	}

	/**
	 * 작품 삭제 시, 작품의 상태를 업데이트하고 패브릭에 작품 소유권 소멸 이력을 추가한다.
	 * 
	 * @param id 작품id
	 * @return DigitalWork
	 */
	@Override
	public DigitalWork 작품삭제(final long id) {
		DigitalWork deleteDigita = null;
		deleteDigita = this.digitalWorkRepository.조회(id);
		System.out.println("deleteDigita1: "+deleteDigita.toString());
		if (deleteDigita != null) {
			this.digitalWorkRepository.삭제(id);
			deleteDigita = this.digitalWorkRepository.조회(id);
			this.fabricService.소유권소멸(deleteDigita.get회원id(), deleteDigita.getId());
		}
		System.out.println("deleteDigita2: "+deleteDigita.toString());
		return deleteDigita;
	}

	@Override
	public DigitalWork 작품정보수정(final DigitalWork 작품) {
		DigitalWork workStored = this.digitalWorkRepository.조회(작품.getId());
		if (workStored == null)
			throw new ApplicationException("해당 작품을 찾을 수 없습니다.");

		if (작품.get회원id() != 0 && 작품.get회원id() != workStored.get회원id())
			throw new ApplicationException("잘못된 접근입니다.");

		if (작품.get이름() == null || "".equals(작품.get이름()))
			작품.set이름(workStored.get이름());
		if (작품.get설명() == null || "".equals(작품.get설명()))
			작품.set설명(workStored.get설명());
		if (작품.get공개여부() == null || "".equals(작품.get공개여부()))
			작품.set공개여부(workStored.get공개여부());
		if (작품.get상태() == null || "".equals(작품.get상태()))
			작품.set상태(workStored.get상태());
		if (작품.get회원id() == 0)
			작품.set회원id(workStored.get회원id());

		int affected = this.digitalWorkRepository.수정(작품);
		
		if (affected == 0)
			throw new ApplicationException("작품정보수정 처리가 반영되지 않았습니다.");

		return 작품;
	}

}
