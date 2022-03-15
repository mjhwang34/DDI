package com.monorama.ddi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.ddi.model.Message;
import com.monorama.ddi.service.DfiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/dfi")
@Api(tags="DFI")
public class DfiController {
	private DfiService dfiService;
	
	@Autowired 
	public DfiController(DfiService dfiService) {
		this.dfiService = dfiService;
	}
	
	@GetMapping("/foods")
	@ApiOperation(value="DFI�� ���� �˻�", notes="DFI�� ������ �˻��Ѵ�. Ű���尡 �ԷµǸ� �ش� Ű���带 �����ϴ� ������ ��ȯ���ְ� ��ǰ�� ������ �ԷµǸ� �� ��ǰ�� ���õ� ������ ��ȯ���ش�.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="���� �˻� ����", response=Message.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="keyword", value="Ű����"), @ApiImplicitParam(name="drug_seq", value="�๰ ������")})
	public Object foods(@RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="drug_seq", required=false) @Nullable Integer drug_seq) {
		Message message = new Message();
		if(keyword!=null) {
			List<HashMap <String, Object>> foodList = dfiService.getDfiFoodListByKeyword(keyword);
			message.setData(foodList);
		}
		if(drug_seq!=null) {
			List<HashMap <String, Object>> foodList = dfiService.getDfiFoodListByDrugSeq((int)drug_seq);
			message.setData(foodList);
		}
		return message;
	}
	
	@GetMapping("/drugs")
	@ApiOperation(value="DFI�� ��ǰ �˻�", notes="DFI�� ��ǰ�� �˻��Ѵ�, Ű���尡 �ԷµǸ� �ش� Ű���带 �����ϴ� ��ǰ�� ��ȯ���ְ� ������ ������ �ԷµǸ� �� ���İ� ���õ� ��ǰ�� ��ȯ���ش�.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="�๰ �˻� ����", response=Message.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="keyword", value="Ű����"), @ApiImplicitParam(name="food_seq", value="���� ������")})
	public Object drugs(@RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="food_seq", required=false) @Nullable Integer food_seq) {
		Message message = new Message();
		if(keyword!=null) {
			List<HashMap<String, Object>> drugList = dfiService.getDfiDrugListByKeyword(keyword);
			message.setData(drugList);
		}
		if(food_seq!=null) {
			List <HashMap <String, Object>> drugList = dfiService.getDfiDrugListByFoodSeq((int)food_seq);
			message.setData(drugList);
		}
		return message;
	}
	
	
	@GetMapping("/search")
	@ApiOperation(value="DFI�� ���İ� ��ǰ�� ���� �˻�", notes="���İ� ��ǰ�� ������ ���� ���¿��� �ش� ���İ� ��ǰ�� ������ ������ ��ȯ�Ѵ�.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="����-��ǰ ���� �˻� ����", response=Message.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="food_seq", value="���� ������"), @ApiImplicitParam(name="drug_seq", value="�๰ ������")})
	public Object search(@RequestParam("food_seq") int food_seq, @RequestParam("drug_seq") int drug_seq) {
		HashMap <String, Integer> info = new HashMap<>();
		info.put("food_seq", food_seq);
		info.put("drug_seq", drug_seq);
		List<HashMap <String, Object>> dfiAndPaper = dfiService.getDfiAndPaperByFoodSeqAndDrugSeq(info);
		Message message = new Message();
		message.setData(dfiAndPaper);
		return message;
	}
}
