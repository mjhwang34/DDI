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
	@ApiOperation(value="DFI의 음식 검색", notes="DFI의 음식을 검색한다. 키워드가 입력되면 해당 키워드를 포함하는 음식을 반환해주고 약품의 정보가 입력되면 그 약품과 관련된 음식을 반환해준다.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="음식 검색 성공", response=Message.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="keyword", value="키워드"), @ApiImplicitParam(name="drug_seq", value="약물 시퀀스")})
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
	@ApiOperation(value="DFI의 약품 검색", notes="DFI의 약품을 검색한다, 키워드가 입력되면 해당 키워드를 포함하는 약품을 반환해주고 음식의 정보가 입력되면 그 음식과 관련된 약품을 반환해준다.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="약물 검색 성공", response=Message.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="keyword", value="키워드"), @ApiImplicitParam(name="food_seq", value="음식 시퀀스")})
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
	@ApiOperation(value="DFI의 음식과 약품의 관계 검색", notes="음식과 약품의 정보를 받은 상태에서 해당 음식과 약품의 관계의 정보를 반환한다.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="음식-약품 관계 검색 성공", response=Message.class)
	})
	@ApiImplicitParams(value= {@ApiImplicitParam(name="food_seq", value="음식 시퀀스"), @ApiImplicitParam(name="drug_seq", value="약물 시퀀스")})
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
