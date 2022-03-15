package com.monorama.ddi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monorama.ddi.mapper.MainMapper;
import com.monorama.ddi.model.Currency;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrencyService {
	private final MainMapper mainMapper;
	
	@Autowired
	public CurrencyService(MainMapper mainMapper) { this.mainMapper=mainMapper;}
	public List<Currency> getCurrencyList(){
		return mainMapper.getCurrencyList();
	}
	
	public Currency getCurrencyByNum(int num) {
		return mainMapper.getCurrencyByNum(num);
	}
	
	@Transactional
	public int currencyUpdate(Currency currency) {
		log.debug("currency id = {}", currency.getNum());
		return mainMapper.currencyUpdate(currency);
	}
	
	@Transactional
	public int currencyDelete(int num) {
		return mainMapper.currencyDelete(num);
	}
	
	@Transactional
	public int currencyInsert(Currency currency) {
		return mainMapper.currencyInsert(currency);
	}
}
