package com.monorama.ddi.controller;
import java.util.List;
import java.util.Map;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.ddi.exception.DuplicatedException;
import com.monorama.ddi.exception.NotFoundException;
import com.monorama.ddi.model.Currency;
import com.monorama.ddi.model.Message;
import com.monorama.ddi.service.CurrencyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("main")
@CrossOrigin(origins = "http://localhost") // 컨트롤러에서 설정
public class CurrencyController {
	
	private CurrencyService currencyService;
	
	@Autowired  // spring 4.3 버전 이상부터는 생략 가능
	public CurrencyController(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}
	
	@GetMapping("old_currencies") // 전체 조회
	public Object old_currencyList() {
		log.debug("조회 시작");
		List<Currency> currencyList = currencyService.getCurrencyList();
		
		return currencyList;
	}
	
	
	@GetMapping("currencies") // 전체 조회
	public Object getCurrencyList() {
		log.debug("조회 시작");
		List<Currency> currencyList = currencyService.getCurrencyList();
		
		Message message = new Message();
		message.setData(currencyList);
		
		if(message.getData()==null) {
			throw new NotFoundException();
		}
		return message;
	}
	
	@GetMapping("currencies/{num}")
	public Object getCurrencyByNum(@PathVariable("num") int num) {
		log.debug("1개 조회 시작");
		Currency currency = currencyService.getCurrencyByNum(num);
		
		if(currency==null) {
			throw new NotFoundException();
		}
		
		Message message = new Message();
		message.setData(currency);
		
		return message;
	}
	
	@PutMapping("currencies/{num}")
	public Object currencyUpdate(@PathVariable("num") int num, @RequestBody Currency body) {
		log.debug("수정 시작");
		Currency currency = new Currency();
		currency.setNum(num);
		currency.setMemo(body.getMemo());
		int result = currencyService.currencyUpdate(currency);
		if(result==0) {
			throw new NotFoundException();
		}
		Message message = new Message();
		message.setData(result);
		return message;
	}
	
	@DeleteMapping("currencies/{num}") //삭제
	public Object currencyDelete(@PathVariable("num") int num) {
		log.debug("삭제 시작");
		log.debug("currency num = {}", num);
		
		Currency _currency = currencyService.getCurrencyByNum(num);
		log.debug("{}", _currency);
		
		if(_currency == null) {
			throw new NotFoundException();
		}
		
		int result = currencyService.currencyDelete(num);
		log.debug("here");
		Message message = new Message();
		message.setData(result);
		return message;
	}
	
	@PostMapping("currencies") //추가
	public Object currencyInsert(@RequestBody Currency currency) {
		log.debug("추가 시작");
		log.debug("{}", currency);	
		Currency _currency = currencyService.getCurrencyByNum(currency.getNum());
		if(_currency != null) {
			throw new DuplicatedException();
		}
		
		Message message = new Message();
		int result = currencyService.currencyInsert(currency);
		message.setData(result);
		
		if(result == 0) {
			throw new NotFoundException();
		}
		
		return message;
	}
}