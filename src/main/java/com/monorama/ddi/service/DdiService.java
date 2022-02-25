package com.monorama.ddi.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monorama.ddi.mapper.MainMapper;
import com.monorama.ddi.model.DdiPoly;
import com.monorama.ddi.model.DdiSnp2;
import com.monorama.ddi.model.PkResponse;

@Service
public class DdiService {
	private final MainMapper mainMapper;
	
	@Autowired
	public DdiService(MainMapper mainMapper) {this.mainMapper = mainMapper; }
	
	public List<HashMap<String, Object>> getDdiDrugListByKeyword(String keyword){
		return mainMapper.getDdiDrugListByKeyword(keyword);
	}
	
	public int getDrugNumByDrugSeq(int drugSeq) {
		return mainMapper.getDrugNumByDrugSeq(drugSeq);
	}
	
	public PkResponse getPkResponseByPdrugNumAndVdrugNum(HashMap <String, Integer> info) {
		return mainMapper.getPkResponseByPdrugNumAndVdrugNum(info);
	}
	
	public String getDrugNameByDrugNum(int drugNum) {
		return mainMapper.getDrugNameByDrugNum(drugNum);
	}
	
	public List<DdiPoly> getDdiPolyListByPdrugNumAndVdrugNum(HashMap <String, Integer> info){
		return mainMapper.getDdiPolyListByPdrugNumAndVdrugNum(info);
	}
	
	public List<DdiSnp2> getDdiSnp2BydrugNum(int drugNum) {
		return mainMapper.getDdiSnp2BydrugNum(drugNum);
	}
	
	public List<String> getDrugAtcByDrugNum(int drugNum){
		return mainMapper.getDrugAtcByDrugNum(drugNum);
	}
	
	public List<Integer> getDrugNumByAtc(String atc){
		return mainMapper.getDrugNumByAtc(atc);
	}

}