package com.monorama.ddi.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.monorama.ddi.model.ActionSession;
import com.monorama.ddi.model.DdiPoly;
import com.monorama.ddi.model.DdiSnp2;
import com.monorama.ddi.model.PkResponse;


@Mapper
public interface MainMapper {
	String getLoginResult(HashMap <String, String> loginInfo);
	int insertActionSession(ActionSession actionSession);
	ActionSession getActionSessionByKey(HashMap <String, Object> info);
	int getUserSeqByLocale(String locale);
	
	List<HashMap<String, Object>> getDfiFoodListByKeyword(String keyword);
	List<HashMap <String, Object>> getDfiFoodListByDrugSeq(int drug_seq);
	List<HashMap<String, Object>> getDfiDrugListByKeyword(String keyword);
	List<HashMap <String, Object>> getDfiDrugListByFoodSeq(int food_seq);
	List<HashMap<String, Object>> getDfiAndPaperByFoodSeqAndDrugSeq(HashMap <String, Integer> info);
	
	List<HashMap<String, Object>> getDdiDrugListByKeyword(String keyword);
	int getDrugNumByDrugSeq(int drugSeq);
	List<PkResponse> getPkResponseByPdrugNumAndVdrugNum(HashMap <String, Integer> info);
	String getDrugNameByDrugNum(int drugNum);
	List<DdiPoly> getDdiPolyListByPdrugNumAndVdrugNum(HashMap <String, Integer> info);
	List<DdiSnp2> getDdiSnp2BydrugNum(int drugNum);
	List<String> getDrugAtcByDrugNum(int drugNum);
	List<Integer> getDrugNumByAtc(String atc);
}
