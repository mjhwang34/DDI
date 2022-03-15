package com.monorama.ddi.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monorama.ddi.mapper.MainMapper;

@Service
public class DfiService {
	private final MainMapper mainMapper;
	
	@Autowired
	public DfiService(MainMapper mainMapper) { this.mainMapper=mainMapper;}
	
	public List<HashMap<String, Object>> getDfiFoodListByKeyword(String keyword) {
		return mainMapper.getDfiFoodListByKeyword(keyword);
	}
	
	public List<HashMap <String, Object>> getDfiFoodListByDrugSeq(int drug_seq){
		return mainMapper.getDfiFoodListByDrugSeq(drug_seq);
	}
	
	public List<HashMap <String, Object>> getDfiDrugListByKeyword(String keyword){
		return mainMapper.getDfiDrugListByKeyword(keyword);
	}
	public List<HashMap <String, Object>> getDfiDrugListByFoodSeq(int food_seq){
		return mainMapper.getDfiDrugListByFoodSeq(food_seq);
	}
	public List<HashMap<String, Object>> getDfiAndPaperByFoodSeqAndDrugSeq(HashMap <String, Integer> info) {
		return mainMapper.getDfiAndPaperByFoodSeqAndDrugSeq(info);
	}
}
