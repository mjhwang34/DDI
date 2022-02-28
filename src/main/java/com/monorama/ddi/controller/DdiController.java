package com.monorama.ddi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.ddi.PkMessage;
import com.monorama.ddi.Utils;
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
	@ApiOperation(value="DDI�� ��ǰ �˻�", notes="DDI�� ��ǰ�� �˻��Ѵ�. Ű���尡 �ԷµǸ� �ش� Ű���带 �����ϴ� ��ǰ�� ��ȯ���ش�.")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="��ǰ �˻� ����", response=Message.class)
	})
	@ApiImplicitParam(name="keyword", value="Ű����", example="ace")
	public Object drugs(@RequestParam("keyword") String keyword) {
		List<HashMap <String, Object>> drugList = ddiService.getDdiDrugListByKeyword(keyword);
		Message message = new Message();
		message.setData(drugList);
		return message;
	}
	
	@GetMapping("/pk")
	@ApiOperation(value="DDI�� ��ǰ���� �ൿ���� ��ǥ �˻�", notes="apiDI�� �ൿ���� ��ǥ�� ��ȭ�� ��Ÿ����. �˻��� �ΰ����� ��ǰ(prepetrator, victim)�� �ԷµǸ� �ش� ��ǰ���� ����ǥ�� ��ȯ�Ѵ�.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="�ൿ���� ��ǥ �˻� ����", response=PkResponse.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="pdrug", value="������ �ϴ� �๰", example="303"), @ApiImplicitParam(name="vdrug", value="������ �޴� �๰", example="1017")})
	public Object pk(@RequestParam("pdrug") int pdrugNum, @RequestParam("vdrug") int vdrugNum, @RequestHeader(value="locale") String locale) {
		Message message = new Message();
		HashMap <String, Integer> info = new HashMap<>();
		info.put("pdrugNum", pdrugNum);
		info.put("vdrugNum", vdrugNum);
		List<PkResponse> pkResponse = ddiService.getPkResponseByPdrugNumAndVdrugNum(info);
		if(pkResponse==null) {
			message.setData(pkResponse);
			return message;
		}
		String prepetratorName = ddiService.getDrugNameByDrugNum(pdrugNum);
		String victimName = ddiService.getDrugNameByDrugNum(vdrugNum);
	
		
		for(int i=0; i<pkResponse.size(); i++) {
			PkResponse item = new PkResponse();
			item.setPrepetrator_name(prepetratorName);
			item.setVictim_name(victimName);
			
			float foldValue = pkResponse.get(i).getFold()-1;
			foldValue = (float) (Math.round(foldValue*100)/100.0);
			item.setValue(Float.toString(foldValue));
			String value = Float.toString(foldValue);
			item.setValue_p(Math.abs((int)(foldValue*100)));
			String value_p = Integer.toString(Math.abs((int)(foldValue*100)));
			String fold = Float.toString(foldValue+1);
			
			PkMessage pkMessage = new PkMessage(locale, prepetratorName, victimName, value_p, fold);
			int reference = pkResponse.get(i).getReference();
			item.setRef(Utils.findReferenceMessage(reference, pkMessage));
			item.setDetail(Utils.findDetailMessage(foldValue, pkMessage));
			pkResponse.set(i, item);
		}
		message.setData(pkResponse);
		return message;
	}
	
	
	@GetMapping("/protein")
	@ApiOperation(value="DDI�� ��ȣ�ۿ� ���� �ܹ��� ���� �˻�", notes="DDI�� ��ȣ�ۿ� ���� �ܹ��� ������ ��ȯ�Ѵ�.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="��ȣ�ۿ� ���� �ܹ��� ���� �˻� ����", response=ProteinResponse.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="pdrug", value="������ �ϴ� �๰", example="196"), @ApiImplicitParam(name="vdrug", value="������ �޴� �๰", example="641")})
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
	@ApiOperation(value="DDI�� �ѱ��� ������ Ư�̺� �˻�", notes="DDI�� victim �๰�� �ܹ��� ����Ʈ�� �ش��ϴ� ������ ��ȯ�Ѵ�.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="�ѱ��� ������ Ư�̺� �˻� ����", response=DdiSnp2.class)
	})
	@ApiImplicitParam(name="vdrug", value="������ �޴� �๰", example="641")
	public Object gene(@RequestParam("vdrug") int vdrugNum) {
		List<DdiSnp2> ddiSnp2List = ddiService.getDdiSnp2BydrugNum(vdrugNum);
		Message message = new Message();
		message.setData(ddiSnp2List);
		return message;
	}
	
	@GetMapping("/substitution")
	@ApiOperation(value="DDI�� ��õ �๰ �� �˻�", notes="Prepetrator, victim�๰�� ��ġ�ϴ� �๰ ����Ʈ�� pk���� �˻��Ͽ� ��ȯ�Ѵ�. ��ü �๰ ����Ʈ�� ��ȯ�Ѵ�")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="��õ �๰ �� �˻� ����", response=PkResponse.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="pdrug", value="������ �ϴ� �๰", example="582"), @ApiImplicitParam(name="vdrug", value="������ �޴� �๰", example="864")})
	public Object substitution(@RequestParam("pdrug") int pdrugNum, @RequestParam("vdrug") int vdrugNum, @RequestHeader(value="locale") String locale) {
		List <Object> resultList = new ArrayList<>();
		List<String> pdrugAtc = ddiService.getDrugAtcByDrugNum(pdrugNum);
		List<String> vdrugAtc = ddiService.getDrugAtcByDrugNum(vdrugNum);
		
		List <Integer> pdrugNumListByAtc = new ArrayList<>();
		for(String val: pdrugAtc) {
			List <Integer> temp = ddiService.getDrugNumByAtc(val);
			pdrugNumListByAtc.addAll(temp);
		}
		Set<Integer> pdrugSet = new HashSet<Integer>(pdrugNumListByAtc); // �ߺ�����
		List<Integer> pdrugList =new ArrayList<Integer>(pdrugSet);
		
		List <Integer> vdrugNumListByAtc = new ArrayList<>();
		for(String val: vdrugAtc) {
			List <Integer> temp = ddiService.getDrugNumByAtc(val);
			vdrugNumListByAtc.addAll(temp);
		}
		Set<Integer> vdrugSet = new HashSet<Integer>(vdrugNumListByAtc); // �ߺ�����
		List<Integer> vdrugList =new ArrayList<Integer>(vdrugSet);
	
		for(int pdrugN: pdrugList) {
			for(int vdrugN: vdrugList) {
				HashMap <String, Integer> info = new HashMap<>();
				info.put("pdrugNum", pdrugN);
				info.put("vdrugNum", vdrugN);
				List<PkResponse> pkResponse = ddiService.getPkResponseByPdrugNumAndVdrugNum(info);
				if(pkResponse==null) {
					continue;
				}
				String prepetratorName = ddiService.getDrugNameByDrugNum(pdrugN);
				String victimName = ddiService.getDrugNameByDrugNum(vdrugN);
				
				for(int i=0; i<pkResponse.size(); i++) {
					PkResponse item = new PkResponse();
					item.setPrepetrator_name(prepetratorName);
					item.setVictim_name(victimName);
					
					float foldValue = pkResponse.get(i).getFold()-1;
					foldValue = (float) (Math.round(foldValue*100)/100.0);
					item.setValue(Float.toString(foldValue));
					String value = Float.toString(foldValue);
					item.setValue_p(Math.abs((int)(foldValue*100)));
					String value_p = Integer.toString(Math.abs((int)(foldValue*100)));
					String fold = Float.toString(foldValue+1);
					
					PkMessage pkMessage = new PkMessage(locale, prepetratorName, victimName, value_p, fold);
					int reference = pkResponse.get(i).getReference();
					item.setRef(Utils.findReferenceMessage(reference, pkMessage));
					item.setDetail(Utils.findDetailMessage(foldValue, pkMessage));
					resultList.add(item);
				}
			}
		}
		
		Message message = new Message();
		message.setData(resultList);
		return message;
	}

}