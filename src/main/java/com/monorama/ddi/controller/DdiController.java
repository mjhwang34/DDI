package com.monorama.ddi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.ddi.model.DdiPoly;
import com.monorama.ddi.model.DdiSnp2;
import com.monorama.ddi.model.Message;
import com.monorama.ddi.model.PkResponse;
import com.monorama.ddi.model.ProteinResponse;
import com.monorama.ddi.service.DdiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/ddi") 
@Api(tags="DDI")
public class DdiController {
	private DdiService ddiService;
	@Autowired 
	public DdiController(DdiService ddiService) {
		this.ddiService = ddiService;
	}
	
	@GetMapping("/drugs")
	@ApiOperation(value="DDI의 약품 검색", notes="DDI의 약품을 검색한다. 키워드가 입력되면 해당 키워드를 포함하는 약품을 반환해준다.")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="약품 검색 성공", response=Message.class)
	})
	@ApiImplicitParam(name="keyword", value="키워드", example="ace")
	public Object drugs(@RequestParam("keyword") String keyword) {
		List<HashMap <String, Object>> drugList = ddiService.getDdiDrugListByKeyword(keyword);
		Message message = new Message();
		message.setData(drugList);
		return message;
	}
	
	@GetMapping("/pk")
	@ApiOperation(value="DDI의 약품간의 약동학적 지표 검색", notes="apiDI의 약동학적 지표의 변화를 나타낸다. 검색할 두가지의 약품(prepetrator, victim)이 입력되면 해당 약품들의 평가지표를 반환한다.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="약동학적 지표 검색 성공", response=PkResponse.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="pdrug", value="공격을 하는 약물", example="303"), @ApiImplicitParam(name="vdrug", value="영향을 받는 약물", example="1017")})
	public Object pk(@RequestParam("pdrug") int pdrugNum, @RequestParam("vdrug") int vdrugNum) {
		Message message = new Message();
		List <Object> resultList = new ArrayList<>();
		HashMap <String, Integer> info = new HashMap<>();
		info.put("pdrugNum", pdrugNum);
		info.put("vdrugNum", vdrugNum);
		PkResponse pkResponse = ddiService.getPkResponseByPdrugNumAndVdrugNum(info);
		if(pkResponse==null) {
			resultList.add(pkResponse);
			return message;
		}
		pkResponse.setPrepetrator_name(ddiService.getDrugNameByDrugNum(pdrugNum));
		pkResponse.setVictim_name(ddiService.getDrugNameByDrugNum(vdrugNum));
		if(pkResponse.getReference()==1) {
			pkResponse.setRef("인공지능 예측결과 입니다.");
		}
		else if(pkResponse.getReference()==0) {
			pkResponse.setRef("FDA 약품설명서에서 추출한 결과입니다.");
		}
		float foldValue = pkResponse.getFold()-1;
		foldValue = (float) (Math.round(foldValue*100)/100.0);
		pkResponse.setValue(Float.toString(foldValue));
		pkResponse.setValue_p(Math.abs((int)(foldValue*100)));
		String m;
		if(foldValue>0) {
			m = pkResponse.getPrepetrator_name()+"은 "+pkResponse.getVictim_name()+"의 AUC를 " + pkResponse.getValue_p()+"%만큼 증가시킴";  
		}
		else if(foldValue<0) {
			m = pkResponse.getPrepetrator_name()+"은 "+pkResponse.getVictim_name()+"의 AUC를 " + pkResponse.getValue_p()+"%만큼 감소시킴";
		}
		else {
			m = pkResponse.getPrepetrator_name()+"은 "+pkResponse.getVictim_name()+"의 AUC에 영향을 주지 않음.";
		}
		pkResponse.setDetail(m);
		resultList.add(pkResponse);
		message.setData(resultList);
		return message;
	}
	
	@GetMapping("/protein")
	@ApiOperation(value="DDI의 상호작용 관여 단백질 정보 검색", notes="DDI의 상호작용 관여 단백질 정보를 반환한다.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="상호작용 관여 단백질 정보 검색 성공", response=ProteinResponse.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="pdrug", value="공격을 하는 약물", example="196"), @ApiImplicitParam(name="vdrug", value="영향을 받는 약물", example="641")})
	public Object protein(@RequestParam("pdrug") int pdrugNum, @RequestParam("vdrug") int vdrugNum) {
		HashMap <String, Integer> info = new HashMap<>();
		info.put("pdrugNum", pdrugNum);
		info.put("vdrugNum", vdrugNum);
		List<DdiPoly> ddiPolyList = ddiService.getDdiPolyListByPdrugNumAndVdrugNum(info);
		
		List <ProteinResponse> resultList = new ArrayList<>();
		for(int i=0; i<ddiPolyList.size(); i++) {
			DdiPoly iDdiPoly = ddiPolyList.get(i);
			String iTypeFront = iDdiPoly.getType().substring(0, 2);
			String iTypeBack = iDdiPoly.getType().substring(3, 5);
			for(int j=i+1; j<ddiPolyList.size(); j++) {
				DdiPoly jDdiPoly = ddiPolyList.get(j);
				if(iDdiPoly.getProtein().equals(jDdiPoly.getProtein())==false) {
					break;
				}
				String jTypeFront = jDdiPoly.getType().substring(0, 2);
				String jTypeBack = jDdiPoly.getType().substring(3, 5);
				if(iDdiPoly.getDrug_n()!=jDdiPoly.getDrug_n() && iTypeFront.equals(jTypeFront)) {
					ProteinResponse proteinResponse = new ProteinResponse();
					if((("ih").equals(iTypeBack) && ("ss").equals(jTypeBack)) || (("ss").equals(iTypeBack) && ("ih").equals(jTypeBack))) {
						proteinResponse.setP("inhibitor");
						proteinResponse.setV("substrate");
						proteinResponse.setProtein(iDdiPoly.getProtein());
						proteinResponse.setPolyname(iDdiPoly.getPolyname());
						resultList.add(proteinResponse);
					}
					else if((("id").equals(iTypeBack) && ("ss").equals(jTypeBack)) || (("ss").equals(iTypeBack) && ("id").equals(jTypeBack))) {
						proteinResponse.setP("inducer");
						proteinResponse.setV("substrate");
						proteinResponse.setProtein(iDdiPoly.getProtein());
						proteinResponse.setPolyname(iDdiPoly.getPolyname());
						resultList.add(proteinResponse);
					}
				}
			}
		}
		Message message = new Message();
		message.setData(resultList);
		return message;
	}
	
	@GetMapping("/gene")
	@ApiOperation(value="DDI의 한국인 유전자 특이빈도 검색", notes="DDI의 victim 약물의 단백질 리스트에 해당하는 정보를 반환한다.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="한국인 유전자 특이빈도 검색 성공", response=DdiSnp2.class)
	})
	@ApiImplicitParam(name="vdrug", value="영향을 받는 약물", example="641")
	public Object gene(@RequestParam("vdrug") int vdrugNum) {
		List<DdiSnp2> ddiSnp2List = ddiService.getDdiSnp2BydrugNum(vdrugNum);
		Message message = new Message();
		message.setData(ddiSnp2List);
		return message;
	}
	
	@GetMapping("/substitution")
	@ApiOperation(value="DDI의 추천 약물 쌍 검색", notes="Prepetrator, victim약물과 일치하는 약물 리스트로 pk정보 검색하여 반환한다. 대체 약물 리스트를 반환한다")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="추천 약물 쌍 검색 성공", response=PkResponse.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="pdrug", value="공격을 하는 약물", example="582"), @ApiImplicitParam(name="vdrug", value="영향을 받는 약물", example="864")})
	public Object substitution(@RequestParam("pdrug") int pdrugNum, @RequestParam("vdrug") int vdrugNum) {
		List <Object> resultList = new ArrayList<>();
		List<String> pdrugAtc = ddiService.getDrugAtcByDrugNum(pdrugNum);
		List<String> vdrugAtc = ddiService.getDrugAtcByDrugNum(vdrugNum);
		
		List <Integer> pdrugNumListByAtc = new ArrayList<>();
		for(String val: pdrugAtc) {
			List <Integer> temp = ddiService.getDrugNumByAtc(val);
			pdrugNumListByAtc.addAll(temp);
		}
		Set<Integer> pdrugSet = new HashSet<Integer>(pdrugNumListByAtc); // 중복제거
		List<Integer> pdrugList =new ArrayList<Integer>(pdrugSet);
		
		List <Integer> vdrugNumListByAtc = new ArrayList<>();
		for(String val: vdrugAtc) {
			List <Integer> temp = ddiService.getDrugNumByAtc(val);
			vdrugNumListByAtc.addAll(temp);
		}
		Set<Integer> vdrugSet = new HashSet<Integer>(vdrugNumListByAtc); // 중복제거
		List<Integer> vdrugList =new ArrayList<Integer>(vdrugSet);
		
		for(int pdrugN: pdrugList) {
			for(int vdrugN: vdrugList) {
				HashMap <String, Integer> info = new HashMap<>();
				info.put("pdrugNum", pdrugN);
				info.put("vdrugNum", vdrugN);
				PkResponse pkResponse = ddiService.getPkResponseByPdrugNumAndVdrugNum(info);
				if(pkResponse==null) {
					continue;
				}
				pkResponse.setPrepetrator_name(ddiService.getDrugNameByDrugNum(pdrugN));
				pkResponse.setVictim_name(ddiService.getDrugNameByDrugNum(vdrugN));
				if(pkResponse.getReference()==1) {
					pkResponse.setRef("인공지능 예측결과 입니다.");
				}
				else if(pkResponse.getReference()==0) {
					pkResponse.setRef("FDA 약품설명서에서 추출한 결과입니다.");
				}
				float foldValue = pkResponse.getFold()-1;
				foldValue = (float) (Math.round(foldValue*100)/100.0);
				pkResponse.setValue(Float.toString(foldValue));
				pkResponse.setValue_p(Math.abs((int)(foldValue*100)));
				String m;
				if(foldValue>0) {
					m = pkResponse.getPrepetrator_name()+"은 "+pkResponse.getVictim_name()+"의 AUC를 " + pkResponse.getValue_p()+"%만큼 증가시킴";  
				}
				else if(foldValue<0) {
					m = pkResponse.getPrepetrator_name()+"은 "+pkResponse.getVictim_name()+"의 AUC를 " + pkResponse.getValue_p()+"%만큼 감소시킴";
				}
				else {
					m = pkResponse.getPrepetrator_name()+"은 "+pkResponse.getVictim_name()+"의 AUC에 영향을 주지 않음.";
				}
				pkResponse.setDetail(m);
				resultList.add(pkResponse);
			}
		}
		
		Message message = new Message();
		message.setData(resultList);
		return message;
	}
	

}