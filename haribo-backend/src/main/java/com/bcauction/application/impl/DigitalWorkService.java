package com.bcauction.application.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.infrastructure.repository.AuctionRepository;

@Service
public class DigitalWorkService implements IDigitalWorkService {
	public static final Logger logger = LoggerFactory.getLogger(DigitalWorkService.class);

	private IDigitalWorkRepository digitalWorkRepository;
	private IFabricService fabricService;
	private IAuctionRepository auctionRepository;

	@Autowired
	public DigitalWorkService(IFabricService fabricService, IDigitalWorkRepository digitalWorkRepository,
			IAuctionRepository auctionRepository) {
		this.fabricService = fabricService;
		this.digitalWorkRepository = digitalWorkRepository;
		this.auctionRepository = auctionRepository;
	}

	@Override
	public List<DigitalWork> 목록조회() {
		List<DigitalWork> list = this.digitalWorkRepository.목록조회();
		for (int i = 0; i < list.size(); i++) {
			String imageString = encodeBase64(list.get(i));
			list.get(i).set작품이미지(imageString);
		}
		return list;
	}

	@Override
	public List<DigitalWork> 사용자작품목록조회(final long id) {
		List<DigitalWork> list = this.digitalWorkRepository.사용자작품목록조회(id);
		for (int i = 0; i < list.size(); i++) {
			String imageString = encodeBase64(list.get(i));
			list.get(i).set작품이미지(imageString);
		}
		return list;
	}

	@Override
	public DigitalWork 조회(final long id) {
		DigitalWork select = this.digitalWorkRepository.조회(id);
		String imageString = encodeBase64(select);
		select.set작품이미지(imageString);
		return select;
	}

	@Override
	public DigitalWork 조회(String name) {
		return this.digitalWorkRepository.조회(name);
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

		Decoder decoder = Base64.getDecoder();
		String data = 작품.get작품이미지().split(",")[1];
		byte[] decodedBytes = decoder.decode(data);
		String path = "artImage/" + 작품.get이름() + ".jpg";
		File file = new File(path);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			outputStream.write(decodedBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		List<Auction> workAucions = this.auctionRepository.작품경매목록조회(id);

		if (workAucions.size() != 0) {
			return null;
		}
		
		deleteDigita = this.digitalWorkRepository.조회(id);
		if (deleteDigita != null) {
			this.digitalWorkRepository.삭제(id);
			deleteDigita = this.digitalWorkRepository.조회(id);
			this.fabricService.소유권소멸(deleteDigita.get회원id(), deleteDigita.getId());
			File file = new File("artImage/"+deleteDigita.get이름()+".jpg");
			if(file.exists()) {
				file.delete();
			}
		}

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

	public String encodeBase64(DigitalWork artWork) {

		String imageString = "data:image/jpg;base64,";
		File f = new File("artImage/" + artWork.get이름() + ".jpg");
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			byte byteArray[] = new byte[(int) f.length()];
			fis.read(byteArray);
			Encoder encoder = Base64.getEncoder();
			imageString += encoder.encodeToString(byteArray);
			fis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return imageString;
	}

	@Override
	public List<DigitalWork> 작품검색조회(String str) {
		List<DigitalWork> allWork = this.목록조회();
		List<DigitalWork> searched = new ArrayList<>();

		for (int i = 0; i < allWork.size(); i++) {
			if (allWork.get(i).get설명().contains(str) || allWork.get(i).get이름().contains(str)) {
				searched.add(allWork.get(i));
			}
		}
		return searched;
	}

}
