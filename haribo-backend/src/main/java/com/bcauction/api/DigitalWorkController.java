package com.bcauction.api;


import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class DigitalWorkController
{
	public static final Logger logger = LoggerFactory.getLogger(DigitalWorkController.class);

	private IDigitalWorkService digitalWorkService;
	private IFabricService fabricService;

	@Autowired
	public DigitalWorkController(IDigitalWorkService digitalWorkService,
	                             IFabricService fabricService) {
		Assert.notNull(digitalWorkService, "digitalWorkService 개체가 반드시 필요!");
		Assert.notNull(fabricService, "fabricService 개체가 반드시 필요!");
		this.digitalWorkService = digitalWorkService;
		this.fabricService = fabricService;
	}

	@RequestMapping(value = "/works", method = RequestMethod.POST)
	public DigitalWork 등록(@RequestBody DigitalWork work) {
		return  digitalWorkService.작품등록(work);
	}


	@RequestMapping(value = "/works", method = RequestMethod.GET)
	public List<DigitalWork> 목록조회() {
		List<DigitalWork> 목록 = digitalWorkService.목록조회();

		if (목록 == null || 목록.isEmpty())
			throw new EmptyListException("NO DATA");

		return 목록;
	}

	@RequestMapping(value = "/works/{id}", method = RequestMethod.GET)
	public DigitalWork 조회(@PathVariable int id) {
		DigitalWork 작품 = digitalWorkService.조회(id);
		if (작품 == null) {
			logger.error("NOT FOUND ID: ", id);
			throw new NotFoundException(id + " 작품 정보를 찾을 수 없습니다.");
		}

		return 작품;
	}

	@RequestMapping(value = "/works", method = RequestMethod.PUT)
	public DigitalWork 수정(@RequestBody DigitalWork work) {
		DigitalWork 수정된작품 = digitalWorkService.작품정보수정(work);
		if (수정된작품 == null) {
			logger.error("NOT FOUND WORK ID: ", work.getId());
			throw new NotFoundException(work.getId() + " 작품 정보를 찾을 수 없습니다.");
		}

		return 수정된작품;
	}

	@RequestMapping(value = "/works/{id}", method = RequestMethod.DELETE)
	public DigitalWork 삭제(@PathVariable int id) {
		return  digitalWorkService.작품삭제(id);
	}


	// TODO : not need to use the function below.
	/**
	 * 협업과제
	 * week. 4-7
	 * mission. 3
	 * Req. 1-1
	 * @param id
	 * @return
	 */
	public List<DigitalWork> 사용자별작품목록조회(@PathVariable int id){
		List<DigitalWork> 목록 = digitalWorkService.사용자작품목록조회(id);

		if (목록 == null || 목록.isEmpty() )
			throw new EmptyListException("사용자 소유의 작품이 없습니다.");

		return 목록;
	}

	/**
	 * 협업과제
	 * 협업과제
	 * week. 4-7
	 * mission. 3
	 * Req. 1-2
	 */
	public List<FabricAsset> 작품이력조회(@PathVariable int id){
		List<FabricAsset> history = this.fabricService.작품이력조회(id);
		if(history == null || history.isEmpty())
			throw new EmptyListException("조회된 작품 이력이 없습니다.");

		return history;
	}

}
