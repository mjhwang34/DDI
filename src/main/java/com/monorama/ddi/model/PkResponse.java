package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class PkResponse {
	
	@ApiModelProperty(value="시퀀스", example="2")
	private int seq;
	
	@ApiModelProperty(value="공격을 하는 약물 번호", example="303")
	private int prepetrator;
	
	@ApiModelProperty(value="영향을 받는 약물 번호", example="1017")
	private int victim;
	
	@ApiModelProperty(value="fold", example="1.00717")
	private float fold;
	
	@ApiModelProperty(value="레퍼런스", example="1")
	private int reference;
	
	@ApiModelProperty(value="버전", example="0")
	private int version;
	
	@ApiModelProperty(value="공격을 하는 약물의 이름", example="Ertapenem")
	private String prepetrator_name;
	
	@ApiModelProperty(value="영향을 받는 약물의 이름", example="Minocyline")
	private String victim_name;
	
	@ApiModelProperty(value="ref", example="인공지능 예측결과 입니다.")
	private String ref;
	
	@ApiModelProperty(value="value", example="0.01")
	private String value;
	
	@ApiModelProperty(value="value의 퍼센티지", example="1")
	private int value_p;
	
	@ApiModelProperty(value="상세 정보", example="Ertapenem은 Minocycline의 AUC를 1%만큼 증가시킴.")
	private String detail;
}

